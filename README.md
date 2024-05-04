# Stamina Attributes
This API adds a stamina system.

## Default implementation
LivingEntities can have up to **_generic.max_stamina_** amounts of stamina. When stamina is reduced, a cooldown of **_generic.stamina_regeneration_delay_threshold_** ticks starts. After the cooldown ends **_generic.stamina_regeneration_** is regenerated every **_generic.stamina_tick_threshold_** ticks.

## Customization
When the gamerule "naturalStaminaRegeneration" is true, players have a stamina regeneration of at least 1.

The client config allows customizing the HUD element.

The server config controls the integration with game mechanics, like stamina costs for various actions like jumping.

## API
Casting a "LivingEntity" to the "StaminaUsingEntity" interface gives access to all relevant methods.