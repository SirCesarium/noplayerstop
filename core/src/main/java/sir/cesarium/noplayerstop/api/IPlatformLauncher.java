package sir.cesarium.noplayerstop.api;

import java.util.logging.Logger;

public interface IPlatformLauncher {
    int getPlayerCount();
    void shutdownServer();
    String getLastActivePlayerName();
    Logger getLogger();
}
