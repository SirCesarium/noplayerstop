package sir.cesarium.noplayerstop.velocity;

import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import sir.cesarium.noplayerstop.api.IModConfig;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class VelocityConfig implements IModConfig {
    private CommentedConfigurationNode root;
    private final YamlConfigurationLoader loader;
    private final Logger logger;

    public VelocityConfig(Path configPath, Logger logger) {
        this.logger = logger;
        if (Files.notExists(configPath)) {
            try {
                Files.createDirectories(configPath.getParent());
                try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.yml")) {
                    if (is != null) {
                        Files.copy(is, configPath);
                    } else {
                        Files.createFile(configPath);
                    }
                }
            } catch (IOException e) {
                logger.error("Could not create default configuration file", e);
            }
        }

        this.loader = YamlConfigurationLoader.builder()
                .path(configPath)
                .nodeStyle(NodeStyle.BLOCK)
                .build();
        reload();
    }

    @Override
    public void reload() {
        try {
            root = loader.load();
            logger.info("Configuration file loaded successfully.");
        } catch (IOException e) {
            logger.error("Failed to load configuration file", e);
        }
    }

    @Override
    public boolean isDisabled() {
        return !root.node("enabled").getBoolean(true);
    }

    @Override
    public int getShutdownDelay() {
        return root.node("shutdown-delay").getInt(300);
    }

    @Override
    public int getWarningTime() {
        return root.node("warning-seconds").getInt(30);
    }

    @Override
    public int getMinPlayers() {
        return root.node("min-players").getInt(0);
    }

    @Override
    public boolean isWebhookEnabled() {
        return root.node("enable-webhooks").getBoolean(false);
    }

    @Override
    public boolean isWebhookTestCommandEnabled() {
        return root.node("webhook-test-command-enabled").getBoolean(false);
    }

    @Override
    public String getWebhookUrl() {
        return root.node("webhook-url").getString("http://127.0.0.1:8000");
    }

    @Override
    public String getWebhookBody() {
        return root.node("webhook-body").getString("{\"content\": \"Server shutting down. Uptime: %time%. Last player: %last_player_active%\"}");
    }
}
