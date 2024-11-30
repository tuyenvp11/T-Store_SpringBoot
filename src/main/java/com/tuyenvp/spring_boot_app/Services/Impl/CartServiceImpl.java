package com.tuyenvp.spring_boot_app.Services.Impl;

import com.tuyenvp.spring_boot_app.Model.Cart;
import com.tuyenvp.spring_boot_app.Model.Product;
import com.tuyenvp.spring_boot_app.Model.UserDtls;
import com.tuyenvp.spring_boot_app.Repository.CartRepo;
import com.tuyenvp.spring_boot_app.Repository.ProductRepo;
import com.tuyenvp.spring_boot_app.Repository.UserRepo;
import com.tuyenvp.spring_boot_app.Services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    public Cart saveCart(Integer product_product_id, Integer user_id) {
        UserDtls userDtls = userRepo.findById(user_id).get();
        Product product = productRepo.findById(product_product_id).get();
        Cart cartStatus = cartRepo.findByProductAndUser(product.getProduct_id(), userDtls.getId());
        

        Cart cart = null;

        if (ObjectUtils.isEmpty(cartStatus)) {
            cart = new Cart();
            cart.setProduct(product);
            cart.setUser_dtls(userDtls);
            cart.setQuantity(1);
            cart.setTotalPrice(1 * product.getProduct_price());
        } else {
            cart = cartStatus;
            cart.setQuantity(cart.getQuantity() + 1);
            cart.setTotalPrice(cart.getQuantity() * cart.getProduct().getProduct_price());
        }

        Cart saveCart = cartRepo.save(cart);

        return saveCart;
    }

    @Override
    public List<Cart> getCartByUser(Integer userId) {
        List<Cart> carts = cartRepo.findByUserId(userId);

        BigDecimal totalOrderPrice = BigDecimal.ZERO;
        List<Cart> updateCarts = new ArrayList<>();
        for (Cart c : carts) {
            if(c.getProduct() != null) {
                BigDecimal productPrice = BigDecimal.valueOf(c.getProduct().getProduct_price());
                BigDecimal quantity = BigDecimal.valueOf(c.getQuantity());
                BigDecimal totalPrice = productPrice.multiply(quantity);
                c.setTotalPrice(totalPrice.doubleValue()); // Assuming setTotalPrice accepts Double.
                totalOrderPrice = totalOrderPrice.add(totalPrice);
                c.setTotalOrderPrice(totalOrderPrice); // Proper BigDecimal assignment.
                updateCarts.add(c);

//                Double totalPrice = (double) (c.getProduct().getProduct_price() * c.getQuantity());
//                c.setTotalPrice(totalPrice);
//                totalOrderPrice = totalOrderPrice + totalPrice;
//                c.setTotalOrderPrice(totalOrderPrice);
//                updateCarts.add(c);
            }
            else {
                c.setTotalPrice(0.0);
                c.setTotalOrderPrice(totalOrderPrice);
            }
        }
        return updateCarts;
    }

    @Override
    public Integer getCountCart(Integer userId) {
        Integer countByUserId = cartRepo.countByUserId(userId);
        return countByUserId;
    }

    @Override
    public void updateQuantity(String sy, Long cid) {
        Cart cart = cartRepo.findById(cid).get();
        int updateQuantity;

        if (sy.equalsIgnoreCase("de")) {
            updateQuantity = cart.getQuantity() - 1;

            if (updateQuantity <= 0) {
                cartRepo.delete(cart);
            } else {
                cart.setQuantity(updateQuantity);
                cartRepo.save(cart);
            }

        } else {
            updateQuantity = cart.getQuantity() + 1;
            cart.setQuantity(updateQuantity);
            cartRepo.save(cart);
        }
    }
}
