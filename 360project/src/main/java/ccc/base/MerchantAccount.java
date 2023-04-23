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
public class MerchantAccount extends Account {

    private int debtToCCC;
    private int supllyToCCC;

    public MerchantAccount(int accountId, int balance, boolean IsClosed, int debtToCCC, int supplyToCCC) {
        super(accountId, balance, IsClosed);
        this.debtToCCC = debtToCCC;
        this.supllyToCCC = supplyToCCC;
    }

    public int getDebtToCCC() {
        return debtToCCC;
    }

    public void setDebtToCCC(int debtToCCC) {
        this.debtToCCC = debtToCCC;
    }

    public int getSupllyToCCC() {
        return supllyToCCC;
    }

    public void setSupllyToCCC(int supllyToCCC) {
        this.supllyToCCC = supllyToCCC;
    }
}
