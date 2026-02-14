package com.github.theredbrain.staminaattributes.gui.hud;

public interface DuckGuiMixin {
	int staminaattributes$getDisplayStamina();

	void staminaattributes$setDisplayStamina(int displayStamina);

	int staminaattributes$getLastStamina();

	void staminaattributes$setLastStamina(int lastStamina);

	long staminaattributes$getLastStaminaTime();

	void staminaattributes$setLastStaminaTime(long lastStaminaTime);

	long staminaattributes$getStaminaIconBlinkTime();

	void staminaattributes$setStaminaIconBlinkTime(long staminaIconBlinkTime);

	int staminaattributes$getTickCount();
}
