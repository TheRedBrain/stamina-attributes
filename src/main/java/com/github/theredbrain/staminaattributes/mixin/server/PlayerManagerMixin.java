package com.github.theredbrain.staminaattributes.mixin.server;

import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

	@Inject(method = "respawnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;onSpawn()V"))
	protected void staminaattributes$respawnPlayer(ServerPlayerEntity player, boolean alive, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayerEntity> cir, @Local(ordinal = 1) ServerPlayerEntity serverPlayerEntity) {
		((StaminaUsingEntity) serverPlayerEntity).staminaattributes$setApplyMaxStamina(true);
	}

}
