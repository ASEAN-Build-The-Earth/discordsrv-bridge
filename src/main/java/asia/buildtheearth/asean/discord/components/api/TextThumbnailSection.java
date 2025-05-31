package asia.buildtheearth.asean.discord.components.api;

import org.jetbrains.annotations.NotNull;

/**
 * A component that display {@link TextDisplay} contextually with a {@link Thumbnail}
 *
 * @see #addTextDisplay(TextDisplay)
 */
public class TextThumbnailSection extends Section {

    /**
     * Create a text-thumbnail section with empty text displays.
     *
     * @param componentID Identifier for component
     * @param thumbnail The thumbnail data of this section
     */
    public TextThumbnailSection(int componentID, @NotNull Thumbnail thumbnail) {
        super(componentID, thumbnail.toMap());
    }

    /**
     * Create a text-thumbnail section with empty text displays.
     *
     * @param thumbnail The  thumbnail data of this section
     */
    public TextThumbnailSection(@NotNull Thumbnail thumbnail) {
        super(thumbnail.toMap());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TextThumbnailSection addTextDisplay(TextDisplay textDisplay) {
        this.components.add(textDisplay);
        return this;
    }
}