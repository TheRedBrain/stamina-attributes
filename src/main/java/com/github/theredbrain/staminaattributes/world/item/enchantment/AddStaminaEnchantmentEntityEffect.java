package com.github.theredbrain.staminaattributes.world.item.enchantment;

import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record AddStaminaEnchantmentEntityEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
	public static final MapCodec<AddStaminaEnchantmentEntityEffect> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(LevelBasedValue.CODEC.fieldOf("amount").forGetter(AddStaminaEnchantmentEntityEffect::amount)).apply(instance, AddStaminaEnchantmentEntityEffect::new)
	);

	@Override
	public void apply(ServerLevel serverLevel, int i, EnchantedItemInUse enchantedItemInUse, Entity entity, Vec3 vec3) {
		if (entity instanceof StaminaUsingEntity staminaUsingEntity) {
			staminaUsingEntity.staminaattributes$addStamina(this.amount.calculate(i));
		}
	}

	@Override
	public MapCodec<AddStaminaEnchantmentEntityEffect> codec() {
		return CODEC;
	}
}
