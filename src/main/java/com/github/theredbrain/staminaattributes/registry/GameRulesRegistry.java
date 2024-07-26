package com.github.theredbrain.staminaattributes.registry;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class GameRulesRegistry {
	public static final GameRules.Key<GameRules.BooleanRule> NATURAL_STAMINA_REGENERATION =
			GameRuleRegistry.register("naturalStaminaRegeneration", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));

	public static void init() {
	}
}
