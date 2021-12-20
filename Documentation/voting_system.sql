-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:8889
-- Generation Time: Dec 20, 2021 at 01:21 AM
-- Server version: 10.4.22-MariaDB
-- PHP Version: 8.0.13

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
-- Table structure for table `categoric`
--

CREATE TABLE `categoric` (
  `voting_session_id` int(11) NOT NULL,
  `with_preferences` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `elector`
--

CREATE TABLE `elector` (
  `id` int(11) NOT NULL,
  `username` varchar(64) NOT NULL,
  `password` varchar(64) NOT NULL,
  `email` varchar(320) NOT NULL,
  `first_name` varchar(64) NOT NULL,
  `last_name` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `elector`
--

INSERT INTO `elector` (`id`, `username`, `password`, `email`, `first_name`, `last_name`) VALUES
(1, 'ciao', '6e6bc4e49dd477ebc98ef4046c067b5f', 'ciao@owo.owo', 'ciao', 'coso'),
(2, 'assignment', '9b6d488d7170bcedde6a36d059255b0f', 'assignment@cinque.sweng', 'Ass', '5');

-- --------------------------------------------------------

--
-- Table structure for table `elector_group`
--

CREATE TABLE `elector_group` (
  `elector_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `manager`
--

CREATE TABLE `manager` (
  `id` int(11) NOT NULL,
  `username` varchar(64) NOT NULL,
  `password` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `option_element`
--

CREATE TABLE `option_element` (
  `option_id` int(11) NOT NULL,
  `element_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ordinal`
--

CREATE TABLE `ordinal` (
  `voting_session_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `referendum`
--

CREATE TABLE `referendum` (
  `voting_session_id` int(11) NOT NULL,
  `quorum` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `vote`
--

CREATE TABLE `vote` (
  `elector_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `voting_session_id` int(11) NOT NULL,
  `has_voted` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `voting_group`
--

CREATE TABLE `voting_group` (
  `id` int(11) NOT NULL,
  `name` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `voting_option`
--

CREATE TABLE `voting_option` (
  `id` int(11) NOT NULL,
  `voting_session_id` int(11) NOT NULL,
  `option_value` int(11) NOT NULL,
  `has_elements` tinyint(1) NOT NULL,
  `votes` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `voting_session`
--

CREATE TABLE `voting_session` (
  `id` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `ends_on` datetime NOT NULL,
  `active` tinyint(1) NOT NULL,
  `cancelled` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categoric`
--
ALTER TABLE `categoric`
  ADD PRIMARY KEY (`voting_session_id`);

--
-- Indexes for table `elector`
--
ALTER TABLE `elector`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `elector_group`
--
ALTER TABLE `elector_group`
  ADD PRIMARY KEY (`elector_id`,`group_id`),
  ADD KEY `group_id` (`group_id`);

--
-- Indexes for table `manager`
--
ALTER TABLE `manager`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `option_element`
--
ALTER TABLE `option_element`
  ADD PRIMARY KEY (`option_id`,`element_id`),
  ADD KEY `element_id` (`element_id`);

--
-- Indexes for table `ordinal`
--
ALTER TABLE `ordinal`
  ADD PRIMARY KEY (`voting_session_id`);

--
-- Indexes for table `referendum`
--
ALTER TABLE `referendum`
  ADD PRIMARY KEY (`voting_session_id`);

--
-- Indexes for table `vote`
--
ALTER TABLE `vote`
  ADD PRIMARY KEY (`elector_id`,`group_id`,`voting_session_id`),
  ADD KEY `group_id` (`group_id`),
  ADD KEY `voting_session_id` (`voting_session_id`);

--
-- Indexes for table `voting_group`
--
ALTER TABLE `voting_group`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `voting_option`
--
ALTER TABLE `voting_option`
  ADD PRIMARY KEY (`id`),
  ADD KEY `voting_session_id` (`voting_session_id`);

--
-- Indexes for table `voting_session`
--
ALTER TABLE `voting_session`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `elector`
--
ALTER TABLE `elector`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `manager`
--
ALTER TABLE `manager`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `voting_group`
--
ALTER TABLE `voting_group`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `voting_option`
--
ALTER TABLE `voting_option`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `voting_session`
--
ALTER TABLE `voting_session`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `categoric`
--
ALTER TABLE `categoric`
  ADD CONSTRAINT `categoric_ibfk_1` FOREIGN KEY (`voting_session_id`) REFERENCES `voting_session` (`id`);

--
-- Constraints for table `elector_group`
--
ALTER TABLE `elector_group`
  ADD CONSTRAINT `elector_group_ibfk_1` FOREIGN KEY (`elector_id`) REFERENCES `elector` (`id`),
  ADD CONSTRAINT `elector_group_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `voting_group` (`id`);

--
-- Constraints for table `option_element`
--
ALTER TABLE `option_element`
  ADD CONSTRAINT `option_element_ibfk_1` FOREIGN KEY (`option_id`) REFERENCES `voting_option` (`id`),
  ADD CONSTRAINT `option_element_ibfk_2` FOREIGN KEY (`element_id`) REFERENCES `voting_option` (`id`);

--
-- Constraints for table `ordinal`
--
ALTER TABLE `ordinal`
  ADD CONSTRAINT `ordinal_ibfk_1` FOREIGN KEY (`voting_session_id`) REFERENCES `voting_session` (`id`);

--
-- Constraints for table `referendum`
--
ALTER TABLE `referendum`
  ADD CONSTRAINT `referendum_ibfk_1` FOREIGN KEY (`voting_session_id`) REFERENCES `voting_session` (`id`);

--
-- Constraints for table `vote`
--
ALTER TABLE `vote`
  ADD CONSTRAINT `vote_ibfk_1` FOREIGN KEY (`elector_id`) REFERENCES `elector` (`id`),
  ADD CONSTRAINT `vote_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `voting_group` (`id`),
  ADD CONSTRAINT `vote_ibfk_3` FOREIGN KEY (`voting_session_id`) REFERENCES `voting_session` (`id`);

--
-- Constraints for table `voting_option`
--
ALTER TABLE `voting_option`
  ADD CONSTRAINT `voting_option_ibfk_1` FOREIGN KEY (`voting_session_id`) REFERENCES `voting_session` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
