# Stamina Attributes
This API adds a stamina system controlled by entity attributes.

## Default implementation
LivingEntities can have up to **_generic.max_stamina_** amounts of stamina. Stamina is regenerated by **_generic.stamina_regeneration_** every **_generic.stamina_tick_threshold_** ticks.

When stamina is reduced, regeneration is stopped for **_generic.stamina_regeneration_delay_threshold_** ticks.

When stamina is <= 0, regeneration is stopped for **_generic.depleted_stamina_regeneration_delay_threshold_** ticks.

### Default attribute values
**_generic.max_stamina_**: 10
**_generic.stamina_regeneration_**: 0
**_generic.stamina_tick_threshold_**: 20
**_generic.stamina_regeneration_delay_threshold_**: 20
**_generic.depleted_stamina_regeneration_delay_threshold_**: 60

## Customization
When the gamerule "naturalStaminaRegeneration" is true, players have a stamina regeneration of at least 1.

The client config allows customizing the HUD element. The details are explained in [this wiki article](https://github.com/TheRedBrain/overhauled-damage/wiki/Resource-Bar-Customization).

The server config controls the integration with game mechanics, like stamina costs for various actions like jumping.

## API
Casting a "LivingEntity" to the "StaminaUsingEntity" interface gives access to all relevant methods.