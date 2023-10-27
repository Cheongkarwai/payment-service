package com.cheong.payment.controller;

import co.omise.ClientException;
import co.omise.models.OmiseException;
import com.cheong.domain.client.OrderClient;
import com.cheong.domain.order.dto.OrderDTO;
import com.cheong.payment.dto.ChargeDTO;
import com.cheong.payment.dto.ChargeResponse;
import com.cheong.payment.dto.PaymentProvider;
import com.cheong.payment.service.IChargeService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/charges")
public class ChargeController {
    private final Map<String,IChargeService> chargeServiceMap;

    public ChargeController(List<IChargeService> chargeServices){
        this.chargeServiceMap = chargeServices.stream()
                .collect(Collectors.toMap(IChargeService::getType, Function.identity()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ChargeResponse> charge(@Valid @RequestBody Mono<ChargeDTO> chargeDTOMono,
                                       @RequestParam("payment_provider") PaymentProvider paymentProvider){

        return chargeDTOMono.flatMap(chargeDTO-> {
                    try {
                        return chargeServiceMap.get(paymentProvider.name()).charge(chargeDTO);
                    } catch (IOException | ClientException | OmiseException e) {
                        return Mono.error(new RuntimeException(e));
                    }
                })
                .onErrorResume(e->{
                    e.printStackTrace();
                    return Mono.error(e);
                });

    }

    @PostMapping("/omise/webhook")
    public Mono<Void> getOmiseEvents(@RequestBody JsonNode omiseEventObject) throws IOException, OmiseException {
       return chargeServiceMap.get(PaymentProvider.OMISE.name()).captureEvents(omiseEventObject);
    }
}
