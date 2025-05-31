package asia.buildtheearth.asean.discord.components;

import github.scarsz.discordsrv.dependencies.jda.api.interactions.components.Component;
import github.scarsz.discordsrv.dependencies.jda.internal.utils.Checks;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.bukkit.plugin.Plugin;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * A class to parse plugin registered components.
 * Will throw {@link IllegalArgumentException} on creation
 * if the given component is not registered by this plugin.
 *
 * @param <T> The component type of this referring plugin-component
 */
public class PluginComponent<T extends Component> {

    /**
     * The parsed component ID as {@link IDPattern}
     *
     * @see IDPattern#parseCustomID(String)
     */
    private final java.util.EnumMap<IDPattern, String> dataID;

    /**
     * The raw {@code custom_id} of this component
     */
    private final String rawID;

    /**
     * The instance of this plugin component
     */
    protected final T component;

    /**
     * Parse a new plugin-registered component.
     *
     * @param plugin The plugin that is supposed to register this component.
     * @param component The component data to be parsed.
     * @throws IllegalArgumentException If the component failed to be validated as plugin registered.
     */
    @Contract("_, null -> fail")
    public PluginComponent(@NotNull Plugin plugin, @Nullable T component) throws IllegalArgumentException {
        if(component == null) throw new IllegalArgumentException("Component cannot be null");

        this.component = component;
        this.rawID = component.getId();
        this.dataID = IDPattern.parseCustomID(this.rawID);

        // Invalid button ID (possibly from other bots)
        if (dataID == null)
            throw new IllegalArgumentException("Component is not created from this plugin");

        // If for whatever reason the button is not from this plugin
        if (!dataID.get(IDPattern.PLUGIN).equals(plugin.getName()))
            throw new IllegalArgumentException("Component ID is invalid");

        Checks.isSnowflake(dataID.get(IDPattern.ID), "Component ID");
        Checks.isSnowflake(dataID.get(IDPattern.USER), "Component's USER ID");
    }

    /**
     * Get the object of this plugin component
     *
     * @return The component data object
     */
    public T get() {
        return component;
    }

    /**
     * Construct a new {@link PluginComponent} instance as an optional.
     *
     * <p>Usage:</p>
     * <blockquote> {@snippet :
     * import github.scarsz.discordsrv.dependencies.jda.api.interactions.components.Button;
     *
     * class Plugin extends org.bukkit.plugin.java.JavaPlugin {
     *    private Button component = Button.success("plugin", "button");
     *    private PluginComponent<Button> button = PluginComponent.getOpt(this, component, PluginComponent::new);
     * }
     *} </blockquote>
     *
     * @param plugin The plugin that is supposed to register this component.
     * @param component The component data to be parsed.
     * @param constructor The construct to construct a {@link PluginComponent}
     * @return A new {@link PluginComponent} as provided by constructor parameter which
     *         handled the initial exception as an {@link Optional#empty() empty}.
     * @param <T> The component type to be parsed.
     * @param <V> The {@link PluginComponent} instance which will be constructed.
     */
    public static <T extends Component, V extends PluginComponent<T>>
    Optional<V> getOpt(@NotNull Plugin plugin, T component, @NotNull BiFunction<Plugin, T, V> constructor) {
        try {
            return Optional.of(constructor.apply(plugin, component));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    /**
     * Construct a new {@link PluginComponent} instance as an optional.
     *
     * @param plugin The plugin that is supposed to register this component.
     * @param component The component data to be parsed.
     * @return A new {@link PluginComponent} as provided by constructor parameter which
     *         handled the initial exception as an {@link Optional#empty() empty}.
     * @param <T> The component type to be parsed.
     */
    public static <T extends Component>
    Optional<PluginComponent<T>> getOpt(@NotNull Plugin plugin, @Nullable T component) {
        return getOpt(plugin, component, PluginComponent::new);
    }

    /**
     * Get the unique ID of this component.
     *
     * @return The ID as a string.
     */
    public @NotNull String getID() {
        return dataID.get(IDPattern.ID);
    }

    /**
     * Get the unique ID as discord snowflake.
     *
     * @return The ID parsed as unsigned long.
     */
    public long getIDLong() {
        return Long.parseUnsignedLong(dataID.get(IDPattern.ID));
    }

    /**
     * Get the optional payload of this component.
     *
     * @return The payload as a string, null if not exist.
     */
    public @Nullable String getPayload() {
        return dataID.get(IDPattern.PAYLOAD);
    }

    /**
     * Get the optional payload of this component and parse it as an integer.
     *
     * @return The payload as an integer, null if not exist.
     * @throws NumberFormatException if the payload value cannot be parsed as integer
     */
    public @Nullable Integer getIntPayload() {
        if (getPayload() == null) return null;
        else return Integer.valueOf(getPayload());
    }

    /**
     * Get the type identifier of this component.
     *
     * @return The type as a string.
     */
    public @NotNull String getType() {
        return dataID.get(IDPattern.TYPE);
    }

    /**
     * Get the user that own this component.
     *
     * @return The user ID as a string
     */
    public @NotNull String getUserID() {
        return dataID.get(IDPattern.USER);
    }

    /**
     * Get the user that own this component.
     *
     * @return The ID parsed as unsigned long.
     */
    public long getUserIDLong() {
        return Long.parseUnsignedLong(dataID.get(IDPattern.USER));
    }

    /**
     * The raw ID of this component
     *
     * @return Un-parsed component's {@code custom_id}
     */
    public String getRawID() {
        return rawID;
    }
}