package G9.Foodi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import G9.Foodi.dto.CartDto;
import G9.Foodi.model.Cart;
import G9.Foodi.repository.CartRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

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
        // Kiểm tra xem item đã tồn tại trong giỏ hàng chưa
        if (cartRepository.findByEmailAndMenuItemId(cartDto.getEmail(), cartDto.getMenuItemId()).isPresent()) {
            throw new EntityExistsException("Product already exists in the cart!");
        }
        
        Cart cart = new Cart();
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
    public void deleteCart(Long id) {
        if (!cartRepository.existsById(id)) {
            throw new EntityNotFoundException("Cart item not found with id: " + id);
        }
        cartRepository.deleteById(id);
    }

    @Override
    public Cart updateCart(Long id, CartDto cartDto) {
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
    public Cart getCartById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found with id: " + id));
    }
} 