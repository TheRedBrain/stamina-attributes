# 1.4.0

- added "generic.reserved_stamina" entity attribute
- added "generic.item_use_stamina_cost" entity attribute
- added "requires_stamina_for_use" item tag. Using items in this tag costs stamina
- changed render call for HUD element to use an event provided by FAPI, instead of a mixin. This should improve compatibility with mods that change the vanilla HUD.
- changed HUD element rendering to use Resource Bar API, this increases the customization options

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