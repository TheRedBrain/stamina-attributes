package com.github.theredbrain.staminaattributes;

import com.github.theredbrain.staminaattributes.config.ClientConfig;
import com.github.theredbrain.staminaattributes.registry.ClientEventsRegistry;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import net.fabricmc.api.ClientModInitializer;

public class StaminaAttributesClient implements ClientModInitializer {
	public static ClientConfig CLIENT_CONFIG = ConfigApiJava.registerAndLoadConfig(ClientConfig::new, RegisterType.CLIENT);

	@Override
	public void onInitializeClient() {
		ClientEventsRegistry.initializeClientEvents();
	}

}