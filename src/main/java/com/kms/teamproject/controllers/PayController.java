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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @RequestMapping(value = "/submit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String submitPayment(@RequestBody List<PayLoadEntity> items){
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("결제할 항목이 제공되지 않습니다.");
        }

        // 데이터 저장
        this.payService.saveAllItemsToLoad(items);

        // 응답 데이터 생성
        JSONObject response = new JSONObject();
        response.put("status", "success");
        response.put("message", "결제가 성공적으로 처리되었습니다.");
        response.put("itemsProcessed", items.size());

        return response.toString();
    }

}
