package com.kms.teamproject.services;

import com.kms.teamproject.entities.CartEntity;
import com.kms.teamproject.entities.MemberEntity;
import com.kms.teamproject.entities.PayEntity;
import com.kms.teamproject.mappers.CartMapper;
import com.kms.teamproject.mappers.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CartService {
    private final CartMapper cartMapper;
    private final MemberMapper memberMapper;


    @Autowired
    public CartService(CartMapper cartMapper, MemberMapper memberMapper) {
        this.cartMapper = cartMapper;
        this.memberMapper = memberMapper;
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


    public void deleteItem(int itemId) {
        CartEntity cartItem = this.cartMapper.selectCartById(itemId);
        if (cartItem == null) {
            throw new IllegalArgumentException("Invalid itemId: " + itemId);
        }
        cartItem.setDeleted(true); // isDeleted = 1로 설정
        this.cartMapper.deleteCartItem(itemId);
    }

    public void deleteSelectedItems(List<Integer> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) {
            throw new IllegalArgumentException("Item ID list is empty");
        }
        this.cartMapper.updateDeletedStatusForItems(itemIds);
    }

    public boolean payMove(PayEntity pay){
        return false;
    }


}
