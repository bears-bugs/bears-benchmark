/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cash.count.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.cash.count.constant.AccountType;
import org.cash.count.dto.AccountCreationDto;
import org.cash.count.dto.AccountDto;
import org.cash.count.dto.AccountUpdatedDto;
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
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * {@link org.cash.count.service.impl.AccountManager} unit tests
 * 
 * @author rafael
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountManagerTest {
    
    private AccountManager accountManager;
    
    @Mock
    private AccountRepository accountRepository;
    
    @Captor
    private ArgumentCaptor<Account> accountCaptor;
    
    /**
     * Initialize class
     */
    @Before
    public void setUp(){
        accountManager = new AccountManager(accountRepository);
    }
    
    /**
     * Successful account creation
     */
    @Test
    public void shouldCreateAccount_hasRequiredFields(){
        AccountCreationDto account = new AccountCreationDto();
        account.setName("New Account");
        account.setDescription("Any Description");
        account.setParentId(1);
        
        Account parentAccount = new Account();
        parentAccount.setId(1);
        parentAccount.setIncreasedBy(AccountType.CREDIT);
        
        Account savedAccount = new Account();
        savedAccount.setId(23);
        savedAccount.setName("New Account");
        savedAccount.setDescription("Any Description");
        savedAccount.setParent(parentAccount);
        savedAccount.setIncreasedBy(AccountType.CREDIT);
        savedAccount.setBalance(BigDecimal.ZERO);
        
        when(accountRepository.findById(1)).thenReturn(Optional.of(parentAccount));
        when(accountRepository.save(any())).thenReturn(savedAccount);
        
        accountManager.create(account);
        
        verify(accountRepository, times(1)).save(accountCaptor.capture());
        
        Account capturedAccount = accountCaptor.getValue();
        assertThat(capturedAccount.getBalance()).isEqualTo(BigDecimal.ZERO);
    }
    
    /**
     * Should not create account. Missing name
     */
    @Test
    public void shouldNotCreateAccount_missingName(){
        AccountCreationDto account = new AccountCreationDto();
        account.setId(23);
        account.setDescription("Any Description");
        account.setParentId(1);
        
        try{
            accountManager.create(account);
            fail();
        } catch(NoSuchElementException e){
            assertThat(e.getMessage()).isEqualTo("Missing Account Name");
        }
        
        verifyZeroInteractions(accountRepository);
    }
    
    /**
     * Should not create account. Missing parent account id
     */
    @Test
    public void shouldNotCreateAccount_missingParentAccountId(){
        AccountCreationDto account = new AccountCreationDto();
        account.setId(23);
        account.setName("New Account");
        account.setDescription("Any Description");
        
        try{
            accountManager.create(account);
            fail();
        } catch(NoSuchElementException e){
            assertThat(e.getMessage()).isEqualTo("Missing Parent Account Id");
        }
        
        verifyZeroInteractions(accountRepository);
    }
    
    /**
     * Should not create account. parent account not found
     */
    @Test(expected = NoSuchElementException.class)
    public void shouldNotCreateAccount_parentAccountNotFound(){
        AccountCreationDto account = new AccountCreationDto();
        account.setId(23);
        account.setName("New Account");
        account.setDescription("Any Description");
        account.setParentId(1);
        
        when(accountRepository.findById(1)).thenReturn(Optional.empty());
        
        accountManager.create(account);
    }
    
    /**
     * Should find account by id
     */
    @Test
    public void shouldFindById_successfulBehaviour(){
        
        Account parentAccount = new Account();
        parentAccount.setId(1);
        
        Account storedAccount = new Account();
        storedAccount.setId(34);
        storedAccount.setName("Name of the account");
        storedAccount.setDescription("Description");
        storedAccount.setBalance(new BigDecimal("4000"));
        storedAccount.setParent(parentAccount);
        
        when(accountRepository.findById(34)).thenReturn(Optional.of(storedAccount));
        
        AccountDto foundAccount = accountManager.findById(34);
        
        assertThat(foundAccount.getId()).isEqualTo(34);
        assertThat(foundAccount.getName()).isEqualTo("Name of the account");
        assertThat(foundAccount.getDescription()).isEqualTo("Description");
        assertThat(foundAccount.getBalance()).isEqualTo(new BigDecimal(4000));
    }
    
    /**
     * Should throw exception. Account not found
     */
    @Test(expected=NoSuchElementException.class)
    public void shouldFindById_accountNotFound(){
        when(accountRepository.findById(0)).thenReturn(Optional.empty());
        accountManager.findById(0);
    }
    
    /**
     * Should update account successfully
     */
    @Test
    public void shouldUpdateAccount_successfulBehaviour(){
        AccountUpdatedDto dto = new AccountUpdatedDto();
        dto.setId(1);
        dto.setName("Name Updated");
        dto.setDescription("new description");
        
        Account foundAccount = new Account();
        foundAccount.setId(1);
        foundAccount.setName("Old Name");
        foundAccount.setDescription("Old Description");
        foundAccount.setBalance(new BigDecimal("3400"));
        
        Account storedAccount = new Account();
        storedAccount.setId(1);
        storedAccount.setName("Name Updated");
        storedAccount.setDescription("new description");
        storedAccount.setBalance(new BigDecimal("3400"));
        
        when(accountRepository.findById(1)).thenReturn(Optional.of(foundAccount));
        when(accountRepository.save(any())).thenReturn(storedAccount);
        
        accountManager.update(dto);
        
        verify(accountRepository, times(1)).save(accountCaptor.capture());
        
        Account capturedAccount = accountCaptor.getValue();
        assertThat(capturedAccount.getId()).isEqualTo(storedAccount.getId());
        assertThat(capturedAccount.getName()).isEqualTo(storedAccount.getName());
        assertThat(capturedAccount.getDescription()).isEqualTo(storedAccount.getDescription());
        assertThat(capturedAccount.getBalance()).isEqualTo(storedAccount.getBalance());
    }
    
    /**
     * Should not update account. Missing name
     */
    @Test
    public void shouldNotUpdateAccount_MissingName(){
        AccountUpdatedDto dto = new AccountUpdatedDto();
        dto.setId(1);
        dto.setDescription("new description");
        
        try{
            accountManager.update(dto);
            fail();
        } catch(NoSuchElementException e){
            assertThat(e.getMessage()).isEqualTo("Missing account name");
        }
    }
    
    /**
     * Should disable account successfully
     */
    @Test
    public void shouldDisableAccount_successfulBehaviour(){
        Account account = new Account();
        account.setId(5);
        account.setDisabled(false);
        account.setChildren(new ArrayList<>());
        
        Account storedAccount = new Account();
        storedAccount.setId(5);
        storedAccount.setDisabled(true);
        
        when(accountRepository.findById(5)).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenReturn(storedAccount);
        
        accountManager.disable(5);
        
        verify(accountRepository).save(accountCaptor.capture());
        
        Account capturedAccount = accountCaptor.getValue();
        assertThat(capturedAccount.isDisabled()).isEqualTo(storedAccount.isDisabled());
    }
    
    /**
     * Should disabled account. Children accounts are all disabled
     */
    @Test
    public void shouldDisableAccount_childrenAccountsDisabled(){
        Account childAccount = new Account();
        childAccount.setId(6);
        childAccount.setDisabled(true);
        
        List<Account> childrenAccounts = new ArrayList<>();
        childrenAccounts.add(childAccount);
        
        Account account = new Account();
        account.setId(5);
        account.setDisabled(false);
        account.setChildren(childrenAccounts);
        
        Account storedAccount = new Account();
        storedAccount.setId(5);
        storedAccount.setDisabled(true);
        
        when(accountRepository.findById(5)).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenReturn(storedAccount);
        
        accountManager.disable(5);
        
        verify(accountRepository).save(accountCaptor.capture());
        
        Account capturedAccount = accountCaptor.getValue();
        assertThat(capturedAccount.isDisabled()).isEqualTo(storedAccount.isDisabled());
    }
    
    /**
     * Should not disable account. Children accounts are enabled
     */
    @Test
    public void shouldNotDisableAccount_accountHasChildrenEnabledAccounts(){
        Account childAccount = new Account();
        childAccount.setId(6);
        childAccount.setDisabled(false);
        
        List<Account> childrenAccounts = new ArrayList<>();
        childrenAccounts.add(childAccount);
        
        Account account = new Account();
        account.setId(5);
        account.setDisabled(false);
        account.setChildren(childrenAccounts);
        
        when(accountRepository.findById(5)).thenReturn(Optional.of(account));
        
        try{
            accountManager.disable(5);
            fail();
        } catch(IllegalStateException e){
            assertThat(e.getMessage()).isEqualTo("Children accounts enabled");
        }
    }
}
