package net.runelite.client.plugins.eventforwarder;

import com.google.gson.Gson;
import com.google.inject.Provides;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.GameObject;
import net.runelite.api.ItemLayer;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.api.WorldView;
import net.runelite.api.Node;
import net.runelite.api.Perspective;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.WidgetClosed;
import net.runelite.api.events.WidgetLoaded;
import java.awt.geom.Rectangle2D;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.diagnostics.DiagnosticsConfig;
import net.runelite.client.plugins.eventforwarder.DTO.AnimationChangedDTO;
import net.runelite.client.plugins.eventforwarder.DTO.ClickableDTO;
import net.runelite.client.plugins.eventforwarder.DTO.ClickableGameObjectDTO;
import net.runelite.client.plugins.eventforwarder.DTO.ClickableNpcDTO;
import net.runelite.client.plugins.eventforwarder.DTO.ClickablePlayerDTO;
import net.runelite.client.plugins.eventforwarder.DTO.ClickableTileItemDTO;
import net.runelite.client.plugins.eventforwarder.DTO.ClientRequestDTO;
import net.runelite.client.plugins.eventforwarder.DTO.GameObjectSpawnedDTO;
import net.runelite.client.plugins.eventforwarder.DTO.RuneliteEvent;

import java.awt.Rectangle;
import java.awt.Shape;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private static final Gson gson = new Gson();
    private final List<EventForwarderHandler> handlers = new CopyOnWriteArrayList<>();
    private static final int MAX_DISTANCE = 2400;
    private static final Map<String, RuneliteEvent> knownEntities = new HashMap<>();
	static final String CONFIG_GROUP_KEY = "eventforwarderconfig";
    private volatile boolean running = true;
    private static volatile ClientRequestDTO clientRequest;
    
    @Inject
    private Client client;

    @Inject
	private EventForwarderConfig config;

    @Inject
    private static ConfigManager configManager;
    
    @Provides
    private EventForwarderConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(EventForwarderConfig.class);
    }

    @Override
    protected void startUp() throws Exception
    {
        clientRequest = new ClientRequestDTO();

        running = true;
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

    // @Subscribe
    // public void onGameObjectSpawned(GameObjectSpawned event)
    // {
    //     RuneliteEvent dto = new GameObjectSpawnedDTO(event);
    //     String json = gson.toJson(dto);
    //     for (EventForwarderHandler handler : handlers) {
    //         handler.send(json);
    //     }
    // }
    
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

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded event)
    {
        if (event.getGroupId() == WidgetID.DIALOG_NPC_GROUP_ID
            || event.getGroupId() == WidgetID.DIALOG_PLAYER_GROUP_ID)
        {
            // log.info("Started talking to NPC or player.");
            System.out.println("Started talking to NPC or player.");
            if (event.getGroupId() == WidgetID.DIALOG_NPC_GROUP_ID)
            {
                // NPC dialogue widget
                Widget npcTextWidget = client.getWidget(WidgetInfo.DIALOG_NPC_TEXT);
                Widget npcNameWidget = client.getWidget(WidgetInfo.DIALOG_NPC_NAME);

                if (npcTextWidget != null && npcTextWidget.getText() != null)
                {
                    System.out.println("NPC says: {}" + npcTextWidget.getText());
                }

                if (npcNameWidget != null && npcNameWidget.getText() != null)
                {
                    System.out.println("Talking to NPC: {}" + npcNameWidget.getText());
                }
            }
            else if (event.getGroupId() == WidgetID.DIALOG_PLAYER_GROUP_ID)
            {
                // Player dialogue (your own lines)
                Widget playerTextWidget = client.getWidget(WidgetInfo.DIALOG_PLAYER_TEXT);

                if (playerTextWidget != null && playerTextWidget.getText() != null)
                {
                    System.out.println("Player says: {}" + playerTextWidget.getText());
                }
            }
        }
    }

    @Subscribe
    public void onWidgetClosed(WidgetClosed event)
    {
        if (event.getGroupId() == WidgetID.DIALOG_NPC_GROUP_ID
            || event.getGroupId() == WidgetID.DIALOG_PLAYER_GROUP_ID)
        {
            // log.info("Stopped talking.");
            System.out.println("Stopped talking");
        }
    }

    @Subscribe
    public void onGameTick(GameTick tick){
        // System.out.println("Game tick...");
        if (config.toggleOnGameTick() == true || clientRequest.isSendByTick() == true){
            WorldView worldView = client.getTopLevelWorldView();
            DiffPayloadDTO diffPayloadDTO = createPayloadUpdateKnownEntities(worldView);
            if (diffPayloadDTO.added.size() > 0 || diffPayloadDTO.removed.size() > 0 || diffPayloadDTO.updated.size() > 0) {
                System.out.println(gson.toJson(diffPayloadDTO));
                String json = gson.toJson(diffPayloadDTO);
                for (EventForwarderHandler handler : handlers) {
                    handler.send(json);
                }
            }
        }
    }

    //End subscritions

    //Start Server Handler
    private static class EventForwarderHandler extends Thread
    {
        private final Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public EventForwarderHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String incoming;
                while ((incoming = in.readLine()) != null) {
                    this.handleIncomingEvent(incoming);
                }
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

        //Handles incoming events and updates local variables
        private void handleIncomingEvent(String incoming){
            RuneliteEvent tempEvent = gson.fromJson(incoming, RuneliteEvent.class);
            if (tempEvent.getType().equals("clientRequest")){
                System.out.println("Received a client request: " + incoming);
                clientRequest = gson.fromJson(incoming, ClientRequestDTO.class);
                System.out.println("ClientRequest: " + clientRequest);
                knownEntities.clear();
            }
        }
    }
    //End Server Handler

    //Start helper methods


    //Create payload of clickable pixel for all requested entities
    private DiffPayloadDTO createPayloadUpdateKnownEntities(WorldView worldView){

        //Declare map of what is currently visible
        Map<String, RuneliteEvent> current = new HashMap<>();

        //Handle players
        for (Player player : worldView.players())
        {
            if (player != null)
            {
                Point computedPixel = Perspective.localToCanvas(client, player.getLocalLocation(), player.getWorldLocation().getPlane(), player.getLogicalHeight() / 2);
                String key;
                RuneliteEvent dto;
                if (isOnScreen(computedPixel.getX(), computedPixel.getY()) == true) {
                    key = "Player:" + player.getName() + ":x=" + computedPixel.getX() + ":y=" + computedPixel.getY();
                    dto = new ClickablePlayerDTO(player, computedPixel.getX(), computedPixel.getY());
                } else {
                    key = "Player:" + player.getName() + ":x=-1" + ":y=-1";
                    dto = new ClickablePlayerDTO(player, -1, -1);
                }
                current.put(key, dto);
            }
        }

        //Handle NPCs
        NPC npcTarget = null; 
        for (NPC npc : worldView.npcs())
        {
            if (npc != null && clientRequest.getTargets().contains(npc.getId()) == true)
            {
                npcTarget = npc;
                break;
            }
        }
        if (npcTarget != null)
        {
            Point computedPixel = Perspective.localToCanvas(client, npcTarget.getLocalLocation(), npcTarget.getWorldLocation().getPlane(), npcTarget.getLogicalHeight() / 2);
            String key;
            RuneliteEvent dto;
            if (isOnScreen(computedPixel.getX(), computedPixel.getY()) == true) {
                key = "NPC:" + npcTarget.getId() + ":x=" + computedPixel.getX() + ":y=" + computedPixel.getY();
                dto = new ClickableNpcDTO(npcTarget, computedPixel.getX(), computedPixel.getY());
            } else {
                key = "NPC:" + npcTarget.getId() + ":x=-1" + ":y=-1";
                dto = new ClickableNpcDTO(npcTarget, -1, -1);
            }
            current.put(key, dto);
        }

        //Handle GameObjects and TileItems (stuff that has to be checked tile by tile)
        Scene scene = worldView.getScene();
        Tile[][][] tiles = scene.getTiles();

        int z = worldView.getPlane();
        
        for (int x = 0; x < Constants.SCENE_SIZE; ++x)
        {
            for (int y = 0; y < Constants.SCENE_SIZE; ++y)
            {
                Tile tile = tiles[z][x][y];
                
                if (tile == null)
                {
                    continue;
                }
                
                Player player = client.getLocalPlayer();
                if (player == null)
                {
                    continue;
                }
                
                GameObject[] gameObjects = tile.getGameObjects();
                if (gameObjects != null)
                {
                    for (GameObject gameObject : gameObjects)
                    {   
                        if (gameObject != null && gameObject.getSceneMinLocation().equals(tile.getSceneLocation()))
                        {
                            if (clientRequest != null && clientRequest.getTargets().contains(gameObject.getId()) == true) {
                                // System.out.println("Game Object found. ID: " + gameObject.getId() + " X: " + gameObject.getX() + " Y: " + gameObject.getY());
                                // String key = "GameObject:" + gameObject.getId() + ":x=" +x + ":y=" + y;
                                // Point computedPixel = Perspective.localToCanvas(client, gameObject.getLocalLocation(), gameObject.getWorldLocation().getPlane(), 0);
                                Shape clickbox = gameObject.getClickbox();
                                if (clickbox != null) {
                                    Rectangle bounds = clickbox.getBounds();
                                    int centerX = (int) bounds.getCenterX();
                                    int centerY = (int) bounds.getCenterY();
                                    String key;
                                    RuneliteEvent dto;
                                    if (isOnScreen(centerX, centerY) == true){
                                        key = "GameObject:" + gameObject.getId() + ":x=" + centerX + ":y=" + centerY;
                                        dto = new ClickableGameObjectDTO(gameObject, centerX, centerY);
                                    } else {
                                        key = "GameObject:" + gameObject.getId() + ":x=-1" + ":y=-1";
                                        dto = new ClickableGameObjectDTO(gameObject, -1, -1);
                                    }
                                    current.put(key, dto);
                                }
                            }
                        }
                    }
                }

                ItemLayer itemLayer = tile.getItemLayer();
                if (itemLayer != null)
                {
                    if (player.getLocalLocation().distanceTo(itemLayer.getLocalLocation()) <= MAX_DISTANCE)
                    {
                        Node currentTopLayer = itemLayer.getTop();
                        while (currentTopLayer instanceof TileItem)
                        {
                            TileItem item = (TileItem) currentTopLayer;
                            if (clientRequest.getTargets().contains(item.getId()) == true) {
                                // System.out.println("Ground Item found. ID: " + item.getId() + " X: " + x + " Y: " + y);
                                // String key = "TileItem:" + item.getId() + ":x=" + x + ":y=" + y;
                                Point computedPixel = Perspective.localToCanvas(client, tile.getLocalLocation(), worldView.getPlane(), 0);
                                String key;
                                RuneliteEvent dto;
                                if (isOnScreen(computedPixel.getX(), computedPixel.getY()) == true){
                                    key = "TileItem:" + item.getId() + ":x=" + computedPixel.getX() + ":y=" + computedPixel.getY();
                                    dto = new ClickableTileItemDTO(item, computedPixel.getX(), computedPixel.getY());
                                } else {
                                    key = "TileItem:" + item.getId() + ":x=" + computedPixel.getX() + ":y=" + computedPixel.getY();
                                    dto = new ClickableTileItemDTO(item, computedPixel.getX(), computedPixel.getY());
                                }
                                current.put(key, dto);
                            }
                            currentTopLayer = currentTopLayer.getNext();
                        }
                    }
                }
            }
        }

        // Compare what currently can be found against our last scan and create a new map + payload object
        List<RuneliteEvent> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        List<RuneliteEvent> updated = new ArrayList<>();

        for (Map.Entry<String, RuneliteEvent> entry : current.entrySet())
        {
            String key = entry.getKey();
            RuneliteEvent dto = entry.getValue();

            if (!knownEntities.containsKey(key))
            {
                added.add(dto);
            }
            else if (!dto.equals(knownEntities.get(key)))
            {
                // ClickableDTO tempDTO = (ClickableDTO) dto; 
                // if (tempDTO.getClickableType().equalsIgnoreCase("Clickable game object")){
                //     tempDTO = (ClickableGameObjectDTO) dto;
                //     System.out.println("Mismatch on key: " + key);
                //     System.out.println("New DTO: " + tempDTO.toString());
                //     tempDTO = (ClickableGameObjectDTO) knownEntities.get(key);
                //     System.out.println("Old DTO: " + tempDTO.toString());
                // }
                updated.add(dto);
            }
        }

        for (String oldKey : knownEntities.keySet())
        {
            if (!current.containsKey(oldKey))
            {
                removed.add(oldKey);
            }
        }
        
        
        if (added.size() > 0 || removed.size() > 0 || updated.size() > 0) {
            System.out.println("Something's going in the payload: " + added + removed + updated);
        }
        // Create Payload
        DiffPayloadDTO payload = new DiffPayloadDTO(added, removed, updated);

        // Update state
        knownEntities.clear();
        knownEntities.putAll(current);

        //Return payload
        return payload;
    }

    private boolean isOnScreen(int x, int y) {
        if (x <= client.getCanvasWidth() && y <= client.getCanvasHeight()){
            return true;
        }

        return false;
    }
        //End helper methods
}
