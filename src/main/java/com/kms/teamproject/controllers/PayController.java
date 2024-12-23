package com.kms.teamproject.controllers;

import com.kms.teamproject.entities.CartEntity;
import com.kms.teamproject.services.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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

}
