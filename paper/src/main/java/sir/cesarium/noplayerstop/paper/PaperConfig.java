package sir.cesarium.noplayerstop.paper;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import sir.cesarium.noplayerstop.api.IModConfig;

public class PaperConfig implements IModConfig {
    private final JavaPlugin plugin;

    public PaperConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
    }

    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    @Override
    public boolean isDisabled() {
        return !getConfig().getBoolean("enabled", true);
    }

    @Override
    public int getShutdownDelay() {
        return getConfig().getInt("shutdown-delay", 300);
    }

    @Override
    public int getWarningTime() {
        return getConfig().getInt("warning-seconds", 30);
    }

    @Override
    public int getMinPlayers() {
        return getConfig().getInt("min-players", 0);
    }

    @Override
    public boolean isWebhookEnabled() {
        return getConfig().getBoolean("enable-webhooks", false);
    }

    @Override
    public boolean isWebhookTestCommandEnabled() {
        return getConfig().getBoolean("webhook-test-command-enabled", false);
    }

    @Override
    public String getWebhookUrl() {
        return getConfig().getString("webhook-url", "http://127.0.0.1:8000");
    }

    @Override
    public String getWebhookBody() {
        return getConfig().getString("webhook-body", "{\"content\": \"Server shutting down. Uptime: %time%. Last player: %last_player_active%\"}");
    }

    @Override
    public void reload() {
        plugin.reloadConfig();
    }
}
