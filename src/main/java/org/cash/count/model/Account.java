/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cash.count.model;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.cash.count.constant.AccountType;

/**
 *
 * @author rafael
 */
@Entity
@Table(name="account")
public class Account {
    
    private int id;
    private String name;
    private String description;
    private BigDecimal balance;
    private AccountType increasedBy;
    private boolean disabled;
    private Account parent;
    private List<Account> children;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name="description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Column(name="balance")
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Column(name="increased_by")
    public AccountType getIncreasedBy() {
        return increasedBy;
    }

    public void setIncreasedBy(AccountType increasedBy) {
        this.increasedBy = increasedBy;
    }

    @Column(name="disabled")
    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @ManyToOne
    public Account getParent() {
        return parent;
    }

    public void setParent(Account parent) {
        this.parent = parent;
    }

    @OneToMany(mappedBy="parent")
    public List<Account> getChildren() {
        return children;
    }

    public void setChildren(List<Account> children) {
        this.children = children;
    }
}
