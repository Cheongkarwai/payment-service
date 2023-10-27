package com.cheong.payment.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTO {

    private String name;

    private int quantity;

    private int amount;
}
