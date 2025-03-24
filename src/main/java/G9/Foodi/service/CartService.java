package G9.Foodi.service;

import java.util.List;

import G9.Foodi.dto.CartDto;
import G9.Foodi.model.Cart;

public interface CartService {
    List<Cart> getCartsByEmail(String email);
    Cart addToCart(CartDto cartDto);
    void deleteCart(String id);
    Cart updateCart(String id, CartDto cartDto);
    Cart getCartById(String id);
}