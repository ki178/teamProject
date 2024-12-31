package com.kms.teamproject.services;

import com.kms.teamproject.entities.CartEntity;
import com.kms.teamproject.entities.PayLoadEntity;
import com.kms.teamproject.mappers.PayMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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

    public boolean validateTotalPrice(List<PayLoadEntity> items, int totalPriceFromClient) {

        int calculatedTotalPrice = 0;
        for (PayLoadEntity item : items) {
            CartEntity dbItem = this.payMapper.selectCartById(item.getPayItemId());
            if (dbItem == null) {
                throw new IllegalArgumentException("상품 ID가 유효하지 않습니다: " + item.getPayItemId());
            }
            // 가격 및 수량 비교
            int dbItemPrice = dbItem.getItemPrice() * dbItem.getQuantity();
            if (dbItemPrice != Integer.parseInt(item.getPayItemPrice()) ||
                    dbItem.getQuantity() != Integer.parseInt(item.getPayQuantity())) {
                throw new IllegalArgumentException("상품 데이터가 일치하지 않습니다. 상품 ID: " + item.getPayItemId());
            }
            // 총합 계산
            calculatedTotalPrice += dbItem.getItemPrice() * dbItem.getQuantity();
        }
        if (calculatedTotalPrice != totalPriceFromClient) {
            throw new IllegalArgumentException("총 결제 금액이 일치하지 않습니다.");
        }
        return true;
    }

    public void saveAllItemsToLoad(List<PayLoadEntity> items) {
        if (items != null && !items.isEmpty()) {
            for (PayLoadEntity item : items) {
                this.payMapper.insertItemLoad(item);
            }
        }
    }
}
