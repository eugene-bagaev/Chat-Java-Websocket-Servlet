import com.google.gson.Gson;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.*;

@ServerEndpoint(value = "/chat_entry_point")
public class ChatEntryPoint {

    private static Gson gson                        = new Gson();
    public static Set<Session> allSessions          = Collections.synchronizedSet(new HashSet<Session>());
    private static Map<Session, User> onlineUsers   = Collections.synchronizedMap(new HashMap<>());

    @OnOpen
    public void onOpen(Session session) {
        allSessions.add(session);
        WebSocketHelper.logNewConnection(session);
     }

    @OnMessage
    public String onMessage(String message, Session session) {

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

        } else {
            return null;
        }
    }

    @OnClose
    public void onClose(Session session) {
        allSessions.remove(session);
        WebSocketHelper.logCloseConnection(session);
    }

    @OnError
    public void onError(Throwable exeption, Session session) {
        allSessions.remove(session);
        WebSocketHelper.logError(exeption, session);
    }

    public class EntryPointWrapper {
        public String type;
        public String data;
    }

}
