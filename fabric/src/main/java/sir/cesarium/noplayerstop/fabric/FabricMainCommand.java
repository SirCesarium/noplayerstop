package sir.cesarium.noplayerstop.fabric;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import sir.cesarium.noplayerstop.NoPlayerStopCore;

import static net.minecraft.server.command.CommandManager.literal;

public class FabricMainCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, NoPlayerStopCore core) {
        dispatcher.register(literal("nps")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(ctx -> send(ctx, "§e[NoPlayerStop] §fUsage: /nps <reload|status|toggle|reset|test-webhook>"))
                .then(literal("reload").executes(ctx -> send(ctx, core.reload())))
                .then(literal("reload").executes(ctx -> send(ctx, core.reload())))
                .then(literal("status").executes(ctx -> send(ctx, core.getStatus())))
                .then(literal("toggle").executes(ctx -> send(ctx, core.togglePause())))
                .then(literal("reset").executes(ctx -> {
                    send(ctx, core.restartTimer());
                    return send(ctx, core.getStatus());
                }))
                .then(literal("test-webhook").executes(ctx -> send(ctx, core.testWebhook())))
        );
    }

    private static int send(CommandContext<ServerCommandSource> ctx, String message) {
        ctx.getSource().sendFeedback(() -> Text.literal(message), false);
        return 1;
    }
}