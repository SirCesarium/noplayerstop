package sir.cesarium.noplayerstop.paper.commands;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import sir.cesarium.noplayerstop.NoPlayerStopCore;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class ReloadSubCmd implements ISubCommand {
    private final NoPlayerStopCore core;

    public ReloadSubCmd(NoPlayerStopCore core) {
        this.core = core;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        source.getSender().sendMessage(core.reload());
    }

    @Override
    public List<String> suggest(CommandSourceStack source, String[] args) {
        return List.of();
    }
}