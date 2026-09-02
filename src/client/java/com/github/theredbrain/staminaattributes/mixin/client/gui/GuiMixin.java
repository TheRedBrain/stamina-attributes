package com.github.theredbrain.staminaattributes.mixin.client.gui;

import com.github.theredbrain.staminaattributes.gui.hud.DuckGuiMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(Hud.class)
public class GuiMixin implements DuckGuiMixin {

	@Shadow
	private int tickCount;

	@Unique
	private int displayStamina;
	@Unique
	private int lastStamina;
	@Unique
	private long lastStaminaTime;
	@Unique
	private long staminaBlinkTime;

	@Override
	public int staminaattributes$getDisplayStamina() {
		return this.displayStamina;
	}

	@Override
	public void staminaattributes$setDisplayStamina(int displayStamina) {
		this.displayStamina = displayStamina;
	}

	@Override
	public int staminaattributes$getLastStamina() {
		return this.lastStamina;
	}

	@Override
	public void staminaattributes$setLastStamina(int lastStamina) {
		this.lastStamina = lastStamina;
	}

	@Override
	public long staminaattributes$getLastStaminaTime() {
		return this.lastStaminaTime;
	}

	@Override
	public void staminaattributes$setLastStaminaTime(long lastStaminaTime) {
		this.lastStaminaTime = lastStaminaTime;
	}

	@Override
	public long staminaattributes$getStaminaIconBlinkTime() {
		return this.staminaBlinkTime;
	}

	@Override
	public void staminaattributes$setStaminaIconBlinkTime(long staminaIconBlinkTime) {
		this.staminaBlinkTime = staminaIconBlinkTime;
	}

	@Override
	public int staminaattributes$getTickCount() {
		return this.tickCount;
	}
}
