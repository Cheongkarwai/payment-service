package com.cheong.payment.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix="omise")
public class OmiseProperties {

    private String secretKey;

    private String publicKey;
}
