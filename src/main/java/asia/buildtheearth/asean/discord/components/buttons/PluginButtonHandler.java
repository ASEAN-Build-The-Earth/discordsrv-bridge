package asia.buildtheearth.asean.discord.components.buttons;

import asia.buildtheearth.asean.discord.commands.interaction.InteractionEvent;
import github.scarsz.discordsrv.dependencies.jda.api.events.interaction.ButtonClickEvent;

/**
 * Base marker interface for all button interaction handlers.
 *
 * <p>This interface is extended by has the respective {@code onInteracted} method for {@link SimpleButtonHandler} and
 * {@link InteractiveButtonHandler} to define specific interaction handling logic.</p>
 *
 * @see #onInteracted(PluginButton, ButtonClickEvent)
 * @see #onInteracted(PluginButton, ButtonClickEvent, InteractionEvent)
 * @see #onInteractedBadOwner(ButtonClickEvent)
 */
public interface PluginButtonHandler{

    /**
     * Handles interaction logic for non-headless buttons.
     * Override this method to provide specific behavior.
     *
     * @param button       The {@link PluginButton} that was clicked.
     * @param event        The {@link ButtonClickEvent} from JDA.
     * @param interactions Additional interaction context or utilities.
     * @throws IllegalArgumentException if not overridden
     */
    default void onInteracted(PluginButton button, ButtonClickEvent event, InteractionEvent interactions) {
        throw new IllegalArgumentException("Plugin button requires an implementation of interaction handling.");
    }

    /**
     * Handles interaction logic for headless buttons.
     * Override this method to provide specific behavior.
     *
     * @param button       The {@link PluginButton} that was clicked.
     * @param event        The {@link ButtonClickEvent} from JDA.
     * @throws IllegalArgumentException if not overridden
     */
    default void onInteracted(PluginButton button, ButtonClickEvent event) {
        throw new IllegalArgumentException("Plugin button requires an implementation of interaction handling.");
    }

    /**
     * Invoked when a user who does not own the button attempts to interact with it.
     * This method is optional to override.
     *
     * @param event The event triggered by the unauthorized user.
     */
    default void onInteractedBadOwner(ButtonClickEvent event) {}
}