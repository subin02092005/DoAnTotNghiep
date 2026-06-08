-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jun 08, 2026 at 08:13 AM
-- Server version: 8.0.30
-- PHP Version: 8.4.21

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
-- Table structure for table `groups`
--

CREATE TABLE `groups` (
  `id` int NOT NULL,
  `phase_id` int NOT NULL,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `group_teams`
--

CREATE TABLE `group_teams` (
  `group_id` int NOT NULL,
  `team_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
  `status` enum('scheduled','ongoing','finished','cancelled','forfeited') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'scheduled',
  `round` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `leg` int DEFAULT NULL,
  `next_match_id` int DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `user_id` int DEFAULT NULL,
  `venue_id` int DEFAULT NULL,
  `is_published` tinyint(1) NOT NULL DEFAULT '0',
  `referee` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `season_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `match_events`
--

CREATE TABLE `match_events` (
  `id` int NOT NULL,
  `match_id` int NOT NULL,
  `player_id` int DEFAULT NULL,
  `team_id` int DEFAULT NULL,
  `type` enum('goal','own_goal','yellow_card','red_card','second_yellow','substitution_in','substitution_out','penalty_scored','penalty_missed') COLLATE utf8mb4_unicode_ci NOT NULL,
  `minute` int DEFAULT NULL,
  `note` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `period` enum('first_half','second_half','extra_time_first','extra_time_second','penalty_shootout') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `added_minute` int DEFAULT NULL,
  `card_color` enum('yellow','red') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sub_out_player_id` int DEFAULT NULL,
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
  `status` enum('official','protested','overturned') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'official',
  `duration` int DEFAULT NULL,
  `notes` text COLLATE utf8mb4_unicode_ci,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

CREATE TABLE `notifications` (
  `id` int NOT NULL,
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
  `deleted_at` datetime(3) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `oauth_accounts`
--

CREATE TABLE `oauth_accounts` (
  `id` int NOT NULL,
  `user_id` int NOT NULL,
  `provider` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `provider_user_id` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email_verified` tinyint(1) NOT NULL DEFAULT '0',
  `avatar_url` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `access_token` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `token_expires_at` datetime(3) DEFAULT NULL,
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `passkey_credentials`
--

CREATE TABLE `passkey_credentials` (
  `id` int NOT NULL,
  `user_id` int NOT NULL,
  `device_id` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `credential_id` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `public_key` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `sign_count` bigint NOT NULL DEFAULT '0',
  `aaguid` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_backup_eligible` tinyint(1) NOT NULL DEFAULT '0',
  `is_backed_up` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL
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
  `deleted_at` datetime(3) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
  `updated_at` datetime(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `user_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
(1, 'admin', 'Quản trị hệ thống', 1, '2026-06-05 10:05:54.506', '2026-06-05 10:05:54.506'),
(2, 'leader', 'Trưởng đội / đại diện đội bóng', 1, '2026-06-05 10:05:54.544', '2026-06-05 10:05:54.544'),
(3, 'player', 'Cầu thủ đã claim tài khoản', 1, '2026-06-05 10:05:54.559', '2026-06-05 10:05:54.559'),
(4, 'referee', 'Trọng tài', 1, '2026-06-05 10:05:54.573', '2026-06-05 10:05:54.573'),
(5, 'user', 'Tài khoản mặc định khi đăng ký', 1, '2026-06-05 10:05:54.586', '2026-06-05 10:05:54.586');

-- --------------------------------------------------------

--
-- Table structure for table `seasons`
--

CREATE TABLE `seasons` (
  `id` int NOT NULL,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` enum('upcoming','registration_open','ongoing','finished','cancelled') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'upcoming',
  `start_date` datetime(3) NOT NULL,
  `end_date` datetime(3) NOT NULL,
  `registration_deadline` datetime(3) NOT NULL,
  `is_registration_open` tinyint(1) NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `tournament_id` int NOT NULL,
  `user_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `season_rules`
--

CREATE TABLE `season_rules` (
  `id` int NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `created_by_id` int DEFAULT NULL,
  `updated_by_id` int DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `deleted_by_id` int DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `season_id` int NOT NULL,
  `points_per_win` int NOT NULL DEFAULT '3',
  `points_per_draw` int NOT NULL DEFAULT '1',
  `points_per_loss` int NOT NULL DEFAULT '0',
  `yellow_cards_suspension` int NOT NULL DEFAULT '3',
  `max_players_per_team` int NOT NULL DEFAULT '25',
  `min_players_per_team` int NOT NULL DEFAULT '11',
  `registration_fee` decimal(10,2) NOT NULL DEFAULT '0.00',
  `forfeit_score` int NOT NULL DEFAULT '3',
  `teams_advance_per_group` int NOT NULL DEFAULT '2',
  `tiebreaker_order` json NOT NULL,
  `user_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `season_teams`
--

CREATE TABLE `season_teams` (
  `id` int NOT NULL,
  `season_id` int NOT NULL,
  `team_id` int NOT NULL,
  `registered_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `status` enum('pending','active','eliminated','withdrawn') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `group_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `season_team_players`
--

CREATE TABLE `season_team_players` (
  `id` int NOT NULL,
  `season_team_id` int NOT NULL,
  `team_player_id` int NOT NULL,
  `jersey_number` int NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `user_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
  `deleted_at` datetime(3) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `user_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
  `deleted_at` datetime(3) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tournaments`
--

CREATE TABLE `tournaments` (
  `id` int NOT NULL,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `logo` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `max_teams` int NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `user_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
  `updated_at` datetime(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `password`, `phone`, `is_active`, `email_verified`, `email_verified_at`, `created_at`, `updated_at`) VALUES
(1, 'System Admin', 'admin@gmail.com', '$2b$12$MPfjwx.hXuMuSsO4IhSPtuLyg44aaJ7nx/qPh9htz1q/i1e0C09Ry', NULL, 1, 1, '2026-06-05 10:07:52.504', '2026-06-05 10:07:52.526', '2026-06-05 10:07:52.526'),
(2, 'Nguyễn Văn An', 'an.2021001@student.edu.vn', '$2b$12$6Ck0/Y6r64XnwTX6VWR3/uw2GeXDx.OfPYv6zQMxnzDgjwtYI5PqK', '0901000001', 1, 1, '2026-06-05 10:07:52.852', '2026-06-05 10:07:52.862', '2026-06-05 10:07:52.862'),
(3, 'Trần Minh Khoa', 'khoa.2021002@student.edu.vn', '$2b$12$LUoV8dY.V1IrQNYyhUbs/.KPry3sjT44Swv8bW4zA0xixNBI4z26O', '0901000002', 1, 1, '2026-06-05 10:07:53.168', '2026-06-05 10:07:53.171', '2026-06-05 10:07:53.171'),
(4, 'Lê Văn Trọng Tài', 'trongtai@football.local', '$2b$12$do2lDRF2tYkDqEx74UoAS.hUOzGWhunQVwIe3GT6qV.Wbstnj9Z5e', NULL, 1, 1, '2026-06-05 10:07:53.483', '2026-06-05 10:07:53.485', '2026-06-05 10:07:53.485'),
(5, 'Phạm Thị Bình', 'binh.register@student.edu.vn', '$2b$12$ntvsPqyciX.WIkeqr73ii.Yi2.ru7OOYEDJSUN7gCnBEVdvKUmDLC', '0901000005', 1, 0, NULL, '2026-06-05 10:07:53.793', '2026-06-05 10:07:53.793');

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
  `deleted_at` datetime(3) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
('17bd1e14-eaaa-4db7-8e8a-c5b06daec8aa', 'd019742b44d0b448cc5e596a34599cb53b151773d3f5c770472ee3daa95acedb', '2026-06-05 06:17:04.489', '20260605061704_remove_field_user_role_table', NULL, NULL, '2026-06-05 06:17:04.268', 1),
('342fa62a-8777-488c-a687-a55ad74d87c9', '75a641759323dd8b5b2eba86116fbff7aeaceb5d5027d31cb6e1d109090feffe', '2026-06-07 10:49:21.082', '20260607104917_add_all_table', NULL, NULL, '2026-06-07 10:49:17.515', 1),
('34500d01-1a0f-4eb5-ba2a-257012fb7401', '996b32f1d38039627e6b55e6fbfac851958ddded876305828edcc73a005bc79c', '2026-06-05 06:15:31.908', '20260605061531_rename_user_role_table', NULL, NULL, '2026-06-05 06:15:31.613', 1),
('5eaa2d7a-143b-4ea1-88c4-db73f4625491', '944e73d2dd2a26dada51c147e6881e3670156dc0b488ce576b76bd4f8b72c23f', '2026-06-05 05:48:56.928', '20260605054856_create_user_table', NULL, NULL, '2026-06-05 05:48:56.832', 1),
('67556091-8f39-4a49-b97d-5f98497e5f0e', '1536fec53abee4e5a7c7524b91adad10bbc402f9088b821521d90f4474c0c648', '2026-06-05 06:18:05.068', '20260605061805_remove_field_two_table', NULL, NULL, '2026-06-05 06:18:05.020', 1),
('6c55f19f-f6ee-471d-a86f-4242688c2665', '1d0fa69d2d89f27c522fe4e79dee96730a5d8d2a25518afec6eb9e482180289b', '2026-06-05 07:35:49.386', '20260605073549_add_unique_name_role_table', NULL, NULL, '2026-06-05 07:35:49.301', 1),
('73c404ed-6d8c-492b-baf4-9035c6bac0dc', '09f00b949ab5795fea8cae4261dbcae944c0e425e77f5274ee45fde886f0626b', '2026-06-05 06:14:33.408', '20260605061433_create_role_table', NULL, NULL, '2026-06-05 06:14:33.189', 1),
('9ffe51d2-b980-40e0-bc5f-832e9de1ce97', '1518572a6671221e400b33dfab4293f5e5b7f019d5d3a5506b11716e81bfc2c3', '2026-06-08 08:09:20.276', '20260608080920_remove_token_table', NULL, NULL, '2026-06-08 08:09:20.154', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `groups`
--
ALTER TABLE `groups`
  ADD PRIMARY KEY (`id`),
  ADD KEY `groups_phase_id_fkey` (`phase_id`);

--
-- Indexes for table `group_teams`
--
ALTER TABLE `group_teams`
  ADD PRIMARY KEY (`group_id`,`team_id`),
  ADD KEY `group_teams_team_id_fkey` (`team_id`);

--
-- Indexes for table `matches`
--
ALTER TABLE `matches`
  ADD PRIMARY KEY (`id`),
  ADD KEY `matches_phase_id_fkey` (`phase_id`),
  ADD KEY `matches_group_id_fkey` (`group_id`),
  ADD KEY `matches_home_team_id_fkey` (`home_team_id`),
  ADD KEY `matches_away_team_id_fkey` (`away_team_id`),
  ADD KEY `matches_next_match_id_fkey` (`next_match_id`),
  ADD KEY `matches_user_id_fkey` (`user_id`),
  ADD KEY `matches_venue_id_fkey` (`venue_id`),
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
-- Indexes for table `oauth_accounts`
--
ALTER TABLE `oauth_accounts`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `oauth_accounts_provider_provider_user_id_key` (`provider`,`provider_user_id`),
  ADD KEY `oauth_accounts_user_id_fkey` (`user_id`);

--
-- Indexes for table `passkey_credentials`
--
ALTER TABLE `passkey_credentials`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `passkey_credentials_credential_id_key` (`credential_id`),
  ADD KEY `passkey_credentials_user_id_fkey` (`user_id`);

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
  ADD KEY `seasons_tournament_id_fkey` (`tournament_id`),
  ADD KEY `seasons_user_id_fkey` (`user_id`);

--
-- Indexes for table `season_rules`
--
ALTER TABLE `season_rules`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `season_rules_season_id_key` (`season_id`),
  ADD KEY `season_rules_user_id_fkey` (`user_id`);

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
-- Indexes for table `season_team_players`
--
ALTER TABLE `season_team_players`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `season_team_players_season_team_id_team_player_id_key` (`season_team_id`,`team_player_id`),
  ADD KEY `season_team_players_team_player_id_fkey` (`team_player_id`);

--
-- Indexes for table `teams`
--
ALTER TABLE `teams`
  ADD PRIMARY KEY (`id`),
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
  ADD KEY `tournaments_user_id_fkey` (`user_id`);

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
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `_prisma_migrations`
--
ALTER TABLE `_prisma_migrations`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `groups`
--
ALTER TABLE `groups`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `matches`
--
ALTER TABLE `matches`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `match_events`
--
ALTER TABLE `match_events`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `match_results`
--
ALTER TABLE `match_results`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `notifications`
--
ALTER TABLE `notifications`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `oauth_accounts`
--
ALTER TABLE `oauth_accounts`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `passkey_credentials`
--
ALTER TABLE `passkey_credentials`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `phases`
--
ALTER TABLE `phases`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `players`
--
ALTER TABLE `players`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `player_statistics`
--
ALTER TABLE `player_statistics`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `seasons`
--
ALTER TABLE `seasons`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `season_rules`
--
ALTER TABLE `season_rules`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `season_teams`
--
ALTER TABLE `season_teams`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `season_team_players`
--
ALTER TABLE `season_team_players`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `teams`
--
ALTER TABLE `teams`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `team_leaders`
--
ALTER TABLE `team_leaders`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `team_players`
--
ALTER TABLE `team_players`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `team_standings`
--
ALTER TABLE `team_standings`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tournaments`
--
ALTER TABLE `tournaments`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `venues`
--
ALTER TABLE `venues`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `groups`
--
ALTER TABLE `groups`
  ADD CONSTRAINT `groups_phase_id_fkey` FOREIGN KEY (`phase_id`) REFERENCES `phases` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `group_teams`
--
ALTER TABLE `group_teams`
  ADD CONSTRAINT `group_teams_group_id_fkey` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `group_teams_team_id_fkey` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `matches`
--
ALTER TABLE `matches`
  ADD CONSTRAINT `matches_away_team_id_fkey` FOREIGN KEY (`away_team_id`) REFERENCES `teams` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `matches_group_id_fkey` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `matches_home_team_id_fkey` FOREIGN KEY (`home_team_id`) REFERENCES `teams` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `matches_next_match_id_fkey` FOREIGN KEY (`next_match_id`) REFERENCES `matches` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
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
-- Constraints for table `oauth_accounts`
--
ALTER TABLE `oauth_accounts`
  ADD CONSTRAINT `oauth_accounts_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `passkey_credentials`
--
ALTER TABLE `passkey_credentials`
  ADD CONSTRAINT `passkey_credentials_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

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
-- Constraints for table `season_rules`
--
ALTER TABLE `season_rules`
  ADD CONSTRAINT `season_rules_season_id_fkey` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `season_rules_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `season_teams`
--
ALTER TABLE `season_teams`
  ADD CONSTRAINT `season_teams_group_id_fkey` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `season_teams_season_id_fkey` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `season_teams_team_id_fkey` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `season_teams_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `season_team_players`
--
ALTER TABLE `season_team_players`
  ADD CONSTRAINT `season_team_players_season_team_id_fkey` FOREIGN KEY (`season_team_id`) REFERENCES `season_teams` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `season_team_players_team_player_id_fkey` FOREIGN KEY (`team_player_id`) REFERENCES `team_players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

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
-- Constraints for table `user_role`
--
ALTER TABLE `user_role`
  ADD CONSTRAINT `User_Role_role_id_fkey` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `User_Role_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
