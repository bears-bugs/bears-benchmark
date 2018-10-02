/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cash.count.service;

import org.cash.count.dto.AccountCreationDto;
import org.cash.count.dto.AccountDto;
import org.cash.count.dto.AccountUpdatedDto;

/**
 * CRUD operations on accounts
 * 
 * @author rafael
 */
public interface IAccountManager {
    
    /**
     * Create an account.
     * 
     * @param account 
     */
    public void create(AccountCreationDto account);
    
    /**
     * Find an account by id.
     * 
     * @param accountId
     * @return the account
     */
    public AccountDto findById (int accountId);
    
    /**
     * Update an account. Balance won't be updated.
     * 
     * @param account 
     */
    public void update(AccountUpdatedDto account);
    
    
    /**
     * Disable an account. A disabled account cannot be used on any transfer.
     * 
     * @param account 
     */
    public void disable(int accountId);
}
