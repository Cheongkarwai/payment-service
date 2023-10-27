package com.cheong.payment.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PaymentDTO {

    @JsonProperty("payment_method")
    private PaymentMethod paymentMethod;;

    private String bankCode;

}
