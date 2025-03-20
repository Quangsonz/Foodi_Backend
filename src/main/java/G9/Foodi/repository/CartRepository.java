package G9.Foodi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import G9.Foodi.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByEmail(String email);
    Optional<Cart> findByEmailAndMenuItemId(String email, String menuItemId);
} 