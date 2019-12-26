package bot;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainBot {

    public static void main(String[] args) throws NullPointerException, ApiException, InterruptedException {

        VKCore vkCore = VKCore.getInstance();

        final int RECONNECT_TIME = 10000;
        System.out.println("Start server");
        while (true) {
            Thread.sleep(300);
            try {
                Message message = vkCore.getMessage();
                if (message != null) {
                    ExecutorService exec = Executors.newCachedThreadPool();
                    exec.execute(new Messenger(message));
                }
            } catch (ClientException e) {
                System.out.println("Get a problems");
                System.out.println("Reconnect through " + RECONNECT_TIME / 1000 + " seconds");
                Thread.sleep(RECONNECT_TIME);
            }
        }
    }
}
