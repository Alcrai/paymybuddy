package com.paymybuddy.service;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.BankAccountRepository;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private BankAccountRepository bankAccountRepository;
    private TransferService transferService;
    private Optional<UserAccount> userAccountOptional;
    private List<Transaction> transactionList=new ArrayList<>();
    private Transaction tOutside;
    private Transaction tInside;

    @BeforeEach
    void initTest(){
        transferService = new TransferService(transactionRepository,userAccountRepository,bankAccountRepository);
        BankAccount bankAccountOut = new BankAccount(1,1,"orange","iban1",false);
        BankAccount bankAccountIn = new BankAccount(2,1,"a.benson@gmail.com","ibanInterne1",true);
        List<BankAccount> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccountIn);
        bankAccountList.add(bankAccountOut);
        UserAccount user = new UserAccount(1,"Alex","Benson","a.benson@gmail.com","password1",bankAccountList);
        userAccountOptional = Optional.of(user);
        tOutside= new Transaction(1,1,2, 120,"test");
        Transaction t2= new Transaction(1,2,1, -10,"test");
        tInside= new Transaction(1,2,3, -10,"test");
        transactionList.add(tOutside);
        transactionList.add(t2);
        transactionList.add(tInside);

    }

  /*  @Test
    void getTransactionTabReturnPageTransaction(){
        Pageable pageable = PageRequest.of(1,3);
        Page<Transaction> transactionPage = new PageImpl<>(transactionList,pageable,0);
        when(userAccountRepository.findById(1)).thenReturn(userAccountOptional);
        when(transactionRepository.getTransationByAccountReceiveIdOrAccountReceiveIdOrderByTransactionIdDesc(1,2,pageable)).thenReturn(transactionPage);
        assertThat(transferService.getTransactionTab(1,pageable)).isNotEmpty();
        verify(userAccountRepository).findById(1);
        verify(transactionRepository).getTransationByAccountReceiveIdOrAccountReceiveIdOrderByTransactionIdDesc(1,2,pageable);
    }*/

    @Test
    void getBalanceReturnBalance(){
        when(userAccountRepository.findById(1)).thenReturn(userAccountOptional);
        when(transactionRepository.getTransationByAccountReceiveIdOrAccountReceiveId(1,2)).thenReturn(transactionList);
        assertThat(transferService.getBalance(1)).isEqualTo(100);
        verify(userAccountRepository).findById(1);
        verify(transactionRepository).getTransationByAccountReceiveIdOrAccountReceiveId(1,2);
    }

    @Test
    void saveTransactionTest() {
        when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(tInside);
        assertThat(transferService.saveTransaction(tInside)).isInstanceOf(Transaction.class);
        assertThat(transferService.saveTransationExterne(tInside)).isInstanceOf(Transaction.class);
        verify(transactionRepository, times(5)).save(Mockito.any(Transaction.class));
    }

}
