import com.google.gson.Gson;
import javax.websocket.Session;
import java.io.IOException;

public class Chat {

    private static final String TYPE_REQUEST_MESSAGES = "MesReq";
    public static final String TYPE_SAVE_MSG_SEND_TO_USERS = "saveMsgSendToUsers";

    private String type;
    private ChatResponse response;
    private ChatDataWrapper data;
    private Session session;

    private static Gson gson = new Gson();

    public Chat(String chatData, Session session) {
        data = gson.fromJson(chatData, ChatDataWrapper.class);

        initData(data, session);

        try {
            runLogic();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public ChatResponse getResponse() {
        return response;
    }

    private void initData(ChatDataWrapper data, Session session) {
        this.type       = data.dataType;
        this.session    = session;
    }

    private void runLogic() throws IOException {
        if (type.equals(TYPE_REQUEST_MESSAGES)) {

            response = MySQLService.getLatestMessages();

        } else if (type.equals(TYPE_SAVE_MSG_SEND_TO_USERS)) {

            ChatMessageWrapper message = gson.fromJson(
                    data.dataValues,
                    ChatMessageWrapper.class
            );

            response = MySQLService.saveMsg(message.userId, message.msg);

            sendMsgToAllOnlineUsers(response);

            response.setType("info");
        }
    }

    private void sendMsgToAllOnlineUsers(ChatResponse message) {
        synchronized (ChatEntryPoint.allSessions) {
            try {

                for (Session item : ChatEntryPoint.allSessions) {
                    item.getBasicRemote().sendText(gson.toJson(message));
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class ChatDataWrapper {
        public String dataType;
        public String dataValues;

        public String toString() {
            return "[dataType="+dataType+";dataValues="+dataValues+"]";
        }
    }

    private class ChatMessageWrapper {
        public Integer  userId;
        public String   name;
        public String   msg;
    }

}
