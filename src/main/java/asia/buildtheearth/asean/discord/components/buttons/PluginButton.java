package asia.buildtheearth.asean.discord.components.buttons;

import asia.buildtheearth.asean.discord.components.PluginComponent;
import github.scarsz.discordsrv.dependencies.jda.api.interactions.components.Button;

/**
 * Represents a Discord button component tied to a plugin context.
 *
 * @see PluginComponent
 */
public class PluginButton extends PluginComponent<Button> {

    /**
     * Constructs a new {@link PluginButton}.
     *
     * @param plugin    The plugin that owns this component.
     * @param component The JDA {@link Button} to be parsed to a plugin component.
     * @throws IllegalArgumentException If the component could not be created or is invalid.
     */
    public PluginButton(org.bukkit.plugin.Plugin plugin, Button component) throws IllegalArgumentException {
        super(plugin, component);
    }
}