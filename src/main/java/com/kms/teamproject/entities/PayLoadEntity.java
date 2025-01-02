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
    private String payRecordId;
    private int payItemId;
    private String payItemName;
    private String payItemPrice;
    private String payQuantity;
    private int itemStatus;
    private String itemImage;
    private int totalPrice;
}
