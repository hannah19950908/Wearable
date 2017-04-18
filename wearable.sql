-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Host: localhost:3306
-- Generation Time: 2017-04-18 16:10:45
-- 服务器版本： 5.7.17-0ubuntu0.16.04.2
-- PHP Version: 7.0.15-0ubuntu0.16.04.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `wearable`
--

-- --------------------------------------------------------

--
-- 表的结构 `acc`
--

CREATE TABLE `acc` (
  `id` int(11) NOT NULL,
  `acountNumber` varchar(10) NOT NULL,
  `accX` int(11) NOT NULL,
  `accY` int(11) NOT NULL,
  `accZ` int(11) NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- 表的结构 `measure`
--

CREATE TABLE `measure` (
  `id` int(11) NOT NULL,
  `accountNumber` varchar(10) NOT NULL,
  `commitTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `device` varchar(100) DEFAULT NULL,
  `step` int(11) DEFAULT NULL,
  `distance` int(11) DEFAULT NULL,
  `heart` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- 表的结构 `myUser`
--

CREATE TABLE `myUser` (
  `accountNumber` varchar(10) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(100) NOT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `relativeName` varchar(20) DEFAULT NULL,
  `relativePhone` varchar(11) DEFAULT NULL,
  `userName` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- 转存表中的数据 `myUser`
--

INSERT INTO `myUser` (`accountNumber`, `email`, `password`, `phone`, `relativeName`, `relativePhone`, `userName`) VALUES
('002', 'cielosun@outlook.com', 'BpCFmIznGGadXqAI0EEM2Q==', '13246825184', 'Wendy', '18826077150', 'Boris'),
('003', 'cielosun@outlook.com', 'BpCFmIznGGadXqAI0EEM2Q==', '13246825184', 'Wendy', '18826077150', 'Scott');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `acc`
--
ALTER TABLE `acc`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `measure`
--
ALTER TABLE `measure`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `myUser`
--
ALTER TABLE `myUser`
  ADD PRIMARY KEY (`accountNumber`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `acc`
--
ALTER TABLE `acc`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- 使用表AUTO_INCREMENT `measure`
--
ALTER TABLE `measure`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
