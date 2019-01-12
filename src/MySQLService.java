import java.sql.*;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

public class MySQLService {

    private static Connection connection;
    public static Statement stmt = getDBStatement();

    public static AutorizationResponse verifyAuth(String login, String password) {
        AutorizationResponse response;
        String query = "SELECT * FROM User WHERE Login = \'" + login + "\'";
        try {
            ResultSet result = stmt.executeQuery(query);
            if (result.next()) {

                if (result.getString("Password").equals(ChatUtils.cryptPassword(password))) {
                    response = new AutorizationResponse(
                            login,
                            result.getString("SecurityToken"),
                            result.getInt(MySQLConstants.USER_ID),
                            true
                    );
                } else {
                    response = new AutorizationResponse(
                            login,
                            null,
                            false,
                            "Invalid password"
                    );
                }
            } else {
                response = new AutorizationResponse(
                        login,
                        null,
                        false,
                        "User does not exist!"
                );
            }
        } catch (SQLException e) {
            response = new AutorizationResponse(
                    login,
                    null,
                    false,
                    "Internal Server Error Occurred"
            );
        }
        System.out.println("Server response: " + response);
        return response;
    }

    public static RegistrationResponse verifyRegistrationAndRegister(
            String login,
            String firstName,
            String lastName,
            String email,
            String password) {

        RegistrationResponse response;

        if (validateData("Login", login)) {
            response = new RegistrationResponse(
                    login,
                    null,
                    "Login",
                    login,
                    false,
                    "Login is already taken"
            );
        } else if (validateData("Email", email)) {
            response = new RegistrationResponse(
                    login,
                    null,
                    "Email",
                    email,
                    false,
                    "Email is already taken"
            );
        } else {
            String token = registerUser(login, firstName, lastName, email, password);
            response = new RegistrationResponse(
                    login,
                    token,
                    true
            );
        }

        return response;
    }

    public static ChatResponse getLatestMessages() {

        ChatResponse response;
        List<Message> messages  = new LinkedList<>();
        String query            = "SELECT * FROM Message JOIN User WHERE Message.UserId = User.Id ORDER BY Message.Id";

        try {

            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                messages.add(new Message(result));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        response = new ChatResponse(messages);
        return response;
    }

    public static ChatResponse saveMsg(Integer userId, String message) {
        ChatResponse response;

        Object[] args = {
                MySQLConstants.MES_MESSAGE,
                MySQLConstants.MES_TYPE,
                MySQLConstants.MES_DATE,
                MySQLConstants.MES_FILEPATH,
                MySQLConstants.MES_USER_ID
        };

        Integer insertedRecordId    = null;
        String currentDateTime      = ChatUtils.getCurrentDatetime();
        MessageFormat formatMessage = new MessageFormat("{0}, {1}, {2}, {3}, {4}");
        String fields               = formatMessage.format(args);
        String query                = "INSERT Into Message (" + fields + ") VALUES (?, ?, ?, ?, ?)";

        try {

            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, message);
            ps.setString(2, "text");
            ps.setString(3, currentDateTime);
            ps.setString(4, null);
            ps.setInt(5, userId);
            ps.executeUpdate();

            ResultSet resultSet = ps.getGeneratedKeys();

            if (resultSet.next()) {
                insertedRecordId = resultSet.getInt(1);
            } else {
                insertedRecordId = null;
            }

            if (insertedRecordId != null && insertedRecordId > 0) {

                ResultSet resultSetLastMsg = stmt.executeQuery(
                        getMessageById(insertedRecordId)
                );

                if (resultSetLastMsg.next()) {
                    Message latestMsg = new Message(resultSetLastMsg);
                    response = new ChatResponse(latestMsg, "newMsg");
                } else {
                    System.out.println("No latest Id and Message");
                    response = new ChatResponse("ERROR");
                }
            } else {
                System.out.println("No inserted record");
                response = new ChatResponse("ERROR");
            }

        } catch (SQLException e) {
            response = new ChatResponse("ERROR IN DB");
        }

        return response;
    }

    public static User getUserInfoById(Integer id) {
        User user;
        String query = "SELECT * FROM User WHERE Id = \'" + id + "\'";

        try {
            ResultSet result = stmt.executeQuery(query);
            if (result.next()) {

                user = new User(
                        result.getString(MySQLConstants.USER_LOGIN),
                        result.getString(MySQLConstants.USER_EMAIL),
                        result.getString(MySQLConstants.USER_NAME),
                        id
                );

            } else {
                user = null;
            }
        } catch (SQLException e) {
            user = null;
            System.out.println(e.getMessage());
        }

        return user;
    }

    private static String getMessageById(Integer recordId) {
        String query = "SELECT * FROM Message JOIN User " +
                "WHERE Message.UserId = User.Id " +
                "AND Message.Id = " + recordId + " " +
                "ORDER BY Message.Id";
        return query;
    }

    private static String getRecordByIdFromTable(String table, Integer recordId) {
        Object[] args = {
                table,
                recordId
        };
        String query = "SELECT * FROM {0} WHERE Id = {1}";
        MessageFormat messageFormat = new MessageFormat(query);

        return messageFormat.format(args);
    }

    private static Boolean validateData(String field, String value) {
    Boolean isTaken  = true;
    String query     = "SELECT Id FROM User WHERE " + field + " = \'" + value + "\'";

        try {
            ResultSet result = stmt.executeQuery(query);

            if (result.next()) {
                isTaken = true;
            } else {
                isTaken = false;
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return isTaken;
    }

    private static String registerUser(
            String login,
            String firstName,
            String lastName,
            String email,
            String password
    ) {

        String token = ChatUtils.getRandomStringToken();

        Object[] args = {
                MySQLConstants.USER_NAME,
                MySQLConstants.USER_LOGIN,
                MySQLConstants.USER_EMAIL,
                MySQLConstants.USER_PASSWORD,
                MySQLConstants.USER_TOKEN,
                MySQLConstants.USER_SETTING
        };

        MessageFormat fm    = new MessageFormat("{0}, {1}, {2}, {3}, {4}, {5}");
        String fields       = fm.format(args);
        String query        = "INSERT INTO User (" + fields + ") VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString (1, firstName + lastName);
            preparedStatement.setString (2, login);
            preparedStatement.setString (3, email);
            preparedStatement.setString (4, ChatUtils.cryptPassword(password));
            preparedStatement.setString (5, token);
            preparedStatement.setInt    (6, 1);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return token;
    }

    private static Statement getDBStatement() {
        Statement stmt          = null;

        try {
            Class.forName(MySQLConstants.JDBC_DRIVER);

            System.out.println("Driver loaded");
            System.out.println("Connecting database...");

            connection = DriverManager.getConnection(
                    MySQLConstants.CHAT_DB_URL,
                    MySQLConstants.CHAT_DB_USER,
                    MySQLConstants.CHAT_DB_PASS
            );

            System.out.println("Database connected!");
            System.out.println("Creating Statement");

            stmt = connection.createStatement();

            System.out.println("Statement created. Ready to execute Query");

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        } catch (ClassNotFoundException clEx) {
            System.out.println("JDBC Driver Not Found!");
        }

        return stmt;
    }
}
