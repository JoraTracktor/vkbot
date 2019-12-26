package bot;

import com.vk.api.sdk.objects.messages.Message;

public abstract class Command {

    public final String name;
    public Command(String name){
        this.name = name;
    }

    public abstract void exec(Message message);

    @Override
    public String toString() {
        return String.format("name: %s",this.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Command){
            if (name.equals(((Command) obj).name)){
                return true;
            }
        }
        return false;
    }
}