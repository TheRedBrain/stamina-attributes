package com.github.theredbrain.staminaattributes.registry;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.advancements.criterion.StaminaUsingEntityPredicate;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.Codec;
import net.minecraft.advancements.predicates.entity.EntitySubPredicate;
import net.minecraft.advancements.triggers.Criterion.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class EntitySubPredicateTypeRegistry {

	public static void init() {
	}

	private static <T extends EntitySubPredicate> Codec<T> register(Identifier id, Codec<T> Codec) {
		return Registry.register(
				BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE,
				id,
				Codec
		);
	}

	static {
		StaminaAttributes.STAMINA_USING_ENTITY_PREDICATE = register(StaminaAttributes.identifier("stamina_using_entity"), StaminaUsingEntityPredicate.CODEC);
	}
}
