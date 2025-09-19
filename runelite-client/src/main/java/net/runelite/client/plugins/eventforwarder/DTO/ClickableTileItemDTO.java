package net.runelite.client.plugins.eventforwarder.DTO;

import net.runelite.api.TileItem;

public class ClickableTileItemDTO extends ClickableDTO {
    
    public ClickableTileItemDTO(TileItem tileItem, int x, int y, int tileX, int tileY, int tileZ){
        //Set super fields
        setType("Clickable");
        setClickableType("Clickable tile item");
        setClickableId(tileItem.getId());
        setClickableX(x);
        setClickableY(y);
        setTileX(tileX);
        setTileY(tileY);
        setTileZ(tileZ);

        //Set this DTO fields
    }
}
