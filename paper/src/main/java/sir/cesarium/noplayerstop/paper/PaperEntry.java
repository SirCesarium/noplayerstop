package sir.cesarium.noplayerstop.paper;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import sir.cesarium.noplayerstop.NoPlayerStopCore;
import sir.cesarium.noplayerstop.paper.commands.MainCommand;

@SuppressWarnings("UnstableApiUsage")
public class PaperEntry extends JavaPlugin implements Listener {
    private final PaperLauncher launcher = new PaperLauncher(this);
    private NoPlayerStopCore core;

    @Override
    public void onEnable() {
        PaperConfig config = new PaperConfig(this);
        core = new NoPlayerStopCore(launcher, config);

        Bukkit.getPluginManager().registerEvents(this, this);

        // Run the core tick every second (20 ticks)
        Bukkit.getScheduler().runTaskTimer(this, core::onTick, 20L, 20L);

        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, this::registerCommands);
    }

    public void registerCommands(ReloadableRegistrarEvent<Commands> ev) {
        final Commands commands = ev.registrar();

        commands.register("nps", "NoPlayerStop Commands", new MainCommand(core));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        launcher.setLastPlayerName(event.getPlayer().getName());
    }
}