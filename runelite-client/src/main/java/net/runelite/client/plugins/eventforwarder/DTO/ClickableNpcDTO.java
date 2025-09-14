package net.runelite.client.plugins.eventforwarder.DTO;

import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Point;

public class ClickableNpcDTO extends ClickableDTO {
    String npcName;
    
    public ClickableNpcDTO(NPC npc, Client client){
        //Set super fields
        setType("Clickable");
        setClickableType("Clickable NPC");
        setClickableId(npc.getId());
        Point npcCoords = Perspective.localToCanvas(client, npc.getLocalLocation(), npc.getWorldLocation().getPlane(), npc.getLogicalHeight() / 2);
        setClickableX(npcCoords.getX());
        setClickableY(npcCoords.getY());

        //Set this DTO fields
        npcName = npc.getName();
    }
}
