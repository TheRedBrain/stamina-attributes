package com.github.theredbrain.staminaattributes.mixin.server;

import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class PlayerListMixin {

	@Inject(method = "respawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setHealth(F)V"))
	protected void staminaattributes$respawn(ServerPlayer serverPlayer, boolean bl, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayer> cir) {
		((StaminaUsingEntity) serverPlayer).staminaattributes$setDelayStaminaTick(true);
		((StaminaUsingEntity) serverPlayer).staminaattributes$setDelayMaxValueApplication(true);
	}

}
