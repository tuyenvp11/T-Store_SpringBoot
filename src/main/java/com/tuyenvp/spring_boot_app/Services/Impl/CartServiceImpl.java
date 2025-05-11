package com.tuyenvp.spring_boot_app.Services.Impl;

import com.tuyenvp.spring_boot_app.Model.Cart;
import com.tuyenvp.spring_boot_app.Model.ProductVariant;
import com.tuyenvp.spring_boot_app.Model.UserDtls;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import com.tuyenvp.spring_boot_app.Services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CartServiceImpl implements CartService {

    @Autowired
    DbConnect Dbconnect;

    public Cart saveCart(Integer variantId, Integer userId) {
        UserDtls userDtls = Dbconnect.userRepo.findById(userId).get();
        ProductVariant productVariant = Dbconnect.productVariantRepo.findById(variantId).get();
        Cart cartStatus = Dbconnect.cartRepo.findByProductVariantAndUser(productVariant.getVariantId(), userDtls.getId());


        Cart cart = null;

        if (ObjectUtils.isEmpty(cartStatus)) {
            cart = new Cart();
            cart.setProductVariant(productVariant);
            cart.setUser(userDtls);
            cart.setQuantity(1);
            //cart.setTotalPrice(product.getSellPrice());
        } else {
            cart = cartStatus;
            cart.setQuantity(cart.getQuantity() + 1);
            //cart.setTotalPrice(cart.getQuantity() * cart.getProduct().getProduct_price());
        }

        Cart saveCart = Dbconnect.cartRepo.save(cart);

        return saveCart;
    }


    @Override
    public List<Cart> getCartByUser(Integer userId) {
        List<Cart> carts = Dbconnect.cartRepo.findByUserId(userId);

        BigDecimal totalOrderPrice = BigDecimal.ZERO;
        List<Cart> updateCarts = new ArrayList<>();
        for (Cart c : carts) {
            if(c.getProductVariant() != null) {
                BigDecimal productPrice = c.getProductVariant().getSellPrice();
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
        Integer countByUserId = Dbconnect.cartRepo.countByUserId(userId);
        return countByUserId;
    }

    @Override
    public void updateQuantity(String sy, Long cid) {
        Cart cart = Dbconnect.cartRepo.findById(cid).get();
        int updateQuantity;

        if (sy.equalsIgnoreCase("de")) {
            updateQuantity = cart.getQuantity() - 1;

            if (updateQuantity <= 0) {
                Dbconnect.cartRepo.delete(cart);
            } else {
                cart.setQuantity(updateQuantity);
                Dbconnect.cartRepo.save(cart);
            }

        } else {
            updateQuantity = cart.getQuantity() + 1;
            cart.setQuantity(updateQuantity);
            Dbconnect.cartRepo.save(cart);
        }
    }

}