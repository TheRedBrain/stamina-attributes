package com.github.theredbrain.staminaattributes.mixin.entity;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements StaminaUsingEntity {

    @Shadow
    public abstract double getAttributeValue(EntityAttribute attribute);

    @Unique
    private int staminaTickTimer = 0;
    @Unique
    private int staminaRegenerationDelayTimer = 0;
    @Unique
    private boolean delayStaminaRegeneration = false;

    @Unique
    private static final TrackedData<Float> STAMINA = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.FLOAT);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initDataTracker", at = @At("RETURN"))
    protected void staminaattributes$initDataTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(STAMINA, 0.0F);

    }

    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void staminaattributes$createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue()
                .add(StaminaAttributes.STAMINA_REGENERATION)
                .add(StaminaAttributes.MAX_STAMINA)
                .add(StaminaAttributes.STAMINA_REGENERATION_DELAY_THRESHOLD)
                .add(StaminaAttributes.STAMINA_TICK_THRESHOLD)
        ;
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void staminaattributes$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {

        if (nbt.contains("stamina", NbtElement.NUMBER_TYPE)) {
            this.staminaattributes$setStamina(nbt.getFloat("stamina"));
        }

    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void staminaattributes$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {

        nbt.putFloat("stamina", this.staminaattributes$getStamina());

    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void staminaattributes$tick(CallbackInfo ci) {
        if (!this.getWorld().isClient) {

            this.staminaTickTimer++;

            if (this.staminaattributes$getStamina() <= 0 && this.delayStaminaRegeneration) {
                this.staminaRegenerationDelayTimer = 0;
                this.delayStaminaRegeneration = false;
            }
            if (this.staminaattributes$getStamina() > 0 && !this.delayStaminaRegeneration) {
                this.delayStaminaRegeneration = true;
            }
            if (this.staminaRegenerationDelayTimer <= this.staminaattributes$getStaminaRegenerationDelayThreshold()) {
                this.staminaRegenerationDelayTimer++;
            }

            if (this.staminaTickTimer >= this.staminaattributes$getStaminaTickThreshold() && this.staminaRegenerationDelayTimer >= this.staminaattributes$getStaminaRegenerationDelayThreshold()) {
                if (this.staminaattributes$getStamina() < this.staminaattributes$getMaxStamina()) {
                    this.staminaattributes$addStamina(this.staminaattributes$getRegeneratedStamina());
                } else if (this.staminaattributes$getStamina() > this.staminaattributes$getMaxStamina()) {
                    this.staminaattributes$setStamina(this.staminaattributes$getMaxStamina());
                }
                this.staminaTickTimer = 0;
            }

        }
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
    public float staminaattributes$getMaxStamina() {
        return (float) this.getAttributeValue(StaminaAttributes.MAX_STAMINA);
    }

    @Override
    public void staminaattributes$addStamina(float amount) {
        float f = this.staminaattributes$getStamina();
        this.staminaattributes$setStamina(f + amount);
        if (amount < 0) {
            this.staminaTickTimer = 0;
        }
    }

    @Override
    public float staminaattributes$getStamina() {
        return this.dataTracker.get(STAMINA);
    }

    @Override
    public void staminaattributes$setStamina(float stamina) {
        this.dataTracker.set(STAMINA, MathHelper.clamp(stamina, -100, this.staminaattributes$getMaxStamina()));
    }

}
