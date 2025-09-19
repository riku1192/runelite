package net.runelite.client.plugins.eventforwarder.DTO;

import java.util.Objects;

import net.runelite.api.GameObject;

public class ClickableGameObjectDTO extends ClickableDTO {
    
    public ClickableGameObjectDTO(GameObject gameObject, int x, int y, int tileX, int tileY, int tileZ){
        //Set super fields
        setType("Clickable");
        setClickableType("Clickable game object");
        setClickableId(gameObject.getId());
        setClickableX(x);
        setClickableY(y);
        setTileX(tileX);
        setTileY(tileY);
        setTileZ(tileZ);

        //Set this DTO fields
    }

    public String toString(){
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClickableDTO)) return false;
        if (!super.equals(o)) return false;
        ClickableDTO that = (ClickableDTO) o;
        return Objects.equals(clickableType, that.clickableType) &&
               Objects.equals(clickableId, that.clickableId) &&
               Objects.equals(clickableX, that.clickableX) &&
               Objects.equals(clickableY, that.clickableY);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), clickableType, clickableId, clickableX, clickableY);
    }
}
