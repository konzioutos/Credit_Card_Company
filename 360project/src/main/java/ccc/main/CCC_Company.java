/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccc.main;

import java.sql.Connection;
import ccc.db.DBConnection;
import ccc.processes.CCCProcesses;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author KonZioutos
 */
public class CCC_Company {

    public static void main(String args[]) {
        try {
            Connection conn = DBConnection.getInitialConnection();
            /* *********************** Database creation ************************* */

            //InitDatabase dbinit = new InitDatabase();

            /**
             * *********************************** DROP *********************************
             */
            //dbinit.dropDatabase(conn);

            /* ***************************************************************************
             */

//            dbinit.createDatabase(conn);
//
//            conn = DBConnection.getConnection();
//            dbinit.createTables(conn);
//            dbinit.insertInitialData(conn);



            CCCProcesses prs = new CCCProcesses();

            /* EMPOROS TOY MHNA */
            //System.out.println(prs.getMerchantOfMonth(10, 2021).toString());
            /*create account-user*/
            // PrivateUser privUser = new PrivateUser("Konstantinos", "Zioutos", "George", "0234570002");
            //prs.createAccount(privUser, "0032010131200232");
//
//            Merchant merch = new Merchant("Konstantinos", "Μathioudakis", "Nikos", "0234560003");
//            prs.createAccount(merch);
//
//            Company comp = new Company("Nike", "0234560054");
//            prs.createAccount(comp, "1032010131200239");
           // prs.closeAccount("1234560012");

            // prs.payDebt("1234560007", 10, PaymentType.MERCHANT_PAYMENT);
            //prs.payDebt("1234560011", 100, PaymentType.CARD_USER_PAYMENT);
//            prs.purchase("1000000011111009", "1234560003", "1234560008", 100);
            //prs.purchase("1000000011111002", "1234560010", "1234560008", 100);
//            System.out.println(prs.getMerchantTransactionsStr("1234560008", "2022-01-18", "2022-01-24"));
//            System.out.println(prs.getPrivateUserTransactionsStr("1000000011111000", "2021-01-01", "2022-01-17"));
//            System.out.println(prs.getCompanyTransactionsStr("1000000011111011", "2021-01-01", "2023-01-17"));
//            prs.purchase("1000000011111013", "1234560000", "1234560008", 100);
//            prs.purchase("1000000011111002", "1234560010", "1234560008", 200);

            //prs.returnProduct(20);
//            System.out.println(prs.getBadPayersStr());
//            System.out.println(prs.getGoodPayersStr());
//            System.out.println(prs.getCompanyHighTransactionsStr("1000000011111010", 50, "2021-01-01", "2023-01-17"));
//            System.out.println(prs.getPrivateUserTransactionsStr("1000000011111001", "2021-01-01", "2022-01-17", "CREDIT"));

//Merchant merch = new Merchant();
//            User user = new PrivateUser(1, "Maria", "Stamati", "Giorgos");
//
//            prs.createAccount(user);

//            Statement stmt = conn.createStatement();
//
//            ResultSet rs;
//            rs = stmt.executeQuery("SELECT * FROM employee");
//            while (rs.next()) {
//                System.out.print(rs.getString(2) + " ");
//                System.out.println(rs.getString(3));
//            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CCC_Company.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CCC_Company.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CCC_Company.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
