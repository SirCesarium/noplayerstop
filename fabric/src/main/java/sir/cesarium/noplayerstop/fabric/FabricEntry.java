package sir.cesarium.noplayerstop.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import sir.cesarium.noplayerstop.NoPlayerStopCore;

public class FabricEntry implements ModInitializer {
    private final FabricLauncher launcher = new FabricLauncher();
    private NoPlayerStopCore core;

    @Override
    public void onInitialize() {
        FabricConfig config = new FabricConfig();
        this.core = new NoPlayerStopCore(launcher, config);

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            launcher.setServer(server);
            core.onTick();
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            launcher.setLastPlayerName(handler.getPlayer().getName().getString());
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            FabricMainCommand.register(dispatcher, core);
        });
    }
}