package net.runelite.client.plugins.diagnostics;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigGroup;

@ConfigGroup(DiagnosticsPlugin.CONFIG_GROUP_KEY)
public interface DiagnosticsConfig extends Config
{
	@ConfigItem(
		keyName = "Enable Overlay",
		name = "Enable the overlay",
		description = "A button to enable an overlay",
		position = 1
	)
	default boolean overlayToggle()
	{
		return false;
	}   
	@ConfigItem(
		keyName = "Every Tick Displays",
		name = "onGameTick",
		description = "A toggle",
		position = 2
	)
	default boolean toggleOnGameTick()
	{
		return false;
	}   
	@ConfigItem(
		keyName = "Game Object Spawned",
		name = "onGameObjectSpawned",
		description = "A toggle",
		position = 3
	)
	default boolean toggleOnGameObjectSpawned()
	{
		return false;
	}   
	@ConfigItem(
		keyName = "Game Object Despawned",
		name = "onGameObjectDespawned",
		description = "A toggle",
		position = 4
	)
	default boolean toggleOnGameObjectDespawned()
	{
		return false;
	}   
	@ConfigItem(
		keyName = "Animation Changed",
		name = "onAnimationChanged",
		description = "A toggle",
		position = 5
	)
	default boolean toggleOnAnimationChanged()
	{
		return false;
	}   
	@ConfigItem(
		keyName = "onNpcSpawned",
		name = "onNpcSpawned",
		description = "A toggle",
		position = 6
	)
	default boolean toggleOnNpcSpawned()
	{
		return false;
	}   
	@ConfigItem(
		keyName = "onNpcChanged",
		name = "onNpcChanged",
		description = "A toggle",
		position = 7
	)
	default boolean toggleOnNpcChanged()
	{
		return false;
	}   
	@ConfigItem(
		keyName = "onMenuOptionClicked",
		name = "onMenuOptionClicked",
		description = "A toggle",
		position = 8
	)
	default boolean toggleOnMenuOptionClicked()
	{
		return false;
	}   
}
