package com.github.theredbrain.staminaattributes.mixin.entity.player;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.github.theredbrain.staminaattributes.registry.GameRulesRegistry;
import com.google.common.collect.HashMultimap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements StaminaUsingEntity {

	@Shadow
	@Final
	private PlayerAbilities abilities;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void staminaattributes$tick(CallbackInfo ci) {
		if (this.getServer() != null && this.getWorld().getGameRules().getBoolean(GameRulesRegistry.NATURAL_STAMINA_REGENERATION)) {
			this.getAttributes().addTemporaryModifiers(getNaturalStaminaRegenerationModifier());
		} else {
			this.getAttributes().removeModifiers(getNaturalStaminaRegenerationModifier());
		}
	}

	@Inject(method = "jump", at = @At("HEAD"), cancellable = true)
	public void staminaattributes$pre_jump(CallbackInfo ci) {
		if (!this.abilities.invulnerable && StaminaAttributes.SERVER_CONFIG.jumping_requires_stamina && this.staminaattributes$getStamina() <= 0) {
			ci.cancel();
		}
	}

	@Inject(method = "jump", at = @At("RETURN"))
	public void staminaattributes$post_jump(CallbackInfo ci) {
		if (!this.abilities.invulnerable) {
			if (this.isSprinting()) {
				this.staminaattributes$addStamina(-StaminaAttributes.SERVER_CONFIG.actionCosts.stamina_cost_sprint_jumping);
			} else {
				this.staminaattributes$addStamina(-StaminaAttributes.SERVER_CONFIG.actionCosts.stamina_cost_jumping);
			}
		}
	}

	@Unique
	private HashMultimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> getNaturalStaminaRegenerationModifier() {
		HashMultimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> hashMultimap = HashMultimap.create();
		hashMultimap.put(StaminaAttributes.STAMINA_REGENERATION, new EntityAttributeModifier(StaminaAttributes.identifier("natural_stamina_regeneration_modifier"), 1.0, EntityAttributeModifier.Operation.ADD_VALUE));
		return hashMultimap;
	}
}
