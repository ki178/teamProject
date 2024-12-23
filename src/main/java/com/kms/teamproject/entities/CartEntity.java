package com.kms.teamproject.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = {"itemId"})
public class CartEntity {
 private int itemId;
 private String itemName;
 private int itemPrice;
 private int quantity;
 private int isChecked;
 private boolean isDeleted;
 private int status;
}
