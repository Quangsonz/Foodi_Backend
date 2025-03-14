package G9.Foodi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import G9.Foodi.models.Cart;
import G9.Foodi.repositories.CartRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    // Lấy danh sách giỏ hàng theo email
    public List<Cart> getCartByEmail(String email) {
        return cartRepository.findByEmail(email);
    }

    // Thêm sản phẩm vào giỏ hàng
    public Cart addToCart(Cart cart) {
        if (cart.getEmail() == null || cart.getMenuItemId() == null) {
            throw new IllegalArgumentException("Email and Menu Item ID are required!");
        }
        return cartRepository.save(cart);
    }

    // Xóa sản phẩm khỏi giỏ hàng theo ID
    @Transactional
    public boolean deleteCart(Long id) {
        if (cartRepository.existsById(id)) {
            cartRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Tìm sản phẩm theo email và menuItemId
    public Optional<Cart> findByEmailAndMenuItemId(String email, String menuItemId) {
        return cartRepository.findByEmailAndMenuItemId(email, menuItemId);
    }

    // Lấy thông tin một sản phẩm trong giỏ hàng theo ID
    public Optional<Cart> getSingleCart(Long id) {
        return cartRepository.findById(id);
    }

    // Cập nhật thông tin giỏ hàng
    @Transactional
    public Cart updateCart(Long id, Cart cartData) {
        return cartRepository.findById(id).map(cart -> {
            if (cartData.getName() != null) cart.setName(cartData.getName());
            if (cartData.getRecipe() != null) cart.setRecipe(cartData.getRecipe());
            if (cartData.getImage() != null) cart.setImage(cartData.getImage());
            if (cartData.getPrice() != null) cart.setPrice(cartData.getPrice());
            if (cartData.getQuantity() != null) cart.setQuantity(cartData.getQuantity());
            return cartRepository.save(cart);
        }).orElseThrow(() -> new RuntimeException("Cart item not found"));
    }
}
