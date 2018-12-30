import java.sql.ResultSet;
import java.sql.SQLException;

public class Message {

    private String  message;
    private String  type;
    private String  date;
    private String  username;
    private String  userlogin;

    public Message(ResultSet sqlRow) throws SQLException {
        this.message    = sqlRow.getString(MySQLConstants.MES_MESSAGE);
        this.type       = sqlRow.getString(MySQLConstants.MES_TYPE);
        this.date       = sqlRow.getString(MySQLConstants.MES_DATE);
        this.username   = sqlRow.getString(MySQLConstants.REL_USER_NAME);
        this.userlogin  = sqlRow.getString(MySQLConstants.REL_USER_LOGIN);
    }

    public Message(
            String message,
            String type,
            String date,
            String username,
            String userlogin
    ) {
        this.userlogin  = userlogin;
        this.username   = username;
        this.date       = date;
        this.type       = type;
        this.message    = message;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getUserlogin() {
        return userlogin;
    }

    public String getUsername() {
        return username;
    }
}
