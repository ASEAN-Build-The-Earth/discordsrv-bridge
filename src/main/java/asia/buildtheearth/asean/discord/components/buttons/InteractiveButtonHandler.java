package asia.buildtheearth.asean.discord.components.buttons;

import asia.buildtheearth.asean.discord.commands.interactions.InteractionEvent;
import github.scarsz.discordsrv.dependencies.jda.api.events.interaction.ButtonClickEvent;

/**
 * Interface for handling advanced Discord button interactions.
 *
 * <p>This interface allows access to the {@link ButtonClickEvent} and
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
     * @param event        The {@link ButtonClickEvent} from JDA.
     * @param interactions Additional interaction context or utilities.
     */
    void onInteracted(PluginButton button, ButtonClickEvent event, InteractionEvent interactions);
}