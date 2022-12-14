package com.paymybuddy.repository;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount,Integer> {
    Optional<BankAccount> findByUserId(int accountReceiveId);

    Iterable<BankAccount> findAllByUserId(int idSession);
}
