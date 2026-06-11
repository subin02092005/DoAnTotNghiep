-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: football_management
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `_prisma_migrations`
--

DROP TABLE IF EXISTS `_prisma_migrations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `_prisma_migrations` (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `checksum` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `finished_at` datetime(3) DEFAULT NULL,
  `migration_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `logs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `rolled_back_at` datetime(3) DEFAULT NULL,
  `started_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `applied_steps_count` int unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `_prisma_migrations`
--

LOCK TABLES `_prisma_migrations` WRITE;
/*!40000 ALTER TABLE `_prisma_migrations` DISABLE KEYS */;
INSERT INTO `_prisma_migrations` VALUES ('17bd1e14-eaaa-4db7-8e8a-c5b06daec8aa','d019742b44d0b448cc5e596a34599cb53b151773d3f5c770472ee3daa95acedb','2026-06-05 06:17:04.489','20260605061704_remove_field_user_role_table',NULL,NULL,'2026-06-05 06:17:04.268',1),('342fa62a-8777-488c-a687-a55ad74d87c9','75a641759323dd8b5b2eba86116fbff7aeaceb5d5027d31cb6e1d109090feffe','2026-06-07 10:49:21.082','20260607104917_add_all_table',NULL,NULL,'2026-06-07 10:49:17.515',1),('34500d01-1a0f-4eb5-ba2a-257012fb7401','996b32f1d38039627e6b55e6fbfac851958ddded876305828edcc73a005bc79c','2026-06-05 06:15:31.908','20260605061531_rename_user_role_table',NULL,NULL,'2026-06-05 06:15:31.613',1),('5eaa2d7a-143b-4ea1-88c4-db73f4625491','944e73d2dd2a26dada51c147e6881e3670156dc0b488ce576b76bd4f8b72c23f','2026-06-05 05:48:56.928','20260605054856_create_user_table',NULL,NULL,'2026-06-05 05:48:56.832',1),('67556091-8f39-4a49-b97d-5f98497e5f0e','1536fec53abee4e5a7c7524b91adad10bbc402f9088b821521d90f4474c0c648','2026-06-05 06:18:05.068','20260605061805_remove_field_two_table',NULL,NULL,'2026-06-05 06:18:05.020',1),('6c55f19f-f6ee-471d-a86f-4242688c2665','1d0fa69d2d89f27c522fe4e79dee96730a5d8d2a25518afec6eb9e482180289b','2026-06-05 07:35:49.386','20260605073549_add_unique_name_role_table',NULL,NULL,'2026-06-05 07:35:49.301',1),('73c404ed-6d8c-492b-baf4-9035c6bac0dc','09f00b949ab5795fea8cae4261dbcae944c0e425e77f5274ee45fde886f0626b','2026-06-05 06:14:33.408','20260605061433_create_role_table',NULL,NULL,'2026-06-05 06:14:33.189',1);
/*!40000 ALTER TABLE `_prisma_migrations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_teams`
--

DROP TABLE IF EXISTS `group_teams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_teams` (
  `group_id` int NOT NULL,
  `team_id` int NOT NULL,
  PRIMARY KEY (`group_id`,`team_id`),
  KEY `group_teams_team_id_fkey` (`team_id`),
  CONSTRAINT `group_teams_group_id_fkey` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `group_teams_team_id_fkey` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_teams`
--

LOCK TABLES `group_teams` WRITE;
/*!40000 ALTER TABLE `group_teams` DISABLE KEYS */;
/*!40000 ALTER TABLE `group_teams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `groups` (
  `id` int NOT NULL AUTO_INCREMENT,
  `phase_id` int NOT NULL,
  `name` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `groups_phase_id_fkey` (`phase_id`),
  CONSTRAINT `groups_phase_id_fkey` FOREIGN KEY (`phase_id`) REFERENCES `phases` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups`
--

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
INSERT INTO `groups` VALUES (1,1,'Bảng A',1,'2026-06-12 03:43:49.452',NULL);
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `match_events`
--

DROP TABLE IF EXISTS `match_events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `match_events` (
  `id` int NOT NULL AUTO_INCREMENT,
  `match_id` int NOT NULL,
  `player_id` int DEFAULT NULL,
  `team_id` int DEFAULT NULL,
  `type` enum('goal','own_goal','yellow_card','red_card','second_yellow','substitution_in','substitution_out','penalty_scored','penalty_missed') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `minute` int DEFAULT NULL,
  `note` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `period` enum('first_half','second_half','extra_time_first','extra_time_second','penalty_shootout') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `added_minute` int DEFAULT NULL,
  `card_color` enum('yellow','red') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sub_out_player_id` int DEFAULT NULL,
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  KEY `match_events_match_id_fkey` (`match_id`),
  CONSTRAINT `match_events_match_id_fkey` FOREIGN KEY (`match_id`) REFERENCES `matches` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `match_events`
--

LOCK TABLES `match_events` WRITE;
/*!40000 ALTER TABLE `match_events` DISABLE KEYS */;
INSERT INTO `match_events` VALUES (1,1,1,1,'goal',25,NULL,'first_half',NULL,NULL,NULL,'2026-06-12 03:41:54.302');
/*!40000 ALTER TABLE `match_events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `match_results`
--

DROP TABLE IF EXISTS `match_results`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `match_results` (
  `id` int NOT NULL AUTO_INCREMENT,
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
  `result_type` enum('full_time','extra_time','penalty','forfeit','walkover') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('official','protested','overturned') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'official',
  `duration` int DEFAULT NULL,
  `notes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `match_results_match_id_key` (`match_id`),
  KEY `match_results_winner_team_id_fkey` (`winner_team_id`),
  CONSTRAINT `match_results_match_id_fkey` FOREIGN KEY (`match_id`) REFERENCES `matches` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `match_results_winner_team_id_fkey` FOREIGN KEY (`winner_team_id`) REFERENCES `teams` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `match_results`
--

LOCK TABLES `match_results` WRITE;
/*!40000 ALTER TABLE `match_results` DISABLE KEYS */;
INSERT INTO `match_results` VALUES (1,1,1,2,1,0,0,NULL,NULL,NULL,NULL,2,1,'full_time','official',NULL,NULL,1,'2026-06-12 03:41:54.293',NULL,NULL,0);
/*!40000 ALTER TABLE `match_results` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `matches`
--

DROP TABLE IF EXISTS `matches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `matches` (
  `id` int NOT NULL AUTO_INCREMENT,
  `phase_id` int NOT NULL,
  `group_id` int DEFAULT NULL,
  `home_team_id` int NOT NULL,
  `away_team_id` int NOT NULL,
  `scheduled_at` datetime(3) DEFAULT NULL,
  `played_at` datetime(3) DEFAULT NULL,
  `home_score` int DEFAULT NULL,
  `away_score` int DEFAULT NULL,
  `status` enum('scheduled','ongoing','finished','cancelled','forfeited') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'scheduled',
  `round` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
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
  `referee` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `season_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `matches_phase_id_fkey` (`phase_id`),
  KEY `matches_group_id_fkey` (`group_id`),
  KEY `matches_home_team_id_fkey` (`home_team_id`),
  KEY `matches_away_team_id_fkey` (`away_team_id`),
  KEY `matches_next_match_id_fkey` (`next_match_id`),
  KEY `matches_user_id_fkey` (`user_id`),
  KEY `matches_venue_id_fkey` (`venue_id`),
  KEY `matches_season_id_fkey` (`season_id`),
  CONSTRAINT `matches_away_team_id_fkey` FOREIGN KEY (`away_team_id`) REFERENCES `teams` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `matches_group_id_fkey` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `matches_home_team_id_fkey` FOREIGN KEY (`home_team_id`) REFERENCES `teams` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `matches_next_match_id_fkey` FOREIGN KEY (`next_match_id`) REFERENCES `matches` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `matches_phase_id_fkey` FOREIGN KEY (`phase_id`) REFERENCES `phases` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `matches_season_id_fkey` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `matches_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `matches_venue_id_fkey` FOREIGN KEY (`venue_id`) REFERENCES `venues` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `matches`
--

LOCK TABLES `matches` WRITE;
/*!40000 ALTER TABLE `matches` DISABLE KEYS */;
INSERT INTO `matches` VALUES (1,1,NULL,1,2,'2026-06-15 15:30:00.000','2026-06-15 17:00:00.000',2,1,'finished','Vòng 1',1,NULL,1,'2026-06-08 03:00:04.000',NULL,NULL,0,1,1,1,'Lê Văn Trọng Tài',1),(2,1,NULL,2,3,'2026-06-18 17:30:00.000',NULL,NULL,NULL,'scheduled','Vòng 1',1,NULL,1,'2026-06-08 03:00:04.000',NULL,NULL,0,1,2,1,'Trần Trọng Trọng Tài',1);
/*!40000 ALTER TABLE `matches` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` enum('match_schedule','match_result','standing_updated','registration_deadline','team_advanced','payment_confirmed','payment_rejected','player_approved','player_rejected','team_approved','team_rejected','general') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `source` enum('manual','system') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `season_id` int DEFAULT NULL,
  `target_team_id` int DEFAULT NULL,
  `recipient_user_id` int DEFAULT NULL,
  `is_read` tinyint(1) NOT NULL DEFAULT '0',
  `ref_entity_type` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ref_entity_id` int DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `notifications_season_id_fkey` (`season_id`),
  KEY `notifications_target_team_id_fkey` (`target_team_id`),
  CONSTRAINT `notifications_season_id_fkey` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `notifications_target_team_id_fkey` FOREIGN KEY (`target_team_id`) REFERENCES `teams` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oauth_accounts`
--

DROP TABLE IF EXISTS `oauth_accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oauth_accounts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `provider` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `provider_user_id` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email_verified` tinyint(1) NOT NULL DEFAULT '0',
  `avatar_url` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `access_token` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `token_expires_at` datetime(3) DEFAULT NULL,
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `oauth_accounts_provider_provider_user_id_key` (`provider`,`provider_user_id`),
  KEY `oauth_accounts_user_id_fkey` (`user_id`),
  CONSTRAINT `oauth_accounts_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauth_accounts`
--

LOCK TABLES `oauth_accounts` WRITE;
/*!40000 ALTER TABLE `oauth_accounts` DISABLE KEYS */;
/*!40000 ALTER TABLE `oauth_accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `passkey_credentials`
--

DROP TABLE IF EXISTS `passkey_credentials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `passkey_credentials` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `device_id` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `credential_id` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `public_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `sign_count` bigint NOT NULL DEFAULT '0',
  `aaguid` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_backup_eligible` tinyint(1) NOT NULL DEFAULT '0',
  `is_backed_up` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `passkey_credentials_credential_id_key` (`credential_id`),
  KEY `passkey_credentials_user_id_fkey` (`user_id`),
  CONSTRAINT `passkey_credentials_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `passkey_credentials`
--

LOCK TABLES `passkey_credentials` WRITE;
/*!40000 ALTER TABLE `passkey_credentials` DISABLE KEYS */;
/*!40000 ALTER TABLE `passkey_credentials` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `season_team_id` int NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `status` enum('pending','confirmed','rejected') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  `transaction_ref` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `paid_at` datetime(3) DEFAULT NULL,
  `confirmed_at` datetime(3) DEFAULT NULL,
  `confirmed_by` int DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `payments_season_team_id_fkey` (`season_team_id`),
  CONSTRAINT `payments_season_team_id_fkey` FOREIGN KEY (`season_team_id`) REFERENCES `season_teams` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phases`
--

DROP TABLE IF EXISTS `phases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `phases` (
  `id` int NOT NULL AUTO_INCREMENT,
  `season_id` int NOT NULL,
  `name` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` enum('group_stage','round_of_16','quarter_final','semi_final','third_place','final') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `format` enum('round_robin','knockout') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `order` int NOT NULL,
  `start_date` datetime(3) DEFAULT NULL,
  `end_date` datetime(3) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `phases_season_id_fkey` (`season_id`),
  CONSTRAINT `phases_season_id_fkey` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phases`
--

LOCK TABLES `phases` WRITE;
/*!40000 ALTER TABLE `phases` DISABLE KEYS */;
INSERT INTO `phases` VALUES (1,1,'Vòng Bảng Toàn Quốc','group_stage','round_robin',1,'2026-06-08 03:00:04.000','2026-06-08 03:00:04.000',1,'2026-06-08 03:00:04.000','2026-06-08 03:00:04.000');
/*!40000 ALTER TABLE `phases` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player_statistics`
--

DROP TABLE IF EXISTS `player_statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `player_statistics` (
  `id` int NOT NULL AUTO_INCREMENT,
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
  `updated_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `player_statistics_player_id_team_id_season_id_key` (`player_id`,`team_id`,`season_id`),
  KEY `player_statistics_team_id_fkey` (`team_id`),
  KEY `player_statistics_season_id_fkey` (`season_id`),
  CONSTRAINT `player_statistics_player_id_fkey` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `player_statistics_season_id_fkey` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `player_statistics_team_id_fkey` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_statistics`
--

LOCK TABLES `player_statistics` WRITE;
/*!40000 ALTER TABLE `player_statistics` DISABLE KEYS */;
/*!40000 ALTER TABLE `player_statistics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `players`
--

DROP TABLE IF EXISTS `players`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `players` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date_of_birth` datetime(3) NOT NULL,
  `position` enum('goalkeeper','defender','midfielder','forward') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `height` decimal(5,2) DEFAULT NULL,
  `weight` decimal(5,2) DEFAULT NULL,
  `nationality` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `avatar` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `players_user_id_key` (`user_id`),
  CONSTRAINT `players_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `players`
--

LOCK TABLES `players` WRITE;
/*!40000 ALTER TABLE `players` DISABLE KEYS */;
INSERT INTO `players` VALUES (1,'2003-05-15 00:00:00.000','forward',175.50,68.00,'Vietnam',NULL,1,'2026-06-12 03:32:21.573',NULL,NULL,0,2),(2,'2004-11-20 00:00:00.000','midfielder',170.00,62.50,'Vietnam',NULL,1,'2026-06-12 03:32:21.573',NULL,NULL,0,3);
/*!40000 ALTER TABLE `players` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `roles_name_key` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'admin','Quản trị hệ thống',1,'2026-06-05 10:05:54.506','2026-06-05 10:05:54.506'),(2,'leader','Trưởng đội / đại diện đội bóng',1,'2026-06-05 10:05:54.544','2026-06-05 10:05:54.544'),(3,'player','Cầu thủ đã claim tài khoản',1,'2026-06-05 10:05:54.559','2026-06-05 10:05:54.559'),(4,'referee','Trọng tài',1,'2026-06-05 10:05:54.573','2026-06-05 10:05:54.573'),(5,'user','Tài khoản mặc định khi đăng ký',1,'2026-06-05 10:05:54.586','2026-06-05 10:05:54.586');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `season_rules`
--

DROP TABLE IF EXISTS `season_rules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `season_rules` (
  `id` int NOT NULL AUTO_INCREMENT,
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
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `season_rules_season_id_key` (`season_id`),
  KEY `season_rules_user_id_fkey` (`user_id`),
  CONSTRAINT `season_rules_season_id_fkey` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `season_rules_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `season_rules`
--

LOCK TABLES `season_rules` WRITE;
/*!40000 ALTER TABLE `season_rules` DISABLE KEYS */;
INSERT INTO `season_rules` VALUES (1,1,'2026-06-12 03:32:21.549',NULL,NULL,NULL,NULL,NULL,0,1,3,1,0,3,25,11,1500000.00,3,2,'[\"points\", \"goal_difference\", \"goals_for\"]',1);
/*!40000 ALTER TABLE `season_rules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `season_team_players`
--

DROP TABLE IF EXISTS `season_team_players`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `season_team_players` (
  `id` int NOT NULL AUTO_INCREMENT,
  `season_team_id` int NOT NULL,
  `team_player_id` int NOT NULL,
  `jersey_number` int NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `season_team_players_season_team_id_team_player_id_key` (`season_team_id`,`team_player_id`),
  KEY `season_team_players_team_player_id_fkey` (`team_player_id`),
  CONSTRAINT `season_team_players_season_team_id_fkey` FOREIGN KEY (`season_team_id`) REFERENCES `season_teams` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `season_team_players_team_player_id_fkey` FOREIGN KEY (`team_player_id`) REFERENCES `team_players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `season_team_players`
--

LOCK TABLES `season_team_players` WRITE;
/*!40000 ALTER TABLE `season_team_players` DISABLE KEYS */;
/*!40000 ALTER TABLE `season_team_players` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `season_teams`
--

DROP TABLE IF EXISTS `season_teams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `season_teams` (
  `id` int NOT NULL AUTO_INCREMENT,
  `season_id` int NOT NULL,
  `team_id` int NOT NULL,
  `registered_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `status` enum('pending','active','eliminated','withdrawn') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `group_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `season_teams_season_id_team_id_key` (`season_id`,`team_id`),
  KEY `season_teams_team_id_fkey` (`team_id`),
  KEY `season_teams_group_id_fkey` (`group_id`),
  KEY `season_teams_user_id_fkey` (`user_id`),
  CONSTRAINT `season_teams_group_id_fkey` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `season_teams_season_id_fkey` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `season_teams_team_id_fkey` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `season_teams_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `season_teams`
--

LOCK TABLES `season_teams` WRITE;
/*!40000 ALTER TABLE `season_teams` DISABLE KEYS */;
INSERT INTO `season_teams` VALUES (1,1,1,'2026-06-08 03:00:04.000','active',1,'2026-06-08 03:00:04.000','2026-06-08 03:00:04.000',NULL,0,NULL,NULL),(2,1,2,'2026-06-08 03:00:04.000','active',1,'2026-06-08 03:00:04.000','2026-06-08 03:00:04.000',NULL,0,NULL,NULL),(3,1,3,'2026-06-08 03:00:04.000','active',1,'2026-06-08 03:00:04.000','2026-06-08 03:00:04.000',NULL,0,NULL,NULL);
/*!40000 ALTER TABLE `season_teams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seasons`
--

DROP TABLE IF EXISTS `seasons`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seasons` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` enum('upcoming','registration_open','ongoing','finished','cancelled') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'upcoming',
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
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `seasons_tournament_id_fkey` (`tournament_id`),
  KEY `seasons_user_id_fkey` (`user_id`),
  CONSTRAINT `seasons_tournament_id_fkey` FOREIGN KEY (`tournament_id`) REFERENCES `tournaments` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `seasons_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seasons`
--

LOCK TABLES `seasons` WRITE;
/*!40000 ALTER TABLE `seasons` DISABLE KEYS */;
INSERT INTO `seasons` VALUES (1,'Mùa giải thứ I','Giai đoạn vòng loại khu vực phía Bắc','ongoing','2026-06-01 00:00:00.000','2026-07-30 23:59:59.000','2026-05-25 17:00:00.000',0,1,'2026-06-08 03:00:04.000','2026-06-08 03:00:04.000',NULL,0,1,1),(2,'Mùa Hè 2026','Mùa giải khởi tranh tháng 6','ongoing','2026-06-01 00:00:00.000','2026-08-01 00:00:00.000','2026-05-25 00:00:00.000',0,1,'2026-06-12 03:32:21.541',NULL,NULL,0,1,1),(3,'Mùa Hè 2026','Mùa giải khởi tranh tháng 6','ongoing','2026-06-01 00:00:00.000','2026-08-01 00:00:00.000','2026-05-25 00:00:00.000',0,1,'2026-06-12 03:33:38.844',NULL,NULL,0,1,1),(4,'Mùa Hè 2026','Mùa giải khởi tranh tháng 6','ongoing','2026-06-01 00:00:00.000','2026-08-01 00:00:00.000','2026-05-25 00:00:00.000',0,1,'2026-06-12 03:34:44.088',NULL,NULL,0,1,1);
/*!40000 ALTER TABLE `seasons` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_leaders`
--

DROP TABLE IF EXISTS `team_leaders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team_leaders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `team_id` int NOT NULL,
  `user_id` int NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `team_leaders_team_id_fkey` (`team_id`),
  KEY `team_leaders_user_id_fkey` (`user_id`),
  CONSTRAINT `team_leaders_team_id_fkey` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `team_leaders_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_leaders`
--

LOCK TABLES `team_leaders` WRITE;
/*!40000 ALTER TABLE `team_leaders` DISABLE KEYS */;
INSERT INTO `team_leaders` VALUES (1,1,2,1,'2026-06-12 03:32:21.566',NULL,NULL,0),(2,2,3,1,'2026-06-12 03:32:21.566',NULL,NULL,0),(3,1,2,1,'2026-06-12 03:40:37.805',NULL,NULL,0),(4,2,3,1,'2026-06-12 03:40:37.805',NULL,NULL,0);
/*!40000 ALTER TABLE `team_leaders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_players`
--

DROP TABLE IF EXISTS `team_players`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team_players` (
  `id` int NOT NULL AUTO_INCREMENT,
  `team_id` int NOT NULL,
  `player_id` int NOT NULL,
  `jersey_number` int NOT NULL,
  `position` enum('goalkeeper','defender','midfielder','forward') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` enum('player','captain','vice_captain') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'player',
  `status` enum('active','injured','suspended') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'active',
  `approval_status` enum('pending','approved','rejected') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `team_players_team_id_jersey_number_key` (`team_id`,`jersey_number`),
  UNIQUE KEY `team_players_team_id_player_id_key` (`team_id`,`player_id`),
  KEY `team_players_player_id_fkey` (`player_id`),
  KEY `team_players_user_id_fkey` (`user_id`),
  CONSTRAINT `team_players_player_id_fkey` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `team_players_team_id_fkey` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `team_players_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_players`
--

LOCK TABLES `team_players` WRITE;
/*!40000 ALTER TABLE `team_players` DISABLE KEYS */;
INSERT INTO `team_players` VALUES (1,1,1,10,'forward','captain','active','approved',1,'2026-06-12 03:32:21.581',NULL,NULL,0,2),(2,2,2,8,'midfielder','player','active','approved',1,'2026-06-12 03:32:21.581',NULL,NULL,0,3);
/*!40000 ALTER TABLE `team_players` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_standings`
--

DROP TABLE IF EXISTS `team_standings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team_standings` (
  `id` int NOT NULL AUTO_INCREMENT,
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
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `team_standings_group_id_team_id_key` (`group_id`,`team_id`),
  KEY `team_standings_team_id_fkey` (`team_id`),
  CONSTRAINT `team_standings_group_id_fkey` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `team_standings_team_id_fkey` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_standings`
--

LOCK TABLES `team_standings` WRITE;
/*!40000 ALTER TABLE `team_standings` DISABLE KEYS */;
INSERT INTO `team_standings` VALUES (5,1,1,1,1,1,0,0,2,1,3,1,'2026-06-12 03:43:49.456',NULL,NULL,0),(6,2,1,2,1,0,0,1,1,2,0,1,'2026-06-12 03:43:49.456',NULL,NULL,0);
/*!40000 ALTER TABLE `team_standings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teams`
--

DROP TABLE IF EXISTS `teams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teams` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `coach_name` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `logo` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `teams_user_id_fkey` (`user_id`),
  CONSTRAINT `teams_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teams`
--

LOCK TABLES `teams` WRITE;
/*!40000 ALTER TABLE `teams` DISABLE KEYS */;
INSERT INTO `teams` VALUES (1,'FC Công Nghệ Thông Tin','HLV Nguyễn Văn Máy Tính','logo_cntt.png','Đội bóng đại diện khoa CNTT',1,'2026-06-08 02:59:27.000','2026-06-08 02:59:27.000',NULL,0,1),(2,'FC Kinh Tế Đối Ngoại','HLV Trần Thị Kế Toán','logo_kinhte.png','Đội bóng tập hợp các siêu sao khoa Kinh Tế',1,'2026-06-08 02:59:27.000','2026-06-08 02:59:27.000',NULL,0,3),(3,'FC Cơ Khí Động Lực','HLV Phạm Văn Động Cơ','logo_cokhi.png','Chiến binh thép khoa Cơ khí',1,'2026-06-08 02:59:27.000','2026-06-08 02:59:27.000',NULL,0,2),(4,'Bách Khoa FC','HLV Park Hang-seo',NULL,'Đội tuyển bóng đá Đại học Bách Khoa',1,'2026-06-12 03:32:21.557',NULL,NULL,0,2),(5,'Kinh Tế FC','HLV Gong Oh-kyun',NULL,'Đội tuyển bóng đá Đại học Kinh Tế',1,'2026-06-12 03:32:21.557',NULL,NULL,0,3),(6,'Bách Khoa FC','HLV Park Hang-seo',NULL,'Đội tuyển bóng đá Đại học Bách Khoa',1,'2026-06-12 03:40:37.803',NULL,NULL,0,2),(7,'Kinh Tế FC','HLV Gong Oh-kyun',NULL,'Đội tuyển bóng đá Đại học Kinh Tế',1,'2026-06-12 03:40:37.803',NULL,NULL,0,3);
/*!40000 ALTER TABLE `teams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tournaments`
--

DROP TABLE IF EXISTS `tournaments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tournaments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `logo` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `max_teams` int NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tournaments_user_id_fkey` (`user_id`),
  CONSTRAINT `tournaments_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tournaments`
--

LOCK TABLES `tournaments` WRITE;
/*!40000 ALTER TABLE `tournaments` DISABLE KEYS */;
INSERT INTO `tournaments` VALUES (1,'Giải Bóng Đá Sinh Viên Toàn Trường 2026','Giải đấu thường niên tranh cúp vô địch trường','cup_2026.png',16,1,'2026-06-08 03:00:04.000','2026-06-08 03:00:04.000',NULL,1),(2,'Giải Bóng Đá Sinh Viên Toàn Thành 2026','Giải đấu thường niên dành cho sinh viên',NULL,16,1,'2026-06-12 03:32:21.532',NULL,NULL,1),(3,'Giải Bóng Đá Sinh Viên Toàn Thành 2026','Giải đấu thường niên dành cho sinh viên',NULL,16,1,'2026-06-12 03:33:38.843',NULL,NULL,1),(4,'Giải Bóng Đá Sinh Viên Toàn Thành 2026','Giải đấu thường niên dành cho sinh viên',NULL,16,1,'2026-06-12 03:34:44.087',NULL,NULL,1);
/*!40000 ALTER TABLE `tournaments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `user_id` int NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `User_Role_role_id_fkey` (`role_id`),
  CONSTRAINT `User_Role_role_id_fkey` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `User_Role_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,1),(2,2),(2,3),(3,3),(4,4),(5,5);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `email_verified` tinyint(1) NOT NULL DEFAULT '0',
  `email_verified_at` datetime(3) DEFAULT NULL,
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `users_email_key` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'System Admin','admin@gmail.com','$2a$12$sOxxd/jZbe6qLyEz8JixOemI/ASHZDqzJrlVXrI.WqzO7pSv0DF2G',NULL,1,1,'2026-06-05 10:07:52.504','2026-06-05 10:07:52.526','2026-06-05 10:07:52.526'),(2,'Nguyễn Văn An','an.2021001@student.edu.vn','$2a$12$sOxxd/jZbe6qLyEz8JixOemI/ASHZDqzJrlVXrI.WqzO7pSv0DF2G','0901000001',1,1,'2026-06-05 10:07:52.852','2026-06-05 10:07:52.862','2026-06-05 10:07:52.862'),(3,'Trần Minh Khoa','khoa.2021002@student.edu.vn','$2a$12$sOxxd/jZbe6qLyEz8JixOemI/ASHZDqzJrlVXrI.WqzO7pSv0DF2G','0901000002',1,1,'2026-06-05 10:07:53.168','2026-06-05 10:07:53.171','2026-06-05 10:07:53.171'),(4,'Lê Văn Trọng Tài','trongtai@football.local','$2a$12$sOxxd/jZbe6qLyEz8JixOemI/ASHZDqzJrlVXrI.WqzO7pSv0DF2G',NULL,1,1,'2026-06-05 10:07:53.483','2026-06-05 10:07:53.485','2026-06-05 10:07:53.485'),(5,'Phạm Thị Bình','binh.register@student.edu.vn','$2a$12$sOxxd/jZbe6qLyEz8JixOemI/ASHZDqzJrlVXrI.WqzO7pSv0DF2G','0901000005',1,0,NULL,'2026-06-05 10:07:53.793','2026-06-05 10:07:53.793');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `venues`
--

DROP TABLE IF EXISTS `venues`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `venues` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `address` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `venues`
--

LOCK TABLES `venues` WRITE;
/*!40000 ALTER TABLE `venues` DISABLE KEYS */;
INSERT INTO `venues` VALUES (1,'Sân Vận Động Trung Tâm','Số 1 Đại Cồ Việt, Hai Bà Trưng, Hà Nội',1,'2026-06-08 03:00:04.000','2026-06-08 03:00:04.000',NULL,0),(2,'Sân Cỏ Nhân Tạo Ký Túc Xá','Khu B Ký túc xá sinh viên',1,'2026-06-08 03:00:04.000','2026-06-08 03:00:04.000',NULL,0),(3,'Sân vận động Bách Khoa','A4, KTX Bách Khoa, Q.10, HCM',1,'2026-06-12 03:31:32.376',NULL,NULL,0),(4,'Sân bóng đá Thống Nhất','138 Đào Duy Từ, Phường 6, Quận 10, HCM',1,'2026-06-12 03:31:32.376',NULL,NULL,0),(5,'Sân vận động Bách Khoa','A4, KTX Bách Khoa, Q.10, HCM',1,'2026-06-12 03:32:21.530',NULL,NULL,0),(6,'Sân bóng đá Thống Nhất','138 Đào Duy Từ, Phường 6, Quận 10, HCM',1,'2026-06-12 03:32:21.530',NULL,NULL,0),(7,'Sân vận động Bách Khoa','A4, KTX Bách Khoa, Q.10, HCM',1,'2026-06-12 03:33:38.842',NULL,NULL,0),(8,'Sân bóng đá Thống Nhất','138 Đào Duy Từ, Phường 6, Quận 10, HCM',1,'2026-06-12 03:33:38.842',NULL,NULL,0),(9,'Sân vận động Bách Khoa','A4, KTX Bách Khoa, Q.10, HCM',1,'2026-06-12 03:34:44.085',NULL,NULL,0),(10,'Sân bóng đá Thống Nhất','138 Đào Duy Từ, Phường 6, Quận 10, HCM',1,'2026-06-12 03:34:44.085',NULL,NULL,0);
/*!40000 ALTER TABLE `venues` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-12  3:50:29
