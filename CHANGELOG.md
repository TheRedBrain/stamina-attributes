# 3.0.0

- updated to 1.21.10

# 2.9.2

- fixed stamina bar being empty when joining a world for the first time
- fixed sprint jumping stamina cost
- bumped various dependency versions

# 2.9.1

- tweaked default client config settings
- fixed default player attribute values
- fixed stamina bar being empty when joining a world / respawning

# 2.9.0

- added an "exhaust" mechanic, when stamina is lower than 0, a configurable status effect is applied
- reworked "swimming_requires_stamina" option

# 2.8.0

- added "swimming_requires_stamina" server config option
- added entity attributes for each activity that can cost stamina (previously defined in the server config)
- added server config options to define the default for each attribute (only affects players)
- changed default value of all entity attributes to 0.0

# 2.7.0

- added alternative stamina bar consisting of icons, similar to vanillas resource bars (this first iteration does not yet support multiple icon types per bar, e.g. reserved stamina)
- reworked the "naturalStaminaRegeneration" game rule (now works with mods that display attribute values)
- fixed an issue where the stamina bar was empty when joining a world for the first time/respawning
- fixed an issue where the stamina bar was visible in creative mode
- fixed an issue where the stamina bar was visible even when the HUD was hidden (pressing F1)

# 2.6.0

HUD rendering overhaul
The initial idea of splitting a bar into three textures per "layer" to allow for easy change of the bar length, came with the cost of massive FPS drops.
With this rewrite the bar size is no longer changeable with a simple config option. Each 'layer' consists of only 1 texture (which can have configurable dimensions).
In addition, the texture can be dynamically replaced by another texture (with configurable dimensions) depending on the max value.
- removed 'is_centered' option, as it was redundant and unnecessarily complicated
- 'fill_direction' no longer chooses between a horizontal and a vertical 'texture set', it only determines the direction from which the bar is filled. This means that changing between horizontal and vertical resource bars also requires a texture change.
- added the option to display an icon (with configurable texture id and dimensions). This can be toggled independently of the bar and the number.

# 2.5.0

- changed "natural stamina regeneration" gamerule to simply add 1 stamina regeneration. The previous implementation prevented stamina regeneration from ever becoming negative.
- fixed "offset_from_origin" config values not working correctly
- stamina number display is now independent of the stamina bar

# 2.4.0

- added "generic.reserved_stamina" entity attribute
- added "generic.item_use_stamina_cost" entity attribute
- added "using_costs_stamina" item tag. Using items in this tag costs stamina (contains snowball by default)
- added "continuous_using_costs_stamina" item tag. Continuously using items in this tag costs stamina (contains bow, crossbow and shield by default)
- HUD element overhaul, improves mod compatibility, increases customization options
- server config can now be edited in game (Thanks to Fzzy Config)
- generally improved config layout
- removed dependency on Cloth Config
- added dependency on Fzzy Config
- added dependency on Resource Bar API

# 2.3.1

- fixed some internal issues

# 2.3.0

- further improvements to stamina bar customization

# 2.2.0

- update to 1.21.1
- improved stamina bar customization

# 2.1.0

- added optional smooth stamina bar animations
- added a stamina regeneration delay after stamina is reduced, controlled by an entity attribute
- added a missing translation key

# 2.0.0

Update to 1.21

# 1.2.1

- fixed an issue with the attribute registration

# 1.2.0

- changed the way the attributes are registered, which increased compatibility with other mods

# 1.1.0

- added more options to configure the HUD element

# 1.0.1

- fixed an issue where some config values where not applied correctly
- fixed an issue where the default stamina regeneration was always 1, even when the corresponding gamerule was set to false

# 1.0.0

First release!

#