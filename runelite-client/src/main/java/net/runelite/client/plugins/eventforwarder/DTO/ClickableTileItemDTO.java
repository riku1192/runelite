package net.runelite.client.plugins.eventforwarder.DTO;

import net.runelite.api.TileItem;

public class ClickableTileItemDTO extends ClickableDTO {
    
    public ClickableTileItemDTO(TileItem tileItem, int x, int y){
        //Set super fields
        setType("Clickable");
        setClickableType("Clickable tile item");
        setClickableId(tileItem.getId());
        setClickableX(x);
        setClickableY(y);

        //Set this DTO fields
    }
}
