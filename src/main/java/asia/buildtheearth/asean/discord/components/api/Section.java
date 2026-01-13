package asia.buildtheearth.asean.discord.components.api;

import com.discordsrv.dependencies.net.dv8tion.jda.api.utils.data.DataArray;
import com.discordsrv.dependencies.net.dv8tion.jda.api.utils.data.DataObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * A component that display {@link TextDisplay} contextually with an accessory.
 *
 * @deprecated Use {@link com.discordsrv.dependencies.net.dv8tion.jda.api.components.section.Section}
 */
@Deprecated(since = "2.0.0")
class Section extends ComponentV2 {

    /**
     * Array of text components of this section
     */
    protected final DataArray components;

    /**
     * Create a new section with an accessory of type.
     *
     * @param componentID Identifier for component
     * @param accessory The raw accessory data to create this section with
     */
    public Section(int componentID, DataObject accessory) {
        super(
            9,
            componentID,
            "components", new ArrayList<>(),
            "accessory", accessory
        );

        this.components = this.getArray("components");
    }

    /**
     * Create a new section with an accessory of type.
     *
     * @param accessory The raw accessory data to create this section with
     */
    public Section(Map<String, Object> accessory) {
        super(
            9,
            "components", new ArrayList<>(),
            "accessory", accessory
        );

        this.components = this.getArray("components");
    }

    /**
     * Add a text display to this section
     *
     * @param textDisplay The {@link TextDisplay} component to be added to this section
     * @return This instance for chaining
     */
    public Section addTextDisplay(TextDisplay textDisplay) {
        this.components.add(textDisplay);
        return this;
    }
}