public class MySQLConstants {

    public static final String DB_MESSAGE       = "Message";

    public static final String JDBC_DRIVER      = "com.mysql.jdbc.Driver";
    public static final String CHAT_DB_URL      = "jdbc:mysql://104.248.243.134:3306/Chat?characterEncoding=cp1251";
    public static final String CHAT_DB_USER     = "java";
    public static final String CHAT_DB_PASS     = "45hPF=VpZ#smZV#knb$AQLP##V_qz-d8_Ek8";

    // User Table Fields
    public static final String USER_ID          = "Id";
    public static final String USER_NAME        = "Name";
    public static final String USER_LOGIN       = "Login";
    public static final String USER_EMAIL       = "Email";
    public static final String USER_PASSWORD    = "Password";
    public static final String USER_TOKEN       = "SecurityToken";
    public static final String USER_SETTING     = "UserSettingId";

    public static final String REL_USER_NAME    = "User.Name";
    public static final String REL_USER_LOGIN   = "User.Login";

    // Message Table Fields
    public static final String MES_MESSAGE      = "Message";
    public static final String MES_TYPE         = "Type";
    public static final String MES_DATE         = "Datetime";
    public static final String MES_FILEPATH     = "Filepath";
    public static final String MES_USER_ID      = "UserId";
}
