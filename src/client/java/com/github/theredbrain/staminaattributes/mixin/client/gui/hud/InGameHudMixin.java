package com.github.theredbrain.staminaattributes.mixin.client.gui.hud;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
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
	private static final Identifier[] STAMINA_TEXTURES = {
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_background_left_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_background_middle_segment.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_background_right_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_left_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_middle_segment.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_right_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_decrease_animation_left_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_decrease_animation_middle_segment.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_decrease_animation_right_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_increase_animation_left_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_increase_animation_middle_segment.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_increase_animation_right_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_overlay.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_reserved_stamina_left_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_reserved_stamina_middle_segment.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_reserved_stamina_right_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_background_top_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_background_middle_segment.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_background_bottom_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_progress_top_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_progress_middle_segment.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_progress_bottom_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_progress_decrease_animation_top_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_progress_decrease_animation_middle_segment.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_progress_decrease_animation_bottom_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_progress_increase_animation_top_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_progress_increase_animation_middle_segment.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_progress_increase_animation_bottom_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_overlay.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_reserved_stamina_top_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_reserved_stamina_middle_segment.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_reserved_stamina_bottom_end.png")
	};

	@Inject(method = "renderStatusBars", at = @At("RETURN"))
	private void staminaattributes$renderStatusBars(DrawContext context, CallbackInfo ci) {
		if (StaminaAttributesClient.clientConfig.show_stamina_bar) {

			this.staminaAttributes$renderStaminaBar(context, this.getCameraPlayer());
		}
	}

	@Unique
	private void staminaAttributes$renderStaminaBar(DrawContext context, PlayerEntity player) {

		var clientConfig = StaminaAttributesClient.clientConfig;

		int stamina = MathHelper.ceil(((StaminaUsingEntity) player).staminaattributes$getStamina());
		int maxStamina = MathHelper.ceil(((StaminaUsingEntity) player).staminaattributes$getMaxStamina());

		if (maxStamina > 0 && (stamina < maxStamina || clientConfig.show_full_stamina_bar)) {

			StaminaAttributesClient.drawResourceBar(
					this.client,
					this.getTextRenderer(),
					context,
					"staminaattributes:stamina",
					stamina,
					maxStamina,
					MathHelper.ceil(((StaminaUsingEntity) player).staminaattributes$getRegeneratedStamina()),
					0,
					clientConfig.origin,
					clientConfig.offset_x,
					clientConfig.offset_y - (clientConfig.dynamically_adjust_to_armor_bar ? 10 : 0),
					STAMINA_TEXTURES,
					clientConfig.fill_direction,
					clientConfig.background_middle_segment_amount,
					clientConfig.horizontal_background_left_end_width,
					clientConfig.horizontal_background_middle_segment_width,
					clientConfig.horizontal_background_right_end_width,
					clientConfig.horizontal_background_height,
					clientConfig.vertical_background_width,
					clientConfig.vertical_background_top_end_height,
					clientConfig.vertical_background_middle_segment_height,
					clientConfig.vertical_background_bottom_end_height,
					clientConfig.progress_offset_x,
					clientConfig.progress_offset_y,
					clientConfig.progress_middle_segment_amount,
					clientConfig.horizontal_progress_left_end_width,
					clientConfig.horizontal_progress_middle_segment_width,
					clientConfig.horizontal_progress_right_end_width,
					clientConfig.horizontal_progress_height,
					clientConfig.vertical_progress_width,
					clientConfig.vertical_progress_top_end_height,
					clientConfig.vertical_progress_middle_segment_height,
					clientConfig.vertical_progress_bottom_end_height,
					clientConfig.reserved_offset_x,
					clientConfig.reserved_offset_y,
					clientConfig.reserved_middle_segment_amount,
					clientConfig.horizontal_reserved_left_end_width,
					clientConfig.horizontal_reserved_middle_segment_width,
					clientConfig.horizontal_reserved_right_end_width,
					clientConfig.horizontal_reserved_height,
					clientConfig.vertical_reserved_width,
					clientConfig.vertical_reserved_top_end_height,
					clientConfig.vertical_reserved_middle_segment_height,
					clientConfig.vertical_reserved_bottom_end_height,
					clientConfig.show_current_value_overlay,
					clientConfig.overlay_offset_x,
					clientConfig.overlay_offset_y,
					clientConfig.horizontal_overlay_width,
					clientConfig.horizontal_overlay_height,
					clientConfig.vertical_overlay_width,
					clientConfig.vertical_overlay_height,
					clientConfig.enable_smooth_animation,
					clientConfig.animation_interval,
					clientConfig.max_value_change_is_animated,
					clientConfig.show_number,
					clientConfig.show_max_value,
					clientConfig.number_offset_x,
					clientConfig.number_offset_y - (clientConfig.dynamically_adjust_to_armor_bar ? 10 : 0),
					clientConfig.number_color
			);
		}
	}
}
