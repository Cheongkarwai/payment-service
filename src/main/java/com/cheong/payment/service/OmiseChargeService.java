package com.cheong.payment.service;

import co.omise.Client;
import co.omise.ClientException;
import co.omise.Serializer;
import co.omise.models.*;
import co.omise.requests.Request;
import com.cheong.domain.billing.Payment;
import com.cheong.domain.client.OrderClient;
import com.cheong.domain.order.Order;
import com.cheong.domain.order.dto.ItemDTO;
import com.cheong.domain.order.dto.OrderDTO;
import com.cheong.payment.dto.*;
import com.cheong.payment.repository.IPaymentRepository;
import com.cheong.payment.repository.PaymentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.reactive.stage.Stage;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("OMISE")
public class OmiseChargeService implements IChargeService{


    private final ObjectMapper objectMapper;

    private final Serializer omiseObjectMapper;


    private final Stage.SessionFactory sessionFactory;
    private final IPaymentRepository paymentRepository;

    private final OrderClient orderClient;

    private final Client omiseClient;

    public OmiseChargeService(ObjectMapper objectMapper, Serializer omiseObjectMapper, Stage.SessionFactory sessionFactory,
                              PaymentRepository paymentRepository,OrderClient orderClient, Client omiseClient){
        this.objectMapper = objectMapper;
        this.omiseObjectMapper = omiseObjectMapper;
        this.sessionFactory = sessionFactory;
        this.paymentRepository = paymentRepository;
        this.orderClient = orderClient;
        this.omiseClient = omiseClient;
    }

    @Override
    public Mono<ChargeResponse> charge(ChargeDTO chargeDTO) throws IOException, ClientException, OmiseException {


        return switch(chargeDTO.getPayment().getPaymentMethod()){

            case TNG-> {

                //Create new source for payment
                Request<Source> sourceRequest = new OmiseSource.OmiseCreateRequestBuilder()
                        .addItems(chargeDTO.getItems().parallelStream()
                                .map(itemDto->{
                                    Item item = new Item();
                                    item.name = itemDto.getName();
                                    item.quantity = itemDto.getQuantity();
                                    item.amount = itemDto.getAmount();
                                    return item;
                                })
                                .collect(Collectors.toList()))
                        .type(SourceType.TouchNGo)
                        .platformType(PlatformType.Web)
                        .currency(Currency.MYR.name())
                        .amount(Double.valueOf(chargeDTO.getTotalAmount()).longValue())
                        .mobileNumber(chargeDTO.getPayer().getPhoneNumber())
                        .email(chargeDTO.getPayer().getEmailAddress())
                        .name(chargeDTO.getPayer().getName().toString())
                        .build();


                Source source = omiseClient.sendRequest(sourceRequest);
                //Create new charge with the source given.
                Request<Charge> chargeRequest = new Charge.CreateRequestBuilder()
                        .amount(source.getAmount())
                        .currency(source.getCurrency())
                        .source(source.getId())
                        .returnUri("http://localhost:4200")
                        .expiresAt(DateTime.now().plusMinutes(1))
                        .build();

                Charge charge = omiseClient.sendRequest(chargeRequest);

                yield Mono.just(ChargeResponse.builder().authorizeUri(charge.getAuthorizeUri()).build());

            }

            case DuitNowOBW -> {
                Request<Source> sourceRequest = new Source.CreateRequestBuilder()
                        .type(SourceType.DuitNowOBW)
                        .platformType(PlatformType.Web)
                        //.currency(Currency.MYR.name())
                        .amount(Double.valueOf(chargeDTO.getTotalAmount()).longValue())
                        .mobileNumber(chargeDTO.getPayer().getPhoneNumber())
                        .email(chargeDTO.getPayer().getEmailAddress())
                        .name(chargeDTO.getPayer().getName().toString())
                        .bank(chargeDTO.getPayment().getBankCode())
                        .build();

                Source source = omiseClient.sendRequest(sourceRequest);
                //Create new charge with the source given.
                Request<Charge> chargeRequest = new Charge.CreateRequestBuilder()
                        .amount(source.getAmount())
                        .currency(source.getCurrency())
                        .source(source.getId())
                        .returnUri("http://localhost:4200")
                        .expiresAt(DateTime.now().plusMinutes(1))
                        .build();

                Charge charge = omiseClient.sendRequest(chargeRequest);

                yield Mono.just(ChargeResponse.builder().authorizeUri(charge.getAuthorizeUri()).build());
            }
            case CREDIT_CARD -> {

                //Create new charge with the source given.
                Request<Charge> chargeRequest = new Charge.CreateRequestBuilder()
                        .amount(Double.valueOf(chargeDTO.getTotalAmount()).longValue())
                        //.email(chargeDTO.getPayer().getEmailAddress())
                        //.name(chargeDTO.getPayer().getName().toString())
                        //.card(order.getPayer().getCreditCard())
                        .returnUri("http://localhost:4200")
                        .expiresAt(DateTime.now().plusMinutes(1))
                        .build();

                Charge charge = omiseClient.sendRequest(chargeRequest);

                yield Mono.just(ChargeResponse.builder().authorizeUri(charge.getAuthorizeUri()).build());
            }
        };

    }

    public Mono<List<BankCode>> findAllAvailableBank(SourceType sourceType) {

        return switch(sourceType){

            case Unknown -> null;
            case Alipay -> null;
            case BarcodeAlipay -> null;
            case BarcodeWechat -> null;
            case BillPaymentTescoLotus -> null;
            case Econtext -> null;
            case Fpx -> null;
            case InstallmentBay -> null;
            case InstallmentBbl -> null;
            case InstallmentFirstChoice -> null;
            case InstallmentKbank -> null;
            case InstallmentKtc -> null;
            case InstallmentScb -> null;
            case InstallmentUob -> null;
            case InstallmentTtb -> null;
            case InternetBankingBay -> null;
            case InternetBankingBbl -> null;
            case InternetBankingKtb -> null;
            case InternetBankingScb -> null;
            case MobileBankingBay -> null;
            case MobileBankingBbl -> null;
            case MobileBankingKbank -> null;
            case MobileBankingOcbcPao -> null;
            case MobileBankingScb -> null;
            case MobileBankingKtb -> null;
            case Paynow -> null;
            case PointsCiti -> null;
            case PromptPay -> null;
            case RabbitLinepay -> null;
            case TrueMoney -> null;
            case AlipayCN -> null;
            case AlipayHK -> null;
            case DANA -> null;
            case GCash -> null;
            case KakaoPay -> null;
            case TouchNGo -> null;
            case Atome -> null;
            case DuitNowOBW ->
                    Mono.just(List.of(
                            BankCode.builder().code("affin").name("Affin Bank").build(),
                            BankCode.builder().code("alliance").name("Alliance Bank").build(),
                            BankCode.builder().code("agro").name("AGRONet").build(),
                            BankCode.builder().code("ambank").name("Am Bank").build(),
                            BankCode.builder().code("islam").name("Bank Islam").build(),
                            BankCode.builder().code("muamalat").name("Bank Muamalat").build(),
                            BankCode.builder().code("rakyat").name("Bank Rakyat").build(),
                            BankCode.builder().code("bsn").name("BSN").build(),
                            BankCode.builder().code("cimb").name("CIMB Clicks").build(),
                            BankCode.builder().code("hongleong").name("Hong Leong").build(),
                            BankCode.builder().code("kfh").name("KFH").build(),
                            BankCode.builder().code("maybank2u").name("Maybank2U").build(),
                            BankCode.builder().code("ocbc").name("OCBC Bank").build(),
                            BankCode.builder().code("public").name("Public Bank").build(),
                            BankCode.builder().code("rhb").name("RHB Bank").build(),
                            BankCode.builder().code("sc").name("Standard Chartered").build(),
                            BankCode.builder().code("uob").name("UOB Bank").build()
                    ));

        };
    }
    @Override
    public String getType() {
        return "OMISE";
    }

    @Override
    public Mono<Void> captureEvents(JsonNode response) throws IOException, OmiseException {
        return switch(response.path("key").asText()){
            case "charge.create"->{
                byte [] sourceBytes = objectMapper.writer().writeValueAsBytes(response.path("data").path("source"));
                Source source = omiseObjectMapper.deserialize(new ByteArrayInputStream(sourceBytes),Source.class);
                yield saveCharge(source);
            }
            case "charge.complete"->{
                //Getting charge by id to get the real charge status
                String chargeId = response.path("data").path("id").asText();
                Request<Charge> request = new Charge.GetRequestBuilder(chargeId).build();
                Charge charge = omiseClient.sendRequest(request);

//                byte [] sourceBytes = objectMapper.writeValueAsBytes(response.path("data").path("source"));
//                Source source = omiseObjectMapper.deserialize(new ByteArrayInputStream(sourceBytes),Source.class);
//                source.setChargeStatus(charge.getStatus());
                List<Item> items = objectMapper.readValue(objectMapper.writeValueAsString(response.path("data").path("source").path("items")), new TypeReference<>() {
                });

                yield updateCharge(charge)
                        .flatMap(payment->{
                            if(payment.getStatus().equals(ChargeStatus.Successful)){
                                OrderDTO orderDTO = OrderDTO.builder()
                                        .paymentId(payment.getId())
                                        .items(items.parallelStream().map(item-> ItemDTO.builder()
                                                .price(item.amount)
                                                .name(item.name)
                                                .quantity(item.quantity)
                                                .build())
                                                .collect(Collectors.toList()))
                                        .build();
                                return orderClient.save(orderDTO);
                            }
                            return Mono.empty();
                        });
            }
            default -> throw new IllegalStateException("Unexpected value: " + response.path("key").asText());
        };
    }


    private Mono<Payment> updateCharge(Charge charge){
        return Mono.fromCompletionStage(
                sessionFactory.withTransaction((session, transaction) -> paymentRepository.findById(session,charge.getSource().getId())
                        .thenCompose(payment -> {
                            payment.setStatus(charge.getSource().getChargeStatus());
                            return paymentRepository.update(session,payment);
                        }))
        );
    }
    private Mono<Void> saveCharge(Source source){
        return Mono.fromCompletionStage(
                sessionFactory.withTransaction((session, transaction) -> paymentRepository.save(session,Payment.builder()
                        .id(source.getId()).amount(source.getAmount())
                        .paymentMethod(source.getType()).currency(source.getCurrency())
                        .status(source.getChargeStatus())
                        .build())));

    }
}
