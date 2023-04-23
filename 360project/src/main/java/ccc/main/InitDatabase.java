/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccc.main;

import ccc.db.DBConnection;
import java.sql.Connection;
import java.sql.Statement;

/**
 *
 * @author KonZioutos
 */
public class InitDatabase {

    public void createDatabase(Connection conn) throws Exception {
        if (conn == null) {
            throw new Exception("Db connection problem.");
        }
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE DATABASE IF NOT EXISTS " + DBConnection.getDatabaseName() + " CHARACTER SET utf8 COLLATE utf8_general_ci");
        stmt.close();
    }

    public void dropDatabase(Connection conn) throws Exception {
        if (conn == null) {
            throw new Exception("Db connection problem.");
        }
        Statement stmt = conn.createStatement();
        stmt.execute("DROP DATABASE " + DBConnection.getDatabaseName());
        stmt.close();
    }

    public void createTables(Connection conn) throws Exception {
        if (conn == null) {
            throw new Exception("Db connection problem.");
        }
        Statement stmt = conn.createStatement();
        String createTableCmd = "CREATE TABLE IF NOT EXISTS merchant_account ( "
                + " merch_acc_id INTEGER AUTO_INCREMENT COMMENT 'primary key', "
                + " acc_balance INTEGER NOT NULL DEFAULT 0  COMMENT 'balance in cents',"
                + " debt_to_ccc INTEGER NOT NULL DEFAULT 0 COMMENT 'debt to ccc in cents',"
                + " supply_to_ccc TINYINT NOT NULL DEFAULT 0 COMMENT 'percentage given to ccc for eache transaction',"
                + " acc_closed INTEGER NOT NULL DEFAULT 0 COMMENT '0 if the account is not closed, 1 if it is closed',"
                + " PRIMARY KEY (merch_acc_id))";
        stmt.execute(createTableCmd);


        createTableCmd = "CREATE TABLE IF NOT EXISTS private_account ("
                + " priv_acc_id INTEGER AUTO_INCREMENT COMMENT 'primary key', "
                + " acc_balance INTEGER NOT NULL DEFAULT 0 COMMENT   'balance in cents',"
                + " acc_closed INTEGER NOT NULL DEFAULT 0 COMMENT   '0 if the account is not closed, 1 if it is closed',"
                + " PRIMARY KEY (priv_acc_id))";
        stmt.execute(createTableCmd);

        createTableCmd = "CREATE TABLE IF NOT EXISTS company_account ("
                + " comp_acc_id INTEGER AUTO_INCREMENT COMMENT 'primary key',"
                + " acc_balance INTEGER NOT NULL DEFAULT 0  COMMENT   'balance in cents',"
                + " acc_closed INTEGER NOT NULL DEFAULT 0 COMMENT   '0 if the account is not closed, 1 if it is closed',"
                + " PRIMARY KEY (comp_acc_id))";
        stmt.execute(createTableCmd);

        createTableCmd = "CREATE TABLE IF NOT EXISTS company ("
                + " company_id INTEGER AUTO_INCREMENT COMMENT 'primary key',"
                + " name VARCHAR(512) NOT NULL,"
                + " afm VARCHAR(10) NOT NULL,"
                + " PRIMARY KEY(company_id),"
                + " UNIQUE KEY (afm))";
        stmt.execute(createTableCmd);

        createTableCmd = "CREATE TABLE IF NOT EXISTS employee ("
                + " emp_id INTEGER AUTO_INCREMENT COMMENT 'primary key',"
                + " firstname VARCHAR(128) NOT NULL,"
                + " surname VARCHAR(128) NOT NULL,"
                + " fathername VARCHAR(128) NOT NULL,"
                + " company_id INTEGER NOT NULL,"
                + " afm INTEGER NOT NULL,"
                + " PRIMARY KEY(emp_id),"
                + " FOREIGN KEY (company_id) REFERENCES company(company_id) ON UPDATE RESTRICT ON DELETE RESTRICT,"
                + " UNIQUE KEY(afm))";
        stmt.execute(createTableCmd);

        createTableCmd = "CREATE TABLE IF NOT EXISTS merchant_user ("
                + " merch_user_id INTEGER AUTO_INCREMENT COMMENT'primary key',"
                + " firstname VARCHAR(128) NOT NULL,"
                + " surname VARCHAR(128) NOT NULL,"
                + " fathername VARCHAR(128) NOT NULL,"
                + " merch_acc_id INTEGER,"
                + " afm VARCHAR(10) NOT NULL,"
                + " PRIMARY KEY(merch_user_id),"
                + " FOREIGN KEY (merch_acc_id) REFERENCES merchant_account(merch_acc_id) ON UPDATE RESTRICT ON DELETE RESTRICT,"
                + " UNIQUE KEY(afm))";
        stmt.execute(createTableCmd);

        createTableCmd = "CREATE TABLE IF NOT EXISTS private_user("
                + " priv_user_id INTEGER AUTO_INCREMENT COMMENT'primary key',"
                + " firstname VARCHAR(128) NOT NULL,"
                + " surname VARCHAR(128) NOT NULL,"
                + " fathername VARCHAR(128) NOT NULL,"
                + " afm VARCHAR(10) NOT NULL,"
                + " PRIMARY KEY(priv_user_id),"
                + " UNIQUE KEY (afm))";
        stmt.execute(createTableCmd);

        createTableCmd = "CREATE TABLE IF NOT EXISTS credit_card ("
                + " card_id INTEGER AUTO_INCREMENT COMMENT 'primary key',"
                + " credit_limit INTEGER NOT NULL DEFAULT 0,"
                + " credit_balance INTEGER NOT NULL DEFAULT 0,"
                + " expiration_date DATE NOT NULL,"
                + " debt_amount INTEGER NOT NULL DEFAULT 0,"
                + " card_number VARCHAR(16) NOT NULL,"
                + " UNIQUE KEY(card_number),"
                + " PRIMARY KEY (card_id))";
        stmt.execute(createTableCmd);

        createTableCmd = "CREATE TABLE IF NOT EXISTS ccc_transaction("
                + " transc_id INTEGER AUTO_INCREMENT COMMENT  'primary key',"
                + " transc_type VARCHAR(128) NOT NULL,"
                + " amount INTEGER NOT NULL DEFAULT 0,"
                + " date_time TIMESTAMP NOT NULL,"
                + " card_id INTEGER NOT NULL,"
                + " merch_acc_id INTEGER NOT NULL,"
                + " related_transc_id INTEGER DEFAULT NULL,"
                + " PRIMARY KEY (transc_id),"
                + " FOREIGN KEY (card_id) REFERENCES credit_card(card_id) ON UPDATE RESTRICT ON DELETE RESTRICT,"
                + " FOREIGN KEY(merch_acc_id) REFERENCES merchant_account(merch_acc_id) ON UPDATE RESTRICT ON DELETE RESTRICT,"
                + " FOREIGN KEY(related_transc_id) REFERENCES ccc_transaction(transc_id) ON UPDATE RESTRICT ON DELETE RESTRICT,"
                + " UNIQUE KEY(related_transc_id))";
        stmt.execute(createTableCmd);

        createTableCmd = "CREATE TABLE IF NOT EXISTS private_account_card("
                + " card_id INTEGER,"
                + " priv_acc_id INTEGER ,"
                + " priv_user_id INTEGER,"
                + " PRIMARY KEY(card_id, priv_acc_id, priv_user_id),"
                + " FOREIGN KEY (card_id) REFERENCES credit_card(card_id) ON UPDATE RESTRICT ON DELETE RESTRICT,"
                + " FOREIGN KEY(priv_user_id) REFERENCES private_user(priv_user_id) ON UPDATE RESTRICT ON DELETE RESTRICT,"
                + " FOREIGN KEY(priv_acc_id) REFERENCES private_account(priv_acc_id) ON UPDATE RESTRICT ON DELETE RESTRICT)";
        stmt.execute(createTableCmd);

        createTableCmd = "CREATE TABLE IF NOT EXISTS company_account_card("
                + " card_id INTEGER,"
                + " comp_acc_id INTEGER ,"
                + " company_id INTEGER,"
                + " PRIMARY KEY(card_id, comp_acc_id, company_id),"
                + " FOREIGN KEY (card_id) REFERENCES credit_card(card_id) ON UPDATE RESTRICT ON DELETE RESTRICT,"
                + " FOREIGN KEY(company_id) REFERENCES company(company_id) ON UPDATE RESTRICT ON DELETE RESTRICT,"
                + " FOREIGN KEY(comp_acc_id) REFERENCES company_account(comp_acc_id) ON UPDATE RESTRICT ON DELETE RESTRICT)";
        stmt.execute(createTableCmd);

        createTableCmd = "CREATE TABLE IF NOT EXISTS employee_transaction("
                + " card_id INTEGER,"
                + " transc_id INTEGER,"
                + " emp_id INTEGER,"
                + " PRIMARY KEY(card_id, transc_id, emp_id),"
                + " FOREIGN KEY (card_id) REFERENCES credit_card(card_id) ON UPDATE RESTRICT ON DELETE RESTRICT,"
                + " FOREIGN KEY(transc_id) REFERENCES ccc_transaction(transc_id) ON UPDATE RESTRICT ON DELETE RESTRICT,"
                + " FOREIGN KEY(emp_id) REFERENCES employee(emp_id) ON UPDATE RESTRICT ON DELETE RESTRICT)";
        stmt.execute(createTableCmd);
        stmt.close();
    }

    public void insertInitialData(Connection conn) throws Exception {
        if (conn == null) {
            throw new Exception("Db connection problem.");
        }

        Statement stmt = conn.createStatement();
        String insCmd = "INSERT INTO merchant_account(merch_acc_id,acc_balance,debt_to_ccc,supply_to_ccc,acc_closed)\n"
                + "VALUES(1,100,20,10,0),\n"
                + "(2,200,50,5,0),\n"
                + "(3,500,100,15,0),\n"
                + "(4,700,200,20,0),\n"
                + "(5,0,0,0,1)";
        stmt.executeUpdate(insCmd);

        stmt = conn.createStatement();
        insCmd = "INSERT INTO private_account(priv_acc_id,acc_balance,acc_closed)\n"
                + "VALUES(1,100,0),\n"
                + "(2,200,0),\n"
                + "(3,300,0),\n"
                + "(4,500,0),\n"
                + "(5,0,0)";
        stmt.executeUpdate(insCmd);

        insCmd = "INSERT INTO company_account(comp_acc_id,acc_balance,acc_closed)\n"
                + "VALUES(1,0,1),\n"
                + "(2,800,0),\n"
                + "(3,900,0),\n"
                + "(4,300,0),\n"
                + "(5,500,0)";
        stmt.executeUpdate(insCmd);

        insCmd = "INSERT INTO company(company_id,name, afm)\n"
                + "VALUES(1,\"GOOGLE\",\"1234560015\"),\n"
                + "(2,\"AMAZON\",\"1234560016\"),\n"
                + "(3,\"TESLA\",\"1234560017\"),\n"
                + "(4,\"FACEBOOK\",\"1234560018\"),\n"
                + "(5,\"APPLE\",\"1234560019\")";
        stmt.executeUpdate(insCmd);

        insCmd = "INSERT INTO employee(emp_id,firstname,surname,fathername,company_id,afm)\n"
                + "VALUES(1,\"Kostas\",\"Papadopoulos\",\"Giorgos\",2,\"1234560000\"),\n"
                + "(2,\"Giorgos\",\"Papadakis\",\"Nikos\",3,\"1234560001\"),\n"
                + "(3,\"Giannis\",\"Georgiou\",\"Mixalis\",1,\"1234560002\"),\n"
                + "(4,\"Vasilis\",\"Ioannou\",\"Manolis\",4,\"1234560003\"),\n"
                + "(5,\"Tasos\",\"Milonas\",\"Kostas\",5,\"1234560004\")";
        stmt.executeUpdate(insCmd);

        insCmd = "INSERT INTO merchant_user(merch_user_id,firstname,surname,fathername,merch_acc_id, afm)\n"
                + "VALUES(1,\"Nikos\",\"Galanis\",\"Giorgos\",2,\"1234560005\"),\n"
                + "(2,\"Petros\",\"Fragkiadakis\",\"Nikos\",5,\"1234560006\"),\n"
                + "(3,\"Vaso\",\"Petrou\",\"Mixalis\",1,\"1234560007\"),\n"
                + "(4,\"Anna\",\"Pappa\",\"Manolis\",4,\"1234560008\"),\n"
                + "(5,\"Dimitra\",\"Nikolaou\",\"Kostas\",1,\"1234560009\")";
        stmt.executeUpdate(insCmd);

        insCmd = "INSERT INTO private_user(priv_user_id,firstname,surname,fathername, afm)\n"
                + "VALUES(1,\"Maria\",\"Stamati\",\"Giorgos\",\"1234560010\"),\n"
                + "(2,\"Xristos\",\"Paulou\",\"Nikos\",\"1234560011\"),\n"
                + "(3,\"Sofia\",\"Kara\",\"Mixalis\",\"1234560012\"),\n"
                + "(4,\"Dimitris\",\"Karalis\",\"Manolis\",\"1234560013\"),\n"
                + "(5,\"Mixalis\",\"Makris\",\"Kostas\",\"1234560014\")";
        stmt.executeUpdate(insCmd);

        insCmd = "INSERT INTO credit_card(card_id,credit_limit,credit_balance,expiration_date,debt_amount, card_number)\n"
                + "VALUES(1,1000,800,\"2025-11-04\",200, \"1000000011111000\"),\n"
                + "(2,1000,900,\"2023-01-14\",100, \"1000000011111014\"),\n"
                + "(3,1000,700,\"2024-03-24\",300, \"1000000011111001\"),\n"
                + "(4,1000,950,\"2022-05-06\",50, \"1000000011111002\"),\n"
                + "(5,1000,100,\"2025-04-12\",0, \"1000000011111003\"),\n"
                + "(6,5000,300,\"2025-10-07\",100, \"1000000011111009\"),\n"
                + "(7,5000,4500,\"2026-12-19\",500, \"1000000011111010\"),\n"
                + "(8,5000,4800,\"2027-01-06\",200, \"1000000011111011\"),\n"
                + "(9,5000,3000,\"2025-02-06\",2000, \"1000000011111012\"),\n"
                + "(10,5000,5000,\"2023-05-03\",0, \"1000000011111013\")\n";
        stmt.executeUpdate(insCmd);

        insCmd = "INSERT INTO ccc_transaction(transc_id,transc_type,amount,date_time,card_id,merch_acc_id)\n"
                + "VALUES(1,\"DEBIT\",100,\"2021-10-02\",1,4),\n"
                + "(2,\"DEBIT\",200,\"2021-01-12\",3,5),\n"
                + "(3,\"CREDIT\",200,\"2021-04-22\",3,5),\n"
                + "(4,\"DEBIT\",50,\"2021-04-09\",4,2),\n"
                + "(5,\"DEBIT\",100,\"2021-04-28\",4,3),\n"
                + "(6,\"DEBIT\",200,\"2021-01-12\",6,5),\n"
                + "(7,\"DEBIT\",300,\"2021-06-22\",7,1),\n"
                + "(8,\"DEBIT\",50,\"2021-07-09\",10,2),\n"
                + "(9,\"DEBIT\",100,\"2021-04-27\",9,3), \n"
                + "(10,\"DEBIT\",200,\"2021-01-09\",8,5),\n"
                + "(11,\"DEBIT\",300,\"2021-06-02\",7,1),\n"
                + "(12,\"DEBIT\",50,\"2021-07-09\",6,2),\n"
                + "(13,\"DEBIT\",100,\"2021-04-08\",5,3)";
        stmt.executeUpdate(insCmd);

        insCmd = "INSERT INTO private_account_card(card_id,priv_acc_id,priv_user_id)\n"
                + "VALUES(1,3,4),\n"
                + "(2,1,2),\n"
                + "(3,4,5),\n"
                + "(4,5,1),\n"
                + "(5,2,3)";
        stmt.executeUpdate(insCmd);

        insCmd = "INSERT INTO company_account_card(card_id,comp_acc_id,company_id)\n"
                + "VALUES(6,3,4),\n"
                + "(7,1,2),\n"
                + "(8,4,5),\n"
                + "(9,5,1),\n"
                + "(10,2,3)";
        stmt.executeUpdate(insCmd);

        insCmd = "INSERT INTO employee_transaction(card_id,transc_id,emp_id)\n"
                + "VALUES(6,6,1),\n"
                + "(7,7,2),\n"
                + "(10,8,5),\n"
                + "(9,9,4),\n"
                + "(8,10,3),\n"
                + "(9,9,1),\n"
                + "(7,11,4),\n"
                + "(6,12,3)";
        stmt.executeUpdate(insCmd);




    }
}
