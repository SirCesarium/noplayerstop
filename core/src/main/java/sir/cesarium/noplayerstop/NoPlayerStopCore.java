package sir.cesarium.noplayerstop;

import sir.cesarium.noplayerstop.api.IModConfig;
import sir.cesarium.noplayerstop.api.IPlatformLauncher;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class NoPlayerStopCore {
    private final IPlatformLauncher platform;
    private final IModConfig config;
    private final HttpClient httpClient;
    private final long startupTime;

    private long emptySince = -1;
    private long lastWarningSent = -1;
    private boolean shuttingDown = false;
    private String lastPlayerName = "None";
    private boolean paused = false;
    private long lastTestTime = 0;

    private static final String INFO = "§bℹ §f";
    private static final String SUCCESS = "§a✔ §f";
    private static final String WARN = "§e⚠ §f";

    public NoPlayerStopCore(IPlatformLauncher platform, IModConfig config) {
        this.platform = platform;
        this.config = config;
        this.startupTime = System.currentTimeMillis();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public void onTick() {
        if (config.isDisabled() || shuttingDown || paused) return;

        int players = platform.getPlayerCount();
        String currentLast = platform.getLastActivePlayerName();
        if (currentLast != null && !currentLast.equals("None")) {
            this.lastPlayerName = currentLast;
        }

        if (players <= config.getMinPlayers()) {
            if (emptySince == -1) {
                emptySince = System.currentTimeMillis();
                platform.getLogger().info("Threshold reached (" + players + " players). Timer started.");
            }

            long secondsEmpty = (System.currentTimeMillis() - emptySince) / 1000;
            long remaining = config.getShutdownDelay() - secondsEmpty;

            if (remaining <= config.getWarningTime() && remaining > 0) {
                if (lastWarningSent != remaining) {
                    platform.getLogger().info("Server shutting down in " + remaining + " seconds...");
                    lastWarningSent = remaining;
                }
            }

            if (secondsEmpty >= config.getShutdownDelay()) {
                shuttingDown = true;
                handleShutdown(players);
            }
        } else {
            if (emptySince != -1) {
                platform.getLogger().info("Player count recovered. Timer reset.");
                emptySince = -1;
                lastWarningSent = -1;
            }
        }
    }

    private void handleShutdown(int playerCount) {
        if (config.isWebhookEnabled() && !config.getWebhookUrl().isBlank()) {
            String body = config.getWebhookBody()
                    .replace("%time%", formatDuration(System.currentTimeMillis() - startupTime))
                    .replace("%players%", String.valueOf(playerCount))
                    .replace("%last_player_active%", lastPlayerName)
                    .replace("%shutdown_delay%", String.valueOf(config.getShutdownDelay()));

            sendWebhookSync(config.getWebhookUrl(), body);
        }
        platform.shutdownServer();
    }

    private String formatDuration(long millis) {
        long s = millis / 1000;
        return String.format("%dh %dm %ds", s / 3600, (s % 3600) / 60, s % 60);
    }

    private void sendWebhookSync(String url, String content) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(5))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(content))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                platform.getLogger().info("[Webhook] Delivered successfully (HTTP " + response.statusCode() + ").");
            } else {
                platform.getLogger().warning("[Webhook] Failed! HTTP Code: " + response.statusCode());
                platform.getLogger().warning("[Webhook] Response Body: " + response.body());
            }
        } catch (Exception ex) {
            platform.getLogger().severe("[Webhook] Critical Error: " + ex.getMessage());
        }
    }

    public String reload() {
        config.reload();
        this.paused = false;
        this.emptySince = -1;
        this.lastWarningSent = -1;
        platform.getLogger().info("Configuration reloaded. Timer reset and unpaused.");
        return SUCCESS + "Configuration reloaded and system resumed.";
    }

    public String restartTimer() {
        this.emptySince = -1;
        this.lastWarningSent = -1;
        platform.getLogger().info("Shutdown timer manually reset by administrator.");
        return SUCCESS + "Shutdown timer has been reset.";
    }

    public String togglePause() {
        this.emptySince = -1;
        this.paused = !paused;

        String state = paused ? "PAUSED" : "RESUMED";
        platform.getLogger().info("Shutdown system " + state + " by administrator.");

        return paused
                ? WARN + "System §epaused§f. The server will §nnot§f auto-stop."
                : SUCCESS + "System §aresumed§f. Monitoring player activity...";
    }

    public String getStatus() {
        if (config.isDisabled()) return WARN + "System is §cDISABLED§f in config file";
        if (paused) return WARN + "System is currently §ePAUSED§f.";
        if (shuttingDown) return "§4§lSHUTDOWN IN PROGRESS...";

        if (emptySince == -1) {
            return INFO + "Status: §aMonitoring§f. Players: §6" + platform.getPlayerCount();
        }

        long remaining = config.getShutdownDelay() - ((System.currentTimeMillis() - emptySince) / 1000);
        String color = (remaining < config.getWarningTime()) ? "§c" : "§e";

        return String.format("%sStatus: %sStopping in %ds§f. Last player: §7%s",
                INFO, color, Math.max(0, remaining), lastPlayerName);
    }

    public String testWebhook() {
        if (!config.isWebhookTestCommandEnabled()) {
            return "§cWebhook test command is §ndisabled§f check config file";
        }

        long now = System.currentTimeMillis();
        if (now - lastTestTime < 5000) {
            return "§cPlease wait before testing again.";
        }
        lastTestTime = now;

        if (!config.isWebhookEnabled() || config.getWebhookUrl().isBlank()) {
            return "§cWebhook is disabled or URL is empty!";
        }

        String testBody = config.getWebhookBody()
                .replace("%time%", "0h 15m 30s (TEST)")
                .replace("%players%", "0 (TEST)")
                .replace("%last_player_active%", "TesterAdmin")
                .replace("%shutdown_delay%", String.valueOf(config.getShutdownDelay()));

        platform.getLogger().info("[Webhook] Sending test payload to: " + config.getWebhookUrl());

        sendWebhookSync(config.getWebhookUrl(), testBody);

        return "§aTest webhook sent! §7Check your Discord/Endpoint.";
    }
}
