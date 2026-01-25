package com.github.theredbrain.staminaattributes.mixin.client;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Shadow
	@Nullable
	public LocalPlayer player;

	@Unique
	private boolean attackSwingAllowed = true;

	@WrapOperation(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;attack(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;)V"))
	private void staminaattributes$doAttack_wrap_attackEntity(MultiPlayerGameMode instance, Player player, Entity target, Operation<Void> original) {
		if (!StaminaAttributes.SERVER_CONFIG.attacking_requires_stamina || ((StaminaUsingEntity) player).staminaattributes$getStamina() > 0) {
			original.call(instance, player, target);
			this.attackSwingAllowed = true;
		} else {
			player.displayClientMessage(Component.translatable("hud.message.staminaTooLow"), true);
			this.attackSwingAllowed = false;
		}
	}

	@WrapOperation(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;startDestroyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z"))
	private boolean staminaattributes$doAttack_wrap_attackBlock(MultiPlayerGameMode instance, BlockPos pos, Direction direction, Operation<Boolean> original) {
		if (!StaminaAttributes.SERVER_CONFIG.block_breaking_requires_stamina || (this.player != null && ((StaminaUsingEntity) this.player).staminaattributes$getStamina() > 0)) {
			this.attackSwingAllowed = true;
		} else {
			if (this.player != null) {
				this.player.displayClientMessage(Component.translatable("hud.message.staminaTooLow"), true);
			}
			this.attackSwingAllowed = false;
		}
		return original.call(instance, pos, direction);
	}

	@WrapOperation(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;swing(Lnet/minecraft/world/InteractionHand;)V"))
	private void staminaattributes$doAttack_wrap_swingHand(LocalPlayer instance, InteractionHand hand, Operation<Void> original) {
		if (this.attackSwingAllowed) {
			original.call(instance, hand);
		}
	}
}
