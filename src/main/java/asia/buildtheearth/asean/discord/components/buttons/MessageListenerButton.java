package asia.buildtheearth.asean.discord.components.buttons;

import asia.buildtheearth.asean.discord.commands.interactions.InteractionEvent;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageReference;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.dependencies.jda.api.events.interaction.ButtonClickEvent;
import github.scarsz.discordsrv.dependencies.jda.api.interactions.components.ActionRow;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * A button handler to retrieve user's latest message.
 *
 * <p>This handler listens for the user's most recent message and retrieve its data to forward as a new button handler.</p>
 *
 * @see InteractiveButtonHandler
 */
public class MessageListenerButton implements InteractiveButtonHandler {

    /**
     * Message data receiver as another button handler to forward any event to.
     */
    private final Function<Message, InteractiveButtonHandler> sender;

    /**
     * Create a message listener button with a forwarding function
     *
     * @param sender The interaction sender function that will be triggered
     *               when the listener successfully resolved message data.
     */
    public MessageListenerButton(@NotNull Function<Message, InteractiveButtonHandler> sender) {
        this.sender = sender;
    }

    /**
     * Interaction data retrieved {@link #onInteracted(PluginButton, ButtonClickEvent, InteractionEvent)} of this listener.
     *
     * @param button       The {@link PluginButton} that was clicked.
     * @param event        The {@link ButtonClickEvent} from JDA.
     * @param interactions Additional interaction context or utilities.
     * @param channel      The channel that the button is listening for message.
     */
    protected record InteractionData(@NotNull PluginButton button,
                                     @NotNull ButtonClickEvent event,
                                     @NotNull InteractionEvent interactions,
                                     @NotNull TextChannel channel) {}

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if this interaction does not occur in a text channel
     */
    @Override
    public void onInteracted(@NotNull PluginButton button, @NotNull ButtonClickEvent event, @NotNull InteractionEvent interactions) {
        InteractionData interaction = new InteractionData(button, event, interactions, event.getInteraction().getTextChannel());

        MessageReference lastMsg = getInteractionLastMessage(interaction.channel());

        // Last message is not sent (it is the button message)
        if(lastMsg.getMessageIdLong() == event.getMessage().getIdLong()) {
            // edit the button as enabled to listen for message again
            if(this.onMessageNotSent(interaction))
                return;
        }

        // Resolve the message data and forward it to sender
        lastMsg.resolve().queue(message -> {
            if(this.onMessageReceived(interaction, message))
                this.sender.apply(message).onInteracted(button, event, interactions);
        }, failed -> this.onMessageRetrievingFailed(interaction, failed));
    }

    /**
     * Invoked if the button is clicked but no recent message is sent.
     *
     * <p>Override this method to define a response for this event,
     * the default response will re-enable the button to continue listening to message.</p>
     *
     * @param interaction The interaction data of this listener including all info like {@link InteractiveButtonHandler}
     * @return True cancel this interaction afterward (Default to {@code true})
     */
    protected boolean onMessageNotSent(@NotNull InteractionData interaction) {
        interaction.event().editButton(interaction.button().get().asEnabled()).queue();
        return true;
    }

    /**
     * Invoked if the message is successfully received
     *
     * <p>Override this method to define a response for this event,
     * the default response will disable the button and forward the action.</p>
     *
     * @param interaction The interaction data of this listener including all info like {@link InteractiveButtonHandler}
     * @param message      The received message instance.
     * @return True to forward the message to this listener's sender (Default to {@code true})
     */
    protected boolean onMessageReceived(@NotNull InteractionData interaction,
                                        @NotNull Message message) {
        interaction.event().editComponents(ActionRow.of(interaction.button().get().asDisabled())).queue();
        return true;
    }

    /**
     * Invoked if the message cannot be resolved.
     *
     * <p>Override this method to define a response for this event,
     * the default response will disable the button.</p>
     *
     * @param interaction The interaction data of this listener including all info like {@link InteractiveButtonHandler}
     * @param error        The {@link Throwable} error that cause the retrieval to fails.
     *
     * @see MessageReference#resolve()
     */
    protected void onMessageRetrievingFailed(@NotNull InteractionData interaction,
                                             @NotNull Throwable error) {
        interaction.event().editComponents(ActionRow.of(interaction.button().get().asDisabled())).queue();
    }

    /**
     * Get the last message of a text channel
     *
     * @param channel The channel to look for
     * @return The last message as a reference
     * @throws IllegalArgumentException If the text channel does not have valid message data
     */
    protected @NotNull MessageReference getInteractionLastMessage(@NotNull TextChannel channel) {
        try {
            long lastMsgID = channel.getLatestMessageIdLong();
            long channelID = channel.getIdLong();
            long guildID = channel.getGuild().getIdLong();

            return new MessageReference(
                lastMsgID,
                channelID,
                guildID,
                null,
                channel.getJDA()
            );
        } catch (IllegalStateException ex) {
            throw new IllegalArgumentException("Attachment listening interaction only support in a text channel, A user should not be able to trigger this");
        }
    }
}
