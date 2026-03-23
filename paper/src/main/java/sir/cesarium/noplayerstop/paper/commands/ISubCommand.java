package sir.cesarium.noplayerstop.paper.commands;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.List;

public interface ISubCommand {
    String getName();
    void execute(CommandSourceStack source, String[] args);
    List<String> suggest(CommandSourceStack source, String[] args);
}
