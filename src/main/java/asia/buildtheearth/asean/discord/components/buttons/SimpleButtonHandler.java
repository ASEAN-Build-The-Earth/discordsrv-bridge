package asia.buildtheearth.asean.discord.components.buttons;

import com.discordsrv.dependencies.net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

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
     * @param event  The {@link ButtonInteractionEvent} from JDA.
     */
    void onInteracted(PluginButton button, ButtonInteractionEvent event);
}