package sir.cesarium.noplayerstop.neoforge;

import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sir.cesarium.noplayerstop.api.IPlatformLauncher;

import java.util.logging.Level;
import java.util.logging.LogRecord;

class NeoforgeLauncher implements IPlatformLauncher {
    private static final Logger SLF4J = LoggerFactory.getLogger("NoPlayerStop");
    private MinecraftServer server;
    private String lastPlayerName = "None";

    private final java.util.logging.Logger loggerBridge = new java.util.logging.Logger("NoPlayerStop", null) {
        @Override
        public void log(LogRecord record) {
            if (record.getLevel() == Level.SEVERE) SLF4J.error(record.getMessage());
            else if (record.getLevel() == Level.WARNING) SLF4J.warn(record.getMessage());
            else SLF4J.info(record.getMessage());
        }
    };

    public void setServer(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public int getPlayerCount() {
        return server != null ? server.getPlayerCount() : 0;
    }

    @Override
    public void shutdownServer() {
        if (server != null) server.halt(false);
    }

    public void setLastPlayerName(String name) {
        this.lastPlayerName = name;
    }

    @Override
    public String getLastActivePlayerName() {

        return lastPlayerName;
    }

    @Override
    public java.util.logging.Logger getLogger() {
        return loggerBridge;
    }
}