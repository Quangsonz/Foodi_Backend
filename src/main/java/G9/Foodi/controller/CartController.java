package G9.Foodi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import G9.Foodi.dto.CartDto;
import G9.Foodi.model.Cart;
import G9.Foodi.service.CartService;

@RestController
@RequestMapping("/api/v1/carts")
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {

    private static final Logger logger = Logger.getLogger(CartController.class.getName());
    
    @Autowired
    private CartService cartService;
    
    @GetMapping
    public ResponseEntity<List<Cart>> getCartsByEmail(@RequestParam String email, Authentication authentication) {
        if (!email.equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(cartService.getCartsByEmail(email));
    }
    
    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody CartDto cartDto, Authentication authentication) {
        // Logging thông tin
        logger.info("Received cart item: " + cartDto);
        logger.info("Authentication: " + (authentication != null ? authentication.getName() : "null"));
        
        // Kiểm tra xác thực
        if (authentication == null || !cartDto.getEmail().equals(authentication.getName())) {
            logger.warning("Authentication failed: " + 
                           (authentication == null ? "Authentication is null" : 
                                                   "Email mismatch: " + cartDto.getEmail() + " vs " + authentication.getName()));
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Unauthorized: Email does not match authenticated user");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        try {
            // Kiểm tra dữ liệu
            if (cartDto.getMenuItemId() == null || cartDto.getMenuItemId().isEmpty()) {
                logger.warning("MenuItemId is null or empty");
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "MenuItemId cannot be null or empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            Cart cart = cartService.addToCart(cartDto);
            logger.info("Cart item added successfully: " + cart);
            return new ResponseEntity<>(cart, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.severe("Error adding to cart: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (Exception e) {
            logger.severe("Unexpected error: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable String id) {
        try {
            cartService.deleteCart(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(cartService.getCartById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Cart> updateCart(@PathVariable String id, @RequestBody CartDto cartDto) {
        try {
            return ResponseEntity.ok(cartService.updateCart(id, cartDto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}