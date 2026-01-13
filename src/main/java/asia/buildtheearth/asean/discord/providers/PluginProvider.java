package asia.buildtheearth.asean.discord.providers;

/**
 * Provider for {@link MinecraftPlugin}
 *
 * @see #getPlugin()
 */
public interface PluginProvider {

    /**
     * The provided plugin.
     *
     * @return The plugin instance.
     */
    MinecraftPlugin getPlugin();
}
