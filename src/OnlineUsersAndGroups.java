import javax.websocket.Session;
import java.io.IOException;
import java.util.*;


public class OnlineUsersAndGroups {

    private static OnlineUsersAndGroups instance    = null;
    private Map<Session, User> onlineUsers    = Collections.synchronizedMap(new HashMap<>());

    private OnlineUsersAndGroups() { }

    public static OnlineUsersAndGroups getInstance() {
        if (instance == null) {
            instance = new OnlineUsersAndGroups();
        }
        return instance;
    }

    public void addSessionWithoutUser(Session session) {
        this.onlineUsers.put(session, null);
    }

    public void initUserBySession(Session session, String data) throws IOException {
        UserDataForInit userData = WebSocketHelper.gson.fromJson(data, UserDataForInit.class);
        User user = MySQLService.getUserInfoById(userData.id);
        this.onlineUsers.put(session, user);
        sendUpdateToAllUsers();
    }

    public void deleteUserFromOnline(Session session) throws IOException {
        this.onlineUsers.remove(session);
        sendUpdateToAllUsers();
    }

    public String getOnlineUsers() {
        ResponceWithAllUsers responce = new ResponceWithAllUsers();
        responce.type = "users";
        List<User> users = new LinkedList<>();
        for (User item : this.onlineUsers.values()) {
            users.add(item);
        }
        responce.users = users;
        return WebSocketHelper.gson.toJson(responce);
    }

    private void sendUpdateToAllUsers() throws IOException {
        for (Session item : onlineUsers.keySet()) {
            item.getBasicRemote().sendText(getOnlineUsers());
        }
    }

    private class UserDataForInit {

        public Integer id;
        public String username;
        public String token;


    }

    private class ResponceWithAllUsers {
        public String type;
        public List<User> users;
    }

}
