package com.kms.teamproject.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = {"payItemId"})
public class PayEntity {
    private int payItemId;
    private String payItemName;
    private String payItemPrice;
    private int payQuantity;
    private String payTotalPrice;
}
