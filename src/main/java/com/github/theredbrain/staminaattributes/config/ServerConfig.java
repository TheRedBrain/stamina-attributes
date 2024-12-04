package com.github.theredbrain.staminaattributes.config;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;

public class ServerConfig extends Config {
	public ServerConfig() {
		super(StaminaAttributes.identifier("server"));
	}
	public int item_use_cooldown_when_no_stamina = 20;
	public boolean jumping_requires_stamina = true;
	public boolean sprinting_requires_stamina = true;
	public TickCostSection tickCosts = new TickCostSection();
	public static class TickCostSection extends ConfigSection {
		public float stamina_cost_sprinting = 0.05F;
		public float stamina_cost_sneaking = 0.05F;
		public float stamina_cost_walking = 0.0F;
		public float stamina_cost_swimming = 0.0F;
		public float stamina_cost_walking_underwater = 0.0F;
		public float stamina_cost_walking_in_water = 0.05F;
		public float stamina_cost_climbing = 0.05F;
	}
	public ActionCostSection actionCosts = new ActionCostSection();
	public static class ActionCostSection extends ConfigSection {
		public float stamina_cost_sprint_jumping = 1.0F;
		public float stamina_cost_jumping = 1.0F;
	}
}
