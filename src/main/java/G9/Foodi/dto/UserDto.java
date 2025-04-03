package G9.Foodi.dto;

public class UserDto {
    private String name;
    private String email;
    private String password;
    private String photoURL;

    // Constructor không tham số
    public UserDto() {
    }

    // Constructor đầy đủ tham số
    public UserDto(String name, String email, String password, String photoURL) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.photoURL = photoURL;
    }

    // Getter và Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    // toString()
    @Override
    public String toString() {
        return "UserDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", photoURL='" + photoURL + '\'' +
                '}';
    }
}
