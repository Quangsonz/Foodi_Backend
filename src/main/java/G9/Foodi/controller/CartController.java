package G9.Foodi.controller;

// Import các thư viện cần thiết
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

// Đánh dấu đây là một REST controller, trả về dữ liệu JSON thay vì view
@RestController
// Định nghĩa prefix cho tất cả các endpoint trong controller này
@RequestMapping("/api/v1/carts")
// Cho phép CORS từ frontend (React) ở địa chỉ này
@CrossOrigin(origins = "http://35.224.60.159:80")
public class CartController {

    // Logger để ghi log cho controller này
    private static final Logger logger = Logger.getLogger(CartController.class.getName());
    
    // Inject CartService để thao tác với logic giỏ hàng
    @Autowired
    private CartService cartService;
    
    // Lấy danh sách giỏ hàng theo email (chỉ cho phép user lấy giỏ hàng của chính mình)
    @GetMapping
    public ResponseEntity<List<Cart>> getCartsByEmail(@RequestParam String email, Authentication authentication) {
        // Kiểm tra email trong request có khớp với user đã xác thực không
        if (!email.equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(cartService.getCartsByEmail(email));
    }
    
    // Thêm sản phẩm vào giỏ hàng
    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody CartDto cartDto, Authentication authentication) {
        // Logging thông tin nhận được
        logger.info("Received cart item: " + cartDto);
        logger.info("Authentication: " + (authentication != null ? authentication.getName() : "null"));
        
        // Kiểm tra xác thực: user phải đăng nhập và email trong cartDto phải khớp với user đã xác thực
        if (authentication == null || !cartDto.getEmail().equals(authentication.getName())) {
            logger.warning("Authentication failed: " + 
                           (authentication == null ? "Authentication is null" : 
                                                   "Email mismatch: " + cartDto.getEmail() + " vs " + authentication.getName()));
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Unauthorized: Email does not match authenticated user");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        try {
            // Kiểm tra dữ liệu: menuItemId không được null hoặc rỗng
            if (cartDto.getMenuItemId() == null || cartDto.getMenuItemId().isEmpty()) {
                logger.warning("MenuItemId is null or empty");
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "MenuItemId cannot be null or empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            // Gọi service để thêm vào giỏ hàng
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
    
    // Xóa một item khỏi giỏ hàng theo id (chỉ cho phép xóa item của chính mình)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable String id, Authentication authentication) {
        try {
            // Kiểm tra item tồn tại
            Cart cart = cartService.getCartById(id);
            
            // Kiểm tra quyền sở hữu
            if (!cart.getEmail().equals(authentication.getName())) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "You can only delete your own cart items");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            
            // Xóa item khỏi giỏ hàng
            cartService.deleteCart(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    // Lấy thông tin một item trong giỏ hàng theo id
    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(cartService.getCartById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Cập nhật thông tin một item trong giỏ hàng (chỉ cho phép cập nhật item của chính mình)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCart(@PathVariable String id, @RequestBody CartDto cartDto, Authentication authentication) {
        try {
            // Kiểm tra item tồn tại
            Cart existingCart = cartService.getCartById(id);
            
            // Kiểm tra quyền sở hữu
            if (!existingCart.getEmail().equals(authentication.getName())) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "You can only update your own cart items");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            
            // Kiểm tra dữ liệu: số lượng phải lớn hơn 0
            if (cartDto.getQuantity() == null || cartDto.getQuantity() < 1) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Quantity must be greater than 0");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            // Giữ nguyên thông tin không thay đổi
            cartDto.setMenuItemId(existingCart.getMenuItemId());
            cartDto.setName(existingCart.getName());
            cartDto.setRecipe(existingCart.getRecipe());
            cartDto.setImage(existingCart.getImage());
            cartDto.setPrice(existingCart.getPrice());
            cartDto.setEmail(existingCart.getEmail());
            
            // Gọi service để cập nhật item trong giỏ hàng
            Cart updatedCart = cartService.updateCart(id, cartDto);
            return ResponseEntity.ok(updatedCart);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
