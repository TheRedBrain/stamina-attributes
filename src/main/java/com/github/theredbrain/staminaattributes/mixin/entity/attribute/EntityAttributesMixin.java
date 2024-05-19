package com.github.theredbrain.staminaattributes.mixin.entity.attribute;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityAttributes.class)
public class EntityAttributesMixin {
    @Shadow
    private static EntityAttribute register(String id, EntityAttribute attribute) {
        throw new AssertionError();
    }

    static {
        StaminaAttributes.STAMINA_REGENERATION = register(StaminaAttributes.MOD_ID + "generic.stamina_regeneration", new ClampedEntityAttribute("attribute.name.generic.stamina_regeneration", 0.0F, -1024.0F, 1024.0F).setTracked(true));
        StaminaAttributes.MAX_STAMINA = register(StaminaAttributes.MOD_ID + "generic.max_stamina", new ClampedEntityAttribute("attribute.name.generic.max_stamina", 0.0F, 0.0F, 1024.0F).setTracked(true));
        StaminaAttributes.STAMINA_REGENERATION_DELAY_THRESHOLD = register(StaminaAttributes.MOD_ID + "generic.stamina_regeneration_delay_threshold", new ClampedEntityAttribute("attribute.name.generic.stamina_regeneration_delay_threshold", 60.0F, 0.0F, 1024.0F).setTracked(true));
        StaminaAttributes.STAMINA_TICK_THRESHOLD = register(StaminaAttributes.MOD_ID + "generic.stamina_tick_threshold", new ClampedEntityAttribute("attribute.name.generic.stamina_tick_threshold", 20.0F, 0.0F, 1024.0F).setTracked(true));
    }
}
