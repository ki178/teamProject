package com.kms.teamproject.controllers;


import com.kms.teamproject.entities.CartEntity;
import com.kms.teamproject.services.CartService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


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

    @RequestMapping(value = "/empty", method = RequestMethod.GET)
    public ModelAndView getCart() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("cart/cart");
        return mav;
    }

    @RequestMapping(value = "/in", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getCartIndex() {
        ModelAndView mav = new ModelAndView();
        List<CartEntity> items = this.cartService.getAllCarts();
        mav.addObject("items", items);
        mav.setViewName("cart/cart-in");
        return mav;
    }


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
    public ResponseEntity<Void> updateCheck(@RequestParam(value = "itemId", required = false) Integer itemId,
                                            @RequestParam(value = "isChecked", required = false) Integer isChecked) {
        if (itemId == null || isChecked == null) {
            return ResponseEntity.badRequest().build();
        }
        this.cartService.updateCheckStatus(itemId, isChecked);
        return ResponseEntity.ok().build();
   }

    @RequestMapping(value = "/totalPrice", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> calculateTotalPrice( @RequestParam(value = "itemId", required = false) List<Integer> itemIds,
                                                                     @RequestParam(value = "itemPrice", required = false) List<Integer> itemPrices) {
        if (itemIds == null || itemPrices == null || itemIds.isEmpty() || itemPrices.isEmpty()) {
            return ResponseEntity.ok(Map.of("totalPrice", 0));
        }

        try {
            List<Integer> itemIdNumbers = itemIds.stream().map(Integer::valueOf).toList();
            List<Integer> itemPriceNumbers = itemPrices.stream().map(Integer::valueOf).toList();
            if (itemIdNumbers.size() != itemPriceNumbers.size()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid input data"));
            }
            int totalPrice = this.cartService.calculateTotalPrice(itemIds, itemPrices);
            return ResponseEntity.ok(Map.of("totalPrice", totalPrice));
        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid number format"));
        }
    }

    @RequestMapping(value = "/deleteItem", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Void> deleteItem(@RequestParam(value = "itemId", required = true) Integer itemId) {
        if (itemId == null) {
            return ResponseEntity.badRequest().build();
        }
       this.cartService.deleteItem(itemId);
        return ResponseEntity.ok().build();
    }




}
