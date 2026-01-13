package asia.buildtheearth.asean.discord.providers;

import asia.buildtheearth.asean.discord.commands.SlashCommand;
import asia.buildtheearth.asean.discord.commands.events.CommandEvent;
import asia.buildtheearth.asean.discord.commands.interactions.Interaction;
import asia.buildtheearth.asean.discord.commands.interactions.InteractionEvent;
import com.discordsrv.api.discord.entity.interaction.command.DiscordCommand;
import com.discordsrv.api.events.discord.interaction.command.DiscordChatInputInteractionEvent;
import com.discordsrv.dependencies.net.dv8tion.jda.api.entities.channel.ChannelType;
import com.discordsrv.dependencies.net.dv8tion.jda.api.interactions.commands.build.CommandData;
import com.discordsrv.dependencies.net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Provider/Manager for discord slash command interactions.
 *
 * @see #register(String, CommandData...) Register slash commands
 * @see #fromClass(Class) Get the registered slash command
 * @see #putPayload(Long, Interaction) Track a slash command event w/ payload
 * @see #getAs(Class, Long) Get the tracked slash command interaction
 */
public abstract class DiscordCommandProvider implements PluginProvider, InteractionEvent, CommandEvent {

    /**
     * Internal slash command object management.
     * Stores all available {@link DiscordCommand}
     * which is the wrapper of all slash command instances.
     */
    private final Set<DiscordCommand> commandSet;

    /**
     * Internal commandData instance, map available class into
     * the actual {@link CommandData} instance it is registered in.
     */
    private final Map<Class<? extends CommandData>, CommandData> registered;

    /**
     * The runtime interactions from user activated slash command events.
     * Provide {@link Interaction} for each retrospective interaction.
     */
    private final Map<Long, Interaction> interactions;

    /**
     * Construct plugin base slash command provider
     */
    public DiscordCommandProvider() {
        this.commandSet = new HashSet<>();
        this.registered = new HashMap<>();
        this.interactions = new HashMap<>();
    }

    /**
     * Get the plugin of this command provider
     *
     * @return The plugin instance that will be used to register any commands
     */
    public abstract @NotNull MinecraftPlugin getPlugin();

    /**
     * Register a new guild-based slash command
     *
     * @param commands list {@link CommandData} to register
     * @param guildID The guild to register slash command in
     */
    public final void register(@NotNull String guildID, CommandData @NotNull ... commands) {
        for(CommandData command : commands) {
            DiscordCommand slashCommand = (DiscordCommand) command;
            commandSet.add(slashCommand);
            registered.put(command.getClass(), command);
        }
    }

    /**
     * Clear all registered slash command data.
     * Required an API update for changes to take effects.
     */
    public void clearCommands() {
        commandSet.clear();
        registered.clear();
    }

    /**
     * {@inheritDoc}
     */
    public final <T extends CommandData> T fromClass(Class<T> command) {
        CommandData slashCommand = registered.get(command);
        if (slashCommand == null)
            throw new IllegalStateException("[Internal Exception] "
                    + "No command instance available for class: "
                    + command.getName());
        return command.cast(slashCommand);
    }

    /**
     * If guard to check on every command because our plugin
     * does not support thread channel and will output it as {@link ChannelType#UNKNOWN}
     *
     * @param event The command that is triggered
     * @return Whether the triggered channel is valid
     */
    private boolean isUnknownChannel(@NotNull DiscordChatInputInteractionEvent event) {
        if(event.asJDA().getChannelType() == ChannelType.UNKNOWN)
            return ifUnknownChannel(event.asJDA().deferReply(true));
        else return false;
    }

    /**
     * Action to do if the event is triggered on an unknown channel.
     * <p>Unknown channel is the channel in which DiscordSRV JDA is not supported, mainly forum channel.</p>
     *
     * @param action The reply action to response to user
     * @return True to cancel this slash command event,
     *         false to trigger the command anyway
     */
    protected abstract boolean ifUnknownChannel(ReplyCallbackAction action);

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T extends Interaction, V extends CommandData>
    void onSlashCommand(@NotNull DiscordChatInputInteractionEvent event,
                        boolean ephemeral,
                        @NotNull Class<V> type,
                        @NotNull Function<V, SlashCommand<T>> resolver,
                        @NotNull Supplier<@Nullable T> payload) {

        if(isUnknownChannel(event)) return;
        else event.deferReply(ephemeral);

        T interaction = payload.get();

        if(interaction != null) this.putPayload(event.asJDA().getIdLong(), interaction);

        resolver.apply(this.fromClass(type)).trigger(event.asJDA().getHook(), interaction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T extends Interaction, V extends CommandData>
    void onSlashCommand(@NotNull DiscordChatInputInteractionEvent event,
                        boolean ephemeral,
                        @NotNull Class<V> type,
                        @NotNull Function<V, SlashCommand<T>> resolver) {
        onSlashCommand(event, ephemeral, type, resolver, () -> null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T extends Interaction> @Nullable T getAs(@NotNull Class<T> interaction, Long eventID) {
        Interaction payload = getPayload(eventID);

        if(payload == null) return null;

        if (interaction.isInstance(payload)) return interaction.cast(payload);
        else throw new ClassCastException("Payload with event id <" + Long.toUnsignedString(eventID) + "> is not of type " + interaction.getSimpleName());
    }

    /**
     * Get all registered slash command provider to be process by DiscordSRV API
     *
     * @return Set of registered provider
     */
    public final Set<DiscordCommand> getSlashCommands() {
        return commandSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void clearInteractions() {
        interactions.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void removeInteraction(Long eventID) {
        interactions.remove(eventID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Interaction getPayload(Long eventID) {
        return interactions.get(eventID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Interaction> void putPayload(Long eventID, T payload) {
        interactions.put(eventID, payload);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInteractionTimeout(Class<? extends Interaction> interaction) {}
}