package sir.cesarium.noplayerstop.paper.commands;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import sir.cesarium.noplayerstop.NoPlayerStopCore;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class PauseSubCmd implements ISubCommand {
    private final NoPlayerStopCore core;

    public PauseSubCmd(NoPlayerStopCore core) {
        this.core = core;
    }

    @Override
    public String getName() {
        return "toggle";
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        source.getSender().sendMessage(core.togglePause());
    }

    @Override
    public List<String> suggest(CommandSourceStack source, String[] args) {
        return List.of();
    }
}