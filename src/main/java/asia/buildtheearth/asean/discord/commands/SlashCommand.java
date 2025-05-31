package asia.buildtheearth.asean.discord.commands;

import asia.buildtheearth.asean.discord.commands.interaction.Interaction;
import github.scarsz.discordsrv.dependencies.jda.api.interactions.InteractionHook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Slash command trigger interface.
 *
 * @param <T> The command interaction class
 *
 * @see #trigger(InteractionHook, Interaction)
 */
public interface SlashCommand<T extends Interaction>  {
    /**
     * Trigger a command with pre-defined follow-up interactions and payload
     *
     * @param hook The triggered command interaction hook
     * @param payload The payload to start this command events
     */
    void trigger(@NotNull InteractionHook hook, @Nullable T payload);
}