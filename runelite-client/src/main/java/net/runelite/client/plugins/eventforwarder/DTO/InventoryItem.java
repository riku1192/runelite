package net.runelite.client.plugins.eventforwarder.DTO;

public class InventoryItem {
    int inventorySlot;
    int inventoryItemId;
    int inventoryItemQuantity;

    public InventoryItem(int inventorySlot, int inventoryItemId, int inventoryItemQuantity) {
        this.inventorySlot = inventorySlot;
        this.inventoryItemId = inventoryItemId;
        this.inventoryItemQuantity = inventoryItemQuantity;
    }
}
