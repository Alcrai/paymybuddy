package com.paymybuddy.service;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.BankAccountRepository;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class TransferService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;

    public TransferService(TransactionRepository transactionRepository, UserAccountRepository userAccountRepository, BankAccountRepository bankAccountRepository) {
        this.transactionRepository = transactionRepository;
        this.userAccountRepository = userAccountRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    public Page<Transaction> getTransactionTab(int idSession, Pageable pageable) {
        Page<Transaction> transactionList= getTransactionReceiveByUserList(idSession,pageable);
        return transactionList;

    }

    public float getBalance(int idSession) {
        final float[] solde = {0};
        Optional<UserAccount> userInSession = userAccountRepository.findById(idSession);
        List<BankAccount> accounts = userInSession.get().getBankAccounts();
        final int[] idAccountInside = new int[1];
        final int[] idAccountOutside = new int[1];
        accounts.forEach(a->{
            if(a.getInside()){
                idAccountInside[0] =a.getBankAccountId();
            }else {
                idAccountOutside[0] =a.getBankAccountId();}
        });
        List<Transaction> transactionList=transactionRepository.getTransationByAccountReceiveIdOrAccountReceiveId(idAccountOutside[0],idAccountInside[0]);

        transactionList.forEach(t-> solde[0] +=t.getAmount());
        return solde[0];
    }

    public Transaction saveTransaction(Transaction transaction) {
        Transaction transactionInverse= new Transaction();
        transactionInverse.setAccountSendId(transaction.getAccountReceiveId());
        transactionInverse.setAccountReceiveId(transaction.getAccountSendId());
        transactionInverse.setAmount(-transaction.getAmount());
        transactionInverse.setComment(transaction.getComment());
        transactionRepository.save(transactionInverse);
        float commission = (float)(transaction.getAmount()*0.005);
        Transaction tcommission = new Transaction();
        tcommission.setAccountSendId(transaction.getAccountSendId());
        tcommission.setAccountReceiveId(11);
        tcommission.setAmount(commission);
        tcommission.setComment("commission");
        transactionRepository.save(tcommission);
        transaction.setAmount(transaction.getAmount()-commission);
        transactionRepository.save(transaction);
        return transaction;
    }

    public Transaction saveTransationExterne(Transaction transaction) {
        float commission = (float)(transaction.getAmount()*0.005);
        Transaction tcommission = new Transaction();
        tcommission.setAccountSendId(transaction.getAccountSendId());
        tcommission.setAccountReceiveId(11);
        if(commission<0){
            commission=-commission;
            transaction.setAmount(transaction.getAmount()+commission);
        } else {
            transaction.setAmount(transaction.getAmount()-commission);
        }
        tcommission.setAmount(commission);
        tcommission.setComment("commission");
        transactionRepository.save(tcommission);
        return transactionRepository.save(transaction);
    }

    @Transactional
    private Page<Transaction> getTransactionReceiveByUserList(int idSession, Pageable pageable){
        Optional<UserAccount> userInSession = userAccountRepository.findById(idSession);
        List<BankAccount> accounts = userInSession.get().getBankAccounts();
        final int[] idAccountInside = new int[1];
        final int[] idAccountOutside = new int[1];
        accounts.forEach(a->{
            if(a.getInside()){
                idAccountInside[0] =a.getBankAccountId();
            }else {
                idAccountOutside[0] =a.getBankAccountId();}

        });
        return transactionRepository.getTransationByAccountReceiveIdOrAccountReceiveIdOrderByTransactionIdDesc(idAccountInside[0],idAccountOutside[0],pageable);
    }
}
