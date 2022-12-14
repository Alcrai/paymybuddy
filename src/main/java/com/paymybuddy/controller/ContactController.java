package com.paymybuddy.controller;

import com.paymybuddy.model.UserAccount;
import com.paymybuddy.service.UserAccountService;
import com.paymybuddy.utils.RetrieveIdSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Controller
@RolesAllowed({"USER","ADMIN"})
public class ContactController {
    @Autowired
    private UserAccountService userAccountService;
    private int idSession;
    private String information="";
    private String email="";
    private UserAccount userAccountTemp;


    @GetMapping("/contact")
    public String contact(Model model){
        RetrieveIdSession id = new RetrieveIdSession();
        this.idSession =id.getIdSession(userAccountService) ;
        List<UserAccount> userBuddy = userAccountService.getUserFriend(idSession);
        model.addAttribute("userbuddy", userBuddy);
        model.addAttribute("information",information);
        return "contact";
    }

    @PostMapping("/addcontact")
    public String postContact(@ModelAttribute UserAccount userAccount, BindingResult errors, Model model){
        this.information="";
        this.userAccountTemp=userAccount;
        String address = "redirect:/addcontact?email="+userAccountTemp.getEmail();
        model.addAttribute("useraccount",userAccount);
        this.email=userAccount.getEmail();
        if (userAccountService.isUserExist(userAccount)){
            this.information="User exists";
        }else {
            this.information="User not exists";
            address="redirect:/contact";
            }
        return address;
    }

    @GetMapping("/addcontact")
    public String getaddcontact(@RequestParam String email, Model model){
        model.addAttribute("email", email);
        return "/addcontact";
    }


    @PostMapping("/contact")
    public String postAddContact(Model model){
        userAccountService.saveFriend(idSession,userAccountTemp);
        this.information="";
        return "redirect:/contact";
    }




}
