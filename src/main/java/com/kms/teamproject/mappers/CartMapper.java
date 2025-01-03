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

    CartEntity selectCartByIndex(@Param("index") int index);

    CartEntity selectCartByPrice(@Param("cartPrice")int cartPrice);

    int updateCart(CartEntity quantity);

    void updateCheckStatus(@Param("index") int index, @Param("isChecked") int isChecked);

    void deleteCartItem(@Param("index") int index);

    void updateDeletedStatusForItems(@Param("indices") List<Integer> indices);


    int countActiveItems();

    int countCheckedItems();
}
