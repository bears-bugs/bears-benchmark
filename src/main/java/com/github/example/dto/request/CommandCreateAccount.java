package com.github.example.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;


public class CommandCreateAccount implements Serializable {
    private static final long serialVersionUID = -4842260737441570738L;

    private BigDecimal initialBalance;

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }
}
