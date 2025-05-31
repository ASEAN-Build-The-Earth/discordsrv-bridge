package asia.buildtheearth.asean.discord.components.api;

import github.scarsz.discordsrv.dependencies.jda.api.utils.data.DataArray;

import java.util.ArrayList;
import java.util.Map;

/**
 * A component that display {@link TextDisplay} contextually with an accessory.
 */
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
    public Section(int componentID, Map<String, Object> accessory) {
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