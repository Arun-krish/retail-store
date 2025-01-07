package com.retail.entity;

import com.retail.util.ApplicationConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("purchase_orders")
@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrders {
    public PurchaseOrders(String customerId, LocalDate orderDate, Double orderTotal) {
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.orderTotal = orderTotal;
    }

    @Id
    String id;
    @NotNull
    String customerId;
    @NotNull
    String orderId;
    @NotNull
    LocalDate orderDate;
    Double orderTotal = 0.0;
    Double totalRewards = 0.0;
    @CreatedDate
    LocalDate createdOn;
    String createdBy= ApplicationConstants.ADMIN_USER;
}
