package com.github.theredbrain.staminaattributes.registry;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.config.ServerConfig;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

public class ServerEventsRegistry {

	public static void init() {

		UseItemCallback.EVENT.register((player, world, hand) -> {
			ItemStack itemStack = player.getStackInHand(hand);
			if (itemStack.isIn(StaminaAttributes.USING_COSTS_STAMINA)) {
				if (((StaminaUsingEntity) player).staminaattributes$getStamina() <= 0 && ((StaminaUsingEntity) player).staminaattributes$getItemUseStaminaCost() > 0) {
					player.getItemCooldownManager().set(itemStack, StaminaAttributes.SERVER_CONFIG.item_use_cooldown_when_no_stamina);
					return ActionResult.FAIL;
				}
				((StaminaUsingEntity) player).staminaattributes$addStamina(-((StaminaUsingEntity) player).staminaattributes$getItemUseStaminaCost());
			}
			return ActionResult.PASS;
		});

		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
			ServerConfig serverConfig = StaminaAttributes.SERVER_CONFIG;
			if (serverConfig.block_breaking_requires_stamina && !player.isCreative() && ((StaminaUsingEntity) player).staminaattributes$getBlockBreakingActionStaminaCost() > 0) {
				if (state.getBlock().getHardness() > 0.0 || serverConfig.breaking_zero_strength_blocks_costs_stamina) {
					((StaminaUsingEntity) player).staminaattributes$addStamina(-((StaminaUsingEntity) player).staminaattributes$getBlockBreakingActionStaminaCost());
				}
			}
		});

	}
}
