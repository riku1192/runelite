package net.runelite.client.plugins.eventforwarder.DTO;

import net.runelite.api.Player;
import net.runelite.api.PlayerComposition;

public class ClickablePlayerDTO extends ClickableDTO {
    String playerName;
    Integer playerLevel;
    Integer playerGender;
    int[] playerEquipmentIds;
    
    public ClickablePlayerDTO(Player player, int x, int y){

        //Set super fields
        setType("Clickable");
        setClickableType("Clickable player");
        setClickableId(player.getId());
        setClickableX(x);
        setClickableY(y);

        //Set this DTO fields
        playerName = player.getName();
        playerLevel = player.getCombatLevel();
        PlayerComposition playerComposition = player.getPlayerComposition();
        playerGender = playerComposition.getGender();
        playerEquipmentIds = playerComposition.getEquipmentIds();
    }
}
