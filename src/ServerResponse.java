public class ServerResponse {

    private String          username;
    private String          fileName;
    private Boolean         hasAccess;
    private String          status = "SUCCESS";
    private String          message;
    private FileInfo        fileInfo;

    public ServerResponse(String username, String fileName, Boolean hasAccess, String message, FileInfo fileInfo) {
        this.username   = username;
        this.fileName   = fileName;
        this.hasAccess  = hasAccess;
        this.message    = message;
        this.fileInfo   = fileInfo;
    }

    public ServerResponse(String username, String fileName, Boolean hasAccess, String message, String status) {
        this.username   = username;
        this.hasAccess  = hasAccess;
        this.fileName   = fileName;
        this.message    = message;
        this.status     = status;
    }

    public String toString() {
        return "[username="+username+";fileName="+fileName+";message="+message+";hasAccess="+hasAccess+";status="+status+";filename=" +fileInfo+"]";
    }
}
