package sir.cesarium.noplayerstop.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import sir.cesarium.noplayerstop.NoPlayerStopCore;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Plugin(
        id = "noplayerstop",
        name = "NoPlayerStop",
        version = "1.0.0",
        authors = {"SirCesarium"}
)
public class VelocityEntry {
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private final VelocityLauncher launcher;

    @Inject
    public VelocityEntry(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.launcher = new VelocityLauncher(server, logger);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        VelocityConfig config = new VelocityConfig(dataDirectory.resolve("config.yml"), logger);
        NoPlayerStopCore core = new NoPlayerStopCore(launcher, config);

        server.getScheduler()
                .buildTask(this, core::onTick)
                .repeat(1, TimeUnit.SECONDS)
                .schedule();

        logger.info("NoPlayerStop initialized for Velocity!");
    }

    @Subscribe
    public void onLeave(DisconnectEvent event) {
        launcher.setLastPlayerName(event.getPlayer().getUsername());
    }
}
