package net.runelite.client.plugins.eventforwarder;

import net.runelite.api.events.GameObjectSpawned;

public class GameObjectSpawnedDTO
{
    public String eventType = "GameObjectSpawned";

    // From Tile
    public int tileX;
    public int tileY;
    public int tilePlane;

    // From GameObject
    public int id;
    public String name;
    public int worldX;
    public int worldY;
    public int plane;
    public int orientation;
    public int sceneX;
    public int sceneY;

    public GameObjectSpawnedDTO(GameObjectSpawned event)
    {
        this.tileX = event.getTile().getWorldLocation().getX();
        this.tileY = event.getTile().getWorldLocation().getY();
        this.tilePlane = event.getTile().getWorldLocation().getPlane();

        this.id = event.getGameObject().getId();
        this.worldX = event.getGameObject().getWorldLocation().getX();
        this.worldY = event.getGameObject().getWorldLocation().getY();
        this.plane = event.getGameObject().getPlane();
        this.orientation = event.getGameObject().getOrientation();
        this.sceneX = event.getGameObject().getSceneMinLocation().getX();
        this.sceneY = event.getGameObject().getSceneMinLocation().getY();
    }
}
