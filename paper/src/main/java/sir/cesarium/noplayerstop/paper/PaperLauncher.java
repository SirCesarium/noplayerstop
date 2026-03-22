package sir.cesarium.noplayerstop.paper;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import sir.cesarium.noplayerstop.api.IPlatformLauncher;

import java.util.logging.Logger;

public class PaperLauncher implements IPlatformLauncher {
    private final JavaPlugin plugin;
    private String lastPlayerName = "None";

    public PaperLauncher(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public int getPlayerCount() {
        return Bukkit.getOnlinePlayers().size();
    }

    @Override
    public void shutdownServer() {
        Bukkit.shutdown();
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
        return plugin.getLogger();
    }
}
