package bot;

import com.vk.api.sdk.objects.messages.Message;
import parser.MainParser;

public class WeatherCommand extends Command {

    public WeatherCommand(String name) {
        super(name);
    }

    @Override
    public void exec(Message message) {
        String weather;
        MainParser parser = MainParser.getInstance();
        weather = parser.getWeather(message.getText());
        VKCore.getInstance().sendMessage(weather, message.getPeerId());
    }
}
