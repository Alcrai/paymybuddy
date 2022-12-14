package com.paymybuddy.controller;

import com.paymybuddy.service.UserAccountService;
import com.paymybuddy.utils.RetrieveIdSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.security.RolesAllowed;

@Controller
@RolesAllowed({"USER","ADMIN"})
public class LoginController {
    @Autowired
    private UserAccountService userAccountService;
    private int idSession;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home(Model model) {
        RetrieveIdSession id = new RetrieveIdSession();
        this.idSession =id.getIdSession(userAccountService) ;
        return "home";
    }

}
