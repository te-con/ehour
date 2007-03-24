/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Temporary table structure for view `ACTIVE_ASSIGNMENTS`
--

DROP TABLE IF EXISTS `ACTIVE_ASSIGNMENTS`;
/*!50001 DROP VIEW IF EXISTS `ACTIVE_ASSIGNMENTS`*/;
/*!50001 CREATE TABLE `ACTIVE_ASSIGNMENTS` (
  `customer_id` int(11),
  `customer_name` varchar(255),
  `project_id` int(11),
  `project_name` varchar(255),
  `default_project` char(1),
  `assignment_id` int(11),
  `DEFAULT_ASSIGNMENT` char(1),
  `date_start` date,
  `date_end` date,
  `user_id` int(11)
) */;

--
-- Table structure for table `CONFIGURATION`
--

DROP TABLE IF EXISTS `CONFIGURATION`;
CREATE TABLE `CONFIGURATION` (
  `config_key` varchar(255) NOT NULL,
  `config_value` varchar(255),
  PRIMARY KEY  (`config_key`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `CONFIGURATION`
--

LOCK TABLES `CONFIGURATION` WRITE;
/*!40000 ALTER TABLE `CONFIGURATION` DISABLE KEYS */;
INSERT INTO `CONFIGURATION` VALUES ('completeDayHours','8'),('showTurnOver','true'),('localeLanguage','en'),('currency','Euro'),('localeCountry',NULL),('availableTranslations','en,nl');
/*!40000 ALTER TABLE `CONFIGURATION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CUSTOMER`
--

DROP TABLE IF EXISTS `CUSTOMER`;
CREATE TABLE `CUSTOMER` (
  `CUSTOMER_ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(1024) default NULL,
  `CODE` varchar(32) NOT NULL,
  `ACTIVE` char(1) NOT NULL default 'Y',
  PRIMARY KEY  (`CUSTOMER_ID`),
  UNIQUE KEY `NAME` (`NAME`,`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `CUSTOMER`
--

LOCK TABLES `CUSTOMER` WRITE;
/*!40000 ALTER TABLE `CUSTOMER` DISABLE KEYS */;
/*!40000 ALTER TABLE `CUSTOMER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROJECT`
--

DROP TABLE IF EXISTS `PROJECT`;
CREATE TABLE `PROJECT` (
  `PROJECT_ID` int(11) NOT NULL auto_increment,
  `CUSTOMER_ID` int(11) default NULL,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(1024) default NULL,
  `CONTACT` varchar(255) default NULL,
  `PROJECT_CODE` varchar(32) NOT NULL,
  `DEFAULT_PROJECT` char(1) NOT NULL default 'N',
  `ACTIVE` char(1) NOT NULL default 'Y',
  PRIMARY KEY  (`PROJECT_ID`),
  KEY `CUSTOMER_ID` (`CUSTOMER_ID`),
  CONSTRAINT `PROJECT_fk` FOREIGN KEY (`CUSTOMER_ID`) REFERENCES `CUSTOMER` (`CUSTOMER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `PROJECT`
--

LOCK TABLES `PROJECT` WRITE;
/*!40000 ALTER TABLE `PROJECT` DISABLE KEYS */;
/*!40000 ALTER TABLE `PROJECT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROJECT_ASSIGNMENT`
--

DROP TABLE IF EXISTS `PROJECT_ASSIGNMENT`;
CREATE TABLE `PROJECT_ASSIGNMENT` (
  `ASSIGNMENT_ID` int(11) NOT NULL auto_increment,
  `PROJECT_ID` int(11) NOT NULL,
  `HOURLY_RATE` float(9,3) default NULL,
  `DATE_START` date default NULL,
  `DATE_END` date default NULL,
  `ROLE` varchar(255) default NULL,
  `USER_ID` int(11) NOT NULL,
  `DEFAULT_ASSIGNMENT` char(1) character set latin1 NOT NULL default 'N',
  `ACTIVE` char(1) character set latin1 NOT NULL default 'Y',
  PRIMARY KEY  (`ASSIGNMENT_ID`),
  KEY `PROJECT_ID` (`PROJECT_ID`),
  KEY `USER_ID` (`USER_ID`),
  CONSTRAINT `PROJECT_ASSIGNMENT_fk` FOREIGN KEY (`PROJECT_ID`) REFERENCES `PROJECT` (`PROJECT_ID`),
  CONSTRAINT `PROJECT_ASSIGNMENT_fk1` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `PROJECT_ASSIGNMENT`
--

LOCK TABLES `PROJECT_ASSIGNMENT` WRITE;
/*!40000 ALTER TABLE `PROJECT_ASSIGNMENT` DISABLE KEYS */;
/*!40000 ALTER TABLE `PROJECT_ASSIGNMENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TIMESHEET_COMMENT`
--

DROP TABLE IF EXISTS `TIMESHEET_COMMENT`;
CREATE TABLE `TIMESHEET_COMMENT` (
  `USER_ID` int(11) NOT NULL,
  `COMMENT_DATE` date NOT NULL,
  `COMMENT` varchar(1024) default NULL,
  PRIMARY KEY  (`COMMENT_DATE`,`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `TIMESHEET_COMMENT`
--

LOCK TABLES `TIMESHEET_COMMENT` WRITE;
/*!40000 ALTER TABLE `TIMESHEET_COMMENT` DISABLE KEYS */;
/*!40000 ALTER TABLE `TIMESHEET_COMMENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TIMESHEET_ENTRY`
--

DROP TABLE IF EXISTS `TIMESHEET_ENTRY`;
CREATE TABLE `TIMESHEET_ENTRY` (
  `ASSIGNMENT_ID` int(11) NOT NULL,
  `ENTRY_DATE` date NOT NULL,
  `HOURS` float(9,3) NOT NULL,
  PRIMARY KEY  (`ENTRY_DATE`,`ASSIGNMENT_ID`),
  KEY `ASSIGNMENT_ID` (`ASSIGNMENT_ID`),
  CONSTRAINT `TIMESHEET_ENTRY_fk` FOREIGN KEY (`ASSIGNMENT_ID`) REFERENCES `PROJECT_ASSIGNMENT` (`ASSIGNMENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `TIMESHEET_ENTRY`
--

LOCK TABLES `TIMESHEET_ENTRY` WRITE;
/*!40000 ALTER TABLE `TIMESHEET_ENTRY` DISABLE KEYS */;
/*!40000 ALTER TABLE `TIMESHEET_ENTRY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER`
--

DROP TABLE IF EXISTS `USER`;
CREATE TABLE `USER` (
  `USER_ID` int(11) NOT NULL auto_increment,
  `USERNAME` varchar(64) NOT NULL,
  `PASSWORD` varchar(128) NOT NULL,
  `FIRST_NAME` varchar(64) default NULL,
  `LAST_NAME` varchar(64) NOT NULL,
  `DEPARTMENT_ID` int(11) NOT NULL,
  `EMAIL` varchar(128) default NULL,
  `ACTIVE` char(1) NOT NULL default 'Y',
  PRIMARY KEY  (`USER_ID`),
  UNIQUE KEY `USER_ID` (`USER_ID`),
  UNIQUE KEY `USERNAME` (`USERNAME`),
  UNIQUE KEY `USERNAME_ACTIVE` (`USERNAME`,`ACTIVE`),
  KEY `IDX_USERNAME_PASSWORD` (`USERNAME`,`PASSWORD`),
  KEY `ORGANISATION_ID` (`DEPARTMENT_ID`),
  CONSTRAINT `USER_fk` FOREIGN KEY (`DEPARTMENT_ID`) REFERENCES `USER_DEPARTMENT` (`DEPARTMENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `USER`
--

LOCK TABLES `USER` WRITE;
/*!40000 ALTER TABLE `USER` DISABLE KEYS */;
INSERT INTO `USER` (`USER_ID`, `USERNAME`, `PASSWORD`, `FIRST_NAME`, `LAST_NAME`, `DEPARTMENT_ID`, `EMAIL`, `ACTIVE`) VALUES (1,'admin','d033e22ae348aeb5660fc2140aec35850c4da997','eHour','Admin',1,'','Y');
/*!40000 ALTER TABLE `USER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_DEPARTMENT`
--

DROP TABLE IF EXISTS `USER_DEPARTMENT`;
CREATE TABLE `USER_DEPARTMENT` (
  `DEPARTMENT_ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(512) NOT NULL,
  `CODE` varchar(64) NOT NULL,
  PRIMARY KEY  (`DEPARTMENT_ID`),
  UNIQUE KEY `DEPARTMENT_ID` (`DEPARTMENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `USER_DEPARTMENT`
--

LOCK TABLES `USER_DEPARTMENT` WRITE;
/*!40000 ALTER TABLE `USER_DEPARTMENT` DISABLE KEYS */;
INSERT INTO `USER_DEPARTMENT` (`DEPARTMENT_ID`, `NAME`, `CODE`) VALUES (1,'TE-CON','TEC');
/*!40000 ALTER TABLE `USER_DEPARTMENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_ROLE`
--

DROP TABLE IF EXISTS `USER_ROLE`;
CREATE TABLE `USER_ROLE` (
  `ROLE` varchar(128) NOT NULL,
  `NAME` varchar(128) NOT NULL,
  PRIMARY KEY  (`ROLE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `USER_ROLE`
--

LOCK TABLES `USER_ROLE` WRITE;
/*!40000 ALTER TABLE `USER_ROLE` DISABLE KEYS */;
INSERT INTO `USER_ROLE` (`ROLE`, `NAME`) VALUES ('ROLE_ADMIN','Administrator'),('ROLE_CONSULTANT','Consultant'),('ROLE_REPORT','Report role');
/*!40000 ALTER TABLE `USER_ROLE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_TO_USERROLE`
--

DROP TABLE IF EXISTS `USER_TO_USERROLE`;
CREATE TABLE `USER_TO_USERROLE` (
  `ROLE` varchar(128) NOT NULL,
  `USER_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ROLE`,`USER_ID`),
  KEY `ROLE` (`ROLE`),
  KEY `USER_ID` (`USER_ID`),
  CONSTRAINT `USER_TO_USERROLE_fk` FOREIGN KEY (`ROLE`) REFERENCES `USER_ROLE` (`ROLE`),
  CONSTRAINT `USER_TO_USERROLE_fk1` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `USER_TO_USERROLE`
--

LOCK TABLES `USER_TO_USERROLE` WRITE;
/*!40000 ALTER TABLE `USER_TO_USERROLE` DISABLE KEYS */;
INSERT INTO `USER_TO_USERROLE` (`ROLE`, `USER_ID`) VALUES ('ROLE_ADMIN',1),('ROLE_REPORT',1);
/*!40000 ALTER TABLE `USER_TO_USERROLE` ENABLE KEYS */;
UNLOCK TABLES;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
