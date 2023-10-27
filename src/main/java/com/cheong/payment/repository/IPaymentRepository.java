package com.cheong.payment.repository;


import com.cheong.domain.billing.Payment;
import org.hibernate.reactive.stage.Stage;

import java.util.concurrent.CompletionStage;

public interface IPaymentRepository extends  CrudRepository<Payment,String> {

}
