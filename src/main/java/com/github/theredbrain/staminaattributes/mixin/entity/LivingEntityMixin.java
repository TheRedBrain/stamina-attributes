package com.github.theredbrain.staminaattributes.mixin.entity;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
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
	public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

	@Shadow
	public abstract boolean isUsingItem();

	@Shadow
	public abstract void stopUsingItem();

	@Shadow
	protected ItemStack activeItemStack;
	@Unique
	private int staminaTickTimer = 0;
	@Unique
	private int depletedStaminaRegenerationDelayTimer = 0;
	@Unique
	private int staminaRegenerationDelayTimer = 0;
	@Unique
	private boolean delayStaminaRegeneration = false;
	@Unique
	private Float oldStamina = null;
	@Unique
	private boolean applyOldStamina = true;
	@Unique
	private boolean applyMaxStamina = false;

	@Unique
	private static final TrackedData<Float> STAMINA = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.FLOAT);

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "initDataTracker", at = @At("RETURN"))
	protected void staminaattributes$initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
		builder.add(STAMINA, 10.0F);

	}

	@Inject(method = "createLivingAttributes", at = @At("RETURN"))
	private static void staminaattributes$createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
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
		;
	}

	@Inject(method = "readCustomData", at = @At("HEAD"))
	public void staminaattributes$readCustomDataFromNbt_head(ReadView view, CallbackInfo ci) {
		float stamina;
		if (view.contains("stamina")) {
			stamina = view.getFloat("stamina", this.staminaattributes$getMaxStamina());
		} else {
			stamina = Float.MIN_VALUE;
		}
		if (stamina != Float.MIN_VALUE) {
			this.oldStamina = stamina;
		}
	}

	@Inject(method = "readCustomData", at = @At("TAIL"))
	public void staminaattributes$readCustomDataFromNbt_tail(ReadView view, CallbackInfo ci) {

		if (view.contains("stamina")) {
			this.staminaattributes$setStamina(view.getFloat("stamina", this.staminaattributes$getMaxStamina()));
		}

	}

	@Inject(method = "writeCustomData", at = @At("TAIL"))
	public void staminaattributes$writeCustomDataToNbt(WriteView view, CallbackInfo ci) {

		view.putFloat("stamina", this.staminaattributes$getStamina());

	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void staminaattributes$tick(CallbackInfo ci) {
		if (!this.getEntityWorld().isClient()) {

			this.staminaTickTimer++;

			if (this.staminaattributes$getStamina() <= 0 && this.delayStaminaRegeneration) {
				this.depletedStaminaRegenerationDelayTimer = 0;
				this.staminaRegenerationDelayTimer = this.staminaattributes$getStaminaRegenerationDelayThreshold();
				this.delayStaminaRegeneration = false;
			}
			if (this.staminaattributes$getStamina() > 0 && !this.delayStaminaRegeneration) {
				this.delayStaminaRegeneration = true;
			}
			if (this.depletedStaminaRegenerationDelayTimer <= this.staminaattributes$getDepletedStaminaRegenerationDelayThreshold()) {
				this.depletedStaminaRegenerationDelayTimer++;
			}
			if (this.staminaRegenerationDelayTimer <= this.staminaattributes$getStaminaRegenerationDelayThreshold()) {
				this.staminaRegenerationDelayTimer++;
			}

			if (
					this.staminaTickTimer > this.staminaattributes$getStaminaTickThreshold()
							&& this.depletedStaminaRegenerationDelayTimer > this.staminaattributes$getDepletedStaminaRegenerationDelayThreshold()
							&& this.staminaRegenerationDelayTimer > this.staminaattributes$getStaminaRegenerationDelayThreshold()
			) {
				if (this.staminaattributes$getStamina() < this.staminaattributes$getUnreservedStamina()) {
					this.staminaattributes$addStamina(this.staminaattributes$getRegeneratedStamina());
				}
				if (this.staminaattributes$getStamina() > this.staminaattributes$getUnreservedStamina() || this.staminaattributes$getRegeneratedStamina() < 0) {
					this.staminaattributes$setStamina(this.staminaattributes$getUnreservedStamina());
				}
				this.staminaTickTimer = 0;
			}

			if (this.isUsingItem() && this.activeItemStack.isIn(StaminaAttributes.CONTINUOUS_USING_COSTS_STAMINA) && this.staminaattributes$getItemUseStaminaCost() > 0 && this.staminaattributes$getStamina() <= 0) {
				if (((LivingEntity) (Object) this) instanceof PlayerEntity playerEntity) {
					playerEntity.getItemCooldownManager().set(this.activeItemStack, StaminaAttributes.SERVER_CONFIG.item_use_cooldown_when_no_stamina);
				}
				this.stopUsingItem();
			}
			if (this.applyOldStamina) {
				if (this.applyMaxStamina) {
					this.oldStamina = this.staminaattributes$getUnreservedStamina();
					this.applyMaxStamina = false;
				}
				if (this.oldStamina != null) {
					this.staminaattributes$setStamina(this.oldStamina);
					this.oldStamina = null;
				}
			} else {
				this.applyOldStamina = true;
			}
		}
	}

	@Inject(method = "tickItemStackUsage", at = @At("HEAD"))
	protected void staminaattributes$tickItemStackUsage(ItemStack stack, CallbackInfo ci) {
		if (stack.isIn(StaminaAttributes.CONTINUOUS_USING_COSTS_STAMINA) && staminaattributes$getItemUseStaminaCost() > 0 && staminaattributes$getStamina() > 0) {
			this.staminaattributes$addStamina(-staminaattributes$getItemUseStaminaCost());
		}
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
	public void staminaattributes$addStamina(float amount) {
		float f = this.staminaattributes$getStamina();
		this.staminaattributes$setStamina(f + amount);
		if (amount < 0) {
			this.staminaRegenerationDelayTimer = 0;
			this.staminaTickTimer = 0;
		}
	}

	@Override
	public float staminaattributes$getStamina() {
		return this.dataTracker.get(STAMINA);
	}

	@Override
	public void staminaattributes$setStamina(float stamina) {
		this.dataTracker.set(STAMINA, MathHelper.clamp(stamina, -100, this.staminaattributes$getUnreservedStamina()));
	}

	@Override
	public void staminaattributes$setApplyOldStamina(boolean applyOldStamina) {
		this.applyOldStamina = applyOldStamina;
	}

	@Override
	public void staminaattributes$setApplyMaxStamina(boolean applyMaxStamina) {
		this.applyMaxStamina = applyMaxStamina;
	}
}
