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
        return payMapper.selectAllCarts();
    }

    public void saveAllItemsToLoad(List<PayLoadEntity> items) {
        if (items != null && !items.isEmpty()) {
            for (PayLoadEntity item : items) {
                payMapper.insertItemLoad(item);
            }
        }
    }
}
