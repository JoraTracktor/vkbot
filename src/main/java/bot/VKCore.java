package bot;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class VKCore {

    private static VKCore instance;

    private VkApiClient vk;
    private static int ts;
    private GroupActor actor;
    private static int maxMsgId = -1;

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
        ts = vk.messages().getLongPollServer(actor).execute().getTs();
    }

    public Message getMessage() throws ClientException, ApiException {
        MessagesGetLongPollHistoryQuery eventsQuery = vk.messages()
                .getLongPollHistory(actor)
                .ts(ts);
        if (maxMsgId > 0){
            eventsQuery.maxMsgId(maxMsgId);
        }
        List<Message> messages = eventsQuery
                .execute()
                .getMessages()
                .getItems();
        System.out.println(messages);
        if (!messages.isEmpty()){
            try {
                ts =  vk.messages()
                        .getLongPollServer(actor)
                        .execute()
                        .getTs();

            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
        if (!messages.isEmpty() && !messages.get(0).isOut()) {
            int messageId = messages.get(0).getId();
            if (messageId > maxMsgId){
                maxMsgId = messageId;
            }
            return messages.get(0);
        }
        return null;
    }

    public void sendMessage(String msg, int peerId){
        if (msg == null){
            System.out.println("null");
            return;
        }
        try {
           vk.messages().send(actor).peerId(peerId).message(msg).randomId(0).execute();

        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }
}
