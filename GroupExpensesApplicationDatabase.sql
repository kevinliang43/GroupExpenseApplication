-- MySQL dump 10.13  Distrib 5.7.12, for osx10.9 (x86_64)
--
-- Host: localhost    Database: GroupExpensesApplication
-- ------------------------------------------------------
-- Server version	5.7.15

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
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accounts` (
  `firstName` varchar(20) NOT NULL,
  `lastName` varchar(20) NOT NULL,
  `accountID` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(15) NOT NULL,
  PRIMARY KEY (`accountID`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `accountID_UNIQUE` (`accountID`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES ('Kevin','Liang',1,'kevinliang1','123'),('Melanie','Chan',2,'KappaWHAT','PhiLambda'),('Kristy','Chen',3,'kristychen2','456'),('Raymond','You',4,'leRayHue69','eggsde'),('Junhao','Dong',5,'turtleteam','lemao43'),('Terry','Zhao',6,'kassaNoob','teemoMain'),('Lisa','Chan',7,'Risaaa','animeWatcher'),('Kevin','Kevin',11,'test','test2');
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expense`
--

DROP TABLE IF EXISTS `expense`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expense` (
  `expenseID` int(11) NOT NULL,
  `groupID` int(11) NOT NULL,
  `accountOwes` int(11) NOT NULL,
  `accountOwed` int(11) NOT NULL,
  `amountOwed` int(11) NOT NULL,
  `paid` tinyint(1) NOT NULL DEFAULT '0',
  `date` date NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`expenseID`,`accountOwes`,`accountOwed`),
  KEY `groupID_idx1` (`groupID`),
  KEY `accountOwed_idx1` (`accountOwed`),
  KEY `accountOwes_idx1` (`accountOwes`),
  CONSTRAINT `accountOwed` FOREIGN KEY (`accountOwed`) REFERENCES `accounts` (`accountID`) ON DELETE CASCADE,
  CONSTRAINT `accountOwes` FOREIGN KEY (`accountOwes`) REFERENCES `accounts` (`accountID`) ON DELETE CASCADE,
  CONSTRAINT `expenseGroupID` FOREIGN KEY (`groupID`) REFERENCES `group` (`groupID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expense`
--

LOCK TABLES `expense` WRITE;
/*!40000 ALTER TABLE `expense` DISABLE KEYS */;
INSERT INTO `expense` VALUES (1,1,4,1,15,0,'2016-09-16','Groceries'),(1,1,5,1,15,1,'2016-09-16','Groceries'),(1,1,6,1,15,0,'2016-09-16','Groceries'),(2,5,3,4,15,0,'2016-10-16','Date'),(3,1,1,4,12,0,'2016-11-03','Dinner'),(3,1,5,4,12,0,'2016-11-03','Dinner'),(4,1,1,5,3,0,'2016-11-03','Uber'),(4,1,4,5,3,0,'2016-11-03','Uber'),(5,1,1,6,3,0,'2016-12-23','Lunch');
/*!40000 ALTER TABLE `expense` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group`
--

DROP TABLE IF EXISTS `group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group` (
  `groupID` int(11) NOT NULL AUTO_INCREMENT,
  `groupName` varchar(45) NOT NULL,
  `adminID` int(11) NOT NULL,
  PRIMARY KEY (`groupID`),
  UNIQUE KEY `groupID_UNIQUE` (`groupID`),
  KEY `adminID_idx` (`adminID`),
  CONSTRAINT `adminID` FOREIGN KEY (`adminID`) REFERENCES `accounts` (`accountID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group`
--

LOCK TABLES `group` WRITE;
/*!40000 ALTER TABLE `group` DISABLE KEYS */;
INSERT INTO `group` VALUES (1,'Willis 311',1),(2,'Burstein',3),(3,'freshmen',1),(5,'bestFriends',3);
/*!40000 ALTER TABLE `group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groupInvite`
--

DROP TABLE IF EXISTS `groupInvite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupInvite` (
  `groupID` int(11) NOT NULL,
  `accountID` int(11) NOT NULL,
  PRIMARY KEY (`groupID`,`accountID`),
  KEY `accountID_idx` (`accountID`),
  CONSTRAINT `groupInviteAccountID` FOREIGN KEY (`accountID`) REFERENCES `accounts` (`accountID`) ON DELETE CASCADE,
  CONSTRAINT `groupInviteGroupID` FOREIGN KEY (`groupID`) REFERENCES `group` (`groupID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groupInvite`
--

LOCK TABLES `groupInvite` WRITE;
/*!40000 ALTER TABLE `groupInvite` DISABLE KEYS */;
/*!40000 ALTER TABLE `groupInvite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groupList`
--

DROP TABLE IF EXISTS `groupList`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupList` (
  `accountID` int(11) NOT NULL,
  `groupID` int(11) NOT NULL,
  KEY `accountID_idx` (`accountID`),
  KEY `groupID_idx` (`groupID`),
  CONSTRAINT `accountID` FOREIGN KEY (`accountID`) REFERENCES `accounts` (`accountID`) ON DELETE CASCADE,
  CONSTRAINT `groupID` FOREIGN KEY (`groupID`) REFERENCES `group` (`groupID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groupList`
--

LOCK TABLES `groupList` WRITE;
/*!40000 ALTER TABLE `groupList` DISABLE KEYS */;
INSERT INTO `groupList` VALUES (1,1),(1,3),(2,2),(2,3),(3,3),(3,2),(4,1),(4,3),(5,3),(5,1),(6,1),(6,3),(7,2);
/*!40000 ALTER TABLE `groupList` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'GroupExpensesApplication'
--
/*!50003 DROP FUNCTION IF EXISTS `maxExpenseID` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `maxExpenseID`() RETURNS int(11)
BEGIN
	DECLARE num INT;
	SELECT MAX(expenseID)+1
    INTO num
	FROM expense;
    RETURN num;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `top_expenses` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `top_expenses`(userID INT)
BEGIN
	SELECT expense.name, expenseID
	FROM expense JOIN accounts
		ON expense.accountOwes = accounts.accountID
	WHERE accounts.accountID = userID
    ORDER BY expense.date DESC
    LIMIT 5;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `totalOwed` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `totalOwed`(userID INT)
BEGIN
	SELECT SUM(expense.amountOwed) as total
	FROM accounts JOIN expense
		ON expense.accountOwed = accounts.accountID
	WHERE userID = expense.accountOwed AND paid = 0;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `totalOwes` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `totalOwes`(userID INT)
BEGIN
	SELECT SUM(expense.amountOwed) as total
	FROM accounts JOIN expense
		ON expense.accountOwes = accounts.accountID
	WHERE userID = expense.accountOwes AND paid = 0;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-12-07  3:45:22
