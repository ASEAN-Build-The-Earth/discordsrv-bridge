package asia.buildtheearth.asean.discord.components.api;

import com.discordsrv.dependencies.net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;
import com.discordsrv.dependencies.net.dv8tion.jda.api.components.buttons.Button;

/**
 * A component that display {@link TextDisplay} contextually with a {@link Button}
 * 
 * @see #addTextDisplay(TextDisplay)
 * @deprecated Use {@link com.discordsrv.dependencies.net.dv8tion.jda.api.components.section.SectionContentComponent}
 */
@Deprecated(since = "2.0.0")
public class TextButtonSection extends Section {

    /**
     * Create a text-button section with empty text displays
     * 
     * @param componentID Identifier for component
     * @param button The button object of this section
     */
    public TextButtonSection(int componentID, @NotNull Button button) {
        super(componentID, (DataObject) button);
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