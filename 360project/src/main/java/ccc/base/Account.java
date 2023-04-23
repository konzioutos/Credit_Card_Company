/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccc.base;

/**
 *
 * @author KonZioutos
 */
public class Account {
    private int accountId;
    private int balance;
    private boolean IsClosed;

    public Account(int accountId, int balance, boolean IsClosed) {
        this.accountId = accountId;
        this.balance = balance;
        this.IsClosed = IsClosed;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public boolean isIsClosed() {
        return IsClosed;
    }

    public void setIsClosed(boolean IsClosed) {
        this.IsClosed = IsClosed;
    }
}
