package com.github.theredbrain.staminaattributes;

import com.github.theredbrain.staminaattributes.config.ServerConfig;
import com.github.theredbrain.staminaattributes.config.ServerConfigWrapper;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.github.theredbrain.staminaattributes.registry.GameRulesRegistry;
import com.google.gson.Gson;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaminaAttributes implements ModInitializer {
	public static final String MOD_ID = "staminaattributes";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig serverConfig;
	private static PacketByteBuf serverConfigSerialized = PacketByteBufs.create();

	public static EntityAttribute STAMINA_REGENERATION;
	public static EntityAttribute MAX_STAMINA;
	public static EntityAttribute STAMINA_REGENERATION_DELAY_THRESHOLD;
	public static EntityAttribute DEPLETED_STAMINA_REGENERATION_DELAY_THRESHOLD;
	public static EntityAttribute STAMINA_TICK_THRESHOLD;
	public static EntityAttribute RESERVED_STAMINA;
	public static EntityAttribute ITEM_USE_STAMINA_COST;

	public static final TagKey<Item> REQUIRES_STAMINA_FOR_USE = TagKey.of(RegistryKeys.ITEM, identifier("requires_stamina_for_use"));

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing stamina!");

		// Config
		AutoConfig.register(ServerConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		serverConfig = ((ServerConfigWrapper) AutoConfig.getConfigHolder(ServerConfigWrapper.class).getConfig()).server;

		// Events
		serverConfigSerialized = ServerConfigSync.write(serverConfig);
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			sender.sendPacket(ServerConfigSync.ID, serverConfigSerialized);
		});
		UseItemCallback.EVENT.register((player, world, hand) -> {

			ItemStack itemStack = player.getStackInHand(hand);
			if (itemStack.isIn(StaminaAttributes.REQUIRES_STAMINA_FOR_USE) && (((StaminaUsingEntity) player).staminaattributes$getStamina() <= 0 && ((StaminaUsingEntity) player).staminaattributes$getItemUseStaminaCost() > 0)) {
				return TypedActionResult.fail(itemStack);
			}
			return TypedActionResult.pass(itemStack);
		});

		GameRulesRegistry.init();
	}

	public static class ServerConfigSync {
		public static Identifier ID = identifier("server_config_sync");

		public static PacketByteBuf write(ServerConfig serverConfig) {
			var gson = new Gson();
			var json = gson.toJson(serverConfig);
			var buffer = PacketByteBufs.create();
			buffer.writeString(json);
			return buffer;
		}

		public static ServerConfig read(PacketByteBuf buffer) {
			var gson = new Gson();
			var json = buffer.readString();
			return gson.fromJson(json, ServerConfig.class);
		}
	}

	public static Identifier identifier(String path) {
		return new Identifier(MOD_ID, path);
	}
}