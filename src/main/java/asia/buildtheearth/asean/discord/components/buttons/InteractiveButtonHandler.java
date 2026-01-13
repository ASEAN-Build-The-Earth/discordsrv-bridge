package asia.buildtheearth.asean.discord.components.buttons;

import asia.buildtheearth.asean.discord.commands.interactions.InteractionEvent;
import com.discordsrv.dependencies.net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

/**
 * Interface for handling advanced Discord button interactions.
 *
 * <p>This interface allows access to the {@link ButtonInteractionEvent} and
 * additional interaction context via {@link InteractionEvent}.</p>
 *
 * @see PluginButton
 * @see SimpleButtonHandler
 */
@FunctionalInterface
public interface InteractiveButtonHandler extends PluginButtonHandler {

    /**
     * Called when a button is interacted with.
     *
     * @param button       The {@link PluginButton} that was clicked.
     * @param event        The {@link ButtonInteractionEvent} from JDA.
     * @param interactions Additional interaction context or utilities.
     */
    void onInteracted(PluginButton button, ButtonInteractionEvent event, InteractionEvent interactions);
}