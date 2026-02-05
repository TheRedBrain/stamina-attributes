package com.github.theredbrain.staminaattributes.registry;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;

public class DataAttachmentRegistry {
	public static AttachmentType<Float> STAMINA;

	public static void init() {
	}

	static {
		STAMINA = AttachmentRegistry.create(StaminaAttributes.identifier("stamina"), builder -> builder
				.persistent(Codec.FLOAT)
				.syncWith(ByteBufCodecs.FLOAT, AttachmentSyncPredicate.all())
		);
	}
}
