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
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if this interaction does not occur in a text channel
     */
    @Override
    public void onInteracted(@NotNull PluginButton button, @NotNull ButtonClickEvent event, @NotNull InteractionEvent interactions) {
        TextChannel channel = event.getInteraction().getTextChannel();
        MessageReference lastMsg = getInteractionLastMessage(channel);

        // Last message is not sent (it is the button message)
        if(lastMsg.getMessageIdLong() == event.getMessage().getIdLong()) {
            // edit the button as enabled to listen for message again
            event.editButton(button.get().asEnabled()).queue();
            return;
        }

        // Resolve the message data and forward it to sender
        lastMsg.resolve().queue((message -> {
            event.editComponents(ActionRow.of(button.get().asDisabled())).queue();
            this.sender.apply(message).onInteracted(button, event, interactions);
        }));
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
