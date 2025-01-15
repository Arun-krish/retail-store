package com.retail.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.retail.entity.PurchaseOrders;
import com.retail.service.PurchaseOrderService;
import com.retail.util.ApplicationConstants;
import com.retail.util.DateUtils;
import com.retail.util.ResponsePojo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PurchaseOrderControllerTests {
    @InjectMocks
    private PurchaseOrderController controller;

    @Mock
    private PurchaseOrderService purchaseOrderService;

    private MockMvc mockMvc;

    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testCreatePurchaseOrder() throws Exception {
        PurchaseOrders purchaseOrders = new PurchaseOrders("customerId", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0);
        when(purchaseOrderService.savePurchaseOrder(any(PurchaseOrders.class))).thenReturn(new ResponsePojo(ApplicationConstants.SUCCESS, "Purchase Order Saved!", purchaseOrders));

        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/purchaseOrder/savePurchaseOrder")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(purchaseOrders))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ApplicationConstants.SUCCESS));

    }

    @Test
    public void testCreatePurchaseOrder_InputValidationScenarios() throws Exception {

        // Scenario 1 - null customer Id
        PurchaseOrders purchaseOrders = new PurchaseOrders(null, new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0);
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/purchaseOrder/savePurchaseOrder")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(purchaseOrders))
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) result.getResolvedException();

        assertNotNull(methodArgumentNotValidException);
        assertNotNull(methodArgumentNotValidException.getFieldErrors());
        assertEquals("customerId", methodArgumentNotValidException.getFieldErrors().get(0).getField());
        assertEquals("must not be null", methodArgumentNotValidException.getFieldErrors().get(0).getDefaultMessage());

        // Scenario 2 - null Order Date
        purchaseOrders = new PurchaseOrders("test", null, 120.0);
        request = MockMvcRequestBuilders
                .post("/api/purchaseOrder/savePurchaseOrder")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(purchaseOrders))
                .contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        methodArgumentNotValidException = (MethodArgumentNotValidException) result.getResolvedException();

        assertNotNull(methodArgumentNotValidException);
        assertNotNull(methodArgumentNotValidException.getFieldErrors());
        assertEquals("orderDate", methodArgumentNotValidException.getFieldErrors().get(0).getField());
        assertEquals("must not be null", methodArgumentNotValidException.getFieldErrors().get(0).getDefaultMessage());

        // Scenario 3 - null Order Value
        purchaseOrders = new PurchaseOrders("test", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), null);
        request = MockMvcRequestBuilders
                .post("/api/purchaseOrder/savePurchaseOrder")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(purchaseOrders))
                .contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        methodArgumentNotValidException = (MethodArgumentNotValidException) result.getResolvedException();

        assertNotNull(methodArgumentNotValidException);
        assertNotNull(methodArgumentNotValidException.getFieldErrors());

        assertEquals("orderTotal", methodArgumentNotValidException.getFieldErrors().get(0).getField());
        assertEquals("must not be null", methodArgumentNotValidException.getFieldErrors().get(0).getDefaultMessage());


    }

    @Test
    public void testBulkProcessPurchaseOrders() throws Exception {
        List<PurchaseOrders> purchaseOrdersList = Arrays.asList(
                new PurchaseOrders("C1", DateUtils.convertStringToDate("2025-01-01").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0),
                new PurchaseOrders("C1", DateUtils.convertStringToDate("2025-01-05").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0));

        when(purchaseOrderService.bulkProcessPurchaseOrders()).thenReturn(new ResponsePojo(ApplicationConstants.SUCCESS, "Purchase Order Saved!", purchaseOrdersList));

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/purchaseOrder/bulkProcessPurchaseOrders");
        mockMvc.perform(request).andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ApplicationConstants.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(2));

    }

    @Test
    public void testFetchOrderHistory() throws Exception {
        List<PurchaseOrders> purchaseOrdersList = Arrays.asList(
                new PurchaseOrders("C1", DateUtils.convertStringToDate("2025-01-01").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0),
                new PurchaseOrders("C1", DateUtils.convertStringToDate("2025-01-05").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0));
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put(ApplicationConstants.CUSTOMER_NAME, "test");
        responseMap.put(ApplicationConstants.CUSTOMER_MOBILE, "9876543210");
        responseMap.put(ApplicationConstants.TOTAL_ORDERS, purchaseOrdersList.size());
        responseMap.put(ApplicationConstants.TOTAL_ORDERS_VALUE, 240);
        responseMap.put(ApplicationConstants.TOTAL_REWARD_POINTS, 180);
        responseMap.put(ApplicationConstants.ORDERS, purchaseOrdersList);
        when(purchaseOrderService.fetchOrderHistory("customerId",null,
                null,false)).thenReturn(new ResponsePojo(ApplicationConstants.SUCCESS, "Details Fetched Successfully!", responseMap));

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/purchaseOrder/fetchOrderHistory").header("customerId","customerId");
         mockMvc.perform(request).andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ApplicationConstants.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.Orders.length()").value(2));

    }


}
