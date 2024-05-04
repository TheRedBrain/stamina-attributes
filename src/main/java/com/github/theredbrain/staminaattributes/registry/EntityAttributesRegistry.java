package com.github.theredbrain.staminaattributes.registry;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class EntityAttributesRegistry {

    public static final EntityAttribute STAMINA_REGENERATION = new ClampedEntityAttribute("attribute.name.generic.stamina_regeneration", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute MAX_STAMINA = new ClampedEntityAttribute("attribute.name.generic.max_stamina", 0.0, 0.0, 1024.0).setTracked(true);
    public static final EntityAttribute STAMINA_REGENERATION_DELAY_THRESHOLD = new ClampedEntityAttribute("attribute.name.generic.stamina_regeneration_delay_threshold", 60.0, 0.0, 1024.0).setTracked(true);
    public static final EntityAttribute STAMINA_TICK_THRESHOLD = new ClampedEntityAttribute("attribute.name.generic.stamina_tick_threshold", 20.0, 0.0, 1024.0).setTracked(true);

    public static void registerAttributes() {
        Registry.register(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.stamina_regeneration"), STAMINA_REGENERATION);
        Registry.register(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.max_stamina"), MAX_STAMINA);
        Registry.register(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.stamina_regeneration_delay_threshold"), STAMINA_REGENERATION_DELAY_THRESHOLD);
        Registry.register(Registries.ATTRIBUTE, StaminaAttributes.identifier("generic.stamina_tick_threshold"), STAMINA_TICK_THRESHOLD);
    }
}
