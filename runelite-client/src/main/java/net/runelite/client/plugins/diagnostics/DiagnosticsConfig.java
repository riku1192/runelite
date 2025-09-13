package net.runelite.client.plugins.diagnostics;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigGroup;

@ConfigGroup(DiagnosticsPlugin.CONFIG_GROUP_KEY)
public interface DiagnosticsConfig extends Config
{
	@ConfigItem(
		keyName = "testButton",
		name = "A test buton",
		description = "A clickable test button",
		position = 1
	)
	default boolean testButton()
	{
		return false;
	}   
}
