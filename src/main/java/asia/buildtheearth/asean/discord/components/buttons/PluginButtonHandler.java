package asia.buildtheearth.asean.discord.components.buttons;

import asia.buildtheearth.asean.discord.commands.interactions.InteractionEvent;
import com.discordsrv.dependencies.net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

/**
 * Base marker interface for all button interaction handlers.
 *
 * <p>This interface is extended by has the respective {@code onInteracted} method for {@link SimpleButtonHandler} and
 * {@link InteractiveButtonHandler} to define specific interaction handling logic.</p>
 *
 * @see #onInteracted(PluginButton, ButtonInteractionEvent)
 * @see #onInteracted(PluginButton, ButtonInteractionEvent, InteractionEvent)
 * @see #onInteractedBadOwner(ButtonInteractionEvent)
 */
public interface PluginButtonHandler{

    /**
     * Handles interaction logic for non-headless buttons.
     * Override this method to provide specific behavior.
     *
     * @param button       The {@link PluginButton} that was clicked.
     * @param event        The {@link ButtonInteractionEvent} from JDA.
     * @param interactions Additional interaction context or utilities.
     * @throws IllegalArgumentException if not overridden
     */
    default void onInteracted(PluginButton button, ButtonInteractionEvent event, InteractionEvent interactions) {
        throw new IllegalArgumentException("Plugin button requires an implementation of interaction handling.");
    }

    /**
     * Handles interaction logic for headless buttons.
     * Override this method to provide specific behavior.
     *
     * @param button       The {@link PluginButton} that was clicked.
     * @param event        The {@link ButtonInteractionEvent} from JDA.
     * @throws IllegalArgumentException if not overridden
     */
    default void onInteracted(PluginButton button, ButtonInteractionEvent event) {
        throw new IllegalArgumentException("Plugin button requires an implementation of interaction handling.");
    }

    /**
     * Invoked when a user who does not own the button attempts to interact with it.
     * This method is optional to override.
     *
     * @param event The event triggered by the unauthorized user.
     */
    default void onInteractedBadOwner(ButtonInteractionEvent event) {}
}