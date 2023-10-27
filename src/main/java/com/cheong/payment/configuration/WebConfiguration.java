package com.cheong.payment.configuration;


import co.omise.Client;
import co.omise.ClientException;
import com.cheong.domain.client.OrderClient;
import com.cheong.payment.dto.OmiseProperties;
import jakarta.persistence.Persistence;
import org.hibernate.reactive.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Arrays;

@Configuration
public class WebConfiguration {

    @Autowired
    private Environment environment;

    @Autowired
    private OmiseProperties omiseProperties;

    @Bean
    public OrderClient orderClient(){

        System.out.println(environment.getProperty("hello"));

        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/api/v1")
                .build();

        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .build();
        return httpServiceProxyFactory.createClient(OrderClient .class);
    }

    @Bean
    public Client client() throws ClientException {
        return new Client.Builder()
                .publicKey(omiseProperties.getPublicKey())
                .secretKey(omiseProperties.getSecretKey())
                .build();
    }

    @Bean
    public Stage.SessionFactory sessionFactory(){
        return Persistence.createEntityManagerFactory("postgres")
                .unwrap(Stage.SessionFactory.class);
    }


}
