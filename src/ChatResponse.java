import java.util.LinkedList;
import java.util.List;

public class ChatResponse {

    private String status           = "Success";
    private String type;
    private Message message;
    private List<Message> messages  = new LinkedList<>();

    public ChatResponse() { }

    public ChatResponse(List<Message> messages) {
        this.messages = messages;
    }

    public ChatResponse(List<Message> messages, String status) {
        this.messages   = messages;
        this.status     = status;
    }

    public ChatResponse(Message latestMessage, String type) {
        this.message    = latestMessage;
        this.type       = type;
    }

    public ChatResponse(String status) {
        this.status = status;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return this.message;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return "Status="+status+";type="+type+";mess="+messages+";msg="+message+";";
    }

}
