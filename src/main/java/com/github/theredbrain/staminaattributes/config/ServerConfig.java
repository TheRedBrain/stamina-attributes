package com.github.theredbrain.staminaattributes.config;

import com.github.theredbrain.staminaattributes.StaminaAttributes;
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

@ConvertFrom(fileName = "server.json5", folder = "staminaattributes")
public class ServerConfig extends Config {

	public ServerConfig() {
		super(StaminaAttributes.identifier("server"));
	}

	public int item_use_cooldown_when_no_stamina = 20;
	public boolean jumping_requires_stamina = true;
	public boolean sprinting_requires_stamina = true;
	public boolean swimming_requires_stamina = true;
	public boolean jumping_in_water_requires_stamina = true;
	public boolean attacking_requires_stamina = true;
	public boolean block_breaking_requires_stamina = true;
	public boolean breaking_zero_destroy_time_blocks_costs_stamina = false;
	// is toggleable for compatibility with Better Combat Extension
	public boolean enable_attacking_stamina_cost = true;
	// is toggleable for compatibility with Overhauled Damage
	public boolean enable_attack_blocking_stamina_cost = true;
	public boolean players_can_exhaust = false;
	public ValidatedIdentifier exhausted_status_effect_identifier = ValidatedIdentifier.ofRegistry(Identifier.parse("minecraft:slowness"), BuiltInRegistries.MOB_EFFECT);

	public NaturalPlayerAttributeValuesSection naturalPlayerAttributeValues = new NaturalPlayerAttributeValuesSection();

	public static class NaturalPlayerAttributeValuesSection extends ConfigSection {
		public float natural_stamina_regeneration = 1.0F;
		public float natural_max_stamina = 10.0F;
		public float natural_depleted_stamina_regeneration_delay_threshold = 80.0F;
		public float natural_stamina_regeneration_delay_threshold = 40.0F;
		public float natural_stamina_tick_threshold = 20.0F;
		public float natural_reserved_stamina = 0.0F;
		public float natural_item_use_stamina_cost = 0.05F;
		public float natural_sprinting_tick_stamina_cost = 0.05F;
		public float natural_sneaking_tick_stamina_cost = 0.05F;
		public float natural_walking_tick_stamina_cost = 0.0F;
		public float natural_swimming_tick_stamina_cost = 0.05F;
		public float natural_walking_underwater_tick_stamina_cost = 0.05F;
		public float natural_walking_in_water_tick_stamina_cost = 0.05F;
		public float natural_climbing_tick_stamina_cost = 0.05F;
		public float natural_action_stamina_cost_sprint_jumping = 1.0F;
		public float natural_action_stamina_cost_jumping = 1.0F;
		public float natural_action_stamina_cost_attack_blocking = 1.0F;
		public float natural_action_stamina_cost_attacking = 1.0F;
		public float natural_action_stamina_cost_block_breaking = 1.0F;
	}
}