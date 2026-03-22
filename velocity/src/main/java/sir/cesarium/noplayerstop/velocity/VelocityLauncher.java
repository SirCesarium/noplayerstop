package sir.cesarium.noplayerstop.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import sir.cesarium.noplayerstop.api.IPlatformLauncher;

import java.util.logging.Logger;

public class VelocityLauncher implements IPlatformLauncher {
    private final ProxyServer server;
    private final org.slf4j.Logger logger;
    private String lastPlayerName = "None";

    public VelocityLauncher(ProxyServer server, org.slf4j.Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Override
    public int getPlayerCount() {
        return server.getPlayerCount();
    }

    @Override
    public void shutdownServer() {
        server.shutdown();
    }

    public void setLastPlayerName(String name) {
        this.lastPlayerName = name;
    }

    @Override
    public String getLastActivePlayerName() {
        return lastPlayerName;
    }

    @Override
    public Logger getLogger() {
        return Logger.getLogger("NoPlayerStop");
    }
}
