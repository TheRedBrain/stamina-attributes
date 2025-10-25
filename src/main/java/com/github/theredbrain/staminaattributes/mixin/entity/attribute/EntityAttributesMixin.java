package com.github.theredbrain.staminaattributes.mixin.entity.attribute;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityAttributes.class)
public class EntityAttributesMixin {
	static {
		StaminaAttributes.STAMINA_REGENERATION = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("stamina_regeneration"), new ClampedEntityAttribute("attribute.name.stamina_regeneration", 0.0F, -1024.0F, 1024.0F).setTracked(true));
		StaminaAttributes.MAX_STAMINA = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("max_stamina"), new ClampedEntityAttribute("attribute.name.max_stamina", 10.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.DEPLETED_STAMINA_REGENERATION_DELAY_THRESHOLD = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("depleted_stamina_regeneration_delay_threshold"), new ClampedEntityAttribute("attribute.name.depleted_stamina_regeneration_delay_threshold", 60.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.STAMINA_REGENERATION_DELAY_THRESHOLD = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("stamina_regeneration_delay_threshold"), new ClampedEntityAttribute("attribute.name.stamina_regeneration_delay_threshold", 20.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.STAMINA_TICK_THRESHOLD = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("stamina_tick_threshold"), new ClampedEntityAttribute("attribute.name.stamina_tick_threshold", 20.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.RESERVED_STAMINA = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("reserved_stamina"), new ClampedEntityAttribute("attribute.name.reserved_stamina", 0.0F, 0.0F, 100.0F).setTracked(true));
		StaminaAttributes.ITEM_USE_STAMINA_COST  = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("item_use_stamina_cost"), new ClampedEntityAttribute("attribute.name.item_use_stamina_cost", 0.0F, -1024.0F, 1024.0F).setTracked(true));
		StaminaAttributes.SPRINTING_TICK_STAMINA_COST = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("sprinting_tick_stamina_cost"), new ClampedEntityAttribute("attribute.name.sprinting_tick_stamina_cost", 0.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.SNEAKING_TICK_STAMINA_COST = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("sneaking_tick_stamina_cost"), new ClampedEntityAttribute("attribute.name.sneaking_tick_stamina_cost", 0.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.WALKING_TICK_STAMINA_COST = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("walking_tick_stamina_cost"), new ClampedEntityAttribute("attribute.name.walking_tick_stamina_cost", 0.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.SWIMMING_TICK_STAMINA_COST = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("swimming_tick_stamina_cost"), new ClampedEntityAttribute("attribute.name.swimming_tick_stamina_cost", 0.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.WALKING_UNDERWATER_TICK_STAMINA_COST = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("walking_underwater_tick_stamina_cost"), new ClampedEntityAttribute("attribute.name.walking_underwater_tick_stamina_cost", 0.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.WALKING_IN_WATER_TICK_STAMINA_COST = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("walking_in_water_tick_stamina_cost"), new ClampedEntityAttribute("attribute.name.walking_in_water_tick_stamina_cost", 0.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.CLIMBING_TICK_STAMINA_COST = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("climbing_tick_stamina_cost"), new ClampedEntityAttribute("attribute.name.climbing_tick_stamina_cost", 0.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.JUMPING_ACTION_STAMINA_COST = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("jumping_action_stamina_cost"), new ClampedEntityAttribute("attribute.name.jumping_action_stamina_cost", 0.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.SPRINT_JUMPING_ACTION_STAMINA_COST = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("sprint_jumping_action_stamina_cost"), new ClampedEntityAttribute("attribute.name.sprint_jumping_action_stamina_cost", 0.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.ATTACK_BLOCKING_ACTION_STAMINA_COST = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("attack_blocking_action_stamina_cost"), new ClampedEntityAttribute("attribute.name.attack_blocking_action_stamina_cost", 0.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.ATTACKING_ACTION_STAMINA_COST = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("attacking_action_stamina_cost"), new ClampedEntityAttribute("attribute.name.attacking_action_stamina_cost", 0.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.BLOCK_BREAKING_ACTION_STAMINA_COST = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("block_breaking_action_stamina_cost"), new ClampedEntityAttribute("attribute.name.block_breaking_action_stamina_cost", 0.0F, 0.0F, 1024.0F).setTracked(true));
	}
}
