public class User {

    private Integer id;
    private String  username;
    private String  email;
    private String  name;

    public User(String username, String email, String name, Integer id) {
        this.id         = id;
        this.username   = username;
        this.email      = email;
        this.name       = name;
    }

    public User() { }

    public String toString() {
        return "[id="+id+";un="+username+";email="+email+";name="+name+";]";
    }

}
