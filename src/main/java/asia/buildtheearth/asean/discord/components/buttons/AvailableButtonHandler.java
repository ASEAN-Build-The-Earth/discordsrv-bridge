package asia.buildtheearth.asean.discord.components.buttons;

/**
 * Definition for creating button handler,
 * conventionally for enum with each value having handler implementation.
 *
 *<blockquote>{@snippet :
 *public enum ButtonHandler implements AvailableButtonHandler {
 *    INTERACTIVE_BUTTON_HANDLER(((button, event, interactions) -> {})),
 *    SIMPLE_BUTTON_HANDLER(((button, event) -> {}));
 *
 *    private final PluginButtonHandler handler;
 *
 *    ButtonHandler(InteractiveButtonHandler handler) {
 *        this.handler = handler;
 *    }
 *
 *    ButtonHandler(SimpleButtonHandler handler) {
 *        this.handler = handler;
 *    }
 *
 *    @Override
 *    public PluginButtonHandler getHandler() {
 *        return handler;
 *    }
 *}}</blockquote>
 */
public interface AvailableButtonHandler {

    /**
     * Retrieve the handler for this button
     *
     * @return The handler implementation,
     *         Null if the component is not a button
     *         (ex. a {@link github.scarsz.discordsrv.dependencies.jda.api.interactions.components.selections.SelectionMenu SelectionMenu})
     */
    @org.jetbrains.annotations.Nullable
    PluginButtonHandler getHandler();
}