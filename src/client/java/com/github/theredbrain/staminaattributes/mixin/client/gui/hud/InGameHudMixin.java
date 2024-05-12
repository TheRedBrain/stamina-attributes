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

                int attributeBarX = this.scaledWidth / 2 - 91;
                int attributeBarY = this.scaledHeight - clientConfig.stamina_bar_y_offset;
                int attributeBarNumberX;
                int attributeBarNumberY;
                int normalizedStaminaRatio = (int) (((double) stamina / Math.max(maxStamina, 1)) * 182);

                if (maxStamina > 0 && (stamina < maxStamina || clientConfig.show_full_stamina_bar)) {
                    this.client.getProfiler().push("stamina_bar");
                    context.drawTexture(BARS_TEXTURE, attributeBarX, attributeBarY, 0, 30, 182, 5);
                    if (normalizedStaminaRatio > 0) {
                        context.drawTexture(BARS_TEXTURE, attributeBarX, attributeBarY, 0, 35, normalizedStaminaRatio, 5);
                    }
                    if (clientConfig.show_stamina_bar_number) {
                        this.client.getProfiler().swap("stamina_bar_number");
                        String string = String.valueOf(stamina);
                        attributeBarNumberX = (this.scaledWidth - this.getTextRenderer().getWidth(string)) / 2;
                        attributeBarNumberY = attributeBarY - 1;
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
