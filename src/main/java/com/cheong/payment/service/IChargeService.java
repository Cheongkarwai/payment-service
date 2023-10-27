package com.cheong.payment.service;

import co.omise.ClientException;
import co.omise.models.OmiseException;
import com.cheong.payment.dto.ChargeDTO;
import com.cheong.payment.dto.ChargeResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;

public interface IChargeService {

    Mono<ChargeResponse> charge(ChargeDTO chargeDTO) throws IOException, ClientException, OmiseException;

    String getType();

    Mono<Void> captureEvents(JsonNode response) throws IOException, OmiseException;

}
