# 1.4.1

- changed "natural stamina regeneration" gamerule to simply add 1 stamina regeneration. The previous implementation prevented stamina regeneration from ever becoming negative.

# 1.4.0

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

# 1.3.0

- added optional smooth stamina bar animations
- added a stamina regeneration delay after stamina is reduced, controlled by an entity attribute
- added a missing translation key

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