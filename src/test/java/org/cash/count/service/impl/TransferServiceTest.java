/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cash.count.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.cash.count.constant.AccountType;
import org.cash.count.model.Account;
import org.cash.count.repository.AccountRepository;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Captor;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * {@link org.cash.count.service.impl.TransferService} unit tests
 * 
 * @author rafael
 */
@RunWith(MockitoJUnitRunner.class)
public class TransferServiceTest {

    private TransferService transferService;
    
    @Mock
    private AccountRepository accountRepository;
    
    @Captor
    private ArgumentCaptor<Account> accountCaptor;
    
    /**
     * Initialize class.
     */
    @Before
    public void setUp(){
        transferService = new TransferService(accountRepository);
    }
    
    /**
     * Should increase the amount for both accounts.
     */
    @Test
    public void shouldTransferBtwAccounts_increasedBalance() {
        
        Account debitAccount = new Account();
        debitAccount.setBalance(new BigDecimal("15000"));
        debitAccount.setIncreasedBy(AccountType.DEBIT);
        
        Account creditAccount = new Account();
        creditAccount.setBalance(new BigDecimal("3000"));
        creditAccount.setIncreasedBy(AccountType.CREDIT);
        
        Account resultingDebitAccount = new Account();
        resultingDebitAccount.setBalance(new BigDecimal("25000"));
        resultingDebitAccount.setIncreasedBy(AccountType.DEBIT);
        
        Account resultingCreditAccount = new Account();
        resultingCreditAccount.setBalance(new BigDecimal("13000"));
        resultingCreditAccount.setIncreasedBy(AccountType.CREDIT);
        
        when(accountRepository.findById(1)).thenReturn(Optional.of(debitAccount));
        when(accountRepository.findById(3)).thenReturn(Optional.of(creditAccount));
        
        when(accountRepository.save(any()))
                .thenReturn(resultingDebitAccount)
                .thenReturn(resultingCreditAccount);
        
        transferService.transfer(1, 3, "10000");
        
        verify(accountRepository, times(2)).save(accountCaptor.capture());
        
        List<Account> accountsCaptured = accountCaptor.getAllValues();
        Account debitedAccountCaptured = accountsCaptured.get(0);
        Account creditedAccountCaptured = accountsCaptured.get(1);
        
        assertThat(debitedAccountCaptured).isNotNull();
        assertThat(debitedAccountCaptured.getIncreasedBy()).isEqualTo(resultingDebitAccount.getIncreasedBy());
        assertThat(debitedAccountCaptured.getBalance()).isEqualTo(resultingDebitAccount.getBalance());
        
        assertThat(creditedAccountCaptured).isNotNull();
        assertThat(creditedAccountCaptured.getIncreasedBy()).isEqualTo(resultingCreditAccount.getIncreasedBy());
        assertThat(creditedAccountCaptured.getBalance()).isEqualTo(resultingCreditAccount.getBalance());
    }
    
    /**
     * Should decrease the amount for both amounts
     */
    @Test
    public void shouldTransferBtwAccounts_decreasedBalance(){
        
        Account creditAccount = new Account();
        creditAccount.setBalance(new BigDecimal("3000"));
        creditAccount.setIncreasedBy(AccountType.CREDIT);
        
        Account debitAccount = new Account();
        debitAccount.setBalance(new BigDecimal("15000"));
        debitAccount.setIncreasedBy(AccountType.DEBIT);
        
        Account resultingCreditAccount = new Account();
        resultingCreditAccount.setBalance(new BigDecimal("2500"));
        resultingCreditAccount.setIncreasedBy(AccountType.CREDIT);
        
        Account resultingDebitAccount = new Account();
        resultingDebitAccount.setBalance(new BigDecimal("14500"));
        resultingDebitAccount.setIncreasedBy(AccountType.DEBIT);
        
        when(accountRepository.findById(2)).thenReturn(Optional.of(debitAccount));
        when(accountRepository.findById(5)).thenReturn(Optional.of(creditAccount));
        
        when(accountRepository.save(any()))
                .thenReturn(resultingDebitAccount)
                .thenReturn(resultingCreditAccount);
        
        transferService.transfer(5, 2, "500");
        
        verify(accountRepository, times(2)).save(accountCaptor.capture());
        
        List<Account> accountsCaptured = accountCaptor.getAllValues();
        Account debitedAccountCaptured = accountsCaptured.get(0);
        Account creditedAccountCaptured = accountsCaptured.get(1);
        
        assertThat(debitedAccountCaptured).isNotNull();
        assertThat(debitedAccountCaptured.getIncreasedBy()).isEqualTo(resultingCreditAccount.getIncreasedBy());
        assertThat(debitedAccountCaptured.getBalance()).isEqualTo(resultingCreditAccount.getBalance());
        
        assertThat(creditedAccountCaptured).isNotNull();
        assertThat(creditedAccountCaptured.getIncreasedBy()).isEqualTo(resultingDebitAccount.getIncreasedBy());
        assertThat(creditedAccountCaptured.getBalance()).isEqualTo(resultingDebitAccount.getBalance());
    }
    
    /**
     * Should transfer the amount from the credited account to the debited account.
     * Both accounts are increased by debits.
     */
    @Test
    public void shouldTransferBtwAccounts_transferBalance_creditedToDebitedAccount(){
        
        Account cashAccount = new Account();
        cashAccount.setBalance(new BigDecimal("4000"));
        cashAccount.setIncreasedBy(AccountType.DEBIT);
        
        Account equipmentAccount = new Account();
        equipmentAccount.setBalance(new BigDecimal("1000"));
        equipmentAccount.setIncreasedBy(AccountType.DEBIT);
        
        Account resultingCashAccount = new Account();
        resultingCashAccount.setBalance(new BigDecimal("3000"));
        resultingCashAccount.setIncreasedBy(AccountType.DEBIT);
        
        Account resultingEquipmentAccount = new Account();
        resultingEquipmentAccount.setBalance(new BigDecimal("2000"));
        resultingEquipmentAccount.setIncreasedBy(AccountType.DEBIT);
        
        when(accountRepository.findById(2)).thenReturn(Optional.of(equipmentAccount));
        when(accountRepository.findById(5)).thenReturn(Optional.of(cashAccount));
        
        when(accountRepository.save(any()))
                .thenReturn(resultingEquipmentAccount)
                .thenReturn(resultingCashAccount);
        
        transferService.transfer(2, 5, "1000");
        
        verify(accountRepository, times(2)).save(accountCaptor.capture());
        
        List<Account> accountsCaptured = accountCaptor.getAllValues();
        Account debitedAccountCaptured = accountsCaptured.get(0);
        Account creditedAccountCaptured = accountsCaptured.get(1);
        
        assertThat(debitedAccountCaptured).isNotNull();
        assertThat(debitedAccountCaptured.getIncreasedBy()).isEqualTo(resultingEquipmentAccount.getIncreasedBy());
        assertThat(debitedAccountCaptured.getBalance()).isEqualTo(resultingEquipmentAccount.getBalance());
        
        assertThat(creditedAccountCaptured).isNotNull();
        assertThat(creditedAccountCaptured.getIncreasedBy()).isEqualTo(resultingCashAccount.getIncreasedBy());
        assertThat(creditedAccountCaptured.getBalance()).isEqualTo(resultingCashAccount.getBalance());
    }
    
    /**
     * Should thrown exception when the account does not exist.
     */
    @Test(expected = NoSuchElementException.class)
    public void shouldThrownException_debitedAccountNotFound(){
        
        when(accountRepository.findById(323)).thenReturn(Optional.empty());
        when(accountRepository.findById(2)).thenReturn(Optional.of(new Account()));
        
        transferService.transfer(323, 2, "100");
    }
    
    /**
     * Should thrown exception when the account does not exist.
     */
    @Test(expected = NoSuchElementException.class)
    public void shouldThrownException_creditedAccountNotFound(){
        
        when(accountRepository.findById(323)).thenReturn(Optional.empty());
        when(accountRepository.findById(2)).thenReturn(Optional.of(new Account()));
        
        transferService.transfer(2, 323, "100");
    }
    
    /**
     * Should not make the transfer. An account is disabled
     */
    @Test
    public void shouldNotExecuteTransfer_accountDisabled(){
        Account cashAccount = new Account();
        cashAccount.setBalance(new BigDecimal("4000"));
        cashAccount.setIncreasedBy(AccountType.DEBIT);
        
        Account equipmentAccount = new Account();
        equipmentAccount.setBalance(new BigDecimal("1000"));
        equipmentAccount.setIncreasedBy(AccountType.DEBIT);
        equipmentAccount.setDisabled(true);
        
        when(accountRepository.findById(2)).thenReturn(Optional.of(equipmentAccount));
        when(accountRepository.findById(5)).thenReturn(Optional.of(cashAccount));
        
        try{
            transferService.transfer(2, 5, "100");
            fail();
        } catch(IllegalStateException e){
            assertThat(e.getMessage()).isEqualTo("Account disabled");
        }
    }
}
