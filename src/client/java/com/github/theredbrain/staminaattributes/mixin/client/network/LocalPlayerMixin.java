package com.github.theredbrain.staminaattributes.mixin.client.network;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin implements StaminaUsingEntity {

	@Shadow
	public abstract boolean isUnderWater();

	@WrapOperation(method = "isSprintingPossible(Z)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;hasEnoughFoodToDoExhaustiveManoeuvres()Z"))
	private boolean staminaattributes$wrap_hasEnoughFoodToDoExhaustiveManoeuvres(LocalPlayer instance, Operation<Boolean> original) {
		return original.call(instance) && (this.isUnderWater() ? (!StaminaAttributes.SERVER_CONFIG.swimming_requires_stamina || this.staminaattributes$getSwimmingTickStaminaCost() <= 0 || this.staminaattributes$getStamina() > 0) : (!StaminaAttributes.SERVER_CONFIG.sprinting_requires_stamina || this.staminaattributes$getSprintingTickStaminaCost() <= 0 || this.staminaattributes$getStamina() > 0));
	}

}
