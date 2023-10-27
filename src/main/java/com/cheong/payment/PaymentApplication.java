package com.cheong.payment;

import co.omise.Serializer;
import com.cheong.domain.client.OrderClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.reactive.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@EnableFeignClients
@SpringBootApplication
public class PaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }


    @Bean
    public Serializer omiseObjectMapper(){
        return Serializer.defaultSerializer();
    }


//    @ServiceActivator(inputChannel = "order-event.errors")
//    public void handlePublishError(ErrorMessage errorMessage){
//        System.out.println(errorMessage);
//    }

//    @Bean
//    OrderClient orderClient(){
//        WebClient webClient = WebClient.builder()
//                .baseUrl("http://localhost:8080/api/v1/orders")
//                .build();
//
//        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
//                .builder(WebClientAdapter.forClient(webClient))
//                .build();
//        return httpServiceProxyFactory.createClient(OrderClient.class);
//    }
}
