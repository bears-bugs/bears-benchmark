/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cash.count.repository;

import org.cash.count.model.Account;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author rafael
 */
public interface AccountRepository extends CrudRepository<Account, Integer>{
    
}
