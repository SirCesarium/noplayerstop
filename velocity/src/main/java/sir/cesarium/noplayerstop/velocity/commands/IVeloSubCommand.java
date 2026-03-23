package sir.cesarium.noplayerstop.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import java.util.List;

public interface IVeloSubCommand {
    String getName();
    void execute(CommandSource source, String[] args);
    List<String> suggest(CommandSource source, String[] args);
}
