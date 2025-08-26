package com.github.theredbrain.staminaattributes.mixin.entity.player;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.github.theredbrain.staminaattributes.registry.GameRulesRegistry;
import com.google.common.collect.HashMultimap;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	@Shadow
	@Final
	private PlayerAbilities abilities;

	@Shadow public abstract boolean isInCreativeMode();

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void staminaattributes$tick(CallbackInfo ci) {
		if (!this.getWorld().isClient()) {
			this.getAttributes().addTemporaryModifiers(getNaturalStaminaModifiers(this.getWorld()));
		}
	}

	@Inject(method = "createPlayerAttributes", at = @At("RETURN"))
	private static void staminaattributes$createPlayerAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		cir.getReturnValue()
				.add(StaminaAttributes.MAX_STAMINA, 0.0)
		;
	}

	@Inject(method = "jump", at = @At("HEAD"), cancellable = true)
	public void staminaattributes$pre_jump(CallbackInfo ci) {
		if (!this.abilities.invulnerable && StaminaAttributes.SERVER_CONFIG.jumping_requires_stamina && ((StaminaUsingEntity) this).staminaattributes$getStamina() <= 0) {
			ci.cancel();
		}
	}

	@Inject(method = "jump", at = @At("RETURN"))
	public void staminaattributes$post_jump(CallbackInfo ci) {
		if (!this.abilities.invulnerable) {
			if (this.isSprinting()) {
				((StaminaUsingEntity) this).staminaattributes$addStamina(-StaminaAttributes.SERVER_CONFIG.actionCosts.stamina_cost_sprint_jumping);
			} else {
				((StaminaUsingEntity) this).staminaattributes$addStamina(-StaminaAttributes.SERVER_CONFIG.actionCosts.stamina_cost_jumping);
			}
		}
	}

	@Unique
	private HashMultimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> getNaturalStaminaModifiers(World world) {
		HashMultimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> hashMultimap = HashMultimap.create();
		hashMultimap.put(StaminaAttributes.STAMINA_REGENERATION, new EntityAttributeModifier(StaminaAttributes.identifier("natural_stamina_regeneration_modifier"), world.getGameRules().get(GameRulesRegistry.NATURAL_STAMINA_REGENERATION).get(), EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(StaminaAttributes.MAX_STAMINA, new EntityAttributeModifier(StaminaAttributes.identifier("natural_maximum_stamina_modifier"), world.getGameRules().get(GameRulesRegistry.NATURAL_MAXIMUM_STAMINA).get(), EntityAttributeModifier.Operation.ADD_VALUE));
		return hashMultimap;
	}
}
