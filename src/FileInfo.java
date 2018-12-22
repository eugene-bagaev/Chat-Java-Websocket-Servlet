public class FileInfo {
    public String name;
    public String modifiedDate;
    public String size;

    public FileInfo(String name, String modifiedDate, String size) {
        this.name           = name;
        this.size           = size;
        this.modifiedDate   = modifiedDate;
    }

    public String toString() {
        return "[name="+name+";modDate="+modifiedDate+";size="+size+"]";
    }
}
