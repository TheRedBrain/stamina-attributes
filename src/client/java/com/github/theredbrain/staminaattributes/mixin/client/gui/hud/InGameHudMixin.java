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

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow private int scaledWidth;

    @Shadow private int scaledHeight;

    @Shadow @Final private MinecraftClient client;

    @Shadow public abstract TextRenderer getTextRenderer();

    @Unique
    private static final Identifier BARS_TEXTURE = new Identifier("textures/gui/bars.png");

    @Inject(method = "renderStatusBars", at = @At("RETURN"))
    private void staminaattributes$renderStatusBars(DrawContext context, CallbackInfo ci) {
        var clientConfig = StaminaAttributesClient.clientConfig;
        if (clientConfig.show_stamina_bar) {
            PlayerEntity playerEntity = this.getCameraPlayer();
            if (playerEntity != null) {
                int stamina = MathHelper.ceil(((StaminaUsingEntity) playerEntity).staminaattributes$getStamina());
                int maxStamina = MathHelper.ceil(((StaminaUsingEntity) playerEntity).staminaattributes$getMaxStamina());

                int attributeBarX = this.scaledWidth / 2 + clientConfig.stamina_bar_x_offset;
                int attributeBarY = this.scaledHeight + clientConfig.stamina_bar_y_offset - (clientConfig.dynamically_adjust_to_armor_bar && playerEntity.getArmor() > 0 ? 10 : 0);
                int stamina_bar_additional_length = clientConfig.stamina_bar_additional_length;
                int attributeBarNumberX;
                int attributeBarNumberY;
                int normalizedStaminaRatio = (int) (((double) stamina / Math.max(maxStamina, 1)) * (5 + clientConfig.stamina_bar_additional_length + 5));

                if (maxStamina > 0 && (stamina < maxStamina || clientConfig.show_full_stamina_bar)) {
                    this.client.getProfiler().push("stamina_bar");

                    // background
                    context.drawTexture(BARS_TEXTURE, attributeBarX, attributeBarY, 0, 30, 5, 5, 256, 256);
                    if (stamina_bar_additional_length > 0) {
                        for (int i = 0; i < stamina_bar_additional_length; i++) {
                            context.drawTexture(BARS_TEXTURE, attributeBarX + 5 + i, attributeBarY, 5, 30, 1, 5, 256, 256);
                        }
                    }
                    context.drawTexture(BARS_TEXTURE, attributeBarX + 5 + stamina_bar_additional_length, attributeBarY, 177, 30, 5, 5, 256, 256);

                    // foreground
                    if (normalizedStaminaRatio > 0) {
                        context.drawTexture(BARS_TEXTURE, attributeBarX, attributeBarY, 0, 35, Math.min(5, normalizedStaminaRatio), 5, 256, 256);
                        if (normalizedStaminaRatio > 5) {
                            if (stamina_bar_additional_length > 0) {
                                for (int i = 5; i < Math.min(5 + stamina_bar_additional_length, normalizedStaminaRatio); i++) {
                                    context.drawTexture(BARS_TEXTURE, attributeBarX + i, attributeBarY, 5, 35, 1, 5, 256, 256);
                                }
                            }
                        }
                        if (normalizedStaminaRatio > (5 + stamina_bar_additional_length)) {
                            context.drawTexture(BARS_TEXTURE, attributeBarX + 5 + stamina_bar_additional_length, attributeBarY, 177, 35, Math.min(5, normalizedStaminaRatio - 5 - stamina_bar_additional_length), 5, 256, 256);
                        }
                    }

                    if (clientConfig.show_stamina_bar_number) {
                        this.client.getProfiler().swap("stamina_bar_number");
                        String string = String.valueOf(stamina);
                        attributeBarNumberX = (this.scaledWidth - this.getTextRenderer().getWidth(string)) / 2 + clientConfig.stamina_bar_number_x_offset;
                        attributeBarNumberY = this.scaledHeight + clientConfig.stamina_bar_number_y_offset - (clientConfig.dynamically_adjust_to_armor_bar && playerEntity.getArmor() > 0 ? 10 : 0);
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
