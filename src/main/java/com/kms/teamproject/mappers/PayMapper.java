package com.kms.teamproject.mappers;

import com.kms.teamproject.entities.CartEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PayMapper {

    List<CartEntity> selectAllCarts();
}
