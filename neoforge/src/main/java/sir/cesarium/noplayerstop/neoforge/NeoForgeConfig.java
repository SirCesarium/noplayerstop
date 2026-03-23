package sir.cesarium.noplayerstop.neoforge;

import net.neoforged.fml.config.ConfigTracker;
import net.neoforged.neoforge.common.ModConfigSpec;
import sir.cesarium.noplayerstop.api.IModConfig;

public class NeoForgeConfig implements IModConfig {
    private final ModConfigSpec.BooleanValue enabled;
    private final ModConfigSpec.IntValue shutdownDelay;
    private final ModConfigSpec.IntValue warningSeconds;
    private final ModConfigSpec.IntValue minPlayers;
    private final ModConfigSpec.BooleanValue enableWebhooks;
    private final ModConfigSpec.BooleanValue enableWebhookTestCommand;
    private final ModConfigSpec.ConfigValue<String> webhookUrl;
    private final ModConfigSpec.ConfigValue<String> webhookBody;

    public NeoForgeConfig(ModConfigSpec.Builder builder) {
        builder.push("GeneralSettings");

        enabled = builder
                .comment("Enable or disable the automatic shutdown system.")
                .define("enabled", true);

        shutdownDelay = builder
                .comment("Seconds to wait before shutting down after player count drops below threshold.")
                .defineInRange("shutdown-delay", 300, 10, 3600);

        warningSeconds = builder
                .comment("Seconds before shutdown to start broadcasting warnings in console/logs.")
                .defineInRange("warning-seconds", 30, 0, 300);

        minPlayers = builder
                .comment("Threshold of players. If count is less than or equal to this, timer starts.")
                .defineInRange("min-players", 0, 0, 100);

        builder.pop();

        builder.push("WebhookNotifications");

        enableWebhooks = builder
                .comment("Whether to allow the /nps test-webhook command.")
                .comment("Set false to prevent potential spam/abuse by administrators.")
                .define("enable-webhooks", false);

        enableWebhookTestCommand = builder
                .comment("Whether to send a JSON POST request when the server shuts down.")
                .define("webhook-test-command-enabled", false);

        webhookUrl = builder
                .comment("Target URL for the webhook (e.g. Discord).")
                .define("webhook-url", "http://127.0.0.1:8000");

        webhookBody = builder
                .comment("JSON payload. Variables: %time%, %players%, %last_player_active%, %shutdown_delay%")
                .define("webhook-body", "{\"content\": \"Server shutting down. Uptime: %time%. Last player: %last_player_active%\"}");

        builder.pop();
    }

    @Override
    public boolean isDisabled() {
        return !enabled.get();
    }

    @Override
    public int getShutdownDelay() {
        return shutdownDelay.get();
    }

    @Override
    public int getWarningTime() {
        return warningSeconds.get();
    }

    @Override
    public boolean isWebhookEnabled() {
        return enableWebhooks.get();
    }

    @Override
    public boolean isWebhookTestCommandEnabled() {
        return enableWebhookTestCommand.get();
    }

    @Override
    public String getWebhookUrl() {
        return webhookUrl.get();
    }

    @Override
    public String getWebhookBody() {
        return webhookBody.get();
    }

    @Override
    public void reload() {
    }

    @Override
    public int getMinPlayers() {
        return minPlayers.get();
    }
}
