package sir.cesarium.noplayerstop.paper.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;
import sir.cesarium.noplayerstop.NoPlayerStopCore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class MainCommand implements BasicCommand {
    private final Map<String, ISubCommand> subcommands = new HashMap<>();

    public MainCommand(NoPlayerStopCore core) {
        registerSubCommand(new ReloadSubCmd(core));
        registerSubCommand(new StatusSubCmd(core));
        registerSubCommand(new PauseSubCmd(core));
        registerSubCommand(new ResetSubCmd(core));
        registerSubCommand(new TestWebHookSubCmd(core));
    }

    private void registerSubCommand(ISubCommand cmd) {
        subcommands.put(cmd.getName().toLowerCase(), cmd);
    }

    @Override
    public void execute(@NonNull CommandSourceStack source, String @NonNull [] args) {
        if (!source.getSender().isOp() && !source.getSender().hasPermission("noplayerstop.admin")) {
            source.getSender().sendMessage("§cYou don't have enough permissions to execute this command.");
            return;
        }

        if (args.length == 0) {
            source.getSender().sendMessage("§e[NoPlayerStop] §fUsage: /nps <" + String.join("|", subcommands.keySet()) + ">");
            return;
        }

        ISubCommand sub = subcommands.get(args[0].toLowerCase());
        if (sub != null) {
            String[] subArgs = new String[args.length - 1];
            System.arraycopy(args, 1, subArgs, 0, args.length - 1);
            sub.execute(source, subArgs);
        } else {
            source.getSender().sendMessage("§cUnknown subcommand.");
        }
    }

    @Override
    public @NonNull Collection<String> suggest(@NonNull CommandSourceStack source, String[] args) {
        if (args.length <= 1) {
            String search = args.length == 1 ? args[0].toLowerCase() : "";
            return subcommands.keySet().stream()
                    .filter(s -> s.startsWith(search))
                    .toList();
        }

        ISubCommand sub = subcommands.get(args[0].toLowerCase());
        if (sub != null) {
            String[] subArgs = new String[args.length - 1];
            System.arraycopy(args, 1, subArgs, 0, args.length - 1);
            return sub.suggest(source, subArgs);
        }

        return List.of();
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp() || sender.hasPermission("noplayerstop.admin");
    }
}