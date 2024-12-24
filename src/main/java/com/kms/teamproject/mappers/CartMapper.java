package com.kms.teamproject.mappers;

import com.kms.teamproject.entities.CartEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CartMapper {
    int insertCart(CartEntity cart);


    List<CartEntity> selectAllCarts();

    CartEntity selectCartById(@Param("itemId") int itemId);

    CartEntity selectCartByPrice(@Param("cartPrice")int cartPrice);

    int updateCart(CartEntity quantity);

    void updateCheckStatus(@Param("itemId") int itemId, @Param("isChecked") int isChecked);

    void deleteCartItem(@Param("itemId") int itemId);
}
