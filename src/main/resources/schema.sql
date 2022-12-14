-- MySQL dump 10.13  Distrib 8.0.22, for osx10.16 (x86_64)
--
-- Host: localhost    Database: paymybuddy
-- ------------------------------------------------------
-- Server version	8.0.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
DROP DATABASE paymybuddy;
CREATE DATABASE paymybuddy;
USE paymybuddy;
--
-- Table structure for table `users`
--
DROP TABLE IF EXISTS `authorities`;
DROP TABLE IF EXISTS `transaction`;
DROP TABLE IF EXISTS `bankaccount`;
DROP TABLE IF EXISTS `users_userfriend`;
DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id_user` int NOT NULL AUTO_INCREMENT,
  `lastname` varchar(100) NOT NULL,
  `firstname` varchar(100) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `enabled` tinyint not null DEFAULT 1,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `username` (`username`)
)AUTO_INCREMENT = 100 ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Benson','Alex','a.benson@gmail.com','$2y$10$faEZ0q.A9PyAXFvopXu3kukd9k552dNAgEbaRPkd998xh49U.9u3S',1),
(2,'Cattoz','Sissi','s.cattoz@gmail.com','$2y$10$.cQqzAVBwNvR98Wa0PjaIOTRRbYIUQeuUwgdmleQc.OUOvS2B4iFe',1),
(3,'Felon','Max','m.felon@gmail.com','$2y$10$D75O49wJqrfy5dZFReKF9eSeHLHqI93Vx3p970854/a2gOwrGVZ3y',1),
(4,'Tarot','Lisa','l.tarot@gmail.com','$2y$10$GKsQBKfh.L3sgj4kgf3xH.Mp2JGE7C8RYt0E8nGocDS88INPz72mi',1),
(5,'Dolipe','Olivier','o.dolipe@gmail.com','$2y$10$Gold/f8GrFdDtAQiw.Ad4.3Z9C4QB6Loi3BfvQr/jtjJOBEZ1XhHS',1),
(6,'admin','commision','admin','$2a$12$075OQPMIP2RscVMl46aOqOc7hvPieKND8nPr7gV6GSOkRcGwHd/DK',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bank_account`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bankaccount` (
`id_bankaccount` int NOT NULL auto_increment,
  `id_user` int NOT NULL,
  `name` varchar(50) NOT NULL,
  `iban` varchar(100) NOT NULL,
  `inside` boolean NOT NULL,
  PRIMARY KEY (`id_bankaccount`),
  FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE
)AUTO_INCREMENT = 100 ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bankaccount`
--

LOCK TABLES `bankaccount` WRITE;
/*!40000 ALTER TABLE `bankaccount` DISABLE KEYS */;
INSERT INTO `bankaccount` VALUES (1,1,'orange','iban1',false),
(2,1,'a.benson@gmail.com','ibaninterne1',true),
(3,2,'BNP','iban2',false),
(4,2,'s.cattoz@gmail.com','ibaninterne2',true),
(5,3,'Cecaz','iban3',false),
(6,3,'m.felon@gmail.com','ibaninterne3',true),
(7,4,'BNP','iban4',false),
(8,4,'l.tarot@gmail.com','ibaninterne4',true),
(9,5,'CA','iban5',false),
(10,5,'o.dolipe@gmail.com','ibaninterne5',true),
(11,6,'commission','ibaninterne6',true),
(12,6,'externe','ibanexterne',false);
/*!40000 ALTER TABLE `bankaccount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction` (
  `id_transaction` int NOT NULL AUTO_INCREMENT,
  `id_account_send` int NOT NULL,
  `id_account_receive` int NOT NULL,
  `amount` float NOT NULL,
  `comment` varchar(100),
  PRIMARY KEY (`id_transaction`)
) AUTO_INCREMENT = 100 ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction`
--

LOCK TABLES `transaction` WRITE;
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;
INSERT INTO `transaction` VALUES (1,3,4,199,'versement'),
(2,4,11,1,'commission'),
(3,1,2,99.5,'versement'),
(4,4,11,0.5,'commission'),
(5,4,2,49.75,'resto annie'),
(6,4,11,0.25,'commission'),
(7,2,4,-50.00,'resto annie'),
(8,5,6,298.5,'versement'),
(9,6,11,1.5,'commission'),
(10,6,2,49.75,'course'),
(11,6,11,0.25,'commission'),
(12,2,6,-50,'course'),
(13,2,1,-50,'transfert'),
(14,2,11,0.25,'commission');
/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_userfriend`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_userfriend` (
  `id_user` int NOT NULL,
  `id_userfriend` int NOT NULL,
  CONSTRAINT `users_userfriend_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `users_userfriendt_ibfk_2` FOREIGN KEY (`id_userfriend`) REFERENCES `users` (`id_user`) ON DELETE RESTRICT ON UPDATE CASCADE
)AUTO_INCREMENT = 100 ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_userfriend`
--

LOCK TABLES `users_userfriend` WRITE;
/*!40000 ALTER TABLE `users_userfriend` DISABLE KEYS */;
INSERT INTO `users_userfriend` VALUES (1,2),(2,1),(3,1),(1,3);
/*!40000 ALTER TABLE `users_userfriend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authorities`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `authorities` (
  `username` varchar(50) NOT NULL,
  `role` varchar(50) NOT NULL,
  KEY `username` (`username`),
  CONSTRAINT `authorithies_ibfk_1` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE RESTRICT ON UPDATE CASCADE
)AUTO_INCREMENT = 100 ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authorities`
--

LOCK TABLES `authorities` WRITE;
/*!40000 ALTER TABLE `authorities` DISABLE KEYS */;
INSERT INTO `authorities` VALUES ('a.benson@gmail.com','ROLE_USER'),
('s.cattoz@gmail.com','ROLE_USER'),
('m.felon@gmail.com','ROLE_USER'),
('l.tarot@gmail.com','ROLE_USER'),
('o.dolipe@gmail.com','ROLE_USER'),
('admin','ROLE_USER');
/*!40000 ALTER TABLE `authorities` ENABLE KEYS */;
UNLOCK TABLES;

CREATE UNIQUE INDEX `ix_auth_username` ON `authorities`(`username`,`role`);


-- Dump completed on 2020-12-17 22:24:54
