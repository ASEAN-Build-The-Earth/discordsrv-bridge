package asia.buildtheearth.asean.discord.components.api;

import org.jetbrains.annotations.NotNull;
import github.scarsz.discordsrv.dependencies.jda.api.interactions.components.Button;

/**
 * A component that display {@link TextDisplay} contextually with a {@link Button}
 * 
 * @see #addTextDisplay(TextDisplay) 
 */
public class TextButtonSection extends Section {

    /**
     * Create a text-button section with empty text displays
     * 
     * @param componentID Identifier for component
     * @param button The button object of this section
     */
    public TextButtonSection(int componentID, @NotNull Button button) {
        super(componentID, button.toData().toMap());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TextButtonSection addTextDisplay(TextDisplay textDisplay) {
        this.components.add(textDisplay);
        return this;
    }
}