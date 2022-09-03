-- MySQL dump 10.13  Distrib 8.0.30, for macos12.4 (arm64)
--
-- Host: 127.0.0.1    Database: voting_system
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

--
-- Table structure for table `elector`
--

DROP TABLE IF EXISTS `elector`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `elector` (
  `id` int NOT NULL,
  `email` varchar(320) NOT NULL,
  `first_name` varchar(64) NOT NULL,
  `last_name` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `elector_ibfk_1` FOREIGN KEY (`id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `elector`
--

LOCK TABLES `elector` WRITE;
/*!40000 ALTER TABLE `elector` DISABLE KEYS */;
INSERT INTO `elector` (`id`, `email`, `first_name`, `last_name`) VALUES (9,'asdasd@gmail.com','Daniel','Balanica'),(20,'asdasd@gmail.com','Dal','Balani'),(28,'asdasd@gmail.com','Dalaaa','Balani'),(31,'asdasd@gmail.com','Dalaaa','Balani');
/*!40000 ALTER TABLE `elector` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `elector_group`
--

DROP TABLE IF EXISTS `elector_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `elector_group` (
  `elector_id` int NOT NULL,
  `voting_group_id` int NOT NULL,
  PRIMARY KEY (`elector_id`,`voting_group_id`),
  KEY `voting_group_id` (`voting_group_id`),
  CONSTRAINT `elector_group_ibfk_1` FOREIGN KEY (`elector_id`) REFERENCES `elector` (`id`),
  CONSTRAINT `elector_group_ibfk_2` FOREIGN KEY (`voting_group_id`) REFERENCES `voting_group` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `elector_group`
--

LOCK TABLES `elector_group` WRITE;
/*!40000 ALTER TABLE `elector_group` DISABLE KEYS */;
INSERT INTO `elector_group` (`elector_id`, `voting_group_id`) VALUES (20,15),(31,15);
/*!40000 ALTER TABLE `elector_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `session_group`
--

DROP TABLE IF EXISTS `session_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `session_group` (
  `voting_session_id` int NOT NULL,
  `voting_group_id` int NOT NULL,
  PRIMARY KEY (`voting_session_id`,`voting_group_id`),
  KEY `session_group_ibfk_1` (`voting_group_id`),
  CONSTRAINT `session_group_ibfk_1` FOREIGN KEY (`voting_group_id`) REFERENCES `voting_group` (`id`) ON DELETE CASCADE,
  CONSTRAINT `session_group_ibfk_2` FOREIGN KEY (`voting_session_id`) REFERENCES `voting_session` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `session_group`
--

LOCK TABLES `session_group` WRITE;
/*!40000 ALTER TABLE `session_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `session_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `session_participation`
--

DROP TABLE IF EXISTS `session_participation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `session_participation` (
  `elector_id` int NOT NULL,
  `voting_session_id` int NOT NULL,
  `has_voted` tinyint(1) NOT NULL,
  PRIMARY KEY (`elector_id`,`voting_session_id`),
  KEY `vote_voting_session_id_fk` (`voting_session_id`),
  CONSTRAINT `vote_elector_id_fk` FOREIGN KEY (`elector_id`) REFERENCES `elector` (`id`),
  CONSTRAINT `vote_voting_session_id_fk` FOREIGN KEY (`voting_session_id`) REFERENCES `voting_session` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `session_participation`
--

LOCK TABLES `session_participation` WRITE;
/*!40000 ALTER TABLE `session_participation` DISABLE KEYS */;
/*!40000 ALTER TABLE `session_participation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(64) NOT NULL,
  `password` varchar(64) NOT NULL,
  `role` enum('ELECTOR','MANAGER') NOT NULL DEFAULT 'ELECTOR',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `username_index` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `username`, `password`, `role`) VALUES (1,'admin','$2a$10$FOZRBerLcj0lMXhdIfcCL.CoKcwkQgKTD3BOdQzR3BScQvo/7DP3a','MANAGER'),(9,'username','$2a$10$x3f1AAUXECBC760p8QP.5.a0gjSFK/HKjM4.jcbncnI2cgu.3xtk6','ELECTOR'),(18,'otherUsername','$2a$10$ZzPWsITqvcTUTxbIKpPwve4Cp2VvA5o2vc14h.LwY63i9IweNbOR.','ELECTOR'),(20,'Dal.Balani1','$2a$10$B3sgLmqxmdWC3fb/WnV2EudMNCKmZzw9qPwghJcukOVsurrnMZwVi','ELECTOR'),(28,'otherUsername2','$2a$10$uiRmdU/U6t/WJgEg/jI7/.66XvvP6vCxZS3R4Eo8cNwZ24SiKjuwC','ELECTOR'),(31,'otherUsername2sd','$2a$10$c44GJqCDZm/1aDxYLB4/LeyXBDuxrO4AFPmT79HW8PQpwRqigAzpG','ELECTOR');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vote`
--

DROP TABLE IF EXISTS `vote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vote` (
  `voting_option_id` int NOT NULL,
  `order_idx` int NOT NULL,
  `id` int NOT NULL,
  PRIMARY KEY (`voting_option_id`,`id`,`order_idx`),
  KEY `table_name_voting_session_id_fk` (`voting_option_id`),
  CONSTRAINT `table_name_voting_session_id_fk` FOREIGN KEY (`voting_option_id`) REFERENCES `voting_option` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vote`
--

LOCK TABLES `vote` WRITE;
/*!40000 ALTER TABLE `vote` DISABLE KEYS */;
/*!40000 ALTER TABLE `vote` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `voting_group`
--

DROP TABLE IF EXISTS `voting_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `voting_group` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `group_name_index` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `voting_group`
--

LOCK TABLES `voting_group` WRITE;
/*!40000 ALTER TABLE `voting_group` DISABLE KEYS */;
INSERT INTO `voting_group` (`id`, `name`) VALUES (6,'asdasd'),(1,'Group0'),(11,'Grousdsp0'),(13,'Grousdssdp0'),(15,'okoko');
/*!40000 ALTER TABLE `voting_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `voting_option`
--

DROP TABLE IF EXISTS `voting_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `voting_option` (
  `id` int NOT NULL AUTO_INCREMENT,
  `voting_session_id` int NOT NULL,
  `option_value` varchar(255) NOT NULL,
  `parent_option_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `voting_session_id` (`voting_session_id`),
  KEY `voting_option_voting_option_id_fk` (`parent_option_id`),
  CONSTRAINT `voting_option_ibfk_1` FOREIGN KEY (`voting_session_id`) REFERENCES `voting_session` (`id`),
  CONSTRAINT `voting_option_voting_option_id_fk` FOREIGN KEY (`parent_option_id`) REFERENCES `voting_option` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `voting_option`
--

LOCK TABLES `voting_option` WRITE;
/*!40000 ALTER TABLE `voting_option` DISABLE KEYS */;
/*!40000 ALTER TABLE `voting_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `voting_session`
--

DROP TABLE IF EXISTS `voting_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `voting_session` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `ends_on` datetime NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '0',
  `state` varchar(64) NOT NULL DEFAULT 'INACTIVE',
  `is_cancelled` tinyint(1) NOT NULL DEFAULT '0',
  `need_absolute_majority` tinyint(1) NOT NULL,
  `has_quorum` tinyint(1) NOT NULL,
  `type` varchar(64) NOT NULL,
  `has_ended` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  CONSTRAINT `check_name` CHECK ((`type` in (_utf8mb4'CATEGORIC_WITH_PREFERENCES',_utf8mb4'CATEGORIC',_utf8mb4'ORDINAL',_utf8mb4'REFERENDUM'))),
  CONSTRAINT `check_state` CHECK ((`state` in (_utf8mb4'INACTIVE',_utf8mb4'ACTIVE',_utf8mb4'CANCELLED',_utf8mb4'ENDED')))
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `voting_session`
--

LOCK TABLES `voting_session` WRITE;
/*!40000 ALTER TABLE `voting_session` DISABLE KEYS */;
INSERT INTO `voting_session` (`id`, `name`, `ends_on`, `is_active`, `state`, `is_cancelled`, `need_absolute_majority`, `has_quorum`, `type`, `has_ended`) VALUES (17,'today','2022-09-02 17:14:21',0,'CANCELLED',0,1,1,'CATEGORIC_WITH_PREFERENCES',0),(18,'today','2022-09-02 17:14:21',0,'INACTIVE',0,1,1,'CATEGORIC_WITH_PREFERENCES',0),(19,'today','2022-09-02 17:14:21',0,'INACTIVE',0,1,1,'CATEGORIC_WITH_PREFERENCES',0),(20,'today','2022-09-02 17:14:21',0,'INACTIVE',0,1,1,'CATEGORIC_WITH_PREFERENCES',0),(21,'asdasdsa','2022-09-02 17:14:21',0,'INACTIVE',0,1,1,'CATEGORIC_WITH_PREFERENCES',0),(22,'sadasdererewrwer','2022-09-02 22:14:21',0,'INACTIVE',0,1,1,'CATEGORIC_WITH_PREFERENCES',0),(23,'sadasdererewrwer','2022-09-02 22:14:21',0,'INACTIVE',0,1,1,'CATEGORIC_WITH_PREFERENCES',0),(24,'sadasdererewrwer','2022-09-02 22:14:21',0,'INACTIVE',0,1,1,'CATEGORIC_WITH_PREFERENCES',0),(25,'sadasdererewrwer','2022-09-02 22:14:21',0,'INACTIVE',0,1,1,'CATEGORIC_WITH_PREFERENCES',0),(26,'sadasdererewrwer','2022-09-02 22:14:21',0,'INACTIVE',0,1,1,'CATEGORIC_WITH_PREFERENCES',0),(27,'sadasdererewrwer','2022-09-02 22:14:21',0,'INACTIVE',0,1,1,'CATEGORIC_WITH_PREFERENCES',0),(28,'sadasdererewrwer','2022-09-02 22:14:21',0,'INACTIVE',0,1,1,'CATEGORIC_WITH_PREFERENCES',0),(29,'sadasdererewrwer','2022-09-02 22:14:21',0,'INACTIVE',0,1,1,'CATEGORIC_WITH_PREFERENCES',0),(30,'sadasdererewrwer','2022-09-02 22:14:21',0,'INACTIVE',0,1,1,'CATEGORIC_WITH_PREFERENCES',0);
/*!40000 ALTER TABLE `voting_session` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-09-03 12:11:57
