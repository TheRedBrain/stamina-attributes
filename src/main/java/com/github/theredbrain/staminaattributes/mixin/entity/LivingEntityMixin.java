package com.github.theredbrain.staminaattributes.mixin.entity;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.entity.DataAttachmentHelper;
import com.github.theredbrain.staminaattributes.entity.LivingEntityHelper;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements StaminaUsingEntity {

	@Shadow
	public abstract double getAttributeValue(Holder<Attribute> attribute);

	@Unique
	private int staminaTickTimer = 0;
	@Unique
	private int depletedStaminaRegenerationDelayTimer = 0;
	@Unique
	private int staminaRegenerationDelayTimer = 0;
	@Unique
	private boolean delayStaminaRegeneration = false;
	@Unique
	private boolean delayStaminaTick = false;
	@Unique
	private boolean delayMaxValueApplication = false;
	@Unique
	private boolean delayedMaxValueApplication = false;

	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "createLivingAttributes", at = @At("RETURN"))
	private static void staminaattributes$createLivingAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
		cir.getReturnValue()
				.add(StaminaAttributes.STAMINA_REGENERATION)
				.add(StaminaAttributes.MAX_STAMINA)
				.add(StaminaAttributes.DEPLETED_STAMINA_REGENERATION_DELAY_THRESHOLD)
				.add(StaminaAttributes.STAMINA_REGENERATION_DELAY_THRESHOLD)
				.add(StaminaAttributes.STAMINA_TICK_THRESHOLD)
				.add(StaminaAttributes.RESERVED_STAMINA)
				.add(StaminaAttributes.ITEM_USE_STAMINA_COST)
				.add(StaminaAttributes.SPRINTING_TICK_STAMINA_COST)
				.add(StaminaAttributes.SNEAKING_TICK_STAMINA_COST)
				.add(StaminaAttributes.WALKING_TICK_STAMINA_COST)
				.add(StaminaAttributes.SWIMMING_TICK_STAMINA_COST)
				.add(StaminaAttributes.WALKING_UNDERWATER_TICK_STAMINA_COST)
				.add(StaminaAttributes.WALKING_IN_WATER_TICK_STAMINA_COST)
				.add(StaminaAttributes.CLIMBING_TICK_STAMINA_COST)
				.add(StaminaAttributes.JUMPING_ACTION_STAMINA_COST)
				.add(StaminaAttributes.SPRINT_JUMPING_ACTION_STAMINA_COST)
				.add(StaminaAttributes.ATTACK_BLOCKING_ACTION_STAMINA_COST)
				.add(StaminaAttributes.ATTACKING_ACTION_STAMINA_COST)
				.add(StaminaAttributes.BLOCK_BREAKING_ACTION_STAMINA_COST)
		;
	}

	@Inject(method = "blockUsingItem", at = @At("TAIL"))
	protected void staminaattributes$blockUsingItem(ServerLevel world, LivingEntity attacker, CallbackInfo ci) {
		if (StaminaAttributes.SERVER_CONFIG.enable_attack_blocking_stamina_cost) {
			this.staminaattributes$addStamina(-this.staminaattributes$getAttackBlockingActionStaminaCost());
		}
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void staminaattributes$tick(CallbackInfo ci) {
		LivingEntityHelper.tick(((LivingEntity) (Object) this));
	}

	@Inject(method = "updateUsingItem", at = @At("HEAD"))
	protected void staminaattributes$updateUsingItem(ItemStack stack, CallbackInfo ci) {
		if (stack.is(StaminaAttributes.CONTINUOUS_USING_COSTS_STAMINA) && staminaattributes$getItemUseStaminaCost() > 0 && staminaattributes$getStamina() > 0) {
			this.staminaattributes$addStamina(-staminaattributes$getItemUseStaminaCost());
		}
	}

	public int staminaattributes$getStaminaTickTimer() {
		return this.staminaTickTimer;
	}

	public void staminaattributes$setStaminaTickTimer(int staminaTickTimer) {
		this.staminaTickTimer = staminaTickTimer;
	}

	public int staminaattributes$getDepletedStaminaRegenerationDelayTimer() {
		return this.depletedStaminaRegenerationDelayTimer;
	}

	public void staminaattributes$setDepletedStaminaRegenerationDelayTimer(int depletedStaminaRegenerationDelayTimer) {
		this.depletedStaminaRegenerationDelayTimer = depletedStaminaRegenerationDelayTimer;
	}

	public int staminaattributes$getStaminaRegenerationDelayTimer() {
		return this.staminaRegenerationDelayTimer;
	}

	public void staminaattributes$setStaminaRegenerationDelayTimer(int staminaRegenerationDelayTimer) {
		this.staminaRegenerationDelayTimer = staminaRegenerationDelayTimer;
	}

	public boolean staminaattributes$delayStaminaRegeneration() {
		return this.delayStaminaRegeneration;
	}

	public void staminaattributes$setDelayStaminaRegeneration(boolean delayStaminaRegeneration) {
		this.delayStaminaRegeneration = delayStaminaRegeneration;
	}

	@Override
	public int staminaattributes$getDepletedStaminaRegenerationDelayThreshold() {
		return (int) this.getAttributeValue(StaminaAttributes.DEPLETED_STAMINA_REGENERATION_DELAY_THRESHOLD);
	}

	@Override
	public int staminaattributes$getStaminaRegenerationDelayThreshold() {
		return (int) this.getAttributeValue(StaminaAttributes.STAMINA_REGENERATION_DELAY_THRESHOLD);
	}

	@Override
	public int staminaattributes$getStaminaTickThreshold() {
		return (int) this.getAttributeValue(StaminaAttributes.STAMINA_TICK_THRESHOLD);
	}

	@Override
	public float staminaattributes$getRegeneratedStamina() {
		return this.staminaattributes$getStaminaRegeneration();
	}

	@Override
	public float staminaattributes$getStaminaRegeneration() {
		return (float) this.getAttributeValue(StaminaAttributes.STAMINA_REGENERATION);
	}

	@Override
	public float staminaattributes$getUnreservedStamina() {
		return this.staminaattributes$getMaxStamina() - ((this.staminaattributes$getMaxStamina() * this.staminaattributes$getReservedStamina()) / 100);
	}

	@Override
	public float staminaattributes$getMaxStamina() {
		return (float) this.getAttributeValue(StaminaAttributes.MAX_STAMINA);
	}

	@Override
	public float staminaattributes$getReservedStamina() {
		return (float) this.getAttributeValue(StaminaAttributes.RESERVED_STAMINA);
	}

	@Override
	public float staminaattributes$getItemUseStaminaCost() {
		return (float) this.getAttributeValue(StaminaAttributes.ITEM_USE_STAMINA_COST);
	}

	@Override
	public float staminaattributes$getSprintingTickStaminaCost() {
		return (float) this.getAttributeValue(StaminaAttributes.SPRINTING_TICK_STAMINA_COST);
	}

	@Override
	public float staminaattributes$getSneakingTickStaminaCost() {
		return (float) this.getAttributeValue(StaminaAttributes.SNEAKING_TICK_STAMINA_COST);
	}

	@Override
	public float staminaattributes$getWalkingTickStaminaCost() {
		return (float) this.getAttributeValue(StaminaAttributes.WALKING_TICK_STAMINA_COST);
	}

	@Override
	public float staminaattributes$getSwimmingTickStaminaCost() {
		return (float) this.getAttributeValue(StaminaAttributes.SWIMMING_TICK_STAMINA_COST);
	}

	@Override
	public float staminaattributes$getWalkingUnderwaterTickStaminaCost() {
		return (float) this.getAttributeValue(StaminaAttributes.WALKING_UNDERWATER_TICK_STAMINA_COST);
	}

	@Override
	public float staminaattributes$getWalkingInWaterTickStaminaCost() {
		return (float) this.getAttributeValue(StaminaAttributes.WALKING_IN_WATER_TICK_STAMINA_COST);
	}

	@Override
	public float staminaattributes$getClimbingTickStaminaCost() {
		return (float) this.getAttributeValue(StaminaAttributes.CLIMBING_TICK_STAMINA_COST);
	}

	@Override
	public float staminaattributes$getJumpingActionStaminaCost() {
		return (float) this.getAttributeValue(StaminaAttributes.JUMPING_ACTION_STAMINA_COST);
	}

	@Override
	public float staminaattributes$getSprintJumpingActionStaminaCost() {
		return (float) this.getAttributeValue(StaminaAttributes.SPRINT_JUMPING_ACTION_STAMINA_COST);
	}

	@Override
	public float staminaattributes$getAttackBlockingActionStaminaCost() {
		return (float) this.getAttributeValue(StaminaAttributes.ATTACK_BLOCKING_ACTION_STAMINA_COST);
	}

	@Override
	public float staminaattributes$getAttackingActionStaminaCost() {
		return (float) this.getAttributeValue(StaminaAttributes.ATTACKING_ACTION_STAMINA_COST);
	}

	@Override
	public float staminaattributes$getBlockBreakingActionStaminaCost() {
		return (float) this.getAttributeValue(StaminaAttributes.BLOCK_BREAKING_ACTION_STAMINA_COST);
	}

	@Override
	public void staminaattributes$addStamina(float amount) {
		float stamina = this.staminaattributes$getStamina();
		this.staminaattributes$setStamina(stamina + amount);
		if (amount < 0) {
			this.staminaRegenerationDelayTimer = this.staminaattributes$getStaminaRegenerationDelayThreshold();
			this.staminaTickTimer = 0;
		}
	}

	@Override
	public float staminaattributes$getStamina() {
		return DataAttachmentHelper.getStamina((LivingEntity) (Object) this);
	}

	@Override
	public void staminaattributes$setStamina(float stamina) {
		DataAttachmentHelper.setStamina((LivingEntity) (Object) this, (float) Mth.clamp(stamina, -100.0, this.staminaattributes$getUnreservedStamina()));
	}

	@Override
	public boolean staminaattributes$delayStaminaTick() {
		return this.delayStaminaTick;
	}

	@Override
	public void staminaattributes$setDelayStaminaTick(boolean delayStaminaTick) {
		this.delayStaminaTick = delayStaminaTick;
	}

	@Override
	public boolean staminaattributes$delayMaxValueApplication() {
		return this.delayMaxValueApplication;
	}

	@Override
	public void staminaattributes$setDelayMaxValueApplication(boolean delayMaxValueApplication) {
		this.delayMaxValueApplication = delayMaxValueApplication;
	}

	@Override
	public boolean staminaattributes$delayedMaxValueApplication() {
		return this.delayedMaxValueApplication;
	}

	@Override
	public void staminaattributes$setDelayedMaxValueApplication(boolean delayedMaxValueApplication) {
		this.delayedMaxValueApplication = delayedMaxValueApplication;
	}
}
