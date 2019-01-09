import com.google.gson.Gson;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@ServerEndpoint(value = "/chat_entry_point")
public class ChatEntryPoint {

    private static Gson gson                        = new Gson();
    public static Set<Session> allSessions          = Collections.synchronizedSet(new HashSet<Session>());

    private static OnlineUsersAndGroups onlineUsersAndGroups = OnlineUsersAndGroups.getInstance();


    @OnOpen
    public void onOpen(Session session) {
        allSessions.add(session);

        onlineUsersAndGroups.addSessionWithoutUser(session);

        WebSocketHelper.logNewConnection(session);
     }

    @OnMessage
    public String onMessage(String message, Session session) throws IOException {

        EntryPointWrapper wrapper = gson.fromJson(message, EntryPointWrapper.class);

        if (wrapper.type.equals(WebSocketHelper.TYPE_LOGIN)) {

            Login login = new Login(wrapper.data);
            return login.response();

        } else if (wrapper.type.equals(WebSocketHelper.TYPE_REGISTRATION)) {

            Registration reg = new Registration(wrapper.data);
            return reg.response();

        } else if (wrapper.type.equals(WebSocketHelper.TYPE_CHAT)) {

            Chat chat = new Chat(wrapper.data, session);
            return gson.toJson(chat.getResponse());

        } else if (wrapper.type.equals(WebSocketHelper.TYEP_INIT_USER)) {

            onlineUsersAndGroups.initUserBySession(session, wrapper.data);
            return onlineUsersAndGroups.getOnlineUsers();

        } else {
            return null;
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        allSessions.remove(session);
        onlineUsersAndGroups.deleteUserFromOnline(session);
        WebSocketHelper.logCloseConnection(session);
    }

    @OnError
    public void onError(Throwable exeption, Session session) throws IOException {
        allSessions.remove(session);
        onlineUsersAndGroups.deleteUserFromOnline(session);
        WebSocketHelper.logError(exeption, session);
    }

    public class EntryPointWrapper {
        public String type;
        public String data;
    }

}
