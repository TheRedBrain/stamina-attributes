package com.github.theredbrain.staminaattributes.mixin.client.gui.hud;

import com.github.theredbrain.staminaattributes.StaminaAttributesClient;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

	@Shadow
	protected abstract PlayerEntity getCameraPlayer();

	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow
	public abstract TextRenderer getTextRenderer();

	@Unique
	private static final Identifier BOSS_BAR_GREEN_PROGRESS_TEXTURE = Identifier.ofVanilla("textures/gui/sprites/boss_bar/green_progress.png");

	@Unique
	private static final Identifier BOSS_BAR_GREEN_BACKGROUND_TEXTURE = Identifier.ofVanilla("textures/gui/sprites/boss_bar/green_background.png");

	@Unique
	private static final Identifier NOTCHED_20_PROGRESS_TEXTURE = Identifier.ofVanilla("textures/gui/sprites/boss_bar/notched_20_progress.png");

	@Unique
	private int oldNormalizedStaminaRatio = -1;

	@Unique
	private int oldMaxStamina = -1;

	@Unique
	private int staminaBarAnimationCounter = 0;

	@Inject(method = "renderStatusBars", at = @At("RETURN"))
	private void staminaattributes$renderStatusBars(DrawContext context, CallbackInfo ci) {
		var clientConfig = StaminaAttributesClient.clientConfig;
		if (clientConfig.show_stamina_bar) {
			PlayerEntity playerEntity = this.getCameraPlayer();
			if (playerEntity != null) {
				int stamina = MathHelper.ceil(((StaminaUsingEntity) playerEntity).staminaattributes$getStamina());
				int maxStamina = MathHelper.ceil(((StaminaUsingEntity) playerEntity).staminaattributes$getMaxStamina());

				int attributeBarX = context.getScaledWindowWidth() / 2 + clientConfig.stamina_bar_x_offset;
				int attributeBarY = context.getScaledWindowHeight() + clientConfig.stamina_bar_y_offset - (clientConfig.dynamically_adjust_to_armor_bar && playerEntity.getArmor() > 0 ? 10 : 0);
				int stamina_bar_additional_length = clientConfig.stamina_bar_additional_length;
				int attributeBarNumberX;
				int attributeBarNumberY;
				int normalizedStaminaRatio = (int) (((double) stamina / Math.max(maxStamina, 1)) * (5 + clientConfig.stamina_bar_additional_length + 5));

				if (this.oldMaxStamina != maxStamina) {
					this.oldMaxStamina = maxStamina;
					this.oldNormalizedStaminaRatio = normalizedStaminaRatio;
				}

				this.staminaBarAnimationCounter = this.staminaBarAnimationCounter + Math.max(1, MathHelper.ceil(((StaminaUsingEntity) playerEntity).staminaattributes$getRegeneratedStamina()));

				if (this.oldNormalizedStaminaRatio != normalizedStaminaRatio && this.staminaBarAnimationCounter > Math.max(0, clientConfig.stamina_bar_animation_interval)) {
					boolean reduceOldRatio = this.oldNormalizedStaminaRatio > normalizedStaminaRatio;
					this.oldNormalizedStaminaRatio = this.oldNormalizedStaminaRatio + (reduceOldRatio ? -1 : 1);
					this.staminaBarAnimationCounter = 0;
				}

				if (maxStamina > 0 && (stamina < maxStamina || clientConfig.show_full_stamina_bar)) {
					this.client.getProfiler().push("stamina_bar");

					// background
					context.drawTexture(BOSS_BAR_GREEN_BACKGROUND_TEXTURE, attributeBarX, attributeBarY, 0, 0, 5, 5, 182, 5);
					if (stamina_bar_additional_length > 0) {
						for (int i = 0; i < stamina_bar_additional_length; i++) {
							context.drawTexture(BOSS_BAR_GREEN_BACKGROUND_TEXTURE, attributeBarX + 5 + i, attributeBarY, 5, 0, 1, 5, 182, 5);
						}
					}
					context.drawTexture(BOSS_BAR_GREEN_BACKGROUND_TEXTURE, attributeBarX + 5 + stamina_bar_additional_length, attributeBarY, 177, 0, 5, 5, 182, 5);

					// foreground
					int displayRatio = clientConfig.enable_smooth_animation ? this.oldNormalizedStaminaRatio : normalizedStaminaRatio;
					if (displayRatio > 0) {
						this.client.getProfiler().swap("stamina_bar_foreground");
						context.drawTexture(BOSS_BAR_GREEN_PROGRESS_TEXTURE, attributeBarX, attributeBarY, 0, 0, Math.min(5, displayRatio), 5, 182, 5);
						if (displayRatio > 5) {
							if (stamina_bar_additional_length > 0) {
								for (int i = 5; i < Math.min(5 + stamina_bar_additional_length, displayRatio); i++) {
									context.drawTexture(BOSS_BAR_GREEN_PROGRESS_TEXTURE, attributeBarX + i, attributeBarY, 5, 0, 1, 5, 182, 5);
								}
							}
						}
						if (displayRatio > (5 + stamina_bar_additional_length)) {
							context.drawTexture(BOSS_BAR_GREEN_PROGRESS_TEXTURE, attributeBarX + 5 + stamina_bar_additional_length, attributeBarY, 177, 0, Math.min(5, displayRatio - 5 - stamina_bar_additional_length), 5, 182, 5);
						}
					}

					// overlay
					if (clientConfig.enable_smooth_animation && clientConfig.show_current_value_overlay) {
						if (stamina > 0 && stamina < maxStamina) {
							this.client.getProfiler().swap("stamina_bar_overlay");
							context.drawTexture(NOTCHED_20_PROGRESS_TEXTURE, attributeBarX + normalizedStaminaRatio - 2, attributeBarY + 1, 7, 1, 5, 3, 182, 5);
						}
					}
					if (clientConfig.show_stamina_bar_number) {
						this.client.getProfiler().swap("stamina_bar_number");
						String string = String.valueOf(stamina);
						attributeBarNumberX = (context.getScaledWindowWidth() - this.getTextRenderer().getWidth(string)) / 2 + clientConfig.stamina_bar_number_x_offset;
						attributeBarNumberY = context.getScaledWindowHeight() + clientConfig.stamina_bar_number_y_offset - (clientConfig.dynamically_adjust_to_armor_bar && playerEntity.getArmor() > 0 ? 10 : 0);
						context.drawText(this.getTextRenderer(), string, attributeBarNumberX + 1, attributeBarNumberY, 0, false);
						context.drawText(this.getTextRenderer(), string, attributeBarNumberX - 1, attributeBarNumberY, 0, false);
						context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY + 1, 0, false);
						context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY - 1, 0, false);
						context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY, clientConfig.stamina_bar_number_color, false);
					}
				}

				this.client.getProfiler().pop();
			}
		}
	}
}
