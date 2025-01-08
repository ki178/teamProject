package com.kms.teamproject.controllers;

import com.kms.teamproject.entities.CartEntity;
import com.kms.teamproject.entities.PayLoadEntity;
import com.kms.teamproject.services.PayService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Controller
@RequestMapping(value = "/pay")
public class PayController {
    private final PayService payService;

    @Autowired
    public PayController(PayService payService) {
        this.payService = payService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getCart() {
        ModelAndView mav = new ModelAndView();
        List<CartEntity> items = this.payService.getAllPay();
        mav.addObject("items", items);
        mav.setViewName("pay/pay");
        return mav;
    }


    @RequestMapping(value = "/submit", method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public String submitPayment(@RequestParam Map<String, String> formData) {
        // 데이터 파싱
        List<PayLoadEntity> payLoad = new ArrayList<>();
        String totalPriceString = formData.get("totalPrice");
        if (totalPriceString == null || totalPriceString.isEmpty()) {
            throw new IllegalArgumentException("totalPrice 값이 없습니다.");
        }
        int totalPrice = Integer.parseInt(totalPriceString);

        // items 파싱
        int index = 0;
        while (formData.containsKey(String.format("items[%d].payItemId", index))) {
            PayLoadEntity item = new PayLoadEntity();
            item.setPayItemId(Integer.parseInt(formData.get(String.format("items[%d].payItemId", index))));
            item.setPayItemName(formData.get(String.format("items[%d].payItemName", index)));
            item.setPayItemPrice(formData.get(String.format("items[%d].payItemPrice", index)));
            item.setPayQuantity(formData.get(String.format("items[%d].payQuantity", index)));
            item.setItemImage(formData.get(String.format("items[%d].itemImage", index)));
            payLoad.add(item);
            index++;
        }

        boolean isValid = this.payService.processPayment(payLoad, totalPrice);

        JSONObject response = new JSONObject();
        if (!isValid) {
            response.put("status", "fail");
            response.put("message", "결제에 실패했습니다.");
        } else {
            response.put("status", "success");
            response.put("message", "결제가 완료되었습니다.");
        }
        return response.toString();
    }


    @RequestMapping(value = "/record", method = RequestMethod.GET)
    public ModelAndView getRecord() {
        ModelAndView mav = new ModelAndView();
        List<PayLoadEntity> items = this.payService.getAllPayByCartId();

        // 날짜별로 묶기
        Map<LocalDateTime, List<PayLoadEntity>> groupedItems = items.stream()
                .collect(Collectors.groupingBy(PayLoadEntity::getPurchaseDay));

        mav.addObject("items", groupedItems);
        mav.setViewName("pay/pay-record");
        return mav;

    }
}
