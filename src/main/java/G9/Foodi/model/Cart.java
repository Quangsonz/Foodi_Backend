package G9.Foodi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_item_id")
    private String menuItemId;

    @Column(nullable = false)
    private String name;

    private String recipe;
    private String image;
    private Double price;
    private Integer quantity;

    @Column(nullable = false)
    private String email;

    // Constructor không tham số
    public Cart() {
    }

    // Constructor đầy đủ tham số
    public Cart(Long id, String menuItemId, String name, String recipe, String image, Double price, Integer quantity, String email) {
        this.id = id;
        this.menuItemId = menuItemId;
        this.name = name;
        this.recipe = recipe;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.email = email;
    }

    // Getter và Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // toString() để debug
    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", menuItemId='" + menuItemId + '\'' +
                ", name='" + name + '\'' +
                ", recipe='" + recipe + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", email='" + email + '\'' +
                '}';
    }
}
