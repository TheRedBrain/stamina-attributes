package com.github.theredbrain.staminaattributes.mixin.entity.player;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.google.common.collect.HashMultimap;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements StaminaUsingEntity {

	@Shadow
	@Final
	private PlayerAbilities abilities;

	@Shadow
	public abstract boolean isInCreativeMode();

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@WrapMethod(method = "isBlockBreakingRestricted")
	public boolean staminaattributes$wrap_isBlockBreakingRestricted(World world, BlockPos pos, GameMode gameMode, Operation<Boolean> original) {
		if (StaminaAttributes.SERVER_CONFIG.block_breaking_requires_stamina && this.staminaattributes$getStamina() <= 0) {
			return true;
		} else {
			return original.call(world, pos, gameMode);
		}
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void staminaattributes$tick(CallbackInfo ci) {
		if (!this.getEntityWorld().isClient()) {
			this.getAttributes().addTemporaryModifiers(getNaturalStaminaModifiers());
			if (StaminaAttributes.SERVER_CONFIG.players_can_exhaust) {
				Optional<RegistryEntry.Reference<StatusEffect>> exhausted_status_effect = Registries.STATUS_EFFECT.getEntry(StaminaAttributes.SERVER_CONFIG.exhausted_status_effect_identifier.get());
				if (exhausted_status_effect.isPresent()) {
					if (this.staminaattributes$getStamina() <= 0) {
						if (!this.hasStatusEffect(exhausted_status_effect.get())) {
							this.addStatusEffect(new StatusEffectInstance(exhausted_status_effect.get(), -1, 0, false, false, true));
						}
					} else {
						this.removeStatusEffect(exhausted_status_effect.get());
					}
				}
			}
		}
	}

	@Inject(method = "createPlayerAttributes", at = @At("RETURN"))
	private static void staminaattributes$createPlayerAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		cir.getReturnValue()
				.add(StaminaAttributes.MAX_STAMINA, 0.0)
				.add(StaminaAttributes.DEPLETED_STAMINA_REGENERATION_DELAY_THRESHOLD, 0.0)
				.add(StaminaAttributes.STAMINA_REGENERATION_DELAY_THRESHOLD, 0.0)
				.add(StaminaAttributes.STAMINA_TICK_THRESHOLD, 0.0)
		;
	}

	@WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;handleAttack(Lnet/minecraft/entity/Entity;)Z"))
	public boolean staminaattributes$attack_cancelAttack(Entity instance, Entity attacker, Operation<Boolean> original) {
		return original.call(instance, attacker) || (StaminaAttributes.SERVER_CONFIG.enable_attacking_stamina_cost && this.staminaattributes$getStamina() < 0);
	}

	@Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;resetLastAttackedTicks()V"))
	public void staminaattributes$attack_applyStaminaCost(Entity target, CallbackInfo ci) {
		if (StaminaAttributes.SERVER_CONFIG.enable_attacking_stamina_cost) {
			this.staminaattributes$addStamina(-this.staminaattributes$getAttackingActionStaminaCost());
		}
	}

	@Override
	public void jump() {
		if (this.abilities.invulnerable || !StaminaAttributes.SERVER_CONFIG.jumping_requires_stamina || ((StaminaUsingEntity) this).staminaattributes$getStamina() > 0) {
			super.jump();
		}
	}

	@Override
	protected void swimUpward(TagKey<Fluid> fluid) {
		if (this.abilities.invulnerable || !StaminaAttributes.SERVER_CONFIG.swimming_requires_stamina || ((StaminaUsingEntity) this).staminaattributes$getStamina() > 0) {
			super.swimUpward(fluid);
		}
	}

	@Unique
	private HashMultimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> getNaturalStaminaModifiers() {
		HashMultimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> hashMultimap = HashMultimap.create();
		hashMultimap.put(StaminaAttributes.STAMINA_REGENERATION, new EntityAttributeModifier(StaminaAttributes.identifier("natural_stamina_regeneration_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_stamina_regeneration, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.MAX_STAMINA, new EntityAttributeModifier(StaminaAttributes.identifier("natural_max_stamina_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_max_stamina, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.DEPLETED_STAMINA_REGENERATION_DELAY_THRESHOLD, new EntityAttributeModifier(StaminaAttributes.identifier("natural_depleted_stamina_regeneration_delay_threshold_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_depleted_stamina_regeneration_delay_threshold, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.STAMINA_REGENERATION_DELAY_THRESHOLD, new EntityAttributeModifier(StaminaAttributes.identifier("natural_stamina_regeneration_delay_threshold_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_stamina_regeneration_delay_threshold, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.STAMINA_TICK_THRESHOLD, new EntityAttributeModifier(StaminaAttributes.identifier("natural_stamina_tick_threshold_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_stamina_tick_threshold, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.RESERVED_STAMINA, new EntityAttributeModifier(StaminaAttributes.identifier("natural_reserved_stamina_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_reserved_stamina, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.ITEM_USE_STAMINA_COST, new EntityAttributeModifier(StaminaAttributes.identifier("natural_item_use_stamina_cost_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_item_use_stamina_cost, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.SPRINTING_TICK_STAMINA_COST, new EntityAttributeModifier(StaminaAttributes.identifier("natural_sprinting_tick_stamina_cost_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_sprinting_tick_stamina_cost, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.SNEAKING_TICK_STAMINA_COST, new EntityAttributeModifier(StaminaAttributes.identifier("natural_sneaking_tick_stamina_cost_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_sneaking_tick_stamina_cost, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.WALKING_TICK_STAMINA_COST, new EntityAttributeModifier(StaminaAttributes.identifier("natural_walking_tick_stamina_cost_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_walking_tick_stamina_cost, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.SWIMMING_TICK_STAMINA_COST, new EntityAttributeModifier(StaminaAttributes.identifier("natural_swimming_tick_stamina_cost_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_swimming_tick_stamina_cost, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.WALKING_UNDERWATER_TICK_STAMINA_COST, new EntityAttributeModifier(StaminaAttributes.identifier("natural_walking_underwater_tick_stamina_cost_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_walking_underwater_tick_stamina_cost, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.WALKING_IN_WATER_TICK_STAMINA_COST, new EntityAttributeModifier(StaminaAttributes.identifier("natural_walking_in_water_tick_stamina_cost_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_walking_in_water_tick_stamina_cost, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.CLIMBING_TICK_STAMINA_COST, new EntityAttributeModifier(StaminaAttributes.identifier("natural_climbing_tick_stamina_cost_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_climbing_tick_stamina_cost, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.SPRINT_JUMPING_ACTION_STAMINA_COST, new EntityAttributeModifier(StaminaAttributes.identifier("natural_action_stamina_cost_sprint_jumping_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_action_stamina_cost_sprint_jumping, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.JUMPING_ACTION_STAMINA_COST, new EntityAttributeModifier(StaminaAttributes.identifier("natural_action_stamina_cost_jumping_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_action_stamina_cost_jumping, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.ATTACK_BLOCKING_ACTION_STAMINA_COST, new EntityAttributeModifier(StaminaAttributes.identifier("natural_action_stamina_cost_attack_blocking_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_action_stamina_cost_attack_blocking, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.ATTACKING_ACTION_STAMINA_COST, new EntityAttributeModifier(StaminaAttributes.identifier("natural_action_stamina_cost_attacking_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_action_stamina_cost_attacking, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.BLOCK_BREAKING_ACTION_STAMINA_COST, new EntityAttributeModifier(StaminaAttributes.identifier("natural_action_stamina_cost_block_breaking_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_action_stamina_cost_block_breaking, EntityAttributeModifier.Operation.ADD_VALUE));
		return hashMultimap;
	}
}
