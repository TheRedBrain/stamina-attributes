package com.github.theredbrain.staminaattributes.mixin.entity.player;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.google.common.collect.HashMultimap;
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
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
	public boolean staminaattributes$wrap_isBlockBreakingRestricted(Level world, BlockPos pos, GameType gameMode, Operation<Boolean> original) {
		if (StaminaAttributes.SERVER_CONFIG.block_breaking_requires_stamina && this.staminaattributes$getStamina() <= 0) {
			return true;
		} else {
			return original.call(world, pos, gameMode);
		}
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void staminaattributes$tick(CallbackInfo ci) {
		if (!this.level().isClientSide()) {
			this.getAttributes().addTransientAttributeModifiers(getNaturalStaminaModifiers());
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
	private static void staminaattributes$createPlayerAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
		cir.getReturnValue()
				.add(StaminaAttributes.MAX_STAMINA, 0.0)
				.add(StaminaAttributes.DEPLETED_STAMINA_REGENERATION_DELAY_THRESHOLD, 0.0)
				.add(StaminaAttributes.STAMINA_REGENERATION_DELAY_THRESHOLD, 0.0)
				.add(StaminaAttributes.STAMINA_TICK_THRESHOLD, 0.0)
		;
	}

	@WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;cannotAttack(Lnet/minecraft/world/entity/Entity;)Z"))
	public boolean staminaattributes$attack_cancelAttack(Player instance, Entity entity, Operation<Boolean> original) {
		return original.call(instance, entity) || (StaminaAttributes.SERVER_CONFIG.enable_attacking_stamina_cost && this.staminaattributes$getStamina() < 0);
	}

	@Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;causeFoodExhaustion(F)V"))
	public void staminaattributes$attack_applyStaminaCost(Entity target, CallbackInfo ci) {
		if (StaminaAttributes.SERVER_CONFIG.enable_attacking_stamina_cost) {
			this.staminaattributes$addStamina(-this.staminaattributes$getAttackingActionStaminaCost());
		}
	}

	@Override
	public void jumpFromGround() {
		if (this.abilities.invulnerable || !StaminaAttributes.SERVER_CONFIG.jumping_requires_stamina || ((StaminaUsingEntity) this).staminaattributes$getStamina() > 0) {
			super.jumpFromGround();
		}
	}

	@Override
	protected void jumpInLiquid(TagKey<Fluid> fluid) {
		if (this.abilities.invulnerable || !StaminaAttributes.SERVER_CONFIG.swimming_requires_stamina || ((StaminaUsingEntity) this).staminaattributes$getStamina() > 0) {
			super.jumpInLiquid(fluid);
		}
	}

	@Unique
	private HashMultimap<Holder<Attribute>, AttributeModifier> getNaturalStaminaModifiers() {
		HashMultimap<Holder<Attribute>, AttributeModifier> hashMultimap = HashMultimap.create();
		hashMultimap.put(StaminaAttributes.STAMINA_REGENERATION, new AttributeModifier(StaminaAttributes.identifier("natural_stamina_regeneration_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_stamina_regeneration, AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.MAX_STAMINA, new AttributeModifier(StaminaAttributes.identifier("natural_max_stamina_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_max_stamina, AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.DEPLETED_STAMINA_REGENERATION_DELAY_THRESHOLD, new AttributeModifier(StaminaAttributes.identifier("natural_depleted_stamina_regeneration_delay_threshold_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_depleted_stamina_regeneration_delay_threshold, AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.STAMINA_REGENERATION_DELAY_THRESHOLD, new AttributeModifier(StaminaAttributes.identifier("natural_stamina_regeneration_delay_threshold_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_stamina_regeneration_delay_threshold, AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.STAMINA_TICK_THRESHOLD, new AttributeModifier(StaminaAttributes.identifier("natural_stamina_tick_threshold_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_stamina_tick_threshold, AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.RESERVED_STAMINA, new AttributeModifier(StaminaAttributes.identifier("natural_reserved_stamina_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_reserved_stamina, AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.ITEM_USE_STAMINA_COST, new AttributeModifier(StaminaAttributes.identifier("natural_item_use_stamina_cost_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_item_use_stamina_cost, AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.SPRINTING_TICK_STAMINA_COST, new AttributeModifier(StaminaAttributes.identifier("natural_sprinting_tick_stamina_cost_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_sprinting_tick_stamina_cost, AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.SNEAKING_TICK_STAMINA_COST, new AttributeModifier(StaminaAttributes.identifier("natural_sneaking_tick_stamina_cost_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_sneaking_tick_stamina_cost, AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.WALKING_TICK_STAMINA_COST, new AttributeModifier(StaminaAttributes.identifier("natural_walking_tick_stamina_cost_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_walking_tick_stamina_cost, AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.SWIMMING_TICK_STAMINA_COST, new AttributeModifier(StaminaAttributes.identifier("natural_swimming_tick_stamina_cost_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_swimming_tick_stamina_cost, AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.WALKING_UNDERWATER_TICK_STAMINA_COST, new AttributeModifier(StaminaAttributes.identifier("natural_walking_underwater_tick_stamina_cost_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_walking_underwater_tick_stamina_cost, AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.WALKING_IN_WATER_TICK_STAMINA_COST, new AttributeModifier(StaminaAttributes.identifier("natural_walking_in_water_tick_stamina_cost_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_walking_in_water_tick_stamina_cost, AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.CLIMBING_TICK_STAMINA_COST, new AttributeModifier(StaminaAttributes.identifier("natural_climbing_tick_stamina_cost_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_climbing_tick_stamina_cost, AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.SPRINT_JUMPING_ACTION_STAMINA_COST, new AttributeModifier(StaminaAttributes.identifier("natural_action_stamina_cost_sprint_jumping_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_action_stamina_cost_sprint_jumping, AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.JUMPING_ACTION_STAMINA_COST, new AttributeModifier(StaminaAttributes.identifier("natural_action_stamina_cost_jumping_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_action_stamina_cost_jumping, AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.ATTACK_BLOCKING_ACTION_STAMINA_COST, new AttributeModifier(StaminaAttributes.identifier("natural_action_stamina_cost_attack_blocking_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_action_stamina_cost_attack_blocking, AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.ATTACKING_ACTION_STAMINA_COST, new AttributeModifier(StaminaAttributes.identifier("natural_action_stamina_cost_attacking_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_action_stamina_cost_attacking, AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.BLOCK_BREAKING_ACTION_STAMINA_COST, new AttributeModifier(StaminaAttributes.identifier("natural_action_stamina_cost_block_breaking_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_action_stamina_cost_block_breaking, AttributeModifier.Operation.ADD_VALUE));
		return hashMultimap;
	}
}
