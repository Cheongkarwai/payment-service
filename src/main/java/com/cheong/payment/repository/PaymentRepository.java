package com.cheong.payment.repository;

import com.cheong.domain.billing.Payment;
import jakarta.persistence.Persistence;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletionStage;

@Repository
public class PaymentRepository implements  IPaymentRepository{

    private Stage.SessionFactory sessionFactory;


    public PaymentRepository(){
        this.sessionFactory = Persistence.createEntityManagerFactory("postgres").unwrap(Stage.SessionFactory.class);
    }


    @Override
    public CompletionStage<Payment> findById(Stage.Session session, String id) {
        return session.find(Payment.class,id);
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, Payment object) {
        return session.persist(object);
    }

    @Override
    public CompletionStage<Payment> update(Stage.Session session, Payment data) {
        return session.merge(data);
    }
}
