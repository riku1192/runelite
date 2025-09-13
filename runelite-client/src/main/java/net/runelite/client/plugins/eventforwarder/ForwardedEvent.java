package net.runelite.client.plugins.eventforwarder;

public class ForwardedEvent {
    public String type;
    public Object data;

    public ForwardedEvent(String type, Object data) {
        this.type = type;
        this.data = data;
    }
}

