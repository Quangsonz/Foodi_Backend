package G9.Foodi.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "menu")
public class Menu {
    @Id
    private String id; // Sử dụng String thay vì Long cho MongoDB

    private String name;
    private String recipe;
    private String image;
    private String category;
    private Double price;
    private Date createdAt;

    // Constructor không tham số
    public Menu() {
    }

    // Constructor đầy đủ tham số
    public Menu(String id, String name, String recipe, String image, String category, Double price, Date createdAt) {
        this.id = id;
        this.name = name;
        this.recipe = recipe;
        this.image = image;
        this.category = category;
        this.price = price;
        this.createdAt = createdAt;
    }

    // Getter và Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // toString()
    @Override
    public String toString() {
        return "Menu{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", recipe='" + recipe + '\'' +
                ", image='" + image + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", createdAt=" + createdAt +
                '}';
    }
}