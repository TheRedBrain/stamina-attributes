package com.github.theredbrain.staminaattributes.mixin.server.network;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player implements StaminaUsingEntity {

	public ServerPlayerMixin(Level world, GameProfile profile) {
		super(world, profile);
	}

	@Inject(method = "checkMovementStatistics", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;causeFoodExhaustion(F)V", ordinal = 0))
	private void staminaattributes$checkMovementStatistics_swimming(CallbackInfo ci) {
		if (!this.getAbilities().invulnerable) {
			this.staminaattributes$addStamina(-this.staminaattributes$getSwimmingTickStaminaCost());
		}
	}

	@Inject(method = "checkMovementStatistics", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;causeFoodExhaustion(F)V", ordinal = 1))
	private void staminaattributes$checkMovementStatistics_walk_underwater(CallbackInfo ci) {
		if (!this.getAbilities().invulnerable) {
			this.staminaattributes$addStamina(-this.staminaattributes$getWalkingUnderwaterTickStaminaCost());
		}
	}

	@Inject(method = "checkMovementStatistics", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;causeFoodExhaustion(F)V", ordinal = 2))
	private void staminaattributes$checkMovementStatistics_walk_in_water(CallbackInfo ci) {
		if (!this.getAbilities().invulnerable) {
			this.staminaattributes$addStamina(-this.staminaattributes$getWalkingInWaterTickStaminaCost());
		}
	}

	@Inject(method = "checkMovementStatistics", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;awardStat(Lnet/minecraft/resources/Identifier;I)V", ordinal = 3))
	private void staminaattributes$checkMovementStatistics_climbing(CallbackInfo ci) {
		if (!this.getAbilities().invulnerable) {
			this.staminaattributes$addStamina(-this.staminaattributes$getClimbingTickStaminaCost());
		}
	}

	@Inject(method = "checkMovementStatistics", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;causeFoodExhaustion(F)V", ordinal = 3))
	private void staminaattributes$checkMovementStatistics_sprinting(CallbackInfo ci) {
		if (!this.getAbilities().invulnerable) {
			this.staminaattributes$addStamina(-this.staminaattributes$getSprintingTickStaminaCost());
		}
	}

	@Inject(method = "checkMovementStatistics", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;causeFoodExhaustion(F)V", ordinal = 4))
	private void staminaattributes$checkMovementStatistics_sneaking(CallbackInfo ci) {
		if (!this.getAbilities().invulnerable) {
			this.staminaattributes$addStamina(-this.staminaattributes$getSneakingTickStaminaCost());
		}
	}

	@Inject(method = "checkMovementStatistics", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;causeFoodExhaustion(F)V", ordinal = 5))
	private void staminaattributes$checkMovementStatistics_walking(CallbackInfo ci) {
		if (!this.getAbilities().invulnerable) {
			this.staminaattributes$addStamina(-this.staminaattributes$getWalkingTickStaminaCost());
		}
	}

	@WrapMethod(method = "jumpFromGround")
	public void staminaattributes$wrap_jumpFromGround(Operation<Void> original) {
		if (this.getAbilities().invulnerable || !StaminaAttributes.SERVER_CONFIG.jumping_requires_stamina || this.staminaattributes$getStamina() > 0) {
			original.call();
		}
	}

	@Inject(method = "jumpFromGround", at = @At("RETURN"))
	public void staminaattributes$post_jumpFromGround(CallbackInfo ci) {
		if (!this.getAbilities().invulnerable) {
			if (this.isSprinting()) {
				this.staminaattributes$addStamina(-this.staminaattributes$getSprintJumpingActionStaminaCost());
			} else {
				this.staminaattributes$addStamina(-this.staminaattributes$getJumpingActionStaminaCost());
			}
		}
	}

}
