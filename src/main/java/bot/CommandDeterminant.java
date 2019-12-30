package bot;

import com.vk.api.sdk.objects.messages.Message;
import java.util.Collection;

public class CommandDeterminant {

    public static Command getCommand(Collection<Command> commands, Message message) {
        String body = message.getText();
        body = body.substring(1, body.length()-1);
        for (Command command : commands) {
            System.out.println(command);
            if (command.name.equals(body.split(" ")[0])) {
                System.out.println("Command: " + command);
                return command;
            }
        }
        return new UnknownCommand("unknown");
    }
}
