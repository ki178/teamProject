package com.kms.teamproject.mappers;

import com.kms.teamproject.entities.CartEntity;
import com.kms.teamproject.entities.PayLoadEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PayMapper {

    List<CartEntity> selectAllCarts();

    void insertItemLoad(PayLoadEntity payLoadEntity);

    CartEntity selectCartById(int itemId);

    Integer getPayIndexByCartIndex(int cartIndex);

}
