/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cash.count.service.impl;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.cash.count.constant.AccountType;
import org.cash.count.model.Account;
import org.cash.count.repository.AccountRepository;
import org.cash.count.service.ITransferService;

/**
 *
 * @author rafael
 */
public class TransferService implements ITransferService{

    private AccountRepository accountRepository;
    
    /**
     * Public constructor
     * 
     * @param accountRepository the account repository dependency
     */
    public TransferService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }
    
    /**
     * @see org.cash.count.service.ITransferService#transfer(int, int, java.lang.String) 
     */
    @Override
    public void transfer(int debitAccountId, int creditAccountId, String amount) {
        Optional<Account> debitedAccountWrapper = accountRepository.findById(debitAccountId);
        Optional<Account> creditedAccountWrapper = accountRepository.findById(creditAccountId);
        
        Account debitedAccount = debitedAccountWrapper.orElseThrow(NoSuchElementException::new);
        Account creditedAccount = creditedAccountWrapper.orElseThrow(NoSuchElementException::new);
        
        if (debitedAccount.isDisabled() || creditedAccount.isDisabled()){
            throw new IllegalStateException("Account disabled");
        }
        
        BigDecimal debitedAccountBalance = calculateDebitedBalance(debitedAccount, amount);
        BigDecimal creditedAccountBalance = calculateCreditedBalance(creditedAccount, amount);
        
        debitedAccount.setBalance(debitedAccountBalance);
        creditedAccount.setBalance(creditedAccountBalance);
        
        accountRepository.save(debitedAccount);
        accountRepository.save(creditedAccount);
    }
    
    private BigDecimal calculateDebitedBalance(Account debitedAccount, String amount){
        
        BigDecimal debitedAccountBalance = new BigDecimal(debitedAccount.getBalance().toString());
        if (debitedAccount.getIncreasedBy() == AccountType.DEBIT){
            return debitedAccountBalance.add(new BigDecimal(amount));
        } 
        return debitedAccountBalance.subtract(new BigDecimal(amount));
    }
    
    private BigDecimal calculateCreditedBalance(Account creditedAccount, String amount){
        
        BigDecimal creditedAccountBalance = new BigDecimal(creditedAccount.getBalance().toString());
        if (creditedAccount.getIncreasedBy() == AccountType.CREDIT){
            return creditedAccountBalance.add(new BigDecimal(amount));
        } 
        return creditedAccountBalance.subtract(new BigDecimal(amount));
    }
}
