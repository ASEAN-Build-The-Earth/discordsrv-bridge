package asia.buildtheearth.asean.discord.components.api;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Display an uploaded file as an attachment to the message
 * and reference it in the component.
 */
public class File extends ComponentV2 {

    /**
     * Create a file component with full context.
     *
     * @param componentID Identifier for component
     * @param attachment attachment references using the {@code attachment://<filename>} syntax
     * @param spoiler Whether the media should be a spoiler (or blurred out)
     */
    public File(int componentID, @NotNull String attachment, boolean spoiler) {
        super(13, componentID, "file", Map.of("url", attachment), "spoiler", spoiler);
    }

    /**
     * Create a file component with attachment and id.
     *
     * @param componentID Identifier for component
     * @param attachment attachment references using the {@code attachment://<filename>} syntax
     */
    public File(int componentID, @NotNull String attachment) {
        super(13, componentID, "file", Map.of("url", attachment));
    }

    /**
     * Create a file component with attachment.
     *
     * @param attachment attachment references using the {@code attachment://<filename>} syntax
     */
    public File(@NotNull String attachment) {
        super(13, "file", Map.of("url", attachment));
    }
}