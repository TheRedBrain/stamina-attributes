package com.github.theredbrain.staminaattributes.registry;

import com.github.theredbrain.resourcebarapi.ResourceBarAPIClient;
import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.StaminaAttributesClient;
import com.github.theredbrain.staminaattributes.config.ClientConfig;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class ClientEventsRegistry {

	private static final Identifier[] STAMINA_TEXTURES = {
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_background_left_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_background_middle_segment.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_background_right_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_background_top_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_background_middle_segment.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_background_bottom_end.png"),

			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_left_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_middle_segment.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_right_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_progress_top_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_progress_middle_segment.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_progress_bottom_end.png"),

			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_decrease_animation_left_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_decrease_animation_middle_segment.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_decrease_animation_right_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_progress_decrease_animation_top_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_progress_decrease_animation_middle_segment.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_progress_decrease_animation_bottom_end.png"),

			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_increase_value_left_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_increase_value_middle_segment.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_increase_value_right_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_progress_increase_value_top_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_progress_increase_value_middle_segment.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_progress_increase_value_bottom_end.png"),

			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_increase_animation_left_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_increase_animation_middle_segment.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_increase_animation_right_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_progress_increase_animation_top_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_progress_increase_animation_middle_segment.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_progress_increase_animation_bottom_end.png"),

			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_reserved_left_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_reserved_middle_segment.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_reserved_right_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_reserved_top_end.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_reserved_middle_segment.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_reserved_bottom_end.png"),

			StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_overlay.png"),
			StaminaAttributes.identifier("textures/gui/sprites/hud/vertical_stamina_overlay.png")
	};

	public static void initializeClientEvents() {
		HudRenderCallback.EVENT.register((matrixStack, delta) -> {
			MinecraftClient minecraftClient = MinecraftClient.getInstance();
			PlayerEntity playerEntity = minecraftClient.player;
			ClientConfig clientConfig = StaminaAttributesClient.CLIENT_CONFIG;
			if (clientConfig.show_stamina_bar && playerEntity != null) {
				double stamina = MathHelper.ceil(((StaminaUsingEntity) playerEntity).staminaattributes$getStamina());
				double maxStamina = MathHelper.ceil(((StaminaUsingEntity) playerEntity).staminaattributes$getMaxStamina());

				if (maxStamina > 0 && (stamina < maxStamina || clientConfig.show_full_stamina_bar)) {

					ResourceBarAPIClient.drawResourceBar(
							minecraftClient,
							minecraftClient.textRenderer,
							matrixStack,
							StaminaAttributes.MOD_ID + ":stamina",
							new double[]{-1, -1, 0, 0, 0, 0, 0, 0},
							stamina,
							maxStamina,
							MathHelper.ceil(((StaminaUsingEntity) playerEntity).staminaattributes$getRegeneratedStamina()),
							MathHelper.ceil(((StaminaUsingEntity) playerEntity).staminaattributes$getUnreservedStamina()),
							clientConfig.positionSettings.origin,
							clientConfig.positionSettings.offsets_x,
							clientConfig.positionSettings.offsets_y,
							0,
							(clientConfig.positionSettings.dynamically_adjust_to_armor_bar && playerEntity.getArmor() > 0) ? 10 : 0,
							clientConfig.positionSettings.is_centered,
							STAMINA_TEXTURES,
							clientConfig.fill_direction,
							clientConfig.textureSettings.backgroundTextureSettings.middle_segment_amounts,
							clientConfig.textureSettings.backgroundTextureSettings.horizontalTextureSettings.horizontal_left_end_width,
							clientConfig.textureSettings.backgroundTextureSettings.horizontalTextureSettings.horizontal_middle_segment_width,
							clientConfig.textureSettings.backgroundTextureSettings.horizontalTextureSettings.horizontal_right_end_width,
							clientConfig.textureSettings.backgroundTextureSettings.horizontalTextureSettings.horizontal_height,
							clientConfig.textureSettings.backgroundTextureSettings.verticalTextureSettings.vertical_width,
							clientConfig.textureSettings.backgroundTextureSettings.verticalTextureSettings.vertical_top_end_height,
							clientConfig.textureSettings.backgroundTextureSettings.verticalTextureSettings.vertical_middle_segment_height,
							clientConfig.textureSettings.backgroundTextureSettings.verticalTextureSettings.vertical_bottom_end_height,
							clientConfig.textureSettings.progressTextureSettings.offset_x,
							clientConfig.textureSettings.progressTextureSettings.offset_y,
							clientConfig.textureSettings.progressTextureSettings.middle_segment_amounts,
							clientConfig.textureSettings.progressTextureSettings.horizontalTextureSettings.horizontal_left_end_width,
							clientConfig.textureSettings.progressTextureSettings.horizontalTextureSettings.horizontal_middle_segment_width,
							clientConfig.textureSettings.progressTextureSettings.horizontalTextureSettings.horizontal_right_end_width,
							clientConfig.textureSettings.progressTextureSettings.horizontalTextureSettings.horizontal_height,
							clientConfig.textureSettings.progressTextureSettings.verticalTextureSettings.vertical_width,
							clientConfig.textureSettings.progressTextureSettings.verticalTextureSettings.vertical_top_end_height,
							clientConfig.textureSettings.progressTextureSettings.verticalTextureSettings.vertical_middle_segment_height,
							clientConfig.textureSettings.progressTextureSettings.verticalTextureSettings.vertical_bottom_end_height,
							clientConfig.textureSettings.reservedTextureSettings.offset_x,
							clientConfig.textureSettings.reservedTextureSettings.offset_y,
							clientConfig.textureSettings.reservedTextureSettings.middle_segment_amounts,
							clientConfig.textureSettings.reservedTextureSettings.horizontalTextureSettings.horizontal_left_end_width,
							clientConfig.textureSettings.reservedTextureSettings.horizontalTextureSettings.horizontal_middle_segment_width,
							clientConfig.textureSettings.reservedTextureSettings.horizontalTextureSettings.horizontal_right_end_width,
							clientConfig.textureSettings.reservedTextureSettings.horizontalTextureSettings.horizontal_height,
							clientConfig.textureSettings.reservedTextureSettings.verticalTextureSettings.vertical_width,
							clientConfig.textureSettings.reservedTextureSettings.verticalTextureSettings.vertical_top_end_height,
							clientConfig.textureSettings.reservedTextureSettings.verticalTextureSettings.vertical_middle_segment_height,
							clientConfig.textureSettings.reservedTextureSettings.verticalTextureSettings.vertical_bottom_end_height,
							clientConfig.show_current_value_overlay,
							clientConfig.textureSettings.overlayTextureSettings.offset_x,
							clientConfig.textureSettings.overlayTextureSettings.offset_y,
							clientConfig.textureSettings.overlayTextureSettings.horizontal_width,
							clientConfig.textureSettings.overlayTextureSettings.horizontal_height,
							clientConfig.textureSettings.overlayTextureSettings.vertical_width,
							clientConfig.textureSettings.overlayTextureSettings.vertical_height,
							clientConfig.enable_smooth_animation,
							clientConfig.animationSettings.animation_interval,
							clientConfig.animationSettings.max_value_change_is_animated,
							clientConfig.show_number,
							clientConfig.numberSettings.show_max_value,
							clientConfig.numberSettings.offset_x,
							clientConfig.numberSettings.offset_y - ((clientConfig.positionSettings.dynamically_adjust_to_armor_bar && playerEntity.getArmor() > 0) ? 10 : 0),
							clientConfig.numberSettings.color.toInt()
					);
				}
			}
		});
	}
}