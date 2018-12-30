import org.bson.Document;

import java.util.Date;

// todo delete class. Used in Mongo logic
@Deprecated
public class MessageWrapper {
    public Integer  id;
    public String   name;
    public String   msg;
    public Date     date;
    public String   filepath;
    public String   state;

    public MessageWrapper(Document document) {
        this.id         = Integer.valueOf(String.valueOf(document.get(MongoConstants.ID)));
        this.name       = document.getString(MongoConstants.CHATMSG_USER);
        this.msg        = document.getString(MongoConstants.CHATMSG_MESSAGE);
        this.date       = document.getDate(MongoConstants.CHATMSG_DATETIME);
        this.filepath   = document.getString(MongoConstants.CHATMSG_FILEPATH);
    }

    public String toString() {
        return "[username="+name+";message="+msg+";date="+date+";filePath="+filepath+";state="+state+"]";
    }

}