package net.runelite.client.plugins.eventforwarder;

import java.util.ArrayList;
import java.util.List;

import net.runelite.client.plugins.eventforwarder.DTO.RuneliteEvent;

public class DiffPayloadDTO extends RuneliteEvent {

    List<RuneliteEvent> added = new ArrayList<>();
    List<RuneliteEvent> removed = new ArrayList<>();

    public DiffPayloadDTO(List<RuneliteEvent> added, List<RuneliteEvent> removed, List<RuneliteEvent> updated) {
        this.setType("DiffPayload");
        this.added = added;
        this.removed = removed;
    }

    public List<RuneliteEvent> getAdded() {
        return added;
    }

    public void setAdded(List<RuneliteEvent> added) {
        this.added = added;
    }

    public List<RuneliteEvent> getRemoved() {
        return removed;
    }

    public void setRemoved(List<RuneliteEvent> removed) {
        this.removed = removed;
    }

    @Override
    public String toString() {
        return "DiffPayloadDTO [type=" + super.getType() + ", added=" + added + ", removed=" + removed + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((added == null) ? 0 : added.hashCode());
        result = prime * result + ((removed == null) ? 0 : removed.hashCode());
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
        DiffPayloadDTO other = (DiffPayloadDTO) obj;
        if (added == null) {
            if (other.added != null)
                return false;
        } else if (!added.equals(other.added))
            return false;
        if (removed == null) {
            if (other.removed != null)
                return false;
        } else if (!removed.equals(other.removed))
            return false;
        return true;
    }

}

