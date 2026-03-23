package sir.cesarium.noplayerstop.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.LoggerFactory;
import sir.cesarium.noplayerstop.api.IPlatformLauncher;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class VelocityLauncher implements IPlatformLauncher {
    private final ProxyServer server;
    private final org.slf4j.Logger SLF4J;
    private String lastPlayerName = "None";

    public VelocityLauncher(ProxyServer server, org.slf4j.Logger logger) {
        this.server = server;
        this.SLF4J = logger;
    }

    private final java.util.logging.Logger loggerBridge = new java.util.logging.Logger("NoPlayerStop", null) {
        @Override
        public void log(LogRecord record) {
            if (record.getLevel() == Level.SEVERE) SLF4J.error(record.getMessage());
            else if (record.getLevel() == Level.WARNING) SLF4J.warn(record.getMessage());
            else SLF4J.info(record.getMessage());
        }
    };

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
        return loggerBridge;
    }
}
