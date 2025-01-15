package com.retail.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.retail.entity.Customers;
import com.retail.service.CustomerService;
import com.retail.util.ApplicationConstants;
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
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTests {

    @InjectMocks
    private CustomerController controller;

    @Mock
    private CustomerService customerService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }


    @Test
    public void testCreateCustomer() throws Exception {
        Customers customers = new Customers("1234", "name", "9876543210", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), ApplicationConstants.ADMIN_USER);
        when(customerService.saveCustomer(any(Customers.class))).thenReturn(new ResponsePojo(ApplicationConstants.SUCCESS, "Customer Saved Successfully!", customers));
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/customer/saveCustomer")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customers))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ApplicationConstants.SUCCESS));

    }

    ObjectMapper mapper;


    @Test
    public void testCreateCustomer_InputValidationScenarios() throws Exception {
        // Scenario 1 - null Name values
        Customers customers = new Customers("1234", null, "9876543210", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), ApplicationConstants.ADMIN_USER);
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/customer/saveCustomer")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customers))
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) result.getResolvedException();

        assertNotNull(methodArgumentNotValidException);
        assertNotNull(methodArgumentNotValidException.getFieldErrors());
        assertEquals("name", methodArgumentNotValidException.getFieldErrors().get(0).getField());
        assertEquals("must not be null", methodArgumentNotValidException.getFieldErrors().get(0).getDefaultMessage());


        // Scenario 2 - Invalid Mobile number
        customers = new Customers("1234", "test", "987654321", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), ApplicationConstants.ADMIN_USER);
        request = MockMvcRequestBuilders
                .post("/api/customer/saveCustomer")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customers))
                .contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        methodArgumentNotValidException = (MethodArgumentNotValidException) result.getResolvedException();
        assertNotNull(methodArgumentNotValidException);
        assertNotNull(methodArgumentNotValidException.getFieldErrors());
        assertEquals("mobile", methodArgumentNotValidException.getFieldErrors().get(0).getField());
        assertEquals("Mobile number should be between 10-15 numbers", methodArgumentNotValidException.getFieldErrors().get(0).getDefaultMessage());
    }
}
