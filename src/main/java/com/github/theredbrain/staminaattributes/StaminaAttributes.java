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
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaminaAttributes implements ModInitializer {
	public static final String MOD_ID = "staminaattributes";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig serverConfig;

	public static RegistryEntry<EntityAttribute> STAMINA_REGENERATION;
	public static RegistryEntry<EntityAttribute> MAX_STAMINA;
	public static RegistryEntry<EntityAttribute> DEPLETED_STAMINA_REGENERATION_DELAY_THRESHOLD;
	public static RegistryEntry<EntityAttribute> STAMINA_REGENERATION_DELAY_THRESHOLD;
	public static RegistryEntry<EntityAttribute> STAMINA_TICK_THRESHOLD;
	public static RegistryEntry<EntityAttribute> RESERVED_STAMINA;
	public static RegistryEntry<EntityAttribute> ITEM_USE_STAMINA_COST;

	public static final TagKey<Item> REQUIRES_STAMINA_FOR_USE = TagKey.of(RegistryKeys.ITEM, identifier("requires_stamina_for_use"));

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing stamina!");

		// Config
		AutoConfig.register(ServerConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		serverConfig = ((ServerConfigWrapper) AutoConfig.getConfigHolder(ServerConfigWrapper.class).getConfig()).server;

		PayloadTypeRegistry.playS2C().register(ServerConfigSyncPacket.PACKET_ID, ServerConfigSyncPacket.PACKET_CODEC);
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayNetworking.send(handler.player, new ServerConfigSyncPacket(serverConfig));
		});
		UseItemCallback.EVENT.register((player, world, hand) -> {
			ItemStack itemStack = player.getStackInHand(hand);
			if (itemStack.isIn(StaminaAttributes.REQUIRES_STAMINA_FOR_USE)) {
				if(((StaminaUsingEntity) player).staminaattributes$getStamina() <= 0 && ((StaminaUsingEntity) player).staminaattributes$getItemUseStaminaCost() > 0){
					return TypedActionResult.fail(itemStack);
				}
				((StaminaUsingEntity) player).staminaattributes$addStamina(-((StaminaUsingEntity) player).staminaattributes$getItemUseStaminaCost());
			}
			return TypedActionResult.pass(itemStack);
		});

		GameRulesRegistry.init();
	}

	public record ServerConfigSyncPacket(ServerConfig serverConfig) implements CustomPayload {
		public static final CustomPayload.Id<ServerConfigSyncPacket> PACKET_ID = new CustomPayload.Id<>(identifier("server_config_sync"));
		public static final PacketCodec<RegistryByteBuf, ServerConfigSyncPacket> PACKET_CODEC = PacketCodec.of(ServerConfigSyncPacket::write, ServerConfigSyncPacket::new);

		public ServerConfigSyncPacket(RegistryByteBuf registryByteBuf) {
			this(new Gson().fromJson(registryByteBuf.readString(), ServerConfig.class));
		}

		private void write(RegistryByteBuf registryByteBuf) {
			registryByteBuf.writeString(new Gson().toJson(serverConfig));
		}

		@Override
		public CustomPayload.Id<? extends CustomPayload> getId() {
			return PACKET_ID;
		}
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}

}