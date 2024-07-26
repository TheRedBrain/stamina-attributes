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
		StaminaAttributes.STAMINA_REGENERATION = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.stamina_regeneration"), new ClampedEntityAttribute("attribute.name.generic.stamina_regeneration", 1.0F, -1024.0F, 1024.0F).setTracked(true));
		StaminaAttributes.MAX_STAMINA = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.max_stamina"), new ClampedEntityAttribute("attribute.name.generic.max_stamina", 10.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.DEPLETED_STAMINA_REGENERATION_DELAY_THRESHOLD = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.depleted_stamina_regeneration_delay_threshold"), new ClampedEntityAttribute("attribute.name.generic.depleted_stamina_regeneration_delay_threshold", 60.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.STAMINA_REGENERATION_DELAY_THRESHOLD = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.stamina_regeneration_delay_threshold"), new ClampedEntityAttribute("attribute.name.generic.stamina_regeneration_delay_threshold", 20.0F, 0.0F, 1024.0F).setTracked(true));
		StaminaAttributes.STAMINA_TICK_THRESHOLD = Registry.registerReference(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.stamina_tick_threshold"), new ClampedEntityAttribute("attribute.name.generic.stamina_tick_threshold", 20.0F, 0.0F, 1024.0F).setTracked(true));
	}
}
