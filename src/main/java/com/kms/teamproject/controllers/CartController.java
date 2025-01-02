package com.kms.teamproject.controllers;


import com.kms.teamproject.entities.CartEntity;
import com.kms.teamproject.entities.MemberEntity;
import com.kms.teamproject.services.CartService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.security.Principal;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "/cart")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }



    @RequestMapping(value = "/in", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getCartIndex() {
        ModelAndView mav = new ModelAndView();
        // 장바구니 아이템 조회
        List<CartEntity> items = this.cartService.getAllCarts();
        boolean hasUncheckedItems = items.stream().anyMatch(item -> item.getIsChecked() == 0);
        mav.addObject("items", items);
        mav.addObject("hasUncheckedItems", hasUncheckedItems);

        mav.setViewName("cart/cart-in");
        return mav;
    }

    // 수량 증가 , 감소
    @RequestMapping(value = "/plus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postCartPlus(@RequestParam(value = "itemQuantity", required = false) int quantity,
                               @RequestParam(value = "itemId", required = false) int itemId,
                               CartEntity cart) throws IllegalArgumentException {
        JSONObject response = new JSONObject();
        int result = this.cartService.plus(cart, quantity, itemId);

        response.put("result", result);
        return response.toString();
    }
    @RequestMapping(value = "/minus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postCartMinus(@RequestParam(value = "itemQuantity", required = false) int quantity,
                               @RequestParam(value = "itemId", required = false) int itemId) throws IllegalArgumentException {
        JSONObject response = new JSONObject();

        try {
            int result = this.cartService.minus(quantity, itemId);
            response.put("result", result);

        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
        }

        return response.toString();
    }


   @RequestMapping(value = "/updateCheck", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String updateCheck(@RequestParam(value = "itemId", required = false) Integer itemId,
                                            @RequestParam(value = "isChecked", required = false) Integer isChecked) {
        JSONObject response = new JSONObject();
        if (itemId == null || isChecked == null) {
            return response.toString();
        }
        this.cartService.updateCheckStatus(itemId, isChecked);
        response.put("result", "success");
        return response.toString();
   }
    // 상품 전체 가격(체크된 항목만)
    @RequestMapping(value = "/totalPrice", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String calculateTotalPrice( @RequestParam(value = "itemId", required = false) List<Integer> itemIds,
                                                                     @RequestParam(value = "itemPrice", required = false) List<Integer> itemPrices) {
        JSONObject response = new JSONObject();
        if (itemIds == null || itemPrices == null || itemIds.isEmpty() || itemPrices.isEmpty()) {
            response.put("totalPrice", 0);
            return response.toString();
        }

        try {

            if (itemIds.size() != itemPrices.size()) {
                response.put("error", "입력크기가 일치하지 않습니다");
                return response.toString();
            }
            int totalPrice = this.cartService.calculateTotalPrice(itemIds, itemPrices);
            response.put("totalPrice", totalPrice);
        }catch (NumberFormatException e) {
            response.put("error","잘못된 숫자 형식입니다.");
        }
        return response.toString();
    }
    // 상품삭제
    @RequestMapping(value = "/deleteItem", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteItem(@RequestParam(value = "itemId", required = true) Integer itemId) {
        JSONObject response = new JSONObject();
        if (itemId == null) {
            response.put("error", "잘못된 item ID 입니다");
            return response.toString();
        }
       this.cartService.deleteItem(itemId);
        response.put("result", "success");
        return response.toString();
    }
    // 상품 선택 삭제
    @RequestMapping(value = "/deleteSelectedItems", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteSelectedItems(@RequestParam("itemIds") List<Integer> itemIds) {
        JSONObject response = new JSONObject();
        if (itemIds == null || itemIds.isEmpty()) {
            response.put("error", "삭제할 항목이 선택되지 않았습니다");
            return response.toString();
        }
        this.cartService.deleteSelectedItems(itemIds);
        response.put("result", "success");
        return response.toString();
    }

    // 전체 선택 체크박스와 샛별배송 체크박스 상황 분석
    @RequestMapping(value = "/getCheckboxStatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getCheckboxStatus() {
        JSONObject response = new JSONObject();
        List<CartEntity> items = this.cartService.getAllCarts();
        boolean isAllChecked = items.stream().allMatch(item -> item.getIsChecked() == 1);
        response.put("isAllChecked", isAllChecked);
        return response.toString();
    }

    // 장바구니에서 결제주문서로 넘어가기 위한 메서드(item이  있는지, 체크된 item이 있는지)
    @RequestMapping(value = "/getCartStatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getCartStatus() {
        JSONObject response = new JSONObject();
        boolean hasItems = this.cartService.hasActiveItems();
        boolean hasCheckedItems = this.cartService.hasCheckedItems();
        response.put("hasItems", hasItems);
        response.put("hasCheckedItems", hasCheckedItems);
        return response.toString();
    }

}
