package com.paymybuddy.service;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserAccountServiceTest {
    @Mock
    private UserAccountRepository userAccountRepository;
    private UserAccountService userAccountService;
    private UserAccount userAccount;
    private Iterable<UserAccount> userAccountIterable;

    @BeforeEach
    void initTest(){
        userAccountService = new UserAccountService(userAccountRepository);
        UserAccount user2 = new UserAccount(2,"Sissi","Cattoz","s.cattoz@gmail.com","password1");
        UserAccount user3 = new UserAccount(3,"thomas","Cattoz","t.cattoz@gmail.com","password1");
        List<UserAccount> userAccountList = new ArrayList<>();
        userAccountList.add(user3);
        userAccountList.add(user2);
        userAccountIterable = userAccountList;
        BankAccount bankAccountOut = new BankAccount(1,1,"orange","iban1",false);
        BankAccount bankAccountIn = new BankAccount(2,1,"a.benson@gmail.com","ibanInterne1",true);
        List<BankAccount> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccountIn);
        bankAccountList.add(bankAccountOut);
        userAccount = new UserAccount(1,"Alex","Benson","a.benson@gmail.com","password1",userAccountList,bankAccountList);

    }

    @Test
    void getUsersTest(){
        when(userAccountRepository.findAll()).thenReturn(userAccountIterable);
        assertThat(userAccountService.getUsers()).isNotEmpty();
        verify(userAccountRepository).findAll();
    }

    @Test
    void addUserTest(){
        when(userAccountRepository.save(Mockito.any(UserAccount.class))).thenReturn(userAccount);
        assertThat(userAccountService.addUser(userAccount)).isInstanceOf(UserAccount.class);
        verify(userAccountRepository).save(Mockito.any(UserAccount.class));
    }

    @Test
    void getUserFriendTest(){
        when (userAccountRepository.findById(1)).thenReturn(Optional.of(userAccount));
        assertThat(userAccountService.getUserFriend(1)).asList();
        verify(userAccountRepository).findById(1);
    }
    @Test
    void saveFriendTest(){
        UserAccount user4 = new UserAccount(4,"françois","Cattoz","f.cattoz@gmail.com","password1");
        when (userAccountRepository.findByEmail("f.cattoz@gmail.com")).thenReturn(Optional.of(user4));
        when (userAccountRepository.findById(1)).thenReturn(Optional.of(userAccount));
        when (userAccountRepository.save(Mockito.any(UserAccount.class))).thenReturn(userAccount);
        userAccountService.saveFriend(1,user4);
        verify(userAccountRepository).findByEmail("f.cattoz@gmail.com");
        verify(userAccountRepository).findById(1);
        verify(userAccountRepository).save(Mockito.any(UserAccount.class));
    }

    @Test
    void isUserExistTest(){
        when(userAccountRepository.findByEmail("a.benson@gmail.com")).thenReturn(Optional.of(userAccount));
        assertThat(userAccountService.isUserExist(userAccount)).isTrue();
        verify(userAccountRepository).findByEmail("a.benson@gmail.com");
    }

    @Test
    void getidUserbyUserName(){
        when (userAccountRepository.findByEmail("a.benson@gmail.com")).thenReturn(Optional.of(userAccount));
        assertThat(userAccountService.getIdUserbyUsername("a.benson@gmail.com")).isEqualTo(1);
        verify(userAccountRepository).findByEmail("a.benson@gmail.com");
    }
}
