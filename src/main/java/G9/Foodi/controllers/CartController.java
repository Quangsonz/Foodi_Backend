package G9.Foodi.controllers;

import G9.Foodi.models.Cart;
import G9.Foodi.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    // Lấy danh sách giỏ hàng theo email
    @GetMapping
    public ResponseEntity<?> getCartByEmail(@RequestParam String email) {
        List<Cart> carts = cartService.getCartByEmail(email);
        if (carts.isEmpty()) {
            return ResponseEntity.status(404).body("No cart items found for this email.");
        }
        return ResponseEntity.ok(carts);
    }

    // Thêm sản phẩm vào giỏ hàng
    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody Cart cart) {
        if (cart.getEmail() == null || cart.getMenuItemId() == null) {
            return ResponseEntity.badRequest().body("Email and Menu Item ID are required!");
        }
        
        Optional<Cart> existingCartItem = cartService.findByEmailAndMenuItemId(cart.getEmail(), cart.getMenuItemId());
        if (existingCartItem.isPresent()) {
            return ResponseEntity.badRequest().body("Product already exists in the cart!");
        }

        Cart savedCart = cartService.addToCart(cart);
        return ResponseEntity.ok(savedCart);
    }

    // Xóa sản phẩm khỏi giỏ hàng theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable Long id) {
        boolean isDeleted = cartService.deleteCart(id);
        if (!isDeleted) {
            return ResponseEntity.status(404).body("Cart item not found!");
        }
        return ResponseEntity.ok("Cart item deleted successfully!");
    }

    // Cập nhật giỏ hàng
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCart(@PathVariable Long id, @RequestBody Cart cartData) {
        try {
            Cart updatedCart = cartService.updateCart(id, cartData);
            return ResponseEntity.ok(updatedCart);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // Lấy thông tin một sản phẩm trong giỏ hàng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getSingleCart(@PathVariable Long id) {
        Optional<Cart> cartItem = cartService.getSingleCart(id);
        return cartItem
                .map(cart -> ResponseEntity.ok(cart))
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }
}



