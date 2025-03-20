package G9.Foodi.dto;

public class CartDto {
    private String menuItemId;
    private String name;
    private String recipe;
    private String image;
    private Double price;
    private Integer quantity;
    private String email;

    // Constructor không tham số
    public CartDto() {
    }

    // Constructor đầy đủ tham số
    public CartDto(String menuItemId, String name, String recipe, String image, Double price, Integer quantity, String email) {
        this.menuItemId = menuItemId;
        this.name = name;
        this.recipe = recipe;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.email = email;
    }

    // Getter và Setter
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

    // toString()
    @Override
    public String toString() {
        return "CartDto{" +
                "menuItemId='" + menuItemId + '\'' +
                ", name='" + name + '\'' +
                ", recipe='" + recipe + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", email='" + email + '\'' +
                '}';
    }
}
