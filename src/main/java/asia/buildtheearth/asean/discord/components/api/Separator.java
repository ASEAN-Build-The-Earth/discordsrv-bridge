package asia.buildtheearth.asean.discord.components.api;

/**
 * Vertical padding and visual division between other components.
 */
public class Separator extends ComponentV2 {
    /**
     * Create a new separator.
     *
     * @param divider Whether a visual divider should be displayed in the component.
     */
    public Separator(boolean divider) {
        super(14, "divider", divider, "spacing", 1);
    }

    /**
     * Create a new separator with divider and default padding.
     */
    public Separator() {
        super(14, "divider", true, "spacing", 1);
    }

    /**
     * Create a new separator.
     *
     * @param componentID Identifier for component
     * @param divider Whether a visual divider should be displayed in the component.
     */
    public Separator(int componentID, boolean divider) {
        super(14, componentID, "divider", divider, "spacing", 1);
    }

    /**
     * Create a new separator.
     *
     * @param componentID Identifier for component
     * @param divider Whether a visual divider should be displayed in the component.
     * @param expandPadding Whether to increase the size of the divider padding
     */
    public Separator(int componentID, boolean divider, boolean expandPadding) {
        super(14, componentID, "divider", divider, "spacing", expandPadding? 2 : 1);
    }

}