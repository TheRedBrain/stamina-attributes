package com.github.theredbrain.staminaattributes.mixin.entity.player;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.entity.PlayerHelper;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements StaminaUsingEntity {

	@Shadow
	@Final
	private Abilities abilities;

	@Shadow
	public abstract boolean hasInfiniteMaterials();

	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@WrapMethod(method = "blockActionRestricted")
	public boolean staminaattributes$wrap_blockActionRestricted(Level world, BlockPos pos, GameType gameMode, Operation<Boolean> original) {
		if (StaminaAttributes.SERVER_CONFIG.block_breaking_requires_stamina && this.staminaattributes$getStamina() <= 0) {
			return true;
		} else {
			return original.call(world, pos, gameMode);
		}
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void staminaattributes$tick(CallbackInfo ci) {
		if (!this.level().isClientSide()) {
			this.getAttributes().addTransientAttributeModifiers(PlayerHelper.getNaturalStaminaModifiers());
			if (StaminaAttributes.SERVER_CONFIG.players_can_exhaust) {
				Optional<Holder.Reference<MobEffect>> exhausted_status_effect = BuiltInRegistries.MOB_EFFECT.get(StaminaAttributes.SERVER_CONFIG.exhausted_status_effect_identifier.get());
				if (exhausted_status_effect.isPresent()) {
					if (this.staminaattributes$getStamina() <= 0) {
						if (!this.hasEffect(exhausted_status_effect.get())) {
							this.addEffect(new MobEffectInstance(exhausted_status_effect.get(), -1, 0, false, false, true));
						}
					} else {
						this.removeEffect(exhausted_status_effect.get());
					}
				}
			}
		}
	}

	@Inject(method = "createAttributes", at = @At("RETURN"))
	private static void staminaattributes$createAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
		cir.getReturnValue()
				.add(StaminaAttributes.MAX_STAMINA, 0.0)
				.add(StaminaAttributes.DEPLETED_STAMINA_REGENERATION_DELAY_THRESHOLD, 0.0)
				.add(StaminaAttributes.STAMINA_REGENERATION_DELAY_THRESHOLD, 0.0)
				.add(StaminaAttributes.STAMINA_TICK_THRESHOLD, 0.0)
		;
	}

	@WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;cannotAttack(Lnet/minecraft/world/entity/Entity;)Z"))
	public boolean staminaattributes$attack_cancelAttack(Player instance, Entity entity, Operation<Boolean> original) {
		return original.call(instance, entity) || (StaminaAttributes.SERVER_CONFIG.enable_attacking_stamina_cost && StaminaAttributes.SERVER_CONFIG.attacking_requires_stamina && this.staminaattributes$getStamina() < 0);
	}

	@Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;causeFoodExhaustion(F)V"))
	public void staminaattributes$attack_applyStaminaCost(Entity target, CallbackInfo ci) {
		if (StaminaAttributes.SERVER_CONFIG.enable_attacking_stamina_cost) {
			this.staminaattributes$addStamina(-this.staminaattributes$getAttackingActionStaminaCost());
		}
	}

	@Override
	public void jumpFromGround() {
		if (this.abilities.invulnerable || !StaminaAttributes.SERVER_CONFIG.jumping_requires_stamina || ((StaminaUsingEntity) this).staminaattributes$getJumpingActionStaminaCost() <= 0 || ((StaminaUsingEntity) this).staminaattributes$getStamina() > 0) {
			super.jumpFromGround();
		}
	}

	@Override
	protected void jumpInLiquid(TagKey<Fluid> fluid) {
		if (this.abilities.invulnerable || !StaminaAttributes.SERVER_CONFIG.jumping_in_water_requires_stamina || (!this.isUnderWater() && ((StaminaUsingEntity) this).staminaattributes$getWalkingInWaterTickStaminaCost() <= 0) || (this.isUnderWater() && ((StaminaUsingEntity) this).staminaattributes$getWalkingUnderwaterTickStaminaCost() <= 0) || ((StaminaUsingEntity) this).staminaattributes$getStamina() > 0) {
			super.jumpInLiquid(fluid);
		}
	}
}
