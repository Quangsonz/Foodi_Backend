package G9.Foodi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import G9.Foodi.model.Cart;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    List<Cart> findByEmail(String email);
    Optional<Cart> findByEmailAndMenuItemId(String email, String menuItemId);
}