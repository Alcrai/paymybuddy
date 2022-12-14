package com.paymybuddy.controller;

import com.paymybuddy.dto.ProfileDTO;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.service.BankAccountService;
import com.paymybuddy.service.TransferService;
import com.paymybuddy.service.UserAccountService;
import com.paymybuddy.utils.RetrieveIdSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RolesAllowed({"USER","ADMIN"})
public class profileController {
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private TransferService transferService;
    @Autowired
    private BankAccountService bankAccountService;

    private int idSession;
    private String information="";
    private int optionTemp;
    private float amountTemp;


    @Transactional
    @GetMapping("/profile")
    public String profile(Model model){
        RetrieveIdSession id = new RetrieveIdSession();
        this.idSession =id.getIdSession(userAccountService) ;
        float balance = transferService.getBalance(idSession);
        UserAccount user = userAccountService.findUserByid(idSession);
        List<BankAccount> accounts= user.getBankAccounts().stream().filter(a->!a.getInside()).collect(Collectors.toList());
        ProfileDTO profile = new ProfileDTO(user.getFirstName(),user.getLastName(),user.getEmail(), user.getPassword(),accounts.get(0).getName(), accounts.get(0).getIban());
        model.addAttribute("profile",profile);
        model.addAttribute("balance",balance);
        return "profile";
    }

    @Transactional
    @GetMapping("/updateprofile")
    public String getUpdateProfile(Model model){
        RetrieveIdSession id = new RetrieveIdSession();
        this.idSession =id.getIdSession(userAccountService) ;
        UserAccount user = userAccountService.findUserByid(idSession);
        List<BankAccount> accounts= user.getBankAccounts().stream().filter(a->!a.getInside()).collect(Collectors.toList());
        ProfileDTO profile = new ProfileDTO(user.getFirstName(),user.getLastName(),user.getEmail(), user.getPassword(),accounts.get(0).getName(), accounts.get(0).getIban());
        model.addAttribute("profile",profile);
        return "/updateprofile";
    }

    @Transactional
    @PostMapping("/updateprofile")
    public String postUpdateProfile(@ModelAttribute ProfileDTO profile, BindingResult errors, Model model){
        RetrieveIdSession id = new RetrieveIdSession();
        this.idSession =id.getIdSession(userAccountService) ;
        model.addAttribute("profile",profile);
        UserAccount user = userAccountService.findUserByid(idSession);
        List<BankAccount> accounts= user.getBankAccounts().stream().filter(a->!a.getInside()).collect(Collectors.toList());
        if(!profile.getNameBank().isEmpty()){accounts.get(0).setName(profile.getNameBank());}
        if(!profile.getIban().isEmpty()){accounts.get(0).setIban(profile.getIban());}
        if(!profile.getPassword().isEmpty()){user.setPassword(passwordEncoder().encode(profile.getPassword()));}
        return "redirect:/profile";
    }

    @GetMapping("/transferprofile")
    public String getTransferProfile(Model model){
        RetrieveIdSession id = new RetrieveIdSession();
        this.idSession =id.getIdSession(userAccountService) ;
        BankAccount bankOutside = bankAccountService.getBankAccountOutside(idSession);
        float balance = transferService.getBalance(idSession);
        model.addAttribute("balance", balance);
        model.addAttribute("bankaccount",bankOutside);
        model.addAttribute("information", information);
        return "/transferprofile";
    }

    @PostMapping("/validationbank")
    public String posttransferprofile(@ModelAttribute BankAccount bankAccount, @RequestParam("option") int option,@RequestParam(value = "amount",defaultValue = "-1") int amount, BindingResult errors, Model model) {
        this.information = "";
        String redirect = "redirect:/validationbank?option="+option+"&amount="+amount;
        if (option == 0 || amount <= 0) {
            this.information = "Please choose your operation and your amount";
            redirect = "redirect:/transferprofile";
        } else {
            switch (option) {
                case 1: /*Supply*/
                    break;
                case 2:/*transfer*/
                    if (amount > transferService.getBalance(idSession)) {
                        this.information = "insufficient balance";
                        redirect = "redirect:/transferprofile";
                    }
                    break;
                default:
                    redirect = "redirect:/transferprofile";
                    break;
            }
        }

        return redirect;
    }

    @GetMapping("/validationbank")
    public String getValidationBank(@RequestParam("option") int option,@RequestParam(value = "amount",defaultValue = "-1") int amount, Model model){
        RetrieveIdSession id = new RetrieveIdSession();
        this.idSession =id.getIdSession(userAccountService) ;
        this.optionTemp=option;
        this.amountTemp=amount;
        String operation = "Supply";
        if (option==2){operation = "Transfer Out";}
        model.addAttribute("operation", operation);
        model.addAttribute("amount",amount);
        model.addAttribute("option", option);
        BankAccount bankOutside = bankAccountService.getBankAccountOutside(idSession);
        model.addAttribute("nameaccount", bankOutside.getName());
        model.addAttribute("iban", bankOutside.getIban());

        return "validationbank";
    }

    @PostMapping("/transfertemp")
    public String postTransferOutside(Model model){
        RetrieveIdSession id = new RetrieveIdSession();
        this.idSession =id.getIdSession(userAccountService) ;
        Transaction transaction = new Transaction();

        switch (optionTemp){
            case 1:
                transaction.setAccountSendId(bankAccountService.getBankAccountOutside(idSession).getBankAccountId());
                transaction.setAccountReceiveId(bankAccountService.getIdInsideAccount(idSession));
                transaction.setAmount(amountTemp);
                transaction.setComment("Supply");
                break;
            case 2:
                transaction.setAccountSendId(bankAccountService.getIdInsideAccount(idSession));
                transaction.setAccountReceiveId(bankAccountService.getBankAccountOutside(idSession).getBankAccountId());
                transaction.setAmount(-amountTemp);
                transaction.setComment("transferOut");
                break;
        }
        transferService.saveTransationExterne(transaction);
        return "redirect:/transfer";
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
