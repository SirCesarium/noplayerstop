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

    public NoPlayerStopCore(IPlatformLauncher platform, IModConfig config) {
        this.platform = platform;
        this.config = config;
        this.startupTime = System.currentTimeMillis();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public void onTick() {
        if (!config.isEnabled() || shuttingDown) return;

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
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            platform.getLogger().warning("Webhook failed: " + ex.getMessage());
        }
    }
}
