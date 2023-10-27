package com.cheong.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChargeDTO {

    @NotNull(message = "Payer must not be null")
    private PayerDTO payer;

    @NotEmpty(message = "Items must not be empty")
    private List<ItemDTO> items;

    @Min(message = "Total amount must be greater than 0", value = 1)
    @JsonProperty("total_amount")
    private double totalAmount;

    private PaymentDTO payment;

}
