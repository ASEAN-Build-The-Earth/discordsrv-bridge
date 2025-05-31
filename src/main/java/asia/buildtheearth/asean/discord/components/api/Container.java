package asia.buildtheearth.asean.discord.components.api;

import github.scarsz.discordsrv.dependencies.jda.api.interactions.components.ActionRow;
import github.scarsz.discordsrv.dependencies.jda.api.utils.data.DataArray;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.ArrayList;

/**
 * <a href="https://discord.com/developers/docs/components/reference#container">Container Component</a>,
 * display an embed-like wrapper to its contents.
 */
public class Container extends ComponentV2 {

    /**
     * The component array of this container
     */
    protected final DataArray components;

    /**
     * Create a new container component.
     *
     * @param componentID Identifier for component
     * @param accentColor Color for the accent on the container as RGB from 0x000000 to 0xFFFFFF
     * @param spoiler Whether the container should be a spoiler (or blurred out).
     */
    public Container(int componentID, int accentColor, boolean spoiler) {
        super(
                17,
                componentID,
                "accent_color", accentColor,
                "components", new ArrayList<>(),
                "spoiler", spoiler
        );

        this.components = this.getArray("components");
    }
    /**
     * Create a new container component.
     *
     * @param componentID Identifier for component
     * @param accentColor Color to be used as {@link Color} object
     * @param spoiler Whether the container should be a spoiler (or blurred out).
     */
    public Container(int componentID, @NotNull Color accentColor, boolean spoiler) {
        this(componentID, accentColor.getRGB() & 0xFFFFFF, spoiler);
    }

    /**
     * Create a container with auto-generated ID.
     */
    public Container() {
        super(17, "components", new ArrayList<>());
        this.components = this.getArray("components");
    }

    /**
     * Create a container with no accent color.
     *
     * @param componentID Identifier for component
     */
    public Container(int componentID) {
        super(17, componentID, "components", new ArrayList<>());
        this.components = this.getArray("components");
    }

    /**
     * Add a component to this container.
     *
     * @param component Components of the type action row, text display, section, media gallery, separator, or file
     */
    public void addComponent(@NotNull ComponentV2 component) {
        components.add(component);
    }

    /**
     * Add an interaction row to this container.
     *
     * @param component The action row to add to this container
     * @return This container for chaining
     */
    public Container addActionRow(@NotNull ActionRow component) {
        components.add(component.toData());
        return this;
    }

    /**
     * Set the accent color of this container.
     *
     * @param color Color to be used as {@link Color} object
     */
    public void setAccentColor(@NotNull Color color) {
        this.put("accent_color", color.getRGB() & 0xFFFFFF);
    }

    /**
     * Set the accent color of this container.
     *
     * @param color Color for the accent on the container as RGB from 0x000000 to 0xFFFFFF
     */
    public void setAccentColor(int color) {
        this.put("accent_color", color);
    }
}