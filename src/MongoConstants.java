// todo delete class. Used in Mongo logic
@Deprecated
public class MongoConstants {

    public static final String MONGO_URI = "mongodb://admin:Gp13kost@cluster0-shard-00-00-p7xcy.mongodb.net:27017," +
            "cluster0-shard-00-01-p7xcy.mongodb.net:27017," +
            "cluster0-shard-00-02-p7xcy.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true";

    // Data Bases
    public static final String DB_CHATUSERS     = "chatUsers";
    public static final String DB_CHATMESSAGES  = "ChatMessages";

    // Collections
    public static final String COLLECTION_CHATUSERS     = "ChatUsers";
    public static final String COLLECTION_CHATMESSAGES  = "messages";

    // Common Fields
    public static final String ID = "_id";

    // DB Fields ChatUsers
    public static final String CHATUSERS_LOGIN      = "login";
    public static final String CHATUSERS_PASSWORD   = "password";
    public static final String CHATUSERS_EMAIL      = "e-mail";
    public static final String CHATUSERS_ROLE       = "role";

    // DB Fields ChatMessages
    public static final String CHATMSG_USER     = "User";
    public static final String CHATMSG_MESSAGE  = "message";
    public static final String CHATMSG_DATETIME = "datetime";
    public static final String CHATMSG_FILEPATH = "filepath";

    // DB ChatUsers Roles
    public static final String ROLE_USER    = "user";
    public static final String ROLE_ADMIN   = "admin";
}
