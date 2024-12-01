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
			ClientConfig clientConfig = StaminaAttributesClient.clientConfig;
			if (clientConfig.show_stamina_bar && playerEntity != null) {
				int stamina = MathHelper.ceil(((StaminaUsingEntity) playerEntity).staminaattributes$getStamina());
				int maxStamina = MathHelper.ceil(((StaminaUsingEntity) playerEntity).staminaattributes$getMaxStamina());

				if (maxStamina > 0 && (stamina < maxStamina || clientConfig.show_full_stamina_bar)) {

					ResourceBarAPIClient.drawResourceBar(
							minecraftClient,
							minecraftClient.textRenderer,
							matrixStack,
							StaminaAttributes.MOD_ID + ":stamina",
							new int[]{-1, -1, 0, 0, 0, 0, 0, 0},
							stamina,
							maxStamina,
							MathHelper.ceil(((StaminaUsingEntity) playerEntity).staminaattributes$getRegeneratedStamina()),
							0,
							clientConfig.origin,
							clientConfig.offsets_x,
							clientConfig.offsets_y,
							0,
							(clientConfig.dynamically_adjust_to_armor_bar && playerEntity.getArmor() > 0) ? 10 : 0,
							clientConfig.is_centered,
							STAMINA_TEXTURES,
							clientConfig.fill_direction,
							clientConfig.background_middle_segment_amounts,
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
							clientConfig.progress_middle_segment_amounts,
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
							clientConfig.reserved_middle_segment_amounts,
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
							clientConfig.number_offset_y - ((clientConfig.dynamically_adjust_to_armor_bar && playerEntity.getArmor() > 0) ? 10 : 0),
							clientConfig.number_color
					);
				}
			}
		});
	}
}