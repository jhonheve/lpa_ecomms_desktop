-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 05, 2018 at 04:17 AM
-- Server version: 10.1.30-MariaDB
-- PHP Version: 7.2.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `lpa_ecomms`
--

-- --------------------------------------------------------

--
-- Table structure for table `lpa_clients`
--
-- Creation: Mar 19, 2018 at 01:45 AM
--

CREATE TABLE IF NOT EXISTS `lpa_clients` (
  `lpa_client_ID` varchar(20) NOT NULL,
  `lpa_client_firstname` varchar(50) NOT NULL,
  `lpa_client_lastname` varchar(50) NOT NULL,
  `lpa_client_address` varchar(250) NOT NULL,
  `lpa_client_phone` varchar(30) NOT NULL,
  `lpa_client_status` char(1) NOT NULL,
  PRIMARY KEY (`lpa_client_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONSHIPS FOR TABLE `lpa_clients`:
--

--
-- Truncate table before insert `lpa_clients`
--

TRUNCATE TABLE `lpa_clients`;
--
-- Dumping data for table `lpa_clients`
--

INSERT DELAYED INTO `lpa_clients` (`lpa_client_ID`, `lpa_client_firstname`, `lpa_client_lastname`, `lpa_client_address`, `lpa_client_phone`, `lpa_client_status`) VALUES
('619', 'JhonE', 'Ed', 'asdfa', '12341234', 'a'),
('692', 'a', 'a', 'a', '1', 'D');

-- --------------------------------------------------------

--
-- Table structure for table `lpa_invoices`
--
-- Creation: Mar 19, 2018 at 02:14 AM
--

CREATE TABLE IF NOT EXISTS `lpa_invoices` (
  `lpa_inv_no` varchar(20) NOT NULL,
  `lpa_inv_date` datetime NOT NULL,
  `lpa_inv_client_ID` varchar(20) NOT NULL,
  `lpa_inv_client_name` varchar(50) NOT NULL,
  `lpa_inv_client_address` varchar(250) NOT NULL,
  `lpa_inv_amount` decimal(8,2) NOT NULL,
  `lpa_inv_status` char(1) NOT NULL,
  PRIMARY KEY (`lpa_inv_no`),
  KEY `lpa_inv_client_ID` (`lpa_inv_client_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONSHIPS FOR TABLE `lpa_invoices`:
--   `lpa_inv_client_ID`
--       `lpa_clients` -> `lpa_client_ID`
--

--
-- Truncate table before insert `lpa_invoices`
--

TRUNCATE TABLE `lpa_invoices`;
--
-- Dumping data for table `lpa_invoices`
--

INSERT DELAYED INTO `lpa_invoices` (`lpa_inv_no`, `lpa_inv_date`, `lpa_inv_client_ID`, `lpa_inv_client_name`, `lpa_inv_client_address`, `lpa_inv_amount`, `lpa_inv_status`) VALUES
('816', '2018-11-05 01:01:06', '619', 'JhonE Ed', 'asdfa', '3121.00', 'A'),
('911', '2018-11-05 01:02:15', '619', 'JhonE Ed', 'asdfa', '3121.00', 'A');

-- --------------------------------------------------------

--
-- Table structure for table `lpa_invoice_items`
--
-- Creation: Mar 19, 2018 at 02:15 AM
--

CREATE TABLE IF NOT EXISTS `lpa_invoice_items` (
  `lpa_invitem_no` varchar(20) NOT NULL,
  `lpa_invitem_inv_no` varchar(20) NOT NULL,
  `lpa_invitem_stock_ID` varchar(20) NOT NULL,
  `lpa_invitem_stock_name` varchar(250) NOT NULL,
  `lpa_invitem_qty` varchar(6) NOT NULL,
  `lpa_invitem_stock_price` decimal(7,2) NOT NULL,
  `lpa_invitem_stock_amount` decimal(7,2) NOT NULL,
  `lpa_inv_status` char(1) NOT NULL,
  PRIMARY KEY (`lpa_invitem_no`),
  KEY `lpa_invitem_stock_ID` (`lpa_invitem_stock_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONSHIPS FOR TABLE `lpa_invoice_items`:
--   `lpa_invitem_stock_ID`
--       `lpa_stock` -> `lpa_stock_ID`
--

--
-- Truncate table before insert `lpa_invoice_items`
--

TRUNCATE TABLE `lpa_invoice_items`;
--
-- Dumping data for table `lpa_invoice_items`
--

INSERT DELAYED INTO `lpa_invoice_items` (`lpa_invitem_no`, `lpa_invitem_inv_no`, `lpa_invitem_stock_ID`, `lpa_invitem_stock_name`, `lpa_invitem_qty`, `lpa_invitem_stock_price`, `lpa_invitem_stock_amount`, `lpa_inv_status`) VALUES
('222', '911', '124', 'Apple iPad 4', '12', '250.00', '3000.00', 'a'),
('458', '911', '111', '111', '11', '11.00', '121.00', 'a');

-- --------------------------------------------------------

--
-- Table structure for table `lpa_stock`
--
-- Creation: Aug 23, 2018 at 11:57 PM
--

CREATE TABLE IF NOT EXISTS `lpa_stock` (
  `lpa_stock_ID` varchar(20) NOT NULL,
  `lpa_stock_name` varchar(250) NOT NULL,
  `lpa_stock_desc` text NOT NULL,
  `lpa_stock_onhand` varchar(5) NOT NULL,
  `lpa_stock_price` decimal(7,2) NOT NULL,
  `lpa_image` varchar(255) DEFAULT NULL,
  `lpa_stock_status` char(1) NOT NULL DEFAULT 'a',
  PRIMARY KEY (`lpa_stock_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONSHIPS FOR TABLE `lpa_stock`:
--

--
-- Truncate table before insert `lpa_stock`
--

TRUNCATE TABLE `lpa_stock`;
--
-- Dumping data for table `lpa_stock`
--

INSERT DELAYED INTO `lpa_stock` (`lpa_stock_ID`, `lpa_stock_name`, `lpa_stock_desc`, `lpa_stock_onhand`, `lpa_stock_price`, `lpa_image`, `lpa_stock_status`) VALUES
('0758596363', '123', 'desc', '11', '12.00', '', 'a'),
('111', '111', '111', '1', '11.00', NULL, 'a'),
('123', 'Computer', 'Computer systemssssssfsd', '0', '1500.00', '', 'a'),
('123456765', 'test2', 'dasd', '20', '12.00', '', 'a'),
('124', 'Apple iPad 4', 'This is an apple iPad 4', '4', '250.00', 'iPad.png', 'D'),
('125', 'Mini Display Port to VGA', 'Cable for Apple', '3', '33.00', '', 'a'),
('126', 'StLab U-470 USB 2.0 to VGA Adapter', ' VGA Adapter', '5', '42.00', '', 'a'),
('127', 'USB2.0 To Ethernet Adapter', 'Cable ', '5', '9.00', '', 'a'),
('128', 'Computer Monitor', 'LCD screen', '4', '21.54', 'monitor.png', 'a'),
('1879746355', 'sdfgsd', 'dfgsdfgsdg', '110', '10.00', '', 'a'),
('2147483647', 'Test1', 'Descr', '1', '10.00', '', 'a'),
('5279703051', 'aabb', 'asdfasd123', '110', '110.00', '', 'D'),
('614156305', 'asdas', 'descsdfsd', '12', '1.00', '', 'i'),
('822', 'q', 'q', '1', '1.00', NULL, 'a'),
('921', 'dfas', 'asdf', '12', '22.00', NULL, 'a'),
('96456', 'a', 'a', '11', '11.00', NULL, 'D'),
('9813', 'a1243', 'a1233', '11', '1.00', NULL, 'a');

-- --------------------------------------------------------

--
-- Table structure for table `lpa_users`
--
-- Creation: Mar 19, 2018 at 01:50 AM
--

CREATE TABLE IF NOT EXISTS `lpa_users` (
  `lpa_user_ID` varchar(20) NOT NULL,
  `lpa_user_username` varchar(30) NOT NULL,
  `lpa_user_password` varchar(50) NOT NULL,
  `lpa_user_firstname` varchar(50) NOT NULL,
  `lpa_user_lastname` varchar(50) NOT NULL,
  `lpa_user_group` varchar(50) NOT NULL,
  `lpa_user_status` char(1) NOT NULL,
  PRIMARY KEY (`lpa_user_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONSHIPS FOR TABLE `lpa_users`:
--

--
-- Truncate table before insert `lpa_users`
--

TRUNCATE TABLE `lpa_users`;
--
-- Dumping data for table `lpa_users`
--

INSERT DELAYED INTO `lpa_users` (`lpa_user_ID`, `lpa_user_username`, `lpa_user_password`, `lpa_user_firstname`, `lpa_user_lastname`, `lpa_user_group`, `lpa_user_status`) VALUES
('1', 'admin', '123', 'Jhon Edison', 'Henao V', 'administrator', '1'),
('2', 'user', '12345', 'Oscar Mauricio', 'Salazar Ospina', 'user', 'a'),
('283', 'abc', '123', 'abc', 'henao', 'administrator', '1'),
('3', 'codea', 'password', 'Steve', 'Coleman', 'administrator', 'a');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `lpa_invoices`
--
ALTER TABLE `lpa_invoices`
  ADD CONSTRAINT `lpa_invoices_ibfk_1` FOREIGN KEY (`lpa_inv_client_ID`) REFERENCES `lpa_clients` (`lpa_client_ID`);

--
-- Constraints for table `lpa_invoice_items`
--
ALTER TABLE `lpa_invoice_items`
  ADD CONSTRAINT `lpa_invoice_items_ibfk_1` FOREIGN KEY (`lpa_invitem_stock_ID`) REFERENCES `lpa_stock` (`lpa_stock_ID`);


--
-- Metadata
--
USE `phpmyadmin`;

--
-- Metadata for table lpa_clients
--

--
-- Truncate table before insert `pma__column_info`
--

TRUNCATE TABLE `pma__column_info`;
--
-- Truncate table before insert `pma__table_uiprefs`
--

TRUNCATE TABLE `pma__table_uiprefs`;
--
-- Truncate table before insert `pma__tracking`
--

TRUNCATE TABLE `pma__tracking`;
--
-- Metadata for table lpa_invoices
--

--
-- Truncate table before insert `pma__column_info`
--

TRUNCATE TABLE `pma__column_info`;
--
-- Truncate table before insert `pma__table_uiprefs`
--

TRUNCATE TABLE `pma__table_uiprefs`;
--
-- Truncate table before insert `pma__tracking`
--

TRUNCATE TABLE `pma__tracking`;
--
-- Metadata for table lpa_invoice_items
--

--
-- Truncate table before insert `pma__column_info`
--

TRUNCATE TABLE `pma__column_info`;
--
-- Truncate table before insert `pma__table_uiprefs`
--

TRUNCATE TABLE `pma__table_uiprefs`;
--
-- Truncate table before insert `pma__tracking`
--

TRUNCATE TABLE `pma__tracking`;
--
-- Metadata for table lpa_stock
--

--
-- Truncate table before insert `pma__column_info`
--

TRUNCATE TABLE `pma__column_info`;
--
-- Truncate table before insert `pma__table_uiprefs`
--

TRUNCATE TABLE `pma__table_uiprefs`;
--
-- Truncate table before insert `pma__tracking`
--

TRUNCATE TABLE `pma__tracking`;
--
-- Metadata for table lpa_users
--

--
-- Truncate table before insert `pma__column_info`
--

TRUNCATE TABLE `pma__column_info`;
--
-- Truncate table before insert `pma__table_uiprefs`
--

TRUNCATE TABLE `pma__table_uiprefs`;
--
-- Truncate table before insert `pma__tracking`
--

TRUNCATE TABLE `pma__tracking`;
--
-- Metadata for database lpa_ecomms
--

--
-- Truncate table before insert `pma__bookmark`
--

TRUNCATE TABLE `pma__bookmark`;
--
-- Truncate table before insert `pma__relation`
--

TRUNCATE TABLE `pma__relation`;
--
-- Truncate table before insert `pma__pdf_pages`
--

TRUNCATE TABLE `pma__pdf_pages`;
--
-- Dumping data for table `pma__pdf_pages`
--

INSERT DELAYED INTO `pma__pdf_pages` (`db_name`, `page_descr`) VALUES
('lpa_ecomms', 'EcommsDiagram');

SET @LAST_PAGE = LAST_INSERT_ID();

--
-- Truncate table before insert `pma__table_coords`
--

TRUNCATE TABLE `pma__table_coords`;
--
-- Dumping data for table `pma__table_coords`
--

INSERT DELAYED INTO `pma__table_coords` (`db_name`, `table_name`, `pdf_page_number`, `x`, `y`) VALUES
('lpa_ecomms', 'lpa_clients', @LAST_PAGE, 116, 261),
('lpa_ecomms', 'lpa_invoice_items', @LAST_PAGE, 624, 17),
('lpa_ecomms', 'lpa_invoices', @LAST_PAGE, 504, 266),
('lpa_ecomms', 'lpa_stock', @LAST_PAGE, 112, 52),
('lpa_ecomms', 'lpa_users', @LAST_PAGE, 362, 104);

--
-- Truncate table before insert `pma__savedsearches`
--

TRUNCATE TABLE `pma__savedsearches`;
--
-- Truncate table before insert `pma__central_columns`
--

TRUNCATE TABLE `pma__central_columns`;COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
