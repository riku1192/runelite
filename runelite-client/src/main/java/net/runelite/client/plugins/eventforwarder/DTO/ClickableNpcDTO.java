package net.runelite.client.plugins.eventforwarder.DTO;

import net.runelite.api.NPC;

public class ClickableNpcDTO extends ClickableDTO {
    String npcName;
    
    public ClickableNpcDTO(NPC npc, int x, int y){
        //Set super fields
        setType("Clickable");
        setClickableType("Clickable NPC");
        setClickableId(npc.getId());
        setClickableX(x);
        setClickableY(y);
        setTileX(npc.getWorldLocation().getX());
        setTileY(npc.getWorldLocation().getY());
        setTileZ(npc.getWorldLocation().getPlane());


        //Set this DTO fields
        npcName = npc.getName();
    }
}
