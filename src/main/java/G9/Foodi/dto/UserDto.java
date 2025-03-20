package G9.Foodi.dto;

public class UserDto {
    private String name;
    private String email;
    private String photoURL;

    // Constructor không tham số
    public UserDto() {
    }

    // Constructor đầy đủ tham số
    public UserDto(String name, String email, String photoURL) {
        this.name = name;
        this.email = email;
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
