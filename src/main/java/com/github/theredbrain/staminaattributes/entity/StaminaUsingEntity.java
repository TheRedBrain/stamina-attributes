package com.github.theredbrain.staminaattributes.entity;

public interface StaminaUsingEntity {
	int staminaattributes$getDepletedStaminaRegenerationDelayThreshold();

	int staminaattributes$getStaminaRegenerationDelayThreshold();

	int staminaattributes$getStaminaTickThreshold();

	float staminaattributes$getRegeneratedStamina();

	float staminaattributes$getStaminaRegeneration();

	float staminaattributes$getUnreservedStamina();

	float staminaattributes$getMaxStamina();

	float staminaattributes$getReservedStamina();

	float staminaattributes$getItemUseStaminaCost();

	float staminaattributes$getSprintingTickStaminaCost();

	float staminaattributes$getSneakingTickStaminaCost();

	float staminaattributes$getWalkingTickStaminaCost();

	float staminaattributes$getSwimmingTickStaminaCost();

	float staminaattributes$getWalkingUnderwaterTickStaminaCost();

	float staminaattributes$getWalkingInWaterTickStaminaCost();

	float staminaattributes$getClimbingTickStaminaCost();

	float staminaattributes$getJumpingActionStaminaCost();

	float staminaattributes$getSprintJumpingActionStaminaCost();

	float staminaattributes$getAttackBlockingActionStaminaCost();

	float staminaattributes$getAttackingActionStaminaCost();

	float staminaattributes$getBlockBreakingActionStaminaCost();

	void staminaattributes$addStamina(float amount);

	float staminaattributes$getStamina();

	void staminaattributes$setStamina(float mana);

	void staminaattributes$setApplyOldStamina(boolean applyOldStamina);

	void staminaattributes$setApplyMaxStamina(boolean applyMaxStamina);
}
