/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccc.base;

import java.util.Date;

/**
 *
 * @author KonZioutos
 */
public class CreditCard {

    private String cardNumber;
    private int creditLimit;
    private int creditBalance;
    private Date expirationDate;
    private int debtAmount;

    public CreditCard(String cardNumber, int creditLimit, int creditBalance, Date expirationDate, int debtAmount) {
        this.cardNumber = cardNumber;
        this.creditLimit = creditLimit;
        this.creditBalance = creditBalance;
        this.expirationDate = expirationDate;
        this.debtAmount = debtAmount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(int creditLimit) {
        this.creditLimit = creditLimit;
    }

    public int getCreditBalance() {
        return creditBalance;
    }

    public void setCreditBalance(int creditBalance) {
        this.creditBalance = creditBalance;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(int debtAmount) {
        this.debtAmount = debtAmount;
    }

}
