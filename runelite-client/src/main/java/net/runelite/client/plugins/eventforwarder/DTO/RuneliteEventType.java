package net.runelite.client.plugins.eventforwarder.DTO;

public enum RuneliteEventType {

    ANIMATION_CHANGED("AnimationChanged"),
    GAME_OBJECT_SPAWNED("GameObjectSpawned"),
    CLIENT_REQUEST("clientRequest"),
    CLICKABLE("Clickable");
    
    String type;

    RuneliteEventType(String type){
        this.type = type;
    }
}
