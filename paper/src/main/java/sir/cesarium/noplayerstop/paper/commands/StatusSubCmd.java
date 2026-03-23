package sir.cesarium.noplayerstop.paper.commands;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import sir.cesarium.noplayerstop.NoPlayerStopCore;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class StatusSubCmd implements ISubCommand {
    private final NoPlayerStopCore core;

    public StatusSubCmd(NoPlayerStopCore core) {
        this.core = core;
    }

    @Override
    public String getName() {
        return "status";
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        String status = core.getStatus();

        source.getSender().sendMessage(status);
    }

    @Override
    public List<String> suggest(CommandSourceStack source, String[] args) {
        return List.of();
    }
}