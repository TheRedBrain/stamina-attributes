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

	void staminaattributes$addStamina(float amount);

	float staminaattributes$getStamina();

	void staminaattributes$setStamina(float mana);
}
