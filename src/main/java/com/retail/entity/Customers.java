package com.retail.entity;

import com.retail.util.ApplicationConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("customers")
@Getter
@Setter
@AllArgsConstructor
public class Customers {

    @Id
    String id;
    @NotNull
    String name;
    @NotNull
    String mobile;
    LocalDate createdOn;
    String createdBy= ApplicationConstants.ADMIN_USER;


}
