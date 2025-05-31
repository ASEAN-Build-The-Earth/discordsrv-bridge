package asia.buildtheearth.asean.discord.components.api;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Content component that is a small image only usable as an accessory in a {@link Section}.
 *
 * @see TextThumbnailSection
 */
public class Thumbnail extends ComponentV2 {

    /**
     * Create a new thumbnail data.
     *
     * @param componentID Identifier for component
     * @param url A url or attachment
     * @param description Alt text for the media, max 1024 characters
     */
    public Thumbnail(int componentID, @NotNull String url, @NotNull String description) {
        super(
            11,
            componentID,
            "media", Map.of("url", url),
            "description", description
        );
    }

    /**
     * Create a new thumbnail data.
     *
     * @param url A url or attachment
     * @param description Alt text for the media, max 1024 characters
     */
    public Thumbnail(@NotNull String url, @NotNull String description) {
        super(
            11,
            "media", Map.of("url", url),
            "description", description
        );
    }

    /**
     * Create a new thumbnail data.
     *
     * @param url A url or attachment
     */
    public Thumbnail(@NotNull String url) {
        super(11, "media", Map.of("url", url));
    }
}