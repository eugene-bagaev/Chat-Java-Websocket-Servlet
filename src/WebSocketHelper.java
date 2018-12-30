import javax.websocket.Session;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebSocketHelper {

    public static final String TYPE_REGISTRATION    = "reg";
    public static final String TYPE_LOGIN           = "login";
    public static final String TYPE_CHAT            = "chat";

    private static final Logger LOGGER = Logger.getLogger(WebSocketHelper.class.getName());

    public static void logNewConnection(Session session) {
        LOGGER.log(
                Level.INFO,
                "New connection with client: {0}",
                session.getId()
        );
    }

    public static void logNewMessageFromClient(String message, Session session) {
        LOGGER.log(
                Level.INFO,
                "New message from client [{0}]: {1}",
                new Object[] {session.getId(), message}
        );
    }

    public static void logCloseConnection(Session session) {
        LOGGER.log(
                Level.INFO,
                "Close connection for client: {0}",
                session.getId()
        );
    }

    public static void logError(Throwable exception, Session session) {
        LOGGER.log(
                Level.INFO,
                "Error for the client: {0}. With msg: {1}",
                new Object[] { session.getId(), exception.getMessage() }
        );
    }

}
