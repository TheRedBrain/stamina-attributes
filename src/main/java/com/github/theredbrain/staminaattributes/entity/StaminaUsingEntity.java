package com.github.theredbrain.staminaattributes.entity;

public interface StaminaUsingEntity {
    int staminaattributes$getStaminaRegenerationDelayThreshold();
    int staminaattributes$getStaminaTickThreshold();
    float staminaattributes$getRegeneratedStamina();
    float staminaattributes$getStaminaRegeneration();
    float staminaattributes$getMaxStamina();
    void staminaattributes$addStamina(float amount);
    float staminaattributes$getStamina();
    void staminaattributes$setStamina(float mana);
}
