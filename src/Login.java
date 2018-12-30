import com.google.gson.Gson;

public class Login {

    private AutorizationResponse response;

    private static Gson gson = new Gson();

    public Login(String loginData) {
        LoginDataWrapper data = gson.fromJson(loginData, LoginDataWrapper.class);
        validateLoginData(data);
    }

    public String response() {
        return gson.toJson(response);
    }

    private void validateLoginData(LoginDataWrapper data) {
        response = MySQLService.verifyAuth(data.login, data.password);
    }

    private class LoginDataWrapper {
        public String login;
        public String password;
    }

}
