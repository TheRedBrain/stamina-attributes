package com.github.theredbrain.staminaattributes.mixin.entity.player;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.google.common.collect.HashMultimap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	@Shadow
	@Final
	private PlayerAbilities abilities;

	@Shadow
	public abstract boolean isInCreativeMode();

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void staminaattributes$tick(CallbackInfo ci) {
		if (!this.getWorld().isClient()) {
			this.getAttributes().addTemporaryModifiers(getNaturalStaminaModifiers());
		}
	}

	@Inject(method = "createPlayerAttributes", at = @At("RETURN"))
	private static void staminaattributes$createPlayerAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		cir.getReturnValue()
				.add(StaminaAttributes.MAX_STAMINA, 0.0)
		;
	}

	@Inject(method = "jump", at = @At("HEAD"), cancellable = true)
	public void staminaattributes$pre_jump(CallbackInfo ci) {
		if (!this.abilities.invulnerable && StaminaAttributes.SERVER_CONFIG.jumping_requires_stamina && ((StaminaUsingEntity) this).staminaattributes$getStamina() <= 0) {
			ci.cancel();
		}
	}

	@Inject(method = "jump", at = @At("RETURN"))
	public void staminaattributes$post_jump(CallbackInfo ci) {
		if (!this.abilities.invulnerable) {
			if (this.isSprinting()) {
				((StaminaUsingEntity) this).staminaattributes$addStamina(-((StaminaUsingEntity) this).staminaattributes$getSprintJumpingActionStaminaCost());
			} else {
				((StaminaUsingEntity) this).staminaattributes$addStamina(-((StaminaUsingEntity) this).staminaattributes$getJumpingActionStaminaCost());
			}
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
		hashMultimap.put(StaminaAttributes.JUMPING_ACTION_STAMINA_COST, new EntityAttributeModifier(StaminaAttributes.identifier("natural_action_stamina_cost_sprint_jumping_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_action_stamina_cost_sprint_jumping, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.JUMPING_ACTION_STAMINA_COST, new EntityAttributeModifier(StaminaAttributes.identifier("natural_action_stamina_cost_jumping_modifier"), StaminaAttributes.SERVER_CONFIG.naturalPlayerAttributeValues.natural_action_stamina_cost_jumping, EntityAttributeModifier.Operation.ADD_VALUE));
		return hashMultimap;
	}
}
