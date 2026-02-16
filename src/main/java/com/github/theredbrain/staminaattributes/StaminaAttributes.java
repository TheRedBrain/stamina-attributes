package com.github.theredbrain.staminaattributes;

import com.github.theredbrain.staminaattributes.advancements.criterion.StaminaUsingEntityPredicate;
import com.github.theredbrain.staminaattributes.config.ServerConfig;
import com.github.theredbrain.staminaattributes.registry.DataAttachmentRegistry;
import com.github.theredbrain.staminaattributes.registry.EnchantmentEntityEffectRegistry;
import com.github.theredbrain.staminaattributes.registry.EntitySubPredicateTypeRegistry;
import com.github.theredbrain.staminaattributes.registry.ServerEventsRegistry;
import com.github.theredbrain.staminaattributes.world.item.enchantment.AddStaminaEnchantmentEntityEffect;
import com.mojang.serialization.MapCodec;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaminaAttributes implements ModInitializer {
	public static final String MOD_ID = "staminaattributes";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig SERVER_CONFIG;

	public static Holder<Attribute> STAMINA_REGENERATION;
	public static Holder<Attribute> MAX_STAMINA;
	public static Holder<Attribute> DEPLETED_STAMINA_REGENERATION_DELAY_THRESHOLD;
	public static Holder<Attribute> STAMINA_REGENERATION_DELAY_THRESHOLD;
	public static Holder<Attribute> STAMINA_TICK_THRESHOLD;
	public static Holder<Attribute> RESERVED_STAMINA;
	public static Holder<Attribute> ITEM_USE_STAMINA_COST;
	public static Holder<Attribute> SPRINTING_TICK_STAMINA_COST;
	public static Holder<Attribute> SNEAKING_TICK_STAMINA_COST;
	public static Holder<Attribute> WALKING_TICK_STAMINA_COST;
	public static Holder<Attribute> SWIMMING_TICK_STAMINA_COST;
	public static Holder<Attribute> WALKING_UNDERWATER_TICK_STAMINA_COST;
	public static Holder<Attribute> WALKING_IN_WATER_TICK_STAMINA_COST;
	public static Holder<Attribute> CLIMBING_TICK_STAMINA_COST;
	public static Holder<Attribute> JUMPING_ACTION_STAMINA_COST;
	public static Holder<Attribute> SPRINT_JUMPING_ACTION_STAMINA_COST;
	public static Holder<Attribute> ATTACK_BLOCKING_ACTION_STAMINA_COST;
	public static Holder<Attribute> ATTACKING_ACTION_STAMINA_COST;
	public static Holder<Attribute> BLOCK_BREAKING_ACTION_STAMINA_COST;

	public static final TagKey<Item> USING_COSTS_STAMINA = TagKey.create(Registries.ITEM, identifier("using_costs_stamina"));
	public static final TagKey<Item> CONTINUOUS_USING_COSTS_STAMINA = TagKey.create(Registries.ITEM, identifier("continuous_using_costs_stamina"));

	public static MapCodec<StaminaUsingEntityPredicate> STAMINA_USING_ENTITY_PREDICATE;

	public static MapCodec<AddStaminaEnchantmentEntityEffect> ADD_STAMINA;

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing stamina!");
		SERVER_CONFIG = ConfigApiJava.registerAndLoadConfig(ServerConfig::new);

		DataAttachmentRegistry.init();
		EnchantmentEntityEffectRegistry.init();
		EntitySubPredicateTypeRegistry.init();
		ServerEventsRegistry.init();
	}

	public static void info(String message) {
		LOGGER.info(message);
	}

	public static Identifier identifier(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

}