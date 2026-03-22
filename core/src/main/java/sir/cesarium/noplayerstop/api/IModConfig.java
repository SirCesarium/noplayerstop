package sir.cesarium.noplayerstop.api;

public interface IModConfig {
    boolean isEnabled();
    int getShutdownDelay();
    int getWarningTime();
    int getMinPlayers();
    boolean isWebhookEnabled();
    String getWebhookUrl();
    String getWebhookBody();
}
