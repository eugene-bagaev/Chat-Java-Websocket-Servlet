import java.sql.*;

public class MySQLService {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String CHAT_DB_URL = "jdbc:mysql://104.248.243.134:3306/Chat";
    private static final String USER        = "java";
    private static final String PASS        = "45hPF=VpZ#smZV#knb$AQLP##V_qz-d8_Ek8";

    public static void testConnToMySQL() throws ClassNotFoundException {
        Statement stmt          = null;

        try {
            stmt = getDBStatement();

            String query = "SELECT m.Id, m.Message, u.Name FROM Message m, User u";

            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                System.out.println("================== Message Start ===================");
                System.out.println("Msg Id: " + resultSet.getInt("m.Id"));
                System.out.println("Msg:" + resultSet.getString("m.Message"));
                System.out.println("User Name: " + resultSet.getString("u.Name"));
                System.out.println("================== Message End ===================");
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public static void verifyAndRegisterUser() {

    }

    private static Statement getDBStatement() {
        Connection connection   = null;
        Statement stmt          = null;

        try {
            Class.forName(JDBC_DRIVER);

            System.out.println("Driver loaded");
            System.out.println("Connecting database...");

            connection = DriverManager.getConnection(CHAT_DB_URL, USER, PASS);

            System.out.println("Database connected!");
            System.out.println("Creating Statement");

            stmt = connection.createStatement();

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        } catch (ClassNotFoundException clEx) {
            System.out.println("JDBC Driver Not Found!");
        }

        return stmt;
    }

}
