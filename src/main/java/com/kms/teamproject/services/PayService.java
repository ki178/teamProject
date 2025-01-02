package com.kms.teamproject.services;

import com.kms.teamproject.entities.CartEntity;
import com.kms.teamproject.entities.PayLoadEntity;
import com.kms.teamproject.mappers.PayMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PayService {
    private final PayMapper payMapper;

    @Autowired
    public PayService(PayMapper payMapper) {
        this.payMapper = payMapper;
    }


    public List<CartEntity> getAllPay(){
        return this.payMapper.selectAllCarts();
    }

    // 전달받은 값을 db에 저장, 하기 전에 검사 진행
    public boolean processPayment(List<PayLoadEntity> payload, int totalPrice) {
        int totalPriceSum = 0;

        for (PayLoadEntity item : payload) {
            CartEntity cartItem = this.payMapper.selectCartById(item.getPayItemId());
            if (cartItem == null ||
            !cartItem.getItemName().equals(item.getPayItemName()) ||
            cartItem.getItemPrice() * cartItem.getQuantity() != Integer.parseInt(item.getPayItemPrice()) ||
            cartItem.getQuantity() != Integer.parseInt(item.getPayQuantity())) {
                return false;
            }
            totalPriceSum += cartItem.getItemPrice() * cartItem.getQuantity();
        }

        if (totalPriceSum == totalPrice) {
            for (PayLoadEntity item : payload) {
                item.setTotalPrice(totalPrice);
                this.payMapper.insertItemLoad(item);
            }
            return false;
        }
        return true;
    }


}
