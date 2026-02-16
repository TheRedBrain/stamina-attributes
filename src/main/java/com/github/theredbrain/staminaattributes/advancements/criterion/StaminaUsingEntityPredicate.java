package com.github.theredbrain.staminaattributes.advancements.criterion;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.EntitySubPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public record StaminaUsingEntityPredicate(MinMaxBounds.Ints stamina_amount) implements EntitySubPredicate {
	public static final MapCodec<StaminaUsingEntityPredicate> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(MinMaxBounds.Ints.CODEC.optionalFieldOf("stamina_amount", MinMaxBounds.Ints.ANY).forGetter(StaminaUsingEntityPredicate::stamina_amount))
					.apply(instance, StaminaUsingEntityPredicate::new)
	);

	@Override
	public MapCodec<? extends EntitySubPredicate> codec() {
		return CODEC;
	}

	@Override
	public boolean matches(Entity entity, ServerLevel serverLevel, @Nullable Vec3 vec3) {
		StaminaAttributes.info("######## stamina_using_entity predicate ########");
		if (entity instanceof StaminaUsingEntity staminaUsingEntity) {
			StaminaAttributes.info("entity instanceof StaminaUsingEntity");
			int currentStamina = Mth.ceil(staminaUsingEntity.staminaattributes$getStamina());
			StaminaAttributes.info("currentStamina: " + currentStamina);
			StaminaAttributes.info("stamina_amount: " + this.stamina_amount.toString());
			boolean bl = this.stamina_amount.matches(currentStamina);
			StaminaAttributes.info("currentStamina matches stamina_amount: " + bl);
			return bl;
		}
		return false;
	}
}
