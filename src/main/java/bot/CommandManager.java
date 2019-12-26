package bot;

import java.util.HashSet;

public class CommandManager {
    private static HashSet<Command> commands = new HashSet<>();

    static {
        commands.add(new WeatherCommand("/weather"));
        commands.add(new JokeCommand("/say"));
    }

    public static HashSet<Command> getCommands(){
        return commands;
    }
}
