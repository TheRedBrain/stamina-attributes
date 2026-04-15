package com.github.theredbrain.staminaattributes.entity;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class LivingEntityHelper {

	public static void tick(LivingEntity livingEntity) {

		if (!livingEntity.level().isClientSide()) {
			if (((StaminaUsingEntity) livingEntity).staminaattributes$delayStaminaTick()) {
				StaminaAttributes.info("delayStaminaTick");
				((StaminaUsingEntity) livingEntity).staminaattributes$setDelayStaminaTick(false);
				return;
			}
			if (((StaminaUsingEntity) livingEntity).staminaattributes$delayMaxValueApplication()) {
				StaminaAttributes.info("delayMaxValueApplication");
				((StaminaUsingEntity) livingEntity).staminaattributes$setDelayedMaxValueApplication(true);
				((StaminaUsingEntity) livingEntity).staminaattributes$setDelayMaxValueApplication(false);
				return;
			}
			if (((StaminaUsingEntity) livingEntity).staminaattributes$delayedMaxValueApplication()) {
				StaminaAttributes.info("delayedMaxValueApplication");
				((StaminaUsingEntity) livingEntity).staminaattributes$setStamina(((StaminaUsingEntity) livingEntity).staminaattributes$getUnreservedStamina());
				((StaminaUsingEntity) livingEntity).staminaattributes$setDelayedMaxValueApplication(false);
				return;
			}

			int staminaTickTimer = ((StaminaUsingEntity) livingEntity).staminaattributes$getStaminaTickTimer();
			int depletedStaminaRegenerationDelayTimer = ((StaminaUsingEntity) livingEntity).staminaattributes$getDepletedStaminaRegenerationDelayTimer();
			int staminaRegenerationDelayTimer = ((StaminaUsingEntity) livingEntity).staminaattributes$getStaminaRegenerationDelayTimer();
			boolean delayStaminaRegeneration = ((StaminaUsingEntity) livingEntity).staminaattributes$delayStaminaRegeneration();

			double stamina = ((StaminaUsingEntity) livingEntity).staminaattributes$getStamina();

			staminaTickTimer++;

			if (stamina <= 0 && delayStaminaRegeneration) {
				depletedStaminaRegenerationDelayTimer = ((StaminaUsingEntity) livingEntity).staminaattributes$getDepletedStaminaRegenerationDelayThreshold();
				staminaRegenerationDelayTimer = 0;
				delayStaminaRegeneration = false;
			}
			if (stamina > 0 && !delayStaminaRegeneration) {
				delayStaminaRegeneration = true;
			}
			if (depletedStaminaRegenerationDelayTimer > 0) {
				depletedStaminaRegenerationDelayTimer--;
			}
			if (staminaRegenerationDelayTimer > 0) {
				staminaRegenerationDelayTimer--;
			}

			if (
					staminaTickTimer >= ((StaminaUsingEntity) livingEntity).staminaattributes$getStaminaTickThreshold()
							&& depletedStaminaRegenerationDelayTimer <= 0
							&& staminaRegenerationDelayTimer <= 0
			) {
				if (stamina < ((StaminaUsingEntity) livingEntity).staminaattributes$getUnreservedStamina() || ((StaminaUsingEntity) livingEntity).staminaattributes$getRegeneratedStamina() < 0) {
					((StaminaUsingEntity) livingEntity).staminaattributes$addStamina(((StaminaUsingEntity) livingEntity).staminaattributes$getRegeneratedStamina());
				}
				if (stamina > ((StaminaUsingEntity) livingEntity).staminaattributes$getUnreservedStamina()) {
					((StaminaUsingEntity) livingEntity).staminaattributes$setStamina(((StaminaUsingEntity) livingEntity).staminaattributes$getUnreservedStamina());
				}
				staminaTickTimer = 0;
			}

			if (livingEntity.isUsingItem() && livingEntity.getUseItem().is(StaminaAttributes.CONTINUOUS_USING_COSTS_STAMINA) && ((StaminaUsingEntity) livingEntity).staminaattributes$getItemUseStaminaCost() > 0 && stamina <= 0) {
				if (livingEntity instanceof Player playerEntity) {
					playerEntity.getCooldowns().addCooldown(livingEntity.getUseItem(), StaminaAttributes.SERVER_CONFIG.item_use_cooldown_when_no_stamina);
				}
				livingEntity.releaseUsingItem();
			}

			((StaminaUsingEntity) livingEntity).staminaattributes$setStaminaTickTimer(staminaTickTimer);
			((StaminaUsingEntity) livingEntity).staminaattributes$setDepletedStaminaRegenerationDelayTimer(depletedStaminaRegenerationDelayTimer);
			((StaminaUsingEntity) livingEntity).staminaattributes$setStaminaRegenerationDelayTimer(staminaRegenerationDelayTimer);
			((StaminaUsingEntity) livingEntity).staminaattributes$setDelayStaminaRegeneration(delayStaminaRegeneration);
		}
	}
}
