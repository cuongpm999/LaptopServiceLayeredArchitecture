package vn.ptit.services;

import vn.ptit.dtos.CartDTO;
import vn.ptit.entities.mysql.Cart;
import vn.ptit.entities.mysql.User;

public interface CartService {
    public CartDTO findCurrentCartByCustomer(String username);
    public void addToCart(String username, int laptopId);
    public void editCart(String username, int laptopId, int quantity);
    public void deleteCart(String username, int laptopId);
}
