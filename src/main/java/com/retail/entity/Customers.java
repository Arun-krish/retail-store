package com.retail.entity;

import com.retail.util.ApplicationConstants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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
    @Pattern(regexp = "^[0-9]+$", message = "Only digits allowed in mobile number")
    @Size(min = 10,max = 10, message = "Mobile number should be between 10-15 numbers")
    String mobile;
    @CreatedDate
    LocalDate createdOn;

    String createdBy = ApplicationConstants.ADMIN_USER;


}
