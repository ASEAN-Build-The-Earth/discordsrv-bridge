package asia.buildtheearth.asean.discord.components.api;

import com.discordsrv.dependencies.net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for all {@code ComponentV2} implementations.
 *
 * @see Container
 * @see MediaGallery
 * @see TextDisplay
 * @see Section
 * @see Separator
 * @see TextButtonSection
 * @see TextThumbnailSection
 * @see Thumbnail
 * @see File
 * @deprecated Use {@link com.discordsrv.dependencies.net.dv8tion.jda.api.components.Component}
 */
@Deprecated(since = "2.0.0")
public abstract class ComponentV2 extends DataObject {

    /**
     * Create a component of type with 1 initial key-value pair.
     *
     * @param type The API type of this component
     * @param k1 The initial key name
     * @param v1 The initial value
     */
    public ComponentV2(int type,
                       @NotNull String k1, @NotNull Object v1) {
        super(new HashMap<>(Map.of("type", type, k1, v1)));
    }

    /**
     * Create a component of type with 2 initial key-value pair.
     *
     * @param type The API type of this component
     * @param k1 1st key
     * @param v1 1st value
     * @param k2 2nd key
     * @param v2 2nd value
     */
    public ComponentV2(int type,
                       @NotNull String k1, @NotNull Object v1,
                       @NotNull String k2, @NotNull Object v2) {
        super(new HashMap<>(Map.of("type", type, k1, v1, k2, v2)));
    }

    /**
     * Create a component of type with
     * a custom identifier and 1 initial key-value pair.
     *
     * @param type The API type of this component
     * @param id Identifier for component
     * @param k1 The initial key name
     * @param v1 The initial value
     */
    public ComponentV2(int type, int id,
                       @NotNull String k1, @NotNull Object v1) {
        super(new HashMap<>(Map.of("type", type, "id", id, k1, v1)));
    }

    /**
     * Create a component of type with
     * a custom identifier and 2 initial key-value pair.
     *
     * @param type The API type of this component
     * @param id Identifier for component
     * @param k1 1st key
     * @param v1 1st value
     * @param k2 2nd key
     * @param v2 2nd value
     */
    public ComponentV2(int type, int id,
                       @NotNull String k1, @NotNull Object v1,
                       @NotNull String k2, @NotNull Object v2) {
        super(new HashMap<>(Map.of("type", type, "id", id, k1, v1, k2, v2)));
    }

    /**
     * Create a component of type with
     * a custom identifier and 3 initial key-value pair.
     *
     * @param type The API type of this component
     * @param id Identifier for component
     * @param k1 1st key
     * @param v1 1st value
     * @param k2 2nd key
     * @param v2 2nd value
     * @param k3 3rd key
     * @param v3 3rd value
     */
    public ComponentV2(int type, int id,
                       @NotNull String k1, @NotNull Object v1,
                       @NotNull String k2, @NotNull Object v2,
                       @NotNull String k3, @NotNull Object v3) {
        super(new HashMap<>(Map.of("type", type, "id", id, k1, v1, k2, v2, k3, v3)));
    }
}