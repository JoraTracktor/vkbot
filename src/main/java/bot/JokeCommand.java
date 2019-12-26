package bot;

import com.vk.api.sdk.objects.messages.Message;

public class JokeCommand extends Command{
    public JokeCommand(String name) {
        super(name);
    }

    @Override
    public void exec(Message message) {
        VKCore.getInstance().sendMessage("Ты пидор", message.getPeerId());
    }
}
