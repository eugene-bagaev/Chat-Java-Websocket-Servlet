import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.*;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import com.google.gson.*;
import java.io.File;
import java.io.*;
import java.nio.ByteBuffer;

@ServerEndpoint(value = "/chat")
public class ChatServer {

    private static File uploadedFile                = null;
    private static String fileName                  = null;
    private static FileOutputStream outputStream    = null;
    private final static String PATH_TO_DOWNLOADS   = "E:/Downloads/";

    private static Set<Session> allSessions         = Collections.synchronizedSet(new HashSet<Session>());
    private static Map<Session, User> onlineUsers   = Collections.synchronizedMap(new HashMap<>());
    private static Gson gson                        = new Gson();

    @OnOpen
    public void onOpen(Session session) throws IOException, ClassNotFoundException {
        // todo init mysql connection here
        allSessions.add(session);
        MySQLService.testConnToMySQL();
//        session.setMaxBinaryMessageBufferSize(1000000);
        WebSocketHelper.logNewConnection(session);
//        sentLatestMessagesToCurrentUser(session);
    }

    @OnMessage
    public void processUpload(Session session, ByteBuffer fileBuffer, boolean last) {
        System.out.println("Process Upload");

        if (last) {
            System.out.println("Is last: " + last);
        }

        while(fileBuffer.hasRemaining()) {
            try {
                outputStream.write(fileBuffer.get());
            } catch (IOException error) {
                error.printStackTrace();
            }
        }
    }

    @OnMessage
    public void message(Session session, String message) throws IOException {
        WebSocketHelper.logNewMessageFromClient(message, session);

        ChatWrapper chat = gson.fromJson(message, ChatWrapper.class);
        System.out.println(chat);
        if (chat.state.equals("Start File")) {

            fileName = chat.msg;
            uploadedFile = new File(ServletConstants.FILE_DISK_PATH + fileName);

            try {
                outputStream = new FileOutputStream(uploadedFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (chat.state.equals("Init User")) {

            onlineUsers.put(session, MongoService.initUser(chat));

        } else if (chat.state.equals("End File")) {

            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (chat.state.equals("File")) {

            chat.filePath                   = uploadedFile.getPath();
            AutorizationResponse response   = MongoService.saveMsgInDatabase(chat);

            sendMessageToAllClients(session, chat);
            System.out.println(onlineUsers);
        } else if (chat.state.equals("File Request")) {
            session.getBasicRemote().sendText(gson.toJson(MongoService.getFileInfo(chat)));
        } else {
            AutorizationResponse response = MongoService.saveMsgInDatabase(chat);
            sendMessageToAllClients(session, chat);
        }
    }

    @OnClose
    public void onClose(Session session) {
        allSessions.remove(session);
        onlineUsers.remove(session);

        WebSocketHelper.logCloseConnection(session);
    }

    @OnError
    public void onError(Throwable exeption, Session session) {
        allSessions.remove(session);
        onlineUsers.remove(session);

//        exeption.printStackTrace();
        WebSocketHelper.logError(exeption, session);
    }

    private void sendMessageToAllClients(Session session, ChatWrapper message) throws IOException {
        synchronized (allSessions) {
            for (Session item : allSessions) {
                if (!item.equals(session)) {
                    item.getBasicRemote().sendText(gson.toJson(message));
                }
            }
        }
    }

    private void sentLatestMessagesToCurrentUser(Session session) throws IOException {
        session.getBasicRemote().sendText(gson.toJson(MongoService.getLatestMessages()));
    }

    public class ChatWrapper {
        public String   id;
        public String   name;
        public String   fileName;
        public String   msg;
        public String   state;
        public String   filePath;

        public String toString() {
            return "[id="+id+";name="+name+";fileName="+fileName+";msg="+msg+";state="+state+";filePath="+filePath+"]";
        }
    }

}
