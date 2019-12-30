package bot;

import com.google.gson.*;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.callback.longpoll.responses.GetLongPollEventsResponse;
import com.vk.api.sdk.objects.groups.LongPollServer;
import com.vk.api.sdk.objects.messages.Message;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VKCore {

    private static VKCore instance;
    private VkApiClient vk;
    private static LongPollServer lps;
    private GroupActor actor;

    public static VKCore getInstance() {
        if (instance == null) {
            try {
                instance = new VKCore();
            } catch (ClientException  | ApiException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public VkApiClient getVk(){
        return vk;
    }

    public GroupActor getActor(){
        return actor;
    }

    public VKCore() throws ClientException, ApiException {
        FileInputStream file;
        Properties property = new Properties();
        Integer groupId = null;
        String token = null;
        try {
            file = new FileInputStream("src/main/resources/vkconfig.properties");
            property.load(file);
            groupId = Integer.parseInt(property.getProperty("group_id"));
            token = property.getProperty("access_token");
        } catch (IOException e) {
            System.err.println("Error! file not found");
        }
        TransportClient transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);
        actor = new GroupActor(groupId, token);
        lps = vk.groups().getLongPollServer(actor, groupId).execute();

    }

    public List<Message> getMessage() throws ClientException, ApiException {
        List<Message> messages = new ArrayList<Message>();
        Message message = new Message();
        JsonObject json;

        GetLongPollEventsResponse resp = vk.longPoll().getEvents(lps.getServer(), lps.getKey(), Integer.parseInt(lps.getTs())).waitTime(30).execute();

        lps.setTs(resp.getTs().toString());

        for (JsonObject obj : resp.getUpdates()) {
            //System.out.println(obj);
            json = (JsonObject) obj.get("object");

            message.setPeerId(json.get("user_id").getAsInt());
            message.setText((json.get("body").toString()));
            messages.add(message);
        }
        return messages;
    }

    public void sendMessage(String msg, int peerId){
        if (msg == null){
            System.out.println("No text");
            return;
        }
        try {
           vk.messages().send(actor).peerId(peerId).message(msg).randomId(0).execute();

        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }
}
