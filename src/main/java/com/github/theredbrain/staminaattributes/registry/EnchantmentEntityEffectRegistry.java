package com.github.theredbrain.staminaattributes.registry;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.world.item.enchantment.AddStaminaEnchantmentEntityEffect;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;

public class EnchantmentEntityEffectRegistry {

	public static void init() {
	}

	private static <T extends EnchantmentEntityEffect> MapCodec<T> register(Identifier id, MapCodec<T> codec) {
		return Registry.register(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, id, codec);
	}

	static {
		StaminaAttributes.ADD_STAMINA = register(StaminaAttributes.identifier("add_stamina"), AddStaminaEnchantmentEntityEffect.CODEC);
	}

}
