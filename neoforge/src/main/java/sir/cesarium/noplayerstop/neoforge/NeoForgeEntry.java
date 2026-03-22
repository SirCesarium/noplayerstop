package sir.cesarium.noplayerstop.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import sir.cesarium.noplayerstop.NoPlayerStopCore;

@Mod(NeoForgeEntry.MOD_ID)
public class NeoForgeEntry {
    public static final String MOD_ID = "noplayerstop";

    private final NeoforgeLauncher launcher = new NeoforgeLauncher();
    private final NoPlayerStopCore core;

    public NeoForgeEntry(IEventBus modEventBus, ModContainer modContainer) {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        NeoForgeConfig config = new NeoForgeConfig(builder);
        modContainer.registerConfig(ModConfig.Type.SERVER, builder.build());

        this.core = new NoPlayerStopCore(launcher, config);

        NeoForge.EVENT_BUS.addListener(this::onServerTick);
        NeoForge.EVENT_BUS.addListener(this::onPlayerLeave);
    }

    private void onServerTick(ServerTickEvent.Post event) {
        launcher.setServer(event.getServer());
        core.onTick();
    }

    private void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        launcher.setLastPlayerName(event.getEntity().getName().getString());
    }
}