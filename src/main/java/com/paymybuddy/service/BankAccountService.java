package com.paymybuddy.service;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BankAccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public int getIdInsideAccount(int idSession) {
        final int[] result = {0};
        Iterable<BankAccount> accounts = bankAccountRepository.findAllByUserId(idSession);
        accounts.forEach(a-> {
            if(a.getInside()){ result[0] =a.getBankAccountId();}
        });
        return result[0];
    }

    public boolean isTransactionOutside(Transaction transaction) {
        Boolean result = true;
        Optional<BankAccount> accountSend = bankAccountRepository.findById(transaction.getAccountSendId());
        Optional<BankAccount> accountReceive = bankAccountRepository.findById(transaction.getAccountReceiveId());
        if (accountSend.get().getInside() && accountReceive.get().getInside()){
            result= false;
        }else {
            result=true;
        }
        return result;
    }

    public List<BankAccount> getBankAccountFiend(List<UserAccount> friends) {
        List<BankAccount> result =new ArrayList<>();
        friends.forEach(f->searchBankAccountbyIdUser(f.getUserId(),true).forEach(e->result.add(e)));
        return result;
    }

    private List<BankAccount> searchBankAccountbyIdUser(int idUser, boolean valeur){
        List<BankAccount> results = new ArrayList<>();
        Iterable<BankAccount> UserBankAccountList = bankAccountRepository.findAllByUserId(idUser);
        UserBankAccountList.forEach(e->{
            if (e.getInside()==valeur){
                results.add(e);
            }
        });
        return results;
    }


    public BankAccount updateBankOutside(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    public BankAccount getBankAccountfindById(int accountReceiveId) {
        return bankAccountRepository.findById(accountReceiveId).get();
    }

    public BankAccount getBankAccountOutside(int idSession) {
        Iterable<BankAccount> bankAccounts = bankAccountRepository.findAllByUserId(idSession);
        final BankAccount[] result = {new BankAccount()};
        bankAccounts.forEach(ba->{
            if(!ba.getInside()){
                result[0] =ba;
            }
        });
        return result[0];
    }
}
