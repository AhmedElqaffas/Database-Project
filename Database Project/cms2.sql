-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Dec 11, 2018 at 07:02 PM
-- Server version: 5.7.23
-- PHP Version: 7.2.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cms2`
--

-- --------------------------------------------------------

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
CREATE TABLE IF NOT EXISTS `client` (
  `client_id` int(11) NOT NULL AUTO_INCREMENT,
  `client_name` varchar(50) NOT NULL,
  `client_mail` varchar(50) NOT NULL,
  `client_phone` varchar(11) DEFAULT NULL,
  `client_city` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`client_id`),
  UNIQUE KEY `client_mail` (`client_mail`),
  UNIQUE KEY `client_phone` (`client_phone`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
CREATE TABLE IF NOT EXISTS `department` (
  `dept_name` varchar(50) NOT NULL,
  `dept_budget` int(10) UNSIGNED DEFAULT NULL,
  `head` int(11) DEFAULT NULL,
  `head_date` date DEFAULT NULL,
  PRIMARY KEY (`dept_name`),
  KEY `head` (`head`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
CREATE TABLE IF NOT EXISTS `employee` (
  `emp_id` int(11) NOT NULL AUTO_INCREMENT,
  `emp_name` varchar(50) NOT NULL,
  `salary` int(10) UNSIGNED DEFAULT NULL,
  `birth_date` date NOT NULL,
  `employee_mail` varchar(50) NOT NULL,
  `employment_date` date NOT NULL,
  `dept_name` varchar(50) NOT NULL,
  `address` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`emp_id`),
  UNIQUE KEY `employee_mail` (`employee_mail`),
  KEY `dept_name` (`dept_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `emp_phone`
--

DROP TABLE IF EXISTS `emp_phone`;
CREATE TABLE IF NOT EXISTS `emp_phone` (
  `emp_id` int(11) NOT NULL,
  `phone` varchar(11) NOT NULL,
  PRIMARY KEY (`emp_id`,`phone`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `milestone`
--

DROP TABLE IF EXISTS `milestone`;
CREATE TABLE IF NOT EXISTS `milestone` (
  `proj_id` int(11) NOT NULL,
  `ms_date` date NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`proj_id`,`ms_date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `needs`
--

DROP TABLE IF EXISTS `needs`;
CREATE TABLE IF NOT EXISTS `needs` (
  `request_id` int(11) NOT NULL AUTO_INCREMENT,
  `item` varchar(100) NOT NULL,
  `request_date` date DEFAULT NULL,
  `priority` varchar(10) DEFAULT NULL,
  `arrived` tinyint(1) DEFAULT NULL,
  `dept_name` varchar(50) NOT NULL,
  `sup_name` varchar(50) DEFAULT NULL,
  `price` int(10) UNSIGNED DEFAULT NULL,
  `purchase_date` date DEFAULT NULL,
  PRIMARY KEY (`request_id`),
  KEY `dept_name` (`dept_name`),
  KEY `sup_name` (`sup_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
CREATE TABLE IF NOT EXISTS `project` (
  `proj_id` int(11) NOT NULL AUTO_INCREMENT,
  `proj_name` varchar(50) NOT NULL,
  `proj_budget` int(10) UNSIGNED DEFAULT NULL,
  `start_date` date NOT NULL,
  `due_date` date DEFAULT NULL,
  `client_id` int(11) DEFAULT NULL,
  `manager` int(11) DEFAULT NULL,
  PRIMARY KEY (`proj_id`),
  KEY `client_id` (`client_id`),
  KEY `manager` (`manager`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `proj_dept`
--

DROP TABLE IF EXISTS `proj_dept`;
CREATE TABLE IF NOT EXISTS `proj_dept` (
  `proj_id` int(11) NOT NULL,
  `dept_name` varchar(50) NOT NULL,
  PRIMARY KEY (`proj_id`,`dept_name`),
  KEY `dept_name` (`dept_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `proj_emp`
--

DROP TABLE IF EXISTS `proj_emp`;
CREATE TABLE IF NOT EXISTS `proj_emp` (
  `proj_id` int(11) NOT NULL,
  `emp_id` int(11) NOT NULL,
  PRIMARY KEY (`proj_id`,`emp_id`),
  KEY `emp_id` (`emp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
CREATE TABLE IF NOT EXISTS `role` (
  `role_name` varchar(50) NOT NULL,
  PRIMARY KEY (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `role_emp`
--

DROP TABLE IF EXISTS `role_emp`;
CREATE TABLE IF NOT EXISTS `role_emp` (
  `emp_id` int(11) NOT NULL,
  `role_name` varchar(50) NOT NULL,
  `role_date` date NOT NULL,
  `active` tinyint(1) NOT NULL,
  PRIMARY KEY (`emp_id`,`role_name`,`role_date`),
  KEY `role_name` (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
CREATE TABLE IF NOT EXISTS `supplier` (
  `sup_name` varchar(50) NOT NULL,
  `sup_city` varchar(20) DEFAULT NULL,
  `sup_mail` varchar(50) NOT NULL,
  `sup_phone` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`sup_name`),
  UNIQUE KEY `sup_mail` (`sup_mail`),
  UNIQUE KEY `sup_phone` (`sup_phone`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `department`
--
ALTER TABLE `department`
  ADD CONSTRAINT `department_ibfk_1` FOREIGN KEY (`head`) REFERENCES `employee` (`emp_id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `employee`
--
ALTER TABLE `employee`
  ADD CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`dept_name`) REFERENCES `department` (`dept_name`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `emp_phone`
--
ALTER TABLE `emp_phone`
  ADD CONSTRAINT `emp_phone_ibfk_1` FOREIGN KEY (`emp_id`) REFERENCES `employee` (`emp_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `milestone`
--
ALTER TABLE `milestone`
  ADD CONSTRAINT `milestone_ibfk_1` FOREIGN KEY (`proj_id`) REFERENCES `project` (`proj_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `needs`
--
ALTER TABLE `needs`
  ADD CONSTRAINT `needs_ibfk_1` FOREIGN KEY (`dept_name`) REFERENCES `department` (`dept_name`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `needs_ibfk_2` FOREIGN KEY (`sup_name`) REFERENCES `supplier` (`sup_name`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `project`
--
ALTER TABLE `project`
  ADD CONSTRAINT `project_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `client` (`client_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `project_ibfk_2` FOREIGN KEY (`manager`) REFERENCES `employee` (`emp_id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `proj_dept`
--
ALTER TABLE `proj_dept`
  ADD CONSTRAINT `proj_dept_ibfk_1` FOREIGN KEY (`proj_id`) REFERENCES `project` (`proj_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `proj_dept_ibfk_2` FOREIGN KEY (`dept_name`) REFERENCES `department` (`dept_name`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `proj_emp`
--
ALTER TABLE `proj_emp`
  ADD CONSTRAINT `proj_emp_ibfk_1` FOREIGN KEY (`proj_id`) REFERENCES `project` (`proj_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `proj_emp_ibfk_2` FOREIGN KEY (`emp_id`) REFERENCES `employee` (`emp_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `role_emp`
--
ALTER TABLE `role_emp`
  ADD CONSTRAINT `role_emp_ibfk_1` FOREIGN KEY (`role_name`) REFERENCES `role` (`role_name`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `role_emp_ibfk_2` FOREIGN KEY (`emp_id`) REFERENCES `employee` (`emp_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
