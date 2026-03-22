package sir.cesarium.noplayerstop.fabric;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sir.cesarium.noplayerstop.api.IModConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class FabricConfig implements IModConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("NoPlayerStop");
    private final Path configPath = FabricLoader.getInstance().getConfigDir().resolve("noplayerstop.properties");
    private final Properties properties = new Properties();

    public FabricConfig() {
        load();
    }

    private void load() {
        if (Files.exists(configPath)) {
            try (InputStream is = Files.newInputStream(configPath)) {
                properties.load(is);
            } catch (IOException e) {
                LOGGER.error("Failed to load configuration", e);
            }
        } else {
            save();
        }
    }

    private void save() {
        try (BufferedWriter writer = Files.newBufferedWriter(configPath, StandardCharsets.UTF_8)) {
            writer.write("# ------------------------------------------------------------\n");
            writer.write("# GeneralSettings\n");
            writer.write("# ------------------------------------------------------------\n\n");

            writer.write("# Enable or disable the automatic shutdown system.\n");
            writeProp(writer, "enabled", "true");

            writer.write("\n# Seconds to wait before shutting down after player count drops below threshold.\n");
            writeProp(writer, "shutdown-delay", "300");

            writer.write("\n# Seconds before shutdown to start broadcasting warnings in console/logs.\n");
            writeProp(writer, "warning-seconds", "30");

            writer.write("\n# Threshold of players. If count is less than or equal to this, timer starts.\n");
            writeProp(writer, "min-players", "0");

            writer.write("\n\n# ------------------------------------------------------------\n");
            writer.write("# WebhookNotifications\n");
            writer.write("# ------------------------------------------------------------\n\n");

            writer.write("# Whether to send a JSON POST request when the server shuts down.\n");
            writeProp(writer, "enable-webhooks", "false");

            writer.write("\n# Target URL for the webhook (e.g. Discord).\n");
            writeProp(writer, "webhook-url", "http://127.0.0.1:8000");

            writer.write("\n# JSON payload. Variables: %time%, %players%, %last_player_active%, %shutdown_delay%\n");
            writeProp(writer, "webhook-body", "{\"content\": \"Server shutting down. Uptime: %time%. Last player: %last_player_active%\"}");

        } catch (IOException e) {
            LOGGER.error("Failed to save configuration", e);
        }
    }

    private void writeProp(BufferedWriter writer, String key, String def) throws IOException {
        String value = properties.getProperty(key, def);
        properties.setProperty(key, value);
        writer.write(key + "=" + value + "\n");
    }

    @Override public boolean isEnabled() { return Boolean.parseBoolean(properties.getProperty("enabled", "true")); }
    @Override public int getShutdownDelay() { return getInt("shutdown-delay", 300); }
    @Override public int getWarningTime() { return getInt("warning-seconds", 30); }
    @Override public int getMinPlayers() { return getInt("min-players", 0); }
    @Override public boolean isWebhookEnabled() { return Boolean.parseBoolean(properties.getProperty("enable-webhooks", "false")); }
    @Override public String getWebhookUrl() { return properties.getProperty("webhook-url", "http://127.0.0.1:8000"); }
    @Override public String getWebhookBody() { return properties.getProperty("webhook-body", "{\"content\": \"...\"}"); }

    private int getInt(String key, int def) {
        try {
            return Integer.parseInt(properties.getProperty(key, String.valueOf(def)));
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
