package com.github.theredbrain.staminaattributes.mixin.server.network;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements StaminaUsingEntity {

	public ServerPlayerEntityMixin(World world, GameProfile profile) {
		super(world, profile);
	}

	@Shadow
	public abstract ServerStatHandler getStatHandler();

	@Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;addExhaustion(F)V", ordinal = 0))
	private void staminaattributes$increaseTravelMotionStats_swimming(CallbackInfo ci) {
		if (!this.getAbilities().invulnerable) {
			this.staminaattributes$addStamina(-this.staminaattributes$getSwimmingTickStaminaCost());
		}
	}

	@Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;addExhaustion(F)V", ordinal = 1))
	private void staminaattributes$increaseTravelMotionStats_walk_underwater(CallbackInfo ci) {
		if (!this.getAbilities().invulnerable) {
			this.staminaattributes$addStamina(-this.staminaattributes$getWalkingUnderwaterTickStaminaCost());
		}
	}

	@Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;addExhaustion(F)V", ordinal = 2))
	private void staminaattributes$increaseTravelMotionStats_walk_in_water(CallbackInfo ci) {
		if (!this.getAbilities().invulnerable) {
			this.staminaattributes$addStamina(-this.staminaattributes$getWalkingInWaterTickStaminaCost());
		}
	}

	@Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;increaseStat(Lnet/minecraft/util/Identifier;I)V", ordinal = 3))
	private void staminaattributes$increaseTravelMotionStats_climbing(CallbackInfo ci) {
		if (!this.getAbilities().invulnerable) {
			this.staminaattributes$addStamina(-this.staminaattributes$getClimbingTickStaminaCost());
		}
	}

	@Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;addExhaustion(F)V", ordinal = 3))
	private void staminaattributes$increaseTravelMotionStats_sprinting(CallbackInfo ci) {
		if (!this.getAbilities().invulnerable) {
			this.staminaattributes$addStamina(-this.staminaattributes$getSprintingTickStaminaCost());
		}
	}

	@Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;addExhaustion(F)V", ordinal = 4))
	private void staminaattributes$increaseTravelMotionStats_sneaking(CallbackInfo ci) {
		if (!this.getAbilities().invulnerable) {
			this.staminaattributes$addStamina(-this.staminaattributes$getSneakingTickStaminaCost());
		}
	}

	@Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;addExhaustion(F)V", ordinal = 5))
	private void staminaattributes$increaseTravelMotionStats_walking(CallbackInfo ci) {
		if (!this.getAbilities().invulnerable) {
			this.staminaattributes$addStamina(-this.staminaattributes$getWalkingTickStaminaCost());
		}
	}

	@Inject(method = "onSpawn", at = @At("TAIL"))
	public void staminaattributes$onSpawn(CallbackInfo ci) {
		this.staminaattributes$setApplyOldStamina(false);
		if (this.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.LEAVE_GAME)) <= 0) {
			this.staminaattributes$setApplyMaxStamina(true);
		}
	}

	@WrapMethod(method = "jump")
	public void staminaattributes$wrap_jump(Operation<Void> original) {
		if (this.getAbilities().invulnerable || !StaminaAttributes.SERVER_CONFIG.jumping_requires_stamina || ((StaminaUsingEntity) this).staminaattributes$getStamina() > 0) {
			original.call();
		}
	}

	@Inject(method = "jump", at = @At("RETURN"))
	public void staminaattributes$post_jump(CallbackInfo ci) {
		if (!this.getAbilities().invulnerable) {
			if (this.isSprinting()) {
				((StaminaUsingEntity) this).staminaattributes$addStamina(-((StaminaUsingEntity) this).staminaattributes$getSprintJumpingActionStaminaCost());
			} else {
				((StaminaUsingEntity) this).staminaattributes$addStamina(-((StaminaUsingEntity) this).staminaattributes$getJumpingActionStaminaCost());
			}
		}
	}

}
