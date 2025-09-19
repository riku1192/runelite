package net.runelite.client.plugins.eventforwarder.DTO;

public class ClickableDTO extends RuneliteEvent {
    String clickableType;
    Integer clickableId;
    Integer clickableX;
    Integer clickableY;
    Integer tileX;
    Integer tileY;
    Integer tileZ;

    public void setClickableType(String clickableType){
        this.clickableType = clickableType;
    }

    public String getClickableType(){
        return clickableType;
    }

    public void setClickableId(Integer clickableId){
        this.clickableId = clickableId;
    }

    public Integer getClickableId(){
        return clickableId;
    }

    public void setClickableX(Integer clickableX){
        this.clickableX = clickableX;
    }

    public Integer getClickableX(){
        return clickableX;
    }

    public void setClickableY(Integer clickableY){
        this.clickableY = clickableY;
    }

    public Integer getClickableY(){
        return clickableY;
    }

    public Integer getTileX() {
        return tileX;
    }

    public void setTileX(Integer tileX) {
        this.tileX = tileX;
    }

    public Integer getTileY() {
        return tileY;
    }

    public void setTileY(Integer tileY) {
        this.tileY = tileY;
    }

    public Integer getTileZ() {
        return tileZ;
    }

    public void setTileZ(Integer tileZ) {
        this.tileZ = tileZ;
    }

    @Override
    public String toString() {
        return "ClickableDTO [type=" + type + ", clickableType=" + clickableType + ", clickableId=" + clickableId
                + ", clickableX=" + clickableX + ", clickableY=" + clickableY + ", tileX=" + tileX + ", tileY=" + tileY
                + ", tileZ=" + tileZ + "]";
    }

}
