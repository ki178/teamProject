package com.kms.teamproject.services;

import com.kms.teamproject.entities.CartEntity;
import com.kms.teamproject.mappers.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CartService {
    private final CartMapper cartMapper;

    @Autowired
    public CartService(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    public List<CartEntity> getAllCarts() {
        return cartMapper.selectAllCarts();
    }


    public int plus(CartEntity cart, int quantity, int itemId) throws IllegalArgumentException {
        final int Max_Quantity = 50;
        if (quantity < 1 || cart.getQuantity() >= Max_Quantity) {
            return cart.getQuantity();
        }



        CartEntity cartItem = this.cartMapper.selectCartById(itemId);
        if (cartItem == null) {
            throw new IllegalArgumentException("Invalid itemId: " + itemId);
        }

        int newQuantity = cartItem.getQuantity() + 1;
        if (newQuantity > Max_Quantity) {
            newQuantity = Max_Quantity;
        }

        cartItem.setQuantity(newQuantity);
        this.cartMapper.updateCart(cartItem);

        return newQuantity;
    }

    public int minus(int quantity, int itemId)throws IllegalArgumentException  {
        final int Min_Quantity = 1;

        CartEntity cartItem = this.cartMapper.selectCartById(itemId);
        if (cartItem == null) {
            throw new IllegalArgumentException("Invalid itemId: " + itemId);
        }

        if (quantity < 1 || cartItem.getQuantity() <= Min_Quantity) {
            return cartItem.getQuantity();  // 수량이 이미 최소값이면 그대로 반환
        }

        int newQuantity  = cartItem.getQuantity() - 1;
        if (newQuantity < Min_Quantity) {
            newQuantity = Min_Quantity; // 최소 수량으로 제한
        }

        cartItem.setQuantity(newQuantity);
        this.cartMapper.updateCart(cartItem);

        return newQuantity;
    }



   public void updateCheckStatus(int itemId, int isChecked){
        this.cartMapper.updateCheckStatus(itemId,isChecked);
   }

    public int calculateTotalPrice(List<Integer> itemIds, List<Integer> itemPrices) {
        if (itemIds == null || itemPrices == null || itemIds.size() != itemPrices.size()) {
            throw new IllegalArgumentException("Invalid input data: itemIds or itemPrices is null or mismatched");
        }

        int totalPrice = 0;
        for (int i = 0; i < itemIds.size(); i++) {
            totalPrice += itemPrices.get(i); // itemPrice 합산
        }
        return totalPrice;
    }

}