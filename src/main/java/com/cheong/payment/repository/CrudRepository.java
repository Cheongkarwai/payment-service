package com.cheong.payment.repository;

import org.hibernate.reactive.stage.Stage;

import java.util.concurrent.CompletionStage;

public interface  CrudRepository<R,ID> {

    CompletionStage<R> findById(Stage.Session session, ID id);

    CompletionStage<Void> save(Stage.Session session,R data);


    CompletionStage<R> update(Stage.Session session, R data);
}
