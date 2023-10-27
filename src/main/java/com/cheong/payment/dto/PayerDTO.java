package com.cheong.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PayerDTO {

    @NotNull(message = "Name must not be null")
    private NameDTO name;

    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @NotNull(message = "Address must not be null")
    private AddressDTO address;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("email_address")
    private String emailAddress;

}
