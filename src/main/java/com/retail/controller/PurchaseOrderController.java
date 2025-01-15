package com.retail.controller;

import com.retail.entity.PurchaseOrders;
import com.retail.exception.InputValidationException;
import com.retail.service.PurchaseOrderService;
import com.retail.util.DateUtils;
import com.retail.util.ResponsePojo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;

@RestController
@RequestMapping("/api/purchaseOrder")
public class PurchaseOrderController {


    @Autowired
    PurchaseOrderService purchaseOrderService;

    /**
     * To save individual Purchase Order
     * @param purchaseOrders
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/savePurchaseOrder")
    public ResponseEntity<ResponsePojo> savePurchaseOrder(@Valid @RequestBody PurchaseOrders purchaseOrders) throws Exception {
        return new ResponseEntity<>(purchaseOrderService.savePurchaseOrder(purchaseOrders), HttpStatus.OK);
    }

    /**
     * To process rewards based on bulk Purchase Orders
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/bulkProcessPurchaseOrders")
    ResponseEntity<ResponsePojo> bulkProcessPurchaseOrders()  throws Exception{
        return new ResponseEntity<>(purchaseOrderService.bulkProcessPurchaseOrders(), HttpStatus.OK);
    }

    /**
     * To fetch order history based on Customer Id and From date
     * @param customerId
     * @param fromDate
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/fetchOrderHistory")
    ResponseEntity<ResponsePojo> fetchOrderHistory(@RequestHeader(required = true) String customerId,
                                                   @RequestHeader(required = false) String fromDate,
                                                   @RequestHeader(required = false) String toDate,
                                                   @RequestHeader(required = false) boolean lastThreeMonths) throws Exception {
        try {
            Date from = null;
            if(fromDate != null) {
                from =DateUtils.convertStringToDate(fromDate);
            }
            Date to = null;
            if(toDate != null) {
                to=DateUtils.convertStringToDate(toDate);
            }
            return new ResponseEntity<>(purchaseOrderService.fetchOrderHistory(customerId, from, to, lastThreeMonths), HttpStatus.OK);
        } catch (ParseException e){
            throw new InputValidationException("Date should be in yyyy-MM-dd format");
        }
    }
}
