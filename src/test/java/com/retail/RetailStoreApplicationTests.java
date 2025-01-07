package com.retail;

import com.retail.controller.CustomerController;
import com.retail.controller.PurchaseOrderController;
import com.retail.entity.Customers;
import com.retail.entity.PurchaseOrders;
import com.retail.repository.CustomerRepository;
import com.retail.repository.PurchaseOrderRepository;
import com.retail.service.CustomerService;
import com.retail.service.PurchaseOrderService;
import com.retail.util.ApplicationConstants;
import com.retail.util.ResponsePojo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class RetailStoreApplicationTests {

    @InjectMocks
    PurchaseOrderService purchaseOrderService;

    @InjectMocks
    CustomerService mockCustomerService;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    PurchaseOrderRepository purchaseOrderRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

  /*  @Test
    void testCreateCustomer() throws Exception {
        Customers customers=new Customers("name","987654321");

        when(mockCustomerService.saveCustomer(customers)).thenReturn(new ResponsePojo(ApplicationConstants.SUCCESS, "Customer Saved Successfully!",customers));
        // when
        ResponseEntity<ResponsePojo> response = customerController.saveCustomer(customers);

        // then
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(ApplicationConstants.SUCCESS, response.getBody().getStatus())
        );
    }*/

    @Test
    void testCreatePurchaseOrderWithDiffOrderTotals() throws Exception {
        PurchaseOrders orders = new PurchaseOrders("customerId", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0);
        Customers customers = new Customers("id", "name", "987654321", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), ApplicationConstants.ADMIN_USER);
        when(customerRepository.findById("customerId")).thenReturn(Optional.of(customers));
        when(purchaseOrderRepository.save(orders)).thenReturn(orders);


        //Scenario 1 - Order Total is 120 hence reward should be 90
        ResponsePojo response = purchaseOrderService.savePurchaseOrder(orders);
        PurchaseOrders purchaseOrders = (PurchaseOrders) response.getData();
        assertNotNull(response);
        assertEquals(ApplicationConstants.SUCCESS, response.getStatus());
        assertEquals(90.0, purchaseOrders.getTotalRewards());


        //Scenario 2 - Order Total is 100 hence reward should be 50
        orders.setOrderTotal(100.0);
        response = purchaseOrderService.savePurchaseOrder(orders);
        purchaseOrders = (PurchaseOrders) response.getData();
        // then
        assertNotNull(response);
        assertEquals(ApplicationConstants.SUCCESS, response.getStatus());
        assertEquals(50.0, purchaseOrders.getTotalRewards());

        //Scenario 3 - Order Total is 75 hence reward should be 25
        orders.setOrderTotal(75.0);
        response = purchaseOrderService.savePurchaseOrder(orders);
        purchaseOrders = (PurchaseOrders) response.getData();
        // then
        assertNotNull(response);
        assertEquals(ApplicationConstants.SUCCESS, response.getStatus());
        assertEquals(25.0, purchaseOrders.getTotalRewards());

        //Scenario 4 - Order Total is 50 hence reward should be 0
        orders.setOrderTotal(50.0);
        response = purchaseOrderService.savePurchaseOrder(orders);
        purchaseOrders = (PurchaseOrders) response.getData();
        // then
        assertNotNull(response);
        assertEquals(ApplicationConstants.SUCCESS, response.getStatus());
        assertEquals(90.0, purchaseOrders.getTotalRewards());
    }


   /* @Test
    void testOrderFetchHistory() throws Exception {

        when(customerRepository.findById(any())).thenReturn(Optional.of(new Customers("name", "987654321")));
        when(purchaseOrderRepository.findByCustomerIdAndOrderDateBetween("customerId", convertStringToDate("2025-01-01"),
                convertStringToDate("2025-01-05"))).thenReturn(Arrays.asList(
                new PurchaseOrders("C1", "O1", convertStringToDate("2025-01-01").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0),
                new PurchaseOrders("C1", "O1", convertStringToDate("2025-01-05").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0)));

        when(purchaseOrderRepository.findByCustomerIdAndOrderDateGreaterThanEqual("customerId", convertStringToDate("2025-01-01"))).thenReturn(Arrays.asList(
                new PurchaseOrders("C1", "O1", convertStringToDate("2025-01-01").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0),
                new PurchaseOrders("C1", "O1", convertStringToDate("2025-01-05").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0),
                new PurchaseOrders("C1", "O1", convertStringToDate("2025-01-10").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0)));

        assertThat(purchaseOrderService.fetchOrderHistory("customerId", convertStringToDate("2025-01-01"),
                convertStringToDate("2025-01-05"), false)).isEqualTo(new ResponsePojo(ApplicationConstants.SUCCESS, "Details Fetched Successfully!", Arrays.asList(
                new PurchaseOrders("C1", "O1", convertStringToDate("2025-01-01").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0),
                new PurchaseOrders("C1", "O1", convertStringToDate("2025-01-05").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0))));
    }

    Date convertStringToDate(String date) throws ParseException {
        Date convertedDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        return convertedDate;
    }*/
}
