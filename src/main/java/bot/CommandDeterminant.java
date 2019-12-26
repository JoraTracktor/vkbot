package bot;

import com.vk.api.sdk.objects.messages.Message;
import java.util.Collection;

public class CommandDeterminant {

    public static Command getCommand(Collection<Command> commands, Message message) {
        String body = message.getText();
        for (Command command : commands) {
            if (command.name.equals(body.split(" ")[0])) {
                System.out.println("Command: " + command);
                return command;
            }
        }
        return new UnknownCommand("unknown");
    }
}
