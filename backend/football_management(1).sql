-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jun 22, 2026 at 03:03 AM
-- Server version: 8.0.30
-- PHP Version: 8.4.21
CREATE DATABASE football_management;
USE football_management;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `football_management`
--

-- --------------------------------------------------------

--
-- Table structure for table `articles`
--

CREATE TABLE `articles` (
  `id` int NOT NULL,
  `title` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `slug` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `cover_image` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` enum('draft','published','archived') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'draft',
  `user_id` int NOT NULL,
  `season_id` int DEFAULT NULL,
  `match_id` int DEFAULT NULL,
  `team_id` int DEFAULT NULL,
  `published_at` datetime(3) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `article_media`
--

CREATE TABLE `article_media` (
  `id` int NOT NULL,
  `article_id` int NOT NULL,
  `type` enum('image','video') COLLATE utf8mb4_unicode_ci NOT NULL,
  `url` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `caption` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `order` int NOT NULL DEFAULT '0',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `article_tags`
--

CREATE TABLE `article_tags` (
  `id` int NOT NULL,
  `article_id` int NOT NULL,
  `tag` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `bracket_slots`
--

CREATE TABLE `bracket_slots` (
  `id` int NOT NULL,
  `phase_id` int NOT NULL,
  `round` int NOT NULL,
  `slot_number` int NOT NULL,
  `match_id` int DEFAULT NULL,
  `source_a_slot_id` int DEFAULT NULL,
  `source_b_slot_id` int DEFAULT NULL,
  `seeded_home_team_id` int DEFAULT NULL,
  `seeded_away_team_id` int DEFAULT NULL,
  `is_bye` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `groups`
--

CREATE TABLE `groups` (
  `id` int NOT NULL,
  `phase_id` int NOT NULL,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `scheduleGeneratedAt` datetime(3) DEFAULT NULL,
  `status` enum('DRAFT','LOCKED','SCHEDULED','SCHEDULE_FAILED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DRAFT'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `groups`
--

INSERT INTO `groups` (`id`, `phase_id`, `name`, `is_active`, `created_at`, `updated_at`, `scheduleGeneratedAt`, `status`) VALUES
(3, 1, 'Bảng A', 1, '2026-06-20 06:17:11.384', '2026-06-20 06:17:11.384', NULL, 'DRAFT'),
(4, 1, 'Bảng B', 1, '2026-06-20 06:17:11.394', '2026-06-20 06:17:11.394', NULL, 'DRAFT'),
(5, 6, 'Bảng A', 1, '2026-06-20 15:30:23.332', '2026-06-20 15:30:23.332', NULL, 'DRAFT'),
(6, 7, 'Bảng A', 1, '2026-06-20 15:30:23.464', '2026-06-20 15:30:23.464', NULL, 'DRAFT'),
(7, 8, 'Bảng A', 1, '2026-06-20 15:30:23.559', '2026-06-20 15:30:23.559', NULL, 'DRAFT'),
(8, 8, 'Bảng B', 1, '2026-06-20 15:30:23.572', '2026-06-20 15:30:23.572', NULL, 'DRAFT'),
(9, 9, 'Bảng A', 1, '2026-06-20 15:30:23.687', '2026-06-20 15:30:23.687', NULL, 'DRAFT'),
(10, 9, 'Bảng B', 1, '2026-06-20 15:30:23.702', '2026-06-20 15:30:23.702', NULL, 'DRAFT'),
(22, 18, 'Bảng A', 1, '2026-06-20 15:33:28.046', '2026-06-20 15:33:28.046', NULL, 'DRAFT'),
(23, 18, 'Bảng B', 1, '2026-06-20 15:33:28.051', '2026-06-20 15:33:28.051', NULL, 'DRAFT');

-- --------------------------------------------------------

--
-- Table structure for table `matches`
--

CREATE TABLE `matches` (
  `id` int NOT NULL,
  `phase_id` int NOT NULL,
  `group_id` int DEFAULT NULL,
  `home_team_id` int NOT NULL,
  `away_team_id` int NOT NULL,
  `scheduled_at` datetime(3) DEFAULT NULL,
  `played_at` datetime(3) DEFAULT NULL,
  `home_score` int DEFAULT NULL,
  `away_score` int DEFAULT NULL,
  `status` enum('scheduled','ongoing','finished','cancelled','forfeited','postponed','bye','abandoned') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'scheduled',
  `round` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `leg` int DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `venue_id` int DEFAULT NULL,
  `is_published` tinyint(1) NOT NULL DEFAULT '0',
  `referee` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `season_id` int DEFAULT NULL,
  `abandoned_minute` int DEFAULT NULL,
  `current_period` enum('first_half','second_half','extra_time_first','extra_time_second','penalty_shootout') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `postponed_from` datetime(3) DEFAULT NULL,
  `postponed_reason` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `replay_of_match_id` int DEFAULT NULL,
  `is_featured` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `matches`
--

INSERT INTO `matches` (`id`, `phase_id`, `group_id`, `home_team_id`, `away_team_id`, `scheduled_at`, `played_at`, `home_score`, `away_score`, `status`, `round`, `leg`, `is_active`, `created_at`, `updated_at`, `deleted_at`, `user_id`, `venue_id`, `is_published`, `referee`, `season_id`, `abandoned_minute`, `current_period`, `postponed_from`, `postponed_reason`, `replay_of_match_id`) VALUES
(16, 1, 3, 1, 2, '2024-03-01 00:00:00.000', '2024-03-01 00:00:00.000', 3, 2, 'finished', NULL, NULL, 1, '2026-06-20 06:17:11.499', '2026-06-20 06:17:11.499', NULL, 1, 1, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL),
(17, 1, 3, 1, 3, '2024-03-04 00:00:00.000', '2024-03-04 00:00:00.000', 2, 0, 'finished', NULL, NULL, 1, '2026-06-20 06:17:11.683', '2026-06-20 06:17:11.683', NULL, 1, 1, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL),
(18, 1, 3, 1, 4, '2024-03-07 00:00:00.000', '2024-03-07 00:00:00.000', 0, 3, 'finished', NULL, NULL, 1, '2026-06-20 06:17:11.796', '2026-06-20 06:17:11.796', NULL, 1, 1, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL),
(19, 1, 3, 2, 3, '2024-03-10 00:00:00.000', '2024-03-10 00:00:00.000', 3, 2, 'finished', NULL, NULL, 1, '2026-06-20 06:17:11.910', '2026-06-20 06:17:11.910', NULL, 1, 1, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL),
(20, 1, 3, 2, 4, '2024-03-13 00:00:00.000', '2024-03-13 00:00:00.000', 3, 1, 'finished', NULL, NULL, 1, '2026-06-20 06:17:12.057', '2026-06-20 06:17:12.057', NULL, 1, 1, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL),
(21, 1, 3, 3, 4, '2024-03-16 00:00:00.000', '2024-03-16 00:00:00.000', 1, 4, 'finished', NULL, NULL, 1, '2026-06-20 06:17:12.151', '2026-06-20 06:17:12.151', NULL, 1, 1, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL),
(23, 1, 3, 1, 2, '2024-03-01 08:00:00.000', '2024-03-01 08:00:00.000', 1, 0, 'finished', NULL, NULL, 1, '2026-06-20 06:34:24.453', '2026-06-20 06:34:24.453', NULL, 1, 1, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL),
(24, 1, 3, 1, 3, '2024-03-04 08:00:00.000', '2024-03-04 08:00:00.000', 1, 2, 'finished', NULL, NULL, 1, '2026-06-20 06:34:24.530', '2026-06-20 06:34:24.530', NULL, 1, 1, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL),
(25, 1, 3, 1, 4, '2024-03-07 08:00:00.000', '2024-03-07 08:00:00.000', 1, 1, 'finished', NULL, NULL, 1, '2026-06-20 06:34:24.646', '2026-06-20 06:34:24.646', NULL, 1, 1, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL),
(26, 1, 3, 2, 3, '2024-03-10 08:00:00.000', '2024-03-10 08:00:00.000', 0, 2, 'finished', NULL, NULL, 1, '2026-06-20 06:34:24.742', '2026-06-20 06:34:24.742', NULL, 1, 1, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL),
(27, 1, 3, 2, 4, '2024-03-13 08:00:00.000', '2024-03-13 08:00:00.000', 2, 3, 'finished', NULL, NULL, 1, '2026-06-20 06:34:24.832', '2026-06-20 06:34:24.832', NULL, 1, 1, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL),
(28, 1, 3, 3, 4, '2024-03-16 08:00:00.000', '2024-03-16 08:00:00.000', 2, 2, 'finished', NULL, NULL, 1, '2026-06-20 06:34:24.965', '2026-06-20 06:34:24.965', NULL, 1, 1, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL),
(29, 1, 4, 5, 6, '2024-03-02 08:00:00.000', '2024-03-02 08:00:00.000', 3, 2, 'finished', NULL, NULL, 1, '2026-06-20 06:34:25.148', '2026-06-20 06:34:25.148', NULL, 1, 1, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL),
(30, 1, 4, 5, 7, '2024-03-05 08:00:00.000', '2024-03-05 08:00:00.000', 1, 4, 'finished', NULL, NULL, 1, '2026-06-20 06:34:25.295', '2026-06-20 06:34:25.295', NULL, 1, 1, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL),
(31, 1, 4, 5, 8, '2024-03-08 08:00:00.000', '2024-03-08 08:00:00.000', 1, 1, 'finished', NULL, NULL, 1, '2026-06-20 06:34:25.402', '2026-06-20 06:34:25.402', NULL, 1, 1, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL),
(32, 1, 4, 6, 7, '2024-03-11 08:00:00.000', '2024-03-11 08:00:00.000', 0, 2, 'finished', NULL, NULL, 1, '2026-06-20 06:34:25.482', '2026-06-20 06:34:25.482', NULL, 1, 1, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL),
(33, 1, 4, 6, 8, '2024-03-14 08:00:00.000', '2024-03-14 08:00:00.000', 2, 1, 'finished', NULL, NULL, 1, '2026-06-20 06:34:25.537', '2026-06-20 06:34:25.537', NULL, 1, 1, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL),
(34, 1, 4, 7, 8, '2024-03-17 08:00:00.000', '2024-03-17 08:00:00.000', 1, 3, 'finished', NULL, NULL, 1, '2026-06-20 06:34:25.658', '2026-06-20 06:34:25.658', NULL, 1, 1, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL),
(35, 2, NULL, 4, 5, '2024-04-20 08:00:00.000', '2024-04-20 08:00:00.000', 1, 1, 'finished', NULL, NULL, 1, '2026-06-20 06:34:25.774', '2026-06-20 06:34:25.774', NULL, 1, 1, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL),
(36, 2, NULL, 7, 1, '2024-04-23 08:00:00.000', '2024-04-23 08:00:00.000', 3, 3, 'finished', NULL, NULL, 1, '2026-06-20 06:34:25.792', '2026-06-20 06:34:25.792', NULL, 1, 1, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL),
(37, 3, NULL, 4, 1, '2024-05-05 08:00:00.000', '2024-05-05 08:00:00.000', 1, 2, 'finished', NULL, NULL, 1, '2026-06-20 06:34:25.816', '2026-06-20 06:34:25.816', NULL, 1, 1, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL),
(38, 18, 22, 5, 6, '2027-06-01 01:00:00.000', NULL, NULL, NULL, 'scheduled', '1', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 1, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(39, 18, 22, 1, 4, '2027-06-01 01:00:00.000', NULL, NULL, NULL, 'scheduled', '1', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 2, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(40, 18, 22, 4, 5, '2027-06-03 01:00:00.000', NULL, NULL, NULL, 'scheduled', '2', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 1, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(41, 18, 22, 1, 6, '2027-06-03 01:00:00.000', NULL, NULL, NULL, 'scheduled', '2', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 2, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(42, 18, 22, 5, 1, '2027-06-05 01:00:00.000', NULL, NULL, NULL, 'scheduled', '3', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 1, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(43, 18, 22, 4, 6, '2027-06-05 01:00:00.000', NULL, NULL, NULL, 'scheduled', '3', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 2, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(44, 18, 22, 6, 5, '2027-06-07 01:00:00.000', NULL, NULL, NULL, 'scheduled', '4', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 1, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(45, 18, 22, 4, 1, '2027-06-07 01:00:00.000', NULL, NULL, NULL, 'scheduled', '4', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 2, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(46, 18, 22, 5, 4, '2027-06-09 01:00:00.000', NULL, NULL, NULL, 'scheduled', '5', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 1, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(47, 18, 22, 6, 1, '2027-06-09 01:00:00.000', NULL, NULL, NULL, 'scheduled', '5', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 2, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(48, 18, 22, 1, 5, '2027-06-11 01:00:00.000', NULL, NULL, NULL, 'scheduled', '6', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 1, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(49, 18, 22, 6, 4, '2027-06-11 01:00:00.000', NULL, NULL, NULL, 'scheduled', '6', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 2, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(50, 18, 23, 7, 3, '2027-06-01 01:00:00.000', NULL, NULL, NULL, 'scheduled', '1', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 3, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(51, 18, 23, 8, 2, '2027-06-01 01:00:00.000', NULL, NULL, NULL, 'scheduled', '1', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 4, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(52, 18, 23, 2, 7, '2027-06-03 01:00:00.000', NULL, NULL, NULL, 'scheduled', '2', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 3, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(53, 18, 23, 8, 3, '2027-06-03 01:00:00.000', NULL, NULL, NULL, 'scheduled', '2', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 4, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(54, 18, 23, 7, 8, '2027-06-05 01:00:00.000', NULL, NULL, NULL, 'scheduled', '3', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 3, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(55, 18, 23, 2, 3, '2027-06-05 01:00:00.000', NULL, NULL, NULL, 'scheduled', '3', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 4, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(56, 18, 23, 3, 7, '2027-06-07 01:00:00.000', NULL, NULL, NULL, 'scheduled', '4', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 3, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(57, 18, 23, 2, 8, '2027-06-07 01:00:00.000', NULL, NULL, NULL, 'scheduled', '4', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 4, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(58, 18, 23, 7, 2, '2027-06-09 01:00:00.000', NULL, NULL, NULL, 'scheduled', '5', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 3, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(59, 18, 23, 3, 8, '2027-06-09 01:00:00.000', NULL, NULL, NULL, 'scheduled', '5', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 4, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(60, 18, 23, 8, 7, '2027-06-11 01:00:00.000', NULL, NULL, NULL, 'scheduled', '6', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 3, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL),
(61, 18, 23, 3, 2, '2027-06-11 01:00:00.000', NULL, NULL, NULL, 'scheduled', '6', NULL, 1, '2026-06-20 15:33:28.097', '2026-06-20 15:33:28.097', NULL, NULL, 4, 0, NULL, 11, NULL, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `match_events`
--

CREATE TABLE `match_events` (
  `id` int NOT NULL,
  `match_id` int NOT NULL,
  `player_id` int DEFAULT NULL,
  `team_id` int DEFAULT NULL,
  `type` enum('goal','own_goal','yellow_card','red_card','second_yellow','substitution_in','substitution_out','penalty_scored','penalty_missed','card_rescinded','goal_disallowed') COLLATE utf8mb4_unicode_ci NOT NULL,
  `minute` int DEFAULT NULL,
  `note` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `period` enum('first_half','second_half','extra_time_first','extra_time_second','penalty_shootout') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `added_minute` int DEFAULT NULL,
  `card_color` enum('yellow','red') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sub_out_player_id` int DEFAULT NULL,
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `match_events`
--

INSERT INTO `match_events` (`id`, `match_id`, `player_id`, `team_id`, `type`, `minute`, `note`, `period`, `added_minute`, `card_color`, `sub_out_player_id`, `created_at`) VALUES
(54, 16, 2, 1, 'goal', 20, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:17:11.506'),
(55, 16, 21, 2, 'goal', 45, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:17:11.515'),
(56, 16, 16, 2, 'goal', 53, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:17:11.524'),
(57, 16, 12, 1, 'goal', 57, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:17:11.545'),
(58, 16, 12, 1, 'goal', 88, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:17:11.564'),
(59, 16, 11, 1, 'yellow_card', 37, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:17:11.571'),
(60, 17, 10, 1, 'goal', 40, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:17:11.690'),
(61, 17, 8, 1, 'goal', 87, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:17:11.696'),
(62, 17, 7, 1, 'yellow_card', 52, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:17:11.702'),
(63, 17, 4, 1, 'yellow_card', 63, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:17:11.707'),
(64, 18, 47, 4, 'goal', 7, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:17:11.803'),
(65, 18, 40, 4, 'goal', 26, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:17:11.809'),
(66, 18, 39, 4, 'goal', 47, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:17:11.815'),
(67, 18, 44, 4, 'yellow_card', 82, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:17:11.822'),
(68, 18, 45, 4, 'yellow_card', 88, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:17:11.828'),
(69, 18, 7, 1, 'yellow_card', 26, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:17:11.835'),
(70, 19, 31, 3, 'goal', 20, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:17:11.917'),
(71, 19, 30, 3, 'goal', 50, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:17:11.923'),
(72, 19, 14, 2, 'goal', 51, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:17:11.929'),
(73, 19, 14, 2, 'goal', 56, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:17:11.935'),
(74, 19, 24, 2, 'goal', 80, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:17:11.941'),
(75, 19, 28, 3, 'yellow_card', 49, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:17:11.947'),
(76, 19, 29, 3, 'yellow_card', 27, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:17:11.953'),
(77, 19, 16, 2, 'yellow_card', 36, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:17:11.959'),
(78, 20, 41, 4, 'goal', 1, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:17:12.064'),
(79, 20, 18, 2, 'goal', 12, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:17:12.070'),
(80, 20, 13, 2, 'goal', 21, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:17:12.077'),
(81, 20, 17, 2, 'goal', 22, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:17:12.083'),
(82, 21, 47, 4, 'goal', 43, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:17:12.157'),
(83, 21, 29, 3, 'goal', 58, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:17:12.163'),
(84, 21, 45, 4, 'goal', 59, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:17:12.169'),
(85, 21, 41, 4, 'goal', 61, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:17:12.174'),
(86, 21, 37, 4, 'goal', 89, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:17:12.180'),
(87, 21, 34, 3, 'yellow_card', 49, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:17:12.185'),
(88, 21, 48, 4, 'yellow_card', 79, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:17:12.191'),
(89, 21, 29, 3, 'yellow_card', 67, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:17:12.197'),
(90, 23, 10, 1, 'goal', 82, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:34:24.466'),
(91, 24, 2, 1, 'goal', 24, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:34:24.538'),
(92, 24, 27, 3, 'goal', 39, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:34:24.546'),
(93, 24, 36, 3, 'goal', 49, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:34:24.553'),
(94, 25, 37, 4, 'goal', 11, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:34:24.653'),
(95, 25, 5, 1, 'goal', 83, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:34:24.661'),
(96, 25, 12, 1, 'yellow_card', 58, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:34:24.670'),
(97, 26, 26, 3, 'goal', 11, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:34:24.751'),
(98, 26, 31, 3, 'goal', 67, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:34:24.759'),
(99, 26, 36, 3, 'yellow_card', 52, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:34:24.766'),
(100, 26, 31, 3, 'yellow_card', 85, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:34:24.772'),
(101, 27, 13, 2, 'goal', 9, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:34:24.841'),
(102, 27, 16, 2, 'goal', 27, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:34:24.847'),
(103, 27, 38, 4, 'goal', 27, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:34:24.853'),
(104, 27, 46, 4, 'goal', 33, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:34:24.860'),
(105, 27, 48, 4, 'goal', 45, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:34:24.866'),
(106, 28, 43, 4, 'goal', 8, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:34:24.972'),
(107, 28, 25, 3, 'goal', 16, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:34:24.979'),
(108, 28, 33, 3, 'goal', 23, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:34:24.987'),
(109, 28, 42, 4, 'goal', 80, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:34:24.996'),
(110, 28, 35, 3, 'yellow_card', 51, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:34:25.001'),
(111, 28, 41, 4, 'yellow_card', 18, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:34:25.009'),
(112, 28, 44, 4, 'yellow_card', 83, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:34:25.015'),
(113, 29, 65, 6, 'goal', 10, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:34:25.156'),
(114, 29, 58, 5, 'goal', 28, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:34:25.163'),
(115, 29, 54, 5, 'goal', 40, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:34:25.168'),
(116, 29, 52, 5, 'goal', 68, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:34:25.174'),
(117, 29, 69, 6, 'goal', 80, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:34:25.181'),
(118, 29, 60, 5, 'yellow_card', 5, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:34:25.189'),
(119, 29, 59, 5, 'yellow_card', 24, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:34:25.197'),
(120, 30, 80, 7, 'goal', 6, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:34:25.301'),
(121, 30, 56, 5, 'goal', 11, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:34:25.308'),
(122, 30, 76, 7, 'goal', 35, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:34:25.316'),
(123, 30, 78, 7, 'goal', 50, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:34:25.323'),
(124, 30, 82, 7, 'goal', 84, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:34:25.328'),
(125, 31, 89, 8, 'goal', 33, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:34:25.408'),
(126, 31, 49, 5, 'goal', 39, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:34:25.415'),
(127, 31, 96, 8, 'yellow_card', 10, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:34:25.420'),
(128, 31, 52, 5, 'yellow_card', 9, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:34:25.425'),
(129, 32, 78, 7, 'goal', 59, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:34:25.488'),
(130, 32, 78, 7, 'goal', 77, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:34:25.494'),
(131, 33, 94, 8, 'goal', 49, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:34:25.544'),
(132, 33, 62, 6, 'goal', 66, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:34:25.550'),
(133, 33, 61, 6, 'goal', 77, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:34:25.556'),
(134, 33, 71, 6, 'yellow_card', 63, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:34:25.562'),
(135, 33, 96, 8, 'yellow_card', 48, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:34:25.568'),
(136, 33, 64, 6, 'yellow_card', 22, NULL, 'second_half', NULL, 'yellow', NULL, '2026-06-20 06:34:25.574'),
(137, 34, 88, 8, 'goal', 8, NULL, 'first_half', NULL, NULL, NULL, '2026-06-20 06:34:25.665'),
(138, 34, 74, 7, 'goal', 68, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:34:25.670'),
(139, 34, 88, 8, 'goal', 85, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:34:25.677'),
(140, 34, 86, 8, 'goal', 90, NULL, 'second_half', NULL, NULL, NULL, '2026-06-20 06:34:25.683');

-- --------------------------------------------------------

--
-- Table structure for table `match_results`
--

CREATE TABLE `match_results` (
  `id` int NOT NULL,
  `match_id` int NOT NULL,
  `winner_team_id` int DEFAULT NULL,
  `home_score` int NOT NULL DEFAULT '0',
  `away_score` int NOT NULL DEFAULT '0',
  `home_half_time_score` int NOT NULL DEFAULT '0',
  `away_half_time_score` int NOT NULL DEFAULT '0',
  `home_extra_time_score` int DEFAULT NULL,
  `away_extra_time_score` int DEFAULT NULL,
  `home_penalty_score` int DEFAULT NULL,
  `away_penalty_score` int DEFAULT NULL,
  `home_final_score` int NOT NULL DEFAULT '0',
  `away_final_score` int NOT NULL DEFAULT '0',
  `result_type` enum('full_time','extra_time','penalty','forfeit','walkover') COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('official','protested','overturned','under_review') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'official',
  `duration` int DEFAULT NULL,
  `notes` text COLLATE utf8mb4_unicode_ci,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `appeal_note` text COLLATE utf8mb4_unicode_ci,
  `appeal_reason` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `match_results`
--

INSERT INTO `match_results` (`id`, `match_id`, `winner_team_id`, `home_score`, `away_score`, `home_half_time_score`, `away_half_time_score`, `home_extra_time_score`, `away_extra_time_score`, `home_penalty_score`, `away_penalty_score`, `home_final_score`, `away_final_score`, `result_type`, `status`, `duration`, `notes`, `is_active`, `created_at`, `updated_at`, `deleted_at`, `appeal_note`, `appeal_reason`) VALUES
(16, 16, 1, 3, 2, 1, 1, NULL, NULL, NULL, NULL, 3, 2, 'full_time', 'official', 90, NULL, 1, '2026-06-20 06:17:11.581', '2026-06-20 06:17:11.581', NULL, NULL, NULL),
(17, 17, 1, 2, 0, 1, 0, NULL, NULL, NULL, NULL, 2, 0, 'full_time', 'official', 90, NULL, 1, '2026-06-20 06:17:11.715', '2026-06-20 06:17:11.715', NULL, NULL, NULL),
(18, 18, 4, 0, 3, 0, 1, NULL, NULL, NULL, NULL, 0, 3, 'full_time', 'official', 90, NULL, 1, '2026-06-20 06:17:11.843', '2026-06-20 06:17:11.843', NULL, NULL, NULL),
(19, 19, 2, 3, 2, 1, 1, NULL, NULL, NULL, NULL, 3, 2, 'full_time', 'official', 90, NULL, 1, '2026-06-20 06:17:11.967', '2026-06-20 06:17:11.967', NULL, NULL, NULL),
(20, 20, 2, 3, 1, 1, 0, NULL, NULL, NULL, NULL, 3, 1, 'full_time', 'official', 90, NULL, 1, '2026-06-20 06:17:12.089', '2026-06-20 06:17:12.089', NULL, NULL, NULL),
(21, 21, 4, 1, 4, 0, 2, NULL, NULL, NULL, NULL, 1, 4, 'full_time', 'official', 90, NULL, 1, '2026-06-20 06:17:12.205', '2026-06-20 06:17:12.205', NULL, NULL, NULL),
(22, 23, 1, 1, 0, 0, 0, NULL, NULL, NULL, NULL, 1, 0, 'full_time', 'official', 90, NULL, 1, '2026-06-20 06:34:24.476', '2026-06-20 06:34:24.476', NULL, NULL, NULL),
(23, 24, 3, 1, 2, 0, 1, NULL, NULL, NULL, NULL, 1, 2, 'full_time', 'official', 90, NULL, 1, '2026-06-20 06:34:24.561', '2026-06-20 06:34:24.561', NULL, NULL, NULL),
(24, 25, NULL, 1, 1, 0, 0, NULL, NULL, NULL, NULL, 1, 1, 'full_time', 'official', 90, NULL, 1, '2026-06-20 06:34:24.678', '2026-06-20 06:34:24.678', NULL, NULL, NULL),
(25, 26, 3, 0, 2, 0, 1, NULL, NULL, NULL, NULL, 0, 2, 'full_time', 'official', 90, NULL, 1, '2026-06-20 06:34:24.779', '2026-06-20 06:34:24.779', NULL, NULL, NULL),
(26, 27, 4, 2, 3, 1, 1, NULL, NULL, NULL, NULL, 2, 3, 'full_time', 'official', 90, NULL, 1, '2026-06-20 06:34:24.874', '2026-06-20 06:34:24.874', NULL, NULL, NULL),
(27, 28, NULL, 2, 2, 1, 1, NULL, NULL, NULL, NULL, 2, 2, 'full_time', 'official', 90, NULL, 1, '2026-06-20 06:34:25.022', '2026-06-20 06:34:25.022', NULL, NULL, NULL),
(28, 29, 5, 3, 2, 1, 1, NULL, NULL, NULL, NULL, 3, 2, 'full_time', 'official', 90, NULL, 1, '2026-06-20 06:34:25.204', '2026-06-20 06:34:25.204', NULL, NULL, NULL),
(29, 30, 7, 1, 4, 0, 2, NULL, NULL, NULL, NULL, 1, 4, 'full_time', 'official', 90, NULL, 1, '2026-06-20 06:34:25.336', '2026-06-20 06:34:25.336', NULL, NULL, NULL),
(30, 31, NULL, 1, 1, 0, 0, NULL, NULL, NULL, NULL, 1, 1, 'full_time', 'official', 90, NULL, 1, '2026-06-20 06:34:25.430', '2026-06-20 06:34:25.430', NULL, NULL, NULL),
(31, 32, 7, 0, 2, 0, 1, NULL, NULL, NULL, NULL, 0, 2, 'full_time', 'official', 90, NULL, 1, '2026-06-20 06:34:25.500', '2026-06-20 06:34:25.500', NULL, NULL, NULL),
(32, 33, 6, 2, 1, 1, 0, NULL, NULL, NULL, NULL, 2, 1, 'full_time', 'official', 90, NULL, 1, '2026-06-20 06:34:25.582', '2026-06-20 06:34:25.582', NULL, NULL, NULL),
(33, 34, 8, 1, 3, 0, 1, NULL, NULL, NULL, NULL, 1, 3, 'full_time', 'official', 90, NULL, 1, '2026-06-20 06:34:25.689', '2026-06-20 06:34:25.689', NULL, NULL, NULL),
(34, 35, 4, 1, 1, 0, 0, NULL, NULL, 5, 4, 1, 1, 'penalty', 'official', 120, NULL, 1, '2026-06-20 06:34:25.782', '2026-06-20 06:34:25.782', NULL, NULL, NULL),
(35, 36, 1, 3, 3, 1, 1, NULL, NULL, 4, 5, 3, 3, 'penalty', 'official', 120, NULL, 1, '2026-06-20 06:34:25.801', '2026-06-20 06:34:25.801', NULL, NULL, NULL),
(36, 37, 1, 1, 2, 0, 1, NULL, NULL, NULL, NULL, 1, 2, 'full_time', 'official', 90, NULL, 1, '2026-06-20 06:34:25.826', '2026-06-20 06:34:25.826', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

CREATE TABLE `notifications` (
  `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `title` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` enum('match_schedule','match_result','standing_updated','registration_deadline','team_advanced','payment_confirmed','payment_rejected','player_approved','player_rejected','team_approved','team_rejected','general') COLLATE utf8mb4_unicode_ci NOT NULL,
  `source` enum('manual','system') COLLATE utf8mb4_unicode_ci NOT NULL,
  `season_id` int DEFAULT NULL,
  `target_team_id` int DEFAULT NULL,
  `recipient_user_id` int DEFAULT NULL,
  `is_read` tinyint(1) NOT NULL DEFAULT '0',
  `ref_entity_type` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ref_entity_id` int DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments` (
  `id` int NOT NULL,
  `season_team_id` int NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `status` enum('pending','confirmed','rejected') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  `transaction_ref` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `paid_at` datetime(3) DEFAULT NULL,
  `confirmed_at` datetime(3) DEFAULT NULL,
  `confirmed_by` int DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `payments`
--

INSERT INTO `payments` (`id`, `season_team_id`, `amount`, `status`, `transaction_ref`, `paid_at`, `confirmed_at`, `confirmed_by`, `is_active`, `created_at`, `updated_at`, `deleted_at`) VALUES
(1, 1, 5000000.00, 'confirmed', 'TXN-1781843373557-1', '2024-02-10 00:00:00.000', '2024-02-11 00:00:00.000', 1, 1, '2026-06-19 04:29:33.560', '2026-06-19 04:29:33.560', NULL),
(2, 2, 5000000.00, 'confirmed', 'TXN-1781843374212-2', '2024-02-10 00:00:00.000', '2024-02-11 00:00:00.000', 1, 1, '2026-06-19 04:29:34.215', '2026-06-19 04:29:34.215', NULL),
(3, 3, 5000000.00, 'confirmed', 'TXN-1781843374792-3', '2024-02-10 00:00:00.000', '2024-02-11 00:00:00.000', 1, 1, '2026-06-19 04:29:34.794', '2026-06-19 04:29:34.794', NULL),
(4, 4, 5000000.00, 'confirmed', 'TXN-1781843375276-4', '2024-02-10 00:00:00.000', '2024-02-11 00:00:00.000', 1, 1, '2026-06-19 04:29:35.277', '2026-06-19 04:29:35.277', NULL),
(5, 5, 5000000.00, 'confirmed', 'TXN-1781843375710-5', '2024-02-10 00:00:00.000', '2024-02-11 00:00:00.000', 1, 1, '2026-06-19 04:29:35.710', '2026-06-19 04:29:35.710', NULL),
(6, 6, 5000000.00, 'confirmed', 'TXN-1781843376091-6', '2024-02-10 00:00:00.000', '2024-02-11 00:00:00.000', 1, 1, '2026-06-19 04:29:36.091', '2026-06-19 04:29:36.091', NULL),
(7, 7, 5000000.00, 'confirmed', 'TXN-1781843376463-7', '2024-02-10 00:00:00.000', '2024-02-11 00:00:00.000', 1, 1, '2026-06-19 04:29:36.464', '2026-06-19 04:29:36.464', NULL),
(8, 8, 5000000.00, 'confirmed', 'TXN-1781843376824-8', '2024-02-10 00:00:00.000', '2024-02-11 00:00:00.000', 1, 1, '2026-06-19 04:29:36.825', '2026-06-19 04:29:36.825', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `phases`
--

CREATE TABLE `phases` (
  `id` int NOT NULL,
  `season_id` int NOT NULL,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` enum('group_stage','round_of_16','quarter_final','semi_final','third_place','final') COLLATE utf8mb4_unicode_ci NOT NULL,
  `format` enum('round_robin','knockout') COLLATE utf8mb4_unicode_ci NOT NULL,
  `order` int NOT NULL,
  `start_date` datetime(3) DEFAULT NULL,
  `end_date` datetime(3) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `status` enum('draft','in_progress','locked') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'draft',
  `min_rest_days_per_team` int NOT NULL DEFAULT '3',
  `legs` int NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `phases`
--

INSERT INTO `phases` (`id`, `season_id`, `name`, `type`, `format`, `order`, `start_date`, `end_date`, `is_active`, `created_at`, `updated_at`, `status`, `min_rest_days_per_team`, `legs`) VALUES
(1, 1, 'Vòng Bảng', 'group_stage', 'round_robin', 1, '2024-03-01 00:00:00.000', '2024-04-15 00:00:00.000', 1, '2026-06-19 04:29:36.924', '2026-06-19 04:29:36.924', 'draft', 3, 1),
(2, 1, 'Tứ Kết', 'quarter_final', 'knockout', 2, '2024-04-20 00:00:00.000', '2024-04-30 00:00:00.000', 1, '2026-06-19 04:29:36.934', '2026-06-19 04:29:36.934', 'draft', 3, 1),
(3, 1, 'Bán Kết', 'semi_final', 'knockout', 3, '2024-05-05 00:00:00.000', '2024-05-15 00:00:00.000', 1, '2026-06-19 04:29:36.943', '2026-06-19 04:29:36.943', 'draft', 3, 1),
(4, 1, 'Tranh Hạng Ba', 'third_place', 'knockout', 4, '2024-06-25 00:00:00.000', '2024-06-25 00:00:00.000', 1, '2026-06-19 04:29:36.952', '2026-06-19 04:29:36.952', 'draft', 3, 1),
(5, 1, 'Chung Kết', 'final', 'knockout', 5, '2024-06-30 00:00:00.000', '2024-06-30 00:00:00.000', 1, '2026-06-19 04:29:36.960', '2026-06-19 04:29:36.960', 'draft', 3, 1),
(6, 2, 'Vòng bảng', 'group_stage', 'round_robin', 1, '2025-03-01 00:00:00.000', '2025-05-01 00:00:00.000', 1, '2026-06-20 15:30:23.319', '2026-06-20 15:30:23.319', 'draft', 3, 1),
(7, 3, 'Vòng bảng', 'group_stage', 'round_robin', 1, '2025-06-01 00:00:00.000', '2025-08-01 00:00:00.000', 1, '2026-06-20 15:30:23.453', '2026-06-20 15:30:23.453', 'draft', 1, 1),
(8, 4, 'Vòng bảng', 'group_stage', 'round_robin', 1, '2025-09-01 00:00:00.000', '2025-11-01 00:00:00.000', 1, '2026-06-20 15:30:23.551', '2026-06-20 15:30:23.551', 'draft', 3, 1),
(9, 5, 'Vòng bảng', 'group_stage', 'round_robin', 1, '2025-12-01 00:00:00.000', '2026-02-01 00:00:00.000', 1, '2026-06-20 15:30:23.676', '2026-06-20 15:30:23.676', 'draft', 3, 1),
(10, 5, 'Bán kết', 'semi_final', 'knockout', 2, '2025-12-01 00:00:00.000', '2026-02-01 00:00:00.000', 1, '2026-06-20 15:30:23.718', '2026-06-20 15:30:23.718', 'draft', 3, 1),
(11, 5, 'Chung kết', 'final', 'knockout', 3, '2025-12-01 00:00:00.000', '2026-02-01 00:00:00.000', 1, '2026-06-20 15:30:23.729', '2026-06-20 15:30:23.729', 'draft', 3, 1),
(18, 11, 'Vòng bảng', 'group_stage', 'round_robin', 1, NULL, NULL, 1, '2026-06-20 15:33:28.032', '2026-06-20 15:33:28.032', 'in_progress', 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `players`
--

		CREATE TABLE `players` (
		  `id` int NOT NULL,
		  `date_of_birth` datetime(3) NOT NULL,
		  `position` enum('goalkeeper','defender','midfielder','forward') COLLATE utf8mb4_unicode_ci NOT NULL,
		  `height` decimal(5,2) DEFAULT NULL,
		  `weight` decimal(5,2) DEFAULT NULL,
		  `nationality` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
		  `avatar` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
		  `is_active` tinyint(1) NOT NULL DEFAULT '1',
		  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
		  `updated_at` datetime(3) DEFAULT NULL,
		  `deleted_at` datetime(3) DEFAULT NULL,
		  `user_id` int NOT NULL
		) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `players`
--

INSERT INTO `players` (`id`, `date_of_birth`, `position`, `height`, `weight`, `nationality`, `avatar`, `is_active`, `created_at`, `updated_at`, `deleted_at`, `user_id`) VALUES
(1, '1996-01-27 17:00:00.000', 'goalkeeper', 165.84, 61.86, 'Việt Nam', NULL, 1, '2026-06-19 04:29:33.026', '2026-06-19 04:29:33.026', NULL, 7),
(2, '1997-11-04 17:00:00.000', 'defender', 167.28, 72.83, 'Việt Nam', NULL, 1, '2026-06-19 04:29:33.100', '2026-06-19 04:29:33.100', NULL, 8),
(3, '1993-07-24 17:00:00.000', 'defender', 166.90, 62.50, 'Việt Nam', NULL, 1, '2026-06-19 04:29:33.142', '2026-06-19 04:29:33.142', NULL, 9),
(4, '1995-05-25 17:00:00.000', 'defender', 167.95, 72.98, 'Việt Nam', NULL, 1, '2026-06-19 04:29:33.182', '2026-06-19 04:29:33.182', NULL, 10),
(5, '1999-09-12 17:00:00.000', 'defender', 167.82, 78.40, 'Việt Nam', NULL, 1, '2026-06-19 04:29:33.216', '2026-06-19 04:29:33.216', NULL, 11),
(6, '1991-04-30 17:00:00.000', 'midfielder', 177.90, 72.58, 'Việt Nam', NULL, 1, '2026-06-19 04:29:33.247', '2026-06-19 04:29:33.247', NULL, 12),
(7, '1991-05-17 17:00:00.000', 'midfielder', 171.06, 74.32, 'Việt Nam', NULL, 1, '2026-06-19 04:29:33.284', '2026-06-19 04:29:33.284', NULL, 13),
(8, '1992-01-22 17:00:00.000', 'midfielder', 178.93, 79.95, 'Việt Nam', NULL, 1, '2026-06-19 04:29:33.320', '2026-06-19 04:29:33.320', NULL, 14),
(9, '1998-01-03 17:00:00.000', 'midfielder', 165.94, 67.10, 'Việt Nam', NULL, 1, '2026-06-19 04:29:33.356', '2026-06-19 04:29:33.356', NULL, 15),
(10, '1993-09-04 17:00:00.000', 'forward', 168.32, 64.60, 'Việt Nam', NULL, 1, '2026-06-19 04:29:33.388', '2026-06-19 04:29:33.388', NULL, 16),
(11, '1999-12-02 17:00:00.000', 'forward', 179.78, 64.38, 'Việt Nam', NULL, 1, '2026-06-19 04:29:33.424', '2026-06-19 04:29:33.424', NULL, 17),
(12, '1999-03-06 17:00:00.000', 'forward', 167.92, 75.19, 'Việt Nam', NULL, 1, '2026-06-19 04:29:33.461', '2026-06-19 04:29:33.461', NULL, 18),
(13, '1995-02-01 17:00:00.000', 'goalkeeper', 178.38, 79.30, 'Việt Nam', NULL, 1, '2026-06-19 04:29:33.797', '2026-06-19 04:29:33.797', NULL, 20),
(14, '1999-09-02 17:00:00.000', 'defender', 175.72, 75.28, 'Việt Nam', NULL, 1, '2026-06-19 04:29:33.836', '2026-06-19 04:29:33.836', NULL, 21),
(15, '1998-06-09 17:00:00.000', 'defender', 177.35, 61.05, 'Việt Nam', NULL, 1, '2026-06-19 04:29:33.872', '2026-06-19 04:29:33.872', NULL, 22),
(16, '1997-03-24 17:00:00.000', 'defender', 178.53, 60.62, 'Việt Nam', NULL, 1, '2026-06-19 04:29:33.907', '2026-06-19 04:29:33.907', NULL, 23),
(17, '1998-03-09 17:00:00.000', 'defender', 184.26, 62.80, 'Việt Nam', NULL, 1, '2026-06-19 04:29:33.938', '2026-06-19 04:29:33.938', NULL, 24),
(18, '1998-12-04 17:00:00.000', 'midfielder', 167.91, 70.06, 'Việt Nam', NULL, 1, '2026-06-19 04:29:33.968', '2026-06-19 04:29:33.968', NULL, 25),
(19, '1996-03-09 17:00:00.000', 'midfielder', 184.64, 60.57, 'Việt Nam', NULL, 1, '2026-06-19 04:29:34.001', '2026-06-19 04:29:34.001', NULL, 26),
(20, '1999-10-08 17:00:00.000', 'midfielder', 183.29, 61.76, 'Việt Nam', NULL, 1, '2026-06-19 04:29:34.036', '2026-06-19 04:29:34.036', NULL, 27),
(21, '1992-05-15 17:00:00.000', 'midfielder', 182.64, 73.49, 'Việt Nam', NULL, 1, '2026-06-19 04:29:34.066', '2026-06-19 04:29:34.066', NULL, 28),
(22, '1998-10-26 17:00:00.000', 'forward', 182.03, 63.95, 'Việt Nam', NULL, 1, '2026-06-19 04:29:34.101', '2026-06-19 04:29:34.101', NULL, 29),
(23, '1999-12-25 17:00:00.000', 'forward', 174.46, 62.28, 'Việt Nam', NULL, 1, '2026-06-19 04:29:34.136', '2026-06-19 04:29:34.136', NULL, 30),
(24, '1993-11-01 17:00:00.000', 'forward', 173.35, 62.51, 'Việt Nam', NULL, 1, '2026-06-19 04:29:34.170', '2026-06-19 04:29:34.170', NULL, 31),
(25, '1990-01-24 17:00:00.000', 'goalkeeper', 182.18, 72.49, 'Việt Nam', NULL, 1, '2026-06-19 04:29:34.407', '2026-06-19 04:29:34.407', NULL, 33),
(26, '1993-05-25 17:00:00.000', 'defender', 176.34, 63.04, 'Việt Nam', NULL, 1, '2026-06-19 04:29:34.440', '2026-06-19 04:29:34.440', NULL, 34),
(27, '1991-10-24 17:00:00.000', 'defender', 174.25, 74.70, 'Việt Nam', NULL, 1, '2026-06-19 04:29:34.477', '2026-06-19 04:29:34.477', NULL, 35),
(28, '1998-07-03 17:00:00.000', 'defender', 169.72, 79.21, 'Việt Nam', NULL, 1, '2026-06-19 04:29:34.512', '2026-06-19 04:29:34.512', NULL, 36),
(29, '1991-02-21 17:00:00.000', 'defender', 169.96, 79.22, 'Việt Nam', NULL, 1, '2026-06-19 04:29:34.544', '2026-06-19 04:29:34.544', NULL, 37),
(30, '1992-11-04 17:00:00.000', 'midfielder', 178.00, 66.71, 'Việt Nam', NULL, 1, '2026-06-19 04:29:34.575', '2026-06-19 04:29:34.575', NULL, 38),
(31, '1992-09-24 17:00:00.000', 'midfielder', 177.43, 79.96, 'Việt Nam', NULL, 1, '2026-06-19 04:29:34.609', '2026-06-19 04:29:34.609', NULL, 39),
(32, '1994-06-10 17:00:00.000', 'midfielder', 172.02, 64.33, 'Việt Nam', NULL, 1, '2026-06-19 04:29:34.642', '2026-06-19 04:29:34.642', NULL, 40),
(33, '1996-09-24 17:00:00.000', 'midfielder', 184.49, 66.91, 'Việt Nam', NULL, 1, '2026-06-19 04:29:34.671', '2026-06-19 04:29:34.671', NULL, 41),
(34, '1996-09-15 17:00:00.000', 'forward', 170.58, 64.37, 'Việt Nam', NULL, 1, '2026-06-19 04:29:34.705', '2026-06-19 04:29:34.705', NULL, 42),
(35, '1991-09-24 17:00:00.000', 'forward', 184.99, 63.73, 'Việt Nam', NULL, 1, '2026-06-19 04:29:34.731', '2026-06-19 04:29:34.731', NULL, 43),
(36, '1990-01-23 17:00:00.000', 'forward', 167.42, 67.75, 'Việt Nam', NULL, 1, '2026-06-19 04:29:34.760', '2026-06-19 04:29:34.760', NULL, 44),
(37, '1994-11-20 17:00:00.000', 'goalkeeper', 178.48, 76.88, 'Việt Nam', NULL, 1, '2026-06-19 04:29:34.961', '2026-06-19 04:29:34.961', NULL, 46),
(38, '1994-11-15 17:00:00.000', 'defender', 171.89, 68.48, 'Việt Nam', NULL, 1, '2026-06-19 04:29:34.992', '2026-06-19 04:29:34.992', NULL, 47),
(39, '1990-08-14 17:00:00.000', 'defender', 179.20, 60.74, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.014', '2026-06-19 04:29:35.014', NULL, 48),
(40, '1995-10-11 17:00:00.000', 'defender', 166.18, 78.71, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.035', '2026-06-19 04:29:35.035', NULL, 49),
(41, '1992-04-23 17:00:00.000', 'defender', 176.35, 79.95, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.057', '2026-06-19 04:29:35.057', NULL, 50),
(42, '1990-11-09 17:00:00.000', 'midfielder', 170.01, 75.47, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.079', '2026-06-19 04:29:35.079', NULL, 51),
(43, '1999-05-04 17:00:00.000', 'midfielder', 172.57, 70.83, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.102', '2026-06-19 04:29:35.102', NULL, 52),
(44, '1993-08-13 17:00:00.000', 'midfielder', 167.80, 61.21, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.129', '2026-06-19 04:29:35.129', NULL, 53),
(45, '1990-01-06 17:00:00.000', 'midfielder', 177.45, 71.97, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.159', '2026-06-19 04:29:35.159', NULL, 54),
(46, '1991-06-01 17:00:00.000', 'forward', 169.66, 72.14, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.189', '2026-06-19 04:29:35.189', NULL, 55),
(47, '1992-09-09 17:00:00.000', 'forward', 178.07, 75.66, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.220', '2026-06-19 04:29:35.220', NULL, 56),
(48, '1996-08-27 17:00:00.000', 'forward', 166.88, 73.72, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.251', '2026-06-19 04:29:35.251', NULL, 57),
(49, '1997-06-25 17:00:00.000', 'goalkeeper', 165.51, 69.83, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.420', '2026-06-19 04:29:35.420', NULL, 59),
(50, '1996-11-30 17:00:00.000', 'defender', 171.87, 75.07, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.449', '2026-06-19 04:29:35.449', NULL, 60),
(51, '1991-02-23 17:00:00.000', 'defender', 179.61, 70.56, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.477', '2026-06-19 04:29:35.477', NULL, 61),
(52, '1997-08-04 17:00:00.000', 'defender', 179.86, 73.12, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.506', '2026-06-19 04:29:35.506', NULL, 62),
(53, '1999-03-27 17:00:00.000', 'defender', 167.98, 61.10, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.534', '2026-06-19 04:29:35.534', NULL, 63),
(54, '1995-05-01 17:00:00.000', 'midfielder', 181.82, 69.80, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.562', '2026-06-19 04:29:35.562', NULL, 64),
(55, '1999-12-06 17:00:00.000', 'midfielder', 174.31, 60.28, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.590', '2026-06-19 04:29:35.590', NULL, 65),
(56, '1995-05-19 17:00:00.000', 'midfielder', 183.92, 73.79, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.613', '2026-06-19 04:29:35.613', NULL, 66),
(57, '1999-09-12 17:00:00.000', 'midfielder', 175.83, 73.56, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.635', '2026-06-19 04:29:35.635', NULL, 67),
(58, '1994-02-28 17:00:00.000', 'forward', 181.50, 71.95, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.658', '2026-06-19 04:29:35.658', NULL, 68),
(59, '1996-11-19 17:00:00.000', 'forward', 165.14, 63.58, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.679', '2026-06-19 04:29:35.679', NULL, 69),
(60, '1996-01-05 17:00:00.000', 'forward', 167.04, 72.41, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.695', '2026-06-19 04:29:35.695', NULL, 70),
(61, '1998-03-08 17:00:00.000', 'goalkeeper', 184.09, 65.68, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.816', '2026-06-19 04:29:35.816', NULL, 72),
(62, '1997-12-20 17:00:00.000', 'defender', 165.22, 65.15, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.835', '2026-06-19 04:29:35.835', NULL, 73),
(63, '1991-03-02 17:00:00.000', 'defender', 171.01, 77.67, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.858', '2026-06-19 04:29:35.858', NULL, 74),
(64, '1994-10-08 17:00:00.000', 'defender', 182.32, 70.63, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.884', '2026-06-19 04:29:35.884', NULL, 75),
(65, '1992-09-11 17:00:00.000', 'defender', 181.69, 78.33, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.910', '2026-06-19 04:29:35.910', NULL, 76),
(66, '1997-05-15 17:00:00.000', 'midfielder', 178.23, 62.74, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.946', '2026-06-19 04:29:35.946', NULL, 77),
(67, '1998-04-21 17:00:00.000', 'midfielder', 166.49, 72.39, 'Việt Nam', NULL, 1, '2026-06-19 04:29:35.973', '2026-06-19 04:29:35.973', NULL, 78),
(68, '1995-12-05 17:00:00.000', 'midfielder', 168.05, 79.43, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.000', '2026-06-19 04:29:36.000', NULL, 79),
(69, '1991-12-05 17:00:00.000', 'midfielder', 183.19, 67.00, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.023', '2026-06-19 04:29:36.023', NULL, 80),
(70, '1998-11-23 17:00:00.000', 'forward', 171.17, 72.27, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.040', '2026-06-19 04:29:36.040', NULL, 81),
(71, '1992-05-03 17:00:00.000', 'forward', 167.84, 79.80, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.056', '2026-06-19 04:29:36.056', NULL, 82),
(72, '1999-07-12 17:00:00.000', 'forward', 182.86, 66.34, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.076', '2026-06-19 04:29:36.076', NULL, 83),
(73, '1997-01-02 17:00:00.000', 'goalkeeper', 183.75, 76.94, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.190', '2026-06-19 04:29:36.190', NULL, 85),
(74, '1993-01-14 17:00:00.000', 'defender', 179.78, 67.22, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.212', '2026-06-19 04:29:36.212', NULL, 86),
(75, '1994-04-07 17:00:00.000', 'defender', 180.15, 77.97, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.235', '2026-06-19 04:29:36.235', NULL, 87),
(76, '1998-12-21 17:00:00.000', 'defender', 180.14, 72.24, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.255', '2026-06-19 04:29:36.255', NULL, 88),
(77, '1994-06-08 17:00:00.000', 'defender', 176.98, 68.10, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.281', '2026-06-19 04:29:36.281', NULL, 89),
(78, '1998-08-18 17:00:00.000', 'midfielder', 178.35, 61.05, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.308', '2026-06-19 04:29:36.308', NULL, 90),
(79, '1998-01-20 17:00:00.000', 'midfielder', 180.88, 75.70, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.333', '2026-06-19 04:29:36.333', NULL, 91),
(80, '1997-10-11 17:00:00.000', 'midfielder', 184.18, 76.74, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.360', '2026-06-19 04:29:36.360', NULL, 92),
(81, '1994-06-03 17:00:00.000', 'midfielder', 179.58, 77.76, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.385', '2026-06-19 04:29:36.385', NULL, 93),
(82, '1995-04-06 17:00:00.000', 'forward', 167.61, 76.56, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.409', '2026-06-19 04:29:36.409', NULL, 94),
(83, '1993-02-03 17:00:00.000', 'forward', 176.62, 68.71, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.431', '2026-06-19 04:29:36.431', NULL, 95),
(84, '1992-02-29 17:00:00.000', 'forward', 168.19, 66.61, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.448', '2026-06-19 04:29:36.448', NULL, 96),
(85, '1994-11-24 17:00:00.000', 'goalkeeper', 170.06, 69.90, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.561', '2026-06-19 04:29:36.561', NULL, 98),
(86, '1995-07-16 17:00:00.000', 'defender', 172.89, 79.83, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.581', '2026-06-19 04:29:36.581', NULL, 99),
(87, '1991-06-10 17:00:00.000', 'defender', 182.08, 79.38, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.598', '2026-06-19 04:29:36.598', NULL, 100),
(88, '1992-11-27 17:00:00.000', 'defender', 177.22, 70.20, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.615', '2026-06-19 04:29:36.615', NULL, 101),
(89, '1992-04-19 17:00:00.000', 'defender', 182.86, 67.09, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.634', '2026-06-19 04:29:36.634', NULL, 102),
(90, '1996-09-19 17:00:00.000', 'midfielder', 168.02, 60.02, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.652', '2026-06-19 04:29:36.652', NULL, 103),
(91, '1995-03-09 17:00:00.000', 'midfielder', 174.83, 69.92, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.674', '2026-06-19 04:29:36.674', NULL, 104),
(92, '1998-04-17 17:00:00.000', 'midfielder', 172.25, 78.61, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.697', '2026-06-19 04:29:36.697', NULL, 105),
(93, '1997-10-06 17:00:00.000', 'midfielder', 177.50, 72.26, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.724', '2026-06-19 04:29:36.724', NULL, 106),
(94, '1997-05-23 17:00:00.000', 'forward', 175.70, 60.64, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.753', '2026-06-19 04:29:36.753', NULL, 107),
(95, '1990-03-18 17:00:00.000', 'forward', 182.39, 76.50, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.778', '2026-06-19 04:29:36.778', NULL, 108),
(96, '1997-06-24 17:00:00.000', 'forward', 183.35, 76.26, 'Việt Nam', NULL, 1, '2026-06-19 04:29:36.804', '2026-06-19 04:29:36.804', NULL, 109);

-- --------------------------------------------------------

--
-- Table structure for table `player_statistics`
--

CREATE TABLE `player_statistics` (
  `id` int NOT NULL,
  `player_id` int NOT NULL,
  `team_id` int NOT NULL,
  `season_id` int NOT NULL,
  `matches_played` int NOT NULL DEFAULT '0',
  `goals_scored` int NOT NULL DEFAULT '0',
  `assists` int NOT NULL DEFAULT '0',
  `yellow_cards` int NOT NULL DEFAULT '0',
  `red_cards` int NOT NULL DEFAULT '0',
  `minutes_played` int NOT NULL DEFAULT '0',
  `accumulated_yellow_cards` int NOT NULL DEFAULT '0',
  `is_suspended` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `player_statistics`
--

INSERT INTO `player_statistics` (`id`, `player_id`, `team_id`, `season_id`, `matches_played`, `goals_scored`, `assists`, `yellow_cards`, `red_cards`, `minutes_played`, `accumulated_yellow_cards`, `is_suspended`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 1, 2, 2, 0, 0, 0, 180, 0, 0, '2026-06-19 04:29:37.378', '2026-06-19 04:29:37.519'),
(2, 5, 1, 1, 2, 1, 0, 1, 0, 180, 1, 0, '2026-06-19 04:29:37.392', '2026-06-20 06:34:24.712'),
(3, 6, 1, 1, 1, 1, 0, 0, 0, 90, 0, 0, '2026-06-19 04:29:37.404', '2026-06-19 04:29:37.404'),
(4, 7, 1, 1, 3, 0, 0, 3, 0, 270, 3, 1, '2026-06-19 04:29:37.412', '2026-06-20 06:17:11.868'),
(5, 22, 2, 1, 1, 0, 0, 1, 0, 90, 1, 0, '2026-06-19 04:29:37.425', '2026-06-19 04:29:37.425'),
(6, 4, 1, 1, 2, 1, 0, 1, 0, 180, 1, 0, '2026-06-19 04:29:37.526', '2026-06-20 06:17:11.760'),
(7, 8, 1, 1, 2, 2, 0, 0, 0, 180, 0, 0, '2026-06-19 04:29:37.533', '2026-06-20 06:17:11.778'),
(8, 9, 1, 1, 1, 1, 0, 1, 0, 90, 1, 0, '2026-06-19 04:29:37.540', '2026-06-19 04:29:37.540'),
(9, 28, 3, 1, 2, 1, 0, 1, 0, 180, 1, 0, '2026-06-19 04:29:37.547', '2026-06-20 06:17:12.023'),
(10, 33, 3, 1, 2, 1, 0, 1, 0, 180, 1, 0, '2026-06-19 04:29:37.553', '2026-06-20 06:34:25.058'),
(11, 3, 1, 1, 1, 1, 0, 0, 0, 90, 0, 0, '2026-06-19 04:29:37.631', '2026-06-19 04:29:37.631'),
(12, 10, 1, 1, 3, 2, 0, 1, 0, 270, 1, 0, '2026-06-19 04:29:37.640', '2026-06-20 06:34:24.522'),
(13, 11, 1, 1, 2, 1, 0, 1, 0, 180, 1, 0, '2026-06-19 04:29:37.648', '2026-06-20 06:17:11.641'),
(14, 37, 4, 1, 4, 5, 0, 0, 0, 360, 0, 0, '2026-06-19 04:29:37.658', '2026-06-20 06:34:24.733'),
(15, 46, 4, 1, 3, 3, 0, 1, 0, 270, 1, 0, '2026-06-19 04:29:37.669', '2026-06-20 06:34:24.946'),
(16, 19, 2, 1, 1, 1, 0, 0, 0, 90, 0, 0, '2026-06-19 04:29:37.739', '2026-06-19 04:29:37.739'),
(17, 17, 2, 1, 2, 1, 0, 1, 0, 180, 1, 0, '2026-06-19 04:29:37.825', '2026-06-20 06:17:12.127'),
(18, 21, 2, 1, 2, 1, 0, 1, 0, 180, 1, 0, '2026-06-19 04:29:37.833', '2026-06-20 06:17:11.676'),
(19, 23, 2, 1, 1, 1, 0, 0, 0, 90, 0, 0, '2026-06-19 04:29:37.838', '2026-06-19 04:29:37.838'),
(20, 24, 2, 1, 2, 2, 0, 0, 0, 180, 0, 0, '2026-06-19 04:29:37.844', '2026-06-20 06:17:12.014'),
(21, 40, 4, 1, 2, 2, 0, 0, 0, 180, 0, 0, '2026-06-19 04:29:37.852', '2026-06-20 06:17:11.882'),
(22, 41, 4, 1, 4, 2, 0, 2, 0, 360, 2, 0, '2026-06-19 04:29:37.860', '2026-06-20 06:34:25.076'),
(23, 30, 3, 1, 2, 2, 0, 0, 0, 180, 0, 0, '2026-06-19 04:29:37.962', '2026-06-20 06:17:12.040'),
(24, 39, 4, 1, 2, 2, 0, 0, 0, 180, 0, 0, '2026-06-19 04:29:37.983', '2026-06-20 06:17:11.875'),
(25, 47, 4, 1, 3, 3, 0, 0, 0, 270, 0, 0, '2026-06-19 04:29:37.992', '2026-06-20 06:17:12.281'),
(26, 56, 5, 1, 2, 1, 0, 1, 0, 180, 1, 0, '2026-06-19 04:29:38.111', '2026-06-20 06:34:25.364'),
(27, 58, 5, 1, 3, 2, 0, 1, 0, 270, 1, 0, '2026-06-19 04:29:38.121', '2026-06-20 06:34:25.250'),
(28, 60, 5, 1, 2, 0, 0, 2, 0, 180, 2, 0, '2026-06-19 04:29:38.131', '2026-06-20 06:34:25.268'),
(29, 61, 6, 1, 2, 2, 0, 0, 0, 180, 0, 0, '2026-06-19 04:29:38.140', '2026-06-20 06:34:25.610'),
(30, 70, 6, 1, 2, 2, 0, 0, 0, 180, 0, 0, '2026-06-19 04:29:38.150', '2026-06-19 04:29:38.462'),
(31, 59, 5, 1, 2, 0, 0, 2, 0, 180, 2, 0, '2026-06-19 04:29:38.255', '2026-06-20 06:34:25.259'),
(32, 74, 7, 1, 2, 2, 0, 0, 0, 180, 0, 0, '2026-06-19 04:29:38.263', '2026-06-20 06:34:25.717'),
(33, 75, 7, 1, 1, 1, 0, 0, 0, 90, 0, 0, '2026-06-19 04:29:38.270', '2026-06-19 04:29:38.270'),
(34, 80, 7, 1, 2, 2, 0, 0, 0, 180, 0, 0, '2026-06-19 04:29:38.275', '2026-06-20 06:34:25.387'),
(35, 84, 7, 1, 1, 0, 0, 1, 0, 90, 1, 0, '2026-06-19 04:29:38.282', '2026-06-19 04:29:38.282'),
(36, 89, 8, 1, 3, 3, 0, 1, 0, 270, 1, 0, '2026-06-19 04:29:38.326', '2026-06-20 06:34:25.468'),
(37, 96, 8, 1, 3, 1, 0, 2, 0, 270, 2, 0, '2026-06-19 04:29:38.333', '2026-06-20 06:34:25.650'),
(38, 66, 6, 1, 1, 1, 0, 0, 0, 90, 0, 0, '2026-06-19 04:29:38.387', '2026-06-19 04:29:38.387'),
(39, 86, 8, 1, 2, 2, 0, 0, 0, 180, 0, 0, '2026-06-19 04:29:38.471', '2026-06-20 06:34:25.726'),
(40, 95, 8, 1, 1, 1, 0, 0, 0, 90, 0, 0, '2026-06-19 04:29:38.491', '2026-06-19 04:29:38.491'),
(41, 77, 7, 1, 1, 0, 0, 1, 0, 90, 1, 0, '2026-06-19 04:29:38.555', '2026-06-19 04:29:38.555'),
(42, 82, 7, 1, 2, 2, 0, 0, 0, 180, 0, 0, '2026-06-19 04:29:38.561', '2026-06-20 06:34:25.395'),
(43, 83, 7, 1, 1, 1, 0, 0, 0, 90, 0, 0, '2026-06-19 04:29:38.568', '2026-06-19 04:29:38.568'),
(44, 2, 1, 1, 2, 2, 0, 0, 0, 180, 0, 0, '2026-06-20 06:17:11.627', '2026-06-20 06:34:24.599'),
(45, 12, 1, 1, 2, 2, 0, 1, 0, 180, 1, 0, '2026-06-20 06:17:11.650', '2026-06-20 06:34:24.724'),
(46, 16, 2, 1, 3, 2, 0, 1, 0, 270, 1, 0, '2026-06-20 06:17:11.665', '2026-06-20 06:34:24.916'),
(47, 44, 4, 1, 2, 0, 0, 2, 0, 180, 2, 0, '2026-06-20 06:17:11.889', '2026-06-20 06:34:25.099'),
(48, 45, 4, 1, 2, 1, 0, 1, 0, 180, 1, 0, '2026-06-20 06:17:11.896', '2026-06-20 06:17:12.273'),
(49, 14, 2, 1, 1, 2, 0, 0, 0, 90, 0, 0, '2026-06-20 06:17:11.997', '2026-06-20 06:17:11.997'),
(50, 29, 3, 1, 2, 1, 0, 2, 0, 180, 2, 0, '2026-06-20 06:17:12.031', '2026-06-20 06:17:12.237'),
(51, 31, 3, 1, 2, 2, 0, 1, 0, 180, 1, 0, '2026-06-20 06:17:12.049', '2026-06-20 06:34:24.816'),
(52, 13, 2, 1, 2, 2, 0, 0, 0, 180, 0, 0, '2026-06-20 06:17:12.118', '2026-06-20 06:34:24.907'),
(53, 18, 2, 1, 1, 1, 0, 0, 0, 90, 0, 0, '2026-06-20 06:17:12.135', '2026-06-20 06:17:12.135'),
(54, 34, 3, 1, 1, 0, 0, 1, 0, 90, 1, 0, '2026-06-20 06:17:12.245', '2026-06-20 06:17:12.245'),
(55, 48, 4, 1, 2, 1, 0, 1, 0, 180, 1, 0, '2026-06-20 06:17:12.289', '2026-06-20 06:34:24.955'),
(56, 27, 3, 1, 1, 1, 0, 0, 0, 90, 0, 0, '2026-06-20 06:34:24.626', '2026-06-20 06:34:24.626'),
(57, 36, 3, 1, 2, 1, 0, 1, 0, 180, 1, 0, '2026-06-20 06:34:24.636', '2026-06-20 06:34:24.824'),
(58, 26, 3, 1, 1, 1, 0, 0, 0, 90, 0, 0, '2026-06-20 06:34:24.807', '2026-06-20 06:34:24.807'),
(59, 38, 4, 1, 1, 1, 0, 0, 0, 90, 0, 0, '2026-06-20 06:34:24.926', '2026-06-20 06:34:24.926'),
(60, 25, 3, 1, 1, 1, 0, 0, 0, 90, 0, 0, '2026-06-20 06:34:25.050', '2026-06-20 06:34:25.050'),
(61, 35, 3, 1, 1, 0, 0, 1, 0, 90, 1, 0, '2026-06-20 06:34:25.068', '2026-06-20 06:34:25.068'),
(62, 42, 4, 1, 1, 1, 0, 0, 0, 90, 0, 0, '2026-06-20 06:34:25.084', '2026-06-20 06:34:25.084'),
(63, 43, 4, 1, 1, 1, 0, 0, 0, 90, 0, 0, '2026-06-20 06:34:25.091', '2026-06-20 06:34:25.091'),
(64, 52, 5, 1, 2, 1, 0, 1, 0, 180, 1, 0, '2026-06-20 06:34:25.234', '2026-06-20 06:34:25.461'),
(65, 54, 5, 1, 1, 1, 0, 0, 0, 90, 0, 0, '2026-06-20 06:34:25.241', '2026-06-20 06:34:25.241'),
(66, 65, 6, 1, 1, 1, 0, 0, 0, 90, 0, 0, '2026-06-20 06:34:25.278', '2026-06-20 06:34:25.278'),
(67, 69, 6, 1, 1, 1, 0, 0, 0, 90, 0, 0, '2026-06-20 06:34:25.286', '2026-06-20 06:34:25.286'),
(68, 76, 7, 1, 1, 1, 0, 0, 0, 90, 0, 0, '2026-06-20 06:34:25.371', '2026-06-20 06:34:25.371'),
(69, 78, 7, 1, 2, 3, 0, 0, 0, 180, 0, 0, '2026-06-20 06:34:25.378', '2026-06-20 06:34:25.529'),
(70, 49, 5, 1, 1, 1, 0, 0, 0, 90, 0, 0, '2026-06-20 06:34:25.453', '2026-06-20 06:34:25.453'),
(71, 62, 6, 1, 1, 1, 0, 0, 0, 90, 0, 0, '2026-06-20 06:34:25.618', '2026-06-20 06:34:25.618'),
(72, 64, 6, 1, 1, 0, 0, 1, 0, 90, 1, 0, '2026-06-20 06:34:25.627', '2026-06-20 06:34:25.627'),
(73, 71, 6, 1, 1, 0, 0, 1, 0, 90, 1, 0, '2026-06-20 06:34:25.634', '2026-06-20 06:34:25.634'),
(74, 94, 8, 1, 1, 1, 0, 0, 0, 90, 0, 0, '2026-06-20 06:34:25.642', '2026-06-20 06:34:25.642'),
(75, 88, 8, 1, 1, 2, 0, 0, 0, 90, 0, 0, '2026-06-20 06:34:25.734', '2026-06-20 06:34:25.734');

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `id` int NOT NULL,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`id`, `name`, `description`, `is_active`, `created_at`, `updated_at`) VALUES
(1, 'admin', 'Quản trị hệ thống', 1, '2026-06-16 14:41:09.901', '2026-06-16 14:41:09.901'),
(2, 'leader', 'Trưởng đội / đại diện đội bóng', 1, '2026-06-16 14:41:09.996', '2026-06-16 14:41:09.996'),
(3, 'player', 'Cầu thủ đã claim tài khoản', 1, '2026-06-16 14:41:10.035', '2026-06-16 14:41:10.035'),
(4, 'referee', 'Trọng tài', 1, '2026-06-16 14:41:10.049', '2026-06-16 14:41:10.049'),
(5, 'user', 'Tài khoản mặc định khi đăng ký', 1, '2026-06-16 14:41:10.060', '2026-06-16 14:41:10.060');

-- --------------------------------------------------------

--
-- Table structure for table `seasons`
--

CREATE TABLE `seasons` (
  `id` int NOT NULL,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` enum('upcoming','registration_open','ongoing','finished','cancelled') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'upcoming',
  `start_date` datetime(3) DEFAULT NULL,
  `end_date` datetime(3) DEFAULT NULL,
  `registration_deadline` datetime(3) DEFAULT NULL,
  `is_registration_open` tinyint(1) NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `tournament_id` int NOT NULL,
  `user_id` int DEFAULT NULL,
  `max_teams` int NOT NULL,
  `registration_fee` decimal(10,2) NOT NULL DEFAULT '0.00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `seasons`
--

INSERT INTO `seasons` (`id`, `name`, `description`, `status`, `start_date`, `end_date`, `registration_deadline`, `is_registration_open`, `is_active`, `created_at`, `updated_at`, `deleted_at`, `tournament_id`, `user_id`, `max_teams`, `registration_fee`) VALUES
(1, 'Mùa Giải 2024', 'Mùa giải chính thức 2024', 'registration_open', '2024-03-01 00:00:00.000', '2024-06-30 00:00:00.000', '2024-02-15 00:00:00.000', 1, 1, '2026-06-19 04:29:32.693', '2026-06-19 04:29:32.693', NULL, 2, 1, 8, 5000000.00),
(2, 'Season Test 01 — 8 teams single group', 'Baseline: 1 group tất cả 8 teams, round-robin', 'upcoming', '2025-03-01 00:00:00.000', '2025-05-01 00:00:00.000', '2025-02-22 00:00:00.000', 0, 1, '2026-06-20 15:30:23.192', '2026-06-20 15:30:23.192', NULL, 1, 1, 16, 0.00),
(3, 'Season Test 02 — 8 teams single group, rest=1', 'Stress test scheduling gap: min rest 1 ngày', 'upcoming', '2025-06-01 00:00:00.000', '2025-08-01 00:00:00.000', '2025-05-25 00:00:00.000', 0, 1, '2026-06-20 15:30:23.368', '2026-06-20 15:30:23.368', NULL, 1, 1, 16, 0.00),
(4, 'Season Test 03 — 2 groups × 4 teams', 'Standard tournament layout', 'upcoming', '2025-09-01 00:00:00.000', '2025-11-01 00:00:00.000', '2025-08-25 00:00:00.000', 0, 1, '2026-06-20 15:30:23.481', '2026-06-20 15:30:23.481', NULL, 1, 1, 16, 0.00),
(5, 'Season Test 04 — 2 groups + semi + final', 'Multi-phase: group → semi_final → final', 'upcoming', '2025-12-01 00:00:00.000', '2026-02-01 00:00:00.000', '2025-11-24 00:00:00.000', 0, 1, '2026-06-20 15:30:23.587', '2026-06-20 15:30:23.587', NULL, 1, 1, 16, 0.00),
(6, 'Season Test 05 — 5 teams (odd, bye expected)', 'Edge: số lẻ → mỗi lượt 1 team bye', 'upcoming', '2026-03-01 00:00:00.000', '2026-05-01 00:00:00.000', '2026-02-22 00:00:00.000', 0, 1, '2026-06-20 15:30:23.740', '2026-06-20 15:30:23.740', NULL, 1, 1, 16, 0.00),
(7, 'Season Test 06 — 7 teams (odd, bye expected)', 'Edge: 7 teams → 1 bye mỗi vòng', 'upcoming', '2026-06-01 00:00:00.000', '2026-08-01 00:00:00.000', '2026-05-25 00:00:00.000', 0, 1, '2026-06-20 15:30:23.814', '2026-06-20 15:30:23.814', NULL, 1, 1, 16, 0.00),
(8, 'Season Test 07 — 2 groups: 4 teams + 3 teams', 'Edge: group B chỉ có 3 team → bye trong B', 'registration_open', '2026-09-01 00:00:00.000', '2026-11-01 00:00:00.000', '2026-08-25 00:00:00.000', 0, 1, '2026-06-20 15:30:23.899', '2026-06-20 15:30:23.899', NULL, 1, 1, 16, 0.00),
(9, 'Season Test 08 — 2 groups: 5 teams + 3 teams', 'Edge: cả hai group đều lẻ', 'registration_open', '2026-12-01 00:00:00.000', '2027-02-01 00:00:00.000', '2026-11-24 00:00:00.000', 0, 1, '2026-06-20 15:30:24.004', '2026-06-20 15:30:24.004', NULL, 1, 1, 16, 0.00),
(10, 'Season Test 09 — 4 groups × 2 teams', 'Minimal group: chỉ 1 trận mỗi group', 'registration_open', '2027-03-01 00:00:00.000', '2027-05-01 00:00:00.000', '2027-02-22 00:00:00.000', 0, 1, '2026-06-20 15:30:24.107', '2026-06-20 15:30:24.107', NULL, 1, 1, 16, 0.00),
(11, 'Season Test 10 — 8 teams, rest=7 days', 'Rest constraint cực cao → scheduler phải spread dài', 'ongoing', '2027-06-01 00:00:00.000', '2027-08-01 00:00:00.000', '2027-05-25 00:00:00.000', 0, 1, '2026-06-20 15:30:24.237', '2026-06-20 15:33:28.117', NULL, 1, 1, 16, 0.00),
(12, 'AutoSched Test 01 — 8 teams single group', 'Baseline: 1 group tất cả 8 teams, round-robin', 'upcoming', '2025-03-01 00:00:00.000', '2025-05-01 00:00:00.000', '2025-02-22 00:00:00.000', 0, 1, '2026-06-22 02:54:27.870', '2026-06-22 02:54:27.870', NULL, 1, 1, 16, 0.00),
(13, 'AutoSched Test 02 — 8 teams, rest=1', 'Stress test scheduling gap: min rest 1 ngày', 'upcoming', '2025-06-01 00:00:00.000', '2025-08-01 00:00:00.000', '2025-05-25 00:00:00.000', 0, 1, '2026-06-22 02:54:28.074', '2026-06-22 02:54:28.074', NULL, 1, 1, 16, 0.00),
(14, 'AutoSched Test 03 — 2 groups × 4 teams', 'Standard tournament layout', 'upcoming', '2025-09-01 00:00:00.000', '2025-11-01 00:00:00.000', '2025-08-25 00:00:00.000', 0, 1, '2026-06-22 02:54:28.226', '2026-06-22 02:54:28.226', NULL, 1, 1, 16, 0.00),
(15, 'AutoSched Test 04 — 2 groups + semi + final', 'Multi-phase: group → semi_final → final', 'upcoming', '2025-12-01 00:00:00.000', '2026-02-01 00:00:00.000', '2025-11-24 00:00:00.000', 0, 1, '2026-06-22 02:54:28.305', '2026-06-22 02:54:28.305', NULL, 1, 1, 16, 0.00),
(16, 'AutoSched Test 05 — 5 teams (odd, bye)', 'Edge: số lẻ → mỗi lượt 1 team bye', 'upcoming', '2026-03-01 00:00:00.000', '2026-05-01 00:00:00.000', '2026-02-22 00:00:00.000', 0, 1, '2026-06-22 02:54:28.583', '2026-06-22 02:54:28.583', NULL, 1, 1, 16, 0.00),
(17, 'AutoSched Test 06 — 7 teams (odd, bye)', 'Edge: 7 teams → 1 bye mỗi vòng', 'upcoming', '2026-06-01 00:00:00.000', '2026-08-01 00:00:00.000', '2026-05-25 00:00:00.000', 0, 1, '2026-06-22 02:54:28.656', '2026-06-22 02:54:28.656', NULL, 1, 1, 16, 0.00),
(18, 'AutoSched Test 07 — 4+3 teams', 'Edge: group B chỉ có 3 team → bye trong B', 'upcoming', '2026-09-01 00:00:00.000', '2026-11-01 00:00:00.000', '2026-08-25 00:00:00.000', 0, 1, '2026-06-22 02:54:28.730', '2026-06-22 02:54:28.730', NULL, 1, 1, 16, 0.00),
(19, 'AutoSched Test 08 — 5+3 teams', 'Edge: cả hai group đều lẻ', 'upcoming', '2026-12-01 00:00:00.000', '2027-02-01 00:00:00.000', '2026-11-24 00:00:00.000', 0, 1, '2026-06-22 02:54:28.799', '2026-06-22 02:54:28.799', NULL, 1, 1, 16, 0.00),
(20, 'AutoSched Test 09 — 4 groups × 2 teams', 'Minimal group: chỉ 1 trận mỗi group', 'upcoming', '2027-03-01 00:00:00.000', '2027-05-01 00:00:00.000', '2027-02-22 00:00:00.000', 0, 1, '2026-06-22 02:54:28.975', '2026-06-22 02:54:28.975', NULL, 1, 1, 16, 0.00),
(21, 'AutoSched Test 10 — 8 teams, rest=7', 'Rest constraint cực cao → scheduler phải spread dài', 'registration_open', '2027-06-01 00:00:00.000', '2027-08-01 00:00:00.000', '2027-05-25 00:00:00.000', 0, 1, '2026-06-22 02:54:29.064', '2026-06-22 02:54:29.064', NULL, 1, 1, 16, 0.00);

-- --------------------------------------------------------

--
-- Table structure for table `season_teams`
--

CREATE TABLE `season_teams` (
  `id` int NOT NULL,
  `season_id` int NOT NULL,
  `team_id` int NOT NULL,
  `status` enum('approved','pending','active','eliminated','withdrawn') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `group_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `season_teams`
--

INSERT INTO `season_teams` (`id`, `season_id`, `team_id`, `status`, `is_active`, `created_at`, `updated_at`, `deleted_at`, `group_id`, `user_id`) VALUES
(1, 1, 1, 'active', 1, '2026-06-19 04:29:33.519', '2026-06-20 06:34:24.380', NULL, 3, 6),
(2, 1, 2, 'active', 1, '2026-06-19 04:29:34.202', '2026-06-20 06:34:24.380', NULL, 3, 19),
(3, 1, 3, 'active', 1, '2026-06-19 04:29:34.783', '2026-06-20 06:34:24.380', NULL, 3, 32),
(4, 1, 4, 'active', 1, '2026-06-19 04:29:35.268', '2026-06-20 06:34:24.380', NULL, 3, 45),
(5, 1, 5, 'active', 1, '2026-06-19 04:29:35.705', '2026-06-20 06:34:24.417', NULL, 4, 58),
(6, 1, 6, 'active', 1, '2026-06-19 04:29:36.086', '2026-06-20 06:34:24.417', NULL, 4, 71),
(7, 1, 7, 'active', 1, '2026-06-19 04:29:36.458', '2026-06-20 06:34:24.417', NULL, 4, 84),
(8, 1, 8, 'active', 1, '2026-06-19 04:29:36.818', '2026-06-20 06:34:24.417', NULL, 4, 97),
(9, 2, 1, 'active', 1, '2026-06-20 15:30:23.223', '2026-06-20 15:30:23.355', NULL, 5, 1),
(10, 2, 2, 'active', 1, '2026-06-20 15:30:23.240', '2026-06-20 15:30:23.355', NULL, 5, 1),
(11, 2, 3, 'active', 1, '2026-06-20 15:30:23.254', '2026-06-20 15:30:23.355', NULL, 5, 1),
(12, 2, 4, 'active', 1, '2026-06-20 15:30:23.265', '2026-06-20 15:30:23.355', NULL, 5, 1),
(13, 2, 5, 'active', 1, '2026-06-20 15:30:23.277', '2026-06-20 15:30:23.355', NULL, 5, 1),
(14, 2, 6, 'active', 1, '2026-06-20 15:30:23.287', '2026-06-20 15:30:23.355', NULL, 5, 1),
(15, 2, 7, 'active', 1, '2026-06-20 15:30:23.298', '2026-06-20 15:30:23.355', NULL, 5, 1),
(16, 2, 8, 'active', 1, '2026-06-20 15:30:23.308', '2026-06-20 15:30:23.355', NULL, 5, 1),
(17, 3, 1, 'active', 1, '2026-06-20 15:30:23.378', '2026-06-20 15:30:23.471', NULL, 6, 1),
(18, 3, 2, 'active', 1, '2026-06-20 15:30:23.390', '2026-06-20 15:30:23.471', NULL, 6, 1),
(19, 3, 3, 'active', 1, '2026-06-20 15:30:23.399', '2026-06-20 15:30:23.471', NULL, 6, 1),
(20, 3, 4, 'active', 1, '2026-06-20 15:30:23.409', '2026-06-20 15:30:23.471', NULL, 6, 1),
(21, 3, 5, 'active', 1, '2026-06-20 15:30:23.418', '2026-06-20 15:30:23.471', NULL, 6, 1),
(22, 3, 6, 'active', 1, '2026-06-20 15:30:23.427', '2026-06-20 15:30:23.471', NULL, 6, 1),
(23, 3, 7, 'active', 1, '2026-06-20 15:30:23.436', '2026-06-20 15:30:23.471', NULL, 6, 1),
(24, 3, 8, 'active', 1, '2026-06-20 15:30:23.444', '2026-06-20 15:30:23.471', NULL, 6, 1),
(25, 4, 1, 'active', 1, '2026-06-20 15:30:23.489', '2026-06-20 15:30:23.565', NULL, 7, 1),
(26, 4, 2, 'active', 1, '2026-06-20 15:30:23.496', '2026-06-20 15:30:23.565', NULL, 7, 1),
(27, 4, 3, 'active', 1, '2026-06-20 15:30:23.505', '2026-06-20 15:30:23.565', NULL, 7, 1),
(28, 4, 4, 'active', 1, '2026-06-20 15:30:23.513', '2026-06-20 15:30:23.565', NULL, 7, 1),
(29, 4, 5, 'active', 1, '2026-06-20 15:30:23.520', '2026-06-20 15:30:23.578', NULL, 8, 1),
(30, 4, 6, 'active', 1, '2026-06-20 15:30:23.527', '2026-06-20 15:30:23.578', NULL, 8, 1),
(31, 4, 7, 'active', 1, '2026-06-20 15:30:23.536', '2026-06-20 15:30:23.578', NULL, 8, 1),
(32, 4, 8, 'active', 1, '2026-06-20 15:30:23.545', '2026-06-20 15:30:23.578', NULL, 8, 1),
(33, 5, 1, 'active', 1, '2026-06-20 15:30:23.596', '2026-06-20 15:30:23.695', NULL, 9, 1),
(34, 5, 2, 'active', 1, '2026-06-20 15:30:23.604', '2026-06-20 15:30:23.695', NULL, 9, 1),
(35, 5, 3, 'active', 1, '2026-06-20 15:30:23.612', '2026-06-20 15:30:23.695', NULL, 9, 1),
(36, 5, 4, 'active', 1, '2026-06-20 15:30:23.621', '2026-06-20 15:30:23.695', NULL, 9, 1),
(37, 5, 5, 'active', 1, '2026-06-20 15:30:23.646', '2026-06-20 15:30:23.710', NULL, 10, 1),
(38, 5, 6, 'active', 1, '2026-06-20 15:30:23.654', '2026-06-20 15:30:23.710', NULL, 10, 1),
(39, 5, 7, 'active', 1, '2026-06-20 15:30:23.662', '2026-06-20 15:30:23.710', NULL, 10, 1),
(40, 5, 8, 'active', 1, '2026-06-20 15:30:23.670', '2026-06-20 15:30:23.710', NULL, 10, 1),
(41, 6, 1, 'active', 1, '2026-06-20 15:30:23.748', '2026-06-20 15:30:23.803', NULL, NULL, 1),
(42, 6, 2, 'active', 1, '2026-06-20 15:30:23.756', '2026-06-20 15:30:23.803', NULL, NULL, 1),
(43, 6, 3, 'active', 1, '2026-06-20 15:30:23.764', '2026-06-20 15:30:23.803', NULL, NULL, 1),
(44, 6, 4, 'active', 1, '2026-06-20 15:30:23.773', '2026-06-20 15:30:23.803', NULL, NULL, 1),
(45, 6, 5, 'active', 1, '2026-06-20 15:30:23.780', '2026-06-20 15:30:23.803', NULL, NULL, 1),
(46, 7, 1, 'active', 1, '2026-06-20 15:30:23.821', '2026-06-20 15:30:23.892', NULL, NULL, 1),
(47, 7, 2, 'active', 1, '2026-06-20 15:30:23.830', '2026-06-20 15:30:23.892', NULL, NULL, 1),
(48, 7, 3, 'active', 1, '2026-06-20 15:30:23.837', '2026-06-20 15:30:23.892', NULL, NULL, 1),
(49, 7, 4, 'active', 1, '2026-06-20 15:30:23.846', '2026-06-20 15:30:23.892', NULL, NULL, 1),
(50, 7, 5, 'active', 1, '2026-06-20 15:30:23.856', '2026-06-20 15:30:23.892', NULL, NULL, 1),
(51, 7, 6, 'active', 1, '2026-06-20 15:30:23.863', '2026-06-20 15:30:23.892', NULL, NULL, 1),
(52, 7, 7, 'active', 1, '2026-06-20 15:30:23.871', '2026-06-20 15:30:23.892', NULL, NULL, 1),
(53, 8, 1, 'active', 1, '2026-06-20 15:30:23.907', '2026-06-20 15:30:23.982', NULL, NULL, 1),
(54, 8, 2, 'active', 1, '2026-06-20 15:30:23.915', '2026-06-20 15:30:23.982', NULL, NULL, 1),
(55, 8, 3, 'active', 1, '2026-06-20 15:30:23.922', '2026-06-20 15:30:23.982', NULL, NULL, 1),
(56, 8, 4, 'active', 1, '2026-06-20 15:30:23.929', '2026-06-20 15:30:23.982', NULL, NULL, 1),
(57, 8, 5, 'active', 1, '2026-06-20 15:30:23.937', '2026-06-20 15:30:23.996', NULL, NULL, 1),
(58, 8, 6, 'active', 1, '2026-06-20 15:30:23.945', '2026-06-20 15:30:23.996', NULL, NULL, 1),
(59, 8, 7, 'active', 1, '2026-06-20 15:30:23.953', '2026-06-20 15:30:23.996', NULL, NULL, 1),
(60, 8, 8, 'active', 1, '2026-06-20 15:30:23.962', '2026-06-20 15:30:23.962', NULL, NULL, 1),
(61, 9, 1, 'active', 1, '2026-06-20 15:30:24.012', '2026-06-20 15:30:24.087', NULL, NULL, 1),
(62, 9, 2, 'active', 1, '2026-06-20 15:30:24.020', '2026-06-20 15:30:24.087', NULL, NULL, 1),
(63, 9, 3, 'active', 1, '2026-06-20 15:30:24.028', '2026-06-20 15:30:24.087', NULL, NULL, 1),
(64, 9, 4, 'active', 1, '2026-06-20 15:30:24.036', '2026-06-20 15:30:24.087', NULL, NULL, 1),
(65, 9, 5, 'active', 1, '2026-06-20 15:30:24.043', '2026-06-20 15:30:24.087', NULL, NULL, 1),
(66, 9, 6, 'active', 1, '2026-06-20 15:30:24.052', '2026-06-20 15:30:24.099', NULL, NULL, 1),
(67, 9, 7, 'active', 1, '2026-06-20 15:30:24.060', '2026-06-20 15:30:24.099', NULL, NULL, 1),
(68, 9, 8, 'active', 1, '2026-06-20 15:30:24.066', '2026-06-20 15:30:24.099', NULL, NULL, 1),
(69, 10, 1, 'active', 1, '2026-06-20 15:30:24.115', '2026-06-20 15:30:24.188', NULL, NULL, 1),
(70, 10, 2, 'active', 1, '2026-06-20 15:30:24.122', '2026-06-20 15:30:24.188', NULL, NULL, 1),
(71, 10, 3, 'active', 1, '2026-06-20 15:30:24.131', '2026-06-20 15:30:24.200', NULL, NULL, 1),
(72, 10, 4, 'active', 1, '2026-06-20 15:30:24.138', '2026-06-20 15:30:24.200', NULL, NULL, 1),
(73, 10, 5, 'active', 1, '2026-06-20 15:30:24.145', '2026-06-20 15:30:24.214', NULL, NULL, 1),
(74, 10, 6, 'active', 1, '2026-06-20 15:30:24.153', '2026-06-20 15:30:24.214', NULL, NULL, 1),
(75, 10, 7, 'active', 1, '2026-06-20 15:30:24.161', '2026-06-20 15:30:24.228', NULL, NULL, 1),
(76, 10, 8, 'active', 1, '2026-06-20 15:30:24.169', '2026-06-20 15:30:24.228', NULL, NULL, 1),
(77, 11, 1, 'active', 1, '2026-06-20 15:30:24.246', '2026-06-20 15:33:28.062', NULL, 22, 1),
(78, 11, 2, 'active', 1, '2026-06-20 15:30:24.254', '2026-06-20 15:33:28.067', NULL, 23, 1),
(79, 11, 3, 'active', 1, '2026-06-20 15:30:24.261', '2026-06-20 15:33:28.067', NULL, 23, 1),
(80, 11, 4, 'active', 1, '2026-06-20 15:30:24.269', '2026-06-20 15:33:28.062', NULL, 22, 1),
(81, 11, 5, 'active', 1, '2026-06-20 15:30:24.277', '2026-06-20 15:33:28.062', NULL, 22, 1),
(82, 11, 6, 'active', 1, '2026-06-20 15:30:24.284', '2026-06-20 15:33:28.062', NULL, 22, 1),
(83, 11, 7, 'active', 1, '2026-06-20 15:30:24.291', '2026-06-20 15:33:28.067', NULL, 23, 1),
(84, 11, 8, 'active', 1, '2026-06-20 15:30:24.298', '2026-06-20 15:33:28.067', NULL, 23, 1),
(85, 12, 1, 'active', 1, '2026-06-22 02:54:27.913', '2026-06-22 02:54:27.913', NULL, NULL, 1),
(86, 12, 2, 'active', 1, '2026-06-22 02:54:27.929', '2026-06-22 02:54:27.929', NULL, NULL, 1),
(87, 12, 3, 'active', 1, '2026-06-22 02:54:27.938', '2026-06-22 02:54:27.938', NULL, NULL, 1),
(88, 12, 4, 'active', 1, '2026-06-22 02:54:27.949', '2026-06-22 02:54:27.949', NULL, NULL, 1),
(89, 12, 5, 'active', 1, '2026-06-22 02:54:27.958', '2026-06-22 02:54:27.958', NULL, NULL, 1),
(90, 12, 6, 'active', 1, '2026-06-22 02:54:27.971', '2026-06-22 02:54:27.971', NULL, NULL, 1),
(91, 12, 7, 'active', 1, '2026-06-22 02:54:27.984', '2026-06-22 02:54:27.984', NULL, NULL, 1),
(92, 12, 8, 'active', 1, '2026-06-22 02:54:27.994', '2026-06-22 02:54:27.994', NULL, NULL, 1),
(93, 13, 1, 'active', 1, '2026-06-22 02:54:28.147', '2026-06-22 02:54:28.147', NULL, NULL, 1),
(94, 13, 2, 'active', 1, '2026-06-22 02:54:28.155', '2026-06-22 02:54:28.155', NULL, NULL, 1),
(95, 13, 3, 'active', 1, '2026-06-22 02:54:28.164', '2026-06-22 02:54:28.164', NULL, NULL, 1),
(96, 13, 4, 'active', 1, '2026-06-22 02:54:28.172', '2026-06-22 02:54:28.172', NULL, NULL, 1),
(97, 13, 5, 'active', 1, '2026-06-22 02:54:28.181', '2026-06-22 02:54:28.181', NULL, NULL, 1),
(98, 13, 6, 'active', 1, '2026-06-22 02:54:28.189', '2026-06-22 02:54:28.189', NULL, NULL, 1),
(99, 13, 7, 'active', 1, '2026-06-22 02:54:28.198', '2026-06-22 02:54:28.198', NULL, NULL, 1),
(100, 13, 8, 'active', 1, '2026-06-22 02:54:28.207', '2026-06-22 02:54:28.207', NULL, NULL, 1),
(101, 14, 1, 'active', 1, '2026-06-22 02:54:28.236', '2026-06-22 02:54:28.236', NULL, NULL, 1),
(102, 14, 2, 'active', 1, '2026-06-22 02:54:28.248', '2026-06-22 02:54:28.248', NULL, NULL, 1),
(103, 14, 3, 'active', 1, '2026-06-22 02:54:28.256', '2026-06-22 02:54:28.256', NULL, NULL, 1),
(104, 14, 4, 'active', 1, '2026-06-22 02:54:28.265', '2026-06-22 02:54:28.265', NULL, NULL, 1),
(105, 14, 5, 'active', 1, '2026-06-22 02:54:28.272', '2026-06-22 02:54:28.272', NULL, NULL, 1),
(106, 14, 6, 'active', 1, '2026-06-22 02:54:28.281', '2026-06-22 02:54:28.281', NULL, NULL, 1),
(107, 14, 7, 'active', 1, '2026-06-22 02:54:28.289', '2026-06-22 02:54:28.289', NULL, NULL, 1),
(108, 14, 8, 'active', 1, '2026-06-22 02:54:28.297', '2026-06-22 02:54:28.297', NULL, NULL, 1),
(109, 15, 1, 'active', 1, '2026-06-22 02:54:28.314', '2026-06-22 02:54:28.314', NULL, NULL, 1),
(110, 15, 2, 'active', 1, '2026-06-22 02:54:28.326', '2026-06-22 02:54:28.326', NULL, NULL, 1),
(111, 15, 3, 'active', 1, '2026-06-22 02:54:28.338', '2026-06-22 02:54:28.338', NULL, NULL, 1),
(112, 15, 4, 'active', 1, '2026-06-22 02:54:28.352', '2026-06-22 02:54:28.352', NULL, NULL, 1),
(113, 15, 5, 'active', 1, '2026-06-22 02:54:28.360', '2026-06-22 02:54:28.360', NULL, NULL, 1),
(114, 15, 6, 'active', 1, '2026-06-22 02:54:28.370', '2026-06-22 02:54:28.370', NULL, NULL, 1),
(115, 15, 7, 'active', 1, '2026-06-22 02:54:28.471', '2026-06-22 02:54:28.471', NULL, NULL, 1),
(116, 15, 8, 'active', 1, '2026-06-22 02:54:28.567', '2026-06-22 02:54:28.567', NULL, NULL, 1),
(117, 16, 1, 'active', 1, '2026-06-22 02:54:28.591', '2026-06-22 02:54:28.591', NULL, NULL, 1),
(118, 16, 2, 'active', 1, '2026-06-22 02:54:28.600', '2026-06-22 02:54:28.600', NULL, NULL, 1),
(119, 16, 3, 'active', 1, '2026-06-22 02:54:28.608', '2026-06-22 02:54:28.608', NULL, NULL, 1),
(120, 16, 4, 'active', 1, '2026-06-22 02:54:28.617', '2026-06-22 02:54:28.617', NULL, NULL, 1),
(121, 16, 5, 'active', 1, '2026-06-22 02:54:28.645', '2026-06-22 02:54:28.645', NULL, NULL, 1),
(122, 17, 1, 'active', 1, '2026-06-22 02:54:28.666', '2026-06-22 02:54:28.666', NULL, NULL, 1),
(123, 17, 2, 'active', 1, '2026-06-22 02:54:28.677', '2026-06-22 02:54:28.677', NULL, NULL, 1),
(124, 17, 3, 'active', 1, '2026-06-22 02:54:28.688', '2026-06-22 02:54:28.688', NULL, NULL, 1),
(125, 17, 4, 'active', 1, '2026-06-22 02:54:28.697', '2026-06-22 02:54:28.697', NULL, NULL, 1),
(126, 17, 5, 'active', 1, '2026-06-22 02:54:28.705', '2026-06-22 02:54:28.705', NULL, NULL, 1),
(127, 17, 6, 'active', 1, '2026-06-22 02:54:28.712', '2026-06-22 02:54:28.712', NULL, NULL, 1),
(128, 17, 7, 'active', 1, '2026-06-22 02:54:28.719', '2026-06-22 02:54:28.719', NULL, NULL, 1),
(129, 18, 1, 'active', 1, '2026-06-22 02:54:28.736', '2026-06-22 02:54:28.736', NULL, NULL, 1),
(130, 18, 2, 'active', 1, '2026-06-22 02:54:28.743', '2026-06-22 02:54:28.743', NULL, NULL, 1),
(131, 18, 3, 'active', 1, '2026-06-22 02:54:28.752', '2026-06-22 02:54:28.752', NULL, NULL, 1),
(132, 18, 4, 'active', 1, '2026-06-22 02:54:28.760', '2026-06-22 02:54:28.760', NULL, NULL, 1),
(133, 18, 5, 'active', 1, '2026-06-22 02:54:28.770', '2026-06-22 02:54:28.770', NULL, NULL, 1),
(134, 18, 6, 'active', 1, '2026-06-22 02:54:28.777', '2026-06-22 02:54:28.777', NULL, NULL, 1),
(135, 18, 7, 'active', 1, '2026-06-22 02:54:28.784', '2026-06-22 02:54:28.784', NULL, NULL, 1),
(136, 18, 8, 'active', 1, '2026-06-22 02:54:28.790', '2026-06-22 02:54:28.790', NULL, NULL, 1),
(137, 19, 1, 'active', 1, '2026-06-22 02:54:28.807', '2026-06-22 02:54:28.807', NULL, NULL, 1),
(138, 19, 2, 'active', 1, '2026-06-22 02:54:28.814', '2026-06-22 02:54:28.814', NULL, NULL, 1),
(139, 19, 3, 'active', 1, '2026-06-22 02:54:28.859', '2026-06-22 02:54:28.859', NULL, NULL, 1),
(140, 19, 4, 'active', 1, '2026-06-22 02:54:28.915', '2026-06-22 02:54:28.915', NULL, NULL, 1),
(141, 19, 5, 'active', 1, '2026-06-22 02:54:28.924', '2026-06-22 02:54:28.924', NULL, NULL, 1),
(142, 19, 6, 'active', 1, '2026-06-22 02:54:28.931', '2026-06-22 02:54:28.931', NULL, NULL, 1),
(143, 19, 7, 'active', 1, '2026-06-22 02:54:28.938', '2026-06-22 02:54:28.938', NULL, NULL, 1),
(144, 19, 8, 'active', 1, '2026-06-22 02:54:28.946', '2026-06-22 02:54:28.946', NULL, NULL, 1),
(145, 20, 1, 'active', 1, '2026-06-22 02:54:28.982', '2026-06-22 02:54:28.982', NULL, NULL, 1),
(146, 20, 2, 'active', 1, '2026-06-22 02:54:28.989', '2026-06-22 02:54:28.989', NULL, NULL, 1),
(147, 20, 3, 'active', 1, '2026-06-22 02:54:28.996', '2026-06-22 02:54:28.996', NULL, NULL, 1),
(148, 20, 4, 'active', 1, '2026-06-22 02:54:29.003', '2026-06-22 02:54:29.003', NULL, NULL, 1),
(149, 20, 5, 'active', 1, '2026-06-22 02:54:29.010', '2026-06-22 02:54:29.010', NULL, NULL, 1),
(150, 20, 6, 'active', 1, '2026-06-22 02:54:29.018', '2026-06-22 02:54:29.018', NULL, NULL, 1),
(151, 20, 7, 'active', 1, '2026-06-22 02:54:29.044', '2026-06-22 02:54:29.044', NULL, NULL, 1),
(152, 20, 8, 'active', 1, '2026-06-22 02:54:29.052', '2026-06-22 02:54:29.052', NULL, NULL, 1),
(153, 21, 1, 'active', 1, '2026-06-22 02:54:29.071', '2026-06-22 02:54:29.071', NULL, NULL, 1),
(154, 21, 2, 'active', 1, '2026-06-22 02:54:29.078', '2026-06-22 02:54:29.078', NULL, NULL, 1),
(155, 21, 3, 'active', 1, '2026-06-22 02:54:29.086', '2026-06-22 02:54:29.086', NULL, NULL, 1),
(156, 21, 4, 'active', 1, '2026-06-22 02:54:29.102', '2026-06-22 02:54:29.102', NULL, NULL, 1),
(157, 21, 5, 'active', 1, '2026-06-22 02:54:29.110', '2026-06-22 02:54:29.110', NULL, NULL, 1),
(158, 21, 6, 'active', 1, '2026-06-22 02:54:29.118', '2026-06-22 02:54:29.118', NULL, NULL, 1),
(159, 21, 7, 'active', 1, '2026-06-22 02:54:29.125', '2026-06-22 02:54:29.125', NULL, NULL, 1),
(160, 21, 8, 'active', 1, '2026-06-22 02:54:29.132', '2026-06-22 02:54:29.132', NULL, NULL, 1);

-- --------------------------------------------------------

--
-- Table structure for table `teams`
--

CREATE TABLE `teams` (
  `id` int NOT NULL,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `coach_name` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `logo` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `user_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `teams`
--

INSERT INTO `teams` (`id`, `name`, `coach_name`, `logo`, `description`, `is_active`, `created_at`, `updated_at`, `deleted_at`, `user_id`) VALUES
(1, 'FC Rồng Vàng', 'Trần Văn Hùng', NULL, NULL, 1, '2026-06-19 04:29:32.912', '2026-06-19 04:29:32.912', NULL, 1),
(2, 'CLB Sao Biển', 'Nguyễn Minh Tuấn', NULL, NULL, 1, '2026-06-19 04:29:33.753', '2026-06-19 04:29:33.753', NULL, 1),
(3, 'FC Hùng Mạnh', 'Lê Văn Đức', NULL, NULL, 1, '2026-06-19 04:29:34.365', '2026-06-19 04:29:34.365', NULL, 1),
(4, 'CLB Thần Tốc', 'Phạm Quốc Bảo', NULL, NULL, 1, '2026-06-19 04:29:34.920', '2026-06-19 04:29:34.920', NULL, 1),
(5, 'FC Bất Bại', 'Hoàng Văn Nam', NULL, NULL, 1, '2026-06-19 04:29:35.388', '2026-06-19 04:29:35.388', NULL, 1),
(6, 'CLB Chiến Thắng', 'Vũ Đình Sơn', NULL, NULL, 1, '2026-06-19 04:29:35.787', '2026-06-19 04:29:35.787', NULL, 1),
(7, 'FC Bão Lửa', 'Đặng Minh Khoa', NULL, NULL, 1, '2026-06-19 04:29:36.161', '2026-06-19 04:29:36.161', NULL, 1),
(8, 'CLB Thiên Lôi', 'Bùi Văn Tùng', NULL, NULL, 1, '2026-06-19 04:29:36.535', '2026-06-19 04:29:36.535', NULL, 1);

-- --------------------------------------------------------

--
-- Table structure for table `team_leaders`
--

CREATE TABLE `team_leaders` (
  `id` int NOT NULL,
  `team_id` int NOT NULL,
  `user_id` int NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `team_leaders`
--

INSERT INTO `team_leaders` (`id`, `team_id`, `user_id`, `is_active`, `created_at`, `updated_at`, `deleted_at`) VALUES
(1, 1, 6, 1, '2026-06-19 04:29:32.973', '2026-06-19 04:29:32.973', NULL),
(2, 2, 19, 1, '2026-06-19 04:29:33.775', '2026-06-19 04:29:33.775', NULL),
(3, 3, 32, 1, '2026-06-19 04:29:34.384', '2026-06-19 04:29:34.384', NULL),
(4, 4, 45, 1, '2026-06-19 04:29:34.940', '2026-06-19 04:29:34.940', NULL),
(5, 5, 58, 1, '2026-06-19 04:29:35.402', '2026-06-19 04:29:35.402', NULL),
(6, 6, 71, 1, '2026-06-19 04:29:35.799', '2026-06-19 04:29:35.799', NULL),
(7, 7, 84, 1, '2026-06-19 04:29:36.174', '2026-06-19 04:29:36.174', NULL),
(8, 8, 97, 1, '2026-06-19 04:29:36.545', '2026-06-19 04:29:36.545', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `team_players`
--

CREATE TABLE `team_players` (
  `id` int NOT NULL,
  `team_id` int NOT NULL,
  `player_id` int NOT NULL,
  `jersey_number` int NOT NULL,
  `position` enum('goalkeeper','defender','midfielder','forward') COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` enum('player','captain','vice_captain') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'player',
  `status` enum('active','injured','suspended') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'active',
  `approval_status` enum('pending','approved','rejected') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `is_starter` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `team_players`
--

INSERT INTO `team_players` (`id`, `team_id`, `player_id`, `jersey_number`, `position`, `role`, `status`, `approval_status`, `is_active`, `created_at`, `updated_at`, `deleted_at`, `user_id`,`is_starter`) VALUES
(1, 1, 1, 1, 'goalkeeper', 'captain', 'active', 'approved', 1, '2026-06-19 04:29:33.072', '2026-06-19 04:29:33.072', NULL, 1,1),
(2, 1, 2, 2, 'defender', 'vice_captain', 'active', 'approved', 1, '2026-06-19 04:29:33.115', '2026-06-19 04:29:33.115', NULL, 1,1),
(3, 1, 3, 3, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:33.159', '2026-06-19 04:29:33.159', NULL, 1,1),
(4, 1, 4, 4, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:33.194', '2026-06-19 04:29:33.194', NULL, 1,1),
(5, 1, 5, 5, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:33.226', '2026-06-19 04:29:33.226', NULL, 1,1),
(6, 1, 6, 6, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:33.260', '2026-06-19 04:29:33.260', NULL, 1,1),
(7, 1, 7, 7, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:33.295', '2026-06-19 04:29:33.295', NULL, 1,1),
(8, 1, 8, 8, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:33.334', '2026-06-19 04:29:33.334', NULL, 1,1),
(9, 1, 9, 9, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:33.367', '2026-06-19 04:29:33.367', NULL, 1,1),
(10, 1, 10, 10, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:33.403', '2026-06-19 04:29:33.403', NULL, 1,1),
(11, 1, 11, 11, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:33.434', '2026-06-19 04:29:33.434', NULL, 1,1),
(12, 1, 12, 12, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:33.475', '2026-06-19 04:29:33.475', NULL, 1,0),
(13, 2, 13, 1, 'goalkeeper', 'captain', 'active', 'approved', 1, '2026-06-19 04:29:33.811', '2026-06-19 04:29:33.811', NULL, 1,1),
(14, 2, 14, 2, 'defender', 'vice_captain', 'active', 'approved', 1, '2026-06-19 04:29:33.850', '2026-06-19 04:29:33.850', NULL, 1,1),
(15, 2, 15, 3, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:33.886', '2026-06-19 04:29:33.886', NULL, 1,1),
(16, 2, 16, 4, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:33.917', '2026-06-19 04:29:33.917', NULL, 1,1),
(17, 2, 17, 5, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:33.947', '2026-06-19 04:29:33.947', NULL, 1,1),
(18, 2, 18, 6, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:33.980', '2026-06-19 04:29:33.980', NULL, 1,1),
(19, 2, 19, 7, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:34.012', '2026-06-19 04:29:34.012', NULL, 1,1),
(20, 2, 20, 8, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:34.045', '2026-06-19 04:29:34.045', NULL, 1,1),
(21, 2, 21, 9, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:34.077', '2026-06-19 04:29:34.077', NULL, 1,1),
(22, 2, 22, 10, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:34.115', '2026-06-19 04:29:34.115', NULL, 1,1),
(23, 2, 23, 11, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:34.146', '2026-06-19 04:29:34.146', NULL, 1,1),
(24, 2, 24, 12, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:34.181', '2026-06-19 04:29:34.181', NULL, 1,0),
(25, 3, 25, 1, 'goalkeeper', 'captain', 'active', 'approved', 1, '2026-06-19 04:29:34.418', '2026-06-19 04:29:34.418', NULL, 1,1),
(26, 3, 26, 2, 'defender', 'vice_captain', 'active', 'approved', 1, '2026-06-19 04:29:34.453', '2026-06-19 04:29:34.453', NULL, 1,1),
(27, 3, 27, 3, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:34.490', '2026-06-19 04:29:34.490', NULL, 1,1),
(28, 3, 28, 4, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:34.522', '2026-06-19 04:29:34.522', NULL, 1,1),
(29, 3, 29, 5, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:34.553', '2026-06-19 04:29:34.553', NULL, 1,1),
(30, 3, 30, 6, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:34.586', '2026-06-19 04:29:34.586', NULL, 1,1),
(31, 3, 31, 7, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:34.619', '2026-06-19 04:29:34.619', NULL, 1,1),
(32, 3, 32, 8, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:34.652', '2026-06-19 04:29:34.652', NULL, 1,1),
(33, 3, 33, 9, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:34.686', '2026-06-19 04:29:34.686', NULL, 1,1),
(34, 3, 34, 10, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:34.714', '2026-06-19 04:29:34.714', NULL, 1,1),
(35, 3, 35, 11, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:34.739', '2026-06-19 04:29:34.739', NULL, 1,1),
(36, 3, 36, 12, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:34.771', '2026-06-19 04:29:34.771', NULL, 1,0),
(37, 4, 37, 1, 'goalkeeper', 'captain', 'active', 'approved', 1, '2026-06-19 04:29:34.972', '2026-06-19 04:29:34.972', NULL, 1,1),
(38, 4, 38, 2, 'defender', 'vice_captain', 'active', 'approved', 1, '2026-06-19 04:29:34.999', '2026-06-19 04:29:34.999', NULL, 1,1),
(39, 4, 39, 3, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.020', '2026-06-19 04:29:35.020', NULL, 1,1),
(40, 4, 40, 4, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.042', '2026-06-19 04:29:35.042', NULL, 1,1),
(41, 4, 41, 5, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.064', '2026-06-19 04:29:35.064', NULL, 1,1),
(42, 4, 42, 6, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.086', '2026-06-19 04:29:35.086', NULL, 1,1),
(43, 4, 43, 7, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.111', '2026-06-19 04:29:35.111', NULL, 1,1),
(44, 4, 44, 8, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.138', '2026-06-19 04:29:35.138', NULL, 1,1),
(45, 4, 45, 9, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.169', '2026-06-19 04:29:35.169', NULL, 1,1),
(46, 4, 46, 10, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.199', '2026-06-19 04:29:35.199', NULL, 1,1),
(47, 4, 47, 11, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.230', '2026-06-19 04:29:35.230', NULL, 1,1),
(48, 4, 48, 12, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.260', '2026-06-19 04:29:35.260', NULL, 1,0),
(49, 5, 49, 1, 'goalkeeper', 'captain', 'active', 'approved', 1, '2026-06-19 04:29:35.428', '2026-06-19 04:29:35.428', NULL, 1,1),
(50, 5, 50, 2, 'defender', 'vice_captain', 'active', 'approved', 1, '2026-06-19 04:29:35.458', '2026-06-19 04:29:35.458', NULL, 1,1),
(51, 5, 51, 3, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.487', '2026-06-19 04:29:35.487', NULL, 1,1),
(52, 5, 52, 4, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.515', '2026-06-19 04:29:35.515', NULL, 1,1),
(53, 5, 53, 5, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.543', '2026-06-19 04:29:35.543', NULL, 1,1),
(54, 5, 54, 6, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.571', '2026-06-19 04:29:35.571', NULL, 1,1),
(55, 5, 55, 7, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.598', '2026-06-19 04:29:35.598', NULL, 1,1),
(56, 5, 56, 8, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.620', '2026-06-19 04:29:35.620', NULL, 1,1),
(57, 5, 57, 9, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.643', '2026-06-19 04:29:35.643', NULL, 1,1),
(58, 5, 58, 10, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.665', '2026-06-19 04:29:35.665', NULL, 1,1),
(59, 5, 59, 11, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.684', '2026-06-19 04:29:35.684', NULL, 1,1),
(60, 5, 60, 12, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.700', '2026-06-19 04:29:35.700', NULL, 1,0),
(61, 6, 61, 1, 'goalkeeper', 'captain', 'active', 'approved', 1, '2026-06-19 04:29:35.823', '2026-06-19 04:29:35.823', NULL, 1,1),
(62, 6, 62, 2, 'defender', 'vice_captain', 'active', 'approved', 1, '2026-06-19 04:29:35.841', '2026-06-19 04:29:35.841', NULL, 1,1),
(63, 6, 63, 3, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.866', '2026-06-19 04:29:35.866', NULL, 1,1),
(64, 6, 64, 4, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.892', '2026-06-19 04:29:35.892', NULL, 1,1),
(65, 6, 65, 5, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.928', '2026-06-19 04:29:35.928', NULL, 1,1),
(66, 6, 66, 6, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.957', '2026-06-19 04:29:35.957', NULL, 1,1),
(67, 6, 67, 7, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:35.982', '2026-06-19 04:29:35.982', NULL, 1,1),
(68, 6, 68, 8, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.011', '2026-06-19 04:29:36.011', NULL, 1,1),
(69, 6, 69, 9, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.029', '2026-06-19 04:29:36.029', NULL, 1,1),
(70, 6, 70, 10, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.045', '2026-06-19 04:29:36.045', NULL, 1,1),
(71, 6, 71, 11, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.063', '2026-06-19 04:29:36.063', NULL, 1,1),
(72, 6, 72, 12, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.082', '2026-06-19 04:29:36.082', NULL, 1,0),
(73, 7, 73, 1, 'goalkeeper', 'captain', 'active', 'approved', 1, '2026-06-19 04:29:36.198', '2026-06-19 04:29:36.198', NULL, 1,1),
(74, 7, 74, 2, 'defender', 'vice_captain', 'active', 'approved', 1, '2026-06-19 04:29:36.220', '2026-06-19 04:29:36.220', NULL, 1,1),
(75, 7, 75, 3, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.241', '2026-06-19 04:29:36.241', NULL, 1,1),
(76, 7, 76, 4, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.264', '2026-06-19 04:29:36.264', NULL, 1,1),
(77, 7, 77, 5, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.289', '2026-06-19 04:29:36.289', NULL, 1,1),
(78, 7, 78, 6, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.316', '2026-06-19 04:29:36.316', NULL, 1,1),
(79, 7, 79, 7, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.342', '2026-06-19 04:29:36.342', NULL, 1,1),
(80, 7, 80, 8, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.368', '2026-06-19 04:29:36.368', NULL, 1,1),
(81, 7, 81, 9, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.392', '2026-06-19 04:29:36.392', NULL, 1,1),
(82, 7, 82, 10, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.416', '2026-06-19 04:29:36.416', NULL, 1,1),
(83, 7, 83, 11, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.437', '2026-06-19 04:29:36.437', NULL, 1,1),
(84, 7, 84, 12, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.454', '2026-06-19 04:29:36.454', NULL, 1,0),
(85, 8, 85, 1, 'goalkeeper', 'captain', 'active', 'approved', 1, '2026-06-19 04:29:36.569', '2026-06-19 04:29:36.569', NULL, 1,1),
(86, 8, 86, 2, 'defender', 'vice_captain', 'active', 'approved', 1, '2026-06-19 04:29:36.586', '2026-06-19 04:29:36.586', NULL, 1,1),
(87, 8, 87, 3, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.603', '2026-06-19 04:29:36.603', NULL, 1,1),
(88, 8, 88, 4, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.620', '2026-06-19 04:29:36.620', NULL, 1,1),
(89, 8, 89, 5, 'defender', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.640', '2026-06-19 04:29:36.640', NULL, 1,1),
(90, 8, 90, 6, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.659', '2026-06-19 04:29:36.659', NULL, 1,1),
(91, 8, 91, 7, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.681', '2026-06-19 04:29:36.681', NULL, 1,1),
(92, 8, 92, 8, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.705', '2026-06-19 04:29:36.705', NULL, 1,1),
(93, 8, 93, 9, 'midfielder', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.733', '2026-06-19 04:29:36.733', NULL, 1,1),
(94, 8, 94, 10, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.761', '2026-06-19 04:29:36.761', NULL, 1,1),
(95, 8, 95, 11, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.786', '2026-06-19 04:29:36.786', NULL, 1,1),
(96, 8, 96, 12, 'forward', 'player', 'active', 'approved', 1, '2026-06-19 04:29:36.812', '2026-06-19 04:29:36.812', NULL, 1,0);

-- --------------------------------------------------------

--
-- Table structure for table `team_standings`
--

CREATE TABLE `team_standings` (
  `id` int NOT NULL,
  `team_id` int NOT NULL,
  `group_id` int NOT NULL,
  `position` int NOT NULL,
  `matches_played` int NOT NULL DEFAULT '0',
  `wins` int NOT NULL DEFAULT '0',
  `draws` int NOT NULL DEFAULT '0',
  `losses` int NOT NULL DEFAULT '0',
  `goals_for` int NOT NULL DEFAULT '0',
  `goals_against` int NOT NULL DEFAULT '0',
  `points` int NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `team_standings`
--

INSERT INTO `team_standings` (`id`, `team_id`, `group_id`, `position`, `matches_played`, `wins`, `draws`, `losses`, `goals_for`, `goals_against`, `points`, `is_active`, `created_at`, `updated_at`, `deleted_at`) VALUES
(9, 1, 3, 2, 6, 3, 1, 2, 8, 8, 10, 1, '2026-06-20 06:17:11.431', '2026-06-20 06:34:25.124', NULL),
(10, 2, 3, 4, 6, 2, 0, 4, 10, 12, 6, 1, '2026-06-20 06:17:11.441', '2026-06-20 06:34:25.139', NULL),
(11, 3, 3, 3, 6, 2, 1, 3, 9, 12, 7, 1, '2026-06-20 06:17:11.453', '2026-06-20 06:34:25.131', NULL),
(12, 4, 3, 1, 6, 3, 2, 1, 14, 9, 11, 1, '2026-06-20 06:17:11.462', '2026-06-20 06:34:25.116', NULL),
(13, 5, 4, 2, 3, 1, 1, 1, 5, 7, 4, 1, '2026-06-20 06:17:11.469', '2026-06-20 06:34:25.750', NULL),
(14, 6, 4, 4, 3, 1, 0, 2, 4, 6, 3, 1, '2026-06-20 06:17:11.475', '2026-06-20 06:34:25.763', NULL),
(15, 7, 4, 1, 3, 2, 0, 1, 7, 4, 6, 1, '2026-06-20 06:17:11.482', '2026-06-20 06:34:25.743', NULL),
(16, 8, 4, 3, 3, 1, 1, 1, 5, 4, 4, 1, '2026-06-20 06:17:11.489', '2026-06-20 06:34:25.757', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `tournaments`
--

CREATE TABLE `tournaments` (
  `id` int NOT NULL,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `logo` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `user_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `tournaments`
--

INSERT INTO `tournaments` (`id`, `name`, `description`, `logo`, `is_active`, `created_at`, `updated_at`, `deleted_at`, `user_id`) VALUES
(1, 'Victory Championship', 'Sân chơi cạnh tranh dành cho các đội tuyển hàng đầu.', 'https://res.cloudinary.com/dwczzcsxh/image/upload/v1781621083/tournaments/logo/202606/89989f33-3db3-48d6-9499-3cb0293f3c22.jpg', 1, '2026-06-16 14:44:44.148', '2026-06-16 14:44:44.148', NULL, 1),
(2, 'Giải Bóng Đá Thành Phố 2024', 'Giải bóng đá thường niên cấp thành phố', NULL, 1, '2026-06-19 04:29:32.605', '2026-06-19 04:29:32.605', NULL, 1);

-- --------------------------------------------------------

--
-- Table structure for table `tournament_rules`
--

CREATE TABLE `tournament_rules` (
  `id` int NOT NULL,
  `tournament_id` int NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `points_per_win` int NOT NULL DEFAULT '3',
  `points_per_draw` int NOT NULL DEFAULT '1',
  `points_per_loss` int NOT NULL DEFAULT '0',
  `forfeit_score` int NOT NULL DEFAULT '3',
  `yellow_cards_suspension` int NOT NULL DEFAULT '3',
  `max_players_per_team` int NOT NULL DEFAULT '11',
  `min_players_per_team` int NOT NULL DEFAULT '7',
  `teams_advance_per_group` int NOT NULL DEFAULT '2',
  `tiebreaker_order` json NOT NULL,
  `user_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `tournament_rules`
--

INSERT INTO `tournament_rules` (`id`, `tournament_id`, `is_active`, `created_at`, `updated_at`, `deleted_at`, `points_per_win`, `points_per_draw`, `points_per_loss`, `forfeit_score`, `yellow_cards_suspension`, `max_players_per_team`, `min_players_per_team`, `teams_advance_per_group`, `tiebreaker_order`, `user_id`) VALUES
(1, 1, 1, '2026-06-16 14:46:29.466', '2026-06-16 14:46:29.466', NULL, 3, 1, 0, 3, 3, 23, 11, 2, '[\"goal_diff\", \"goals_scored\", \"head_to_head\"]', 1),
(2, 2, 1, '2026-06-19 04:29:32.654', '2026-06-19 04:29:32.654', NULL, 3, 1, 0, 3, 3, 20, 7, 2, '[\"goal_diff\", \"goals_scored\", \"head_to_head\"]', 1);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int NOT NULL,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `email_verified` tinyint(1) NOT NULL DEFAULT '0',
  `email_verified_at` datetime(3) DEFAULT NULL,
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `fcm_token` TEXT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `password`, `phone`, `is_active`, `email_verified`, `email_verified_at`, `created_at`, `updated_at`) VALUES
(1, 'System Admin', 'admin@gmail.com', '$2b$12$vllbUdZfTl9hxTQWLhLwX.LDvtag.SgqiECe/tkXyj2fAVRHJbmVO', NULL, 1, 1, '2026-06-16 14:41:10.508', '2026-06-16 14:41:10.551', '2026-06-16 14:41:10.551'),
(2, 'Nguyễn Văn An', 'an.2021001@student.edu.vn', '$2b$12$2SHx/owyI0085SqrsaarrORB9NhYs9cX.TNv1G/fPqYO5Fkwu/0h2', '0901000001', 1, 1, '2026-06-16 14:41:11.022', '2026-06-16 14:41:11.031', '2026-06-16 14:41:11.031'),
(3, 'Trần Minh Khoa', 'khoa.2021002@student.edu.vn', '$2b$12$WYsHRynoEk463qF.ZApAHOnJCJUwFnC3jNmu4Sf/J8Dv3uwxvQHLe', '0901000002', 1, 1, '2026-06-16 14:41:11.373', '2026-06-16 14:41:11.376', '2026-06-16 14:41:11.376'),
(4, 'Lê Văn Trọng Tài', 'trongtai@football.local', '$2b$12$yMiG112juZa6NQdcCUzfqehh0IeUWD3HvHRqQdZnz5HCWvZw7q1Ya', NULL, 1, 1, '2026-06-16 14:41:11.919', '2026-06-16 14:41:11.921', '2026-06-16 14:41:11.921'),
(5, 'Phạm Thị Bình', 'binh.register@student.edu.vn', '$2b$12$oUN2emgbVkD3/Ef1uYIQ/uwOyzWBmJ7E0695rt3XU/pWaJQE1QHj6', '0901000005', 1, 0, NULL, '2026-06-16 14:41:12.363', '2026-06-16 14:41:12.363'),
(6, 'Leader FC Rồng Vàng', 'leader_team1@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:32.922', '2026-06-19 04:29:32.927', '2026-06-19 04:29:32.927'),
(7, 'Nguyễn Văn An', 'player_t1_p1@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:32.981', '2026-06-19 04:29:32.984', '2026-06-19 04:29:32.984'),
(8, 'Trần Thị Bình', 'player_t1_p2@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:33.082', '2026-06-19 04:29:33.085', '2026-06-19 04:29:33.085'),
(9, 'Lê Văn Cường', 'player_t1_p3@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:33.125', '2026-06-19 04:29:33.128', '2026-06-19 04:29:33.128'),
(10, 'Phạm Thị Dung', 'player_t1_p4@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:33.166', '2026-06-19 04:29:33.169', '2026-06-19 04:29:33.169'),
(11, 'Hoàng Văn Em', 'player_t1_p5@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:33.202', '2026-06-19 04:29:33.204', '2026-06-19 04:29:33.204'),
(12, 'Vũ Thị Phương', 'player_t1_p6@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:33.233', '2026-06-19 04:29:33.235', '2026-06-19 04:29:33.235'),
(13, 'Đặng Văn Giang', 'player_t1_p7@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:33.267', '2026-06-19 04:29:33.270', '2026-06-19 04:29:33.270'),
(14, 'Bùi Thị Hoa', 'player_t1_p8@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:33.303', '2026-06-19 04:29:33.306', '2026-06-19 04:29:33.306'),
(15, 'Đỗ Văn Inh', 'player_t1_p9@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:33.341', '2026-06-19 04:29:33.344', '2026-06-19 04:29:33.344'),
(16, 'Ngô Thị Kim', 'player_t1_p10@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:33.373', '2026-06-19 04:29:33.376', '2026-06-19 04:29:33.376'),
(17, 'Dương Văn Long', 'player_t1_p11@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:33.410', '2026-06-19 04:29:33.412', '2026-06-19 04:29:33.412'),
(18, 'Mai Thị Minh', 'player_t1_p12@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:33.445', '2026-06-19 04:29:33.448', '2026-06-19 04:29:33.448'),
(19, 'Leader CLB Sao Biển', 'leader_team2@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:33.760', '2026-06-19 04:29:33.762', '2026-06-19 04:29:33.762'),
(20, 'Lý Văn Năm', 'player_t2_p1@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:33.781', '2026-06-19 04:29:33.783', '2026-06-19 04:29:33.783'),
(21, 'Trương Thị Oanh', 'player_t2_p2@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:33.820', '2026-06-19 04:29:33.823', '2026-06-19 04:29:33.823'),
(22, 'Huỳnh Văn Phú', 'player_t2_p3@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:33.857', '2026-06-19 04:29:33.860', '2026-06-19 04:29:33.860'),
(23, 'Phan Thị Quỳnh', 'player_t2_p4@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:33.893', '2026-06-19 04:29:33.895', '2026-06-19 04:29:33.895'),
(24, 'Đinh Văn Rồng', 'player_t2_p5@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:33.924', '2026-06-19 04:29:33.926', '2026-06-19 04:29:33.926'),
(25, 'Cao Thị Sen', 'player_t2_p6@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:33.954', '2026-06-19 04:29:33.956', '2026-06-19 04:29:33.956'),
(26, 'Tô Văn Thắng', 'player_t2_p7@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:33.987', '2026-06-19 04:29:33.990', '2026-06-19 04:29:33.990'),
(27, 'Lưu Thị Uyên', 'player_t2_p8@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:34.019', '2026-06-19 04:29:34.022', '2026-06-19 04:29:34.022'),
(28, 'Kiều Văn Vinh', 'player_t2_p9@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:34.052', '2026-06-19 04:29:34.054', '2026-06-19 04:29:34.054'),
(29, 'Châu Thị Xuân', 'player_t2_p10@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:34.085', '2026-06-19 04:29:34.088', '2026-06-19 04:29:34.088'),
(30, 'Hồ Văn Yên', 'player_t2_p11@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:34.122', '2026-06-19 04:29:34.124', '2026-06-19 04:29:34.124'),
(31, 'Đào Thị Zung', 'player_t2_p12@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:34.154', '2026-06-19 04:29:34.156', '2026-06-19 04:29:34.156'),
(32, 'Leader FC Hùng Mạnh', 'leader_team3@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:34.372', '2026-06-19 04:29:34.374', '2026-06-19 04:29:34.374'),
(33, 'Mạc Văn Anh', 'player_t3_p1@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:34.391', '2026-06-19 04:29:34.393', '2026-06-19 04:29:34.393'),
(34, 'Từ Thị Bảo', 'player_t3_p2@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:34.424', '2026-06-19 04:29:34.427', '2026-06-19 04:29:34.427'),
(35, 'Tăng Văn Chi', 'player_t3_p3@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:34.462', '2026-06-19 04:29:34.465', '2026-06-19 04:29:34.465'),
(36, 'Văn Thị Diệu', 'player_t3_p4@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:34.497', '2026-06-19 04:29:34.500', '2026-06-19 04:29:34.500'),
(37, 'Nghiêm Văn Ến', 'player_t3_p5@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:34.529', '2026-06-19 04:29:34.531', '2026-06-19 04:29:34.531'),
(38, 'Sầm Thị Én', 'player_t3_p6@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:34.560', '2026-06-19 04:29:34.562', '2026-06-19 04:29:34.562'),
(39, 'Ông Văn Gà', 'player_t3_p7@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:34.594', '2026-06-19 04:29:34.596', '2026-06-19 04:29:34.596'),
(40, 'Hà Thị Hải', 'player_t3_p8@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:34.627', '2026-06-19 04:29:34.630', '2026-06-19 04:29:34.630'),
(41, 'Khúc Văn Ích', 'player_t3_p9@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:34.659', '2026-06-19 04:29:34.661', '2026-06-19 04:29:34.661'),
(42, 'Tề Thị Lan', 'player_t3_p10@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:34.693', '2026-06-19 04:29:34.695', '2026-06-19 04:29:34.695'),
(43, 'Lạc Văn Mạnh', 'player_t3_p11@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:34.720', '2026-06-19 04:29:34.722', '2026-06-19 04:29:34.722'),
(44, 'Tiêu Thị Nga', 'player_t3_p12@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:34.746', '2026-06-19 04:29:34.748', '2026-06-19 04:29:34.748'),
(45, 'Leader CLB Thần Tốc', 'leader_team4@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:34.928', '2026-06-19 04:29:34.930', '2026-06-19 04:29:34.930'),
(46, 'Mao Văn Ổn', 'player_t4_p1@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:34.947', '2026-06-19 04:29:34.949', '2026-06-19 04:29:34.949'),
(47, 'Hứa Thị Phi', 'player_t4_p2@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:34.980', '2026-06-19 04:29:34.982', '2026-06-19 04:29:34.982'),
(48, 'Vu Văn Quân', 'player_t4_p3@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.004', '2026-06-19 04:29:35.005', '2026-06-19 04:29:35.005'),
(49, 'Rong Thị Sang', 'player_t4_p4@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.026', '2026-06-19 04:29:35.027', '2026-06-19 04:29:35.027'),
(50, 'Vàng Văn Thịnh', 'player_t4_p5@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.047', '2026-06-19 04:29:35.049', '2026-06-19 04:29:35.049'),
(51, 'Ưu Thị Ung', 'player_t4_p6@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.069', '2026-06-19 04:29:35.071', '2026-06-19 04:29:35.071'),
(52, 'Xu Văn Vỹ', 'player_t4_p7@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.091', '2026-06-19 04:29:35.093', '2026-06-19 04:29:35.093'),
(53, 'Yên Thị Xuyên', 'player_t4_p8@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.117', '2026-06-19 04:29:35.119', '2026-06-19 04:29:35.119'),
(54, 'Zi Văn Ý', 'player_t4_p9@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.145', '2026-06-19 04:29:35.147', '2026-06-19 04:29:35.147'),
(55, 'An Thị Bé', 'player_t4_p10@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.175', '2026-06-19 04:29:35.178', '2026-06-19 04:29:35.178'),
(56, 'Ba Văn Cao', 'player_t4_p11@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.205', '2026-06-19 04:29:35.208', '2026-06-19 04:29:35.208'),
(57, 'Chị Thị Dạy', 'player_t4_p12@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.237', '2026-06-19 04:29:35.239', '2026-06-19 04:29:35.239'),
(58, 'Leader FC Bất Bại', 'leader_team5@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.394', '2026-06-19 04:29:35.395', '2026-06-19 04:29:35.395'),
(59, 'Em Văn Phú', 'player_t5_p1@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.407', '2026-06-19 04:29:35.409', '2026-06-19 04:29:35.409'),
(60, 'Kì Thị Lạ', 'player_t5_p2@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.435', '2026-06-19 04:29:35.437', '2026-06-19 04:29:35.437'),
(61, 'Mới Văn Nhỏ', 'player_t5_p3@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.465', '2026-06-19 04:29:35.467', '2026-06-19 04:29:35.467'),
(62, 'Nhất Thị Ổn', 'player_t5_p4@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.493', '2026-06-19 04:29:35.495', '2026-06-19 04:29:35.495'),
(63, 'Phúc Văn Quý', 'player_t5_p5@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.521', '2026-06-19 04:29:35.523', '2026-06-19 04:29:35.523'),
(64, 'Rõ Thị Sáng', 'player_t5_p6@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.549', '2026-06-19 04:29:35.551', '2026-06-19 04:29:35.551'),
(65, 'Tốt Văn Uy', 'player_t5_p7@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.577', '2026-06-19 04:29:35.579', '2026-06-19 04:29:35.579'),
(66, 'Vui Thị Xanh', 'player_t5_p8@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.603', '2026-06-19 04:29:35.605', '2026-06-19 04:29:35.605'),
(67, 'Yêu Văn Zin', 'player_t5_p9@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.625', '2026-06-19 04:29:35.627', '2026-06-19 04:29:35.627'),
(68, 'Ân Thị Bình', 'player_t5_p10@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.648', '2026-06-19 04:29:35.650', '2026-06-19 04:29:35.650'),
(69, 'Bất Văn Chiến', 'player_t5_p11@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.669', '2026-06-19 04:29:35.671', '2026-06-19 04:29:35.671'),
(70, 'Đạt Thị Đẹp', 'player_t5_p12@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.688', '2026-06-19 04:29:35.689', '2026-06-19 04:29:35.689'),
(71, 'Leader CLB Chiến Thắng', 'leader_team6@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.790', '2026-06-19 04:29:35.792', '2026-06-19 04:29:35.792'),
(72, 'Hiền Văn Giỏi', 'player_t6_p1@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.803', '2026-06-19 04:29:35.805', '2026-06-19 04:29:35.805'),
(73, 'Khỏe Thị Lành', 'player_t6_p2@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.826', '2026-06-19 04:29:35.828', '2026-06-19 04:29:35.828'),
(74, 'Mạnh Văn Ngay', 'player_t6_p3@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.845', '2026-06-19 04:29:35.847', '2026-06-19 04:29:35.847'),
(75, 'Nhân Thị Ổn', 'player_t6_p4@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.871', '2026-06-19 04:29:35.874', '2026-06-19 04:29:35.874'),
(76, 'Phước Văn Quang', 'player_t6_p5@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.897', '2026-06-19 04:29:35.899', '2026-06-19 04:29:35.899'),
(77, 'Rực Thị Sáng', 'player_t6_p6@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.933', '2026-06-19 04:29:35.935', '2026-06-19 04:29:35.935'),
(78, 'Tài Văn Uy', 'player_t6_p7@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.962', '2026-06-19 04:29:35.964', '2026-06-19 04:29:35.964'),
(79, 'Vững Thị Xây', 'player_t6_p8@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:35.987', '2026-06-19 04:29:35.990', '2026-06-19 04:29:35.990'),
(80, 'Yên Văn Trong', 'player_t6_p9@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.015', '2026-06-19 04:29:36.017', '2026-06-19 04:29:36.017'),
(81, 'Ổn Thị Bình', 'player_t6_p10@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.032', '2026-06-19 04:29:36.033', '2026-06-19 04:29:36.033'),
(82, 'Bền Văn Chí', 'player_t6_p11@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.049', '2026-06-19 04:29:36.050', '2026-06-19 04:29:36.050'),
(83, 'Đức Thị Độ', 'player_t6_p12@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.067', '2026-06-19 04:29:36.069', '2026-06-19 04:29:36.069'),
(84, 'Leader FC Bão Lửa', 'leader_team7@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.164', '2026-06-19 04:29:36.166', '2026-06-19 04:29:36.166'),
(85, 'Hòa Văn Giải', 'player_t7_p1@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.179', '2026-06-19 04:29:36.181', '2026-06-19 04:29:36.181'),
(86, 'Kiên Thị Lực', 'player_t7_p2@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.202', '2026-06-19 04:29:36.204', '2026-06-19 04:29:36.204'),
(87, 'Minh Văn Nhanh', 'player_t7_p3@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.224', '2026-06-19 04:29:36.225', '2026-06-19 04:29:36.225'),
(88, 'Nhờ Thị Ở', 'player_t7_p4@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.245', '2026-06-19 04:29:36.247', '2026-06-19 04:29:36.247'),
(89, 'Phúc Văn Quý', 'player_t7_p5@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.269', '2026-06-19 04:29:36.271', '2026-06-19 04:29:36.271'),
(90, 'Rộng Thị Sạch', 'player_t7_p6@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.294', '2026-06-19 04:29:36.296', '2026-06-19 04:29:36.296'),
(91, 'Thắng Văn Uy', 'player_t7_p7@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.321', '2026-06-19 04:29:36.323', '2026-06-19 04:29:36.323'),
(92, 'Vẻ Thị Xinh', 'player_t7_p8@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.347', '2026-06-19 04:29:36.350', '2026-06-19 04:29:36.350'),
(93, 'Yêu Văn Zay', 'player_t7_p9@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.373', '2026-06-19 04:29:36.375', '2026-06-19 04:29:36.375'),
(94, 'Ân Thị Ban', 'player_t7_p10@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.397', '2026-06-19 04:29:36.399', '2026-06-19 04:29:36.399'),
(95, 'Bền Văn Cao', 'player_t7_p11@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.420', '2026-06-19 04:29:36.421', '2026-06-19 04:29:36.421'),
(96, 'Chín Thị Dồi', 'player_t7_p12@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.441', '2026-06-19 04:29:36.442', '2026-06-19 04:29:36.442'),
(97, 'Leader CLB Thiên Lôi', 'leader_team8@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.538', '2026-06-19 04:29:36.539', '2026-06-19 04:29:36.539'),
(98, 'Em Văn Phúc', 'player_t8_p1@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.549', '2026-06-19 04:29:36.551', '2026-06-19 04:29:36.551'),
(99, 'Kha Thị Lắm', 'player_t8_p2@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.572', '2026-06-19 04:29:36.574', '2026-06-19 04:29:36.574'),
(100, 'Mau Văn Nhớ', 'player_t8_p3@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.589', '2026-06-19 04:29:36.591', '2026-06-19 04:29:36.591'),
(101, 'Nhớ Thị Ổn', 'player_t8_p4@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.607', '2026-06-19 04:29:36.608', '2026-06-19 04:29:36.608'),
(102, 'Phú Văn Quý', 'player_t8_p5@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.624', '2026-06-19 04:29:36.625', '2026-06-19 04:29:36.625'),
(103, 'Rợp Thị Sóng', 'player_t8_p6@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.644', '2026-06-19 04:29:36.646', '2026-06-19 04:29:36.646'),
(104, 'Tốt Văn Uy', 'player_t8_p7@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.664', '2026-06-19 04:29:36.666', '2026-06-19 04:29:36.666'),
(105, 'Vui Thị Xuân', 'player_t8_p8@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.685', '2026-06-19 04:29:36.688', '2026-06-19 04:29:36.688'),
(106, 'Yên Văn Dạy', 'player_t8_p9@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.710', '2026-06-19 04:29:36.712', '2026-06-19 04:29:36.712'),
(107, 'Ổn Thị Bền', 'player_t8_p10@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.739', '2026-06-19 04:29:36.742', '2026-06-19 04:29:36.742'),
(108, 'Bất Văn Chi', 'player_t8_p11@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.766', '2026-06-19 04:29:36.768', '2026-06-19 04:29:36.768'),
(109, 'Đại Thị Đồng', 'player_t8_p12@seed.local', '$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C', NULL, 1, 1, '2026-06-19 04:29:36.791', '2026-06-19 04:29:36.794', '2026-06-19 04:29:36.794');

-- --------------------------------------------------------

--
-- Table structure for table `user_role`
--

CREATE TABLE `user_role` (
  `user_id` int NOT NULL,
  `role_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `user_role`
--

INSERT INTO `user_role` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 2),
(2, 3),
(3, 3),
(4, 4),
(5, 5);

-- --------------------------------------------------------

--
-- Table structure for table `venues`
--

CREATE TABLE `venues` (
  `id` int NOT NULL,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `address` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `venues`
--

INSERT INTO `venues` (`id`, `name`, `address`, `is_active`, `created_at`, `updated_at`, `deleted_at`) VALUES
(1, 'Sân bóng An Phú', 'Địa chỉ 1, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(2, 'Sân bóng Bình Minh', 'Địa chỉ 2, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(3, 'Sân bóng Thành Công', 'Địa chỉ 3, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(4, 'Sân bóng Hoàng Gia', 'Địa chỉ 4, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(5, 'Sân bóng Phú Mỹ', 'Địa chỉ 5, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(6, 'Sân bóng Thanh Niên', 'Địa chỉ 6, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(7, 'Sân bóng Đông Á', 'Địa chỉ 7, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(8, 'Sân bóng Tây Đô', 'Địa chỉ 8, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(9, 'Sân bóng Đại Nam', 'Địa chỉ 9, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(10, 'Sân bóng Hòa Bình', 'Địa chỉ 10, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(11, 'Sân bóng Victory', 'Địa chỉ 11, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(12, 'Sân bóng Galaxy', 'Địa chỉ 12, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(13, 'Sân bóng Dragon', 'Địa chỉ 13, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(14, 'Sân bóng Green Field', 'Địa chỉ 14, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(15, 'Sân bóng Golden Star', 'Địa chỉ 15, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(16, 'Sân bóng Tân Sơn', 'Địa chỉ 16, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(17, 'Sân bóng Long Thành', 'Địa chỉ 17, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(18, 'Sân bóng Thống Nhất', 'Địa chỉ 18, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(19, 'Sân bóng Minh Châu', 'Địa chỉ 19, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(20, 'Sân bóng Kim Cương', 'Địa chỉ 20, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(21, 'Sân bóng Blue Sky', 'Địa chỉ 21, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(22, 'Sân bóng Red Star', 'Địa chỉ 22, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(23, 'Sân bóng Future', 'Địa chỉ 23, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(24, 'Sân bóng Champions', 'Địa chỉ 24, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(25, 'Sân bóng Elite', 'Địa chỉ 25, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(26, 'Sân bóng City Sport', 'Địa chỉ 26, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(27, 'Sân bóng River Park', 'Địa chỉ 27, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(28, 'Sân bóng Central', 'Địa chỉ 28, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(29, 'Sân bóng Arena', 'Địa chỉ 29, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL),
(30, 'Sân bóng Pro League', 'Địa chỉ 30, TP.HCM', 1, '2026-06-16 14:41:12.386', '2026-06-16 14:41:12.386', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `_prisma_migrations`
--

CREATE TABLE `_prisma_migrations` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `checksum` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `finished_at` datetime(3) DEFAULT NULL,
  `migration_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `logs` text COLLATE utf8mb4_unicode_ci,
  `rolled_back_at` datetime(3) DEFAULT NULL,
  `started_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `applied_steps_count` int UNSIGNED NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `_prisma_migrations`
--

INSERT INTO `_prisma_migrations` (`id`, `checksum`, `finished_at`, `migration_name`, `logs`, `rolled_back_at`, `started_at`, `applied_steps_count`) VALUES
('06752b1d-ca08-419a-9e28-22a86b7045f1', '944e73d2dd2a26dada51c147e6881e3670156dc0b488ce576b76bd4f8b72c23f', '2026-06-16 08:48:43.270', '20260605054856_create_user_table', NULL, NULL, '2026-06-16 08:48:43.187', 1),
('06d156b5-e1c1-44b7-af6f-ea050759de56', '921836da5ebcc0ffd20b1f38eee7a812d1e5a533ff8e0a37526bce571fac86f5', '2026-06-16 08:49:21.550', '20260616084920_rename_seasonrule_change_to_tournamentrule_table', NULL, NULL, '2026-06-16 08:49:21.034', 1),
('0f3f4dc6-58d2-4172-9c0d-968e3762aefc', 'c2328034cda33ec19e788769cc119ec6aef59b522d81ed970700dbe8cc682aa9', '2026-06-18 01:45:18.063', '20260618014517_add_fields_tournament_rule_table', NULL, NULL, '2026-06-18 01:45:17.761', 1),
('16456d56-c669-4e41-9b4c-747b4c71212a', '75a641759323dd8b5b2eba86116fbff7aeaceb5d5027d31cb6e1d109090feffe', '2026-06-16 08:48:49.568', '20260607104917_add_all_table', NULL, NULL, '2026-06-16 08:48:44.392', 1),
('1fc279e6-9e40-46fe-9eb6-b693561bd3e7', '35b6de4e85126da5518022901069cf44484ae114f6fb16eb05375c774013edca', '2026-06-21 11:01:45.804', '20260621110144_create_bracketslot_table', NULL, NULL, '2026-06-21 11:01:44.780', 1),
('24b3cbf1-2ec3-40ca-81c8-36a3f0ed3c43', '9fc922eedd06c59adfe4f755ce07410520c0c5d91b76b3c14f019b7180368c35', '2026-06-16 08:48:49.848', '20260616041416_add_unique_name_tournament_table', NULL, NULL, '2026-06-16 08:48:49.758', 1),
('25f7349c-0cb9-4eba-bf4b-ca8b63978561', '251eec0d142dda9eb570ffd4b81de9477405632932f3613e10c19d7cd5943e15', '2026-06-19 09:35:32.566', '20260619093532_remove_seasonteamplayer_table', NULL, NULL, '2026-06-19 09:35:32.494', 1),
('39771eb2-2e1c-40b7-bf1b-1c7c742c1d62', '94b3dc760b43eb542bc62ed173bb740ae18721075e2c5640051553f8ea8fe678', '2026-06-19 06:12:31.978', '20260619061231_remove_groupteam_table', NULL, NULL, '2026-06-19 06:12:31.933', 1),
('44a7017a-0082-4502-bf63-fffc13d26242', '996b32f1d38039627e6b55e6fbfac851958ddded876305828edcc73a005bc79c', '2026-06-16 08:48:43.849', '20260605061531_rename_user_role_table', NULL, NULL, '2026-06-16 08:48:43.492', 1),
('49f1e0d2-4f58-42a3-a553-0b9612d43926', '49e913f40107e057814a3a818bda6b9e5527013eb992384289853ee81ae14541', '2026-06-17 01:32:12.548', '20260617013212_remove_is_deleted_all_table', NULL, NULL, '2026-06-17 01:32:12.137', 1),
('57506bd2-0688-403c-a88f-ed129a7fe109', 'c0d5e015f3f2b55971ca4aed5a7be7c9e6557e5868f37e76dcb169953ceeebc4', '2026-06-16 09:31:43.916', '20260616093143_remove_by_on_tournament_rule_table', NULL, NULL, '2026-06-16 09:31:43.876', 1),
('5928db05-def3-42c7-a55f-b4df465f4627', '09f00b949ab5795fea8cae4261dbcae944c0e425e77f5274ee45fde886f0626b', '2026-06-16 08:48:43.489', '20260605061433_create_role_table', NULL, NULL, '2026-06-16 08:48:43.274', 1),
('5a2dd335-3225-492d-9ccc-bb5b9ce316e1', 'f4a2a5434aff11350791b6fe655684d9488f5bac893172e99e840350f166ea86', '2026-06-19 07:14:38.569', '20260619071438_add_phase_status', NULL, NULL, '2026-06-19 07:14:38.511', 1),
('5ab653d7-1e5f-4b20-84d6-a36b9d1e9885', 'cf00100f3fbb5ccc0bd1691fe8a1e233746c0f73d9453ec7c77f96d8187e5b72', '2026-06-19 04:33:30.801', '20260619043330_fix_all_table', NULL, NULL, '2026-06-19 04:33:30.720', 1),
('7478de7c-417d-464e-87c5-bc44902d6920', '1518572a6671221e400b33dfab4293f5e5b7f019d5d3a5506b11716e81bfc2c3', '2026-06-16 08:48:49.642', '20260608080920_remove_token_table', NULL, NULL, '2026-06-16 08:48:49.571', 1),
('84ab914d-3999-4bbd-bcc3-92a18a461f9d', 'd019742b44d0b448cc5e596a34599cb53b151773d3f5c770472ee3daa95acedb', '2026-06-16 08:48:44.030', '20260605061704_remove_field_user_role_table', NULL, NULL, '2026-06-16 08:48:43.852', 1),
('a386a579-3307-4d5d-92ea-bcc1afad2291', 'c5735449e33b33bc32f32861cd7c6a37dc2e6cdd6e7fd67a480458740305791c', '2026-06-20 06:04:35.450', '20260620060435_update_enum_group_table', NULL, NULL, '2026-06-20 06:04:35.187', 1),
('b199fa9d-437d-4310-a5a4-1b66136c3504', '938ba9b7751838900d1b66d90b72ecdd3f7724bda6d9784f8e1f522b3e0c931b', '2026-06-19 05:14:08.126', '20260619051406_add_seasonstatus_enum', NULL, NULL, '2026-06-19 05:14:06.801', 1),
('cc2d46a8-9b74-47dd-ba95-7c8530c1a33f', '598d1b1fd1466c0a5d1437181ef257432852ccb311e37b2ad13ec3a467d55a9a', '2026-06-16 09:57:47.700', '20260616095747_remove_is_deleted_season_table', NULL, NULL, '2026-06-16 09:57:47.618', 1),
('d21f3540-5f65-45d0-a6bf-36c0a64c8389', '4eef50fe2f12e124e94a9472641d8b4fcc0ededff9caeebc41a18dbe5569f178', '2026-06-19 11:07:16.890', '20260619110714_add_table_articlemedia_table', NULL, NULL, '2026-06-19 11:07:14.963', 1),
('d257d25d-bd8c-4fbc-9a66-df5cdf5a9919', 'e41b445afe04017d2f977daafb1f6aee10d799bfff083aa34f510d1302a221b7', '2026-06-16 08:48:49.754', '20260615105408_remove_max_team_tournament_table', NULL, NULL, '2026-06-16 08:48:49.645', 1),
('e941b201-631e-48dc-8e6b-23bf211c1a88', '1d0fa69d2d89f27c522fe4e79dee96730a5d8d2a25518afec6eb9e482180289b', '2026-06-16 08:48:44.388', '20260605073549_add_unique_name_role_table', NULL, NULL, '2026-06-16 08:48:44.164', 1),
('e9ed8e23-20cd-4536-8ad7-507089fcb70f', '459f00012bd926a1b7e871d67f6d9c11392b3bcf4f54fd81c6cabe11b861b743', '2026-06-16 08:48:50.150', '20260616065831_option_date_season_table', NULL, NULL, '2026-06-16 08:48:49.851', 1),
('f9df31e7-3072-4948-8b76-8fa19b899d69', '1536fec53abee4e5a7c7524b91adad10bbc402f9088b821521d90f4474c0c648', '2026-06-16 08:48:44.159', '20260605061805_remove_field_two_table', NULL, NULL, '2026-06-16 08:48:44.034', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `articles`
--
ALTER TABLE `articles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `articles_slug_key` (`slug`),
  ADD KEY `articles_user_id_fkey` (`user_id`),
  ADD KEY `articles_season_id_fkey` (`season_id`),
  ADD KEY `articles_match_id_fkey` (`match_id`),
  ADD KEY `articles_team_id_fkey` (`team_id`);

--
-- Indexes for table `article_media`
--
ALTER TABLE `article_media`
  ADD PRIMARY KEY (`id`),
  ADD KEY `article_media_article_id_fkey` (`article_id`);

--
-- Indexes for table `article_tags`
--
ALTER TABLE `article_tags`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `article_tags_article_id_tag_key` (`article_id`,`tag`);

--
-- Indexes for table `bracket_slots`
--
ALTER TABLE `bracket_slots`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `bracket_slots_phase_id_round_slot_number_key` (`phase_id`,`round`,`slot_number`),
  ADD UNIQUE KEY `bracket_slots_match_id_key` (`match_id`),
  ADD KEY `bracket_slots_source_a_slot_id_fkey` (`source_a_slot_id`),
  ADD KEY `bracket_slots_source_b_slot_id_fkey` (`source_b_slot_id`);

--
-- Indexes for table `groups`
--
ALTER TABLE `groups`
  ADD PRIMARY KEY (`id`),
  ADD KEY `groups_phase_id_fkey` (`phase_id`);

--
-- Indexes for table `matches`
--
ALTER TABLE `matches`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `matches_venue_id_scheduled_at_key` (`venue_id`,`scheduled_at`),
  ADD KEY `matches_phase_id_fkey` (`phase_id`),
  ADD KEY `matches_group_id_fkey` (`group_id`),
  ADD KEY `matches_home_team_id_fkey` (`home_team_id`),
  ADD KEY `matches_away_team_id_fkey` (`away_team_id`),
  ADD KEY `matches_user_id_fkey` (`user_id`),
  ADD KEY `matches_season_id_fkey` (`season_id`);

--
-- Indexes for table `match_events`
--
ALTER TABLE `match_events`
  ADD PRIMARY KEY (`id`),
  ADD KEY `match_events_match_id_fkey` (`match_id`);

--
-- Indexes for table `match_results`
--
ALTER TABLE `match_results`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `match_results_match_id_key` (`match_id`),
  ADD KEY `match_results_winner_team_id_fkey` (`winner_team_id`);

--
-- Indexes for table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`id`),
  ADD KEY `notifications_season_id_fkey` (`season_id`),
  ADD KEY `notifications_target_team_id_fkey` (`target_team_id`);

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `payments_season_team_id_fkey` (`season_team_id`);

--
-- Indexes for table `phases`
--
ALTER TABLE `phases`
  ADD PRIMARY KEY (`id`),
  ADD KEY `phases_season_id_fkey` (`season_id`);

--
-- Indexes for table `players`
--
ALTER TABLE `players`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `players_user_id_key` (`user_id`);

--
-- Indexes for table `player_statistics`
--
ALTER TABLE `player_statistics`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `player_statistics_player_id_team_id_season_id_key` (`player_id`,`team_id`,`season_id`),
  ADD KEY `player_statistics_team_id_fkey` (`team_id`),
  ADD KEY `player_statistics_season_id_fkey` (`season_id`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `roles_name_key` (`name`);

--
-- Indexes for table `seasons`
--
ALTER TABLE `seasons`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `seasons_name_key` (`name`),
  ADD KEY `seasons_tournament_id_fkey` (`tournament_id`),
  ADD KEY `seasons_user_id_fkey` (`user_id`);

--
-- Indexes for table `season_teams`
--
ALTER TABLE `season_teams`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `season_teams_season_id_team_id_key` (`season_id`,`team_id`),
  ADD KEY `season_teams_team_id_fkey` (`team_id`),
  ADD KEY `season_teams_group_id_fkey` (`group_id`),
  ADD KEY `season_teams_user_id_fkey` (`user_id`);

--
-- Indexes for table `teams`
--
ALTER TABLE `teams`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `teams_name_key` (`name`),
  ADD KEY `teams_user_id_fkey` (`user_id`);

--
-- Indexes for table `team_leaders`
--
ALTER TABLE `team_leaders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `team_leaders_team_id_fkey` (`team_id`),
  ADD KEY `team_leaders_user_id_fkey` (`user_id`);

--
-- Indexes for table `team_players`
--
ALTER TABLE `team_players`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `team_players_team_id_jersey_number_key` (`team_id`,`jersey_number`),
  ADD UNIQUE KEY `team_players_team_id_player_id_key` (`team_id`,`player_id`),
  ADD KEY `team_players_player_id_fkey` (`player_id`),
  ADD KEY `team_players_user_id_fkey` (`user_id`);

--
-- Indexes for table `team_standings`
--
ALTER TABLE `team_standings`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `team_standings_group_id_team_id_key` (`group_id`,`team_id`),
  ADD KEY `team_standings_team_id_fkey` (`team_id`);

--
-- Indexes for table `tournaments`
--
ALTER TABLE `tournaments`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `tournaments_name_key` (`name`),
  ADD KEY `tournaments_user_id_fkey` (`user_id`);

--
-- Indexes for table `tournament_rules`
--
ALTER TABLE `tournament_rules`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `tournament_rules_tournament_id_key` (`tournament_id`),
  ADD KEY `tournament_rules_user_id_fkey` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `users_email_key` (`email`);

--
-- Indexes for table `user_role`
--
ALTER TABLE `user_role`
  ADD PRIMARY KEY (`user_id`,`role_id`),
  ADD KEY `User_Role_role_id_fkey` (`role_id`);

--
-- Indexes for table `venues`
--
ALTER TABLE `venues`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `venues_name_key` (`name`);

--
-- Indexes for table `_prisma_migrations`
--
ALTER TABLE `_prisma_migrations`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `articles`
--
ALTER TABLE `articles`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `article_media`
--
ALTER TABLE `article_media`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `article_tags`
--
ALTER TABLE `article_tags`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `bracket_slots`
--
ALTER TABLE `bracket_slots`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `groups`
--
ALTER TABLE `groups`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `matches`
--
ALTER TABLE `matches`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=62;

--
-- AUTO_INCREMENT for table `match_events`
--
ALTER TABLE `match_events`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=141;

--
-- AUTO_INCREMENT for table `match_results`
--
ALTER TABLE `match_results`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT for table `notifications`
--
ALTER TABLE `notifications`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `phases`
--
ALTER TABLE `phases`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `players`
--
ALTER TABLE `players`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=97;

--
-- AUTO_INCREMENT for table `player_statistics`
--
ALTER TABLE `player_statistics`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=76;

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `seasons`
--
ALTER TABLE `seasons`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `season_teams`
--
ALTER TABLE `season_teams`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=161;

--
-- AUTO_INCREMENT for table `teams`
--
ALTER TABLE `teams`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `team_leaders`
--
ALTER TABLE `team_leaders`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `team_players`
--
ALTER TABLE `team_players`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=97;

--
-- AUTO_INCREMENT for table `team_standings`
--
ALTER TABLE `team_standings`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `tournaments`
--
ALTER TABLE `tournaments`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `tournament_rules`
--
ALTER TABLE `tournament_rules`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=110;

--
-- AUTO_INCREMENT for table `venues`
--
ALTER TABLE `venues`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=63;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `articles`
--
ALTER TABLE `articles`
  ADD CONSTRAINT `articles_match_id_fkey` FOREIGN KEY (`match_id`) REFERENCES `matches` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `articles_season_id_fkey` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `articles_team_id_fkey` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `articles_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `article_media`
--
ALTER TABLE `article_media`
  ADD CONSTRAINT `article_media_article_id_fkey` FOREIGN KEY (`article_id`) REFERENCES `articles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `article_tags`
--
ALTER TABLE `article_tags`
  ADD CONSTRAINT `article_tags_article_id_fkey` FOREIGN KEY (`article_id`) REFERENCES `articles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `bracket_slots`
--
ALTER TABLE `bracket_slots`
  ADD CONSTRAINT `bracket_slots_match_id_fkey` FOREIGN KEY (`match_id`) REFERENCES `matches` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `bracket_slots_phase_id_fkey` FOREIGN KEY (`phase_id`) REFERENCES `phases` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `bracket_slots_source_a_slot_id_fkey` FOREIGN KEY (`source_a_slot_id`) REFERENCES `bracket_slots` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `bracket_slots_source_b_slot_id_fkey` FOREIGN KEY (`source_b_slot_id`) REFERENCES `bracket_slots` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `groups`
--
ALTER TABLE `groups`
  ADD CONSTRAINT `groups_phase_id_fkey` FOREIGN KEY (`phase_id`) REFERENCES `phases` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `matches`
--
ALTER TABLE `matches`
  ADD CONSTRAINT `matches_away_team_id_fkey` FOREIGN KEY (`away_team_id`) REFERENCES `teams` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `matches_group_id_fkey` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `matches_home_team_id_fkey` FOREIGN KEY (`home_team_id`) REFERENCES `teams` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `matches_phase_id_fkey` FOREIGN KEY (`phase_id`) REFERENCES `phases` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `matches_season_id_fkey` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `matches_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `matches_venue_id_fkey` FOREIGN KEY (`venue_id`) REFERENCES `venues` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `match_events`
--
ALTER TABLE `match_events`
  ADD CONSTRAINT `match_events_match_id_fkey` FOREIGN KEY (`match_id`) REFERENCES `matches` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `match_results`
--
ALTER TABLE `match_results`
  ADD CONSTRAINT `match_results_match_id_fkey` FOREIGN KEY (`match_id`) REFERENCES `matches` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `match_results_winner_team_id_fkey` FOREIGN KEY (`winner_team_id`) REFERENCES `teams` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `notifications_season_id_fkey` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `notifications_target_team_id_fkey` FOREIGN KEY (`target_team_id`) REFERENCES `teams` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `payments`
--
ALTER TABLE `payments`
  ADD CONSTRAINT `payments_season_team_id_fkey` FOREIGN KEY (`season_team_id`) REFERENCES `season_teams` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `phases`
--
ALTER TABLE `phases`
  ADD CONSTRAINT `phases_season_id_fkey` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `players`
--
ALTER TABLE `players`
  ADD CONSTRAINT `players_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `player_statistics`
--
ALTER TABLE `player_statistics`
  ADD CONSTRAINT `player_statistics_player_id_fkey` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `player_statistics_season_id_fkey` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `player_statistics_team_id_fkey` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `seasons`
--
ALTER TABLE `seasons`
  ADD CONSTRAINT `seasons_tournament_id_fkey` FOREIGN KEY (`tournament_id`) REFERENCES `tournaments` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `seasons_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `season_teams`
--
ALTER TABLE `season_teams`
  ADD CONSTRAINT `season_teams_group_id_fkey` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `season_teams_season_id_fkey` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `season_teams_team_id_fkey` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `season_teams_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `teams`
--
ALTER TABLE `teams`
  ADD CONSTRAINT `teams_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `team_leaders`
--
ALTER TABLE `team_leaders`
  ADD CONSTRAINT `team_leaders_team_id_fkey` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `team_leaders_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `team_players`
--
ALTER TABLE `team_players`
  ADD CONSTRAINT `team_players_player_id_fkey` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `team_players_team_id_fkey` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `team_players_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `team_standings`
--
ALTER TABLE `team_standings`
  ADD CONSTRAINT `team_standings_group_id_fkey` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `team_standings_team_id_fkey` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tournaments`
--
ALTER TABLE `tournaments`
  ADD CONSTRAINT `tournaments_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `tournament_rules`
--
ALTER TABLE `tournament_rules`
  ADD CONSTRAINT `tournament_rules_tournament_id_fkey` FOREIGN KEY (`tournament_id`) REFERENCES `tournaments` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tournament_rules_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `user_role`
--
ALTER TABLE `user_role`
  ADD CONSTRAINT `User_Role_role_id_fkey` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `User_Role_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
