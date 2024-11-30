package com.tuyenvp.spring_boot_app.Services;

import com.tuyenvp.spring_boot_app.Model.Cart;

import java.util.List;

public interface CartService {
    public Cart saveCart(Integer product_product_id, Integer user_id);
    public List<Cart> getCartByUser(Integer userId);
    public Integer getCountCart(Integer user_id);
    public void updateQuantity(String sy, Long cid);
}
