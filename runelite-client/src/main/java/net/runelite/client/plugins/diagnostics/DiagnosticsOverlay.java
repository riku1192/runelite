package net.runelite.client.plugins.diagnostics;

import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.*;

public class DiagnosticsOverlay extends Overlay 
{
    // Local dependencies
    // private final DiagnosticsConfig config;
    private final Client client;

    @Inject
    private DiagnosticsOverlay(DiagnosticsConfig config, Client client)
    {
        // this.config = config;
        this.client = client;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE); // drawn over 3D scene
    }

    @Override
    public Dimension render(Graphics2D g)
    {
        if (client.getTopLevelWorldView() == null)
        {
            return null;
        }

        for (NPC npc : client.getTopLevelWorldView().npcs())
        {
            if (npc == null || npc.getName() == null) continue;

            // Get the center of the NPC's clickbox
            Shape clickbox = npc.getConvexHull();
            if (clickbox != null)
            {
                Rectangle bounds = clickbox.getBounds();
                int centerX = (int) bounds.getCenterX();
                int centerY = (int) bounds.getCenterY();

                // Draw a small pink circle (not used in RS color palette)
                g.setColor(Color.PINK);
                int radius = 6;
                g.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
            }
        }
        return null;
    }
}