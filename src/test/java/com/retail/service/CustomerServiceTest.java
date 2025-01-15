package com.retail.service;

import com.retail.entity.Customers;
import com.retail.repository.CustomerRepository;
import com.retail.util.ApplicationConstants;
import com.retail.util.ResponsePojo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @InjectMocks
    CustomerService mockCustomerService;

    @Mock
    CustomerRepository customerRepository;



    @Test
    void testCreateCustomer() throws Exception {
        Customers customers = new Customers("1234", "name", "987654321", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), ApplicationConstants.ADMIN_USER);
        when(customerRepository.save(Mockito.any(Customers.class))).thenReturn(customers);
        ResponsePojo response = mockCustomerService.saveCustomer(customers);
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(ApplicationConstants.SUCCESS, response.getStatus())
        );
    }
}
