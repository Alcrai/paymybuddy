package com.paymybuddy.service;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.BankAccountRepository;
import lombok.experimental.StandardException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {
    @Mock
    private BankAccountRepository bankAccountRepository;

    private BankAccountService bankAccountService;

    private Optional<BankAccount> bankAccountOutside;
    private Optional<BankAccount> bankAccountInside;
    private Iterable<BankAccount> accountIterable;




    @BeforeEach
    void initTest(){
        bankAccountService= new BankAccountService(bankAccountRepository);
        BankAccount bankAccountOut = new BankAccount(1,1,"orange","iban1",false);
        bankAccountOutside = Optional.of(bankAccountOut);
        BankAccount bankAccountIn = new BankAccount(2,1,"a.benson@gmail.com","ibanInterne1",true);
        bankAccountInside = Optional.of(bankAccountIn);
        List<BankAccount> accounts = new ArrayList<>();
        accounts.add(bankAccountIn);
        accounts.add(bankAccountOut);
        accountIterable = accounts;

    }

    @Test
    void getInsideAccountReturnIdBankAccountInside(){

        int idSession=1;

        when(bankAccountRepository.findAllByUserId(idSession)).thenReturn(accountIterable);
        assertThat(bankAccountService.getIdInsideAccount(idSession)).isEqualTo(2);
        verify(bankAccountRepository).findAllByUserId(idSession);
    }

    @Test
    void isTransactionIsOutside(){
        Transaction transaction= new Transaction(1,1,2, 12.5f,"test");

        when(bankAccountRepository.findById(transaction.getAccountSendId())).thenReturn(bankAccountInside);
        when(bankAccountRepository.findById(transaction.getAccountReceiveId())).thenReturn(bankAccountOutside);
        assertThat(bankAccountService.isTransactionOutside(transaction)).isTrue();
        verify(bankAccountRepository).findById(transaction.getAccountSendId());
        verify(bankAccountRepository).findById(transaction.getAccountReceiveId());
    }

    @Test
    void withListUserReturnBankAccoountOfUser(){
        UserAccount userfriend = new UserAccount(1,"Alex","Benson","a.benson@gmail.com","password1");
        List<UserAccount> friendList = new ArrayList<>();
        friendList.add(userfriend);

        when(bankAccountRepository.findAllByUserId(1)).thenReturn(accountIterable);
        assertThat(bankAccountService.getBankAccountFiend(friendList)).asList();
        verify(bankAccountRepository).findAllByUserId(1);
    }

    @Test
    void updateBankAccountTest(){
        BankAccount bankAccount = bankAccountInside.get();
        when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);
        assertThat(bankAccountService.updateBankOutside(bankAccount)).isEqualTo(bankAccount);
        verify(bankAccountRepository).save(bankAccount);
    }

    @Test
    void getBankAccountfingByIdTest(){
        BankAccount bankAccount = bankAccountInside.get();
        when(bankAccountRepository.findById(1)).thenReturn(Optional.of(bankAccount));
        assertThat(bankAccountService.getBankAccountfindById(1)).isEqualTo(bankAccount);
        verify(bankAccountRepository).findById(1);
    }
    
    @Test
    void getBankaccountOutsideTest(){
        when(bankAccountRepository.findAllByUserId(1)).thenReturn(accountIterable);
        assertThat(bankAccountService.getBankAccountOutside(1)).isEqualTo(bankAccountOutside.get());
        verify(bankAccountRepository).findAllByUserId(1);
    }


}
