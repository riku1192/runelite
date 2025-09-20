package net.runelite.client.plugins.eventforwarder.DTO;

import java.util.List;

import net.runelite.api.Prayer;

public class PlayerStateDTO extends RuneliteEvent {
    int playerX;
    int playerY;
    //plane
    int playerZ;
    //yaw: north = 0; west = 512; south = 1024; east = 1536
    int cameraYaw;
    List<InventoryItem> inventoryItems;
    int[] playerEquipmentIds;
    boolean isRunning;
    int runEnergy;
    boolean isPrayerActive;
    Prayer[] activePrayers;
    
    public PlayerStateDTO() {
        super.setType("PlayerState");
    }

    public int getPlayerX() {
        return playerX;
    }

    public void setPlayerX(int playerX) {
        this.playerX = playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public void setPlayerY(int playerY) {
        this.playerY = playerY;
    }

    public int getPlayerZ() {
        return playerZ;
    }

    public void setPlayerZ(int playerZ) {
        this.playerZ = playerZ;
    }

    public int getCameraYaw() {
        return cameraYaw;
    }

    public void setCameraYaw(int cameraYaw) {
        this.cameraYaw = cameraYaw;
    }

    public List<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public void setInventoryItems(List<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    public int[] getPlayerEquipmentIds() {
        return playerEquipmentIds;
    }

    public void addInventoryItem(InventoryItem inventoryItem){
        this.inventoryItems.add(inventoryItem);
    }

    public void setPlayerEquipmentIds(int[] playerEquipmentIds) {
        this.playerEquipmentIds = playerEquipmentIds;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public int getRunEnergy() {
        return runEnergy;
    }

    public void setRunEnergy(int runEnergy) {
        this.runEnergy = runEnergy;
    }

    public boolean isPrayerActive() {
        return isPrayerActive;
    }

    public void setPrayerActive(boolean isPrayerActive) {
        this.isPrayerActive = isPrayerActive;
    }

    public Prayer[] getActivePrayers() {
        return activePrayers;
    }

    public void setActivePrayers(Prayer[] activePrayers) {
        this.activePrayers = activePrayers;
    }
    
}
