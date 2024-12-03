package com.github.theredbrain.staminaattributes;

import com.github.theredbrain.staminaattributes.config.ServerConfig;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.github.theredbrain.staminaattributes.registry.GameRulesRegistry;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
	public static ServerConfig SERVER_CONFIG = ConfigApiJava.registerAndLoadConfig(ServerConfig::new);

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

		UseItemCallback.EVENT.register((player, world, hand) -> {
			ItemStack itemStack = player.getStackInHand(hand);
			if (itemStack.isIn(StaminaAttributes.REQUIRES_STAMINA_FOR_USE)) {
				if (((StaminaUsingEntity) player).staminaattributes$getStamina() <= 0 && ((StaminaUsingEntity) player).staminaattributes$getItemUseStaminaCost() > 0) {
					return TypedActionResult.fail(itemStack);
				}
				((StaminaUsingEntity) player).staminaattributes$addStamina(-((StaminaUsingEntity) player).staminaattributes$getItemUseStaminaCost());
			}
			return TypedActionResult.pass(itemStack);
		});

		GameRulesRegistry.init();
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}

}