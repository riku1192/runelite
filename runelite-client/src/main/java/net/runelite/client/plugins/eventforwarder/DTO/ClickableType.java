package net.runelite.client.plugins.eventforwarder.DTO;

public enum ClickableType {

    TILE_ITEM("Clickable tile item"),
    PLAYER("Clickable player"),
    NPC("Clickable NPC"),
    GAME_OBJECT("Clickable game object");
    
    String type;

    ClickableType(String type){
        this.type = type;
    }
}
