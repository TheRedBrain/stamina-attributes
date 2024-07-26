package com.github.theredbrain.staminaattributes.config;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(
		name = "staminaattributes"
)
public class ClientConfigWrapper extends PartitioningSerializer.GlobalData {
	@ConfigEntry.Category("client")
	@ConfigEntry.Gui.TransitiveObject
	public ClientConfig client = new ClientConfig();

	public ClientConfigWrapper() {
	}
}
