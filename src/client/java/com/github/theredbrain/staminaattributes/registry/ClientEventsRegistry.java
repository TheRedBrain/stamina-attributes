package com.github.theredbrain.staminaattributes.registry;

import com.github.theredbrain.resourcebarapi.ResourceBarAPI;
import com.github.theredbrain.resourcebarapi.ResourceBarAPIClient;
import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.StaminaAttributesClient;
import com.github.theredbrain.staminaattributes.config.ClientConfig;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.github.theredbrain.staminaattributes.gui.hud.DuckGuiMixin;
import me.fzzyhmstrs.fzzy_config.api.ConfigApi;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.client.gui.*;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.List;

public class ClientEventsRegistry {
	private static final String RESOURCE_BAR_IDENTIFIER_STRING = StaminaAttributes.MOD_ID + ":stamina";
	private static final Identifier ICON_STAMINA_CONTAINER = StaminaAttributes.identifier("hud/icon_stamina_container");
	private static final Identifier ICON_STAMINA_FULL = StaminaAttributes.identifier("hud/icon_stamina_full");
	private static final Identifier ICON_STAMINA_HALF = StaminaAttributes.identifier("hud/icon_stamina_half");
	private static final Identifier ICON_STAMINA_CONTAINER_BLINKING = StaminaAttributes.identifier("hud/icon_stamina_container_blinking");
	private static final Identifier ICON_STAMINA_FULL_BLINKING = StaminaAttributes.identifier("hud/icon_stamina_full_blinking");
	private static final Identifier ICON_STAMINA_HALF_BLINKING = StaminaAttributes.identifier("hud/icon_stamina_half_blinking");
	private static final Identifier ICON_STAMINA_CONTAINER_RESERVED = StaminaAttributes.identifier("hud/icon_stamina_container_reserved");
	private static final Identifier ICON_STAMINA_FULL_RESERVED = StaminaAttributes.identifier("hud/icon_stamina_full_reserved");
	private static final Identifier ICON_STAMINA_HALF_RESERVED = StaminaAttributes.identifier("hud/icon_stamina_half_reserved");

	public static void initializeClientEvents() {
		HudElementRegistry.attachElementAfter(VanillaHudElements.HEALTH_BAR, StaminaAttributes.identifier("stamina"), ((guiGraphicsExtractor, deltaTracker) -> {
			Minecraft minecraft = Minecraft.getInstance();
			LocalPlayer localPlayer = minecraft.player;
			ClientConfig clientConfig = StaminaAttributesClient.CLIENT_CONFIG;
			if (localPlayer != null && !minecraft.gui.hud.isHidden()) {
				int stamina = Mth.ceil(((StaminaUsingEntity) localPlayer).staminaattributes$getStamina());

				DuckGuiMixin gui = ((DuckGuiMixin) minecraft.gui.hud);

				boolean shouldBlink = false;
				int currentDisplayStamina = stamina;

				if (clientConfig.iconBarSettings.enable_icon_blinking.get()) {
					shouldBlink = gui.staminaattributes$getStaminaIconBlinkTime() > gui.staminaattributes$getTickCount() && (gui.staminaattributes$getStaminaIconBlinkTime() - gui.staminaattributes$getTickCount()) / 3L % 2L == 1L;
					long l = Util.getMillis();
					if (stamina < gui.staminaattributes$getLastStamina()) {
						gui.staminaattributes$setLastStaminaTime(l);
						gui.staminaattributes$setStaminaIconBlinkTime(gui.staminaattributes$getTickCount() + 10);
					} else if (stamina > gui.staminaattributes$getLastStamina()) {
						gui.staminaattributes$setLastStaminaTime(l);
						gui.staminaattributes$setStaminaIconBlinkTime(gui.staminaattributes$getTickCount() + 5);
					}

					if (l - gui.staminaattributes$getLastStaminaTime() > 100L) {
						gui.staminaattributes$setDisplayStamina(stamina);
						gui.staminaattributes$setLastStaminaTime(l);
					}

					gui.staminaattributes$setLastStamina(stamina);
					currentDisplayStamina = gui.staminaattributes$getDisplayStamina();
				}

				double maxStamina = Math.max(Mth.ceil(((StaminaUsingEntity) localPlayer).staminaattributes$getMaxStamina()), Math.max(currentDisplayStamina, stamina));
				double unreservedStamina = Mth.ceil(((StaminaUsingEntity) localPlayer).staminaattributes$getUnreservedStamina());

				if (!localPlayer.isCreative() && maxStamina > 0) {

					int maxAirSupply = localPlayer.getMaxAirSupply();
					int currentAirSupply = Math.min(localPlayer.getAirSupply(), maxAirSupply);
					int air_offset = clientConfig.dynamically_adjust_to_air_bar && localPlayer.isEyeInFluid(FluidTags.WATER) || currentAirSupply < maxAirSupply ? -10 : 0;
					int armor_offset = clientConfig.dynamically_adjust_to_armor_bar && localPlayer.getArmorValue() > 0 ? -10 : 0;

					MutablePair<Integer, Integer> originPos = ResourceBarAPIClient.getOriginPos(guiGraphicsExtractor, clientConfig.origin);

					if (clientConfig.stamina_bar_display == ResourceBarAPI.ResourceBarDisplay.ICON && (stamina < maxStamina || clientConfig.show_full_stamina_bar)) {

						List<ResourceBarAPI.ResourceBarIconType> list = new ArrayList<>();
						list.add(new ResourceBarAPI.ResourceBarIconType(
								currentDisplayStamina,
								unreservedStamina,
								shouldBlink ? ICON_STAMINA_CONTAINER_BLINKING : ICON_STAMINA_CONTAINER,
								shouldBlink ? ICON_STAMINA_FULL_BLINKING : ICON_STAMINA_FULL,
								shouldBlink ? ICON_STAMINA_HALF_BLINKING : ICON_STAMINA_HALF,
								ResourceBarAPI.ContinuationType.NEW_ICON
						));
						list.add(new ResourceBarAPI.ResourceBarIconType(
								maxStamina - unreservedStamina,
								maxStamina - unreservedStamina,
								ICON_STAMINA_CONTAINER_RESERVED,
								ICON_STAMINA_FULL_RESERVED,
								ICON_STAMINA_HALF_RESERVED,
								ResourceBarAPI.ContinuationType.NEW_ICON
						));
						ResourceBarAPIClient.drawIconResourceBar(
								guiGraphicsExtractor,
								list,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.iconBarSettings.offset_x.get(),
								clientConfig.iconBarSettings.offset_y.get() + air_offset + armor_offset,
								clientConfig.fill_direction,
								clientConfig.iconBarSettings.reverse_stack_direction.get(),
								clientConfig.iconBarSettings.max_icon_amount_per_bar.get()
						);
					} else if (clientConfig.stamina_bar_display == ResourceBarAPI.ResourceBarDisplay.SMOOTH && (stamina < maxStamina || clientConfig.show_full_stamina_bar)) {
						ResourceBarAPIClient.drawSmoothResourceBar(
								minecraft,
								guiGraphicsExtractor,
								RESOURCE_BAR_IDENTIFIER_STRING,
								new double[]{
										-1,
										-1,
										0,
										-91,
										-45,
										5,
										182,
										5,
										182,
										5,
										182,
										5,
										5,
										0,
										0
								},
								new Identifier[]{
										StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_background.png"),
										StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_decrease_animation.png"),
										StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_increase_animation.png"),
										StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_increase_value.png"),
										StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress.png"),
										StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_reserved.png"),
										StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_overlay.png"),
										null
								},
								currentDisplayStamina,
								maxStamina,
								Mth.ceil(((StaminaUsingEntity) localPlayer).staminaattributes$getRegeneratedStamina()),
								unreservedStamina,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.smoothBarSettings.positionSettings.offsets_x,
								clientConfig.smoothBarSettings.positionSettings.offsets_y,
								0,
								air_offset + armor_offset,
								clientConfig.fill_direction,
								clientConfig.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_heights,
								clientConfig.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_widths,
								clientConfig.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_ids,
								clientConfig.smoothBarSettings.textureSettings.progressTextureSettings.offset_x,
								clientConfig.smoothBarSettings.textureSettings.progressTextureSettings.offset_y,
								clientConfig.smoothBarSettings.textureSettings.progressTextureSettings.texture_heights,
								clientConfig.smoothBarSettings.textureSettings.progressTextureSettings.texture_widths,
								clientConfig.smoothBarSettings.textureSettings.progressTextureSettings.progress_decrease_animation_texture_ids,
								clientConfig.smoothBarSettings.textureSettings.progressTextureSettings.progress_increase_animation_texture_ids,
								clientConfig.smoothBarSettings.textureSettings.progressTextureSettings.progress_increase_value_texture_ids,
								clientConfig.smoothBarSettings.textureSettings.progressTextureSettings.progress_texture_ids,
								clientConfig.smoothBarSettings.textureSettings.reservedTextureSettings.offset_x,
								clientConfig.smoothBarSettings.textureSettings.reservedTextureSettings.offset_y,
								clientConfig.smoothBarSettings.textureSettings.reservedTextureSettings.texture_heights,
								clientConfig.smoothBarSettings.textureSettings.reservedTextureSettings.texture_widths,
								clientConfig.smoothBarSettings.textureSettings.reservedTextureSettings.texture_ids,
								clientConfig.smoothBarSettings.show_current_value_overlay,
								clientConfig.smoothBarSettings.textureSettings.overlayTextureSettings.offset_x,
								clientConfig.smoothBarSettings.textureSettings.overlayTextureSettings.offset_y,
								clientConfig.smoothBarSettings.textureSettings.overlayTextureSettings.texture_heights,
								clientConfig.smoothBarSettings.textureSettings.overlayTextureSettings.texture_widths,
								clientConfig.smoothBarSettings.textureSettings.overlayTextureSettings.texture_ids,
								clientConfig.smoothBarSettings.show_icon,
								clientConfig.smoothBarSettings.iconTextureSettings.offset_x,
								clientConfig.smoothBarSettings.iconTextureSettings.offset_y,
								clientConfig.smoothBarSettings.iconTextureSettings.texture_heights,
								clientConfig.smoothBarSettings.iconTextureSettings.texture_widths,
								clientConfig.smoothBarSettings.iconTextureSettings.texture_ids,
								clientConfig.smoothBarSettings.enable_smooth_animation,
								clientConfig.smoothBarSettings.animationSettings.animation_interval,
								clientConfig.smoothBarSettings.animationSettings.max_value_change_is_animated
						);
					}
					if (clientConfig.numberSettings.show_number && (stamina < maxStamina || clientConfig.numberSettings.show_when_stamina_full)) {
						ResourceBarAPIClient.drawResourceNumber(
								minecraft,
								minecraft.font,
								guiGraphicsExtractor,
								RESOURCE_BAR_IDENTIFIER_STRING,
								stamina,
								maxStamina,
								unreservedStamina,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.numberSettings.show_max_value,
								clientConfig.numberSettings.offset_x,
								clientConfig.numberSettings.offset_y + air_offset + armor_offset,
								clientConfig.numberSettings.color.toInt()
						);
					}
				}
			}
		}));
		ConfigApi.event().onUpdateClient((identifier, config) -> {
			if (identifier.equals(Identifier.fromNamespaceAndPath(StaminaAttributes.MOD_ID, "client"))) {
				ResourceBarAPIClient.clearCache(
						RESOURCE_BAR_IDENTIFIER_STRING,
						new double[]{
								-1,
								-1,
								0,
								-91,
								-45,
								5,
								182,
								5,
								182,
								5,
								182,
								5,
								5,
								0,
								0
						},
						new Identifier[]{
								StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_background.png"),
								StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_decrease_animation.png"),
								StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_increase_animation.png"),
								StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_increase_value.png"),
								StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_progress.png"),
								StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_reserved.png"),
								StaminaAttributes.identifier("textures/gui/sprites/hud/horizontal_stamina_overlay.png"),
								null
						}
				);
			}
		});
	}
}