package com.github.theredbrain.staminaattributes.mixin.client.network;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin implements StaminaUsingEntity {

	@Inject(method = "canSprint", at = @At("RETURN"), cancellable = true)
	private void staminaattributes$canSprint(CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(cir.getReturnValue() && (!StaminaAttributes.serverConfig.sprinting_requires_stamina || this.staminaattributes$getStamina() > 0));
	}
}
