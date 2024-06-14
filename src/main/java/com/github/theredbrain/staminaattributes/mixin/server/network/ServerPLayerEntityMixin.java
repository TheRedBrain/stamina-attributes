package com.github.theredbrain.staminaattributes.mixin.server.network;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPLayerEntityMixin extends PlayerEntity implements StaminaUsingEntity {

    public ServerPLayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;addExhaustion(F)V", ordinal = 0))
    private void staminaattributes$increaseTravelMotionStats_swimming(CallbackInfo ci) {
        if (!this.getAbilities().invulnerable) {
            this.staminaattributes$addStamina(-StaminaAttributes.serverConfig.stamina_cost_swimming);
        }
    }

    @Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;addExhaustion(F)V", ordinal = 1))
    private void staminaattributes$increaseTravelMotionStats_walk_underwater(CallbackInfo ci) {
        if (!this.getAbilities().invulnerable) {
            this.staminaattributes$addStamina(-StaminaAttributes.serverConfig.stamina_cost_walking_underwater);
        }
    }

    @Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;addExhaustion(F)V", ordinal = 2))
    private void staminaattributes$increaseTravelMotionStats_walk_in_water(CallbackInfo ci) {
        if (!this.getAbilities().invulnerable) {
            this.staminaattributes$addStamina(-StaminaAttributes.serverConfig.stamina_cost_walking_in_water);
        }
    }

    @Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;increaseStat(Lnet/minecraft/util/Identifier;I)V", ordinal = 3))
    private void staminaattributes$increaseTravelMotionStats_climbing(CallbackInfo ci) {
        if (!this.getAbilities().invulnerable) {
            this.staminaattributes$addStamina(-StaminaAttributes.serverConfig.stamina_cost_climbing);
        }
    }

    @Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;addExhaustion(F)V", ordinal = 3))
    private void staminaattributes$increaseTravelMotionStats_sprinting(CallbackInfo ci) {
        if (!this.getAbilities().invulnerable) {
            this.staminaattributes$addStamina(-StaminaAttributes.serverConfig.stamina_cost_sprinting);
        }
    }

    @Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;addExhaustion(F)V", ordinal = 4))
    private void staminaattributes$increaseTravelMotionStats_sneaking(CallbackInfo ci) {
        if (!this.getAbilities().invulnerable) {
            this.staminaattributes$addStamina(-StaminaAttributes.serverConfig.stamina_cost_sneaking);
        }
    }

    @Inject(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;addExhaustion(F)V", ordinal = 5))
    private void staminaattributes$increaseTravelMotionStats_walking(CallbackInfo ci) {
        if (!this.getAbilities().invulnerable) {
            this.staminaattributes$addStamina(-StaminaAttributes.serverConfig.stamina_cost_walking);
        }
    }

}
