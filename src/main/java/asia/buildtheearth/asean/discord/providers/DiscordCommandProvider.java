package asia.buildtheearth.asean.discord.providers;

import asia.buildtheearth.asean.discord.commands.SlashCommand;
import asia.buildtheearth.asean.discord.commands.events.CommandEvent;
import asia.buildtheearth.asean.discord.commands.interactions.Interaction;
import asia.buildtheearth.asean.discord.commands.interactions.InteractionEvent;
import github.scarsz.discordsrv.api.commands.PluginSlashCommand;
import github.scarsz.discordsrv.api.commands.SlashCommandProvider;
import github.scarsz.discordsrv.dependencies.jda.api.entities.ChannelType;
import github.scarsz.discordsrv.dependencies.jda.api.events.interaction.SlashCommandEvent;
import github.scarsz.discordsrv.dependencies.jda.api.interactions.commands.CommandInteraction;
import github.scarsz.discordsrv.dependencies.jda.api.interactions.commands.build.CommandData;
import github.scarsz.discordsrv.dependencies.jda.api.requests.restaction.interactions.ReplyAction;
import github.scarsz.discordsrv.util.SchedulerUtil;
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
 * Managed externally by DiscordSRV API {@link SlashCommandProvider}.
 *
 * @see #register(String, CommandData...) Register slash commands
 * @see #fromClass(Class) Get the registered slash command
 * @see #putPayload(Long, Interaction) Track a slash command event w/ payload
 * @see #getAs(Class, Long) Get the tracked slash command interaction
 */
public abstract class DiscordCommandProvider implements SlashCommandProvider, InteractionEvent, CommandEvent {

    /**
     * Internal slash command object management.
     * Stores all available {@link PluginSlashCommand}
     * which is the wrapper of all slash command instances.
     */
    private final Set<PluginSlashCommand> commandSet;

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
    public abstract @NotNull org.bukkit.plugin.Plugin getPlugin();

    /**
     * Register a new guild-based slash command
     *
     * @param commands list {@link CommandData} to register
     * @param guildID The guild to register slash command in
     */
    public final void register(@NotNull String guildID, CommandData @NotNull ... commands) {
        for(CommandData command : commands) {
            PluginSlashCommand slashCommand = new PluginSlashCommand(this.getPlugin(), command, guildID);
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
    private boolean isUnknownChannel(@NotNull CommandInteraction event) {
        if(event.getChannelType() == ChannelType.UNKNOWN)
            return ifUnknownChannel(event.deferReply(true));
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
    protected abstract boolean ifUnknownChannel(@NotNull ReplyAction action);

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T extends Interaction, V extends CommandData>
    void onSlashCommand(@NotNull SlashCommandEvent event,
                        boolean ephemeral,
                        @NotNull Class<V> type,
                        @NotNull Function<V, SlashCommand<T>> resolver,
                        @NotNull Supplier<@Nullable T> payload) {

        if(isUnknownChannel(event)) return;
        else event.deferReply(ephemeral).queue();

        T interaction = payload.get();

        if(interaction != null) this.putPayload(event.getIdLong(), interaction);

        resolver.apply(this.fromClass(type)).trigger(event.getHook(), interaction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T extends Interaction, V extends CommandData>
    void onSlashCommand(@NotNull SlashCommandEvent event,
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
    @Override
    public final Set<PluginSlashCommand> getSlashCommands() {
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
    public final <T extends Interaction> void putPayload(Long eventID, T payload) {
        interactions.put(eventID, payload);

        // Payload is guaranteed to be expired in 15 minutes (15min * 60000ms / 50tick = 18000)
        SchedulerUtil.runTaskLaterAsynchronously(this.getPlugin(), () -> {
            if(interactions.remove(eventID) != null)
                onInteractionTimeout(payload.getClass());
        }, 18000);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInteractionTimeout(Class<? extends Interaction> interaction) {}
}