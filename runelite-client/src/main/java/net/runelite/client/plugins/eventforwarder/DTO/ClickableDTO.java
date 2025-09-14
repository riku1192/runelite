package net.runelite.client.plugins.eventforwarder.DTO;

public class ClickableDTO extends RuneliteEvent {
    String clickableType;
    Integer clickableId;
    Integer clickableX;
    Integer clickableY;

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
        return clickableX;
    }


}
