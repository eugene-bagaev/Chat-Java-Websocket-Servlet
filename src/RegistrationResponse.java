public class RegistrationResponse {

    private String  username;
    private String key;
    private Boolean hasAccess;
    private String field;
    private String value;
    private String  status = "SUCCESS";

    public RegistrationResponse(String username, String key, Boolean hasAccess) {
        this.username   = username;
        this.key        = key;
        this.hasAccess  = hasAccess;
    }
    public RegistrationResponse(String username, String key, String field, String value, Boolean hasAccess, String status) {
        this.username   = username;
        this.key        = key;
        this.hasAccess  = hasAccess;
        this.field      = field;
        this.value      = value;
        this.status     = status;
    }

    public String toString() {
        return "[username="+username+";key="+key+";hasAccess="+hasAccess+";status="+status+"]";
    }

}
