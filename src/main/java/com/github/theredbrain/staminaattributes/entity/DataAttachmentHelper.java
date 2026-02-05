package com.github.theredbrain.staminaattributes.entity;

import com.github.theredbrain.staminaattributes.registry.DataAttachmentRegistry;
import net.minecraft.world.entity.LivingEntity;

public class DataAttachmentHelper {

	public static float getStamina(LivingEntity livingEntity) {
		return livingEntity.getAttachedOrElse(DataAttachmentRegistry.STAMINA, 0.0F);
	}

	public static void setStamina(LivingEntity livingEntity, double stamina) {
		livingEntity.setAttached(DataAttachmentRegistry.STAMINA, (float) stamina);
	}

}
