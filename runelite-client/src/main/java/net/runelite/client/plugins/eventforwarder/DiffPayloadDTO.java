package net.runelite.client.plugins.eventforwarder;

import java.util.ArrayList;
import java.util.List;

import net.runelite.client.plugins.eventforwarder.DTO.RuneliteEvent;

public class DiffPayloadDTO extends RuneliteEvent {

    List<RuneliteEvent> added = new ArrayList<>();
    List<String> removed = new ArrayList<>();
    List<RuneliteEvent> updated = new ArrayList<>();

    public DiffPayloadDTO(List<RuneliteEvent> added, List<String> removed, List<RuneliteEvent> updated) {
        this.setType("DiffPayload");
        this.added = added;
        this.removed = removed;
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "DiffPayloadDTO [added=" + added + ", removed=" + removed + ", updated=" + updated + "]";
    }

    public List<RuneliteEvent> getAdded() {
        return added;
    }

    public void setAdded(List<RuneliteEvent> added) {
        this.added = added;
    }

    public List<String> getRemoved() {
        return removed;
    }

    public void setRemoved(List<String> removed) {
        this.removed = removed;
    }

    public List<RuneliteEvent> getUpdated() {
        return updated;
    }

    public void setUpdated(List<RuneliteEvent> updated) {
        this.updated = updated;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((added == null) ? 0 : added.hashCode());
        result = prime * result + ((removed == null) ? 0 : removed.hashCode());
        result = prime * result + ((updated == null) ? 0 : updated.hashCode());
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
        if (updated == null) {
            if (other.updated != null)
                return false;
        } else if (!updated.equals(other.updated))
            return false;
        return true;
    }
}

