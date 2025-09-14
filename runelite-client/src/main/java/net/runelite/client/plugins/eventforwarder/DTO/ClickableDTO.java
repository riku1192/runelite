package net.runelite.client.plugins.eventforwarder.DTO;

import java.util.Objects;

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
        return clickableY;
    }

    public String toString(){
        return super.toString() + ", [clickableType="+ clickableType + "]" 
                    + ", [clickableId="+ clickableId + "]" 
                    + ", [clickableX="+ clickableX + "]" 
                    + ", [clickableY="+ clickableY + "]";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RuneliteEvent)) return false;
        RuneliteEvent that = (RuneliteEvent) o;
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

}
