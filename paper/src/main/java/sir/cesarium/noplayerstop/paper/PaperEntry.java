package sir.cesarium.noplayerstop.paper;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import sir.cesarium.noplayerstop.NoPlayerStopCore;

public class PaperEntry extends JavaPlugin implements Listener {
    private final PaperLauncher launcher = new PaperLauncher(this);

    @Override
    public void onEnable() {
        PaperConfig config = new PaperConfig(this);
        NoPlayerStopCore core = new NoPlayerStopCore(launcher, config);

        Bukkit.getPluginManager().registerEvents(this, this);

        // Run the core tick every second (20 ticks)
        Bukkit.getScheduler().runTaskTimer(this, core::onTick, 20L, 20L);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        launcher.setLastPlayerName(event.getPlayer().getName());
    }
}