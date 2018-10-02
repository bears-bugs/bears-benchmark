/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cash.count.service;

/**
 * Account transfer service
 * 
 * @author rafael
 */
public interface ITransferService {
    
    /**
     * Transfer between accounts
     * 
     * @param debitAccountId the id of the account to be debited
     * @param creditAccountId the id of the account to be credited
     * @param amount the transfer amount
     */
    void transfer (int debitAccountId, int creditAccountId, String amount);
}
