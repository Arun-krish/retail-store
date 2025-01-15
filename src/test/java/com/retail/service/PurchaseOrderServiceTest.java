package com.retail.service;

import com.retail.entity.Customers;
import com.retail.entity.PurchaseOrders;
import com.retail.repository.CustomerRepository;
import com.retail.repository.PurchaseOrderRepository;
import com.retail.util.ApplicationConstants;
import com.retail.util.ResponsePojo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.util.*;

import static com.retail.util.DateUtils.convertStringToDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PurchaseOrderServiceTest {

    @InjectMocks
    PurchaseOrderService purchaseOrderService;

    @Mock
    PurchaseOrderRepository purchaseOrderRepository;

    @Mock
    CustomerRepository customerRepository;

    @Test
    void testCreatePurchaseOrderWithRewardPointsCalculationScenario1() throws Exception {
        PurchaseOrders orders = new PurchaseOrders("customerId", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0);
        Customers customers = new Customers("1234", "name", "987654321", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), ApplicationConstants.ADMIN_USER);
        when(customerRepository.findById(any())).thenReturn(Optional.of(customers));
        when(purchaseOrderRepository.save(Mockito.any(PurchaseOrders.class))).thenReturn(orders);


        //Scenario 1 - Order Total is 120 hence reward should be 90
        ResponsePojo response = purchaseOrderService.savePurchaseOrder(orders);
        PurchaseOrders purchaseOrders = (PurchaseOrders) response.getData();
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(ApplicationConstants.SUCCESS, response.getStatus()),
                () -> assertEquals(90.0, purchaseOrders.getTotalRewards())
        );


    }

    @Test
    void testCreatePurchaseOrderWithRewardPointsCalculationScenario2() throws Exception {
        PurchaseOrders orders = new PurchaseOrders("customerId", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 100.0);
        Customers customers = new Customers("1234", "name", "987654321", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), ApplicationConstants.ADMIN_USER);
        when(customerRepository.findById(any())).thenReturn(Optional.of(customers));
        when(purchaseOrderRepository.save(Mockito.any(PurchaseOrders.class))).thenReturn(orders);
        //Scenario 2 - Order Total is 100 hence reward should be 50
        ResponsePojo response = purchaseOrderService.savePurchaseOrder(orders);
        PurchaseOrders purchaseOrders = (PurchaseOrders) response.getData();
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(ApplicationConstants.SUCCESS, response.getStatus()),
                () -> assertEquals(50.0, purchaseOrders.getTotalRewards())
        );
    }

    @Test
    void testCreatePurchaseOrderWithRewardPointsCalculationScenario3() throws Exception {
        PurchaseOrders orders = new PurchaseOrders("customerId", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 75.0);
        Customers customers = new Customers("1234", "name", "987654321", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), ApplicationConstants.ADMIN_USER);
        when(customerRepository.findById(any())).thenReturn(Optional.of(customers));
        when(purchaseOrderRepository.save(Mockito.any(PurchaseOrders.class))).thenReturn(orders);
        //Scenario 3 - Order Total is 75 hence reward should be 25
        ResponsePojo response = purchaseOrderService.savePurchaseOrder(orders);
        PurchaseOrders purchaseOrders = (PurchaseOrders) response.getData();
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(ApplicationConstants.SUCCESS, response.getStatus()),
                () -> assertEquals(25.0, purchaseOrders.getTotalRewards())
        );
    }

    @Test
    void testCreatePurchaseOrderWithRewardPointsCalculationScenario4() throws Exception {
        PurchaseOrders orders = new PurchaseOrders("customerId", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 50.0);
        Customers customers = new Customers("1234", "name", "987654321", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), ApplicationConstants.ADMIN_USER);
        when(customerRepository.findById(any())).thenReturn(Optional.of(customers));
        when(purchaseOrderRepository.save(Mockito.any(PurchaseOrders.class))).thenReturn(orders);
        //Scenario 4 - Order Total is 50 hence reward should be 0
        ResponsePojo response = purchaseOrderService.savePurchaseOrder(orders);
        PurchaseOrders purchaseOrders = (PurchaseOrders) response.getData();
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(ApplicationConstants.SUCCESS, response.getStatus()),
                () -> assertEquals(0.0, purchaseOrders.getTotalRewards())
        );
    }

    @Test
    void testBulkProcessPurchaseOrders() throws Exception {
        PurchaseOrders orders = new PurchaseOrders("customerId", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 50.0);
        when(customerRepository.findById(any())).thenReturn(Optional.of(new Customers("1234", "name", "987654321", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), ApplicationConstants.ADMIN_USER)));
        when(purchaseOrderRepository.save(Mockito.any(PurchaseOrders.class))).thenReturn(orders);

        ResponsePojo response = purchaseOrderService.bulkProcessPurchaseOrders();
        List<PurchaseOrders> purchaseOrders = (List<PurchaseOrders>) response.getData();
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(ApplicationConstants.SUCCESS, response.getStatus()),
                ()-> assertEquals(5,purchaseOrders.size())
        );
    }


    @Test
    void testOrderFetchHistory() throws Exception {

        when(customerRepository.findById(any())).thenReturn(Optional.of(new Customers("1234", "name", "987654321", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), ApplicationConstants.ADMIN_USER)));
        when(purchaseOrderRepository.findByCustomerIdAndOrderDateBetween(any(), any(),
                any())).thenReturn(Arrays.asList(
                new PurchaseOrders("C1", convertStringToDate("2025-01-01").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0),
                new PurchaseOrders("C1", convertStringToDate("2025-01-05").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0)));

        when(purchaseOrderRepository.findByCustomerIdAndOrderDateGreaterThanEqual(any(), any())).thenReturn(Arrays.asList(
                new PurchaseOrders("C1", convertStringToDate("2025-01-01").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0),
                new PurchaseOrders("C1", convertStringToDate("2025-01-05").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0),
                new PurchaseOrders("C1", convertStringToDate("2024-11-01").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0)));


        ResponsePojo response = purchaseOrderService.fetchOrderHistory(any(), convertStringToDate("2025-01-01"), convertStringToDate("2025-01-05"), false);
        Map<String, Object> responseMap = (Map<String, Object>) response.getData();

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(ApplicationConstants.SUCCESS, response.getStatus()),
                () -> assertEquals(2, responseMap.get(ApplicationConstants.TOTAL_ORDERS)));


        ResponsePojo responseScenario2 = purchaseOrderService.fetchOrderHistory(any(), null, null, true);
        Map<String, Object> responseMapScenario2 = (Map<String, Object>) responseScenario2.getData();
        assertAll(
                () -> assertNotNull(responseScenario2),
                () -> assertEquals(ApplicationConstants.SUCCESS, responseScenario2.getStatus()),
                () -> assertEquals(3, responseMapScenario2.get(ApplicationConstants.TOTAL_ORDERS)));
    }

}
