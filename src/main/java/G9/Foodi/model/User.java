package G9.Foodi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String photoURL;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    // Enum Role
    public enum Role {
        USER, ADMIN
    }

    // Constructor không tham số
    public User() {
    }

    // Constructor đầy đủ tham số
    public User(Long id, String name, String email, String photoURL, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.photoURL = photoURL;
        this.role = role;
    }

    // Getter và Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // toString()
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", photoURL='" + photoURL + '\'' +
                ", role=" + role +
                '}';
    }
}
