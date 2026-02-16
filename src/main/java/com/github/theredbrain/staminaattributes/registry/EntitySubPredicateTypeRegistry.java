package com.github.theredbrain.staminaattributes.registry;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.advancements.criterion.StaminaUsingEntityPredicate;
import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.criterion.EntitySubPredicate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class EntitySubPredicateTypeRegistry {

	public static void init() {
	}

	private static <T extends EntitySubPredicate> MapCodec<T> register(Identifier id, MapCodec<T> mapCodec) {
		return Registry.register(
				BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE,
				id,
				mapCodec
		);
	}

	static {
		StaminaAttributes.STAMINA_USING_ENTITY_PREDICATE = register(StaminaAttributes.identifier("stamina_using_entity"), StaminaUsingEntityPredicate.CODEC);
	}
}
