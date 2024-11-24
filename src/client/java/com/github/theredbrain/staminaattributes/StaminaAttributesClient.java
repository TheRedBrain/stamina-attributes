package com.github.theredbrain.staminaattributes;

import com.github.theredbrain.staminaattributes.config.ClientConfig;
import com.github.theredbrain.staminaattributes.config.ClientConfigWrapper;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StaminaAttributesClient implements ClientModInitializer {
	public static ClientConfig clientConfig;

	@Override
	public void onInitializeClient() {
		// Config
		AutoConfig.register(ClientConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		clientConfig = ((ClientConfigWrapper) AutoConfig.getConfigHolder(ClientConfigWrapper.class).getConfig()).client;

		// Packets
		ClientPlayNetworking.registerGlobalReceiver(StaminaAttributes.ServerConfigSyncPacket.PACKET_ID, (payload, context) -> {
			StaminaAttributes.serverConfig = payload.serverConfig();
		});
	}

}