package com.github.theredbrain.staminaattributes;

import com.github.theredbrain.staminaattributes.config.ClientConfig;
import com.github.theredbrain.staminaattributes.config.ClientConfigWrapper;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class StaminaAttributesClient implements ClientModInitializer {
	public static ClientConfig clientConfig;
	@Override
	public void onInitializeClient() {
		// Config
		AutoConfig.register(ClientConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		clientConfig = ((ClientConfigWrapper)AutoConfig.getConfigHolder(ClientConfigWrapper.class).getConfig()).client;

		// Packets
		ClientPlayNetworking.registerGlobalReceiver(StaminaAttributes.ServerConfigSync.ID, (client, handler, buf, responseSender) -> {
			StaminaAttributes.serverConfig = StaminaAttributes.ServerConfigSync.read(buf);
		});
	}
}