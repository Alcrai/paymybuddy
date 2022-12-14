package com.paymybuddy.utils;

import com.paymybuddy.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class RetrieveIdSession {

    public int getIdSession(UserAccountService userAccountService) {
        int id =0;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            System.out.println(currentUserName);
            id = userAccountService.getIdUserbyUsername(currentUserName);
            System.out.println(id);
        }
        return id;
    }

}
