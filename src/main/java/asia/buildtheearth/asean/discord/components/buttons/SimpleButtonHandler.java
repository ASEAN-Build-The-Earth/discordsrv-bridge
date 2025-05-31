package asia.buildtheearth.asean.discord.components.buttons;

import github.scarsz.discordsrv.dependencies.jda.api.events.interaction.ButtonClickEvent;

/**
 * Interface for handling basic Discord button clicks.
 *
 * <p>This version of a handler provides only the button and event data
 * without additional interaction context.</p>
 *
 * @see PluginButton
 * @see InteractiveButtonHandler
 */
@FunctionalInterface
public interface SimpleButtonHandler extends PluginButtonHandler {

    /**
     * Called when a button is interacted with.
     *
     * @param button The {@link PluginButton} that was clicked.
     * @param event  The {@link ButtonClickEvent} from JDA.
     */
    void onInteracted(PluginButton button, ButtonClickEvent event);
}