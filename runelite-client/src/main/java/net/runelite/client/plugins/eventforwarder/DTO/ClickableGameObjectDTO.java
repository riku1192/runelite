package net.runelite.client.plugins.eventforwarder.DTO;

import net.runelite.api.GameObject;

public class ClickableGameObjectDTO extends ClickableDTO {
    
    public ClickableGameObjectDTO(GameObject gameObject, int x, int y){
        //Set super fields
        setType("Clickable");
        setClickableType("Clickable game object");
        setClickableId(gameObject.getId());
        setClickableX(x);
        setClickableY(y);

        //Set this DTO fields
    }
}
