package com.kms.teamproject.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = {"index"})
public class CartEntity {
 private int index; // 추가한 것
 private String memberId; // 추가한 것
 private String cartId; // 추가한 것
 private int itemId;
 private String itemName;
 private int itemPrice;
 private String itemImage;
 private int quantity;
 private int isChecked;
 private boolean isDeleted;
 private int status;
}
