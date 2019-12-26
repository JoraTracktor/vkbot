package bot;

import com.vk.api.sdk.objects.messages.Message;

public class UnknownCommand extends Command {

    public UnknownCommand(String name) {
        super(name);
    }

    @Override
    public void exec(Message message) {
        String mess = "Unknown command\n\nList of commands:\n\t/weather cityName\n\t/say";
        VKCore.getInstance().sendMessage(mess, message.getPeerId());
    }
}