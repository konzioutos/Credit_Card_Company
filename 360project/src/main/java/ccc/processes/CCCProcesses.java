/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccc.processes;

import ccc.base.Company;
import ccc.base.Merchant;
import ccc.base.PaymentType;
import ccc.base.PrivateUser;
import ccc.base.User;
import ccc.db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author KonZioutos
 */
public class CCCProcesses {

    private Connection conn;

    public CCCProcesses() {
        try {
            conn = DBConnection.getConnection();
            if (conn == null) {
                throw new SQLException();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createAccount(User user, String cardNumber) {
        try {
            if (!this.cardNumberIsValid(cardNumber)) {
                throw new Exception("Invalid card number");
            }
            conn.setAutoCommit(false);
            PreparedStatement stmt = null;
            if (user instanceof PrivateUser) {
                int accountId;
                int creditCardId;
                int privUserId;
                String insertPrivateUser = "INSERT INTO private_user (firstname, surname, fathername, afm)"
                        + " VALUES ('" + ((PrivateUser) user).getFirstname() + "',"
                        + "'" + ((PrivateUser) user).getSurname() + "', '" + ((PrivateUser) user).getFathername() + "', '" + user.getAfm() + "')";
                System.out.println(insertPrivateUser);
                stmt = conn.prepareStatement(insertPrivateUser, Statement.RETURN_GENERATED_KEYS);
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                rs.next();
                privUserId = rs.getInt(1);

                String insertPrivtAccount = "INSERT INTO private_account (acc_balance, acc_closed) VALUES (0, 0)";
                stmt = conn.prepareStatement(insertPrivtAccount, Statement.RETURN_GENERATED_KEYS);
                stmt.executeUpdate();
                rs = stmt.getGeneratedKeys();
                rs.next();
                accountId = rs.getInt(1);

                String insertCreditCard = "INSERT INTO credit_card (credit_limit, expiration_date, card_number) VALUES (1000, '2025-12-31', '" + cardNumber + "')";
                stmt = conn.prepareStatement(insertCreditCard, Statement.RETURN_GENERATED_KEYS);
                stmt.executeUpdate();
                rs = stmt.getGeneratedKeys();
                rs.next();
                creditCardId = rs.getInt(1);

                String insertPrivateAccountCard = "INSERT INTO private_account_card (card_id, priv_acc_id, priv_user_id)"
                        + " VALUES (" + creditCardId + ", " + accountId + ", " + privUserId + ") ";
                stmt = conn.prepareStatement(insertPrivateAccountCard);
                stmt.executeUpdate();
                System.out.println("Private account was created successfully.");

            } else if (user instanceof Merchant) {
                int accountId;
                int creditCardId;
                int merchantId;
                String insertMerchAccount = "INSERT INTO merchant_account (acc_balance,debt_to_ccc,supply_to_ccc, acc_closed) VALUES (0, 0,10,0)";
                System.out.println(insertMerchAccount);
                stmt = conn.prepareStatement(insertMerchAccount, Statement.RETURN_GENERATED_KEYS);
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                rs.next();
                accountId = rs.getInt(1);

                String insertMerchant = "INSERT INTO merchant_user (firstname, surname, fathername, merch_acc_id, afm)"
                        + " VALUES ('" + ((Merchant) user).getFirstname() + "',"
                        + "'" + ((Merchant) user).getSurname() + "', '" + ((Merchant) user).getFathername() + "', " + accountId + ", '" + user.getAfm() + "')";

                System.out.println(insertMerchant);
                stmt = conn.prepareStatement(insertMerchant, Statement.RETURN_GENERATED_KEYS);
                stmt.executeUpdate();
                //ResultSet rs = stmt.getGeneratedKeys();
                //rs.next();
                //privUserId = rs.getInt(1);


            } else if (user instanceof Company) {
                int accountId;
                int creditCardId;
                int companyId;

                String insertCompany = "INSERT INTO company (name, afm)"
                        + " VALUES ('" + ((Company) user).getName() + "', '" + user.getAfm() + "')";
                System.out.println(insertCompany);
                stmt = conn.prepareStatement(insertCompany, Statement.RETURN_GENERATED_KEYS);
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                rs.next();
                companyId = rs.getInt(1);

                String insertCompAccount = "INSERT INTO company_account (acc_balance,acc_closed) VALUES (0, 0)";
                stmt = conn.prepareStatement(insertCompAccount, Statement.RETURN_GENERATED_KEYS);
                stmt.executeUpdate();
                rs = stmt.getGeneratedKeys();
                rs.next();
                accountId = rs.getInt(1);

                String insertCreditCard = "INSERT INTO credit_card (credit_limit, expiration_date, card_number) VALUES (5000, '2025-12-31', '" + cardNumber + "')";
                stmt = conn.prepareStatement(insertCreditCard, Statement.RETURN_GENERATED_KEYS);
                stmt.executeUpdate();
                rs = stmt.getGeneratedKeys();
                rs.next();
                creditCardId = rs.getInt(1);

                String insertPrivateAccountCard = "INSERT INTO company_account_card (card_id,comp_acc_id,company_id)"
                        + " VALUES (" + creditCardId + ", " + accountId + ", " + companyId + ") ";
                stmt = conn.prepareStatement(insertPrivateAccountCard);
                stmt.executeUpdate();
                System.out.println("Company account was created successfully.");

            }
            conn.commit();
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception exc) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, exc);
        }
    }

    public boolean closeAccount(String afm) {
        try {
            int merchantId = -1;
            Statement stmt = conn.createStatement();

            /* Looking int merchant's table */
            String sqlQuery = "SELECT merchant_user.merch_user_id, merchant_user.merch_acc_id, merchant_account.debt_to_ccc "
                    + " FROM merchant_user, merchant_account WHERE merchant_user.afm = '" + afm + "'"
                    + " AND merchant_user.merch_acc_id = merchant_account.merch_acc_id";
            System.out.println(sqlQuery);
            ResultSet rs = stmt.executeQuery(sqlQuery);
            if (rs.next()) {
                /*Case where account close refers to merchant */
                if (rs.getInt(3) != 0) {
                    System.out.println("There is debt");
                    return false;
                }
                int merchAccId = rs.getInt(2);
                String closeAccSql = "UPDATE merchant_account SET acc_closed= CASE WHEN debt_to_ccc=0 THEN 1 END WHERE merch_acc_id =" + merchAccId;
                int updatedRowsNum = stmt.executeUpdate(closeAccSql);
                return true;
            }

            /* Looking int company's table */
            String sqlCompQuery = "SELECT company_account.comp_acc_id, credit_card.card_id, credit_card.debt_amount "
                    + " FROM company, company_account, credit_card, company_account_card WHERE company.afm = '" + afm + "'"
                    + " AND company_account_card.company_id = company.company_id "
                    + " AND company_account_card.comp_acc_id = company_account.comp_acc_id"
                    + " AND credit_card.card_id = company_account_card.card_id";

            System.out.println(sqlCompQuery);
            rs = stmt.executeQuery(sqlCompQuery);
            if (rs.next()) {
                if (rs.getInt(3) != 0) {
                    System.out.println("There is debt");
                    return false;
                }
                int companyAccountId = rs.getInt(1);
                String closeAccSql = "UPDATE company_account SET acc_closed= 1 WHERE comp_acc_id =" + companyAccountId;
                stmt.executeUpdate(closeAccSql);
                return true;
            }

            /* Looking int private_user's table */
            String sqlPrivQuery = "SELECT private_user.priv_user_id, private_account_card.card_id,credit_card.debt_amount "
                    + " FROM private_user, private_account, private_account_card,credit_card WHERE private_user.afm = '" + afm + "'"
                    + " AND private_user.priv_user_id = private_account_card.priv_user_id "
                    + " AND private_account.priv_acc_id=private_account_card.priv_acc_id "
                    + " AND credit_card.card_id=private_account_card.card_id";
            System.out.println(sqlPrivQuery);
            rs = stmt.executeQuery(sqlPrivQuery);
            if (rs.next()) {
                /*Case where account close refers to merchant */
                if (rs.getInt(3) != 0) {
                    System.out.println("There is debt");
                    return false;
                }
                int privAccId = rs.getInt(2);
                String closeAccSql = "UPDATE private_account SET acc_closed=1 WHERE priv_acc_id =" + privAccId;
                stmt.executeUpdate(closeAccSql);
                return true;
            }


        } catch (SQLException ex) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public Merchant getMerchantOfMonth(int month, int year) {
        try {
            int merchantId = -1;
            Statement stmt = conn.createStatement();

            Calendar calendar = Calendar.getInstance();

            calendar.set(year, month, 1);
            int maximumMonthDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            String daysOfMonth = String.valueOf(maximumMonthDay);
            String startDatetime = String.valueOf(year) + '-' + String.valueOf(month) + "-01 00:00:00";
            String endDatetime = String.valueOf(year) + '-' + String.valueOf(month) + "-" + daysOfMonth + " 00:00:00";
            ResultSet rs;

            String sqlQuery = "SELECT ccc_transaction.merch_acc_id, merchant_user.merch_user_id, merchant_user.firstname, "
                    + " merchant_user.surname, merchant_user.fathername, merchant_user.afm, COUNT(*) AS COUNT_PURCHASES "
                    + " FROM `ccc_transaction`, merchant_user  WHERE transc_type = 'DEBIT' "
                    + " AND ccc_transaction.merch_acc_id = merchant_user.merch_user_id"
                    + " AND date_time >= '" + startDatetime + "' AND date_time <= '" + endDatetime + "'"
                    + " GROUP BY merch_acc_id ORDER BY COUNT_PURCHASES DESC";
            rs = stmt.executeQuery(sqlQuery);
            if (!rs.next()) {
                return null;
            }
            merchantId = rs.getInt(1);
            Statement stmt2 = conn.createStatement();

            String sqlUpdCmnd = "UPDATE merchant_account SET debt_to_ccc = debt_to_ccc - debt_to_ccc * 0.05 WHERE merch_acc_id = " + merchantId;
            if (stmt2.executeUpdate(sqlUpdCmnd) == 0) {
                throw new Exception();
            }
            Merchant merchant = new Merchant(rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
            merchant.setId(rs.getInt(2));
            return merchant;

        } catch (SQLException ex) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public void payDebt(String afm, int amount, PaymentType paymentType) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = null;

            if (paymentType == PaymentType.MERCHANT_PAYMENT) {
                String sqlQueryMerchAccount = "SELECT merchant_account.debt_to_ccc"
                        + " FROM merchant_user, merchant_account"
                        + " WHERE merchant_user.merch_acc_id = merchant_account.merch_acc_id AND afm='" + afm + "'";
                System.out.println(sqlQueryMerchAccount);
                rs = stmt.executeQuery(sqlQueryMerchAccount);
                if (rs.next()) {
                    if (rs.getInt(1) - amount < 0) {
                        throw new Exception("The payment did not completed. You gave amount larger than the merchant's debt");
                    }
                } else {
                    throw new Exception("Not found merchant with that AFM.");
                }

                String sqlUpdateMerchDebt = "UPDATE merchant_account SET merchant_account.debt_to_ccc = "
                        + " IF (merchant_account.debt_to_ccc - " + amount + " >= 0, merchant_account.debt_to_ccc - " + amount + ", merchant_account.debt_to_ccc)"
                        + " WHERE merch_acc_id = (SELECT merch_acc_id FROM merchant_user WHERE afm='" + afm + "')";
                System.out.println(sqlUpdateMerchDebt);                
                stmt.executeUpdate(sqlUpdateMerchDebt);

            } else if (paymentType == PaymentType.CARD_USER_PAYMENT) {
                String sqlQueryPrivateUserCard = "SELECT credit_card.debt_amount"
                        + " FROM private_user, credit_card, private_account_card"
                        + " WHERE private_user.afm='" + afm + "' AND private_user.priv_user_id = private_account_card.priv_user_id"
                        + " AND credit_card.card_id = private_account_card.card_id";
                System.out.println(sqlQueryPrivateUserCard);
                rs = stmt.executeQuery(sqlQueryPrivateUserCard);
                if (rs.next()) {
                    if (rs.getInt(1) - amount < 0) {
                        throw new Exception("The payment did not completed. You gave amount larger than the user's debt");
                    }
                    String sqlUpdatePrivateUserDebt = "UPDATE credit_card SET "
                            + " credit_card.credit_balance = IF (credit_card.debt_amount - " + amount + " >= 0, credit_card.credit_balance + " + amount + ", credit_card.credit_balance),"
                            + " credit_card.debt_amount = IF (credit_card.debt_amount - " + amount + " >= 0, credit_card.debt_amount - " + amount + ", credit_card.debt_amount)"
                            + " WHERE credit_card.card_id = (SELECT private_account_card.card_id FROM private_account_card, private_user WHERE afm='" + afm + "'"
                            + " AND private_user.priv_user_id=private_account_card.priv_user_id)";
                    System.out.println(sqlUpdatePrivateUserDebt);
                    stmt.executeUpdate(sqlUpdatePrivateUserDebt);
                } else {
                    String sqlQueryCompanyCard = "SELECT credit_card.debt_amount"
                            + " FROM company, credit_card, company_account_card"
                            + " WHERE company.afm='" + afm + "' AND company.company_id = company_account_card.company_id"
                            + " AND credit_card.card_id = company_account_card.card_id";
                    System.out.println(sqlQueryCompanyCard);
                    rs = stmt.executeQuery(sqlQueryCompanyCard);
                    if (rs.next()) {
                        if (rs.getInt(1) - amount < 0) {
                            throw new Exception("The payment did not completed. You gave amount larger than the company's debt");
                        }
                        String sqlUpdateCompanyDebt = "UPDATE credit_card SET "
                                + " credit_card.credit_balance = IF (credit_card.debt_amount - " + amount + " >= 0, credit_card.credit_balance + " + amount + ", credit_card.credit_balance),"
                                + " credit_card.debt_amount = IF (credit_card.debt_amount - " + amount + " >= 0, credit_card.debt_amount - " + amount + ", credit_card.debt_amount)"
                                + " WHERE credit_card.card_id = (SELECT company_account_card.card_id FROM company_account_card, company WHERE afm='" + afm + "'"
                                + " AND company.company_id=company_account_card.company_id)";
                        System.out.println(sqlUpdateCompanyDebt);
                        stmt.executeUpdate(sqlUpdateCompanyDebt);
                    } else {
                        throw new Exception("No company or pricate user was found in the database with the given AFM.");
                    }
                }

            }
        } catch (SQLException sqlexc) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, sqlexc);
        } catch (Exception exc) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, exc);
        }
    }

    public boolean purchase(String clientCardNumber, String clientAfm, String afmMerchant, int purchaseAmount) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs;
            String sqlQueryMerchant = "SELECT merch_user_id FROM merchant_user"
                    + " WHERE afm = '" + afmMerchant + "'";
            rs = stmt.executeQuery(sqlQueryMerchant);
            int merchantId;
            if (rs.next()) {
                merchantId = rs.getInt(1);
            } else {
                throw new Exception("Database error. No merchant with the given AFM was found.");
            }

            String sqlQueryBalance = "SELECT credit_balance FROM credit_card"
                    + " WHERE card_number = '" + clientCardNumber + "'";
            rs = stmt.executeQuery(sqlQueryBalance);

            if (rs.next()) {
                int balance = rs.getInt(1);
                if (balance < purchaseAmount) {
                    throw new Exception("Not enough money for the purchase.");
                }

                String sqlQueryIsCompanyCard = "SELECT credit_card.card_id FROM credit_card, company_account_card"
                        + " WHERE credit_card.card_number = '" + clientCardNumber + "' AND credit_card.card_id=company_account_card.card_id";
                rs = stmt.executeQuery(sqlQueryIsCompanyCard);
                int cardId;
                if (rs.next()) {
                    /* We have company card. So:
                        1)Update balance, debt amount of card
                        2)insert transaction table
                        3)insert employee transaction
                        4)update fields in merchant_account
                     */
                    cardId = rs.getInt(1);
                    conn.setAutoCommit(false);
                    String sqlCompanyEmployee = "SELECT employee.company_id, company.afm, employee.emp_id FROM employee, company "
                            + " WHERE employee.afm ='" + clientAfm + "' AND employee.company_id=company.company_id";
                    rs = stmt.executeQuery(sqlCompanyEmployee);
                    System.out.println(sqlCompanyEmployee);
                    int companyId;
                    String companyAfm;
                    int employeeId;
                    if (rs.next()) {
                        companyId = rs.getInt(1);
                        companyAfm = rs.getString(2);
                        employeeId = rs.getInt(3);
                    } else {
                        throw new Exception("Database error.");
                    }

                    String sqlUpdateCompanyDebt = "UPDATE credit_card SET "
                            + " credit_card.debt_amount = IF (credit_card.credit_balance - " + purchaseAmount + " >= 0, credit_card.debt_amount + " + purchaseAmount + ", credit_card.debt_amount),"
                            + " credit_card.credit_balance = IF (credit_card.credit_balance - " + purchaseAmount + " >= 0, credit_card.credit_balance - " + purchaseAmount + ", credit_card.credit_balance)"
                            + " WHERE credit_card.card_id = (SELECT company_account_card.card_id FROM company_account_card, company WHERE company.afm='" + companyAfm + "'"
                            + " AND company.company_id=company_account_card.company_id)";
                    System.out.println(sqlUpdateCompanyDebt);
                    stmt.executeUpdate(sqlUpdateCompanyDebt);

                    PreparedStatement insertTransctStmt;
                    String insertTransaction = "INSERT INTO ccc_transaction (transc_type, amount, card_id, merch_acc_id)"
                            + " VALUES ('DEBIT'," + purchaseAmount + ", " + cardId + ", " + merchantId + ")";
                    System.out.println(insertTransaction);
                    insertTransctStmt = conn.prepareStatement(insertTransaction, Statement.RETURN_GENERATED_KEYS);
                    insertTransctStmt.executeUpdate();
                    ResultSet rsInsertTransc = insertTransctStmt.getGeneratedKeys();
                    rsInsertTransc.next();
                    int newTransactionId = rsInsertTransc.getInt(1);

                    String insertEmployeeTransaction = "INSERT INTO employee_transaction (card_id, transc_id, emp_id) "
                            + " VALUES (" + cardId + ", " + newTransactionId + ", " + employeeId + ")";
                    System.out.println(insertEmployeeTransaction);
                    stmt.executeUpdate(insertEmployeeTransaction);

                    String sqlUpdateMerchantAccount = "UPDATE merchant_account SET "
                            + " debt_to_ccc = debt_to_ccc + supply_to_ccc/100 * " + purchaseAmount
                            + " WHERE merch_acc_id=" + this.getMerchantAccount(merchantId);
                    System.out.println(sqlUpdateMerchantAccount);
                    stmt.executeUpdate(sqlUpdateMerchantAccount);

                    conn.commit();
                } else {
                    /* We will check if it is card of private user */
                    String sqlQueryIsPrivateCard = "SELECT credit_card.card_id FROM credit_card, private_account_card"
                            + " WHERE credit_card.card_number = '" + clientCardNumber + "' AND credit_card.card_id= private_account_card.card_id";
                    rs = stmt.executeQuery(sqlQueryIsPrivateCard);
                    if (rs.next()) {
                        //1)Update balance, debt amount of card
                        //2)insert transaction table
                        //3)update fields in merchant_account
                        cardId = rs.getInt(1);
                        conn.setAutoCommit(false);
                        String sqlUpdatePrivateUserDebt = "UPDATE credit_card SET "
                                + " credit_card.debt_amount = IF (credit_card.credit_balance - " + purchaseAmount + " >= 0, credit_card.debt_amount + " + purchaseAmount + ", credit_card.debt_amount),"
                                + " credit_card.credit_balance = IF (credit_card.credit_balance - " + purchaseAmount + " >= 0, credit_card.credit_balance - " + purchaseAmount + ", credit_card.credit_balance)"
                                + " WHERE credit_card.card_id = (SELECT private_account_card.card_id FROM private_account_card, private_user WHERE private_user.afm='" + clientAfm + "'"
                                + " AND private_user.priv_user_id=private_account_card.priv_user_id)";
                        System.out.println(sqlUpdatePrivateUserDebt);
                        stmt.executeUpdate(sqlUpdatePrivateUserDebt);

                        String sqlInsertTransaction = "INSERT INTO ccc_transaction (transc_type, amount, card_id, merch_acc_id)"
                                + " VALUES ('DEBIT'," + purchaseAmount + ", " + cardId + ", " + merchantId + ")";
                        System.out.println(sqlInsertTransaction);
                        stmt.executeUpdate(sqlInsertTransaction);

                        String sqlUpdateMerchAcc = "UPDATE merchant_account SET "
                                + " debt_to_ccc = debt_to_ccc + supply_to_ccc/100 * " + purchaseAmount
                                + " WHERE merch_acc_id=" + this.getMerchantAccount(merchantId);
                        System.out.println(sqlUpdateMerchAcc);
                        stmt.executeUpdate(sqlUpdateMerchAcc);
                        conn.commit();
                    } else {
                        throw new Exception("Card number could not be found in the database.");
                    }
                }



            } else {
                throw new Exception("No card with such number was found in the database.");
            }
        } catch (SQLException sqlExc) {
            try {
                conn.rollback();
            } catch (SQLException rollbackExc) {
                Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, rollbackExc);
            }
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, sqlExc);
        } catch (Exception exc) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, exc);
        }
        return false;
    }

    public void returnProduct(int transactionId) {
        String sqlQueryTransc = "SELECT transc_type, amount, date_time, card_id, merch_acc_id FROM ccc_transaction WHERE transc_id=" + transactionId;
        System.out.println(sqlQueryTransc);
        try {
            String transcDatetime;
            int cardId, merchAccId, amount;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQueryTransc);
            if (rs.next()) {
                if (rs.getString(1).equalsIgnoreCase("CREDIT")) {
                    throw new Exception("Trying to create credit for credit transaction.");
                }
                amount = rs.getInt(2);
                transcDatetime = rs.getString(3);
                cardId = rs.getInt(4);
                merchAccId = rs.getInt(5);

            } else {
                throw new Exception("Invalid transaction id.");
            }

            conn.setAutoCommit(false);

            String insCreditTransaction = "INSERT INTO ccc_transaction (transc_type, amount, date_time, card_id, merch_acc_id, related_transc_id) "
                    + " VALUES ('CREDIT', " + amount + ", '" + transcDatetime + "', " + cardId + ", " + merchAccId + ", " + transactionId + ") ";
            System.out.println(insCreditTransaction);

            PreparedStatement prepedStmt = conn.prepareStatement(insCreditTransaction, Statement.RETURN_GENERATED_KEYS);
            prepedStmt.executeUpdate();
            rs = prepedStmt.getGeneratedKeys();
            rs.next();
            int creditTranscId = rs.getInt(1);

            String updCreditCard = "UPDATE credit_card SET "
                    + " credit_balance=credit_balance+" + amount + ", "
                    + " debt_amount=debt_amount-" + amount
                    + " WHERE card_id=" + cardId;
            System.out.println(updCreditCard);
            stmt.executeUpdate(updCreditCard);

            String updMerchantAccount = "UPDATE merchant_account SET "
                    + " debt_to_ccc=debt_to_ccc - (supply_to_ccc/100)*" + amount
                    + " WHERE merch_acc_id=" + merchAccId;
            System.out.println(updMerchantAccount);
            stmt.executeUpdate(updMerchantAccount);

            /*Check if it is employee transaction and insert new row for the credit one in employee_transactio table*/
            String sqlQueryEmplTransc = "SELECT emp_id FROM employee_transaction WHERE transc_id=" + transactionId;
            System.out.println(sqlQueryEmplTransc);
            rs = stmt.executeQuery(sqlQueryEmplTransc);
            if (rs.next()) {
                int employeeId = rs.getInt(1);
                String insEmplTransc = "INSERT INTO employee_transaction (card_id, transc_id, emp_id) VALUES (" + cardId + ", " + creditTranscId + ", " + employeeId + ")";
                System.out.println(insEmplTransc);
                stmt.executeUpdate(insEmplTransc);
            }

            conn.commit();
        } catch (SQLException sqlExc) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, ex);
            }
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, sqlExc);
        } catch (Exception exc) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, ex);
            }
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, exc);
        }

    }

    public String getMerchantTransactionsStr(String merchantAfm, String startDate, String endDate) {
        String strTransactions = "";
        ResultSet rs = this.getMerchantTransactions(merchantAfm, startDate, endDate);
        try {
            while (rs.next()) {
                strTransactions += rs.getString(3) + " " + rs.getString(1) + " " + String.valueOf(rs.getInt(2)) + "\n";
            }
        } catch (SQLException ex) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, ex);
        }
        return strTransactions;
    }

    public ResultSet getMerchantTransactions(String merchantAfm, String startDate, String endDate) {
        String sqlQueryTransc = "SELECT ccc_transaction.transc_type, ccc_transaction.amount, ccc_transaction.date_time "
                + " FROM merchant_user, ccc_transaction, merchant_account "
                + " WHERE merchant_user.afm='" + merchantAfm + "'"
                + " AND merchant_user.merch_acc_id=merchant_account.merch_acc_id"
                + " AND merchant_account.merch_acc_id=ccc_transaction.merch_acc_id"
                + " AND ccc_transaction.date_time >= '" + startDate + " 00:00:00'"
                + " AND ccc_transaction.date_time <= '" + endDate + " 23:59:59'";
        System.out.println(sqlQueryTransc);
        ResultSet rs;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlQueryTransc);
            return rs;
        } catch (SQLException sqlExc) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, sqlExc);
        }
        return null;
    }

    public String getPrivateUserTransactionsStr(String cardNumber, String startDate, String endDate) {
        String strTransactions = "";
        ResultSet rs = this.getPrivateUserTransactions(cardNumber, startDate, endDate);
        try {
            while (rs.next()) {
                strTransactions += rs.getString(3) + " " + rs.getString(1) + " " + String.valueOf(rs.getInt(2)) + "\n";
            }
        } catch (SQLException ex) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, ex);
        }
        return strTransactions;
    }

    public ResultSet getPrivateUserTransactions(String cardNumber, String startDate, String endDate) {
        String sqlQueryTransc = "SELECT ccc_transaction.transc_type, ccc_transaction.amount, ccc_transaction.date_time"
                + " FROM credit_card, ccc_transaction"
                + " WHERE credit_card.card_number='" + cardNumber + "'"
                + " AND credit_card.card_id=ccc_transaction.card_id"
                + " AND ccc_transaction.date_time >= '" + startDate + " 00:00:00'"
                + " AND ccc_transaction.date_time <= '" + endDate + " 23:59:59'";
        System.out.println(sqlQueryTransc);
        ResultSet rs;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlQueryTransc);
            return rs;
        } catch (SQLException sqlExc) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, sqlExc);
        }
        return null;
    }

    public String getPrivateUserTransactionsStr(String cardNumber, String startDate, String endDate, String transactionType) {
        String strTransactions = "";
        ResultSet rs = this.getPrivateUserTransactions(cardNumber, startDate, endDate, transactionType);
        try {
            while (rs.next()) {
                strTransactions += rs.getString(3) + " " + rs.getString(1) + " " + String.valueOf(rs.getInt(2)) + "\n";
            }
        } catch (SQLException ex) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, ex);
        }
        return strTransactions;
    }

    public ResultSet getPrivateUserTransactions(String cardNumber, String startDate, String endDate, String transactionType) {
        transactionType = transactionType.toUpperCase();
        String sqlQueryTransc = "SELECT ccc_transaction.transc_type, ccc_transaction.amount, ccc_transaction.date_time"
                + " FROM credit_card, ccc_transaction"
                + " WHERE credit_card.card_number='" + cardNumber + "'"
                + " AND credit_card.card_id=ccc_transaction.card_id"
                + " AND ccc_transaction.date_time >= '" + startDate + " 00:00:00'"
                + " AND ccc_transaction.date_time <= '" + endDate + " 23:59:59'"
                + " AND ccc_transaction.transc_type = '" + transactionType + "'";
        System.out.println(sqlQueryTransc);
        ResultSet rs;
        try {
            if (!transactionType.equalsIgnoreCase("DEBIT") && !transactionType.equalsIgnoreCase("CREDIT")) {
                throw new Exception("Invalid transaction type given. Please specify DEBIT or CREDIT");
            }
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlQueryTransc);
            return rs;
        } catch (SQLException sqlExc) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, sqlExc);
        } catch (Exception ex) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getCompanyTransactionsStr(String cardNumber, String employeeAfm, String startDate, String endDate) {
        String strTransactions = "";
        ResultSet rs = this.getCompanyTransactions(cardNumber, employeeAfm, startDate, endDate);
        try {
            while (rs.next()) {
                strTransactions += rs.getString(3) + " " + rs.getString(1) + " " + String.valueOf(rs.getInt(2)) + " "
                        + rs.getString(4) + " " + rs.getString(5) + " " + rs.getString(6) + "\n";
            }
        } catch (SQLException ex) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, ex);
        }
        return strTransactions;
    }

    public String getCompanyTransactionsStr(String cardNumber, String startDate, String endDate) {
        String strTransactions = "";
        ResultSet rs = this.getCompanyTransactions(cardNumber, startDate, endDate);
        try {
            while (rs.next()) {
                strTransactions += rs.getString(3) + " " + rs.getString(1) + " " + String.valueOf(rs.getInt(2)) + " "
                        + rs.getString(4) + " " + rs.getString(5) + " " + rs.getString(6) + "\n";
            }
        } catch (SQLException ex) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, ex);
        }
        return strTransactions;
    }

    public String getCompanyHighTransactionsStr(String cardNumber, int limit, String startDate, String endDate) {
        String strTransactions = "";
        ResultSet rs = this.getCompanyHighTransactions(cardNumber, limit, startDate, endDate);
        try {
            while (rs.next()) {
                strTransactions += rs.getString(3) + " " + rs.getString(1) + " " + String.valueOf(rs.getInt(2)) + " "
                        + rs.getString(4) + " " + rs.getString(5) + " " + rs.getString(6) + "\n";
            }
        } catch (SQLException ex) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, ex);
        }
        return strTransactions;
    }

    public ResultSet getCompanyHighTransactions(String cardNumber, int limit, String startDate, String endDate) {
        String sqlQueryTransc = this.sqlQueryCompanyTransactions(cardNumber, startDate, endDate);
        sqlQueryTransc += " AND ccc_transaction.amount >= " + limit;
        System.out.println(sqlQueryTransc);
        ResultSet rs;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlQueryTransc);
            return rs;
        } catch (SQLException sqlExc) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, sqlExc);
        }
        return null;
    }

    public ResultSet getCompanyTransactions(String cardNumber, String employeeAfm, String startDate, String endDate) {
        String sqlQueryTransc = this.sqlQueryCompanyTransactions(cardNumber, startDate, endDate);
        sqlQueryTransc += " AND employee.afm='" + employeeAfm + "'";
        System.out.println(sqlQueryTransc);
        ResultSet rs;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlQueryTransc);
            return rs;
        } catch (SQLException sqlExc) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, sqlExc);
        }
        return null;
    }

    public ResultSet getCompanyTransactions(String cardNumber, String startDate, String endDate) {
        String sqlQueryTransc = this.sqlQueryCompanyTransactions(cardNumber, startDate, endDate);
        System.out.println(sqlQueryTransc);
        ResultSet rs;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlQueryTransc);
            return rs;
        } catch (SQLException sqlExc) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, sqlExc);
        }
        return null;
    }

    public String getGoodPayersStr() {
        String goodPayersReport = "";
        ResultSet rs = this.getGoodPayers();
        try {
            while (rs.next()) {
                goodPayersReport += rs.getString(1) + "\t" + rs.getString(3) + "\t" + rs.getString(2) + "\n";
            }
        } catch (SQLException ex) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, ex);
        }
        return goodPayersReport;
    }

    public ResultSet getGoodPayers() {
        ResultSet rs = null;
        try {
            String sqlQuery = "SELECT merchant_user.afm, CONCAT(merchant_user.firstname, ' ', merchant_user.surname, ' ',merchant_user.fathername) AS name, 'MERCHANT USER' AS type"
                    + " FROM merchant_user, merchant_account WHERE merchant_account.debt_to_ccc = 0 "
                    + " AND merchant_user.merch_acc_id=merchant_account.merch_acc_id "
                    + " UNION "
                    + " SELECT company.afm, company.name AS name, 'COMPANY USER' AS type  FROM company, company_account_card, credit_card "
                    + " WHERE credit_card.debt_amount <= 0 "
                    + " AND credit_card.card_id=company_account_card.card_id AND company_account_card.company_id=company.company_id"
                    + " UNION "
                    + " SELECT private_user.afm, CONCAT(private_user.firstname, ' ', private_user.surname, ' ', private_user.fathername) AS name, 'PRIVATE USER' AS type FROM private_user, private_account_card, credit_card "
                    + " WHERE credit_card.debt_amount <= 0 "
                    + " AND credit_card.card_id=private_account_card.card_id AND private_account_card.priv_user_id=private_user.priv_user_id";
            System.out.println(sqlQuery);
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlQuery);
        } catch (SQLException ex) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public String getBadPayersStr() {
        String goodPayersReport = "DEBT\tAFM\tUSER TYPE\tNAME\n";
        ResultSet rs = this.getBadPayers();
        try {
            while (rs.next()) {
                goodPayersReport += rs.getString(4) + "\t" + rs.getString(1) + "\t" + rs.getString(3) + "\t" + rs.getString(2) + "\n";
            }
        } catch (SQLException ex) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, ex);
        }
        return goodPayersReport;
    }

    public ResultSet getBadPayers() {
        ResultSet rs = null;
        try {
            String sqlQuery = "SELECT merchant_user.afm, CONCAT(merchant_user.firstname, ' ', merchant_user.surname, ' ',merchant_user.fathername) AS name,"
                    + " 'MERCHANT USER' AS type, merchant_account.debt_to_ccc AS DEBT"
                    + " FROM merchant_user, merchant_account WHERE merchant_account.debt_to_ccc > 0 "
                    + " AND merchant_user.merch_acc_id=merchant_account.merch_acc_id "
                    + " UNION "
                    + " SELECT company.afm, company.name AS name, 'COMPANY USER' AS type, credit_card.debt_amount AS DEBT  "
                    + " FROM company, company_account_card, credit_card "
                    + " WHERE credit_card.debt_amount > 0 "
                    + " AND credit_card.card_id=company_account_card.card_id AND company_account_card.company_id=company.company_id"
                    + " UNION "
                    + " SELECT private_user.afm, CONCAT(private_user.firstname, ' ', private_user.surname, ' ', "
                    + " private_user.fathername) AS name, 'PRIVATE USER' AS type, credit_card.debt_amount "
                    + " FROM private_user, private_account_card, credit_card "
                    + " WHERE credit_card.debt_amount > 0 "
                    + " AND credit_card.card_id=private_account_card.card_id AND private_account_card.priv_user_id=private_user.priv_user_id"
                    + " ORDER BY DEBT";
            System.out.println(sqlQuery);
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlQuery);
        } catch (SQLException ex) {
            Logger.getLogger(CCCProcesses.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    private String sqlQueryCompanyTransactions(String cardNumber, String startDate, String endDate) {
        return "SELECT ccc_transaction.transc_type, ccc_transaction.amount, ccc_transaction.date_time, employee.firstname, employee.surname, employee.fathername"
                + " FROM credit_card, ccc_transaction, employee, employee_transaction "
                + " WHERE credit_card.card_number='" + cardNumber + "'"
                + " AND ccc_transaction.date_time >= '" + startDate + " 00:00:00'"
                + " AND ccc_transaction.date_time <= '" + endDate + " 23:59:59'"
                + " AND credit_card.card_id=ccc_transaction.card_id"
                + " AND credit_card.card_id=employee_transaction.card_id"
                + " AND employee.emp_id=employee_transaction.emp_id"
                + " AND employee_transaction.transc_id=ccc_transaction.transc_id";
    }

    private int getMerchantAccount(int merchantId) throws Exception {
        Statement stmt = conn.createStatement();
        ResultSet rs;
        String sqlQueryMerchant = "SELECT merch_acc_id FROM merchant_user WHERE merchant_user.merch_user_id=" + merchantId;
        rs = stmt.executeQuery(sqlQueryMerchant);
        int accountId;
        if (rs.next()) {
            accountId = rs.getInt(1);
        } else {
            throw new Exception("Database error. No merchant with the given AFM was found.");
        }
        return accountId;
    }

    private boolean cardNumberIsValid(String cardNumber) {
        if (cardNumber.length() != 16 || !cardNumber.matches("[0-9]+")) {
            return false;
        }
        return true;
    }
}
