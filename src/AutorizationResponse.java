public class AutorizationResponse {

    private String  username;
    private Integer key;
    private Boolean hasAccess;
    private String  status = "SUCCESS";

    public AutorizationResponse(String username, Integer key, Boolean hasAccess) {
        this.username   = username;
        this.key        = key;
        this.hasAccess  = hasAccess;
    }
    public AutorizationResponse(String username, Integer key, Boolean hasAccess, String status) {
        this.username   = username;
        this.key        = key;
        this.hasAccess  = hasAccess;
        this.status     = status;
    }

    public String toString() {
        return "[username="+username+";key="+key+";hasAccess="+hasAccess+";status="+status+"]";
    }
}
