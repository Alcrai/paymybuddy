package com.paymybuddy.service;

import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.UserAccountRepository;
import net.bytebuddy.dynamic.DynamicType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {
    @Autowired
    private UserAccountRepository userAccountRepository;


    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }
    public Iterable<UserAccount> getUsers(){
        return userAccountRepository.findAll();
    }

    public UserAccount addUser(UserAccount userAccount){
        return userAccountRepository.save(userAccount);
    }

    @Transactional
    public List<UserAccount> getUserFriend(int idUser) {
        List<UserAccount> friends = new ArrayList<>();
        Optional<UserAccount> userById = userAccountRepository.findById(idUser);
        friends = userById.get().getFriends();
        return friends;
    }

    public UserAccount findUserByid(int i) {
        return userAccountRepository.findById(i).get();
    }

  /*  public Iterable<UserAccount> findUserAccountList() {
        return userAccountRepository.findAll();
    }*/
    @Transactional
    public void saveFriend(int idSession, UserAccount accountfriend) {
        Optional<UserAccount> newFriend = userAccountRepository.findByEmail(accountfriend.getEmail());
        Optional<UserAccount> userInSession = userAccountRepository.findById(idSession);
        List<UserAccount> friendList = userInSession.get().getFriends();
        friendList.add(newFriend.get());
        userInSession.get().setFriends(friendList);
        userAccountRepository.save(userInSession.get());
    }


    public boolean isUserExist(UserAccount userAccount) {
        Boolean result=true;
        Optional<UserAccount> user = userAccountRepository.findByEmail(userAccount.getEmail());
        if (user.isEmpty()){
            result = false;
        } else {result=true;}
        return result;
    }

    public int getIdUserbyUsername(String email) {
        Optional<UserAccount> user = userAccountRepository.findByEmail(email);
        return user.get().getUserId();
    }

  /*  public UserAccount saveUser(UserAccount user) {
        return userAccountRepository.save(user);
    }*/
}
