package com.github.theredbrain.staminaattributes;

import com.github.theredbrain.staminaattributes.config.ClientConfig;
import com.github.theredbrain.staminaattributes.config.ClientConfigWrapper;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class StaminaAttributesClient implements ClientModInitializer {
	public static ClientConfig clientConfig;

	public static Map<String, int[]> CACHED_RESOURCE_BAR_VALUES = new HashMap<>();

	@Override
	public void onInitializeClient() {
		// Config
		AutoConfig.register(ClientConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		clientConfig = ((ClientConfigWrapper) AutoConfig.getConfigHolder(ClientConfigWrapper.class).getConfig()).client;

		// Packets
		ClientPlayNetworking.registerGlobalReceiver(StaminaAttributes.ServerConfigSyncPacket.PACKET_ID, (payload, context) -> {
			StaminaAttributes.serverConfig = payload.serverConfig();
		});
	}

	public static void drawResourceBar(
			MinecraftClient client,
			TextRenderer textRenderer,
			DrawContext context,
			String identifier_string,
			int current_value,
			int max_value,
			int current_value_reduction,
			StaminaAttributes.ResourceBarOrigin resource_bar_origin,
			int element_offset_x,
			int element_offset_y,
			Identifier[] texture_ids,
			StaminaAttributes.ResourceBarFillDirection resource_bar_fill_direction,
			int background_additional_middle_segment_amount,
			int horizontal_background_left_end_width,
			int horizontal_background_middle_segment_width,
			int horizontal_background_right_end_width,
			int horizontal_background_height,
			int vertical_background_width,
			int vertical_background_top_end_height,
			int vertical_background_middle_segment_height,
			int vertical_background_bottom_end_height,
			int progress_offset_x,
			int progress_offset_y,
			int progress_additional_middle_segment_amount,
			int horizontal_progress_left_end_width,
			int horizontal_progress_middle_segment_width,
			int horizontal_progress_right_end_width,
			int horizontal_progress_height,
			int vertical_progress_width,
			int vertical_progress_top_end_height,
			int vertical_progress_middle_segment_height,
			int vertical_progress_bottom_end_height,
			boolean show_current_value_overlay,
			int overlay_offset_x,
			int overlay_offset_y,
			int horizontal_overlay_width,
			int horizontal_overlay_height,
			int vertical_overlay_width,
			int vertical_overlay_height,
			boolean enable_smooth_animation,
			int animation_interval,
			boolean max_value_change_is_animated,
			boolean show_number,
			boolean show_max_value,
			int number_offset_x,
			int number_offset_y,
			int resource_bar_number_color
	) {

		int progressBarLength;
		int backgroundTextureHeight;
		int backgroundTextureWidth;
		int backgroundMiddleSectionLength;
		int progressTextureHeight;
		int progressTextureWidth;
		int progressMiddleSectionLength;
		int originX;
		int originY;
		if (resource_bar_origin == StaminaAttributes.ResourceBarOrigin.TOP_MIDDLE) {
			originX = context.getScaledWindowWidth() / 2;
			originY = 0;
		} else if (resource_bar_origin == StaminaAttributes.ResourceBarOrigin.TOP_RIGHT) {
			originX = context.getScaledWindowWidth();
			originY = 0;
		} else if (resource_bar_origin == StaminaAttributes.ResourceBarOrigin.MIDDLE_LEFT) {
			originX = 0;
			originY = context.getScaledWindowHeight() / 2;
		} else if (resource_bar_origin == StaminaAttributes.ResourceBarOrigin.MIDDLE_MIDDLE) {
			originX = context.getScaledWindowWidth() / 2;
			originY = context.getScaledWindowHeight() / 2;
		} else if (resource_bar_origin == StaminaAttributes.ResourceBarOrigin.MIDDLE_RIGHT) {
			originX = context.getScaledWindowWidth();
			originY = context.getScaledWindowHeight() / 2;
		} else if (resource_bar_origin == StaminaAttributes.ResourceBarOrigin.BOTTOM_LEFT) {
			originX = 0;
			originY = context.getScaledWindowHeight();
		} else if (resource_bar_origin == StaminaAttributes.ResourceBarOrigin.BOTTOM_MIDDLE) {
			originX = context.getScaledWindowWidth() / 2;
			originY = context.getScaledWindowHeight();
		} else if (resource_bar_origin == StaminaAttributes.ResourceBarOrigin.BOTTOM_RIGHT) {
			originX = context.getScaledWindowWidth();
			originY = context.getScaledWindowHeight();
		} else {
			originX = 0;
			originY = 0;
		}
		int elementX = originX + element_offset_x;
		int elementY = originY + element_offset_y;

		// region variable calculation
		if (resource_bar_fill_direction == StaminaAttributes.ResourceBarFillDirection.BOTTOM_TO_TOP || resource_bar_fill_direction == StaminaAttributes.ResourceBarFillDirection.TOP_TO_BOTTOM) {
			backgroundTextureHeight = vertical_background_top_end_height + vertical_background_middle_segment_height + vertical_background_bottom_end_height;
			backgroundTextureWidth = vertical_background_width;
			progressTextureHeight = vertical_progress_top_end_height + vertical_progress_middle_segment_height + vertical_progress_bottom_end_height;
			progressTextureWidth = vertical_progress_width;
			backgroundMiddleSectionLength = background_additional_middle_segment_amount * vertical_background_middle_segment_height;
			progressMiddleSectionLength = progress_additional_middle_segment_amount * vertical_progress_middle_segment_height;
			progressBarLength = vertical_progress_top_end_height + progressMiddleSectionLength + vertical_progress_bottom_end_height;
		} else {
			backgroundTextureHeight = horizontal_background_height;
			backgroundTextureWidth = horizontal_background_left_end_width + horizontal_background_middle_segment_width + horizontal_background_right_end_width;
			progressTextureHeight = horizontal_progress_height;
			progressTextureWidth = horizontal_progress_left_end_width + horizontal_progress_middle_segment_width + horizontal_progress_right_end_width;
			backgroundMiddleSectionLength = background_additional_middle_segment_amount * horizontal_background_middle_segment_width;
			progressMiddleSectionLength = progress_additional_middle_segment_amount * horizontal_progress_middle_segment_width;
			progressBarLength = horizontal_progress_left_end_width + progressMiddleSectionLength + horizontal_progress_right_end_width;
		}
		// endregion variable calculation

		int normalizedResourceRatio = (int) (((double) current_value / Math.max(max_value, 1)) * (progressBarLength));

		int[] cachedValues = CACHED_RESOURCE_BAR_VALUES.getOrDefault(identifier_string, new int[]{-1, -1, 0});

		int oldMaxBuildUp = cachedValues[0];
		int oldNormalizedBuildUpRatio = cachedValues[1];
		int buildUpBarAnimationCounter = cachedValues[2];

		if (oldMaxBuildUp != max_value) {
			oldMaxBuildUp = max_value;
			if (!max_value_change_is_animated) {
				oldNormalizedBuildUpRatio = normalizedResourceRatio;
			}
		}

		buildUpBarAnimationCounter = buildUpBarAnimationCounter + Math.max(1, current_value_reduction);

		if (oldNormalizedBuildUpRatio != normalizedResourceRatio && buildUpBarAnimationCounter > Math.max(0, animation_interval)) {
			boolean reduceOldRatio = oldNormalizedBuildUpRatio > normalizedResourceRatio;
			oldNormalizedBuildUpRatio = oldNormalizedBuildUpRatio + (reduceOldRatio ? -1 : 1);
			buildUpBarAnimationCounter = 0;
		}

		CACHED_RESOURCE_BAR_VALUES.put(identifier_string, new int[]{oldMaxBuildUp, oldNormalizedBuildUpRatio, buildUpBarAnimationCounter});

		client.getProfiler().push(identifier_string + "_bar");

		// background
		if (resource_bar_fill_direction == StaminaAttributes.ResourceBarFillDirection.BOTTOM_TO_TOP || resource_bar_fill_direction == StaminaAttributes.ResourceBarFillDirection.TOP_TO_BOTTOM) {
			context.drawTexture(texture_ids[3], elementX, elementY, 0, 0, backgroundTextureWidth, vertical_background_top_end_height, backgroundTextureWidth, backgroundTextureHeight);
			if (background_additional_middle_segment_amount > 0) {
				for (int i = 0; i < background_additional_middle_segment_amount; i++) {
					context.drawTexture(texture_ids[3], elementX, elementY + vertical_background_top_end_height + (i * vertical_background_middle_segment_height), 0, vertical_background_top_end_height, backgroundTextureWidth, vertical_background_middle_segment_height, backgroundTextureWidth, backgroundTextureHeight);
				}
			}
			context.drawTexture(texture_ids[3], elementX, elementY + vertical_background_top_end_height + backgroundMiddleSectionLength, 0, vertical_background_top_end_height + vertical_background_middle_segment_height, backgroundTextureWidth, vertical_background_bottom_end_height, backgroundTextureWidth, backgroundTextureHeight);
		} else {
			context.drawTexture(texture_ids[0], elementX, elementY, 0, 0, horizontal_background_left_end_width, backgroundTextureHeight, backgroundTextureWidth, backgroundTextureHeight);
			if (background_additional_middle_segment_amount > 0) {
				for (int i = 0; i < background_additional_middle_segment_amount; i++) {
					context.drawTexture(texture_ids[0], elementX + horizontal_background_left_end_width + (i * horizontal_background_middle_segment_width), elementY, horizontal_background_left_end_width, 0, horizontal_background_middle_segment_width, backgroundTextureHeight, backgroundTextureWidth, backgroundTextureHeight);
				}
			}
			context.drawTexture(texture_ids[0], elementX + horizontal_background_left_end_width + backgroundMiddleSectionLength, elementY, horizontal_background_left_end_width + horizontal_background_middle_segment_width, 0, horizontal_progress_right_end_width, backgroundTextureHeight, backgroundTextureWidth, backgroundTextureHeight);
		}

		// progress
		int displayRatio = enable_smooth_animation ? oldNormalizedBuildUpRatio : normalizedResourceRatio;
		if (displayRatio > 0) {
			int ratioFirstPart;
			int ratioLastPart;
			int progressElementX = elementX + progress_offset_x;
			int progressElementY = elementY + progress_offset_y;

			if (resource_bar_fill_direction == StaminaAttributes.ResourceBarFillDirection.BOTTOM_TO_TOP) {
				// 1: bottom to top

				ratioFirstPart = Math.min(vertical_progress_bottom_end_height, displayRatio);
				ratioLastPart = Math.min(vertical_progress_top_end_height, displayRatio - vertical_progress_bottom_end_height - progressMiddleSectionLength);

				context.drawTexture(texture_ids[4], progressElementX, progressElementY + progressBarLength - ratioFirstPart, 0, progressTextureHeight - ratioFirstPart, progressTextureWidth, ratioFirstPart, progressTextureWidth, progressTextureHeight);
				if (displayRatio > vertical_progress_bottom_end_height && background_additional_middle_segment_amount > 0) {
					boolean breakDisplay = false;
					for (int i = 0; i < progress_additional_middle_segment_amount; i++) {
						for (int j = 1; j <= vertical_progress_middle_segment_height; j++) {
							int currentTextureY = vertical_progress_bottom_end_height + (i * vertical_progress_middle_segment_height) + j;
							if (currentTextureY > displayRatio) {
								breakDisplay = true;
								break;
							}
							context.drawTexture(texture_ids[4], progressElementX, progressElementY + progressBarLength - currentTextureY, 0, vertical_progress_top_end_height + vertical_progress_middle_segment_height - j, progressTextureWidth, 1, progressTextureWidth, progressTextureHeight);
						}
						if (breakDisplay) {
							break;
						}
					}

				}
				if (displayRatio > (vertical_progress_bottom_end_height + progressMiddleSectionLength)) {
					context.drawTexture(texture_ids[4], progressElementX, progressElementY + vertical_progress_top_end_height - ratioLastPart, 0, vertical_progress_top_end_height - ratioLastPart, progressTextureWidth, ratioLastPart, progressTextureWidth, progressTextureHeight);
				}
			}
			else if (resource_bar_fill_direction == StaminaAttributes.ResourceBarFillDirection.RIGHT_TO_LEFT) {
				// 2: right to left

				ratioFirstPart = Math.min(horizontal_progress_right_end_width, displayRatio);
				ratioLastPart = Math.min(horizontal_progress_left_end_width, displayRatio - horizontal_progress_right_end_width - progressMiddleSectionLength);

				context.drawTexture(texture_ids[1], progressElementX + progressBarLength - ratioFirstPart, progressElementY, progressTextureWidth - ratioFirstPart, 0, ratioFirstPart, progressTextureHeight, progressTextureWidth, progressTextureHeight);
				if (displayRatio > horizontal_progress_right_end_width && background_additional_middle_segment_amount > 0) {
					boolean breakDisplay = false;
					for (int i = 0; i < progress_additional_middle_segment_amount; i++) {
						for (int j = 1; j <= horizontal_progress_middle_segment_width; j++) {
							int currentTextureX = horizontal_progress_left_end_width + (i * horizontal_progress_middle_segment_width) + j;
							if (currentTextureX > displayRatio) {
								breakDisplay = true;
								break;
							}
							context.drawTexture(texture_ids[1], progressElementX + progressBarLength - currentTextureX, progressElementY, horizontal_progress_left_end_width + horizontal_progress_middle_segment_width - j, 0, 1, progressTextureHeight, progressTextureWidth, progressTextureHeight);
						}
						if (breakDisplay) {
							break;
						}
					}

				}
				if (displayRatio > (horizontal_progress_right_end_width + progressMiddleSectionLength)) {
					context.drawTexture(texture_ids[1], progressElementX + horizontal_progress_left_end_width - ratioLastPart, progressElementY, horizontal_progress_left_end_width - ratioLastPart, 0, ratioLastPart, progressTextureHeight, progressTextureWidth, progressTextureHeight);
				}
			}
			else if (resource_bar_fill_direction == StaminaAttributes.ResourceBarFillDirection.TOP_TO_BOTTOM) {
				// 3: top to bottom

				ratioFirstPart = Math.min(vertical_progress_top_end_height, displayRatio);
				ratioLastPart = Math.min(vertical_progress_bottom_end_height, displayRatio - vertical_progress_top_end_height - progressMiddleSectionLength);

				context.drawTexture(texture_ids[4], progressElementX, progressElementY, 0, 0, progressTextureWidth, ratioFirstPart, progressTextureWidth, progressTextureHeight);
				if (displayRatio > vertical_progress_top_end_height && background_additional_middle_segment_amount > 0) {
					boolean breakDisplay = false;
					for (int i = 0; i < progress_additional_middle_segment_amount; i++) {
						for (int j = 0; j < vertical_progress_middle_segment_height; j++) {
							int currentTextureY = vertical_progress_top_end_height + (i * vertical_progress_middle_segment_height) + j;
							if (currentTextureY > displayRatio) {
								breakDisplay = true;
								break;
							}
							context.drawTexture(texture_ids[4], progressElementX, progressElementY + currentTextureY, 0, vertical_progress_top_end_height + j, progressTextureWidth, 1, progressTextureWidth, progressTextureHeight);
						}
						if (breakDisplay) {
							break;
						}
					}
				}
				if (displayRatio > (vertical_progress_top_end_height + progressMiddleSectionLength)) {
					context.drawTexture(texture_ids[4], progressElementX, progressElementY + vertical_progress_top_end_height + progressMiddleSectionLength, 0, vertical_progress_top_end_height + vertical_progress_middle_segment_height, progressTextureWidth, ratioLastPart, progressTextureWidth, progressTextureHeight);
				}
			}
			else {
				// 0: left to right

				ratioFirstPart = Math.min(horizontal_progress_left_end_width, displayRatio);
				ratioLastPart = Math.min(horizontal_progress_right_end_width, displayRatio - horizontal_progress_left_end_width - progressMiddleSectionLength);

				context.drawTexture(texture_ids[1], progressElementX, progressElementY, 0, 0, ratioFirstPart, progressTextureHeight, progressTextureWidth, progressTextureHeight);
				if (displayRatio > horizontal_progress_left_end_width && background_additional_middle_segment_amount > 0) {
					boolean breakDisplay = false;
					for (int i = 0; i < progress_additional_middle_segment_amount; i++) {
						for (int j = 0; j < horizontal_progress_middle_segment_width; j++) {
							int currentTextureX = horizontal_progress_left_end_width + (i * horizontal_progress_middle_segment_width) + j;
							if (currentTextureX > displayRatio) {
								breakDisplay = true;
								break;
							}
							context.drawTexture(texture_ids[1], progressElementX + currentTextureX, progressElementY, horizontal_progress_left_end_width + j, 0, 1, progressTextureHeight, progressTextureWidth, progressTextureHeight);
						}
						if (breakDisplay) {
							break;
						}
					}
				}
				if (displayRatio > (horizontal_progress_left_end_width + progressMiddleSectionLength)) {
					context.drawTexture(texture_ids[1], progressElementX + horizontal_progress_left_end_width + progressMiddleSectionLength, progressElementY, horizontal_progress_left_end_width + horizontal_progress_middle_segment_width, 0, ratioLastPart, progressTextureHeight, progressTextureWidth, progressTextureHeight);
				}
			}

			// overlay
			if (show_current_value_overlay) {
				int overlayElementX = progressElementX + overlay_offset_x;
				int overlayElementY = progressElementY + overlay_offset_y;
				if (resource_bar_fill_direction == StaminaAttributes.ResourceBarFillDirection.BOTTOM_TO_TOP) {
					// 1: bottom to top
					if (current_value > 0 && current_value < max_value) {
						context.drawTexture(texture_ids[5], overlayElementX, overlayElementY + progressBarLength - normalizedResourceRatio, 0, 0, vertical_overlay_width, vertical_overlay_height, vertical_overlay_width, horizontal_overlay_height);
					}
				} else if (resource_bar_fill_direction == StaminaAttributes.ResourceBarFillDirection.RIGHT_TO_LEFT) {
					// 2: right to left
					if (current_value > 0 && current_value < max_value) {
						context.drawTexture(texture_ids[2], overlayElementX + progressBarLength - normalizedResourceRatio, overlayElementY, 0, 0, horizontal_overlay_width, horizontal_overlay_height, horizontal_overlay_width, horizontal_overlay_height);
					}
				} else if (resource_bar_fill_direction == StaminaAttributes.ResourceBarFillDirection.TOP_TO_BOTTOM) {
					// 3: top to bottom
					if (current_value > 0 && current_value < max_value) {
						context.drawTexture(texture_ids[5], overlayElementX, overlayElementY + normalizedResourceRatio, 0, 0, vertical_overlay_width, vertical_overlay_height, vertical_overlay_width, horizontal_overlay_height);
					}
				} else {
					// 0: left to right
					if (current_value > 0 && current_value < max_value) {
						context.drawTexture(texture_ids[2], overlayElementX + normalizedResourceRatio, overlayElementY, 0, 0, horizontal_overlay_width, horizontal_overlay_height, horizontal_overlay_width, horizontal_overlay_height);
					}
				}
			}

			if (show_number) {
				String resourceBarNumberString = show_max_value ? current_value + "/" + max_value : String.valueOf(current_value);
				int resourceBarNumberX = originX - (textRenderer.getWidth(resourceBarNumberString) / 2) + number_offset_x;
				int resourceBarNumberY = originY + number_offset_y;

				client.getProfiler().swap(identifier_string + "_number");

				context.drawText(textRenderer, resourceBarNumberString, resourceBarNumberX + 1, resourceBarNumberY, 0, false);
				context.drawText(textRenderer, resourceBarNumberString, resourceBarNumberX - 1, resourceBarNumberY, 0, false);
				context.drawText(textRenderer, resourceBarNumberString, resourceBarNumberX, resourceBarNumberY + 1, 0, false);
				context.drawText(textRenderer, resourceBarNumberString, resourceBarNumberX, resourceBarNumberY - 1, 0, false);
				context.drawText(textRenderer, resourceBarNumberString, resourceBarNumberX, resourceBarNumberY, resource_bar_number_color, false);
			}
		}

		client.getProfiler().pop();
	}
}