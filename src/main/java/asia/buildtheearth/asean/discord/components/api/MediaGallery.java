package asia.buildtheearth.asean.discord.components.api;

import github.scarsz.discordsrv.dependencies.jda.api.utils.data.DataArray;

import java.util.ArrayList;
import java.util.Map;

/**
 * Display media attachments in an organized gallery format.
 * Each item can have optional descriptions and can be marked as spoilers.
 */
public class MediaGallery extends ComponentV2 {
    /**
     * Array of media gallery item
     */
    protected final DataArray items;

    /**
     * Construct a new media gallery with empty data.
     *
     * @param componentID Identifier for component
     */
    public MediaGallery(int componentID) {
        super(12, componentID, "items", new ArrayList<>());
        this.items = this.getArray("items");
    }

    /**
     * Add a new media to this gallery.
     *
     * @param url A url or attachment
     * @param description Alt text for the media, max 1024 characters
     * @param spoiler Whether the media should be a spoiler (or blurred out)
     * @return This instance for chaining
     */
    public MediaGallery addMedia(String url, String description, boolean spoiler) {
        items.add(Map.of(
            "media", Map.of("url", url),
            "description", description,
            "spoiler", spoiler)
        );
        return this;
    }

    /**
     * Add a new media to this gallery.
     *
     * @param url A url or attachment
     * @param description Alt text for the media, max 1024 characters
     * @return This instance for chaining
     */
    public MediaGallery addMedia(String url, String description) {
        return this.addMedia(url, description, false);
    }

    /**
     * Add a new media to this gallery.
     *
     * @param url A url or attachment
     * @return This instance for chaining
     */
    public MediaGallery addMedia(String url) {
        items.add(Map.of("media", Map.of("url", url)));
        return this;
    }

}