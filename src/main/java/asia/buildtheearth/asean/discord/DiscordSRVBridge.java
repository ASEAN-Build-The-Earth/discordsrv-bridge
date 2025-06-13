package asia.buildtheearth.asean.discord;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.ApiManager;
import github.scarsz.discordsrv.api.commands.SlashCommandProvider;
import github.scarsz.discordsrv.api.events.DiscordReadyEvent;
import github.scarsz.discordsrv.dependencies.jda.api.JDA;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Bridge interface for integrating with the {@link DiscordSRV} plugin.
 * <p>
 * Provides default methods that delegate to corresponding
 * methods in the DiscordSRV API.
 * <p>
 * Implement this interface to override specific behaviors.
 * Otherwise, the default methods serve as direct pass through to the DiscordSRV implementation.
 *
 * @see #getJDA()
 * @see #discordIsReady()
 * @see #subscribeSRV(Object)
 * @see #unsubscribeSRV(Object)
 * @see #addSlashCommandProvider(SlashCommandProvider)
 * @see #removeSlashCommandProvider(SlashCommandProvider)
 * @see #updateSlashCommands()
 * @see #getMainGuild()
 * @see #getAsDiscordMember(OfflinePlayer)
 */
public interface DiscordSRVBridge {

    /**
     * Get the JDA instance of this plugin
     *
     * @return The instance, {@code Null} if {@link #discordIsReady()} return {@code false}
     * @see DiscordSRV#getJda()
     */
    default JDA getJDA() {
        return DiscordSRV.getPlugin().getJda();
    }

    /**
     * Subscribe an event listener to DiscordSRV
     *
     * @param listener A {@link DiscordSRV} listener with available method
     *                 annotated with {@link github.scarsz.discordsrv.api.Subscribe}
     * @see ApiManager#subscribe(Object)
     */
    default void subscribeSRV(Object listener) {
        DiscordSRV.api.subscribe(listener);
    }

    /**
     * Unsubscribe an event listener from DiscordSRV
     *
     * @param listener Subscribed {@link DiscordSRV} listener.
     * @see ApiManager#unsubscribe(Object)
     */
    default boolean unsubscribeSRV(Object listener) {
        return DiscordSRV.api.unsubscribe(listener);
    }

    /**
     * Register a provider for all available discord slash command(s).
     *
     * @param provider The provider which contain {@link SlashCommandProvider#getSlashCommands()}
     * @see ApiManager#addSlashCommandProvider(SlashCommandProvider)
     */
    default void  addSlashCommandProvider(SlashCommandProvider provider) {
        DiscordSRV.api.addSlashCommandProvider(provider);
    }

    /**
     * Remove a registered slash command provider.
     *
     * @param provider The DiscordSRV registered provider instance.
     * @return The result of removing the provider
     * @see java.util.Set#remove(Object)
     */
    default boolean removeSlashCommandProvider(@NotNull SlashCommandProvider provider) {
        return DiscordSRV.api.removeSlashCommandProvider(provider);
    }

    /**
     * Update the slash command provider for this plugin.
     *
     * <p>This resolve to a PUT request to discord API
     * which will update the command data if changes is detected.</p>
     * {@snippet :
     * github.scarsz.discordsrv.dependencies.jda.internal.requests.Route.put(
     *      "applications/{application_id}/guilds/{guild_id}/commands"
     * );}
     *
     * @see ApiManager#updateSlashCommands()
     * @see github.scarsz.discordsrv.dependencies.jda.internal.requests.Route.Interactions#UPDATE_GUILD_COMMANDS
     */
    default void updateSlashCommands() {
        DiscordSRV.api.updateSlashCommands();
    }

    /**
     * Check if discord JDA instance is ready and initialized.
     *
     * @return Is DiscordSRV {@link DiscordReadyEvent} has been called.
     * @see DiscordSRV#isReady
     */
    default boolean discordIsReady() {
        return DiscordSRV.isReady;
    }

    /**
     * Get the main guild of this plugin registered by {@link DiscordSRV}
     *
     * @return The JDA {@link Guild} entity or {@code null}
     *         if JDA is not {@link #discordIsReady() ready} to fetch for guild information.
     * @see DiscordSRV#getMainGuild()
     */
    default Guild getMainGuild() {
        return DiscordSRV.getPlugin().getMainGuild();
    }

    /**
     * Get a player's linked discord account and return null if not existed.
     *
     * @param player The {@link OfflinePlayer player} to look for linked account
     * @return JDA {@link Member} object or null if the player have no linked discord account
     * @throws IllegalArgumentException Might throw if DiscordSRV has a problem with the account link manager
     * @see github.scarsz.discordsrv.objects.managers.AccountLinkManager#getDiscordId(java.util.UUID) AccountLinkManager
     * @see DiscordUtil#getMemberById(String)
     */
    @Nullable
    default Member getAsDiscordMember(@NotNull OfflinePlayer player) {
        String userId = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId());
        if(userId != null) return DiscordUtil.getMemberById(userId);
        else return null;
    }
}