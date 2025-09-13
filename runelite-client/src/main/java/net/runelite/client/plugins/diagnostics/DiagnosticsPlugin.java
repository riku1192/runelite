package net.runelite.client.plugins.diagnostics;

import com.google.inject.Provides;
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
    private WorldPoint holdPos = new WorldPoint(0, 0, 0);

    @Inject
    private Client client;
    
    @Inject
	private OverlayManager overlayManager;
    
    @Inject
	private DiagnosticsOverlay overlay;
    
    // Not Injected Variables used in this plugin
    private WorldView worldView;
    private Player player;

    @Provides
    private DiagnosticsConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(DiagnosticsConfig.class);
    }

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
        GameObject obj = event.getGameObject();
        int id = obj.getId();

        // Example: normal tree IDs are around 1276, 1278, etc.
        if (id == 1276 || id == 1278)
        {
            System.out.println("Tree spawned at: " + obj.getWorldLocation());
        }
    }

    // Called when a game object despawns (tree chopped, rock mined, etc.)
    @Subscribe
    public void onGameObjectDespawned(GameObjectDespawned event)
    {
        GameObject obj = event.getGameObject();
        System.out.println("Object despawned: " + obj.getId());
    }

    // Called when a player’s animation changes
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
        }
    }

    // Example: Check player’s position every game tick
    @Subscribe
    public void onGameTick(GameTick tick)
    {
        player = client.getLocalPlayer();
        worldView = client.getTopLevelWorldView();

        if (player != null)
        {
            WorldPoint pos = player.getWorldLocation();
            if (pos.getX() != holdPos.getX() || pos.getY() != holdPos.getY() || pos.getPlane() != holdPos.getPlane()) {
                holdPos = pos;
                System.out.println("Player position: " + pos); 
            }
        }

        for (NPC npc : worldView.npcs())
        {
            if (npc == null) continue;

            String name = npc.getName();
            Integer id = npc.getId();
            WorldPoint pos = npc.getWorldLocation();

            if (name != null && pos != null)
            {
                System.out.println(name + " at " + pos + " id " + id);
            }
        }
    }

    
	@Subscribe
	public void onNpcSpawned(NpcSpawned npcSpawned)
	{
		NPC npc = npcSpawned.getNpc();
        System.out.println("NPC spawned " + npc.getName() + " at " + npc.getLocalLocation());
	}

	@Subscribe
	public void onNpcChanged(NpcChanged npcCompositionChanged)
	{
		NPC npc = npcCompositionChanged.getNpc();
        System.out.println("NPC changed " + npc.getName() + " at " + npc.getLocalLocation());
	}
}
