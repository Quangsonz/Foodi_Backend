package G9.Foodi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "carts")
public class Cart {
    @Id
    private String id; // Sử dụng String thay vì Long cho MongoDB

    private String menuItemId;
    private String name;
    private String recipe;
    private String image;
    private Double price;
    private Integer quantity;
    private String email;

    // Constructor không tham số
    public Cart() {
    }

    // Constructor đầy đủ tham số
    public Cart(String id, String menuItemId, String name, String recipe, String image, Double price, Integer quantity, String email) {
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
    public String getId() {
        return id;
    }

    public void setId(String id) {
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
                "id='" + id + '\'' +
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