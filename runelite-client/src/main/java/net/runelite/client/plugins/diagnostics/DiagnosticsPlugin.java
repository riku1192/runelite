package net.runelite.client.plugins.diagnostics;

import com.google.inject.Provides;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.inject.Inject;

import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.config.ConfigManager;

@PluginDescriptor(
    name = "Diagnostics"
    ,enabledByDefault = false
)
public class DiagnosticsPlugin extends Plugin
{

	static final String CONFIG_GROUP_KEY = "diagnosticscontrol";
    private static final int MAX_DISTANCE = 2400;
    private WorldPoint holdPos = new WorldPoint(0, 0, 0);

    @Inject
    private Client client;
    
    @Inject
	private OverlayManager overlayManager;
    
    @Inject
	private DiagnosticsOverlay overlay;

	@Inject
	private DiagnosticsConfig config;
    
    @Provides
    private DiagnosticsConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(DiagnosticsConfig.class);
    }
    
    // Not Injected Variables used in this plugin
    private WorldView worldView;
    private Player player;

	@Override
	protected void startUp()
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}

    // Called when a game object (like a tree, rock, or fishing spot) spawns
    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned event)
    {
        if (config.toggleOnGameObjectSpawned() == true) {
            GameObject obj = event.getGameObject();
            int id = obj.getId();
            
            // Example: normal tree IDs are around 1276, 1278, etc.
            if (id == 1276 || id == 1278)
            {
                System.out.println("Tree spawned at: " + obj.getWorldLocation());
            }
        }
    }

    // Called when a game object despawns (tree chopped, rock mined, etc.)
    @Subscribe
    public void onGameObjectDespawned(GameObjectDespawned event)
    {
        if (config.toggleOnGameObjectDespawned() == true) {
            GameObject obj = event.getGameObject();
            System.out.println("Object despawned: " + obj.getId());
        }
    }

    // Called when a player’s animation changes
    @Subscribe
    public void onAnimationChanged(AnimationChanged event)
    {
        if (config.toggleOnAnimationChanged() == true) {
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
            }
        }
    }

    @Subscribe
    public void onGameTick(GameTick tick)
    {
        //Track mouse positon on canvas
        System.out.println("Mouse Canvas position: X:" + client.getMouseCanvasPosition().getX() + " Y:" + client.getMouseCanvasPosition().getY());
        
        //Main display logic
        if (config.toggleOnGameTick() == true) {
            
            System.out.println("Camera X: " + client.getCameraX() + " Camera Y: " + client.getCameraY()  + " Camera Z: " + client.getCameraZ()  + " Camera Yaw: " + client.getCameraYaw());
    
            //Track absolute mouse position when hovering canvas
            Point mouseCanvas = client.getMouseCanvasPosition();
    
            java.awt.Point mouseScreen = toScreenCoords(client, 
                new java.awt.Point(mouseCanvas.getX(), mouseCanvas.getY()));
    
            if (mouseScreen != null)
            {
                System.out.println("Mouse on screen: " + mouseScreen);
            }
            
            player = client.getLocalPlayer();
            worldView = client.getTopLevelWorldView();
            Actor actor = player;
            

            //Display rough center of my player model
            Point computedPixelOfPlayer = Perspective.localToCanvas(client, player.getLocalLocation(),player.getWorldLocation().getPlane(),actor.getLogicalHeight() / 2);
            System.out.println("Player pixel position from localToCanvas method " + computedPixelOfPlayer.getX() + " " + computedPixelOfPlayer.getY());
    
            //Display rough center of Gielinor Guide
            NPC npcTarget = null; 
            for (NPC npc : worldView.npcs())
            {
                if (npc != null && npc.getName() != null && npc.getName().equals("Gielinor Guide"))
                {
                    npcTarget = npc;
                    break;
                }
            }
            if (npcTarget != null)
            {
                // LocalPoint localNpc = target.getLocalLocation();
                Point computedPixelOfLocalNpc = Perspective.localToCanvas(client, npcTarget.getLocalLocation(), npcTarget.getWorldLocation().getPlane(), npcTarget.getLogicalHeight() / 2);
                if (computedPixelOfLocalNpc.getX() <= client.getCanvasWidth() && computedPixelOfLocalNpc.getY() <= client.getCanvasHeight()) {
                    System.out.println("NPC pixel position: X=" + computedPixelOfLocalNpc.getX() + " Y=" + computedPixelOfLocalNpc.getY());
                } else {
                    System.err.println("NPC is off screen but still detected");
                }
            }

            //Displays coords of every game object and ground item
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
                                System.out.println("Game Object found. ID: " + gameObject.getId() + " X: " + gameObject.getX() + " Y: " + gameObject.getY());
                            }
                        }
                    }

                    ItemLayer itemLayer = tile.getItemLayer();
                    if (itemLayer != null)
                    {
                        if (player.getLocalLocation().distanceTo(itemLayer.getLocalLocation()) <= MAX_DISTANCE)
                        {
                            Node current = itemLayer.getTop();
                            while (current instanceof TileItem)
                            {
                                TileItem item = (TileItem) current;
                                System.out.println("Ground Item found. ID: " + item.getId() + " X: " + x + " Y: " + y);
                                current = current.getNext();
                            }
                        }
                    }
                }
            }
    
            //Displays the player location whenever it changes
            if (player != null)
            {
                WorldPoint pos = player.getWorldLocation();
                if (pos.getX() != holdPos.getX() || pos.getY() != holdPos.getY() || pos.getPlane() != holdPos.getPlane()) {
                    holdPos = pos;
                    System.out.println("Player position: " + pos); 
                }
            }
    
            // for (NPC npc : worldView.npcs())
            // {
            //     if (npc == null) continue;
    
            //     String name = npc.getName();
            //     Integer id = npc.getId();
            //     WorldPoint pos = npc.getWorldLocation();
    
            //     if (name != null && pos != null)
            //     {
            //         System.out.println(name + " at " + pos + " id " + id);
            //     }
            // }
        }
    }

	@Subscribe
	public void onNpcSpawned(NpcSpawned npcSpawned)
	{
        if (config.toggleOnNpcSpawned() == true) {
            NPC npc = npcSpawned.getNpc();
            System.out.println("NPC spawned " + npc.getName() + " at " + npc.getLocalLocation());
        }
	}

	@Subscribe
	public void onNpcChanged(NpcChanged npcCompositionChanged)
	{
        if (config.toggleOnNpcChanged() == true) {
            NPC npc = npcCompositionChanged.getNpc();
            System.out.println("NPC changed " + npc.getName() + " at " + npc.getLocalLocation());
        }
	}

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event) {
        if (config.toggleOnMenuOptionClicked() == true) {
            MenuAction menuAction = event.getMenuAction();
            
            // Now you can work with the MenuAction object
            // For example, to get the ID of the action:
            int actionId = menuAction.getId();
            String clickTarget = event.getMenuTarget();
            System.out.println("Detected a menu option clicked: " + actionId);
            System.out.println("Targeting: " + clickTarget);
            // Or to check for a specific action type:
            // if (menuAction == MenuAction.ITEM_USE_ON_GAME_OBJECT) {
            //     // Handle item use on game object
            // }
        }
    }

    /**
     * Converts a canvas-local point (e.g. from client.getMouseCanvasPosition())
     * into absolute screen coordinates.
     *
     * @param client RuneLite client
     * @param canvasPoint Point relative to the canvas (0,0 = top-left of game area)
     * @return Point in absolute screen coordinates, or null if canvas is not visible
     */
    public static java.awt.Point toScreenCoords(Client client, java.awt.Point canvasPoint)
    {
        if (client == null || canvasPoint == null)
        {
            return null;
        }

        Canvas canvas = client.getCanvas();

        try
        {
            java.awt.Point canvasOnScreen = canvas.getLocationOnScreen();
            return new java.awt.Point(
                (int) canvasOnScreen.getX() + (int) canvasPoint.getX(),
                (int) canvasOnScreen.getY() + (int) canvasPoint.getY()
            );
        }
        catch (Exception e)
        {
            // Happens if RuneLite window is minimized or canvas not displayable
            return null;
        }
    }

}
