package G9.Foodi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import G9.Foodi.dto.CartDto;
import G9.Foodi.model.Cart;
import G9.Foodi.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public List<Cart> getCartsByEmail(String email) {
        return cartRepository.findByEmail(email);
    }

    @Override
    public Cart addToCart(CartDto cartDto) {
        return cartRepository.findByEmailAndMenuItemId(cartDto.getEmail(), cartDto.getMenuItemId())
            .map(existingItem -> {
                // Nếu đã tồn tại, tăng số lượng
                existingItem.setQuantity(existingItem.getQuantity() + cartDto.getQuantity());
                return cartRepository.save(existingItem);
            })
            .orElseGet(() -> {
                // Nếu chưa tồn tại, tạo mới
                Cart cart = new Cart();
                cart.setMenuItemId(cartDto.getMenuItemId());
                cart.setName(cartDto.getName());
                cart.setRecipe(cartDto.getRecipe() != null ? cartDto.getRecipe() : "");
                cart.setImage(cartDto.getImage());
                cart.setPrice(cartDto.getPrice());
                cart.setQuantity(cartDto.getQuantity());
                cart.setEmail(cartDto.getEmail());
                return cartRepository.save(cart);
            });
    }

    @Override
    public void deleteCart(String id) {
        if (!cartRepository.existsById(id)) {
            throw new IllegalArgumentException("Cart item not found with id: " + id);
        }
        cartRepository.deleteById(id);
    }

    @Override
    public Cart updateCart(String id, CartDto cartDto) {
        Cart cart = getCartById(id);
        
        cart.setMenuItemId(cartDto.getMenuItemId());
        cart.setName(cartDto.getName());
        cart.setRecipe(cartDto.getRecipe());
        cart.setImage(cartDto.getImage());
        cart.setPrice(cartDto.getPrice());
        cart.setQuantity(cartDto.getQuantity());
        cart.setEmail(cartDto.getEmail());
        
        return cartRepository.save(cart);
    }

    @Override
    public Cart getCartById(String id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found with id: " + id));
    }
}