package asia.buildtheearth.asean.discord.commands.interactions;

import github.scarsz.discordsrv.dependencies.jda.api.interactions.commands.build.CommandData;

/**
 * Interaction handler for all active slash-command interactions.
 */
public interface InteractionEvent {

    /**
     * Remove an interaction event from available cache.
     *
     * @param eventID The event ID to remove
     */
    void removeInteraction(Long eventID);

    /**
     * Clear all plugin created interactions
     */
    void clearInteractions();

    /**
     * Get the cached interaction data by event ID
     *
     * @param eventID The event ID that this interaction is registered with
     * @return The payload as interface
     */
    Interaction getPayload(Long eventID);

    /**
     * Register an interaction event with specify payload data.
     *
     * <p>Payload is expected to be handled by {@link #removeInteraction(Long)} when the interaction has ended</p>
     *
     * @param eventID The event ID to register as discord snowflake long
     * @param payload The payload to put as the interaction data
     * @param <T> Payload type to put as
     */
    <T extends Interaction> void putPayload(Long eventID, T payload);

    /**
     * Get an interaction data as the specify class
     * by casting the restored interaction by event ID with the class.
     *
     * @param interaction The interaction class to get as
     * @param eventID The interaction ID to get, must be able to case to interaction class
     * @return The interaction payload
     * @param <T> The interaction class that the payload is registered as
     * @throws ClassCastException If the event does not related to specified interaction class
     */
    <T extends Interaction> T getAs(Class<T> interaction, Long eventID);

    /**
     * Get a command data as a specified class.
     *
     * @param <T> Template which defines the command data object.
     * @param command The base class to get the command data as.
     * @return The command data instance that the specified class.
     * @throws IllegalArgumentException If the command data has not been registered by the provider.
     */
    <T extends CommandData> T fromClass(Class<T> command);


    /**
     * Called when a registered interaction times out without being handled.
     *
     * <p>Override this method to perform cleanup, logging, or other timeout-related behavior.
     * The default implementation does nothing.</p>
     *
     * @param interaction the class type of the interaction that timed out
     */
    void onInteractionTimeout(Class<? extends Interaction> interaction);
}