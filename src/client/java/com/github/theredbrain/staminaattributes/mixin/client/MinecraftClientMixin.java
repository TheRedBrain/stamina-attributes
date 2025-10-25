package com.github.theredbrain.staminaattributes.mixin.client;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Shadow
	@Nullable
	public ClientPlayerEntity player;

	@Unique
	private boolean attackSwingAllowed = true;

	@WrapOperation(method = "doAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;attackEntity(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;)V"))
	private void staminaattributes$doAttack_wrap_attackEntity(ClientPlayerInteractionManager instance, PlayerEntity player, Entity target, Operation<Void> original) {
		if (!StaminaAttributes.SERVER_CONFIG.attacking_requires_stamina || ((StaminaUsingEntity) player).staminaattributes$getStamina() > 0) {
			original.call(instance, player, target);
			this.attackSwingAllowed = true;
		} else {
			player.sendMessage(Text.translatable("hud.message.staminaTooLow"), true);
			this.attackSwingAllowed = false;
		}
	}

	@WrapOperation(method = "doAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;attackBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z"))
	private boolean staminaattributes$doAttack_wrap_attackBlock(ClientPlayerInteractionManager instance, BlockPos pos, Direction direction, Operation<Boolean> original) {
		if (!StaminaAttributes.SERVER_CONFIG.block_breaking_requires_stamina || (this.player != null && ((StaminaUsingEntity) this.player).staminaattributes$getStamina() > 0)) {
			this.attackSwingAllowed = true;
		} else {
			if (this.player != null) {
				this.player.sendMessage(Text.translatable("hud.message.staminaTooLow"), true);
			}
			this.attackSwingAllowed = false;
		}
		return original.call(instance, pos, direction);
	}

	@WrapOperation(method = "doAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;swingHand(Lnet/minecraft/util/Hand;)V"))
	private void staminaattributes$doAttack_wrap_swingHand(ClientPlayerEntity instance, Hand hand, Operation<Void> original) {
		if (this.attackSwingAllowed) {
			original.call(instance, hand);
		}
	}
}
