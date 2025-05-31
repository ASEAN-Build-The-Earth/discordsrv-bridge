package asia.buildtheearth.asean.discord.components.api;

/**
 * Component that allows you to add text to your message
 * formatted with markdown and mention users and roles.
 */
public class TextDisplay extends ComponentV2 {

    /**
     * Create a text display with defined identifier.
     *
     * @param componentID Identifier for component
     * @param content Text that will be displayed similar to a message
     */
    public TextDisplay(int componentID, @org.jetbrains.annotations.NotNull String content) {
        super(10, componentID, "content", content);
    }

    /**
     * Create a text display.
     *
     * @param content Text that will be displayed similar to a message
     */
    public TextDisplay(@org.jetbrains.annotations.NotNull String content) {
        super(10, "content", content);
    }
}