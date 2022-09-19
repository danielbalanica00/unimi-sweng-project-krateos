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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `elector`
--

LOCK TABLES `elector` WRITE;
/*!40000 ALTER TABLE `elector` DISABLE KEYS */;
INSERT INTO `elector` (`id`, `email`, `first_name`, `last_name`) VALUES (9,'asdasd@gmail.com','Daniel','Balanica'),(20,'asdasd@gmail.com','Dal','Balani'),(28,'asdasd@gmail.com','Dalaaa','Balani'),(31,'asdasd@gmail.com','Dalaaa','Balani'),(33,'dasdasd@gma.com','sad','asd'),(34,'sadasd@gmail.com','sd','j'),(35,'dasdas@gmail.com','sd','ds');
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
INSERT INTO `elector_group` (`elector_id`, `voting_group_id`) VALUES (20,15),(31,15),(9,16),(9,17),(33,17),(34,17),(35,17);
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
  CONSTRAINT `session_group_ibfk_2` FOREIGN KEY (`voting_session_id`) REFERENCES `voting_session` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `session_group`
--

LOCK TABLES `session_group` WRITE;
/*!40000 ALTER TABLE `session_group` DISABLE KEYS */;
INSERT INTO `session_group` (`voting_session_id`, `voting_group_id`) VALUES (32,1),(42,1),(43,1),(46,1),(56,1),(57,1),(59,1),(64,1),(41,6),(42,6),(45,6),(50,6),(59,6),(64,6),(32,11),(41,11),(47,11),(52,11),(56,11),(59,11),(64,11),(41,13),(44,13),(45,13),(47,13),(50,13),(52,13),(56,13),(57,13),(59,13),(64,13),(41,15),(59,15),(64,15),(58,16),(59,16),(64,16),(65,17),(66,17),(67,17);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `session_participation`
--

LOCK TABLES `session_participation` WRITE;
/*!40000 ALTER TABLE `session_participation` DISABLE KEYS */;
INSERT INTO `session_participation` (`elector_id`, `voting_session_id`, `has_voted`) VALUES (9,17,1),(9,58,1),(9,59,1),(9,64,1),(9,65,1),(9,66,0),(9,67,1),(20,59,0),(20,64,0),(31,59,0),(31,64,0),(33,65,1),(33,66,0),(33,67,0),(34,65,0),(34,66,0),(34,67,1),(35,65,1),(35,66,0),(35,67,0);
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
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `username`, `password`, `role`) VALUES (1,'admin','$2a$10$FOZRBerLcj0lMXhdIfcCL.CoKcwkQgKTD3BOdQzR3BScQvo/7DP3a','MANAGER'),(9,'username','$2a$10$x3f1AAUXECBC760p8QP.5.a0gjSFK/HKjM4.jcbncnI2cgu.3xtk6','ELECTOR'),(18,'otherUsername','$2a$10$ZzPWsITqvcTUTxbIKpPwve4Cp2VvA5o2vc14h.LwY63i9IweNbOR.','ELECTOR'),(20,'Dal.Balani1','$2a$10$B3sgLmqxmdWC3fb/WnV2EudMNCKmZzw9qPwghJcukOVsurrnMZwVi','ELECTOR'),(28,'otherUsername2','$2a$10$uiRmdU/U6t/WJgEg/jI7/.66XvvP6vCxZS3R4Eo8cNwZ24SiKjuwC','ELECTOR'),(31,'otherUsername2sd','$2a$10$c44GJqCDZm/1aDxYLB4/LeyXBDuxrO4AFPmT79HW8PQpwRqigAzpG','ELECTOR'),(33,'username1','$2a$10$qNdtUFtWqLtasmlsXAk4RurwrYfXWDZILhKUEI6aqE.QvYMdrW9uK','ELECTOR'),(34,'username2','$2a$10$8SBgbAuhGvQFv2ueA64mRuQEXJoFHCtdkHYZUv5/LRjwLUaYifd/6','ELECTOR'),(35,'username3','$2a$10$J5sYw6xULd9Hpx1AnEoMAeKMv740OKYwuVnHhw6XgyE5QRiimHGJ6','ELECTOR');
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vote`
--

LOCK TABLES `vote` WRITE;
/*!40000 ALTER TABLE `vote` DISABLE KEYS */;
INSERT INTO `vote` (`voting_option_id`, `order_idx`, `id`) VALUES (7,1,1),(8,2,1),(49,1,2),(51,1,3),(53,1,4),(54,2,4),(55,3,4),(56,4,4),(57,5,4),(58,1,5),(58,1,6),(58,4,7),(58,5,8),(59,2,5),(59,2,6),(59,5,7),(59,4,8),(60,3,5),(60,3,6),(60,1,7),(60,3,8),(61,4,5),(61,4,6),(61,2,7),(61,2,8),(62,5,5),(62,5,6),(62,3,7),(62,1,8),(66,1,9),(67,1,10);
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
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `voting_group`
--

LOCK TABLES `voting_group` WRITE;
/*!40000 ALTER TABLE `voting_group` DISABLE KEYS */;
INSERT INTO `voting_group` (`id`, `name`) VALUES (6,'asdasd'),(17,'gli username'),(1,'Group0'),(11,'Grousdsp0'),(13,'Grousdssdp0'),(20,'Grousdssdp0asdadasd'),(16,'gruppo1'),(15,'okoko');
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
  CONSTRAINT `voting_option_ibfk_1` FOREIGN KEY (`voting_session_id`) REFERENCES `voting_session` (`id`) ON DELETE CASCADE,
  CONSTRAINT `voting_option_voting_option_id_fk` FOREIGN KEY (`parent_option_id`) REFERENCES `voting_option` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `voting_option`
--

LOCK TABLES `voting_option` WRITE;
/*!40000 ALTER TABLE `voting_option` DISABLE KEYS */;
INSERT INTO `voting_option` (`id`, `voting_session_id`, `option_value`, `parent_option_id`) VALUES (7,17,'Ciao come va?',NULL),(8,17,'Male',7),(9,17,'Bene',7),(10,31,'ciaocai',NULL),(12,32,'3423423',NULL),(13,32,'asdasd',NULL),(14,32,'2132',NULL),(15,34,'ciao',NULL),(16,34,'hello',NULL),(17,34,'stocazzo',NULL),(18,35,'wqeqwe',NULL),(19,36,'asdasd',NULL),(20,37,'asdasdasd',NULL),(21,37,'asdasd',NULL),(22,38,'weweqwe',NULL),(23,41,'qweqweqe',NULL),(24,41,'weqweqwe',NULL),(25,41,'213',NULL),(26,42,'asdasdasd',NULL),(27,43,'asdasd',NULL),(28,44,'asdsadasd',NULL),(29,45,'wqeqwewqe',NULL),(30,46,'123123',NULL),(31,47,'1232131',NULL),(32,48,'wqe12e',NULL),(33,49,'qweqwe',NULL),(34,50,'1231231',NULL),(36,52,'do you wanna die?',NULL),(37,54,'asdasda',NULL),(38,55,'ciaoasddas',NULL),(39,55,'stoc azzo',NULL),(40,55,'sto altro cazzo',NULL),(41,55,'tua madre',NULL),(42,55,'tuo padre',NULL),(43,56,'tua madre',NULL),(44,56,'tuo padre',NULL),(45,56,'sto cazzo',NULL),(46,56,'sto altro cazzo',NULL),(47,57,'asdasd',NULL),(48,57,'asdasdasd',NULL),(49,58,'asdadasd',NULL),(50,58,'asdasd',NULL),(51,59,'opt1',NULL),(52,59,'opt2',NULL),(53,64,'primo',NULL),(54,64,'secondo',NULL),(55,64,'terzo',NULL),(56,64,'quarto',NULL),(57,64,'quinto',NULL),(58,65,'primo',NULL),(59,65,'secondo',NULL),(60,65,'terzo',NULL),(61,65,'quarto',NULL),(62,65,'quinto',NULL),(63,66,'a',NULL),(64,66,'b',NULL),(65,66,'c',NULL),(66,67,'a',NULL),(67,67,'b',NULL),(68,67,'c',NULL);
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
  `state` varchar(64) NOT NULL DEFAULT 'INACTIVE',
  `need_absolute_majority` tinyint(1) NOT NULL,
  `has_quorum` tinyint(1) NOT NULL,
  `type` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `check_name` CHECK ((`type` in (_utf8mb4'CATEGORIC_WITH_PREFERENCES',_utf8mb4'CATEGORIC',_utf8mb4'ORDINAL',_utf8mb4'REFERENDUM'))),
  CONSTRAINT `check_state` CHECK ((`state` in (_utf8mb4'INACTIVE',_utf8mb4'ACTIVE',_utf8mb4'CANCELLED',_utf8mb4'ENDED',_utf8mb4'INVALID')))
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `voting_session`
--

LOCK TABLES `voting_session` WRITE;
/*!40000 ALTER TABLE `voting_session` DISABLE KEYS */;
INSERT INTO `voting_session` (`id`, `name`, `ends_on`, `state`, `need_absolute_majority`, `has_quorum`, `type`) VALUES (17,'today','2022-09-02 17:14:21','INVALID',1,1,'CATEGORIC_WITH_PREFERENCES'),(18,'today','2022-09-02 17:14:21','INACTIVE',1,1,'CATEGORIC_WITH_PREFERENCES'),(19,'today','2022-09-02 17:14:21','INACTIVE',1,1,'CATEGORIC_WITH_PREFERENCES'),(20,'today','2022-09-02 17:14:21','INACTIVE',1,1,'CATEGORIC_WITH_PREFERENCES'),(21,'asdasdsa','2022-09-02 17:14:21','INACTIVE',1,1,'CATEGORIC_WITH_PREFERENCES'),(22,'sadasdererewrwer','2022-09-02 22:14:21','INACTIVE',1,1,'CATEGORIC_WITH_PREFERENCES'),(23,'sadasdererewrwer','2022-09-02 22:14:21','INACTIVE',1,1,'CATEGORIC_WITH_PREFERENCES'),(24,'sadasdererewrwer','2022-09-02 22:14:21','INACTIVE',1,1,'CATEGORIC_WITH_PREFERENCES'),(25,'sadasdererewrwer','2022-09-02 22:14:21','INACTIVE',1,1,'CATEGORIC_WITH_PREFERENCES'),(26,'sadasdererewrwer','2022-09-02 22:14:21','INACTIVE',1,1,'CATEGORIC_WITH_PREFERENCES'),(27,'sadasdererewrwer','2022-09-02 22:14:21','INACTIVE',1,1,'CATEGORIC_WITH_PREFERENCES'),(28,'sadasdererewrwer','2022-09-02 22:14:21','INACTIVE',1,1,'CATEGORIC_WITH_PREFERENCES'),(29,'sadasdererewrwer','2022-09-02 22:14:21','INACTIVE',1,1,'CATEGORIC_WITH_PREFERENCES'),(30,'sadasdererewrwer','2022-09-02 22:14:21','INACTIVE',1,1,'CATEGORIC_WITH_PREFERENCES'),(31,'ciao bellllooooo','2022-09-30 17:53:09','INACTIVE',1,1,'CATEGORIC'),(32,'otherr','2022-09-07 17:54:24','INACTIVE',0,1,'CATEGORIC'),(33,'asdasd','2022-09-08 20:06:25','INACTIVE',0,1,'CATEGORIC'),(34,'asd','2022-09-03 20:38:59','INACTIVE',0,0,'CATEGORIC'),(35,'qweqwe','2022-09-04 20:40:33','INACTIVE',0,0,'CATEGORIC'),(36,'asdasd','2022-09-23 20:42:11','INACTIVE',0,0,'CATEGORIC'),(37,'asdasd','2022-09-15 20:44:56','INACTIVE',0,1,'CATEGORIC'),(38,'asdasdasd','2022-09-22 20:46:24','INACTIVE',0,0,'CATEGORIC'),(41,'asdasdasd','2022-09-29 20:47:01','INACTIVE',1,1,'CATEGORIC'),(42,'asdsad','2022-09-14 21:04:22','INACTIVE',0,1,'CATEGORIC'),(43,'asdasdasd','2022-09-08 21:09:59','INACTIVE',0,1,'CATEGORIC'),(44,'asdasda','2022-09-09 21:12:10','INACTIVE',0,0,'CATEGORIC'),(45,'sadasdasdasd','2022-09-22 21:18:05','INACTIVE',0,1,'CATEGORIC'),(46,'dasdasd','2022-09-08 21:18:58','INACTIVE',0,0,'CATEGORIC'),(47,'asdasdsad','2022-09-28 21:20:46','INACTIVE',0,0,'CATEGORIC'),(48,'sadasdas','2022-09-22 21:28:02','INACTIVE',0,0,'CATEGORIC'),(49,'qweqwe','2022-09-15 21:28:13','INACTIVE',0,1,'CATEGORIC'),(50,'sdasdasd','2022-09-22 21:29:37','INACTIVE',0,0,'CATEGORIC'),(51,'this is a referendum','2022-09-21 22:30:35','INACTIVE',0,1,'REFERENDUM'),(52,'this is another referendum','2022-09-21 22:32:23','INACTIVE',0,1,'REFERENDUM'),(54,'asdasd','2022-09-09 22:33:57','INACTIVE',0,1,'REFERENDUM'),(55,'asdasd','2022-10-06 22:41:36','INACTIVE',1,1,'ORDINAL'),(56,'asdasd','2022-10-06 22:41:57','INACTIVE',1,1,'ORDINAL'),(57,'asdasd','2022-09-30 16:31:39','INACTIVE',1,1,'CATEGORIC'),(58,'newasadasd','2022-09-29 16:38:32','CANCELLED',1,1,'CATEGORIC'),(59,'elezionestupends','2022-09-28 17:53:14','ACTIVE',1,1,'CATEGORIC'),(60,'sessioneordinaleee','2022-09-30 12:13:45','INACTIVE',0,0,'ORDINAL'),(61,'sessioneordinaleee','2022-09-30 12:13:45','INACTIVE',0,0,'ORDINAL'),(62,'sessioneordinaleee','2022-09-30 12:13:48','INACTIVE',0,0,'ORDINAL'),(63,'sessioneordinaleee','2022-09-30 12:13:48','INACTIVE',0,0,'ORDINAL'),(64,'sessioneordinaleee','2022-09-30 12:14:42','ACTIVE',0,0,'ORDINAL'),(65,'ordinalenuovaebella','2022-09-29 12:20:30','ENDED',0,0,'ORDINAL'),(66,'categoricTest','2022-09-28 19:15:48','ENDED',0,0,'CATEGORIC'),(67,'nuovotest1','2022-09-28 19:18:11','CANCELLED',0,0,'CATEGORIC');
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

-- Dump completed on 2022-09-19 17:05:05
