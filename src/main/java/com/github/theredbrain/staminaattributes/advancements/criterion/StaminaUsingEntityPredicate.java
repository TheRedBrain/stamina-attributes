package com.github.theredbrain.staminaattributes.advancements.criterion;

import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.entity.EntitySubPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.predicates.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public record StaminaUsingEntityPredicate(MinMaxBounds.Ints stamina_amount) implements EntitySubPredicate {
	public static final Codec<StaminaUsingEntityPredicate> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(MinMaxBounds.Ints.CODEC.optionalFieldOf("stamina_amount", MinMaxBounds.Ints.ANY).forGetter(StaminaUsingEntityPredicate::stamina_amount))
					.apply(instance, StaminaUsingEntityPredicate::new)
	);

	//@Override
	//public Codec<? extends EntitySubPredicate> codec() {
	//	return CODEC;
	//}

	@Override
	public boolean matches(Entity entity, ServerLevel serverLevel, @Nullable Vec3 vec3) {
		if (entity instanceof StaminaUsingEntity staminaUsingEntity) {
			return this.stamina_amount.matches(Mth.ceil(staminaUsingEntity.staminaattributes$getStamina()));
		}
		return false;
	}
}
