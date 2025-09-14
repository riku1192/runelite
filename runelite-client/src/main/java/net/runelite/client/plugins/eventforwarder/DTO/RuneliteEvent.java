package net.runelite.client.plugins.eventforwarder.DTO;

public class RuneliteEvent {
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String toString(){
        return "[type="+ type + "]";
    }
}
