import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import com.google.gson.*;

@ServerEndpoint(value = "/registration")
public class RegistrationController {

    private static Gson gson = new Gson();

    @OnOpen
    public void onOpen(Session session) {
        WebSocketHelper.logNewConnection(session);
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        WebSocketHelper.logNewMessageFromClient(message, session);

        RegistrationWrapper request     = gson.fromJson(message, RegistrationWrapper.class);
        AutorizationResponse response   = null;

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

    public class RegistrationWrapper {
        public String username;
        public String password;
        public String email;
    }
}
