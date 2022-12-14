package com.paymybuddy.repository;

import com.paymybuddy.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction,Integer> {
    Iterable<Transaction> getTransactionByAccountSendId(int id);

    Page<Transaction> getTransationByAccountReceiveIdOrAccountReceiveIdOrderByTransactionIdDesc(int id, int id1, Pageable pageable);

    List<Transaction> getTransationByAccountReceiveIdOrAccountReceiveId(int id, int id1);
}
