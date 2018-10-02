/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cash.count.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.cash.count.constant.AccountType;
import org.cash.count.dto.AccountCreationDto;
import org.cash.count.dto.AccountDto;
import org.cash.count.model.Account;
import org.cash.count.repository.AccountRepository;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *{@link org.cash.count.service.impl.AccountManager} integration tests
 * 
 * @author rafael
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountManagerIT {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    private AccountManager accountManager;
    
    /**
     * Initialize class
     */
    @Before
    public void setUp(){
        accountManager = new AccountManager(accountRepository);
    }
    
    /**
     * Should created an account
     */
    @Test
    public void shouldCreateAnAccount(){
        AccountCreationDto account = new AccountCreationDto();
        account.setName("New Account");
        account.setDescription("Any Description");
        account.setParentId(1);
        
        Account parentAccount = new Account();
        parentAccount.setId(1);
        parentAccount.setName("Parent Account");
        parentAccount.setIncreasedBy(AccountType.CREDIT);
        entityManager.merge(parentAccount);
        entityManager.flush();
        
        accountManager.create(account);
        
        AccountDto createdAccount = accountManager.findById(2);
        
        assertThat(createdAccount.getParentId()).isEqualTo(1);
        assertThat(createdAccount.getName()).isEqualTo("New Account");
    }
    
    /**
     * Should disable an account
     */
    @Test
    public void shouldDisableAnAccount(){
        AccountCreationDto account = new AccountCreationDto();
        account.setName("New Account");
        account.setDescription("Any Description");
        account.setParentId(3);
        
        Account parentAccount = new Account();
        parentAccount.setId(3);
        parentAccount.setName("Parent Account");
        parentAccount.setIncreasedBy(AccountType.CREDIT);
        entityManager.merge(parentAccount);
        entityManager.flush();
        
        accountManager.create(account);
        
        accountManager.disable(4);
        
        AccountDto disabledAccount = accountManager.findById(4);
        
        assertThat(disabledAccount.isDisabled()).isEqualTo(true);
    }
}
