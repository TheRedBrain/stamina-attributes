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
		StaminaAttributes.STAMINA_REGENERATION = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.stamina_regeneration"), new ClampedEntityAttribute("attribute.name.generic.stamina_regeneration", 0.0F, -1024.0F, 1024.0F).setTracked(true));
		StaminaAttributes.MAX_STAMINA = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.max_stamina"), new ClampedEntityAttribute("attribute.name.generic.max_stamina", 10.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.DEPLETED_STAMINA_REGENERATION_DELAY_THRESHOLD = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.depleted_stamina_regeneration_delay_threshold"), new ClampedEntityAttribute("attribute.name.generic.depleted_stamina_regeneration_delay_threshold", 60.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.STAMINA_REGENERATION_DELAY_THRESHOLD = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.stamina_regeneration_delay_threshold"), new ClampedEntityAttribute("attribute.name.generic.stamina_regeneration_delay_threshold", 20.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.STAMINA_TICK_THRESHOLD = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.stamina_tick_threshold"), new ClampedEntityAttribute("attribute.name.generic.stamina_tick_threshold", 20.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.RESERVED_STAMINA = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.reserved_stamina"), new ClampedEntityAttribute("attribute.name.generic.reserved_stamina", 0.0F, 0.0F, 100.0F).setTracked(true));
		StaminaAttributes.ITEM_USE_STAMINA_COST  = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.item_use_stamina_cost"), new ClampedEntityAttribute("attribute.name.generic.item_use_stamina_cost", 0.0F, -1024.0F, 1024.0F).setTracked(true));
		StaminaAttributes.SPRINTING_TICK_STAMINA_COST = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.sprinting_tick_stamina_cost"), new ClampedEntityAttribute("attribute.name.generic.sprinting_tick_stamina_cost", 0.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.SNEAKING_TICK_STAMINA_COST = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.sneaking_tick_stamina_cost"), new ClampedEntityAttribute("attribute.name.generic.sneaking_tick_stamina_cost", 0.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.WALKING_TICK_STAMINA_COST = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.walking_tick_stamina_cost"), new ClampedEntityAttribute("attribute.name.generic.walking_tick_stamina_cost", 0.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.SWIMMING_TICK_STAMINA_COST = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.swimming_tick_stamina_cost"), new ClampedEntityAttribute("attribute.name.generic.swimming_tick_stamina_cost", 0.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.WALKING_UNDERWATER_TICK_STAMINA_COST = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.walking_underwater_tick_stamina_cost"), new ClampedEntityAttribute("attribute.name.generic.walking_underwater_tick_stamina_cost", 0.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.WALKING_IN_WATER_TICK_STAMINA_COST = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.walking_in_water_tick_stamina_cost"), new ClampedEntityAttribute("attribute.name.generic.walking_in_water_tick_stamina_cost", 0.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.CLIMBING_TICK_STAMINA_COST = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.climbing_tick_stamina_cost"), new ClampedEntityAttribute("attribute.name.generic.climbing_tick_stamina_cost", 0.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.JUMPING_ACTION_STAMINA_COST = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.jumping_action_stamina_cost"), new ClampedEntityAttribute("attribute.name.generic.jumping_action_stamina_cost", 0.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.SPRINT_JUMPING_ACTION_STAMINA_COST = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.sprint_jumping_action_stamina_cost"), new ClampedEntityAttribute("attribute.name.generic.sprint_jumping_action_stamina_cost", 0.0F, 0.0F, 1024.0F).setTracked(true));
	}
}
