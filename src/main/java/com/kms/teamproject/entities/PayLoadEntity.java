package com.kms.teamproject.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMapping;

@Getter
@Setter
@EqualsAndHashCode(of = {"index"})
public class PayLoadEntity {
    private int index;
    private int payIndex;
    private int payItemId;
    private String payItemName;
    private String payItemPrice;
    private String payQuantity;
    private String itemImage;
    private int totalPrice;
}
