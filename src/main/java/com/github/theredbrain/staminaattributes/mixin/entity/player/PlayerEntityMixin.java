package com.github.theredbrain.staminaattributes.mixin.entity.player;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.github.theredbrain.staminaattributes.registry.GameRulesRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements StaminaUsingEntity {

	@Shadow
	@Final
	private PlayerAbilities abilities;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "jump", at = @At("HEAD"), cancellable = true)
	public void staminaattributes$pre_jump(CallbackInfo ci) {
		if (!this.abilities.invulnerable && StaminaAttributes.serverConfig.jumping_requires_stamina && this.staminaattributes$getStamina() <= 0) {
			ci.cancel();
		}
	}

	@Inject(method = "jump", at = @At("RETURN"))
	public void staminaattributes$post_jump(CallbackInfo ci) {
		if (!this.abilities.invulnerable) {
			if (this.isSprinting()) {
				this.staminaattributes$addStamina(-StaminaAttributes.serverConfig.stamina_cost_sprint_jumping);
			} else {
				this.staminaattributes$addStamina(-StaminaAttributes.serverConfig.stamina_cost_jumping);
			}
		}
	}

	@Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addExhaustion(F)V", ordinal = 0))
	private void staminaattributes$increaseTravelMotionStats_swimming(CallbackInfo ci) {
		if (!this.abilities.invulnerable) {
			this.staminaattributes$addStamina(-StaminaAttributes.serverConfig.stamina_cost_swimming);
		}
	}

	@Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addExhaustion(F)V", ordinal = 1))
	private void staminaattributes$increaseTravelMotionStats_walk_underwater(CallbackInfo ci) {
		if (!this.abilities.invulnerable) {
			this.staminaattributes$addStamina(-StaminaAttributes.serverConfig.stamina_cost_walking_underwater);
		}
	}

	@Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addExhaustion(F)V", ordinal = 2))
	private void staminaattributes$increaseTravelMotionStats_walk_in_water(CallbackInfo ci) {
		if (!this.abilities.invulnerable) {
			this.staminaattributes$addStamina(-StaminaAttributes.serverConfig.stamina_cost_walking_in_water);
		}
	}

	@Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;increaseStat(Lnet/minecraft/util/Identifier;I)V", ordinal = 3))
	private void staminaattributes$increaseTravelMotionStats_climbing(CallbackInfo ci) {
		if (!this.abilities.invulnerable) {
			this.staminaattributes$addStamina(-StaminaAttributes.serverConfig.stamina_cost_climbing);
		}
	}

	@Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addExhaustion(F)V", ordinal = 3))
	private void staminaattributes$increaseTravelMotionStats_sprinting(CallbackInfo ci) {
		if (!this.abilities.invulnerable) {
			this.staminaattributes$addStamina(-StaminaAttributes.serverConfig.stamina_cost_sprinting);
		}
	}

	@Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addExhaustion(F)V", ordinal = 4))
	private void staminaattributes$increaseTravelMotionStats_sneaking(CallbackInfo ci) {
		if (!this.abilities.invulnerable) {
			this.staminaattributes$addStamina(-StaminaAttributes.serverConfig.stamina_cost_sneaking);
		}
	}

	@Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addExhaustion(F)V", ordinal = 5))
	private void staminaattributes$increaseTravelMotionStats_walking(CallbackInfo ci) {
		if (!this.abilities.invulnerable) {
			this.staminaattributes$addStamina(-StaminaAttributes.serverConfig.stamina_cost_walking);
		}
	}

	@Override
	public float staminaattributes$getRegeneratedStamina() {
		return Math.max(this.staminaattributes$getStaminaRegeneration(), (this.getServer() != null && this.getServer().getGameRules().getBoolean(GameRulesRegistry.NATURAL_STAMINA_REGENERATION) ? 1.0F : 0.0F));
	}
}
