package sir.cesarium.noplayerstop.neoforge;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import sir.cesarium.noplayerstop.NoPlayerStopCore;

public class NeoForgeMainCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, NoPlayerStopCore core) {
        dispatcher.register(Commands.literal("nps")
                .requires(source -> source.hasPermission(2))
                .executes(ctx -> send(ctx, "§e[NoPlayerStop] §fUsage: /nps <status|toggle|reset|test-webhook>"))
                .then(Commands.literal("status").executes(ctx -> send(ctx, core.getStatus())))
                .then(Commands.literal("toggle").executes(ctx -> send(ctx, core.togglePause())))
                .then(Commands.literal("reset").executes(ctx -> {
                    send(ctx, core.restartTimer());
                    return send(ctx, core.getStatus());
                }))
                .then(Commands.literal("test-webhook").executes(ctx -> send(ctx, core.testWebhook())))
        );
    }

    private static int send(CommandContext<CommandSourceStack> ctx, String message) {
        ctx.getSource().sendSystemMessage(Component.literal(message));
        return 1;
    }
}
