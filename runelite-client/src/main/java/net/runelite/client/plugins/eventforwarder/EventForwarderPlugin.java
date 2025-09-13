package net.runelite.client.plugins.eventforwarder;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.eventforwarder.DTO.AnimationChangedDTO;
import net.runelite.client.plugins.eventforwarder.DTO.GameObjectSpawnedDTO;
import net.runelite.client.plugins.eventforwarder.DTO.RuneliteEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
        name = "Event Forwarder"
	    ,enabledByDefault = false
)
public class EventForwarderPlugin extends Plugin
{
    private ServerSocket serverSocket;
    private final Gson gson = new Gson();
    private final List<EventForwarderHandler> handlers = new CopyOnWriteArrayList<>();
    private volatile boolean running = true;
    
    @Inject
    private Client client;

    @Override
    protected void startUp() throws Exception
    {
        int port = 12345;
        serverSocket = new ServerSocket(port);

        Thread acceptThread = new Thread(() -> {
            log.info("Server is listening on port {}...", port);
            while (running) {
                try {
                    Socket client = serverSocket.accept();
                    EventForwarderHandler handler = new EventForwarderHandler(client);
                    handlers.add(handler);
                    handler.start();
                    log.info("New client connection accepted.");
                } catch (IOException e) {
                    if (running) {
                        log.error("Error accepting client connection", e);
                    }
                }
            }
        });

        acceptThread.setDaemon(true);
        acceptThread.start();
    }

    @Override
    protected void shutDown() throws Exception
    {
        running = false;
        for (EventForwarderHandler h : handlers) {
            h.close();
        }
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
        handlers.clear();
    }

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned event)
    {
        RuneliteEvent dto = new GameObjectSpawnedDTO(event);
        String json = gson.toJson(dto);
        for (EventForwarderHandler handler : handlers) {
            handler.send(json);
        }
    }
    
    @Subscribe
    public void onAnimationChanged(AnimationChanged event)
    {
        if (event.getActor() == client.getLocalPlayer())
        {
            int anim = client.getLocalPlayer().getAnimation();
            System.out.println("Player animation changed: " + anim);

            // -1 means idle
            if (anim == -1)
            {
                System.out.println("Player is idle.");
            }
            else
            {
                System.out.println("Player is doing animation ID: " + anim);
            }
            RuneliteEvent dto = new AnimationChangedDTO(event);
            String json = gson.toJson(dto);
            for (EventForwarderHandler handler : handlers) {
                System.out.println("Sending some JSON " + json);
                handler.send(json);
            }
        }
    }

    private static class EventForwarderHandler extends Thread
    {
        private final Socket clientSocket;
        private PrintWriter out;

        public EventForwarderHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                // If you don’t care about client->server messages, you can skip reading input
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void send(String message) {
            if (out != null) {
                out.println(message);
            }
        }

        public void close() {
            try {
                if (out != null) out.close();
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                }
            } catch (IOException ignored) {}
        }
    }
}
