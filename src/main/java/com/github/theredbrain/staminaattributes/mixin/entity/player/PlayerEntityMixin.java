package com.github.theredbrain.staminaattributes.mixin.entity.player;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.github.theredbrain.staminaattributes.registry.GameRulesRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements StaminaUsingEntity {

	@Shadow
	@Final
	private PlayerAbilities abilities;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "jump", at = @At("HEAD"), cancellable = true)
	public void staminaattributes$pre_jump(CallbackInfo ci) {
		if (!this.abilities.invulnerable && StaminaAttributes.serverConfig.jumping_requires_stamina && this.staminaattributes$getStamina() <= 0) {
			ci.cancel();
		}
	}

	@Inject(method = "jump", at = @At("RETURN"))
	public void staminaattributes$post_jump(CallbackInfo ci) {
		if (!this.abilities.invulnerable) {
			if (this.isSprinting()) {
				this.staminaattributes$addStamina(-StaminaAttributes.serverConfig.stamina_cost_sprint_jumping);
			} else {
				this.staminaattributes$addStamina(-StaminaAttributes.serverConfig.stamina_cost_jumping);
			}
		}
	}

	@Override
	public float staminaattributes$getRegeneratedStamina() {
		return Math.max(this.staminaattributes$getStaminaRegeneration(), (this.getServer() != null && this.getServer().getGameRules().getBoolean(GameRulesRegistry.NATURAL_STAMINA_REGENERATION) ? 1.0F : 0.0F));
	}
}
