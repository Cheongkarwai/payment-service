package com.cheong.payment.service;

import com.cheong.payment.dto.ChargeDTO;
import com.cheong.payment.dto.ChargeResponse;
import com.cheong.payment.repository.IPaymentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.Persistence;
import org.hibernate.reactive.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service("PAYPAL")
public class PaypalChargeService implements IChargeService{

    public final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    private final Stage.SessionFactory sessionFactory;

    private final IPaymentRepository paymentRepository;


    public PaypalChargeService(IPaymentRepository paymentRepository, Stage.SessionFactory sessionFactory){
        this.paymentRepository = paymentRepository;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Mono<ChargeResponse> charge(ChargeDTO chargeDTO) {
//        return Mono.fromCompletionStage(
//                sessionFactory.withSession(session ->
//                        paymentRepository.findById(session,1L)
//                                .thenAccept(e-> {
//                                    if(e.getStatus().equals(PaymentStatus.APPROVED)){
//                                        streamBridge.send("consumePayment-in-0",e.getId());
//                                    }
//                                })));

        return Mono.empty();

    }

    public String getType(){
        return "PAYPAL";
    }

    @Override
    public Mono<Void> captureEvents(JsonNode response) {

        return Mono.empty();
    }
}
