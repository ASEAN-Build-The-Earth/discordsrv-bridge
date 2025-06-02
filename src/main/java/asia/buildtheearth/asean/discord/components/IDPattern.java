package asia.buildtheearth.asean.discord.components;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Plugin Component's ID pattern that is
 * used to parse component's {@code custom_id} payload.
 *
 * <p>Refer to {@link asia.buildtheearth.asean.discord.providers.ComponentProvider}
 * for creating a component with a valid ID pattern that can be parse by this class.</p>
 *
 * @see #PLUGIN
 * @see #TYPE
 * @see #ID
 * @see #USER
 * @see #PAYLOAD
 * @see #parseCustomID(String)
 */
public enum IDPattern {
    /** The plugin name owning this component */
    PLUGIN("plugin"),
    /** The component's unique type identifier */
    TYPE("type"),
    /** The component's unique snowflake ID */
    ID("id"),
    /** The component's owner snowflake ID */
    USER("user"),
    /** The optional payload of a component */
    PAYLOAD("payload");

    /**
     * Group token for each ID
     */
    private final String group;

    /**
     * Plugin interaction button {@code custom_id} convention.
     * <p>
     * Matches component IDs in the format: {@code <plugin>/<type>/<id>/<user>} or {@code <plugin>/<type>/<id>/<user>/<payload>}.
     * <p>
     * Regex pattern used: {@code "^(?<plugin>\\w+)/(?<type>\\w+)/(?<id>\\w+)/(?<user>\\w+)(?:/(?<payload>.*?))?/?$"}
     */
    public static final Pattern COMPONENT_PATTERN = Pattern.compile(
        String.format(
            "^(?<%s>\\w+)/(?<%s>\\w+)/(?<%s>\\w+)/(?<%s>\\w+)(?:/(?<%s>.*?))?/?$",
            PLUGIN.group, TYPE.group, ID.group, USER.group, PAYLOAD.group
        )
    );

    IDPattern(@NotNull String group) {
        this.group = group;
    }

    /**
     * Parses a {@code custom_id} string to extract the components based on the {@link IDPattern} convention.
     * The returned {@link EnumMap} maps {@link IDPattern} values to their corresponding extracted string values.
     *
     * @param customId the {@code custom_id} string to be parsed
     * @return an {@link EnumMap} with the extracted components, or {@code null} if the {@code custom_id} doesn't match the pattern
     */
    @Nullable
    public static EnumMap<IDPattern, String> parseCustomID(@NotNull String customId) {
        Matcher matcher = COMPONENT_PATTERN.matcher(customId);
        if (!matcher.matches()) return null;

        EnumMap<IDPattern, String> result = new EnumMap<>(IDPattern.class);
        for (IDPattern component : values()) {

            String value = matcher.group(component.group);
            if (value != null) result.put(component, value);
        }
        return result;
    }
}