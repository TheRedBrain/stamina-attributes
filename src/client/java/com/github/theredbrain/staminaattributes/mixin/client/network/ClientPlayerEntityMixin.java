package com.github.theredbrain.staminaattributes.mixin.client.network;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin implements StaminaUsingEntity {

	@WrapMethod(method = "canSprint()Z")
	private boolean staminaattributes$canSprint(Operation<Boolean> original) {
		return original.call() && (!StaminaAttributes.SERVER_CONFIG.sprinting_requires_stamina || this.staminaattributes$getStamina() > 0);
	}
}
