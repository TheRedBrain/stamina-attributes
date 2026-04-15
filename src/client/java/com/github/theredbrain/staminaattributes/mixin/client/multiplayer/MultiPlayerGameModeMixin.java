package com.github.theredbrain.staminaattributes.mixin.client.multiplayer;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.config.ServerConfig;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

	@Shadow
	@Final
	private Minecraft minecraft;

	@Shadow
	private BlockPos destroyBlockPos;

	@WrapOperation(method = "continueDestroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z"))
	public boolean staminaattributes$wrap_isAir(BlockState instance, Operation<Boolean> original) {
		ServerConfig serverConfig = StaminaAttributes.SERVER_CONFIG;
		boolean bl = (serverConfig.block_breaking_requires_stamina && this.minecraft.player != null && ((StaminaUsingEntity) this.minecraft.player).staminaattributes$getBlockBreakingActionStaminaCost() > 0 && ((StaminaUsingEntity) this.minecraft.player).staminaattributes$getStamina() <= 0);
		if (bl && this.minecraft.level != null) {
			this.minecraft.player.sendOverlayMessage(Component.translatable("hud.message.staminaTooLow"));
			this.minecraft.level.destroyBlockProgress(this.minecraft.player.getId(), this.destroyBlockPos, -1);
		}
		return original.call(instance) || bl;
	}
}
