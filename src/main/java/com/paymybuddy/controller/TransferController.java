package com.paymybuddy.controller;

import com.paymybuddy.dto.TabTransactionDTO;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.service.BankAccountService;
import com.paymybuddy.service.TransferService;
import com.paymybuddy.service.UserAccountService;
import com.paymybuddy.utils.RetrieveIdSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

@Controller
@RolesAllowed({"USER", "ADMIN"})
public class TransferController {
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private TransferService transferService;
    @Autowired
    private BankAccountService bankAccountService;

    private int idSession;
    private String information = "";
    private Transaction transactionTemp=new Transaction();

    @GetMapping("/transfer")
    public String getTransaction(Model model, @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "3") int size) {
        /*active session*/
        RetrieveIdSession id = new RetrieveIdSession();
        this.idSession = id.getIdSession(userAccountService);
        /*remplie ma liste de compte et d'ami*/
        List<UserAccount> friends = userAccountService.getUserFriend(idSession);
        List<BankAccount> options = bankAccountService.getBankAccountFiend(friends);
        model.addAttribute("options", options);

        /*affiche transaction*/
        List<Transaction> transactionList = new ArrayList<>();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Transaction> transactionPage = transferService.getTransactionTab(idSession, pageable);
        transactionList = transactionPage.getContent();

        List<TabTransactionDTO> tabTransactionDTOList = new ArrayList<>();
        transactionList.forEach(tl -> {
            BankAccount bankAccount = bankAccountService.getBankAccountfindById(tl.getAccountSendId());
            UserAccount userAccount = userAccountService.findUserByid(bankAccount.getUserId());
            tabTransactionDTOList.add(new TabTransactionDTO(userAccount.getFirstName() + ' ' + userAccount.getLastName(), tl.getComment(), tl.getAmount()));
        });

        model.addAttribute("transactionList", tabTransactionDTOList);
        model.addAttribute("currentPage", transactionPage.getNumber() + 1);
        model.addAttribute("totalItems", transactionPage.getTotalElements());
        model.addAttribute("totalPages", transactionPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("information", information);
        return "/transfer";
    }

    @GetMapping("/validatetransfer")
    public String getValidateTransfer(Model model, @RequestParam(value = "accountReceiveId") int accountReceive, @RequestParam(value = "amount", defaultValue = "-1") float amount) {
        String redirect = "validatetransfer";
        RetrieveIdSession id = new RetrieveIdSession();
        this.idSession = id.getIdSession(userAccountService);
        if (accountReceive == 0 || amount == -1) {
            this.information = "Input Error : you must choise Connection and amount";
            redirect = "redirect:/transfer";
        } else {
            Transaction transaction = new Transaction();
            transaction.setAccountSendId(bankAccountService.getIdInsideAccount(idSession));
            transaction.setAccountReceiveId(accountReceive);
            transaction.setAmount(amount);
            this.transactionTemp = transaction;
            BankAccount bankAccountReceive = bankAccountService.getBankAccountfindById(transaction.getAccountReceiveId());
            UserAccount userReceive = userAccountService.findUserByid(bankAccountReceive.getUserId());
            model.addAttribute("userreceive", userReceive);
            model.addAttribute("transactionIn", transaction);
        }
        return redirect;
    }

    @PostMapping("/validatetransfer")
    public String postValidateTransaction(@ModelAttribute Transaction transaction, BindingResult errors, Model model) {
        this.information = "";
        RetrieveIdSession id = new RetrieveIdSession();
        this.idSession = id.getIdSession(userAccountService);
        /*recupere la transaction*/
        this.transactionTemp.setComment(transaction.getComment());
        model.addAttribute(transaction);
        if (isCompleteForTransaction(transactionTemp)) {
            if (bankAccountService.isTransactionOutside(transactionTemp)) {
                if (transactionTemp.getAmount() + transferService.getBalance(idSession) >= 0) {
                    transferService.saveTransationExterne(transactionTemp);
                } else {
                    this.information = "insufficient balance";
                }
            } else {
                if (transactionTemp.getAmount() > 0 && transactionTemp.getAmount() <= transferService.getBalance(idSession)) {
                    transferService.saveTransaction(transactionTemp);
                } else {
                    this.information = "insufficient balance";
                }
            }
        } else {
            this.information = "select a connection and choise amount";
        }
        return "redirect:/transfer";
    }

    private Boolean isCompleteForTransaction(Transaction transaction) {
        if (transaction.getAccountSendId() > 0 && transaction.getAccountReceiveId() > 0 &&
                transaction.getAmount() != 0) {
            return true;
        } else {
            return false;
        }
    }
}
