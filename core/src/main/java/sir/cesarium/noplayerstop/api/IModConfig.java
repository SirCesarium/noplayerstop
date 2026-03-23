package sir.cesarium.noplayerstop.api;

public interface IModConfig {
    boolean isDisabled();
    int getShutdownDelay();
    int getWarningTime();
    int getMinPlayers();
    boolean isWebhookEnabled();
    boolean isWebhookTestCommandEnabled();
    String getWebhookUrl();
    String getWebhookBody();

    void reload();
}
