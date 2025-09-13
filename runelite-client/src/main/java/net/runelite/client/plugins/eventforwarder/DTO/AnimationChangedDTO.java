package net.runelite.client.plugins.eventforwarder.DTO;

import net.runelite.api.Actor;
import net.runelite.api.events.AnimationChanged;

public class AnimationChangedDTO extends RuneliteEvent {

    // Actor info
    public int actorId;        // internal index RuneLite uses
    public String name;        // player or NPC name
    public String NPCtype;        // "PLAYER", "NPC", or "UNKNOWN"

    // Animation info
    public int animationId;

    public int worldX;
    public int worldY;
    public int plane;

    public AnimationChangedDTO(AnimationChanged event)
    {
        this.setType("AnimationChanged");
        Actor actor = event.getActor();
        // this.actorId = actor.getId();
        this.name = actor.getName();
        this.NPCtype = actor.getName() != null ? "NPC" : "PLAYER"; // rough distinction

        this.animationId = actor.getAnimation();

        this.worldX = actor.getWorldLocation().getX();
        this.worldY = actor.getWorldLocation().getY();
        this.plane = actor.getWorldLocation().getPlane();
    }
}
