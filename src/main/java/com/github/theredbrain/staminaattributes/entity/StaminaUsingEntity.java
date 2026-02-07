package com.github.theredbrain.staminaattributes.entity;

public interface StaminaUsingEntity {
	
	int staminaattributes$getStaminaTickTimer();

	void staminaattributes$setStaminaTickTimer(int staminaTickTimer);

	int staminaattributes$getDepletedStaminaRegenerationDelayTimer();

	void staminaattributes$setDepletedStaminaRegenerationDelayTimer(int depletedStaminaRegenerationDelayTimer);

	int staminaattributes$getStaminaRegenerationDelayTimer();

	void staminaattributes$setStaminaRegenerationDelayTimer(int staminaRegenerationDelayTimer);

	boolean staminaattributes$delayStaminaRegeneration();

	void staminaattributes$setDelayStaminaRegeneration(boolean delayStaminaRegeneration);

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

	void staminaattributes$setStamina(float stamina);

	boolean staminaattributes$delayStaminaTick();

	void staminaattributes$setDelayStaminaTick(boolean delayStaminaTick);

	boolean staminaattributes$delayMaxValueApplication();

	void staminaattributes$setDelayMaxValueApplication(boolean delayMaxValueApplication);

	boolean staminaattributes$delayedMaxValueApplication();

	void staminaattributes$setDelayedMaxValueApplication(boolean delayedMaxValueApplication);
}
