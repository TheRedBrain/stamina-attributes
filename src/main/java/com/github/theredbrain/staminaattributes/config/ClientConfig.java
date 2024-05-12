package com.github.theredbrain.staminaattributes.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(
        name = "client"
)
public class ClientConfig implements ConfigData {
    public boolean show_stamina_bar = true;
    public boolean show_full_stamina_bar = true;
    public boolean show_stamina_bar_number = true;
    public int stamina_bar_number_color = -6250336;
    public int stamina_bar_y_offset = 45;
    public ClientConfig() {

    }
}
