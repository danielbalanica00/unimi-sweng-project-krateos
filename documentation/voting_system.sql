-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 23, 2022 at 12:18 PM
-- Server version: 8.0.30
-- PHP Version: 8.1.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `voting_system`
--

-- --------------------------------------------------------

--
-- Table structure for table `elector`
--

CREATE TABLE `elector` (
  `id` int NOT NULL,
  `email` varchar(320) COLLATE utf8mb4_general_ci NOT NULL,
  `first_name` varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
  `last_name` varchar(64) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `elector`
--

INSERT INTO `elector` (`id`, `email`, `first_name`, `last_name`) VALUES
(9, 'asdasd@gmail.com', 'Daniel', 'Balanica'),
(20, 'asdasd@gmail.com', 'Dal', 'Balani'),
(33, 'dasdasd@gma.com', 'utente', 'uno'),
(34, 'sadasd@gmail.com', 'utente', 'due'),
(35, 'dasdas@gmail.com', 'utente', 'tre'),
(36, 'username4@example.com', 'utente', 'quattro'),
(37, 'username5@example.com', 'utente', 'cinque'),
(38, 'username6@example.com', 'utente', 'sei');

-- --------------------------------------------------------

--
-- Table structure for table `elector_group`
--

CREATE TABLE `elector_group` (
  `elector_id` int NOT NULL,
  `voting_group_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `elector_group`
--

INSERT INTO `elector_group` (`elector_id`, `voting_group_id`) VALUES
(9, 16),
(9, 17),
(33, 17),
(34, 17),
(35, 17),
(36, 17),
(37, 17),
(38, 17),
(9, 26),
(20, 26),
(34, 26),
(35, 26);

-- --------------------------------------------------------

--
-- Table structure for table `session_group`
--

CREATE TABLE `session_group` (
  `voting_session_id` int NOT NULL,
  `voting_group_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `session_group`
--

INSERT INTO `session_group` (`voting_session_id`, `voting_group_id`) VALUES
(64, 1),
(88, 1),
(89, 1),
(90, 1),
(64, 16),
(88, 16),
(89, 16),
(90, 16),
(78, 17),
(79, 17),
(86, 17),
(88, 17),
(89, 17),
(90, 17),
(86, 26),
(88, 26),
(89, 26),
(90, 26);

-- --------------------------------------------------------

--
-- Table structure for table `session_participation`
--

CREATE TABLE `session_participation` (
  `elector_id` int NOT NULL,
  `voting_session_id` int NOT NULL,
  `has_voted` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `session_participation`
--

INSERT INTO `session_participation` (`elector_id`, `voting_session_id`, `has_voted`) VALUES
(9, 17, 1),
(9, 64, 1),
(9, 78, 0),
(9, 79, 0),
(9, 86, 1),
(9, 88, 1),
(9, 89, 1),
(9, 90, 1),
(20, 64, 0),
(20, 86, 0),
(20, 88, 0),
(20, 89, 0),
(20, 90, 0),
(33, 78, 0),
(33, 79, 0),
(33, 86, 1),
(33, 88, 0),
(33, 89, 0),
(33, 90, 0),
(34, 78, 0),
(34, 79, 0),
(34, 86, 1),
(34, 88, 0),
(34, 89, 0),
(34, 90, 0),
(35, 78, 0),
(35, 79, 0),
(35, 86, 1),
(35, 88, 0),
(35, 89, 0),
(35, 90, 0);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int NOT NULL,
  `username` varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
  `role` enum('ELECTOR','MANAGER') COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'ELECTOR'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `username`, `password`, `role`) VALUES
(1, 'admin', '$2a$10$FOZRBerLcj0lMXhdIfcCL.CoKcwkQgKTD3BOdQzR3BScQvo/7DP3a', 'MANAGER'),
(9, 'username', '$2a$10$x3f1AAUXECBC760p8QP.5.a0gjSFK/HKjM4.jcbncnI2cgu.3xtk6', 'ELECTOR'),
(18, 'otherUsername', '$2a$10$ZzPWsITqvcTUTxbIKpPwve4Cp2VvA5o2vc14h.LwY63i9IweNbOR.', 'ELECTOR'),
(20, 'Dal.Balani1', '$2a$10$B3sgLmqxmdWC3fb/WnV2EudMNCKmZzw9qPwghJcukOVsurrnMZwVi', 'ELECTOR'),
(28, 'otherUsername2', '$2a$10$uiRmdU/U6t/WJgEg/jI7/.66XvvP6vCxZS3R4Eo8cNwZ24SiKjuwC', 'ELECTOR'),
(31, 'otherUsername2sd', '$2a$10$c44GJqCDZm/1aDxYLB4/LeyXBDuxrO4AFPmT79HW8PQpwRqigAzpG', 'ELECTOR'),
(33, 'username1', '$2a$10$qNdtUFtWqLtasmlsXAk4RurwrYfXWDZILhKUEI6aqE.QvYMdrW9uK', 'ELECTOR'),
(34, 'username2', '$2a$10$8SBgbAuhGvQFv2ueA64mRuQEXJoFHCtdkHYZUv5/LRjwLUaYifd/6', 'ELECTOR'),
(35, 'username3', '$2a$10$J5sYw6xULd9Hpx1AnEoMAeKMv740OKYwuVnHhw6XgyE5QRiimHGJ6', 'ELECTOR'),
(36, 'username4', '$2a$10$x6fQSrLnSJLEfg8MEi5Fnu1AjuBGptyeTYabDDD4TKRaASbJi3eQW', 'ELECTOR'),
(37, 'username5', '$2a$10$DHqzDdNVifQO9B2ww/qAguGpRQlyKi7X1/7pxJHfLbNqOApOELtgm', 'ELECTOR'),
(38, 'username6', '$2a$10$wTWYA0HXTgqEnABIF0ZTAOiOJcabAVHxZNpUcS31k8Gab27G/YLCa', 'ELECTOR');

-- --------------------------------------------------------

--
-- Table structure for table `vote`
--

CREATE TABLE `vote` (
  `voting_option_id` int NOT NULL,
  `order_idx` int NOT NULL,
  `id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `vote`
--

INSERT INTO `vote` (`voting_option_id`, `order_idx`, `id`) VALUES
(7, 1, 1),
(8, 2, 1),
(53, 1, 4),
(54, 2, 4),
(55, 3, 4),
(56, 4, 4),
(57, 5, 4),
(122, 1, 8),
(123, 1, 9),
(123, 1, 10),
(123, 1, 11),
(127, 1, 6),
(128, 3, 6),
(129, 5, 6),
(130, 4, 6),
(131, 2, 6),
(132, 6, 6),
(133, 1, 5),
(137, 1, 5),
(146, 1, 7);

-- --------------------------------------------------------

--
-- Table structure for table `voting_group`
--

CREATE TABLE `voting_group` (
  `id` int NOT NULL,
  `name` varchar(64) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `voting_group`
--

INSERT INTO `voting_group` (`id`, `name`) VALUES
(17, 'Gli username'),
(16, 'Group 1'),
(1, 'Group 2'),
(26, 'gruppo esempio');

-- --------------------------------------------------------

--
-- Table structure for table `voting_option`
--

CREATE TABLE `voting_option` (
  `id` int NOT NULL,
  `voting_session_id` int NOT NULL,
  `option_value` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `parent_option_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `voting_option`
--

INSERT INTO `voting_option` (`id`, `voting_session_id`, `option_value`, `parent_option_id`) VALUES
(7, 17, 'Ciao come va?', NULL),
(8, 17, 'Male', 7),
(9, 17, 'Bene', 7),
(53, 64, 'primo', NULL),
(54, 64, 'secondo', NULL),
(55, 64, 'terzo', NULL),
(56, 64, 'quarto', NULL),
(57, 64, 'quinto', NULL),
(99, 78, 'opzione 1', NULL),
(100, 78, 'opzione 2', NULL),
(101, 78, 'opzione 3', NULL),
(102, 78, 'opzione 4', NULL),
(103, 78, 'opzione 5', NULL),
(104, 78, 'opzione 6', NULL),
(105, 79, 'lorem ipsum dolor sit amet', NULL),
(106, 79, 'Yes', 105),
(107, 79, 'No', 105),
(120, 86, 'opzione 1', NULL),
(121, 86, 'opzione 2', NULL),
(122, 86, 'opzione 3', NULL),
(123, 86, 'opzione 4', NULL),
(124, 86, 'opzione 5', NULL),
(126, 88, 'lorem ipsum dolor sit amet', NULL),
(127, 89, 'opzione 1', NULL),
(128, 89, 'opzione 2', NULL),
(129, 89, 'opzione 3', NULL),
(130, 89, 'opzione 4', NULL),
(131, 89, 'opzione 5', NULL),
(132, 89, 'opzione 6', NULL),
(133, 90, 'opzione 1', NULL),
(134, 90, 'opzione 2', NULL),
(135, 90, 'opzione 3', NULL),
(136, 90, 'sottopzione 1.1', 133),
(137, 90, 'sottopzione 1.2', 133),
(138, 90, 'sottopzione 1.3', 133),
(139, 90, 'sottopzione 2.1', 134),
(140, 90, 'sottopzione 2.2', 134),
(141, 90, 'sottopzione 2.3', 134),
(142, 90, 'sottopzione 3.1', 135),
(143, 90, 'sottopzione 3.2', 135),
(144, 90, 'sottopzione 3.3', 135),
(145, 90, 'sottopzione 3.4', 135),
(146, 88, 'Yes', 126),
(147, 88, 'No', 126);

-- --------------------------------------------------------

--
-- Table structure for table `voting_session`
--

CREATE TABLE `voting_session` (
  `id` int NOT NULL,
  `name` varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
  `ends_on` datetime NOT NULL,
  `state` varchar(64) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'INACTIVE',
  `need_absolute_majority` tinyint(1) NOT NULL,
  `has_quorum` tinyint(1) NOT NULL,
  `type` varchar(64) COLLATE utf8mb4_general_ci NOT NULL
) ;

--
-- Dumping data for table `voting_session`
--

INSERT INTO `voting_session` (`id`, `name`, `ends_on`, `state`, `need_absolute_majority`, `has_quorum`, `type`) VALUES
(17, 'best fruit', '2022-09-02 17:14:21', 'INVALID', 1, 1, 'CATEGORIC_WITH_PREFERENCES'),
(64, 'films ranking', '2022-09-30 12:14:42', 'CANCELLED', 0, 0, 'ORDINAL'),
(78, 'test sessione 1', '2022-09-27 10:22:17', 'ACTIVE', 0, 0, 'CATEGORIC'),
(79, 'test session 2', '2022-09-26 10:23:07', 'ACTIVE', 0, 0, 'REFERENDUM'),
(86, 'sessione esempio', '2022-09-30 11:19:22', 'ENDED', 0, 0, 'CATEGORIC'),
(88, 'esempio referendum', '2022-09-28 13:14:56', 'ACTIVE', 0, 0, 'REFERENDUM'),
(89, 'esempio sessione ordinale', '2022-09-30 13:15:25', 'ACTIVE', 0, 0, 'ORDINAL'),
(90, 'esempio sessione categorica con preferenza', '2022-09-30 13:16:23', 'ACTIVE', 0, 0, 'CATEGORIC_WITH_PREFERENCES');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `elector`
--
ALTER TABLE `elector`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `elector_group`
--
ALTER TABLE `elector_group`
  ADD PRIMARY KEY (`elector_id`,`voting_group_id`),
  ADD KEY `voting_group_id` (`voting_group_id`);

--
-- Indexes for table `session_group`
--
ALTER TABLE `session_group`
  ADD PRIMARY KEY (`voting_session_id`,`voting_group_id`),
  ADD KEY `session_group_ibfk_1` (`voting_group_id`);

--
-- Indexes for table `session_participation`
--
ALTER TABLE `session_participation`
  ADD PRIMARY KEY (`elector_id`,`voting_session_id`),
  ADD KEY `vote_voting_session_id_fk` (`voting_session_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD KEY `username_index` (`username`);

--
-- Indexes for table `vote`
--
ALTER TABLE `vote`
  ADD PRIMARY KEY (`voting_option_id`,`id`,`order_idx`),
  ADD KEY `table_name_voting_session_id_fk` (`voting_option_id`);

--
-- Indexes for table `voting_group`
--
ALTER TABLE `voting_group`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `group_name_index` (`name`);

--
-- Indexes for table `voting_option`
--
ALTER TABLE `voting_option`
  ADD PRIMARY KEY (`id`),
  ADD KEY `voting_session_id` (`voting_session_id`),
  ADD KEY `voting_option_voting_option_id_fk` (`parent_option_id`);

--
-- Indexes for table `voting_session`
--
ALTER TABLE `voting_session`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;

--
-- AUTO_INCREMENT for table `voting_group`
--
ALTER TABLE `voting_group`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `voting_option`
--
ALTER TABLE `voting_option`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=148;

--
-- AUTO_INCREMENT for table `voting_session`
--
ALTER TABLE `voting_session`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `elector`
--
ALTER TABLE `elector`
  ADD CONSTRAINT `elector_ibfk_1` FOREIGN KEY (`id`) REFERENCES `user` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `elector_group`
--
ALTER TABLE `elector_group`
  ADD CONSTRAINT `elector_group_ibfk_1` FOREIGN KEY (`elector_id`) REFERENCES `elector` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `elector_group_ibfk_2` FOREIGN KEY (`voting_group_id`) REFERENCES `voting_group` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `session_group`
--
ALTER TABLE `session_group`
  ADD CONSTRAINT `session_group_ibfk_1` FOREIGN KEY (`voting_group_id`) REFERENCES `voting_group` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `session_group_ibfk_2` FOREIGN KEY (`voting_session_id`) REFERENCES `voting_session` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `session_participation`
--
ALTER TABLE `session_participation`
  ADD CONSTRAINT `vote_elector_id_fk` FOREIGN KEY (`elector_id`) REFERENCES `elector` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `vote_voting_session_id_fk` FOREIGN KEY (`voting_session_id`) REFERENCES `voting_session` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `vote`
--
ALTER TABLE `vote`
  ADD CONSTRAINT `table_name_voting_session_id_fk` FOREIGN KEY (`voting_option_id`) REFERENCES `voting_option` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `voting_option`
--
ALTER TABLE `voting_option`
  ADD CONSTRAINT `voting_option_ibfk_1` FOREIGN KEY (`voting_session_id`) REFERENCES `voting_session` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `voting_option_voting_option_id_fk` FOREIGN KEY (`parent_option_id`) REFERENCES `voting_option` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
