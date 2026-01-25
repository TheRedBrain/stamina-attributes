package com.github.theredbrain.staminaattributes.mixin.entity.attribute;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Attributes.class)
public class AttributesMixin {
	static {
		StaminaAttributes.STAMINA_REGENERATION = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, StaminaAttributes.identifier("stamina_regeneration"), new RangedAttribute("attribute.name.stamina_regeneration", 0.0F, -1024.0F, 1024.0F).setSyncable(true));
		StaminaAttributes.MAX_STAMINA = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, StaminaAttributes.identifier("max_stamina"), new RangedAttribute("attribute.name.max_stamina", 10.0F, 0.0F, 1024.0F).setSyncable(true));
		StaminaAttributes.DEPLETED_STAMINA_REGENERATION_DELAY_THRESHOLD = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, StaminaAttributes.identifier("depleted_stamina_regeneration_delay_threshold"), new RangedAttribute("attribute.name.depleted_stamina_regeneration_delay_threshold", 60.0F, 0.0F, 1024.0F).setSyncable(true));
		StaminaAttributes.STAMINA_REGENERATION_DELAY_THRESHOLD = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, StaminaAttributes.identifier("stamina_regeneration_delay_threshold"), new RangedAttribute("attribute.name.stamina_regeneration_delay_threshold", 20.0F, 0.0F, 1024.0F).setSyncable(true));
		StaminaAttributes.STAMINA_TICK_THRESHOLD = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, StaminaAttributes.identifier("stamina_tick_threshold"), new RangedAttribute("attribute.name.stamina_tick_threshold", 20.0F, 0.0F, 1024.0F).setSyncable(true));
		StaminaAttributes.RESERVED_STAMINA = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, StaminaAttributes.identifier("reserved_stamina"), new RangedAttribute("attribute.name.reserved_stamina", 0.0F, 0.0F, 100.0F).setSyncable(true));
		StaminaAttributes.ITEM_USE_STAMINA_COST = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, StaminaAttributes.identifier("item_use_stamina_cost"), new RangedAttribute("attribute.name.item_use_stamina_cost", 0.0F, -1024.0F, 1024.0F).setSyncable(true));
		StaminaAttributes.SPRINTING_TICK_STAMINA_COST = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, StaminaAttributes.identifier("sprinting_tick_stamina_cost"), new RangedAttribute("attribute.name.sprinting_tick_stamina_cost", 0.0F, 0.0F, 1024.0F).setSyncable(true));
		StaminaAttributes.SNEAKING_TICK_STAMINA_COST = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, StaminaAttributes.identifier("sneaking_tick_stamina_cost"), new RangedAttribute("attribute.name.sneaking_tick_stamina_cost", 0.0F, 0.0F, 1024.0F).setSyncable(true));
		StaminaAttributes.WALKING_TICK_STAMINA_COST = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, StaminaAttributes.identifier("walking_tick_stamina_cost"), new RangedAttribute("attribute.name.walking_tick_stamina_cost", 0.0F, 0.0F, 1024.0F).setSyncable(true));
		StaminaAttributes.SWIMMING_TICK_STAMINA_COST = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, StaminaAttributes.identifier("swimming_tick_stamina_cost"), new RangedAttribute("attribute.name.swimming_tick_stamina_cost", 0.0F, 0.0F, 1024.0F).setSyncable(true));
		StaminaAttributes.WALKING_UNDERWATER_TICK_STAMINA_COST = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, StaminaAttributes.identifier("walking_underwater_tick_stamina_cost"), new RangedAttribute("attribute.name.walking_underwater_tick_stamina_cost", 0.0F, 0.0F, 1024.0F).setSyncable(true));
		StaminaAttributes.WALKING_IN_WATER_TICK_STAMINA_COST = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, StaminaAttributes.identifier("walking_in_water_tick_stamina_cost"), new RangedAttribute("attribute.name.walking_in_water_tick_stamina_cost", 0.0F, 0.0F, 1024.0F).setSyncable(true));
		StaminaAttributes.CLIMBING_TICK_STAMINA_COST = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, StaminaAttributes.identifier("climbing_tick_stamina_cost"), new RangedAttribute("attribute.name.climbing_tick_stamina_cost", 0.0F, 0.0F, 1024.0F).setSyncable(true));
		StaminaAttributes.JUMPING_ACTION_STAMINA_COST = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, StaminaAttributes.identifier("jumping_action_stamina_cost"), new RangedAttribute("attribute.name.jumping_action_stamina_cost", 0.0F, 0.0F, 1024.0F).setSyncable(true));
		StaminaAttributes.SPRINT_JUMPING_ACTION_STAMINA_COST = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, StaminaAttributes.identifier("sprint_jumping_action_stamina_cost"), new RangedAttribute("attribute.name.sprint_jumping_action_stamina_cost", 0.0F, 0.0F, 1024.0F).setSyncable(true));
		StaminaAttributes.ATTACK_BLOCKING_ACTION_STAMINA_COST = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, StaminaAttributes.identifier("attack_blocking_action_stamina_cost"), new RangedAttribute("attribute.name.attack_blocking_action_stamina_cost", 0.0F, 0.0F, 1024.0F).setSyncable(true));
		StaminaAttributes.ATTACKING_ACTION_STAMINA_COST = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, StaminaAttributes.identifier("attacking_action_stamina_cost"), new RangedAttribute("attribute.name.attacking_action_stamina_cost", 0.0F, 0.0F, 1024.0F).setSyncable(true));
		StaminaAttributes.BLOCK_BREAKING_ACTION_STAMINA_COST = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, StaminaAttributes.identifier("block_breaking_action_stamina_cost"), new RangedAttribute("attribute.name.block_breaking_action_stamina_cost", 0.0F, 0.0F, 1024.0F).setSyncable(true));
	}
}
