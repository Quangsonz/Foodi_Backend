package G9.Foodi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import G9.Foodi.models.Cart;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByEmail(String email);
    Optional<Cart> findByEmailAndMenuItemId(String email, String menuItemId);
}
