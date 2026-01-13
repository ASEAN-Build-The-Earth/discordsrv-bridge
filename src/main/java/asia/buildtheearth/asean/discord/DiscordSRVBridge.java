package asia.buildtheearth.asean.discord;

import com.discordsrv.api.player.DiscordSRVPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.discordsrv.api.DiscordSRV;
import com.discordsrv.dependencies.net.dv8tion.jda.api.JDA;
import com.discordsrv.api.discord.entity.interaction.command.DiscordCommand;
import com.discordsrv.api.module.Module;

/**
 * Bridge interface for integrating with the {@link DiscordSRV DiscordSRV-Ascension} plugin.
 * <p>
 * Provides default methods that delegate to corresponding
 * methods in the DiscordSRV API.
 * <p>
 * Implement this interface to override specific behaviors.
 * Otherwise, the default methods serve as direct pass through to the DiscordSRV implementation.
 *
 * @see #getJDA()
 * @see #discordIsReady()
 * @see #subscribeSRV(Module)
 * @see #unsubscribeSRV(Module)
 * @see #addSlashCommand(DiscordCommand)
 * @see #removeSlashCommand(DiscordCommand)
 * @see #getAsDiscordMember(java.util.UUID)
 */
public interface DiscordSRVBridge {

    /**
     * Get the JDA instance of this plugin
     *
     * @return The instance, {@code Null} if {@link #discordIsReady()} return {@code false}
     * @see com.discordsrv.api.DiscordSRV#jda()
     */
    default JDA getJDA() throws IllegalStateException {
        return DiscordSRV.get().jda();
    }

    /**
     * Subscribe an event listener to DiscordSRV
     *
     * @param module A {@link DiscordSRV} listener with available method,
     *               must implement {@link Module}.<br/>
     *               Annotate methods with {@link com.discordsrv.api.eventbus.Subscribe}
     *               to listen to DiscordSRV event bus.
     * @see DiscordSRV#registerModule(Module)
     */
    default void subscribeSRV(Module module) throws IllegalStateException {
        DiscordSRV.get().registerModule(module);
    }

    /**
     * Unsubscribe an event listener from DiscordSRV
     *
     * @param module Subscribed {@link DiscordSRV} listener.
     * @see DiscordSRV#unregisterModule(Module)
     */
    default void unsubscribeSRV(Module module) throws IllegalStateException {
        DiscordSRV.get().unregisterModule(module);
    }

    /**
     * Register a provider for all available discord slash command(s).
     *
     * @param command The {@link DiscordCommand}
     * @see com.discordsrv.api.discord.DiscordAPI#registerCommand(DiscordCommand)
     */
    default void addSlashCommand(DiscordCommand command) throws IllegalStateException {
        DiscordSRV.get().discordAPI().registerCommand(command);
    }

    /**
     * Remove a registered slash command provider.
     *
     * @param provider The DiscordSRV registered provider instance.
     * @see java.util.Set#remove(Object)
     */
    default void removeSlashCommand(@NotNull DiscordCommand provider) throws IllegalStateException {
        DiscordSRV.get().discordAPI().unregisterCommand(provider);
    }

    /**
     * Check if discord JDA instance is ready and initialized.
     *
     * @return Is DiscordSRV status set to ready.
     * @see DiscordSRV#isReady
     */
    default boolean discordIsReady() {
        return DiscordSRV.optional().map(DiscordSRV::isReady).orElse(false);
    }

    /**
     * Get a player's linked discord account and return null if not existed.
     *
     * @param uniqueID The {@link java.util.UUID uuid} to look for linked account
     * @return {@link DiscordSRVPlayer} object or null if the player have no linked discord account
     * @throws IllegalArgumentException Might throw if DiscordSRV has a problem with the account link manager
     * @see com.discordsrv.api.player.PlayerProvider#player(java.util.UUID) AccountLinkManager
     */
    @Nullable
    default DiscordSRVPlayer getAsDiscordMember(@NotNull java.util.UUID uniqueID) throws IllegalStateException {
        return DiscordSRV.get().playerProvider().player(uniqueID);
    }
}