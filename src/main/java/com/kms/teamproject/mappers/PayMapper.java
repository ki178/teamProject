package com.kms.teamproject.mappers;

import com.kms.teamproject.entities.CartEntity;
import com.kms.teamproject.entities.PayLoadEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PayMapper {

    List<CartEntity> selectAllCarts();

    List<PayLoadEntity> selectAllPayLoads();

    void insertItemLoad(PayLoadEntity payLoadEntity);

    CartEntity selectCartById(int itemId);

    List<Integer> getPayIndexByCartIndex(@Param("payItemId") int payItemId);

}
