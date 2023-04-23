-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 23, 2022 at 06:47 PM
-- Server version: 10.4.22-MariaDB
-- PHP Version: 8.1.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `project_hy360_database`
--

-- --------------------------------------------------------

--
-- Table structure for table `ccc_transaction`
--

CREATE TABLE `ccc_transaction` (
  `transc_id` int(11) NOT NULL COMMENT 'primary key',
  `transc_type` varchar(128) NOT NULL,
  `amount` int(11) NOT NULL DEFAULT 0,
  `date_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `card_id` int(11) NOT NULL,
  `merch_acc_id` int(11) NOT NULL,
  `related_transc_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ccc_transaction`
--

INSERT INTO `ccc_transaction` (`transc_id`, `transc_type`, `amount`, `date_time`, `card_id`, `merch_acc_id`, `related_transc_id`) VALUES
(1, 'DEBIT', 100, '2021-10-01 21:00:00', 1, 4, NULL),
(2, 'DEBIT', 200, '2021-01-11 22:00:00', 3, 5, NULL),
(3, 'CREDIT', 200, '2021-04-21 21:00:00', 3, 5, NULL),
(4, 'DEBIT', 50, '2021-04-08 21:00:00', 4, 2, NULL),
(5, 'DEBIT', 100, '2021-04-27 21:00:00', 4, 3, NULL),
(6, 'DEBIT', 200, '2021-01-11 22:00:00', 6, 5, NULL),
(7, 'DEBIT', 200, '2022-01-23 16:34:39', 7, 1, NULL),
(8, 'DEBIT', 50, '2021-07-08 21:00:00', 10, 2, NULL),
(9, 'DEBIT', 100, '2021-04-26 21:00:00', 9, 3, NULL),
(10, 'DEBIT', 200, '2021-01-08 22:00:00', 8, 5, NULL),
(11, 'DEBIT', 300, '2021-06-01 21:00:00', 7, 1, NULL),
(12, 'DEBIT', 50, '2021-07-08 21:00:00', 6, 2, NULL),
(13, 'DEBIT', 100, '2021-04-07 21:00:00', 5, 3, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `company`
--

CREATE TABLE `company` (
  `company_id` int(11) NOT NULL COMMENT 'primary key',
  `name` varchar(512) NOT NULL,
  `afm` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `company`
--

INSERT INTO `company` (`company_id`, `name`, `afm`) VALUES
(1, 'GOOGLE', '1234560015'),
(2, 'AMAZON', '1234560016'),
(3, 'TESLA', '1234560017'),
(4, 'FACEBOOK', '1234560018'),
(5, 'APPLE', '1234560019');

-- --------------------------------------------------------

--
-- Table structure for table `company_account`
--

CREATE TABLE `company_account` (
  `comp_acc_id` int(11) NOT NULL COMMENT 'primary key',
  `acc_balance` int(11) NOT NULL DEFAULT 0 COMMENT 'balance in cents',
  `acc_closed` int(11) NOT NULL DEFAULT 0 COMMENT '0 if the account is not closed, 1 if it is closed'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `company_account`
--

INSERT INTO `company_account` (`comp_acc_id`, `acc_balance`, `acc_closed`) VALUES
(1, 0, 1),
(2, 800, 0),
(3, 900, 0),
(4, 300, 0),
(5, 500, 0);

-- --------------------------------------------------------

--
-- Table structure for table `company_account_card`
--

CREATE TABLE `company_account_card` (
  `card_id` int(11) NOT NULL,
  `comp_acc_id` int(11) NOT NULL,
  `company_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `company_account_card`
--

INSERT INTO `company_account_card` (`card_id`, `comp_acc_id`, `company_id`) VALUES
(6, 3, 4),
(7, 1, 2),
(8, 4, 5),
(9, 5, 1),
(10, 2, 3);

-- --------------------------------------------------------

--
-- Table structure for table `credit_card`
--

CREATE TABLE `credit_card` (
  `card_id` int(11) NOT NULL COMMENT 'primary key',
  `credit_limit` int(11) NOT NULL DEFAULT 0,
  `credit_balance` int(11) NOT NULL DEFAULT 0,
  `expiration_date` date NOT NULL,
  `debt_amount` int(11) NOT NULL DEFAULT 0,
  `card_number` varchar(16) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `credit_card`
--

INSERT INTO `credit_card` (`card_id`, `credit_limit`, `credit_balance`, `expiration_date`, `debt_amount`, `card_number`) VALUES
(1, 1000, 800, '2025-11-04', 200, '1000000011111000'),
(2, 1000, 900, '2023-01-14', 100, '1000000011111014'),
(3, 1000, 700, '2024-03-24', 300, '1000000011111001'),
(4, 1000, 950, '2022-05-06', 50, '1000000011111002'),
(5, 1000, 100, '2025-04-12', 0, '1000000011111003'),
(6, 5000, 300, '2025-10-07', 100, '1000000011111009'),
(7, 5000, 4500, '2026-12-19', 500, '1000000011111010'),
(8, 5000, 4800, '2027-01-06', 200, '1000000011111011'),
(9, 5000, 3000, '2025-02-06', 2000, '1000000011111012'),
(10, 5000, 5000, '2023-05-03', 0, '1000000011111013');

-- --------------------------------------------------------

--
-- Table structure for table `employee`
--

CREATE TABLE `employee` (
  `emp_id` int(11) NOT NULL COMMENT 'primary key',
  `firstname` varchar(128) NOT NULL,
  `surname` varchar(128) NOT NULL,
  `fathername` varchar(128) NOT NULL,
  `company_id` int(11) NOT NULL,
  `afm` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `employee`
--

INSERT INTO `employee` (`emp_id`, `firstname`, `surname`, `fathername`, `company_id`, `afm`) VALUES
(1, 'Kostas', 'Papadopoulos', 'Giorgos', 2, 1234560000),
(2, 'Giorgos', 'Papadakis', 'Nikos', 3, 1234560001),
(3, 'Giannis', 'Georgiou', 'Mixalis', 1, 1234560002),
(4, 'Vasilis', 'Ioannou', 'Manolis', 4, 1234560003),
(5, 'Tasos', 'Milonas', 'Kostas', 5, 1234560004);

-- --------------------------------------------------------

--
-- Table structure for table `employee_transaction`
--

CREATE TABLE `employee_transaction` (
  `card_id` int(11) NOT NULL,
  `transc_id` int(11) NOT NULL,
  `emp_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `employee_transaction`
--

INSERT INTO `employee_transaction` (`card_id`, `transc_id`, `emp_id`) VALUES
(6, 6, 1),
(6, 12, 3),
(7, 7, 2),
(7, 11, 4),
(8, 10, 3),
(9, 9, 1),
(9, 9, 4),
(10, 8, 5);

-- --------------------------------------------------------

--
-- Table structure for table `merchant_account`
--

CREATE TABLE `merchant_account` (
  `merch_acc_id` int(11) NOT NULL COMMENT 'primary key',
  `acc_balance` int(11) NOT NULL DEFAULT 0 COMMENT 'balance in cents',
  `debt_to_ccc` int(11) NOT NULL DEFAULT 0 COMMENT 'debt to ccc in cents',
  `supply_to_ccc` tinyint(4) NOT NULL DEFAULT 0 COMMENT 'percentage given to ccc for eache transaction',
  `acc_closed` int(11) NOT NULL DEFAULT 0 COMMENT '0 if the account is not closed, 1 if it is closed'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `merchant_account`
--

INSERT INTO `merchant_account` (`merch_acc_id`, `acc_balance`, `debt_to_ccc`, `supply_to_ccc`, `acc_closed`) VALUES
(1, 100, 20, 10, 0),
(2, 200, 50, 5, 0),
(3, 500, 100, 15, 0),
(4, 700, 200, 20, 0),
(5, 0, 0, 0, 1);

-- --------------------------------------------------------

--
-- Table structure for table `merchant_user`
--

CREATE TABLE `merchant_user` (
  `merch_user_id` int(11) NOT NULL COMMENT 'primary key',
  `firstname` varchar(128) NOT NULL,
  `surname` varchar(128) NOT NULL,
  `fathername` varchar(128) NOT NULL,
  `merch_acc_id` int(11) DEFAULT NULL,
  `afm` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `merchant_user`
--

INSERT INTO `merchant_user` (`merch_user_id`, `firstname`, `surname`, `fathername`, `merch_acc_id`, `afm`) VALUES
(1, 'Nikos', 'Galanis', 'Giorgos', 2, '1234560005'),
(2, 'Petros', 'Fragkiadakis', 'Nikos', 5, '1234560006'),
(3, 'Vaso', 'Petrou', 'Mixalis', 1, '1234560007'),
(4, 'Anna', 'Pappa', 'Manolis', 4, '1234560008'),
(5, 'Dimitra', 'Nikolaou', 'Kostas', 1, '1234560009');

-- --------------------------------------------------------

--
-- Table structure for table `private_account`
--

CREATE TABLE `private_account` (
  `priv_acc_id` int(11) NOT NULL COMMENT 'primary key',
  `acc_balance` int(11) NOT NULL DEFAULT 0 COMMENT 'balance in cents',
  `acc_closed` int(11) NOT NULL DEFAULT 0 COMMENT '0 if the account is not closed, 1 if it is closed'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `private_account`
--

INSERT INTO `private_account` (`priv_acc_id`, `acc_balance`, `acc_closed`) VALUES
(1, 100, 0),
(2, 200, 0),
(3, 300, 0),
(4, 500, 0),
(5, 0, 1);

-- --------------------------------------------------------

--
-- Table structure for table `private_account_card`
--

CREATE TABLE `private_account_card` (
  `card_id` int(11) NOT NULL,
  `priv_acc_id` int(11) NOT NULL,
  `priv_user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `private_account_card`
--

INSERT INTO `private_account_card` (`card_id`, `priv_acc_id`, `priv_user_id`) VALUES
(1, 3, 4),
(2, 1, 2),
(3, 4, 5),
(4, 5, 1),
(5, 2, 3);

-- --------------------------------------------------------

--
-- Table structure for table `private_user`
--

CREATE TABLE `private_user` (
  `priv_user_id` int(11) NOT NULL COMMENT 'primary key',
  `firstname` varchar(128) NOT NULL,
  `surname` varchar(128) NOT NULL,
  `fathername` varchar(128) NOT NULL,
  `afm` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `private_user`
--

INSERT INTO `private_user` (`priv_user_id`, `firstname`, `surname`, `fathername`, `afm`) VALUES
(1, 'Maria', 'Stamati', 'Giorgos', '1234560010'),
(2, 'Xristos', 'Paulou', 'Nikos', '1234560011'),
(3, 'Sofia', 'Kara', 'Mixalis', '1234560012'),
(4, 'Dimitris', 'Karalis', 'Manolis', '1234560013'),
(5, 'Mixalis', 'Makris', 'Kostas', '1234560014');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ccc_transaction`
--
ALTER TABLE `ccc_transaction`
  ADD PRIMARY KEY (`transc_id`),
  ADD UNIQUE KEY `related_transc_id` (`related_transc_id`),
  ADD KEY `card_id` (`card_id`),
  ADD KEY `merch_acc_id` (`merch_acc_id`);

--
-- Indexes for table `company`
--
ALTER TABLE `company`
  ADD PRIMARY KEY (`company_id`),
  ADD UNIQUE KEY `afm` (`afm`);

--
-- Indexes for table `company_account`
--
ALTER TABLE `company_account`
  ADD PRIMARY KEY (`comp_acc_id`);

--
-- Indexes for table `company_account_card`
--
ALTER TABLE `company_account_card`
  ADD PRIMARY KEY (`card_id`,`comp_acc_id`,`company_id`),
  ADD KEY `company_id` (`company_id`),
  ADD KEY `comp_acc_id` (`comp_acc_id`);

--
-- Indexes for table `credit_card`
--
ALTER TABLE `credit_card`
  ADD PRIMARY KEY (`card_id`),
  ADD UNIQUE KEY `card_number` (`card_number`);

--
-- Indexes for table `employee`
--
ALTER TABLE `employee`
  ADD PRIMARY KEY (`emp_id`),
  ADD UNIQUE KEY `afm` (`afm`),
  ADD KEY `company_id` (`company_id`);

--
-- Indexes for table `employee_transaction`
--
ALTER TABLE `employee_transaction`
  ADD PRIMARY KEY (`card_id`,`transc_id`,`emp_id`),
  ADD KEY `transc_id` (`transc_id`),
  ADD KEY `emp_id` (`emp_id`);

--
-- Indexes for table `merchant_account`
--
ALTER TABLE `merchant_account`
  ADD PRIMARY KEY (`merch_acc_id`);

--
-- Indexes for table `merchant_user`
--
ALTER TABLE `merchant_user`
  ADD PRIMARY KEY (`merch_user_id`),
  ADD UNIQUE KEY `afm` (`afm`),
  ADD KEY `merch_acc_id` (`merch_acc_id`);

--
-- Indexes for table `private_account`
--
ALTER TABLE `private_account`
  ADD PRIMARY KEY (`priv_acc_id`);

--
-- Indexes for table `private_account_card`
--
ALTER TABLE `private_account_card`
  ADD PRIMARY KEY (`card_id`,`priv_acc_id`,`priv_user_id`),
  ADD KEY `priv_user_id` (`priv_user_id`),
  ADD KEY `priv_acc_id` (`priv_acc_id`);

--
-- Indexes for table `private_user`
--
ALTER TABLE `private_user`
  ADD PRIMARY KEY (`priv_user_id`),
  ADD UNIQUE KEY `afm` (`afm`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ccc_transaction`
--
ALTER TABLE `ccc_transaction`
  MODIFY `transc_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'primary key', AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `company`
--
ALTER TABLE `company`
  MODIFY `company_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'primary key', AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `company_account`
--
ALTER TABLE `company_account`
  MODIFY `comp_acc_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'primary key', AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `credit_card`
--
ALTER TABLE `credit_card`
  MODIFY `card_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'primary key', AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `employee`
--
ALTER TABLE `employee`
  MODIFY `emp_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'primary key', AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `merchant_account`
--
ALTER TABLE `merchant_account`
  MODIFY `merch_acc_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'primary key', AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `merchant_user`
--
ALTER TABLE `merchant_user`
  MODIFY `merch_user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'primary key', AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `private_account`
--
ALTER TABLE `private_account`
  MODIFY `priv_acc_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'primary key', AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `private_user`
--
ALTER TABLE `private_user`
  MODIFY `priv_user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'primary key', AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `ccc_transaction`
--
ALTER TABLE `ccc_transaction`
  ADD CONSTRAINT `ccc_transaction_ibfk_1` FOREIGN KEY (`card_id`) REFERENCES `credit_card` (`card_id`),
  ADD CONSTRAINT `ccc_transaction_ibfk_2` FOREIGN KEY (`merch_acc_id`) REFERENCES `merchant_account` (`merch_acc_id`),
  ADD CONSTRAINT `ccc_transaction_ibfk_3` FOREIGN KEY (`related_transc_id`) REFERENCES `ccc_transaction` (`transc_id`);

--
-- Constraints for table `company_account_card`
--
ALTER TABLE `company_account_card`
  ADD CONSTRAINT `company_account_card_ibfk_1` FOREIGN KEY (`card_id`) REFERENCES `credit_card` (`card_id`),
  ADD CONSTRAINT `company_account_card_ibfk_2` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`),
  ADD CONSTRAINT `company_account_card_ibfk_3` FOREIGN KEY (`comp_acc_id`) REFERENCES `company_account` (`comp_acc_id`);

--
-- Constraints for table `employee`
--
ALTER TABLE `employee`
  ADD CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`);

--
-- Constraints for table `employee_transaction`
--
ALTER TABLE `employee_transaction`
  ADD CONSTRAINT `employee_transaction_ibfk_1` FOREIGN KEY (`card_id`) REFERENCES `credit_card` (`card_id`),
  ADD CONSTRAINT `employee_transaction_ibfk_2` FOREIGN KEY (`transc_id`) REFERENCES `ccc_transaction` (`transc_id`),
  ADD CONSTRAINT `employee_transaction_ibfk_3` FOREIGN KEY (`emp_id`) REFERENCES `employee` (`emp_id`);

--
-- Constraints for table `merchant_user`
--
ALTER TABLE `merchant_user`
  ADD CONSTRAINT `merchant_user_ibfk_1` FOREIGN KEY (`merch_acc_id`) REFERENCES `merchant_account` (`merch_acc_id`);

--
-- Constraints for table `private_account_card`
--
ALTER TABLE `private_account_card`
  ADD CONSTRAINT `private_account_card_ibfk_1` FOREIGN KEY (`card_id`) REFERENCES `credit_card` (`card_id`),
  ADD CONSTRAINT `private_account_card_ibfk_2` FOREIGN KEY (`priv_user_id`) REFERENCES `private_user` (`priv_user_id`),
  ADD CONSTRAINT `private_account_card_ibfk_3` FOREIGN KEY (`priv_acc_id`) REFERENCES `private_account` (`priv_acc_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
