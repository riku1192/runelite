package net.runelite.client.plugins.eventforwarder.DTO;

import java.util.ArrayList;

public class ClientRequestDTO extends RuneliteEvent {
    public boolean sendByTick;
    public ArrayList<Integer> targets;

    public ClientRequestDTO() {
    }

    public boolean isSendByTick() {
        return sendByTick;
    }

    public void setSendByTick(boolean sendByTick) {
        this.sendByTick = sendByTick;
    }

    public ArrayList<Integer> getTargets() {
        return targets;
    }

    public void setTargets(ArrayList<Integer> targets) {
        this.targets = targets;
    }

    @Override
    public String toString() {
        return "ClientRequestDTO [type=" + type + ", sendByTick=" + sendByTick + ", targets=" + targets + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (sendByTick ? 1231 : 1237);
        result = prime * result + ((targets == null) ? 0 : targets.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ClientRequestDTO other = (ClientRequestDTO) obj;
        if (sendByTick != other.sendByTick)
            return false;
        if (targets == null) {
            if (other.targets != null)
                return false;
        } else if (!targets.equals(other.targets))
            return false;
        return true;
    }


    
}
