package net.runelite.client.plugins.eventforwarder.DTO;

import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.PlayerComposition;
import net.runelite.api.Perspective;
import net.runelite.api.Point;

public class ClickablePlayerDTO extends ClickableDTO {
    String playerName;
    Integer playerLevel;
    Integer playerGender;
    int[] playerEquipmentIds;
    
    public ClickablePlayerDTO(Player player, Client client){

        //Set super fields
        setType("Clickable");
        setClickableType("Clickable player");
        setClickableId(player.getId());
        Point playerCoords = Perspective.localToCanvas(client, player.getLocalLocation(), player.getWorldLocation().getPlane(), player.getLogicalHeight() / 2);
        setClickableX(playerCoords.getX());
        setClickableY(playerCoords.getY());

        //Set this DTO fields
        playerName = player.getName();
        playerLevel = player.getCombatLevel();
        PlayerComposition playerComposition = player.getPlayerComposition();
        playerGender = playerComposition.getGender();
        playerEquipmentIds = playerComposition.getEquipmentIds();
    }
}
