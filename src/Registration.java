import com.google.gson.Gson;

public class Registration {

    private RegistrationResponse response;
    private static Gson gson = new Gson();

    public Registration(String regData) {
        RegistrationDataWrapper data = gson.fromJson(regData, RegistrationDataWrapper.class);
        validateRegistrationData(data);
    }

    public String response() {
        return gson.toJson(response);
    }

    private void validateRegistrationData(RegistrationDataWrapper data) {
        response = MySQLService.verifyRegistrationAndRegister(
                data.login,
                data.firstName,
                data.lastName,
                data.email,
                data.password
        );
    }

    private class RegistrationDataWrapper {
        public String login;
        public String firstName;
        public String lastName;
        public String email;
        public String password;

    }

}
