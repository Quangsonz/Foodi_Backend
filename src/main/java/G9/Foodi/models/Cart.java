package G9.Foodi.models;

import jakarta.persistence.*;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String menuItemId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String recipe;

    @Column(length = 500)
    private String image;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String email;

    // Constructor không tham số
    public Cart() {}

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

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMenuItemId() { return menuItemId; }
    public void setMenuItemId(String menuItemId) { this.menuItemId = menuItemId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRecipe() { return recipe; }
    public void setRecipe(String recipe) { this.recipe = recipe; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
