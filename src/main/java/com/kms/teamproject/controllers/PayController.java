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


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


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

    @RequestMapping(value = "/record", method = RequestMethod.GET)
    public ModelAndView getRecord() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("pay/pay-record");
        return mav;

    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public String submitPayment(@RequestBody JSONObject payload) {
        System.out.println(payload);
        int totalPrice = payload.getInt("totalPrice");
        List<PayLoadEntity> items = new ArrayList<>();
        payload.getJSONArray("items").forEach(item -> {
            JSONObject jsonItem = (JSONObject) item;
            PayLoadEntity entity = new PayLoadEntity();
            entity.setPayItemId(jsonItem.getInt("itemId"));
            entity.setPayItemName(jsonItem.getString("itemName"));
            entity.setPayItemPrice(String.valueOf(jsonItem.getInt("itemPrice")));
            entity.setPayQuantity(String.valueOf(jsonItem.getInt("itemQuantity")));
            entity.setItemImage(jsonItem.getString("itemImage"));
            items.add(entity);
        });

        // 디버깅 출력
        System.out.println("생성된 items 리스트: " + items);

        // 리스트가 비어 있는 경우 처리
        if (items.isEmpty()) {
            throw new IllegalArgumentException("결제 항목이 없습니다.");
        }
        this.payService.validateTotalPrice(items, totalPrice);
        this.payService.saveAllItemsToLoad(items);

        JSONObject response = new JSONObject();
        response.put("status", "success");
        response.put("message", "결제가 성공적으로 처리되었습니다.");
        response.put("itemsProcessed", items.size());
        return response.toString();
    }

}
