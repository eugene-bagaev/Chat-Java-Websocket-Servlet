import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import com.google.gson.*;

// todo delete this class
@Deprecated
@ServerEndpoint(value = "/registration")
public class RegistrationController {

    private static Gson gson = new Gson();

    @OnOpen
    public void onOpen(Session session) {
        WebSocketHelper.logNewConnection(session);
        System.out.println("Reg user conn");
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        WebSocketHelper.logNewMessageFromClient(message, session);

        RegistrationWrapper request     = gson.fromJson(message, RegistrationWrapper.class);

        System.out.println(request);

//        RegistrationResponse response   = MySQLService.verifyRegistrationAndRegister(request);

        return null;
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
        public String login;
        public String password;
        public String email;
        public String firstName;
        public String lastName;

        public String toString() {
            return "[login="+login+";pass="+password+";email="+email+";fName="+firstName+";lName="+lastName+"]";
        }
    }
}
