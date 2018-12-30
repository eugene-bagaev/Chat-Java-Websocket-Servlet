import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import com.google.gson.*;

// todo delete this unused class
@Deprecated
@ServerEndpoint(value = "/login")
public class LoginController {

    private static Gson gson = new Gson();

    @OnOpen
    public void onOpen(Session session) throws ClassNotFoundException {
        WebSocketHelper.logNewConnection(session);
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        WebSocketHelper.logNewMessageFromClient(message, session);

        AutorizationResponse response = null;

        LoginFormWrapper loginData = gson.fromJson(message, LoginFormWrapper.class);

        return gson.toJson(response);
    }

    @OnClose
    public void onClose(Session session) {
        WebSocketHelper.logCloseConnection(session);
    }

    @OnError
    public void onError(Throwable exeption, Session session) {
        WebSocketHelper.logError(exeption, session);
    }

    public class LoginFormWrapper {
        public String   login;
        public Boolean  remember;

        // todo delete this field after deleting mongo service
        public String   username;

        public String   password;

        // todo delete this field after deleting mongo service
        public String   id;

        // todo delete this field after deleting mongo service
        public Boolean  isLogin;
    }
}
