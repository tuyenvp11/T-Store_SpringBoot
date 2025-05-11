package com.tuyenvp.spring_boot_app.Services;

import com.tuyenvp.spring_boot_app.Model.Cart;

import java.util.List;

public interface CartService {
    public Cart saveCart(Integer variantId, Integer userId);
    public List<Cart> getCartByUser(Integer userId);
    public Integer getCountCart(Integer userId);
    public void updateQuantity(String sy, Long cid);
    //void addToCart(String userName, int variantId, int quantity);
    //List<Cart> getUserCart(String username);
}
