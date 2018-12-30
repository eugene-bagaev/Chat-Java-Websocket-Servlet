//import com.mongodb.*;
//import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoCursor;
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.model.BulkWriteOptions;
//import com.mongodb.client.model.DeleteOneModel;
//import com.mongodb.client.model.InsertOneModel;
//import com.mongodb.client.model.ReplaceOneModel;
//import com.mongodb.client.model.UpdateOneModel;
//import com.mongodb.client.model.WriteModel;
//import com.mongodb.client.result.DeleteResult;
//import com.mongodb.client.result.UpdateResult;
//import org.bson.BSONObject;
//import org.bson.BsonDocument;
//import org.bson.Document;
//import org.bson.types.ObjectId;
//
//import java.io.File;
//import java.math.BigInteger;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.attribute.BasicFileAttributes;
//import java.security.MessageDigest;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import static com.mongodb.client.model.Filters.eq;
//import static com.mongodb.client.model.Filters.in;
//import static com.mongodb.client.model.Filters.ne;
//
//public class MongoService {
//
//    private static MongoClientURI uri           = new MongoClientURI(MongoConstants.MONGO_URI);
//    private static MongoClient mongoClient      = new MongoClient(uri);
//
//    private static MongoDatabase chatUsersDB    = mongoClient.getDatabase(MongoConstants.DB_CHATUSERS);
//    private static MongoDatabase chatMsgDB      = mongoClient.getDatabase(MongoConstants.DB_CHATMESSAGES);
//
//    private static MongoCollection chatUsersCollection  = chatUsersDB.getCollection(MongoConstants.COLLECTION_CHATUSERS);
//    private static MongoCollection chatMsgCollection    = chatMsgDB.getCollection(MongoConstants.COLLECTION_CHATMESSAGES);
//
////    private static MongoDatabase chatUsersDB    = new MongoClient("localhost", 27017).getDatabase("chatUsers");
////    private static MongoDatabase chatMsgDB      = new MongoClient("localhost");
//
//    public static AutorizationResponse verifyAuth(LoginController.LoginFormWrapper input) {
//        Document user = (Document)chatUsersCollection.find(
//                eq(MongoConstants.CHATUSERS_LOGIN, input.username)
//        ).first();
//
//        if (user == null) {
//            return new AutorizationResponse(
//                    input.username,
//                    null,
//                    false,
//                    "User Not Found"
//            );
//        } else if (ChatUtils.cryptPassword(input.password).equals(user.getString(MongoConstants.CHATUSERS_PASSWORD))) {
//            return new AutorizationResponse(
//                    input.username,
//                    user.getInteger(MongoConstants.ID),
//                    true
//            );
//        } else {
//            return new AutorizationResponse(
//                    input.username,
//                    null,
//                    false,
//                    "Invalid Password"
//            );
//        }
//    }
//
//    public static AutorizationResponse registerUser(RegistrationController.RegistrationWrapper input) {
//        Integer nextUserId          = getMaxChatUserId() + 1;
//
//        Document user = (Document)chatUsersCollection.find(
//                eq(MongoConstants.CHATUSERS_LOGIN, input.username)
//        ).first();
//
//        if (user != null) {
//            return new AutorizationResponse(
//                    input.username, nextUserId,
//                    false,
//                    "User already existed!");
//        }
//
//        Document document = new Document(MongoConstants.CHATUSERS_LOGIN, input.username)
//                .append(MongoConstants.CHATUSERS_PASSWORD, ChatUtils.cryptPassword(input.password))
//                .append(MongoConstants.CHATUSERS_EMAIL, input.email)
//                .append(MongoConstants.CHATUSERS_ROLE, MongoConstants.ROLE_USER)
//                .append(MongoConstants.ID, nextUserId);
//
//        try {
//            chatUsersCollection.insertOne(document);
//        } catch (Exception ex) {
//            return new AutorizationResponse(
//                    input.username,
//                    nextUserId,
//                    false,
//                    "Error in:" + ex.getMessage());
//        }
//        return new AutorizationResponse(input.username, nextUserId, true);
//    }
//
//    public static AutorizationResponse verifyLoginAndId(LoginController.LoginFormWrapper input) {
//        Document user = (Document)chatUsersCollection.find(
//                eq(MongoConstants.CHATUSERS_LOGIN, input.username)
//        ).first();
//
//        if (user.getInteger(MongoConstants.ID).equals(Integer.valueOf(input.id))) {
//            return new AutorizationResponse(
//                    input.username,
//                    user.getInteger(MongoConstants.ID),
//                    true
//            );
//        } else {
//            return new AutorizationResponse(
//                    input.username,
//                    null,
//                    false
//            );
//        }
//    }
//
//    public static AutorizationResponse saveMsgInDatabase(ChatServer.ChatWrapper input) {
//        Integer nextMgsId = getMaxMsgId() + 1;
//
//        Document document = new Document(MongoConstants.ID, nextMgsId)
//                .append(MongoConstants.CHATMSG_USER, input.name)
//                .append(MongoConstants.CHATMSG_MESSAGE, input.msg)
//                .append(MongoConstants.CHATMSG_FILEPATH, input.filePath)
//                .append(MongoConstants.CHATMSG_DATETIME, LocalDateTime.now());
//
//        try {
//            chatMsgCollection.insertOne(document);
//        } catch (Exception ex) {
//            return new AutorizationResponse(
//                    input.name,
//                    null,
//                    false,
//                    "Error in:" + ex.getMessage()
//            );
//        }
//        return new AutorizationResponse(input.name, null, true);
//    }
//
//    public static ArrayList<MessageWrapper> getLatestMessages() {
//        ArrayList<MessageWrapper> messages = new ArrayList<>();
//
//        MongoCursor<Document> msgs = chatMsgCollection.find().sort(eq(MongoConstants.ID, -1)).iterator();
//
//        try {
//            while (msgs.hasNext()) {
//                messages.add(new MessageWrapper(msgs.next()));
//            }
//        } finally {
//            msgs.close();
//        }
//
//        for (MessageWrapper item : messages) {
//            if (item.filepath != null) {
//                item.state = "File";
//            }
//        }
//
//        return messages;
//    }
//
//    public static ServerResponse getFileInfo(ChatServer.ChatWrapper input) {
//        Document fileInDB = (Document)chatMsgCollection.find(
//                eq(MongoConstants.ID, Integer.valueOf(input.id))
//        ).first();
//
//        if (fileInDB != null) {
//
//            File file               = new File(fileInDB.getString(MongoConstants.CHATMSG_FILEPATH));
//            SimpleDateFormat sdf    = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//            FileInfo fileInfo       = new FileInfo(
//                    file.getName(),
//                    sdf.format(file.lastModified()),
//                    readableFileSize(file.length())
//            );
//            System.out.println(input.name);
//            System.out.println(input.fileName);
//            if (fileInfo.size.equals("0")) {
//                return new ServerResponse(input.name, input.fileName, false, "File doesn't exist", "ERROR");
//            } else {
//                return new ServerResponse(input.name, input.fileName, true, "File exist", fileInfo);
//            }
//        } else {
//            return new ServerResponse(input.name, input.fileName, false, "File not found", "ERROR");
//        }
//    }
//
//    public static User initUser(ChatServer.ChatWrapper input) {
//        Document user = (Document)chatUsersCollection.find(
//                eq(MongoConstants.CHATUSERS_LOGIN, input.name)
//        ).first();
//
//        return new User(user.getString(MongoConstants.CHATUSERS_LOGIN), user.getString(MongoConstants.CHATUSERS_LOGIN));
//
//    }
//
//    private static Integer getMaxChatUserId() {
//        Document max = (Document)chatUsersCollection
//                .find()
//                .sort(eq(MongoConstants.ID, -1))
//                .first();
//        return max.getInteger(MongoConstants.ID);
//    }
//    private static Integer getMaxMsgId() {
//        Document max = (Document)chatMsgCollection
//                .find()
//                .sort(eq(MongoConstants.ID, -1))
//                .first();
//        return  max.getInteger(MongoConstants.ID);
//    }
//
//    public static String readableFileSize(long size) {
//        if(size <= 0) return "0";
//        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
//        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
//        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
//    }
//
//}
