package net.runelite.client.plugins.eventforwarder;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigGroup;

@ConfigGroup(EventForwarderPlugin.CONFIG_GROUP_KEY)
public interface EventForwarderConfig extends Config {

	public static boolean toggleOnGameTick = false;

    @ConfigItem(
		keyName = "onGameTick",
		name = "onGameTick",
		description = "A toggle",
		position = 1
	)
	default boolean toggleOnGameTick()
	{
		return toggleOnGameTick;
	}   
}
