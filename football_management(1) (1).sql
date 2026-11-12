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
INSERT INTO `_prisma_migrations` VALUES ('06752b1d-ca08-419a-9e28-22a86b7045f1','944e73d2dd2a26dada51c147e6881e3670156dc0b488ce576b76bd4f8b72c23f','2026-06-16 08:48:43.270','20260605054856_create_user_table',NULL,NULL,'2026-06-16 08:48:43.187',1),('06d156b5-e1c1-44b7-af6f-ea050759de56','921836da5ebcc0ffd20b1f38eee7a812d1e5a533ff8e0a37526bce571fac86f5','2026-06-16 08:49:21.550','20260616084920_rename_seasonrule_change_to_tournamentrule_table',NULL,NULL,'2026-06-16 08:49:21.034',1),('0f3f4dc6-58d2-4172-9c0d-968e3762aefc','c2328034cda33ec19e788769cc119ec6aef59b522d81ed970700dbe8cc682aa9','2026-06-18 01:45:18.063','20260618014517_add_fields_tournament_rule_table',NULL,NULL,'2026-06-18 01:45:17.761',1),('16456d56-c669-4e41-9b4c-747b4c71212a','75a641759323dd8b5b2eba86116fbff7aeaceb5d5027d31cb6e1d109090feffe','2026-06-16 08:48:49.568','20260607104917_add_all_table',NULL,NULL,'2026-06-16 08:48:44.392',1),('1fc279e6-9e40-46fe-9eb6-b693561bd3e7','35b6de4e85126da5518022901069cf44484ae114f6fb16eb05375c774013edca','2026-06-21 11:01:45.804','20260621110144_create_bracketslot_table',NULL,NULL,'2026-06-21 11:01:44.780',1),('24b3cbf1-2ec3-40ca-81c8-36a3f0ed3c43','9fc922eedd06c59adfe4f755ce07410520c0c5d91b76b3c14f019b7180368c35','2026-06-16 08:48:49.848','20260616041416_add_unique_name_tournament_table',NULL,NULL,'2026-06-16 08:48:49.758',1),('25f7349c-0cb9-4eba-bf4b-ca8b63978561','251eec0d142dda9eb570ffd4b81de9477405632932f3613e10c19d7cd5943e15','2026-06-19 09:35:32.566','20260619093532_remove_seasonteamplayer_table',NULL,NULL,'2026-06-19 09:35:32.494',1),('39771eb2-2e1c-40b7-bf1b-1c7c742c1d62','94b3dc760b43eb542bc62ed173bb740ae18721075e2c5640051553f8ea8fe678','2026-06-19 06:12:31.978','20260619061231_remove_groupteam_table',NULL,NULL,'2026-06-19 06:12:31.933',1),('44a7017a-0082-4502-bf63-fffc13d26242','996b32f1d38039627e6b55e6fbfac851958ddded876305828edcc73a005bc79c','2026-06-16 08:48:43.849','20260605061531_rename_user_role_table',NULL,NULL,'2026-06-16 08:48:43.492',1),('49f1e0d2-4f58-42a3-a553-0b9612d43926','49e913f40107e057814a3a818bda6b9e5527013eb992384289853ee81ae14541','2026-06-17 01:32:12.548','20260617013212_remove_is_deleted_all_table',NULL,NULL,'2026-06-17 01:32:12.137',1),('57506bd2-0688-403c-a88f-ed129a7fe109','c0d5e015f3f2b55971ca4aed5a7be7c9e6557e5868f37e76dcb169953ceeebc4','2026-06-16 09:31:43.916','20260616093143_remove_by_on_tournament_rule_table',NULL,NULL,'2026-06-16 09:31:43.876',1),('5928db05-def3-42c7-a55f-b4df465f4627','09f00b949ab5795fea8cae4261dbcae944c0e425e77f5274ee45fde886f0626b','2026-06-16 08:48:43.489','20260605061433_create_role_table',NULL,NULL,'2026-06-16 08:48:43.274',1),('5a2dd335-3225-492d-9ccc-bb5b9ce316e1','f4a2a5434aff11350791b6fe655684d9488f5bac893172e99e840350f166ea86','2026-06-19 07:14:38.569','20260619071438_add_phase_status',NULL,NULL,'2026-06-19 07:14:38.511',1),('5ab653d7-1e5f-4b20-84d6-a36b9d1e9885','cf00100f3fbb5ccc0bd1691fe8a1e233746c0f73d9453ec7c77f96d8187e5b72','2026-06-19 04:33:30.801','20260619043330_fix_all_table',NULL,NULL,'2026-06-19 04:33:30.720',1),('7478de7c-417d-464e-87c5-bc44902d6920','1518572a6671221e400b33dfab4293f5e5b7f019d5d3a5506b11716e81bfc2c3','2026-06-16 08:48:49.642','20260608080920_remove_token_table',NULL,NULL,'2026-06-16 08:48:49.571',1),('84ab914d-3999-4bbd-bcc3-92a18a461f9d','d019742b44d0b448cc5e596a34599cb53b151773d3f5c770472ee3daa95acedb','2026-06-16 08:48:44.030','20260605061704_remove_field_user_role_table',NULL,NULL,'2026-06-16 08:48:43.852',1),('a386a579-3307-4d5d-92ea-bcc1afad2291','c5735449e33b33bc32f32861cd7c6a37dc2e6cdd6e7fd67a480458740305791c','2026-06-20 06:04:35.450','20260620060435_update_enum_group_table',NULL,NULL,'2026-06-20 06:04:35.187',1),('b199fa9d-437d-4310-a5a4-1b66136c3504','938ba9b7751838900d1b66d90b72ecdd3f7724bda6d9784f8e1f522b3e0c931b','2026-06-19 05:14:08.126','20260619051406_add_seasonstatus_enum',NULL,NULL,'2026-06-19 05:14:06.801',1),('cc2d46a8-9b74-47dd-ba95-7c8530c1a33f','598d1b1fd1466c0a5d1437181ef257432852ccb311e37b2ad13ec3a467d55a9a','2026-06-16 09:57:47.700','20260616095747_remove_is_deleted_season_table',NULL,NULL,'2026-06-16 09:57:47.618',1),('d21f3540-5f65-45d0-a6bf-36c0a64c8389','4eef50fe2f12e124e94a9472641d8b4fcc0ededff9caeebc41a18dbe5569f178','2026-06-19 11:07:16.890','20260619110714_add_table_articlemedia_table',NULL,NULL,'2026-06-19 11:07:14.963',1),('d257d25d-bd8c-4fbc-9a66-df5cdf5a9919','e41b445afe04017d2f977daafb1f6aee10d799bfff083aa34f510d1302a221b7','2026-06-16 08:48:49.754','20260615105408_remove_max_team_tournament_table',NULL,NULL,'2026-06-16 08:48:49.645',1),('e941b201-631e-48dc-8e6b-23bf211c1a88','1d0fa69d2d89f27c522fe4e79dee96730a5d8d2a25518afec6eb9e482180289b','2026-06-16 08:48:44.388','20260605073549_add_unique_name_role_table',NULL,NULL,'2026-06-16 08:48:44.164',1),('e9ed8e23-20cd-4536-8ad7-507089fcb70f','459f00012bd926a1b7e871d67f6d9c11392b3bcf4f54fd81c6cabe11b861b743','2026-06-16 08:48:50.150','20260616065831_option_date_season_table',NULL,NULL,'2026-06-16 08:48:49.851',1),('f9df31e7-3072-4948-8b76-8fa19b899d69','1536fec53abee4e5a7c7524b91adad10bbc402f9088b821521d90f4474c0c648','2026-06-16 08:48:44.159','20260605061805_remove_field_two_table',NULL,NULL,'2026-06-16 08:48:44.034',1);
/*!40000 ALTER TABLE `_prisma_migrations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article_media`
--

DROP TABLE IF EXISTS `article_media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article_media` (
  `id` int NOT NULL AUTO_INCREMENT,
  `article_id` int NOT NULL,
  `type` enum('image','video') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `url` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `caption` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `order` int NOT NULL DEFAULT '0',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  KEY `article_media_article_id_fkey` (`article_id`),
  CONSTRAINT `article_media_article_id_fkey` FOREIGN KEY (`article_id`) REFERENCES `articles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_media`
--

LOCK TABLES `article_media` WRITE;
/*!40000 ALTER TABLE `article_media` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_media` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article_tags`
--

DROP TABLE IF EXISTS `article_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article_tags` (
  `id` int NOT NULL AUTO_INCREMENT,
  `article_id` int NOT NULL,
  `tag` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `article_tags_article_id_tag_key` (`article_id`,`tag`),
  CONSTRAINT `article_tags_article_id_fkey` FOREIGN KEY (`article_id`) REFERENCES `articles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_tags`
--

LOCK TABLES `article_tags` WRITE;
/*!40000 ALTER TABLE `article_tags` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `articles`
--

DROP TABLE IF EXISTS `articles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `articles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `slug` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `cover_image` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` enum('draft','published','archived') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'draft',
  `user_id` int NOT NULL,
  `season_id` int DEFAULT NULL,
  `match_id` int DEFAULT NULL,
  `team_id` int DEFAULT NULL,
  `published_at` datetime(3) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `articles_slug_key` (`slug`),
  KEY `articles_user_id_fkey` (`user_id`),
  KEY `articles_season_id_fkey` (`season_id`),
  KEY `articles_match_id_fkey` (`match_id`),
  KEY `articles_team_id_fkey` (`team_id`),
  CONSTRAINT `articles_match_id_fkey` FOREIGN KEY (`match_id`) REFERENCES `matches` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `articles_season_id_fkey` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `articles_team_id_fkey` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `articles_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `articles`
--

LOCK TABLES `articles` WRITE;
/*!40000 ALTER TABLE `articles` DISABLE KEYS */;
/*!40000 ALTER TABLE `articles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bracket_slots`
--

DROP TABLE IF EXISTS `bracket_slots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bracket_slots` (
  `id` int NOT NULL AUTO_INCREMENT,
  `phase_id` int NOT NULL,
  `round` int NOT NULL,
  `slot_number` int NOT NULL,
  `match_id` int DEFAULT NULL,
  `source_a_slot_id` int DEFAULT NULL,
  `source_b_slot_id` int DEFAULT NULL,
  `seeded_home_team_id` int DEFAULT NULL,
  `seeded_away_team_id` int DEFAULT NULL,
  `is_bye` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `bracket_slots_phase_id_round_slot_number_key` (`phase_id`,`round`,`slot_number`),
  UNIQUE KEY `bracket_slots_match_id_key` (`match_id`),
  KEY `bracket_slots_source_a_slot_id_fkey` (`source_a_slot_id`),
  KEY `bracket_slots_source_b_slot_id_fkey` (`source_b_slot_id`),
  CONSTRAINT `bracket_slots_match_id_fkey` FOREIGN KEY (`match_id`) REFERENCES `matches` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `bracket_slots_phase_id_fkey` FOREIGN KEY (`phase_id`) REFERENCES `phases` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `bracket_slots_source_a_slot_id_fkey` FOREIGN KEY (`source_a_slot_id`) REFERENCES `bracket_slots` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `bracket_slots_source_b_slot_id_fkey` FOREIGN KEY (`source_b_slot_id`) REFERENCES `bracket_slots` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bracket_slots`
--

LOCK TABLES `bracket_slots` WRITE;
/*!40000 ALTER TABLE `bracket_slots` DISABLE KEYS */;
INSERT INTO `bracket_slots` VALUES (82,66,1,1,152,NULL,NULL,41,40,0),(83,67,1,1,NULL,NULL,NULL,NULL,NULL,1),(84,67,1,2,NULL,NULL,NULL,NULL,NULL,1),(85,67,1,3,NULL,NULL,NULL,NULL,NULL,1),(86,67,1,4,NULL,NULL,NULL,NULL,NULL,1),(87,67,2,1,NULL,83,84,NULL,NULL,0),(88,67,2,2,NULL,85,86,NULL,NULL,0),(89,67,3,1,NULL,87,88,NULL,NULL,0);
/*!40000 ALTER TABLE `bracket_slots` ENABLE KEYS */;
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
  `scheduleGeneratedAt` datetime(3) DEFAULT NULL,
  `status` enum('DRAFT','LOCKED','SCHEDULED','SCHEDULE_FAILED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DRAFT',
  PRIMARY KEY (`id`),
  KEY `groups_phase_id_fkey` (`phase_id`),
  CONSTRAINT `groups_phase_id_fkey` FOREIGN KEY (`phase_id`) REFERENCES `phases` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups`
--

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
INSERT INTO `groups` VALUES (3,1,'Bảng A',1,'2026-06-20 06:17:11.384','2026-06-20 06:17:11.384','2026-07-08 01:33:08.000','SCHEDULED'),(4,1,'Bảng B',1,'2026-06-20 06:17:11.394','2026-06-20 06:17:11.394','2026-07-08 01:33:08.000','SCHEDULED'),(5,6,'Bảng A',1,'2026-06-20 15:30:23.332','2026-06-20 15:30:23.332','2026-07-08 02:40:44.000','SCHEDULED'),(6,7,'Bảng A',1,'2026-06-20 15:30:23.464','2026-06-20 15:30:23.464',NULL,'DRAFT'),(7,8,'Bảng A',1,'2026-06-20 15:30:23.559','2026-06-20 15:30:23.559',NULL,'DRAFT'),(8,8,'Bảng B',1,'2026-06-20 15:30:23.572','2026-06-20 15:30:23.572',NULL,'DRAFT'),(9,9,'Bảng A',1,'2026-06-20 15:30:23.687','2026-06-20 15:30:23.687',NULL,'DRAFT'),(10,9,'Bảng B',1,'2026-06-20 15:30:23.702','2026-06-20 15:30:23.702',NULL,'DRAFT'),(61,65,'Bảng A',1,'2026-07-16 12:33:17.254','2026-07-16 12:33:17.254','2026-07-16 12:37:16.237','SCHEDULED'),(62,66,'TRẬN CHUNG KẾT (2 ĐỘI)',1,'2026-07-16 12:39:23.717','2026-07-16 12:39:23.717',NULL,'DRAFT'),(63,67,'NHÁNH ĐẤU',1,'2026-11-04 23:12:13.982','2026-11-04 23:12:13.982',NULL,'DRAFT');
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
  `type` enum('goal','own_goal','yellow_card','red_card','second_yellow','substitution_in','substitution_out','penalty_scored','penalty_missed','card_rescinded','goal_disallowed') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=164 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `match_events`
--

LOCK TABLES `match_events` WRITE;
/*!40000 ALTER TABLE `match_events` DISABLE KEYS */;
INSERT INTO `match_events` VALUES (54,16,2,1,'goal',20,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:17:11.506'),(55,16,21,2,'goal',45,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:17:11.515'),(56,16,16,2,'goal',53,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:17:11.524'),(57,16,12,1,'goal',57,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:17:11.545'),(58,16,12,1,'goal',88,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:17:11.564'),(59,16,11,1,'yellow_card',37,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:17:11.571'),(60,17,10,1,'goal',40,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:17:11.690'),(61,17,8,1,'goal',87,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:17:11.696'),(62,17,7,1,'yellow_card',52,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:17:11.702'),(63,17,4,1,'yellow_card',63,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:17:11.707'),(64,18,47,4,'goal',7,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:17:11.803'),(65,18,40,4,'goal',26,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:17:11.809'),(66,18,39,4,'goal',47,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:17:11.815'),(67,18,44,4,'yellow_card',82,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:17:11.822'),(68,18,45,4,'yellow_card',88,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:17:11.828'),(69,18,7,1,'yellow_card',26,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:17:11.835'),(70,19,31,3,'goal',20,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:17:11.917'),(71,19,30,3,'goal',50,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:17:11.923'),(72,19,14,2,'goal',51,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:17:11.929'),(73,19,14,2,'goal',56,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:17:11.935'),(74,19,24,2,'goal',80,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:17:11.941'),(75,19,28,3,'yellow_card',49,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:17:11.947'),(76,19,29,3,'yellow_card',27,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:17:11.953'),(77,19,16,2,'yellow_card',36,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:17:11.959'),(78,20,41,4,'goal',1,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:17:12.064'),(79,20,18,2,'goal',12,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:17:12.070'),(80,20,13,2,'goal',21,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:17:12.077'),(81,20,17,2,'goal',22,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:17:12.083'),(82,21,47,4,'goal',43,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:17:12.157'),(83,21,29,3,'goal',58,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:17:12.163'),(84,21,45,4,'goal',59,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:17:12.169'),(85,21,41,4,'goal',61,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:17:12.174'),(86,21,37,4,'goal',89,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:17:12.180'),(87,21,34,3,'yellow_card',49,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:17:12.185'),(88,21,48,4,'yellow_card',79,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:17:12.191'),(89,21,29,3,'yellow_card',67,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:17:12.197'),(90,23,10,1,'goal',82,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:34:24.466'),(91,24,2,1,'goal',24,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:34:24.538'),(92,24,27,3,'goal',39,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:34:24.546'),(93,24,36,3,'goal',49,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:34:24.553'),(94,25,37,4,'goal',11,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:34:24.653'),(95,25,5,1,'goal',83,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:34:24.661'),(96,25,12,1,'yellow_card',58,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:34:24.670'),(97,26,26,3,'goal',11,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:34:24.751'),(98,26,31,3,'goal',67,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:34:24.759'),(99,26,36,3,'yellow_card',52,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:34:24.766'),(100,26,31,3,'yellow_card',85,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:34:24.772'),(101,27,13,2,'goal',9,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:34:24.841'),(102,27,16,2,'goal',27,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:34:24.847'),(103,27,38,4,'goal',27,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:34:24.853'),(104,27,46,4,'goal',33,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:34:24.860'),(105,27,48,4,'goal',45,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:34:24.866'),(106,28,43,4,'goal',8,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:34:24.972'),(107,28,25,3,'goal',16,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:34:24.979'),(108,28,33,3,'goal',23,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:34:24.987'),(109,28,42,4,'goal',80,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:34:24.996'),(110,28,35,3,'yellow_card',51,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:34:25.001'),(111,28,41,4,'yellow_card',18,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:34:25.009'),(112,28,44,4,'yellow_card',83,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:34:25.015'),(113,29,65,6,'goal',10,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:34:25.156'),(114,29,58,5,'goal',28,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:34:25.163'),(115,29,54,5,'goal',40,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:34:25.168'),(116,29,52,5,'goal',68,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:34:25.174'),(117,29,69,6,'goal',80,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:34:25.181'),(118,29,60,5,'yellow_card',5,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:34:25.189'),(119,29,59,5,'yellow_card',24,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:34:25.197'),(120,30,80,7,'goal',6,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:34:25.301'),(121,30,56,5,'goal',11,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:34:25.308'),(122,30,76,7,'goal',35,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:34:25.316'),(123,30,78,7,'goal',50,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:34:25.323'),(124,30,82,7,'goal',84,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:34:25.328'),(125,31,89,8,'goal',33,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:34:25.408'),(126,31,49,5,'goal',39,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:34:25.415'),(127,31,96,8,'yellow_card',10,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:34:25.420'),(128,31,52,5,'yellow_card',9,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:34:25.425'),(129,32,78,7,'goal',59,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:34:25.488'),(130,32,78,7,'goal',77,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:34:25.494'),(131,33,94,8,'goal',49,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:34:25.544'),(132,33,62,6,'goal',66,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:34:25.550'),(133,33,61,6,'goal',77,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:34:25.556'),(134,33,71,6,'yellow_card',63,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:34:25.562'),(135,33,96,8,'yellow_card',48,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:34:25.568'),(136,33,64,6,'yellow_card',22,NULL,'second_half',NULL,'yellow',NULL,'2026-06-20 06:34:25.574'),(137,34,88,8,'goal',8,NULL,'first_half',NULL,NULL,NULL,'2026-06-20 06:34:25.665'),(138,34,74,7,'goal',68,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:34:25.670'),(139,34,88,8,'goal',85,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:34:25.677'),(140,34,86,8,'goal',90,NULL,'second_half',NULL,NULL,NULL,'2026-06-20 06:34:25.683'),(161,150,115,41,'goal',1,NULL,'first_half',NULL,NULL,NULL,'2026-07-16 12:37:58.000'),(162,149,114,40,'goal',1,NULL,'first_half',NULL,NULL,NULL,'2026-07-16 12:38:43.000'),(163,152,115,41,'goal',1,NULL,'first_half',NULL,NULL,NULL,'2026-07-16 12:47:13.000');
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
  `status` enum('official','protested','overturned','under_review') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'official',
  `duration` int DEFAULT NULL,
  `notes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `appeal_note` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `appeal_reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  UNIQUE KEY `match_results_match_id_key` (`match_id`),
  KEY `match_results_winner_team_id_fkey` (`winner_team_id`),
  CONSTRAINT `match_results_match_id_fkey` FOREIGN KEY (`match_id`) REFERENCES `matches` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `match_results_winner_team_id_fkey` FOREIGN KEY (`winner_team_id`) REFERENCES `teams` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `match_results`
--

LOCK TABLES `match_results` WRITE;
/*!40000 ALTER TABLE `match_results` DISABLE KEYS */;
INSERT INTO `match_results` VALUES (16,16,1,3,2,1,1,NULL,NULL,NULL,NULL,3,2,'full_time','official',90,NULL,1,'2026-06-20 06:17:11.581','2026-07-06 02:05:33.000',NULL,NULL,NULL),(17,17,1,2,0,1,0,NULL,NULL,NULL,NULL,2,0,'full_time','official',90,NULL,1,'2026-06-20 06:17:11.715','2026-06-20 06:17:11.715',NULL,NULL,NULL),(18,18,4,0,3,0,1,NULL,NULL,NULL,NULL,1,3,'full_time','official',90,NULL,1,'2026-06-20 06:17:11.843','2026-07-08 01:28:42.000',NULL,NULL,NULL),(19,19,2,3,2,1,1,NULL,NULL,NULL,NULL,3,2,'full_time','official',90,NULL,1,'2026-06-20 06:17:11.967','2026-06-20 06:17:11.967',NULL,NULL,NULL),(20,20,2,3,1,1,0,NULL,NULL,NULL,NULL,3,1,'full_time','official',90,NULL,1,'2026-06-20 06:17:12.089','2026-06-20 06:17:12.089',NULL,NULL,NULL),(21,21,4,1,4,0,2,NULL,NULL,NULL,NULL,1,4,'full_time','official',90,NULL,1,'2026-06-20 06:17:12.205','2026-06-20 06:17:12.205',NULL,NULL,NULL),(22,23,1,1,0,0,0,NULL,NULL,NULL,NULL,1,0,'full_time','official',90,NULL,1,'2026-06-20 06:34:24.476','2026-06-20 06:34:24.476',NULL,NULL,NULL),(23,24,3,1,2,0,1,NULL,NULL,NULL,NULL,1,2,'full_time','official',90,NULL,1,'2026-06-20 06:34:24.561','2026-06-20 06:34:24.561',NULL,NULL,NULL),(24,25,NULL,1,1,0,0,NULL,NULL,NULL,NULL,1,1,'full_time','official',90,NULL,1,'2026-06-20 06:34:24.678','2026-06-20 06:34:24.678',NULL,NULL,NULL),(25,26,3,0,2,0,1,NULL,NULL,NULL,NULL,0,2,'full_time','official',90,NULL,1,'2026-06-20 06:34:24.779','2026-06-20 06:34:24.779',NULL,NULL,NULL),(26,27,4,2,3,1,1,NULL,NULL,NULL,NULL,2,3,'full_time','official',90,NULL,1,'2026-06-20 06:34:24.874','2026-06-20 06:34:24.874',NULL,NULL,NULL),(27,28,NULL,2,2,1,1,NULL,NULL,NULL,NULL,2,2,'full_time','official',90,NULL,1,'2026-06-20 06:34:25.022','2026-06-20 06:34:25.022',NULL,NULL,NULL),(28,29,5,3,2,1,1,NULL,NULL,NULL,NULL,3,2,'full_time','official',90,NULL,1,'2026-06-20 06:34:25.204','2026-06-20 06:34:25.204',NULL,NULL,NULL),(29,30,7,1,4,0,2,NULL,NULL,NULL,NULL,1,4,'full_time','official',90,NULL,1,'2026-06-20 06:34:25.336','2026-06-20 06:34:25.336',NULL,NULL,NULL),(30,31,NULL,1,1,0,0,NULL,NULL,NULL,NULL,1,1,'full_time','official',90,NULL,1,'2026-06-20 06:34:25.430','2026-06-20 06:34:25.430',NULL,NULL,NULL),(31,32,7,0,2,0,1,NULL,NULL,NULL,NULL,0,2,'full_time','official',90,NULL,1,'2026-06-20 06:34:25.500','2026-06-20 06:34:25.500',NULL,NULL,NULL),(32,33,6,2,1,1,0,NULL,NULL,NULL,NULL,2,1,'full_time','official',90,NULL,1,'2026-06-20 06:34:25.582','2026-06-20 06:34:25.582',NULL,NULL,NULL),(33,34,8,1,3,0,1,NULL,NULL,NULL,NULL,1,3,'full_time','official',90,NULL,1,'2026-06-20 06:34:25.689','2026-06-20 06:34:25.689',NULL,NULL,NULL),(34,35,4,1,1,0,0,NULL,NULL,5,4,1,1,'penalty','official',120,NULL,1,'2026-06-20 06:34:25.782','2026-06-20 06:34:25.782',NULL,NULL,NULL),(35,36,1,3,3,1,1,NULL,NULL,4,5,3,3,'penalty','official',120,NULL,1,'2026-06-20 06:34:25.801','2026-06-20 06:34:25.801',NULL,NULL,NULL),(36,37,1,1,2,0,1,NULL,NULL,NULL,NULL,1,2,'full_time','official',90,NULL,1,'2026-06-20 06:34:25.826','2026-06-20 06:34:25.826',NULL,NULL,NULL),(62,150,41,1,0,0,0,NULL,NULL,NULL,NULL,1,0,'full_time','official',NULL,NULL,1,'2026-07-16 12:37:58.000','2026-07-16 12:38:34.000',NULL,NULL,NULL),(63,149,40,0,1,0,0,NULL,NULL,NULL,NULL,0,1,'full_time','official',NULL,NULL,1,'2026-07-16 12:38:43.000','2026-07-16 12:38:50.000',NULL,NULL,NULL),(64,148,NULL,0,0,0,0,NULL,NULL,NULL,NULL,0,0,'full_time','official',NULL,NULL,1,'2026-07-16 12:38:53.408',NULL,NULL,NULL,NULL),(65,147,NULL,0,0,0,0,NULL,NULL,NULL,NULL,0,0,'full_time','official',NULL,NULL,1,'2026-07-16 12:38:53.954',NULL,NULL,NULL,NULL),(66,146,NULL,0,0,0,0,NULL,NULL,NULL,NULL,0,0,'full_time','official',NULL,NULL,1,'2026-07-16 12:38:54.606',NULL,NULL,NULL,NULL),(67,145,NULL,0,0,0,0,NULL,NULL,NULL,NULL,0,0,'full_time','official',NULL,NULL,1,'2026-07-16 12:38:55.023',NULL,NULL,NULL,NULL),(68,152,41,1,0,0,0,NULL,NULL,NULL,NULL,1,0,'full_time','official',NULL,NULL,1,'2026-07-16 12:47:13.000','2026-07-16 12:47:57.000',NULL,NULL,NULL);
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
  `status` enum('scheduled','ongoing','finished','cancelled','forfeited','postponed','bye','abandoned') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'scheduled',
  `round` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `leg` int DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `venue_id` int DEFAULT NULL,
  `is_published` tinyint(1) NOT NULL DEFAULT '0',
  `referee` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `season_id` int DEFAULT NULL,
  `abandoned_minute` int DEFAULT NULL,
  `current_period` enum('first_half','second_half','extra_time_first','extra_time_second','penalty_shootout') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `postponed_from` datetime(3) DEFAULT NULL,
  `postponed_reason` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `replay_of_match_id` int DEFAULT NULL,
  `is_featured` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `matches_venue_id_scheduled_at_key` (`venue_id`,`scheduled_at`),
  KEY `matches_phase_id_fkey` (`phase_id`),
  KEY `matches_group_id_fkey` (`group_id`),
  KEY `matches_home_team_id_fkey` (`home_team_id`),
  KEY `matches_away_team_id_fkey` (`away_team_id`),
  KEY `matches_user_id_fkey` (`user_id`),
  KEY `matches_season_id_fkey` (`season_id`),
  CONSTRAINT `matches_away_team_id_fkey` FOREIGN KEY (`away_team_id`) REFERENCES `teams` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `matches_group_id_fkey` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `matches_home_team_id_fkey` FOREIGN KEY (`home_team_id`) REFERENCES `teams` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `matches_phase_id_fkey` FOREIGN KEY (`phase_id`) REFERENCES `phases` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `matches_season_id_fkey` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `matches_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `matches_venue_id_fkey` FOREIGN KEY (`venue_id`) REFERENCES `venues` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=153 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `matches`
--

LOCK TABLES `matches` WRITE;
/*!40000 ALTER TABLE `matches` DISABLE KEYS */;
INSERT INTO `matches` VALUES (16,1,3,1,2,'2024-03-01 00:00:00.000','2024-03-01 00:00:00.000',3,2,'finished',NULL,NULL,1,'2026-06-20 06:17:11.499','2026-07-09 18:20:33.000',NULL,1,1,1,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(17,1,3,1,3,'2024-03-04 00:00:00.000','2024-03-04 00:00:00.000',2,0,'finished',NULL,NULL,1,'2026-06-20 06:17:11.683','2026-07-09 18:20:27.000',NULL,1,1,1,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(18,1,3,1,4,'2024-03-07 00:00:00.000','2024-03-07 00:00:00.000',1,3,'finished',NULL,NULL,1,'2026-06-20 06:17:11.796','2026-07-08 01:28:51.000',NULL,1,1,1,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(19,1,3,2,3,'2024-03-10 00:00:00.000','2024-03-10 00:00:00.000',3,2,'finished',NULL,NULL,1,'2026-06-20 06:17:11.910','2026-07-06 03:38:33.000',NULL,1,1,1,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(20,1,3,2,4,'2024-03-13 00:00:00.000','2024-03-13 00:00:00.000',3,1,'finished',NULL,NULL,1,'2026-06-20 06:17:12.057','2026-06-20 06:17:12.057',NULL,1,1,1,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(21,1,3,3,4,'2024-03-16 00:00:00.000','2024-03-16 00:00:00.000',1,4,'finished',NULL,NULL,1,'2026-06-20 06:17:12.151','2026-07-06 03:16:24.000',NULL,1,1,1,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(23,1,3,1,2,'2024-03-01 08:00:00.000','2024-03-01 08:00:00.000',1,0,'finished',NULL,NULL,1,'2026-06-20 06:34:24.453','2026-07-09 18:20:29.000',NULL,1,1,1,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(24,1,3,1,3,'2024-03-04 08:00:00.000','2024-03-04 08:00:00.000',1,2,'finished',NULL,NULL,1,'2026-06-20 06:34:24.530','2026-07-09 18:20:26.000',NULL,1,1,1,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(25,1,3,1,4,'2024-03-07 08:00:00.000','2024-03-07 08:00:00.000',1,1,'finished',NULL,NULL,1,'2026-06-20 06:34:24.646','2026-07-06 03:38:55.000',NULL,1,1,1,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(26,1,3,2,3,'2024-03-10 08:00:00.000','2024-03-10 08:00:00.000',0,2,'finished',NULL,NULL,1,'2026-06-20 06:34:24.742','2026-06-20 06:34:24.742',NULL,1,1,1,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(27,1,3,2,4,'2024-03-13 08:00:00.000','2024-03-13 08:00:00.000',2,3,'finished',NULL,NULL,1,'2026-06-20 06:34:24.832','2026-06-20 06:34:24.832',NULL,1,1,1,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(28,1,3,3,4,'2024-03-16 08:00:00.000','2024-03-16 08:00:00.000',2,2,'finished',NULL,NULL,1,'2026-06-20 06:34:24.965','2026-06-20 06:34:24.965',NULL,1,1,1,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(29,1,4,5,6,'2024-03-02 08:00:00.000','2024-03-02 08:00:00.000',3,2,'finished',NULL,NULL,1,'2026-06-20 06:34:25.148','2026-07-09 18:20:31.000',NULL,1,1,1,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(30,1,4,5,7,'2024-03-05 08:00:00.000','2024-03-05 08:00:00.000',1,4,'finished',NULL,NULL,1,'2026-06-20 06:34:25.295','2026-07-09 18:20:24.000',NULL,1,1,1,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(31,1,4,5,8,'2024-03-08 08:00:00.000','2024-03-08 08:00:00.000',1,1,'finished',NULL,NULL,1,'2026-06-20 06:34:25.402','2026-06-20 06:34:25.402',NULL,1,1,1,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(32,1,4,6,7,'2024-03-11 08:00:00.000','2024-03-11 08:00:00.000',0,2,'finished',NULL,NULL,1,'2026-06-20 06:34:25.482','2026-06-20 06:34:25.482',NULL,1,1,1,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(33,1,4,6,8,'2024-03-14 08:00:00.000','2024-03-14 08:00:00.000',2,1,'finished',NULL,NULL,1,'2026-06-20 06:34:25.537','2026-07-06 03:38:34.000',NULL,1,1,1,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(34,1,4,7,8,'2024-03-17 08:00:00.000','2024-03-17 08:00:00.000',1,3,'finished',NULL,NULL,1,'2026-06-20 06:34:25.658','2026-06-20 06:34:25.658',NULL,1,1,1,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(35,2,NULL,4,5,'2024-04-20 08:00:00.000','2024-04-20 08:00:00.000',1,1,'finished',NULL,NULL,1,'2026-06-20 06:34:25.774','2026-06-20 06:34:25.774',NULL,1,1,1,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(36,2,NULL,7,1,'2024-04-23 08:00:00.000','2024-04-23 08:00:00.000',3,3,'finished',NULL,NULL,1,'2026-06-20 06:34:25.792','2026-06-20 06:34:25.792',NULL,1,1,1,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(37,3,NULL,4,1,'2024-05-05 08:00:00.000','2024-05-05 08:00:00.000',1,2,'finished',NULL,NULL,1,'2026-06-20 06:34:25.816','2026-06-20 06:34:25.816',NULL,1,1,1,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(62,1,3,4,3,'2025-06-01 18:00:00.000','2026-07-08 01:34:00.000',0,3,'cancelled',NULL,NULL,1,'2026-07-08 01:33:08.000','2026-07-16 08:49:32.000',NULL,NULL,NULL,0,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(63,1,3,4,2,'2025-06-01 20:00:00.000','2026-07-08 01:34:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 01:33:08.000','2026-07-16 08:49:32.000',NULL,NULL,NULL,0,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(64,1,3,2,3,'2025-06-01 22:00:00.000','2026-07-08 01:34:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 01:33:08.000','2026-07-16 08:49:32.000',NULL,NULL,NULL,0,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(65,1,4,7,8,'2025-06-02 00:00:00.000','2026-07-08 01:34:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 01:33:08.000','2026-07-16 08:49:31.000',NULL,NULL,NULL,0,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(66,1,4,5,7,'2025-06-02 02:00:00.000','2026-07-08 01:34:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 01:33:08.000','2026-07-16 08:49:31.000',NULL,NULL,NULL,0,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(67,1,4,8,6,'2025-06-02 04:00:00.000','2026-07-08 01:34:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 01:33:08.000','2026-07-16 08:49:30.000',NULL,NULL,NULL,0,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(68,1,4,5,8,'2025-06-02 06:00:00.000','2026-07-08 01:34:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 01:33:08.000','2026-07-16 08:49:30.000',NULL,NULL,NULL,0,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(69,1,4,5,6,'2025-06-02 08:00:00.000','2026-07-08 01:34:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 01:33:08.000','2026-07-16 08:49:30.000',NULL,NULL,NULL,0,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(70,1,4,6,7,'2025-06-02 10:00:00.000','2026-07-08 01:34:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 01:33:08.000','2026-07-16 08:49:29.000',NULL,NULL,NULL,0,NULL,1,NULL,NULL,NULL,NULL,NULL,0),(71,6,5,6,8,'2025-06-01 18:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:33.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(72,6,5,5,4,'2025-06-01 20:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:32.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(73,6,5,6,4,'2025-06-01 22:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:32.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(74,6,5,5,1,'2025-06-02 00:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:31.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(75,6,5,1,2,'2025-06-02 02:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:31.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(76,6,5,6,7,'2025-06-02 04:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:31.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(77,6,5,7,2,'2025-06-02 06:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:30.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(78,6,5,5,7,'2025-06-02 08:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:30.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(79,6,5,7,1,'2025-06-02 10:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:29.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(80,6,5,8,4,'2025-06-02 12:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:29.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(81,6,5,6,5,'2025-06-02 14:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:29.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(82,6,5,4,2,'2025-06-02 16:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:29.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(83,6,5,7,8,'2025-06-02 18:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:29.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(84,6,5,4,7,'2025-06-02 20:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:28.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(85,6,5,8,3,'2025-06-02 22:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:28.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(86,6,5,2,3,'2025-06-03 00:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:28.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(87,6,5,2,5,'2025-06-03 02:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:28.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(88,6,5,6,2,'2025-06-03 04:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:11.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(89,6,5,3,7,'2025-06-03 06:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:27.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(90,6,5,6,3,'2025-06-03 08:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:27.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(91,6,5,8,5,'2025-06-03 10:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:19.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(92,6,5,3,5,'2025-06-03 12:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:17.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(93,6,5,3,4,'2025-06-03 14:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:08.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(94,6,5,1,8,'2025-06-03 16:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:07.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(95,6,5,1,3,'2025-06-03 18:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:05.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(96,6,5,6,1,'2025-06-03 20:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:04.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(97,6,5,2,8,'2025-06-03 22:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:03.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(98,6,5,4,1,'2025-06-04 00:00:00.000','2026-07-08 02:41:00.000',NULL,NULL,'cancelled',NULL,NULL,1,'2026-07-08 02:40:44.000','2026-07-16 08:49:02.000',NULL,NULL,NULL,0,NULL,2,NULL,NULL,NULL,NULL,NULL,0),(145,65,61,39,40,'2026-07-17 18:00:00.000','2026-07-16 12:37:24.000',0,0,'finished',NULL,NULL,1,'2026-07-16 12:37:16.206','2026-07-16 12:38:55.000',NULL,NULL,1,0,NULL,27,NULL,NULL,NULL,NULL,NULL,0),(146,65,61,40,41,'2026-07-17 20:00:00.000','2026-07-16 12:37:25.000',0,0,'finished',NULL,NULL,1,'2026-07-16 12:37:16.220','2026-07-16 12:38:54.000',NULL,NULL,1,0,NULL,27,NULL,NULL,NULL,NULL,NULL,0),(147,65,61,38,41,'2026-07-17 22:00:00.000','2026-07-16 12:37:26.000',0,0,'finished',NULL,NULL,1,'2026-07-16 12:37:16.224','2026-07-16 12:38:53.000',NULL,NULL,1,0,NULL,27,NULL,NULL,NULL,NULL,NULL,0),(148,65,61,38,39,'2026-07-18 00:00:00.000','2026-07-16 12:37:28.000',0,0,'finished',NULL,NULL,1,'2026-07-16 12:37:16.226','2026-07-16 12:38:53.000',NULL,NULL,1,0,NULL,27,NULL,NULL,NULL,NULL,NULL,0),(149,65,61,38,40,'2026-07-18 02:00:00.000','2026-07-16 12:37:29.000',0,1,'finished',NULL,NULL,1,'2026-07-16 12:37:16.229','2026-07-16 12:38:50.000',NULL,NULL,1,0,NULL,27,NULL,NULL,NULL,NULL,NULL,0),(150,65,61,41,39,'2026-07-18 04:00:00.000','2026-07-16 12:37:30.000',1,0,'finished',NULL,NULL,1,'2026-07-16 12:37:16.233','2026-07-16 12:38:34.000',NULL,NULL,1,0,NULL,27,NULL,NULL,NULL,NULL,NULL,0),(152,66,62,41,40,'2026-07-18 06:00:00.000','2026-07-16 12:47:06.000',1,0,'finished',NULL,NULL,1,'2026-07-16 12:46:53.875','2026-07-16 12:47:57.000',NULL,NULL,3,0,NULL,27,NULL,NULL,NULL,NULL,NULL,0);
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
  PRIMARY KEY (`id`),
  KEY `notifications_season_id_fkey` (`season_id`),
  KEY `notifications_target_team_id_fkey` (`target_team_id`),
  CONSTRAINT `notifications_season_id_fkey` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `notifications_target_team_id_fkey` FOREIGN KEY (`target_team_id`) REFERENCES `teams` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (77,'dasdadasdas','dasd','general','manual',NULL,NULL,NULL,0,NULL,NULL,1,'2026-07-11 06:54:04.393','2026-07-11 08:16:51.000',NULL),(91,'Cập nhật luật giải đấu mới','Luật mới: 7-11 cầu thủ. Điểm: T(3) H(1) B(0). Ghi chú: 02asdadasd','general','manual',NULL,NULL,NULL,0,NULL,NULL,1,'2026-07-11 15:49:57.165',NULL,NULL),(93,'dasd','dasdasdasd','match_schedule','manual',NULL,37,NULL,0,NULL,NULL,1,'2026-07-13 01:51:08.594',NULL,NULL),(95,'Cập nhật luật giải đấu Season Test 01 — 8 teams single group','Giải [ Season Test 01 — 8 teams single group] áp dụng luật: Đội hình 7-11 cầu thủ. Điểm số: Thắng (3) - Hòa (1) - Thua (0). ','general','system',2,NULL,NULL,0,'tournament_rules',1,1,'2026-07-13 03:23:33.912',NULL,NULL),(96,'Thông báo đăng ký giải đấu','Đội nva đã đăng ký thành công giải asdasdasd. Vui lòng hoàn tất thanh toán phí tham dự.','general','system',NULL,34,112,0,NULL,NULL,1,'2026-07-16 08:55:52.000',NULL,NULL),(97,'Thông báo đăng ký giải đấu','Đội nva đã đăng ký thành công giải asdasdasd. Vui lòng hoàn tất thanh toán phí tham dự.','general','system',NULL,34,112,0,NULL,NULL,1,'2026-07-16 08:56:08.000',NULL,NULL),(98,'Thông báo đăng ký giải đấu','Đội nva đã đăng ký thành công giải asdasdasd. Vui lòng hoàn tất thanh toán phí tham dự.','general','system',NULL,34,112,0,NULL,NULL,1,'2026-07-16 09:07:15.000',NULL,NULL),(99,'Giải đấu Giai sinh vien  đã mở!','danh cho sinh vien','general','system',NULL,NULL,6,0,NULL,NULL,1,'2026-07-16 10:30:26.000',NULL,NULL),(100,'Giải đấu Giai sinh vien  đã mở!','danh cho sinh vien','general','system',NULL,NULL,7,0,NULL,NULL,1,'2026-07-16 10:30:27.000',NULL,NULL),(101,'Giải đấu Giai sinh vien  đã mở!','danh cho sinh vien','general','system',NULL,NULL,110,0,NULL,NULL,1,'2026-07-16 10:30:27.000',NULL,NULL),(102,'Giải đấu Giai sinh vien  đã mở!','danh cho sinh vien','general','system',NULL,NULL,111,0,NULL,NULL,1,'2026-07-16 10:30:27.000',NULL,NULL),(103,'Giải đấu Giai sinh vien  đã mở!','danh cho sinh vien','general','system',NULL,NULL,112,0,NULL,NULL,1,'2026-07-16 10:30:27.000',NULL,NULL),(104,'Giải đấu Giai sinh vien  đã mở!','danh cho sinh vien','general','system',NULL,NULL,113,0,NULL,NULL,1,'2026-07-16 10:30:27.000',NULL,NULL),(105,'Giải đấu Giai sinh vien  đã mở!','danh cho sinh vien','general','system',NULL,NULL,114,0,NULL,NULL,1,'2026-07-16 10:30:28.000',NULL,NULL),(106,'Giải đấu Giai sinh vien  đã mở!','danh cho sinh vien','general','system',NULL,NULL,115,0,NULL,NULL,1,'2026-07-16 10:30:29.000',NULL,NULL),(107,'Giải đấu Giai sinh vien  đã mở!','danh cho sinh vien','general','system',NULL,NULL,116,0,NULL,NULL,1,'2026-07-16 10:30:29.000',NULL,NULL),(108,'Giải đấu mới!','Mùa giải 26-27 đã sẵn sàng đón nhận đăng ký.','general','system',NULL,NULL,6,0,NULL,NULL,1,'2026-07-16 10:42:04.000',NULL,NULL),(109,'Giải đấu mới!','Mùa giải 26-27 đã sẵn sàng đón nhận đăng ký.','general','system',NULL,NULL,7,0,NULL,NULL,1,'2026-07-16 10:42:05.000',NULL,NULL),(110,'Giải đấu mới!','Mùa giải 26-27 đã sẵn sàng đón nhận đăng ký.','general','system',NULL,NULL,110,0,NULL,NULL,1,'2026-07-16 10:42:05.000',NULL,NULL),(111,'Giải đấu mới!','Mùa giải 26-27 đã sẵn sàng đón nhận đăng ký.','general','system',NULL,NULL,111,0,NULL,NULL,1,'2026-07-16 10:42:06.000',NULL,NULL),(112,'Giải đấu mới!','Mùa giải 26-27 đã sẵn sàng đón nhận đăng ký.','general','system',NULL,NULL,112,0,NULL,NULL,1,'2026-07-16 10:42:06.000',NULL,NULL),(113,'Giải đấu mới!','Mùa giải 26-27 đã sẵn sàng đón nhận đăng ký.','general','system',NULL,NULL,113,0,NULL,NULL,1,'2026-07-16 10:42:06.000',NULL,NULL),(114,'Giải đấu mới!','Mùa giải 26-27 đã sẵn sàng đón nhận đăng ký.','general','system',NULL,NULL,114,0,NULL,NULL,1,'2026-07-16 10:42:06.000',NULL,NULL),(115,'Giải đấu mới!','Mùa giải 26-27 đã sẵn sàng đón nhận đăng ký.','general','system',NULL,NULL,115,0,NULL,NULL,1,'2026-07-16 10:42:07.000',NULL,NULL),(116,'Giải đấu mới!','Mùa giải 26-27 đã sẵn sàng đón nhận đăng ký.','general','system',NULL,NULL,116,0,NULL,NULL,1,'2026-07-16 10:42:07.000',NULL,NULL),(117,'Thông báo đăng ký giải đấu','Đội vui fc đã đăng ký thành công giải 26-27. Vui lòng hoàn tất thanh toán phí tham dự.','general','system',NULL,38,116,0,NULL,NULL,1,'2026-07-16 11:19:21.000',NULL,NULL),(118,'Thông báo đăng ký giải đấu','Đội Buon fc đã đăng ký thành công giải 26-27. Vui lòng hoàn tất thanh toán phí tham dự.','general','system',NULL,39,117,0,NULL,NULL,1,'2026-07-16 11:19:42.000',NULL,NULL),(119,'Thông báo đăng ký giải đấu','Đội Khoc fc đã đăng ký thành công giải 26-27. Vui lòng hoàn tất thanh toán phí tham dự.','general','system',NULL,40,118,0,NULL,NULL,1,'2026-07-16 11:19:58.000',NULL,NULL),(120,'Thông báo đăng ký giải đấu','Đội Cuoi fc đã đăng ký thành công giải 26-27. Vui lòng hoàn tất thanh toán phí tham dự.','general','system',NULL,41,119,0,NULL,NULL,1,'2026-07-16 11:20:16.000',NULL,NULL),(121,'Giải đấu mới!','Mùa giải Giai moi  đã sẵn sàng đón nhận đăng ký.','general','system',NULL,NULL,6,0,NULL,NULL,1,'2026-07-16 12:02:16.000',NULL,NULL),(122,'Giải đấu mới!','Mùa giải Giai moi  đã sẵn sàng đón nhận đăng ký.','general','system',NULL,NULL,7,0,NULL,NULL,1,'2026-07-16 12:02:19.000',NULL,NULL),(123,'Giải đấu mới!','Mùa giải Giai moi  đã sẵn sàng đón nhận đăng ký.','general','system',NULL,NULL,110,0,NULL,NULL,1,'2026-07-16 12:02:20.000',NULL,NULL),(124,'Giải đấu mới!','Mùa giải Giai moi  đã sẵn sàng đón nhận đăng ký.','general','system',NULL,NULL,111,0,NULL,NULL,1,'2026-07-16 12:02:20.000',NULL,NULL),(125,'Giải đấu mới!','Mùa giải Giai moi  đã sẵn sàng đón nhận đăng ký.','general','system',NULL,NULL,112,0,NULL,NULL,1,'2026-07-16 12:02:20.000',NULL,NULL),(126,'Giải đấu mới!','Mùa giải Giai moi  đã sẵn sàng đón nhận đăng ký.','general','system',NULL,NULL,113,0,NULL,NULL,1,'2026-07-16 12:02:21.000',NULL,NULL),(127,'Giải đấu mới!','Mùa giải Giai moi  đã sẵn sàng đón nhận đăng ký.','general','system',NULL,NULL,114,0,NULL,NULL,1,'2026-07-16 12:02:21.000',NULL,NULL),(128,'Giải đấu mới!','Mùa giải Giai moi  đã sẵn sàng đón nhận đăng ký.','general','system',NULL,NULL,115,0,NULL,NULL,1,'2026-07-16 12:02:21.000',NULL,NULL),(129,'Giải đấu mới!','Mùa giải Giai moi  đã sẵn sàng đón nhận đăng ký.','general','system',NULL,NULL,116,0,NULL,NULL,1,'2026-07-16 12:02:21.000',NULL,NULL),(130,'Giải đấu mới!','Mùa giải Giai moi  đã sẵn sàng đón nhận đăng ký.','general','system',NULL,NULL,117,0,NULL,NULL,1,'2026-07-16 12:02:22.000',NULL,NULL),(131,'Giải đấu mới!','Mùa giải Giai moi  đã sẵn sàng đón nhận đăng ký.','general','system',NULL,NULL,118,0,NULL,NULL,1,'2026-07-16 12:02:22.000',NULL,NULL),(132,'Giải đấu mới!','Mùa giải Giai moi  đã sẵn sàng đón nhận đăng ký.','general','system',NULL,NULL,119,0,NULL,NULL,1,'2026-07-16 12:02:23.000',NULL,NULL),(133,'Thông báo đăng ký giải đấu','Đội Cuoi fc đã đăng ký thành công giải Giai moi . Vui lòng hoàn tất thanh toán phí tham dự.','general','system',NULL,41,119,0,NULL,NULL,1,'2026-07-16 12:08:13.000',NULL,NULL),(134,'Thông báo đăng ký giải đấu','Đội Khoc fc đã đăng ký thành công giải Giai moi . Vui lòng hoàn tất thanh toán phí tham dự.','general','system',NULL,40,118,0,NULL,NULL,1,'2026-07-16 12:08:40.000',NULL,NULL),(135,'Thông báo đăng ký giải đấu','Đội Buon fc đã đăng ký thành công giải Giai moi . Vui lòng hoàn tất thanh toán phí tham dự.','general','system',NULL,39,117,0,NULL,NULL,1,'2026-07-16 12:09:04.000',NULL,NULL),(136,'Thông báo đăng ký giải đấu','Đội vui fc đã đăng ký thành công giải Giai moi . Vui lòng hoàn tất thanh toán phí tham dự.','general','system',NULL,38,116,0,NULL,NULL,1,'2026-07-16 12:09:18.000',NULL,NULL),(137,'Cập nhật luật giải đấu Season Test 01 — 8 teams single group','Giải [ Season Test 01 — 8 teams single group] áp dụng luật: Đội hình 6-11 cầu thủ. Điểm số: Thắng (3) - Hòa (1) - Thua (0). ','general','system',2,NULL,NULL,0,'tournament_rules',1,1,'2026-07-16 13:35:59.273',NULL,NULL),(138,'Cập nhật luật giải đấu asdasdasd','Giải [ asdasdasd] áp dụng luật: Đội hình 7-11 cầu thủ. Điểm số: Thắng (3) - Hòa (1) - Thua (0). ','general','system',24,NULL,NULL,0,'tournament_rules',12,1,'2026-07-16 13:42:33.848',NULL,NULL),(139,'Cập nhật luật giải đấu 26-27','Giải [ 26-27] áp dụng luật: Đội hình 6-11 cầu thủ. Điểm số: Thắng (3) - Hòa (1) - Thua (0). Ghi chú: Giải [ Season Test 01 — 8 teams single group] áp dụng luật: Đội hình 6-11 cầu thủ. Điểm số: Thắng (3) - Hòa (1) - Thua (0). ','general','system',26,NULL,NULL,0,'tournament_rules',13,1,'2026-07-16 13:42:55.010',NULL,NULL),(140,'Thông báo đăng ký giải đấu','Đội fc d đã đăng ký thành công giải Giai moi . Vui lòng hoàn tất thanh toán phí tham dự.','general','system',NULL,33,111,0,NULL,NULL,1,'2026-07-16 14:37:01.000',NULL,NULL),(141,'Thông báo đăng ký giải đấu','Đội fc d đã đăng ký thành công giải Giai moi . Vui lòng hoàn tất thanh toán phí tham dự.','general','system',NULL,33,110,0,NULL,NULL,1,'2026-07-16 14:37:03.000',NULL,NULL),(142,'Thông báo đăng ký giải đấu','Đội fc d đã đăng ký thành công giải Giai moi . Vui lòng hoàn tất thanh toán phí tham dự.','general','system',NULL,33,111,0,NULL,NULL,1,'2026-07-16 14:37:54.000',NULL,NULL),(143,'Thông báo đăng ký giải đấu','Đội fc d đã đăng ký thành công giải Giai moi . Vui lòng hoàn tất thanh toán phí tham dự.','general','system',NULL,33,110,0,NULL,NULL,1,'2026-07-16 14:37:56.000',NULL,NULL),(144,'Thông báo đăng ký giải đấu','Đội fc d đã đăng ký thành công giải 26-27. Vui lòng hoàn tất thanh toán phí tham dự.','general','system',NULL,33,111,0,NULL,NULL,1,'2026-07-16 14:39:17.000',NULL,NULL),(145,'Thông báo đăng ký giải đấu','Đội fc d đã đăng ký thành công giải 26-27. Vui lòng hoàn tất thanh toán phí tham dự.','general','system',NULL,33,110,0,NULL,NULL,1,'2026-07-16 14:39:18.000',NULL,NULL);
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
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
  PRIMARY KEY (`id`),
  KEY `payments_season_team_id_fkey` (`season_team_id`),
  CONSTRAINT `payments_season_team_id_fkey` FOREIGN KEY (`season_team_id`) REFERENCES `season_teams` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (2,2,5000000.00,'confirmed','TXN-1781843374212-2','2024-02-10 00:00:00.000','2024-02-11 00:00:00.000',1,1,'2026-06-19 04:29:34.215','2026-06-19 04:29:34.215',NULL),(3,3,5000000.00,'confirmed','TXN-1781843374792-3','2024-02-10 00:00:00.000','2024-02-11 00:00:00.000',1,1,'2026-06-19 04:29:34.794','2026-06-19 04:29:34.794',NULL),(4,4,5000000.00,'confirmed','TXN-1781843375276-4','2024-02-10 00:00:00.000','2024-02-11 00:00:00.000',1,1,'2026-06-19 04:29:35.277','2026-06-19 04:29:35.277',NULL),(5,5,5000000.00,'confirmed','TXN-1781843375710-5','2024-02-10 00:00:00.000','2024-02-11 00:00:00.000',1,1,'2026-06-19 04:29:35.710','2026-06-19 04:29:35.710',NULL),(6,6,5000000.00,'confirmed','TXN-1781843376091-6','2024-02-10 00:00:00.000','2024-02-11 00:00:00.000',1,1,'2026-06-19 04:29:36.091','2026-06-19 04:29:36.091',NULL),(7,7,5000000.00,'confirmed','TXN-1781843376463-7','2024-02-10 00:00:00.000','2024-02-11 00:00:00.000',1,1,'2026-06-19 04:29:36.464','2026-06-19 04:29:36.464',NULL),(8,8,5000000.00,'confirmed','TXN-1781843376824-8','2024-02-10 00:00:00.000','2024-02-11 00:00:00.000',1,1,'2026-06-19 04:29:36.825','2026-06-19 04:29:36.825',NULL),(15,186,0.00,'confirmed','TXNA89CEECB','2026-07-08 03:04:46.000',NULL,NULL,1,'2026-07-08 03:04:42.000',NULL,NULL),(17,188,0.00,'confirmed','TXNDD357687','2026-07-16 09:12:10.000',NULL,NULL,1,'2026-07-08 03:05:19.000',NULL,NULL),(19,190,0.00,'confirmed','TXNAA063A7E','2026-07-08 11:21:26.000',NULL,NULL,1,'2026-07-08 11:21:18.000',NULL,NULL),(23,194,0.00,'confirmed','TXN9AB8B114','2026-07-11 05:08:17.000',NULL,NULL,1,'2026-07-11 05:08:14.000',NULL,NULL),(24,188,0.00,'confirmed','TXNDD357687','2026-07-16 09:12:10.000',NULL,NULL,1,'2026-07-16 08:55:52.000',NULL,NULL),(25,188,0.00,'confirmed','TXNDD357687','2026-07-16 09:12:10.000','2026-07-16 08:56:59.000',NULL,1,'2026-07-16 08:56:08.000','2026-07-16 08:56:59.000',NULL),(26,188,0.00,'confirmed','TXNDD357687','2026-07-16 09:12:10.000',NULL,NULL,1,'2026-07-16 09:07:15.000',NULL,NULL),(31,199,100.00,'confirmed','TXN2A30865C','2026-07-16 12:08:26.000',NULL,NULL,1,'2026-07-16 12:08:13.000',NULL,NULL),(32,200,100.00,'confirmed','TXN83F89A5B','2026-07-16 12:08:43.000',NULL,NULL,1,'2026-07-16 12:08:40.000',NULL,NULL),(33,201,100.00,'confirmed','TXNCD45B2DE','2026-07-16 12:09:07.000',NULL,NULL,1,'2026-07-16 12:09:04.000',NULL,NULL),(34,202,100.00,'confirmed','TXN38A80E17','2026-07-16 12:09:20.000',NULL,NULL,1,'2026-07-16 12:09:18.000',NULL,NULL),(35,203,100.00,'confirmed','NEONFE5461','2026-07-16 14:38:24.000',NULL,NULL,1,'2026-07-16 14:37:01.000',NULL,NULL),(36,203,100.00,'confirmed','NEONFE5461','2026-07-16 14:38:24.000',NULL,NULL,1,'2026-07-16 14:37:54.000',NULL,NULL),(37,204,0.00,'confirmed',NULL,NULL,'2026-10-15 00:57:16.000',NULL,1,'2026-07-16 14:39:17.000','2026-10-15 00:57:16.000',NULL);
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
  `status` enum('draft','in_progress','locked') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'draft',
  `min_rest_days_per_team` int NOT NULL DEFAULT '3',
  `legs` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `phases_season_id_fkey` (`season_id`),
  CONSTRAINT `phases_season_id_fkey` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phases`
--

LOCK TABLES `phases` WRITE;
/*!40000 ALTER TABLE `phases` DISABLE KEYS */;
INSERT INTO `phases` VALUES (1,1,'Vòng Bảng','group_stage','round_robin',1,'2024-03-01 00:00:00.000','2024-04-15 00:00:00.000',1,'2026-06-19 04:29:36.924','2026-06-19 04:29:36.924','draft',3,1),(2,1,'Tứ Kết','quarter_final','knockout',2,'2024-04-20 00:00:00.000','2024-04-30 00:00:00.000',1,'2026-06-19 04:29:36.934','2026-06-19 04:29:36.934','draft',3,1),(3,1,'Bán Kết','semi_final','knockout',3,'2024-05-05 00:00:00.000','2024-05-15 00:00:00.000',1,'2026-06-19 04:29:36.943','2026-06-19 04:29:36.943','draft',3,1),(4,1,'Tranh Hạng Ba','third_place','knockout',4,'2024-06-25 00:00:00.000','2024-06-25 00:00:00.000',1,'2026-06-19 04:29:36.952','2026-06-19 04:29:36.952','draft',3,1),(5,1,'Chung Kết','final','knockout',5,'2024-06-30 00:00:00.000','2024-06-30 00:00:00.000',1,'2026-06-19 04:29:36.960','2026-06-19 04:29:36.960','draft',3,1),(6,2,'Vòng bảng','group_stage','round_robin',1,'2025-03-01 00:00:00.000','2025-05-01 00:00:00.000',1,'2026-06-20 15:30:23.319','2026-06-20 15:30:23.319','draft',3,1),(7,3,'Vòng bảng','group_stage','round_robin',1,'2025-06-01 00:00:00.000','2025-08-01 00:00:00.000',1,'2026-06-20 15:30:23.453','2026-06-20 15:30:23.453','draft',1,1),(8,4,'Vòng bảng','group_stage','round_robin',1,'2025-09-01 00:00:00.000','2025-11-01 00:00:00.000',1,'2026-06-20 15:30:23.551','2026-06-20 15:30:23.551','draft',3,1),(9,5,'Vòng bảng','group_stage','round_robin',1,'2025-12-01 00:00:00.000','2026-02-01 00:00:00.000',1,'2026-06-20 15:30:23.676','2026-06-20 15:30:23.676','draft',3,1),(10,5,'Bán kết','semi_final','knockout',2,'2025-12-01 00:00:00.000','2026-02-01 00:00:00.000',1,'2026-06-20 15:30:23.718','2026-06-20 15:30:23.718','draft',3,1),(11,5,'Chung kết','final','knockout',3,'2025-12-01 00:00:00.000','2026-02-01 00:00:00.000',1,'2026-06-20 15:30:23.729','2026-06-20 15:30:23.729','draft',3,1),(64,26,'Vòng Bảng','group_stage','round_robin',1,NULL,NULL,1,'2026-07-16 12:31:44.250','2026-07-16 12:31:44.250','draft',3,1),(65,27,'Vòng Bảng','group_stage','round_robin',1,NULL,NULL,1,'2026-07-16 12:33:17.252','2026-07-16 12:33:17.252','draft',3,1),(66,27,'Chung Kết','final','knockout',5,NULL,NULL,1,'2026-07-16 12:39:23.716','2026-07-16 12:39:23.716','draft',3,1),(67,26,'Vòng Bảng','group_stage','knockout',1,NULL,NULL,1,'2026-11-04 23:12:13.975','2026-11-04 23:12:13.975','draft',3,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_statistics`
--

LOCK TABLES `player_statistics` WRITE;
/*!40000 ALTER TABLE `player_statistics` DISABLE KEYS */;
INSERT INTO `player_statistics` VALUES (1,1,1,1,2,2,0,0,0,180,0,0,'2026-06-19 04:29:37.378','2026-06-19 04:29:37.519'),(2,5,1,1,2,1,0,1,0,180,1,0,'2026-06-19 04:29:37.392','2026-06-20 06:34:24.712'),(3,6,1,1,1,1,0,0,0,90,0,0,'2026-06-19 04:29:37.404','2026-06-19 04:29:37.404'),(4,7,1,1,3,0,0,3,0,270,3,1,'2026-06-19 04:29:37.412','2026-06-20 06:17:11.868'),(5,22,2,1,1,0,0,1,0,90,1,0,'2026-06-19 04:29:37.425','2026-06-19 04:29:37.425'),(6,4,1,1,2,1,0,1,0,180,1,0,'2026-06-19 04:29:37.526','2026-06-20 06:17:11.760'),(7,8,1,1,2,2,0,0,0,180,0,0,'2026-06-19 04:29:37.533','2026-06-20 06:17:11.778'),(8,9,1,1,1,1,0,1,0,90,1,0,'2026-06-19 04:29:37.540','2026-06-19 04:29:37.540'),(9,28,3,1,2,1,0,1,0,180,1,0,'2026-06-19 04:29:37.547','2026-06-20 06:17:12.023'),(10,33,3,1,2,1,0,1,0,180,1,0,'2026-06-19 04:29:37.553','2026-06-20 06:34:25.058'),(11,3,1,1,1,1,0,0,0,90,0,0,'2026-06-19 04:29:37.631','2026-06-19 04:29:37.631'),(12,10,1,1,3,2,0,1,0,270,1,0,'2026-06-19 04:29:37.640','2026-06-20 06:34:24.522'),(13,11,1,1,2,1,0,1,0,180,1,0,'2026-06-19 04:29:37.648','2026-06-20 06:17:11.641'),(14,37,4,1,4,5,0,0,0,360,0,0,'2026-06-19 04:29:37.658','2026-06-20 06:34:24.733'),(15,46,4,1,3,3,0,1,0,270,1,0,'2026-06-19 04:29:37.669','2026-06-20 06:34:24.946'),(16,19,2,1,1,1,0,0,0,90,0,0,'2026-06-19 04:29:37.739','2026-06-19 04:29:37.739'),(17,17,2,1,2,1,0,1,0,180,1,0,'2026-06-19 04:29:37.825','2026-06-20 06:17:12.127'),(18,21,2,1,2,1,0,1,0,180,1,0,'2026-06-19 04:29:37.833','2026-06-20 06:17:11.676'),(19,23,2,1,1,1,0,0,0,90,0,0,'2026-06-19 04:29:37.838','2026-06-19 04:29:37.838'),(20,24,2,1,2,2,0,0,0,180,0,0,'2026-06-19 04:29:37.844','2026-06-20 06:17:12.014'),(21,40,4,1,2,2,0,0,0,180,0,0,'2026-06-19 04:29:37.852','2026-06-20 06:17:11.882'),(22,41,4,1,4,2,0,2,0,360,2,0,'2026-06-19 04:29:37.860','2026-06-20 06:34:25.076'),(23,30,3,1,2,2,0,0,0,180,0,0,'2026-06-19 04:29:37.962','2026-06-20 06:17:12.040'),(24,39,4,1,2,2,0,0,0,180,0,0,'2026-06-19 04:29:37.983','2026-06-20 06:17:11.875'),(25,47,4,1,3,3,0,0,0,270,0,0,'2026-06-19 04:29:37.992','2026-06-20 06:17:12.281'),(26,56,5,1,2,1,0,1,0,180,1,0,'2026-06-19 04:29:38.111','2026-06-20 06:34:25.364'),(27,58,5,1,3,2,0,1,0,270,1,0,'2026-06-19 04:29:38.121','2026-06-20 06:34:25.250'),(28,60,5,1,2,0,0,2,0,180,2,0,'2026-06-19 04:29:38.131','2026-06-20 06:34:25.268'),(29,61,6,1,2,2,0,0,0,180,0,0,'2026-06-19 04:29:38.140','2026-06-20 06:34:25.610'),(30,70,6,1,2,2,0,0,0,180,0,0,'2026-06-19 04:29:38.150','2026-06-19 04:29:38.462'),(31,59,5,1,2,0,0,2,0,180,2,0,'2026-06-19 04:29:38.255','2026-06-20 06:34:25.259'),(32,74,7,1,2,2,0,0,0,180,0,0,'2026-06-19 04:29:38.263','2026-06-20 06:34:25.717'),(33,75,7,1,1,1,0,0,0,90,0,0,'2026-06-19 04:29:38.270','2026-06-19 04:29:38.270'),(34,80,7,1,2,2,0,0,0,180,0,0,'2026-06-19 04:29:38.275','2026-06-20 06:34:25.387'),(35,84,7,1,1,0,0,1,0,90,1,0,'2026-06-19 04:29:38.282','2026-06-19 04:29:38.282'),(36,89,8,1,3,3,0,1,0,270,1,0,'2026-06-19 04:29:38.326','2026-06-20 06:34:25.468'),(37,96,8,1,3,1,0,2,0,270,2,0,'2026-06-19 04:29:38.333','2026-06-20 06:34:25.650'),(38,66,6,1,1,1,0,0,0,90,0,0,'2026-06-19 04:29:38.387','2026-06-19 04:29:38.387'),(39,86,8,1,2,2,0,0,0,180,0,0,'2026-06-19 04:29:38.471','2026-06-20 06:34:25.726'),(40,95,8,1,1,1,0,0,0,90,0,0,'2026-06-19 04:29:38.491','2026-06-19 04:29:38.491'),(41,77,7,1,1,0,0,1,0,90,1,0,'2026-06-19 04:29:38.555','2026-06-19 04:29:38.555'),(42,82,7,1,2,2,0,0,0,180,0,0,'2026-06-19 04:29:38.561','2026-06-20 06:34:25.395'),(43,83,7,1,1,1,0,0,0,90,0,0,'2026-06-19 04:29:38.568','2026-06-19 04:29:38.568'),(44,2,1,1,2,2,0,0,0,180,0,0,'2026-06-20 06:17:11.627','2026-06-20 06:34:24.599'),(45,12,1,1,2,2,0,1,0,180,1,0,'2026-06-20 06:17:11.650','2026-06-20 06:34:24.724'),(46,16,2,1,3,2,0,1,0,270,1,0,'2026-06-20 06:17:11.665','2026-06-20 06:34:24.916'),(47,44,4,1,2,0,0,2,0,180,2,0,'2026-06-20 06:17:11.889','2026-06-20 06:34:25.099'),(48,45,4,1,2,1,0,1,0,180,1,0,'2026-06-20 06:17:11.896','2026-06-20 06:17:12.273'),(49,14,2,1,1,2,0,0,0,90,0,0,'2026-06-20 06:17:11.997','2026-06-20 06:17:11.997'),(50,29,3,1,2,1,0,2,0,180,2,0,'2026-06-20 06:17:12.031','2026-06-20 06:17:12.237'),(51,31,3,1,2,2,0,1,0,180,1,0,'2026-06-20 06:17:12.049','2026-06-20 06:34:24.816'),(52,13,2,1,2,2,0,0,0,180,0,0,'2026-06-20 06:17:12.118','2026-06-20 06:34:24.907'),(53,18,2,1,1,1,0,0,0,90,0,0,'2026-06-20 06:17:12.135','2026-06-20 06:17:12.135'),(54,34,3,1,1,0,0,1,0,90,1,0,'2026-06-20 06:17:12.245','2026-06-20 06:17:12.245'),(55,48,4,1,2,1,0,1,0,180,1,0,'2026-06-20 06:17:12.289','2026-06-20 06:34:24.955'),(56,27,3,1,1,1,0,0,0,90,0,0,'2026-06-20 06:34:24.626','2026-06-20 06:34:24.626'),(57,36,3,1,2,1,0,1,0,180,1,0,'2026-06-20 06:34:24.636','2026-06-20 06:34:24.824'),(58,26,3,1,1,1,0,0,0,90,0,0,'2026-06-20 06:34:24.807','2026-06-20 06:34:24.807'),(59,38,4,1,1,1,0,0,0,90,0,0,'2026-06-20 06:34:24.926','2026-06-20 06:34:24.926'),(60,25,3,1,1,1,0,0,0,90,0,0,'2026-06-20 06:34:25.050','2026-06-20 06:34:25.050'),(61,35,3,1,1,0,0,1,0,90,1,0,'2026-06-20 06:34:25.068','2026-06-20 06:34:25.068'),(62,42,4,1,1,1,0,0,0,90,0,0,'2026-06-20 06:34:25.084','2026-06-20 06:34:25.084'),(63,43,4,1,1,1,0,0,0,90,0,0,'2026-06-20 06:34:25.091','2026-06-20 06:34:25.091'),(64,52,5,1,2,1,0,1,0,180,1,0,'2026-06-20 06:34:25.234','2026-06-20 06:34:25.461'),(65,54,5,1,1,1,0,0,0,90,0,0,'2026-06-20 06:34:25.241','2026-06-20 06:34:25.241'),(66,65,6,1,1,1,0,0,0,90,0,0,'2026-06-20 06:34:25.278','2026-06-20 06:34:25.278'),(67,69,6,1,1,1,0,0,0,90,0,0,'2026-06-20 06:34:25.286','2026-06-20 06:34:25.286'),(68,76,7,1,1,1,0,0,0,90,0,0,'2026-06-20 06:34:25.371','2026-06-20 06:34:25.371'),(69,78,7,1,2,3,0,0,0,180,0,0,'2026-06-20 06:34:25.378','2026-06-20 06:34:25.529'),(70,49,5,1,1,1,0,0,0,90,0,0,'2026-06-20 06:34:25.453','2026-06-20 06:34:25.453'),(71,62,6,1,1,1,0,0,0,90,0,0,'2026-06-20 06:34:25.618','2026-06-20 06:34:25.618'),(72,64,6,1,1,0,0,1,0,90,1,0,'2026-06-20 06:34:25.627','2026-06-20 06:34:25.627'),(73,71,6,1,1,0,0,1,0,90,1,0,'2026-06-20 06:34:25.634','2026-06-20 06:34:25.634'),(74,94,8,1,1,1,0,0,0,90,0,0,'2026-06-20 06:34:25.642','2026-06-20 06:34:25.642'),(75,88,8,1,1,2,0,0,0,90,0,0,'2026-06-20 06:34:25.734','2026-06-20 06:34:25.734');
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
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `players_user_id_key` (`user_id`),
  CONSTRAINT `players_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=116 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `players`
--

LOCK TABLES `players` WRITE;
/*!40000 ALTER TABLE `players` DISABLE KEYS */;
INSERT INTO `players` VALUES (1,'1997-07-17 00:00:00.000','goalkeeper',165.84,61.86,'Việt Nam',NULL,0,'2026-06-19 04:29:33.026','2026-06-19 04:29:33.026',NULL,7),(2,'1997-11-04 17:00:00.000','defender',167.28,72.83,'Việt Nam',NULL,1,'2026-06-19 04:29:33.100','2026-06-19 04:29:33.100',NULL,8),(3,'1993-07-24 17:00:00.000','defender',166.90,62.50,'Việt Nam',NULL,1,'2026-06-19 04:29:33.142','2026-06-19 04:29:33.142',NULL,9),(4,'1995-05-25 17:00:00.000','defender',167.95,72.98,'Việt Nam',NULL,1,'2026-06-19 04:29:33.182','2026-06-19 04:29:33.182',NULL,10),(5,'1999-09-12 17:00:00.000','defender',167.82,78.40,'Việt Nam',NULL,1,'2026-06-19 04:29:33.216','2026-06-19 04:29:33.216',NULL,11),(6,'1991-04-30 17:00:00.000','midfielder',177.90,72.58,'Việt Nam',NULL,1,'2026-06-19 04:29:33.247','2026-06-19 04:29:33.247',NULL,12),(7,'1991-05-17 17:00:00.000','midfielder',171.06,74.32,'Việt Nam',NULL,1,'2026-06-19 04:29:33.284','2026-06-19 04:29:33.284',NULL,13),(8,'1992-01-22 17:00:00.000','midfielder',178.93,79.95,'Việt Nam',NULL,1,'2026-06-19 04:29:33.320','2026-06-19 04:29:33.320',NULL,14),(9,'1998-01-03 17:00:00.000','midfielder',165.94,67.10,'Việt Nam',NULL,1,'2026-06-19 04:29:33.356','2026-06-19 04:29:33.356',NULL,15),(10,'1993-09-04 17:00:00.000','forward',168.32,64.60,'Việt Nam',NULL,1,'2026-06-19 04:29:33.388','2026-06-19 04:29:33.388',NULL,16),(11,'1999-12-02 17:00:00.000','forward',179.78,64.38,'Việt Nam',NULL,1,'2026-06-19 04:29:33.424','2026-06-19 04:29:33.424',NULL,17),(12,'1999-03-06 17:00:00.000','forward',167.92,75.19,'Việt Nam',NULL,1,'2026-06-19 04:29:33.461','2026-06-19 04:29:33.461',NULL,18),(13,'1995-02-01 17:00:00.000','goalkeeper',178.38,79.30,'Việt Nam',NULL,1,'2026-06-19 04:29:33.797','2026-06-19 04:29:33.797',NULL,20),(14,'1999-09-02 17:00:00.000','defender',175.72,75.28,'Việt Nam',NULL,1,'2026-06-19 04:29:33.836','2026-06-19 04:29:33.836',NULL,21),(15,'1998-06-09 17:00:00.000','defender',177.35,61.05,'Việt Nam',NULL,1,'2026-06-19 04:29:33.872','2026-06-19 04:29:33.872',NULL,22),(16,'1997-03-24 17:00:00.000','defender',178.53,60.62,'Việt Nam',NULL,1,'2026-06-19 04:29:33.907','2026-06-19 04:29:33.907',NULL,23),(17,'1998-03-09 17:00:00.000','defender',184.26,62.80,'Việt Nam',NULL,1,'2026-06-19 04:29:33.938','2026-06-19 04:29:33.938',NULL,24),(18,'1998-12-04 17:00:00.000','midfielder',167.91,70.06,'Việt Nam',NULL,1,'2026-06-19 04:29:33.968','2026-06-19 04:29:33.968',NULL,25),(19,'1996-03-09 17:00:00.000','midfielder',184.64,60.57,'Việt Nam',NULL,1,'2026-06-19 04:29:34.001','2026-06-19 04:29:34.001',NULL,26),(20,'1999-10-08 17:00:00.000','midfielder',183.29,61.76,'Việt Nam',NULL,1,'2026-06-19 04:29:34.036','2026-06-19 04:29:34.036',NULL,27),(21,'1992-05-15 17:00:00.000','midfielder',182.64,73.49,'Việt Nam',NULL,1,'2026-06-19 04:29:34.066','2026-06-19 04:29:34.066',NULL,28),(22,'1998-10-26 17:00:00.000','forward',182.03,63.95,'Việt Nam',NULL,1,'2026-06-19 04:29:34.101','2026-06-19 04:29:34.101',NULL,29),(23,'1999-12-25 17:00:00.000','forward',174.46,62.28,'Việt Nam',NULL,1,'2026-06-19 04:29:34.136','2026-06-19 04:29:34.136',NULL,30),(24,'1993-11-01 17:00:00.000','forward',173.35,62.51,'Việt Nam',NULL,1,'2026-06-19 04:29:34.170','2026-06-19 04:29:34.170',NULL,31),(25,'1990-01-24 17:00:00.000','goalkeeper',182.18,72.49,'Việt Nam',NULL,1,'2026-06-19 04:29:34.407','2026-06-19 04:29:34.407',NULL,33),(26,'1993-05-25 17:00:00.000','defender',176.34,63.04,'Việt Nam',NULL,1,'2026-06-19 04:29:34.440','2026-06-19 04:29:34.440',NULL,34),(27,'1991-10-24 17:00:00.000','defender',174.25,74.70,'Việt Nam',NULL,1,'2026-06-19 04:29:34.477','2026-06-19 04:29:34.477',NULL,35),(28,'1998-07-03 17:00:00.000','defender',169.72,79.21,'Việt Nam',NULL,1,'2026-06-19 04:29:34.512','2026-06-19 04:29:34.512',NULL,36),(29,'1991-02-21 17:00:00.000','defender',169.96,79.22,'Việt Nam',NULL,1,'2026-06-19 04:29:34.544','2026-06-19 04:29:34.544',NULL,37),(30,'1992-11-04 17:00:00.000','midfielder',178.00,66.71,'Việt Nam',NULL,1,'2026-06-19 04:29:34.575','2026-06-19 04:29:34.575',NULL,38),(31,'1992-09-24 17:00:00.000','midfielder',177.43,79.96,'Việt Nam',NULL,1,'2026-06-19 04:29:34.609','2026-06-19 04:29:34.609',NULL,39),(32,'1994-06-10 17:00:00.000','midfielder',172.02,64.33,'Việt Nam',NULL,1,'2026-06-19 04:29:34.642','2026-06-19 04:29:34.642',NULL,40),(33,'1996-09-24 17:00:00.000','midfielder',184.49,66.91,'Việt Nam',NULL,1,'2026-06-19 04:29:34.671','2026-06-19 04:29:34.671',NULL,41),(34,'1996-09-15 17:00:00.000','forward',170.58,64.37,'Việt Nam',NULL,1,'2026-06-19 04:29:34.705','2026-06-19 04:29:34.705',NULL,42),(35,'1991-09-24 17:00:00.000','forward',184.99,63.73,'Việt Nam',NULL,1,'2026-06-19 04:29:34.731','2026-06-19 04:29:34.731',NULL,43),(36,'1990-01-23 17:00:00.000','forward',167.42,67.75,'Việt Nam',NULL,1,'2026-06-19 04:29:34.760','2026-06-19 04:29:34.760',NULL,44),(37,'1994-11-20 17:00:00.000','goalkeeper',178.48,76.88,'Việt Nam',NULL,1,'2026-06-19 04:29:34.961','2026-06-19 04:29:34.961',NULL,46),(38,'1994-11-15 17:00:00.000','defender',171.89,68.48,'Việt Nam',NULL,1,'2026-06-19 04:29:34.992','2026-06-19 04:29:34.992',NULL,47),(39,'1990-08-14 17:00:00.000','defender',179.20,60.74,'Việt Nam',NULL,1,'2026-06-19 04:29:35.014','2026-06-19 04:29:35.014',NULL,48),(40,'1995-10-11 17:00:00.000','defender',166.18,78.71,'Việt Nam',NULL,1,'2026-06-19 04:29:35.035','2026-06-19 04:29:35.035',NULL,49),(41,'1992-04-23 17:00:00.000','defender',176.35,79.95,'Việt Nam',NULL,1,'2026-06-19 04:29:35.057','2026-06-19 04:29:35.057',NULL,50),(42,'1990-11-09 17:00:00.000','midfielder',170.01,75.47,'Việt Nam',NULL,1,'2026-06-19 04:29:35.079','2026-06-19 04:29:35.079',NULL,51),(43,'1999-05-04 17:00:00.000','midfielder',172.57,70.83,'Việt Nam',NULL,1,'2026-06-19 04:29:35.102','2026-06-19 04:29:35.102',NULL,52),(44,'1993-08-13 17:00:00.000','midfielder',167.80,61.21,'Việt Nam',NULL,1,'2026-06-19 04:29:35.129','2026-06-19 04:29:35.129',NULL,53),(45,'1990-01-06 17:00:00.000','midfielder',177.45,71.97,'Việt Nam',NULL,1,'2026-06-19 04:29:35.159','2026-06-19 04:29:35.159',NULL,54),(46,'1991-06-01 17:00:00.000','forward',169.66,72.14,'Việt Nam',NULL,1,'2026-06-19 04:29:35.189','2026-06-19 04:29:35.189',NULL,55),(47,'1992-09-09 17:00:00.000','forward',178.07,75.66,'Việt Nam',NULL,1,'2026-06-19 04:29:35.220','2026-06-19 04:29:35.220',NULL,56),(48,'1996-08-27 17:00:00.000','forward',166.88,73.72,'Việt Nam',NULL,1,'2026-06-19 04:29:35.251','2026-06-19 04:29:35.251',NULL,57),(49,'1997-06-25 17:00:00.000','goalkeeper',165.51,69.83,'Việt Nam',NULL,1,'2026-06-19 04:29:35.420','2026-06-19 04:29:35.420',NULL,59),(50,'1996-11-30 17:00:00.000','defender',171.87,75.07,'Việt Nam',NULL,1,'2026-06-19 04:29:35.449','2026-06-19 04:29:35.449',NULL,60),(51,'1991-02-23 17:00:00.000','defender',179.61,70.56,'Việt Nam',NULL,1,'2026-06-19 04:29:35.477','2026-06-19 04:29:35.477',NULL,61),(52,'1997-08-04 17:00:00.000','defender',179.86,73.12,'Việt Nam',NULL,1,'2026-06-19 04:29:35.506','2026-06-19 04:29:35.506',NULL,62),(53,'1999-03-27 17:00:00.000','defender',167.98,61.10,'Việt Nam',NULL,1,'2026-06-19 04:29:35.534','2026-06-19 04:29:35.534',NULL,63),(54,'1995-05-01 17:00:00.000','midfielder',181.82,69.80,'Việt Nam',NULL,1,'2026-06-19 04:29:35.562','2026-06-19 04:29:35.562',NULL,64),(55,'1999-12-06 17:00:00.000','midfielder',174.31,60.28,'Việt Nam',NULL,1,'2026-06-19 04:29:35.590','2026-06-19 04:29:35.590',NULL,65),(56,'1995-05-19 17:00:00.000','midfielder',183.92,73.79,'Việt Nam',NULL,1,'2026-06-19 04:29:35.613','2026-06-19 04:29:35.613',NULL,66),(57,'1999-09-12 17:00:00.000','midfielder',175.83,73.56,'Việt Nam',NULL,1,'2026-06-19 04:29:35.635','2026-06-19 04:29:35.635',NULL,67),(58,'1994-02-28 17:00:00.000','forward',181.50,71.95,'Việt Nam',NULL,1,'2026-06-19 04:29:35.658','2026-06-19 04:29:35.658',NULL,68),(59,'1996-11-19 17:00:00.000','forward',165.14,63.58,'Việt Nam',NULL,1,'2026-06-19 04:29:35.679','2026-06-19 04:29:35.679',NULL,69),(60,'1996-01-05 17:00:00.000','forward',167.04,72.41,'Việt Nam',NULL,1,'2026-06-19 04:29:35.695','2026-06-19 04:29:35.695',NULL,70),(61,'1998-03-08 17:00:00.000','goalkeeper',184.09,65.68,'Việt Nam',NULL,1,'2026-06-19 04:29:35.816','2026-06-19 04:29:35.816',NULL,72),(62,'1997-12-20 17:00:00.000','defender',165.22,65.15,'Việt Nam',NULL,1,'2026-06-19 04:29:35.835','2026-06-19 04:29:35.835',NULL,73),(63,'1991-03-02 17:00:00.000','defender',171.01,77.67,'Việt Nam',NULL,1,'2026-06-19 04:29:35.858','2026-06-19 04:29:35.858',NULL,74),(64,'1994-10-08 17:00:00.000','defender',182.32,70.63,'Việt Nam',NULL,1,'2026-06-19 04:29:35.884','2026-06-19 04:29:35.884',NULL,75),(65,'1992-09-11 17:00:00.000','defender',181.69,78.33,'Việt Nam',NULL,1,'2026-06-19 04:29:35.910','2026-06-19 04:29:35.910',NULL,76),(66,'1997-05-15 17:00:00.000','midfielder',178.23,62.74,'Việt Nam',NULL,1,'2026-06-19 04:29:35.946','2026-06-19 04:29:35.946',NULL,77),(67,'1998-04-21 17:00:00.000','midfielder',166.49,72.39,'Việt Nam',NULL,1,'2026-06-19 04:29:35.973','2026-06-19 04:29:35.973',NULL,78),(68,'1995-12-05 17:00:00.000','midfielder',168.05,79.43,'Việt Nam',NULL,1,'2026-06-19 04:29:36.000','2026-06-19 04:29:36.000',NULL,79),(69,'1991-12-05 17:00:00.000','midfielder',183.19,67.00,'Việt Nam',NULL,1,'2026-06-19 04:29:36.023','2026-06-19 04:29:36.023',NULL,80),(70,'1998-11-23 17:00:00.000','forward',171.17,72.27,'Việt Nam',NULL,1,'2026-06-19 04:29:36.040','2026-06-19 04:29:36.040',NULL,81),(71,'1992-05-03 17:00:00.000','forward',167.84,79.80,'Việt Nam',NULL,1,'2026-06-19 04:29:36.056','2026-06-19 04:29:36.056',NULL,82),(72,'1999-07-12 17:00:00.000','forward',182.86,66.34,'Việt Nam',NULL,1,'2026-06-19 04:29:36.076','2026-06-19 04:29:36.076',NULL,83),(73,'1997-01-02 17:00:00.000','goalkeeper',183.75,76.94,'Việt Nam',NULL,1,'2026-06-19 04:29:36.190','2026-06-19 04:29:36.190',NULL,85),(74,'1993-01-14 17:00:00.000','defender',179.78,67.22,'Việt Nam',NULL,1,'2026-06-19 04:29:36.212','2026-06-19 04:29:36.212',NULL,86),(75,'1994-04-07 17:00:00.000','defender',180.15,77.97,'Việt Nam',NULL,1,'2026-06-19 04:29:36.235','2026-06-19 04:29:36.235',NULL,87),(76,'1998-12-21 17:00:00.000','defender',180.14,72.24,'Việt Nam',NULL,1,'2026-06-19 04:29:36.255','2026-06-19 04:29:36.255',NULL,88),(77,'1994-06-08 17:00:00.000','defender',176.98,68.10,'Việt Nam',NULL,1,'2026-06-19 04:29:36.281','2026-06-19 04:29:36.281',NULL,89),(78,'1998-08-18 17:00:00.000','midfielder',178.35,61.05,'Việt Nam',NULL,1,'2026-06-19 04:29:36.308','2026-06-19 04:29:36.308',NULL,90),(79,'1998-01-20 17:00:00.000','midfielder',180.88,75.70,'Việt Nam',NULL,1,'2026-06-19 04:29:36.333','2026-06-19 04:29:36.333',NULL,91),(80,'1997-10-11 17:00:00.000','midfielder',184.18,76.74,'Việt Nam',NULL,1,'2026-06-19 04:29:36.360','2026-06-19 04:29:36.360',NULL,92),(81,'1994-06-03 17:00:00.000','midfielder',179.58,77.76,'Việt Nam',NULL,1,'2026-06-19 04:29:36.385','2026-06-19 04:29:36.385',NULL,93),(82,'1995-04-06 17:00:00.000','forward',167.61,76.56,'Việt Nam',NULL,1,'2026-06-19 04:29:36.409','2026-06-19 04:29:36.409',NULL,94),(83,'1993-02-03 17:00:00.000','forward',176.62,68.71,'Việt Nam',NULL,1,'2026-06-19 04:29:36.431','2026-06-19 04:29:36.431',NULL,95),(84,'1992-02-29 17:00:00.000','forward',168.19,66.61,'Việt Nam',NULL,1,'2026-06-19 04:29:36.448','2026-06-19 04:29:36.448',NULL,96),(85,'1994-11-24 17:00:00.000','goalkeeper',170.06,69.90,'Việt Nam',NULL,1,'2026-06-19 04:29:36.561','2026-06-19 04:29:36.561',NULL,98),(86,'1995-07-16 17:00:00.000','defender',172.89,79.83,'Việt Nam',NULL,1,'2026-06-19 04:29:36.581','2026-06-19 04:29:36.581',NULL,99),(87,'1991-06-10 17:00:00.000','defender',182.08,79.38,'Việt Nam',NULL,1,'2026-06-19 04:29:36.598','2026-06-19 04:29:36.598',NULL,100),(88,'1992-11-27 17:00:00.000','defender',177.22,70.20,'Việt Nam',NULL,1,'2026-06-19 04:29:36.615','2026-06-19 04:29:36.615',NULL,101),(89,'1992-04-19 17:00:00.000','defender',182.86,67.09,'Việt Nam',NULL,1,'2026-06-19 04:29:36.634','2026-06-19 04:29:36.634',NULL,102),(90,'1996-09-19 17:00:00.000','midfielder',168.02,60.02,'Việt Nam',NULL,1,'2026-06-19 04:29:36.652','2026-06-19 04:29:36.652',NULL,103),(91,'1995-03-09 17:00:00.000','midfielder',174.83,69.92,'Việt Nam',NULL,1,'2026-06-19 04:29:36.674','2026-06-19 04:29:36.674',NULL,104),(92,'1998-04-17 17:00:00.000','midfielder',172.25,78.61,'Việt Nam',NULL,1,'2026-06-19 04:29:36.697','2026-06-19 04:29:36.697',NULL,105),(93,'1997-10-06 17:00:00.000','midfielder',177.50,72.26,'Việt Nam',NULL,1,'2026-06-19 04:29:36.724','2026-06-19 04:29:36.724',NULL,106),(94,'1997-05-23 17:00:00.000','forward',175.70,60.64,'Việt Nam',NULL,1,'2026-06-19 04:29:36.753','2026-06-19 04:29:36.753',NULL,107),(95,'1990-03-18 17:00:00.000','forward',182.39,76.50,'Việt Nam',NULL,1,'2026-06-19 04:29:36.778','2026-06-19 04:29:36.778',NULL,108),(96,'1997-06-24 17:00:00.000','forward',183.35,76.26,'Việt Nam',NULL,1,'2026-06-19 04:29:36.804','2026-06-19 04:29:36.804',NULL,109),(102,'2005-09-24 00:00:00.000','goalkeeper',NULL,NULL,NULL,NULL,1,'2026-07-01 15:00:48.857',NULL,NULL,111),(103,'2000-07-13 00:00:00.000','goalkeeper',NULL,NULL,NULL,NULL,1,'2026-07-02 00:21:12.874',NULL,NULL,110),(105,'2000-07-29 00:00:00.000','goalkeeper',NULL,NULL,NULL,NULL,1,'2026-07-04 02:50:32.963',NULL,NULL,6),(108,'2026-07-08 00:00:00.000','goalkeeper',NULL,NULL,NULL,NULL,1,'2026-07-07 01:22:33.083',NULL,NULL,112),(109,'2005-07-14 00:00:00.000','goalkeeper',NULL,NULL,NULL,NULL,1,'2026-07-07 01:25:33.672',NULL,NULL,113),(110,'2006-07-12 00:00:00.000','goalkeeper',NULL,NULL,NULL,NULL,1,'2026-07-07 01:26:37.242',NULL,NULL,114),(111,'2026-07-09 00:00:00.000','goalkeeper',NULL,NULL,NULL,NULL,1,'2026-07-07 01:37:25.135',NULL,NULL,115),(112,'2009-07-08 00:00:00.000','goalkeeper',NULL,NULL,NULL,NULL,1,'2026-07-16 10:44:19.886',NULL,NULL,116),(113,'2026-07-16 00:00:00.000','goalkeeper',NULL,NULL,NULL,NULL,1,'2026-07-16 11:15:57.709',NULL,NULL,117),(114,'2026-07-09 00:00:00.000','goalkeeper',NULL,NULL,NULL,NULL,1,'2026-07-16 11:16:47.505',NULL,NULL,118),(115,'2026-07-01 00:00:00.000','goalkeeper',NULL,NULL,NULL,NULL,1,'2026-07-16 11:18:07.869',NULL,NULL,119);
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
INSERT INTO `roles` VALUES (1,'admin','Quản trị hệ thống',1,'2026-06-16 14:41:09.901','2026-06-16 14:41:09.901'),(2,'leader','Trưởng đội / đại diện đội bóng',1,'2026-06-16 14:41:09.996','2026-06-16 14:41:09.996'),(3,'player','Cầu thủ đã claim tài khoản',1,'2026-06-16 14:41:10.035','2026-06-16 14:41:10.035'),(4,'referee','Trọng tài',1,'2026-06-16 14:41:10.049','2026-06-16 14:41:10.049'),(5,'user','Tài khoản mặc định khi đăng ký',1,'2026-06-16 14:41:10.060','2026-06-16 14:41:10.060');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
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
  `status` enum('approved','pending','active','eliminated','withdrawn') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=205 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `season_teams`
--

LOCK TABLES `season_teams` WRITE;
/*!40000 ALTER TABLE `season_teams` DISABLE KEYS */;
INSERT INTO `season_teams` VALUES (2,1,2,'active',1,'2026-06-19 04:29:34.202','2026-06-20 06:34:24.380',NULL,3,19),(3,1,3,'active',1,'2026-06-19 04:29:34.783','2026-06-20 06:34:24.380',NULL,3,32),(4,1,4,'active',1,'2026-06-19 04:29:35.268','2026-06-20 06:34:24.380',NULL,3,45),(5,1,5,'active',1,'2026-06-19 04:29:35.705','2026-06-20 06:34:24.417',NULL,4,58),(6,1,6,'active',1,'2026-06-19 04:29:36.086','2026-06-20 06:34:24.417',NULL,4,71),(7,1,7,'active',1,'2026-06-19 04:29:36.458','2026-06-20 06:34:24.417',NULL,4,84),(8,1,8,'active',1,'2026-06-19 04:29:36.818','2026-06-20 06:34:24.417',NULL,4,97),(9,2,1,'approved',1,'2026-06-20 15:30:23.223','2026-06-20 15:30:23.355',NULL,5,1),(10,2,2,'active',1,'2026-06-20 15:30:23.240','2026-06-20 15:30:23.355',NULL,5,1),(11,2,3,'active',1,'2026-06-20 15:30:23.254','2026-06-20 15:30:23.355',NULL,5,1),(12,2,4,'active',1,'2026-06-20 15:30:23.265','2026-06-20 15:30:23.355',NULL,5,1),(13,2,5,'active',1,'2026-06-20 15:30:23.277','2026-06-20 15:30:23.355',NULL,5,1),(14,2,6,'active',1,'2026-06-20 15:30:23.287','2026-06-20 15:30:23.355',NULL,5,1),(15,2,7,'active',1,'2026-06-20 15:30:23.298','2026-06-20 15:30:23.355',NULL,5,1),(16,2,8,'active',1,'2026-06-20 15:30:23.308','2026-06-20 15:30:23.355',NULL,5,1),(17,3,1,'approved',1,'2026-06-20 15:30:23.378','2026-06-20 15:30:23.471',NULL,6,1),(18,3,2,'active',1,'2026-06-20 15:30:23.390','2026-06-20 15:30:23.471',NULL,6,1),(19,3,3,'active',1,'2026-06-20 15:30:23.399','2026-06-20 15:30:23.471',NULL,6,1),(20,3,4,'active',1,'2026-06-20 15:30:23.409','2026-06-20 15:30:23.471',NULL,6,1),(21,3,5,'active',1,'2026-06-20 15:30:23.418','2026-06-20 15:30:23.471',NULL,6,1),(22,3,6,'active',1,'2026-06-20 15:30:23.427','2026-06-20 15:30:23.471',NULL,6,1),(23,3,7,'active',1,'2026-06-20 15:30:23.436','2026-06-20 15:30:23.471',NULL,6,1),(24,3,8,'active',1,'2026-06-20 15:30:23.444','2026-06-20 15:30:23.471',NULL,6,1),(25,4,1,'active',1,'2026-06-20 15:30:23.489','2026-06-20 15:30:23.565',NULL,7,1),(26,4,2,'active',1,'2026-06-20 15:30:23.496','2026-06-20 15:30:23.565',NULL,7,1),(27,4,3,'active',1,'2026-06-20 15:30:23.505','2026-06-20 15:30:23.565',NULL,7,1),(28,4,4,'active',1,'2026-06-20 15:30:23.513','2026-06-20 15:30:23.565',NULL,7,1),(29,4,5,'active',1,'2026-06-20 15:30:23.520','2026-06-20 15:30:23.578',NULL,8,1),(30,4,6,'active',1,'2026-06-20 15:30:23.527','2026-06-20 15:30:23.578',NULL,8,1),(31,4,7,'active',1,'2026-06-20 15:30:23.536','2026-06-20 15:30:23.578',NULL,8,1),(32,4,8,'active',1,'2026-06-20 15:30:23.545','2026-06-20 15:30:23.578',NULL,8,1),(33,5,1,'active',1,'2026-06-20 15:30:23.596','2026-06-20 15:30:23.695',NULL,9,1),(34,5,2,'active',1,'2026-06-20 15:30:23.604','2026-06-20 15:30:23.695',NULL,9,1),(35,5,3,'active',1,'2026-06-20 15:30:23.612','2026-06-20 15:30:23.695',NULL,9,1),(36,5,4,'active',1,'2026-06-20 15:30:23.621','2026-06-20 15:30:23.695',NULL,9,1),(37,5,5,'active',1,'2026-06-20 15:30:23.646','2026-06-20 15:30:23.710',NULL,10,1),(38,5,6,'active',1,'2026-06-20 15:30:23.654','2026-06-20 15:30:23.710',NULL,10,1),(39,5,7,'active',1,'2026-06-20 15:30:23.662','2026-06-20 15:30:23.710',NULL,10,1),(40,5,8,'active',1,'2026-06-20 15:30:23.670','2026-06-20 15:30:23.710',NULL,10,1),(41,6,1,'active',1,'2026-06-20 15:30:23.748','2026-06-20 15:30:23.803',NULL,NULL,1),(42,6,2,'active',1,'2026-06-20 15:30:23.756','2026-06-20 15:30:23.803',NULL,NULL,1),(43,6,3,'active',1,'2026-06-20 15:30:23.764','2026-06-20 15:30:23.803',NULL,NULL,1),(44,6,4,'active',1,'2026-06-20 15:30:23.773','2026-06-20 15:30:23.803',NULL,NULL,1),(45,6,5,'active',1,'2026-06-20 15:30:23.780','2026-06-20 15:30:23.803',NULL,NULL,1),(46,7,1,'approved',1,'2026-06-20 15:30:23.821','2026-06-20 15:30:23.892',NULL,NULL,1),(47,7,2,'active',1,'2026-06-20 15:30:23.830','2026-06-20 15:30:23.892',NULL,NULL,1),(48,7,3,'active',1,'2026-06-20 15:30:23.837','2026-06-20 15:30:23.892',NULL,NULL,1),(49,7,4,'active',1,'2026-06-20 15:30:23.846','2026-06-20 15:30:23.892',NULL,NULL,1),(50,7,5,'active',1,'2026-06-20 15:30:23.856','2026-06-20 15:30:23.892',NULL,NULL,1),(51,7,6,'active',1,'2026-06-20 15:30:23.863','2026-06-20 15:30:23.892',NULL,NULL,1),(52,7,7,'active',1,'2026-06-20 15:30:23.871','2026-06-20 15:30:23.892',NULL,NULL,1),(54,8,2,'active',1,'2026-06-20 15:30:23.915','2026-06-20 15:30:23.982',NULL,NULL,1),(55,8,3,'active',1,'2026-06-20 15:30:23.922','2026-06-20 15:30:23.982',NULL,NULL,1),(56,8,4,'active',1,'2026-06-20 15:30:23.929','2026-06-20 15:30:23.982',NULL,NULL,1),(57,8,5,'active',1,'2026-06-20 15:30:23.937','2026-06-20 15:30:23.996',NULL,NULL,1),(58,8,6,'active',1,'2026-06-20 15:30:23.945','2026-06-20 15:30:23.996',NULL,NULL,1),(59,8,7,'active',1,'2026-06-20 15:30:23.953','2026-06-20 15:30:23.996',NULL,NULL,1),(60,8,8,'active',1,'2026-06-20 15:30:23.962','2026-06-20 15:30:23.962',NULL,NULL,1),(62,9,2,'active',1,'2026-06-20 15:30:24.020','2026-06-20 15:30:24.087',NULL,NULL,1),(63,9,3,'active',1,'2026-06-20 15:30:24.028','2026-06-20 15:30:24.087',NULL,NULL,1),(64,9,4,'active',1,'2026-06-20 15:30:24.036','2026-06-20 15:30:24.087',NULL,NULL,1),(65,9,5,'active',1,'2026-06-20 15:30:24.043','2026-06-20 15:30:24.087',NULL,NULL,1),(66,9,6,'active',1,'2026-06-20 15:30:24.052','2026-06-20 15:30:24.099',NULL,NULL,1),(67,9,7,'active',1,'2026-06-20 15:30:24.060','2026-06-20 15:30:24.099',NULL,NULL,1),(68,9,8,'active',1,'2026-06-20 15:30:24.066','2026-06-20 15:30:24.099',NULL,NULL,1),(70,10,2,'active',1,'2026-06-20 15:30:24.122','2026-06-20 15:30:24.188',NULL,NULL,1),(71,10,3,'active',1,'2026-06-20 15:30:24.131','2026-06-20 15:30:24.200',NULL,NULL,1),(72,10,4,'active',1,'2026-06-20 15:30:24.138','2026-06-20 15:30:24.200',NULL,NULL,1),(73,10,5,'active',1,'2026-06-20 15:30:24.145','2026-06-20 15:30:24.214',NULL,NULL,1),(74,10,6,'active',1,'2026-06-20 15:30:24.153','2026-06-20 15:30:24.214',NULL,NULL,1),(75,10,7,'active',1,'2026-06-20 15:30:24.161','2026-06-20 15:30:24.228',NULL,NULL,1),(76,10,8,'active',1,'2026-06-20 15:30:24.169','2026-06-20 15:30:24.228',NULL,NULL,1),(77,11,1,'active',1,'2026-06-20 15:30:24.246','2026-06-20 15:33:28.062',NULL,NULL,1),(78,11,2,'active',1,'2026-06-20 15:30:24.254','2026-06-20 15:33:28.067',NULL,NULL,1),(79,11,3,'active',1,'2026-06-20 15:30:24.261','2026-06-20 15:33:28.067',NULL,NULL,1),(80,11,4,'active',1,'2026-06-20 15:30:24.269','2026-06-20 15:33:28.062',NULL,NULL,1),(81,11,5,'active',1,'2026-06-20 15:30:24.277','2026-06-20 15:33:28.062',NULL,NULL,1),(82,11,6,'active',1,'2026-06-20 15:30:24.284','2026-06-20 15:33:28.062',NULL,NULL,1),(83,11,7,'active',1,'2026-06-20 15:30:24.291','2026-06-20 15:33:28.067',NULL,NULL,1),(84,11,8,'active',1,'2026-06-20 15:30:24.298','2026-06-20 15:33:28.067',NULL,NULL,1),(85,12,1,'active',1,'2026-06-22 02:54:27.913','2026-06-22 02:54:27.913',NULL,NULL,1),(86,12,2,'active',1,'2026-06-22 02:54:27.929','2026-06-22 02:54:27.929',NULL,NULL,1),(87,12,3,'active',1,'2026-06-22 02:54:27.938','2026-06-22 02:54:27.938',NULL,NULL,1),(88,12,4,'active',1,'2026-06-22 02:54:27.949','2026-06-22 02:54:27.949',NULL,NULL,1),(89,12,5,'active',1,'2026-06-22 02:54:27.958','2026-06-22 02:54:27.958',NULL,NULL,1),(90,12,6,'active',1,'2026-06-22 02:54:27.971','2026-06-22 02:54:27.971',NULL,NULL,1),(91,12,7,'active',1,'2026-06-22 02:54:27.984','2026-06-22 02:54:27.984',NULL,NULL,1),(92,12,8,'active',1,'2026-06-22 02:54:27.994','2026-06-22 02:54:27.994',NULL,NULL,1),(93,13,1,'active',1,'2026-06-22 02:54:28.147','2026-06-22 02:54:28.147',NULL,NULL,1),(94,13,2,'active',1,'2026-06-22 02:54:28.155','2026-06-22 02:54:28.155',NULL,NULL,1),(95,13,3,'active',1,'2026-06-22 02:54:28.164','2026-06-22 02:54:28.164',NULL,NULL,1),(96,13,4,'active',1,'2026-06-22 02:54:28.172','2026-06-22 02:54:28.172',NULL,NULL,1),(97,13,5,'active',1,'2026-06-22 02:54:28.181','2026-06-22 02:54:28.181',NULL,NULL,1),(98,13,6,'active',1,'2026-06-22 02:54:28.189','2026-06-22 02:54:28.189',NULL,NULL,1),(99,13,7,'active',1,'2026-06-22 02:54:28.198','2026-06-22 02:54:28.198',NULL,NULL,1),(100,13,8,'active',1,'2026-06-22 02:54:28.207','2026-06-22 02:54:28.207',NULL,NULL,1),(101,14,1,'active',1,'2026-06-22 02:54:28.236','2026-06-22 02:54:28.236',NULL,NULL,1),(102,14,2,'active',1,'2026-06-22 02:54:28.248','2026-06-22 02:54:28.248',NULL,NULL,1),(103,14,3,'active',1,'2026-06-22 02:54:28.256','2026-06-22 02:54:28.256',NULL,NULL,1),(104,14,4,'active',1,'2026-06-22 02:54:28.265','2026-06-22 02:54:28.265',NULL,NULL,1),(105,14,5,'active',1,'2026-06-22 02:54:28.272','2026-06-22 02:54:28.272',NULL,NULL,1),(106,14,6,'active',1,'2026-06-22 02:54:28.281','2026-06-22 02:54:28.281',NULL,NULL,1),(107,14,7,'active',1,'2026-06-22 02:54:28.289','2026-06-22 02:54:28.289',NULL,NULL,1),(108,14,8,'active',1,'2026-06-22 02:54:28.297','2026-06-22 02:54:28.297',NULL,NULL,1),(109,15,1,'active',1,'2026-06-22 02:54:28.314','2026-06-22 02:54:28.314',NULL,NULL,1),(110,15,2,'active',1,'2026-06-22 02:54:28.326','2026-06-22 02:54:28.326',NULL,NULL,1),(111,15,3,'active',1,'2026-06-22 02:54:28.338','2026-06-22 02:54:28.338',NULL,NULL,1),(112,15,4,'active',1,'2026-06-22 02:54:28.352','2026-06-22 02:54:28.352',NULL,NULL,1),(113,15,5,'active',1,'2026-06-22 02:54:28.360','2026-06-22 02:54:28.360',NULL,NULL,1),(114,15,6,'active',1,'2026-06-22 02:54:28.370','2026-06-22 02:54:28.370',NULL,NULL,1),(115,15,7,'active',1,'2026-06-22 02:54:28.471','2026-06-22 02:54:28.471',NULL,NULL,1),(116,15,8,'active',1,'2026-06-22 02:54:28.567','2026-06-22 02:54:28.567',NULL,NULL,1),(117,16,1,'active',1,'2026-06-22 02:54:28.591','2026-06-22 02:54:28.591',NULL,NULL,1),(118,16,2,'active',1,'2026-06-22 02:54:28.600','2026-06-22 02:54:28.600',NULL,NULL,1),(119,16,3,'active',1,'2026-06-22 02:54:28.608','2026-06-22 02:54:28.608',NULL,NULL,1),(120,16,4,'active',1,'2026-06-22 02:54:28.617','2026-06-22 02:54:28.617',NULL,NULL,1),(121,16,5,'active',1,'2026-06-22 02:54:28.645','2026-06-22 02:54:28.645',NULL,NULL,1),(122,17,1,'active',1,'2026-06-22 02:54:28.666','2026-06-22 02:54:28.666',NULL,NULL,1),(123,17,2,'active',1,'2026-06-22 02:54:28.677','2026-06-22 02:54:28.677',NULL,NULL,1),(124,17,3,'active',1,'2026-06-22 02:54:28.688','2026-06-22 02:54:28.688',NULL,NULL,1),(125,17,4,'active',1,'2026-06-22 02:54:28.697','2026-06-22 02:54:28.697',NULL,NULL,1),(126,17,5,'active',1,'2026-06-22 02:54:28.705','2026-06-22 02:54:28.705',NULL,NULL,1),(127,17,6,'active',1,'2026-06-22 02:54:28.712','2026-06-22 02:54:28.712',NULL,NULL,1),(128,17,7,'active',1,'2026-06-22 02:54:28.719','2026-06-22 02:54:28.719',NULL,NULL,1),(129,18,1,'active',1,'2026-06-22 02:54:28.736','2026-06-22 02:54:28.736',NULL,NULL,1),(130,18,2,'active',1,'2026-06-22 02:54:28.743','2026-06-22 02:54:28.743',NULL,NULL,1),(131,18,3,'active',1,'2026-06-22 02:54:28.752','2026-06-22 02:54:28.752',NULL,NULL,1),(132,18,4,'active',1,'2026-06-22 02:54:28.760','2026-06-22 02:54:28.760',NULL,NULL,1),(133,18,5,'active',1,'2026-06-22 02:54:28.770','2026-06-22 02:54:28.770',NULL,NULL,1),(134,18,6,'active',1,'2026-06-22 02:54:28.777','2026-06-22 02:54:28.777',NULL,NULL,1),(135,18,7,'active',1,'2026-06-22 02:54:28.784','2026-06-22 02:54:28.784',NULL,NULL,1),(136,18,8,'active',1,'2026-06-22 02:54:28.790','2026-06-22 02:54:28.790',NULL,NULL,1),(137,19,1,'active',1,'2026-06-22 02:54:28.807','2026-06-22 02:54:28.807',NULL,NULL,1),(138,19,2,'active',1,'2026-06-22 02:54:28.814','2026-06-22 02:54:28.814',NULL,NULL,1),(139,19,3,'active',1,'2026-06-22 02:54:28.859','2026-06-22 02:54:28.859',NULL,NULL,1),(140,19,4,'active',1,'2026-06-22 02:54:28.915','2026-06-22 02:54:28.915',NULL,NULL,1),(141,19,5,'active',1,'2026-06-22 02:54:28.924','2026-06-22 02:54:28.924',NULL,NULL,1),(142,19,6,'active',1,'2026-06-22 02:54:28.931','2026-06-22 02:54:28.931',NULL,NULL,1),(143,19,7,'active',1,'2026-06-22 02:54:28.938','2026-06-22 02:54:28.938',NULL,NULL,1),(144,19,8,'active',1,'2026-06-22 02:54:28.946','2026-06-22 02:54:28.946',NULL,NULL,1),(145,20,1,'approved',1,'2026-06-22 02:54:28.982','2026-06-22 02:54:28.982',NULL,NULL,1),(146,20,2,'active',1,'2026-06-22 02:54:28.989','2026-06-22 02:54:28.989',NULL,NULL,1),(147,20,3,'active',1,'2026-06-22 02:54:28.996','2026-06-22 02:54:28.996',NULL,NULL,1),(148,20,4,'active',1,'2026-06-22 02:54:29.003','2026-06-22 02:54:29.003',NULL,NULL,1),(149,20,5,'active',1,'2026-06-22 02:54:29.010','2026-06-22 02:54:29.010',NULL,NULL,1),(150,20,6,'active',1,'2026-06-22 02:54:29.018','2026-06-22 02:54:29.018',NULL,NULL,1),(151,20,7,'active',1,'2026-06-22 02:54:29.044','2026-06-22 02:54:29.044',NULL,NULL,1),(152,20,8,'active',1,'2026-06-22 02:54:29.052','2026-06-22 02:54:29.052',NULL,NULL,1),(154,21,2,'active',1,'2026-06-22 02:54:29.078','2026-06-22 02:54:29.078',NULL,NULL,1),(155,21,3,'active',1,'2026-06-22 02:54:29.086','2026-06-22 02:54:29.086',NULL,NULL,1),(156,21,4,'active',1,'2026-06-22 02:54:29.102','2026-06-22 02:54:29.102',NULL,NULL,1),(157,21,5,'active',1,'2026-06-22 02:54:29.110','2026-06-22 02:54:29.110',NULL,NULL,1),(158,21,6,'active',1,'2026-06-22 02:54:29.118','2026-06-22 02:54:29.118',NULL,NULL,1),(159,21,7,'active',1,'2026-06-22 02:54:29.125','2026-06-22 02:54:29.125',NULL,NULL,1),(160,21,8,'active',1,'2026-06-22 02:54:29.132','2026-06-22 02:54:29.132',NULL,NULL,1),(186,24,33,'active',1,'2026-07-08 03:04:42.000','2026-07-16 08:49:59.000',NULL,NULL,NULL),(188,24,34,'active',1,'2026-07-08 03:05:19.000','2026-07-16 09:43:33.000',NULL,NULL,NULL),(190,24,36,'active',1,'2026-07-08 11:21:18.000','2026-07-16 08:50:01.000',NULL,NULL,NULL),(194,24,37,'active',1,'2026-07-11 05:08:14.000','2026-07-16 08:50:02.000',NULL,NULL,NULL),(199,27,41,'active',1,'2026-07-16 12:08:13.000','2026-07-16 12:09:50.000',NULL,62,NULL),(200,27,40,'active',1,'2026-07-16 12:08:40.000','2026-07-16 12:09:50.000',NULL,62,NULL),(201,27,39,'active',1,'2026-07-16 12:09:04.000','2026-07-16 12:09:49.000',NULL,61,NULL),(202,27,38,'active',1,'2026-07-16 12:09:18.000','2026-07-16 12:09:48.000',NULL,61,NULL),(203,27,33,'withdrawn',0,'2026-07-16 14:37:01.000','2026-07-16 14:37:54.000','2026-07-16 14:39:02.000',NULL,NULL),(204,26,33,'approved',0,'2026-07-16 14:39:17.000','2026-10-15 00:57:16.000','2026-07-16 14:39:21.000',NULL,NULL);
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
  `registration_fee` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `seasons_name_key` (`name`),
  KEY `seasons_tournament_id_fkey` (`tournament_id`),
  KEY `seasons_user_id_fkey` (`user_id`),
  CONSTRAINT `seasons_tournament_id_fkey` FOREIGN KEY (`tournament_id`) REFERENCES `tournaments` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `seasons_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seasons`
--

LOCK TABLES `seasons` WRITE;
/*!40000 ALTER TABLE `seasons` DISABLE KEYS */;
INSERT INTO `seasons` VALUES (1,'Mùa Giải 2024','Mùa giải chính thức 2024','registration_open','2024-03-01 00:00:00.000','2024-06-30 00:00:00.000','2024-02-15 00:00:00.000',1,1,'2026-06-19 04:29:32.693','2026-07-08 02:31:02.000',NULL,2,1,8,5000000.00),(2,'Season Test 01 — 8 teams single group','Baseline: 1 group tất cả 8 teams, round-robin','registration_open','2025-03-01 00:00:00.000','2025-05-01 00:00:00.000','2025-02-22 00:00:00.000',1,1,'2026-06-20 15:30:23.192','2026-07-08 03:01:33.000',NULL,1,1,16,0.00),(3,'Season Test 02 — 8 teams single group, rest=1','Stress test scheduling gap: min rest 1 ngày','upcoming','2025-06-01 00:00:00.000','2025-08-01 00:00:00.000','2025-05-25 00:00:00.000',0,1,'2026-06-20 15:30:23.368','2026-06-20 15:30:23.368',NULL,1,1,16,0.00),(4,'Season Test 03 — 2 groups × 4 teams','Standard tournament layout','upcoming','2025-09-01 00:00:00.000','2025-11-01 00:00:00.000','2025-08-25 00:00:00.000',0,1,'2026-06-20 15:30:23.481','2026-06-20 15:30:23.481',NULL,1,1,16,0.00),(5,'Season Test 04 — 2 groups + semi + final','Multi-phase: group → semi_final → final','upcoming','2025-12-01 00:00:00.000','2026-02-01 00:00:00.000','2025-11-24 00:00:00.000',0,1,'2026-06-20 15:30:23.587','2026-06-20 15:30:23.587',NULL,1,1,16,0.00),(6,'Season Test 05 — 5 teams (odd, bye expected)','Edge: số lẻ → mỗi lượt 1 team bye','upcoming','2026-03-01 00:00:00.000','2026-05-01 00:00:00.000','2026-02-22 00:00:00.000',0,1,'2026-06-20 15:30:23.740','2026-06-20 15:30:23.740',NULL,1,1,16,0.00),(7,'Season Test 06 — 7 teams (odd, bye expected)','Edge: 7 teams → 1 bye mỗi vòng','upcoming','2026-06-01 00:00:00.000','2026-08-01 00:00:00.000','2026-05-25 00:00:00.000',0,1,'2026-06-20 15:30:23.814','2026-06-20 15:30:23.814',NULL,1,1,16,0.00),(8,'Season Test 07 — 2 groups: 4 teams + 3 teams','Edge: group B chỉ có 3 team → bye trong B','registration_open','2026-09-01 00:00:00.000','2026-11-01 00:00:00.000','2026-08-25 00:00:00.000',0,1,'2026-06-20 15:30:23.899','2026-06-20 15:30:23.899',NULL,1,1,16,0.00),(9,'Season Test 08 — 2 groups: 5 teams + 3 teams','Edge: cả hai group đều lẻ','registration_open','2026-12-01 00:00:00.000','2027-02-01 00:00:00.000','2026-11-24 00:00:00.000',0,1,'2026-06-20 15:30:24.004','2026-06-20 15:30:24.004',NULL,1,1,16,0.00),(10,'Season Test 09 — 4 groups × 2 teams','Minimal group: chỉ 1 trận mỗi group','registration_open','2027-03-01 00:00:00.000','2027-05-01 00:00:00.000','2027-02-22 00:00:00.000',0,1,'2026-06-20 15:30:24.107','2026-06-20 15:30:24.107',NULL,1,1,16,0.00),(11,'Season Test 10 — 8 teams, rest=7 days','Rest constraint cực cao → scheduler phải spread dài','ongoing','2027-06-01 00:00:00.000','2027-08-01 00:00:00.000','2027-05-25 00:00:00.000',0,1,'2026-06-20 15:30:24.237','2026-06-20 15:33:28.117',NULL,1,1,16,0.00),(12,'AutoSched Test 01 — 8 teams single group','Baseline: 1 group tất cả 8 teams, round-robin','registration_open','2025-03-01 00:00:00.000','2025-05-01 00:00:00.000','2025-02-22 00:00:00.000',1,1,'2026-06-22 02:54:27.870','2026-07-07 16:49:36.000',NULL,1,1,16,0.00),(13,'AutoSched Test 02 — 8 teams, rest=1','Stress test scheduling gap: min rest 1 ngày','upcoming','2025-06-01 00:00:00.000','2025-08-01 00:00:00.000','2025-05-25 00:00:00.000',0,1,'2026-06-22 02:54:28.074','2026-06-22 02:54:28.074',NULL,1,1,16,0.00),(14,'AutoSched Test 03 — 2 groups × 4 teams','Standard tournament layout','upcoming','2025-09-01 00:00:00.000','2025-11-01 00:00:00.000','2025-08-25 00:00:00.000',0,1,'2026-06-22 02:54:28.226','2026-06-22 02:54:28.226',NULL,1,1,16,0.00),(15,'AutoSched Test 04 — 2 groups + semi + final','Multi-phase: group → semi_final → final','upcoming','2025-12-01 00:00:00.000','2026-02-01 00:00:00.000','2025-11-24 00:00:00.000',0,1,'2026-06-22 02:54:28.305','2026-06-22 02:54:28.305',NULL,1,1,16,0.00),(16,'AutoSched Test 05 — 5 teams (odd, bye)','Edge: số lẻ → mỗi lượt 1 team bye','upcoming','2026-03-01 00:00:00.000','2026-05-01 00:00:00.000','2026-02-22 00:00:00.000',0,1,'2026-06-22 02:54:28.583','2026-06-22 02:54:28.583',NULL,1,1,16,0.00),(17,'AutoSched Test 06 — 7 teams (odd, bye)','Edge: 7 teams → 1 bye mỗi vòng','upcoming','2026-06-01 00:00:00.000','2026-08-01 00:00:00.000','2026-05-25 00:00:00.000',0,1,'2026-06-22 02:54:28.656','2026-06-22 02:54:28.656',NULL,1,1,16,0.00),(18,'AutoSched Test 07 — 4+3 teams','Edge: group B chỉ có 3 team → bye trong B','upcoming','2026-09-01 00:00:00.000','2026-11-01 00:00:00.000','2026-08-25 00:00:00.000',0,1,'2026-06-22 02:54:28.730','2026-06-22 02:54:28.730',NULL,1,1,16,0.00),(19,'AutoSched Test 08 — 5+3 teams','Edge: cả hai group đều lẻ','upcoming','2026-12-01 00:00:00.000','2027-02-01 00:00:00.000','2026-11-24 00:00:00.000',0,1,'2026-06-22 02:54:28.799','2026-06-22 02:54:28.799',NULL,1,1,16,0.00),(20,'AutoSched Test 09 — 4 groups × 2 teams','Minimal group: chỉ 1 trận mỗi group','upcoming','2027-03-01 00:00:00.000','2027-05-01 00:00:00.000','2027-02-22 00:00:00.000',0,1,'2026-06-22 02:54:28.975','2026-06-22 02:54:28.975',NULL,1,1,16,0.00),(21,'AutoSched Test 10 — 8 teams, rest=7','Rest constraint cực cao → scheduler phải spread dài','registration_open','2027-06-01 00:00:00.000','2027-08-01 00:00:00.000','2027-05-25 00:00:00.000',0,1,'2026-06-22 02:54:29.064','2026-06-22 02:54:29.064',NULL,1,1,16,0.00),(24,'asdasdasd','asdasdasd','upcoming','2026-07-08 00:00:00.000','2025-07-09 00:00:00.000','2026-07-09 00:00:00.000',0,1,'2026-07-08 03:03:46.000','2026-07-16 10:20:24.000',NULL,12,1,4,0.00),(25,'asdasd','sadasdasdad','upcoming','2026-09-07 00:00:00.000','2026-09-07 00:00:00.000','2026-09-07 00:00:00.000',0,1,'2026-07-09 11:42:03.000','2026-07-13 01:21:36.000',NULL,12,1,4,0.00),(26,'26-27','adsasda','registration_open','2026-08-01 00:00:00.000','2026-08-30 23:59:59.000','2026-08-10 23:59:59.000',1,1,'2026-07-16 10:42:04.000','2026-07-16 11:54:34.000',NULL,13,NULL,16,0.00),(27,'Giai moi ','cho vui ','registration_open','2026-08-01 00:00:00.000','2026-09-30 23:59:59.000','2026-07-25 23:59:59.000',1,1,'2026-07-16 12:02:16.000','2026-07-16 12:02:16.000',NULL,13,NULL,16,100.00);
/*!40000 ALTER TABLE `seasons` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_formations`
--

DROP TABLE IF EXISTS `team_formations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team_formations` (
  `id` int NOT NULL AUTO_INCREMENT,
  `team_id` int NOT NULL,
  `field_type` int NOT NULL,
  `starter_player_ids` json NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_team_field` (`team_id`,`field_type`),
  KEY `idx_team_id` (`team_id`),
  CONSTRAINT `chk_field_type` CHECK ((`field_type` in (5,7,11)))
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_formations`
--

LOCK TABLES `team_formations` WRITE;
/*!40000 ALTER TABLE `team_formations` DISABLE KEYS */;
INSERT INTO `team_formations` VALUES (1,1,5,'[3]','2026-11-08 18:55:13','2026-11-08 19:22:56');
/*!40000 ALTER TABLE `team_formations` ENABLE KEYS */;
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
  PRIMARY KEY (`id`),
  KEY `team_leaders_team_id_fkey` (`team_id`),
  KEY `team_leaders_user_id_fkey` (`user_id`),
  CONSTRAINT `team_leaders_team_id_fkey` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `team_leaders_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_leaders`
--

LOCK TABLES `team_leaders` WRITE;
/*!40000 ALTER TABLE `team_leaders` DISABLE KEYS */;
INSERT INTO `team_leaders` VALUES (1,1,6,1,'2026-06-19 04:29:32.973','2026-06-19 04:29:32.973',NULL),(2,2,19,1,'2026-06-19 04:29:33.775','2026-06-19 04:29:33.775',NULL),(3,3,32,1,'2026-06-19 04:29:34.384','2026-06-19 04:29:34.384',NULL),(4,4,45,1,'2026-06-19 04:29:34.940','2026-06-19 04:29:34.940',NULL),(5,5,58,1,'2026-06-19 04:29:35.402','2026-06-19 04:29:35.402',NULL),(6,6,71,1,'2026-06-19 04:29:35.799','2026-06-19 04:29:35.799',NULL),(7,7,84,1,'2026-06-19 04:29:36.174','2026-06-19 04:29:36.174',NULL),(8,8,97,1,'2026-06-19 04:29:36.545','2026-06-19 04:29:36.545',NULL),(14,33,111,1,'2026-07-02 01:36:16.307',NULL,NULL);
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
  `user_id` int DEFAULT NULL,
  `is_starter` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `team_players_team_id_jersey_number_key` (`team_id`,`jersey_number`),
  UNIQUE KEY `team_players_team_id_player_id_key` (`team_id`,`player_id`),
  KEY `team_players_player_id_fkey` (`player_id`),
  KEY `team_players_user_id_fkey` (`user_id`),
  CONSTRAINT `team_players_player_id_fkey` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `team_players_team_id_fkey` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `team_players_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=114 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_players`
--

LOCK TABLES `team_players` WRITE;
/*!40000 ALTER TABLE `team_players` DISABLE KEYS */;
INSERT INTO `team_players` VALUES (1,1,1,1,'goalkeeper','captain','active','approved',1,'2026-06-19 04:29:33.072','2026-06-19 04:29:33.072',NULL,1,1),(2,1,2,2,'defender','vice_captain','active','approved',1,'2026-06-19 04:29:33.115','2026-06-19 04:29:33.115',NULL,1,1),(3,1,3,3,'defender','player','active','approved',1,'2026-06-19 04:29:33.159','2026-06-19 04:29:33.159',NULL,1,1),(4,1,4,4,'defender','player','active','approved',1,'2026-06-19 04:29:33.194','2026-06-19 04:29:33.194',NULL,1,1),(5,1,5,5,'defender','player','active','approved',1,'2026-06-19 04:29:33.226','2026-06-19 04:29:33.226',NULL,1,1),(6,1,6,6,'midfielder','player','active','approved',1,'2026-06-19 04:29:33.260','2026-06-19 04:29:33.260',NULL,1,1),(7,1,7,7,'midfielder','player','active','approved',1,'2026-06-19 04:29:33.295','2026-06-19 04:29:33.295',NULL,1,0),(8,1,8,8,'midfielder','player','active','approved',1,'2026-06-19 04:29:33.334','2026-06-19 04:29:33.334',NULL,1,1),(9,1,9,9,'midfielder','player','active','approved',1,'2026-06-19 04:29:33.367','2026-06-19 04:29:33.367',NULL,1,1),(10,1,10,10,'forward','player','active','approved',0,'2026-06-19 04:29:33.403','2026-06-19 04:29:33.403',NULL,1,1),(11,1,11,11,'forward','player','active','approved',1,'2026-06-19 04:29:33.434','2026-06-19 04:29:33.434',NULL,1,1),(12,1,12,12,'forward','player','active','approved',1,'2026-06-19 04:29:33.475','2026-06-19 04:29:33.475',NULL,1,1),(13,2,13,1,'goalkeeper','captain','active','approved',1,'2026-06-19 04:29:33.811','2026-06-19 04:29:33.811',NULL,1,1),(14,2,14,2,'defender','vice_captain','active','approved',1,'2026-06-19 04:29:33.850','2026-06-19 04:29:33.850',NULL,1,1),(15,2,15,3,'defender','player','active','approved',1,'2026-06-19 04:29:33.886','2026-06-19 04:29:33.886',NULL,1,1),(16,2,16,4,'defender','player','active','approved',1,'2026-06-19 04:29:33.917','2026-06-19 04:29:33.917',NULL,1,1),(17,2,17,5,'defender','player','active','approved',1,'2026-06-19 04:29:33.947','2026-06-19 04:29:33.947',NULL,1,1),(18,2,18,6,'midfielder','player','active','approved',1,'2026-06-19 04:29:33.980','2026-06-19 04:29:33.980',NULL,1,1),(19,2,19,7,'midfielder','player','active','approved',1,'2026-06-19 04:29:34.012','2026-06-19 04:29:34.012',NULL,1,1),(20,2,20,8,'midfielder','player','active','approved',1,'2026-06-19 04:29:34.045','2026-06-19 04:29:34.045',NULL,1,0),(21,2,21,9,'midfielder','player','active','approved',1,'2026-06-19 04:29:34.077','2026-06-19 04:29:34.077',NULL,1,1),(22,2,22,10,'forward','player','active','approved',1,'2026-06-19 04:29:34.115','2026-06-19 04:29:34.115',NULL,1,1),(23,2,23,11,'forward','player','active','approved',1,'2026-06-19 04:29:34.146','2026-06-19 04:29:34.146',NULL,1,0),(24,2,24,12,'forward','player','active','approved',1,'2026-06-19 04:29:34.181','2026-06-19 04:29:34.181',NULL,1,0),(25,3,25,1,'goalkeeper','captain','active','approved',1,'2026-06-19 04:29:34.418','2026-06-19 04:29:34.418',NULL,1,0),(26,3,26,2,'defender','vice_captain','active','approved',1,'2026-06-19 04:29:34.453','2026-06-19 04:29:34.453',NULL,1,0),(27,3,27,3,'defender','player','active','approved',1,'2026-06-19 04:29:34.490','2026-06-19 04:29:34.490',NULL,1,0),(28,3,28,4,'defender','player','active','approved',1,'2026-06-19 04:29:34.522','2026-06-19 04:29:34.522',NULL,1,0),(29,3,29,5,'defender','player','active','approved',1,'2026-06-19 04:29:34.553','2026-06-19 04:29:34.553',NULL,1,0),(30,3,30,6,'midfielder','player','active','approved',1,'2026-06-19 04:29:34.586','2026-06-19 04:29:34.586',NULL,1,0),(31,3,31,7,'midfielder','player','active','approved',1,'2026-06-19 04:29:34.619','2026-06-19 04:29:34.619',NULL,1,0),(32,3,32,8,'midfielder','player','active','approved',1,'2026-06-19 04:29:34.652','2026-06-19 04:29:34.652',NULL,1,0),(33,3,33,9,'midfielder','player','active','approved',1,'2026-06-19 04:29:34.686','2026-06-19 04:29:34.686',NULL,1,0),(34,3,34,10,'forward','player','active','approved',1,'2026-06-19 04:29:34.714','2026-06-19 04:29:34.714',NULL,1,0),(35,3,35,11,'forward','player','active','approved',1,'2026-06-19 04:29:34.739','2026-06-19 04:29:34.739',NULL,1,0),(36,3,36,12,'forward','player','active','approved',1,'2026-06-19 04:29:34.771','2026-06-19 04:29:34.771',NULL,1,0),(37,4,37,1,'goalkeeper','captain','active','approved',1,'2026-06-19 04:29:34.972','2026-06-19 04:29:34.972',NULL,1,0),(38,4,38,2,'defender','vice_captain','active','approved',1,'2026-06-19 04:29:34.999','2026-06-19 04:29:34.999',NULL,1,0),(39,4,39,3,'defender','player','active','approved',1,'2026-06-19 04:29:35.020','2026-06-19 04:29:35.020',NULL,1,0),(40,4,40,4,'defender','player','active','approved',1,'2026-06-19 04:29:35.042','2026-06-19 04:29:35.042',NULL,1,0),(41,4,41,5,'defender','player','active','approved',1,'2026-06-19 04:29:35.064','2026-06-19 04:29:35.064',NULL,1,0),(42,4,42,6,'midfielder','player','active','approved',1,'2026-06-19 04:29:35.086','2026-06-19 04:29:35.086',NULL,1,0),(43,4,43,7,'midfielder','player','active','approved',1,'2026-06-19 04:29:35.111','2026-06-19 04:29:35.111',NULL,1,0),(44,4,44,8,'midfielder','player','active','approved',1,'2026-06-19 04:29:35.138','2026-06-19 04:29:35.138',NULL,1,0),(45,4,45,9,'midfielder','player','active','approved',1,'2026-06-19 04:29:35.169','2026-06-19 04:29:35.169',NULL,1,0),(46,4,46,10,'forward','player','active','approved',1,'2026-06-19 04:29:35.199','2026-06-19 04:29:35.199',NULL,1,0),(47,4,47,11,'forward','player','active','approved',1,'2026-06-19 04:29:35.230','2026-06-19 04:29:35.230',NULL,1,0),(48,4,48,12,'forward','player','active','approved',1,'2026-06-19 04:29:35.260','2026-06-19 04:29:35.260',NULL,1,0),(49,5,49,1,'goalkeeper','captain','active','approved',1,'2026-06-19 04:29:35.428','2026-06-19 04:29:35.428',NULL,1,0),(50,5,50,2,'defender','vice_captain','active','approved',1,'2026-06-19 04:29:35.458','2026-06-19 04:29:35.458',NULL,1,0),(51,5,51,3,'defender','player','active','approved',1,'2026-06-19 04:29:35.487','2026-06-19 04:29:35.487',NULL,1,0),(52,5,52,4,'defender','player','active','approved',1,'2026-06-19 04:29:35.515','2026-06-19 04:29:35.515',NULL,1,0),(53,5,53,5,'defender','player','active','approved',1,'2026-06-19 04:29:35.543','2026-06-19 04:29:35.543',NULL,1,0),(54,5,54,6,'midfielder','player','active','approved',1,'2026-06-19 04:29:35.571','2026-06-19 04:29:35.571',NULL,1,0),(55,5,55,7,'midfielder','player','active','approved',1,'2026-06-19 04:29:35.598','2026-06-19 04:29:35.598',NULL,1,0),(56,5,56,8,'midfielder','player','active','approved',1,'2026-06-19 04:29:35.620','2026-06-19 04:29:35.620',NULL,1,0),(57,5,57,9,'midfielder','player','active','approved',1,'2026-06-19 04:29:35.643','2026-06-19 04:29:35.643',NULL,1,0),(58,5,58,10,'forward','player','active','approved',1,'2026-06-19 04:29:35.665','2026-06-19 04:29:35.665',NULL,1,0),(59,5,59,11,'forward','player','active','approved',1,'2026-06-19 04:29:35.684','2026-06-19 04:29:35.684',NULL,1,0),(60,5,60,12,'forward','player','active','approved',1,'2026-06-19 04:29:35.700','2026-06-19 04:29:35.700',NULL,1,0),(61,6,61,1,'goalkeeper','captain','active','approved',1,'2026-06-19 04:29:35.823','2026-06-19 04:29:35.823',NULL,1,0),(62,6,62,2,'defender','vice_captain','active','approved',1,'2026-06-19 04:29:35.841','2026-06-19 04:29:35.841',NULL,1,0),(63,6,63,3,'defender','player','active','approved',1,'2026-06-19 04:29:35.866','2026-06-19 04:29:35.866',NULL,1,0),(64,6,64,4,'defender','player','active','approved',1,'2026-06-19 04:29:35.892','2026-06-19 04:29:35.892',NULL,1,0),(65,6,65,5,'defender','player','active','approved',1,'2026-06-19 04:29:35.928','2026-06-19 04:29:35.928',NULL,1,0),(66,6,66,6,'midfielder','player','active','approved',1,'2026-06-19 04:29:35.957','2026-06-19 04:29:35.957',NULL,1,0),(67,6,67,7,'midfielder','player','active','approved',1,'2026-06-19 04:29:35.982','2026-06-19 04:29:35.982',NULL,1,0),(68,6,68,8,'midfielder','player','active','approved',1,'2026-06-19 04:29:36.011','2026-06-19 04:29:36.011',NULL,1,0),(69,6,69,9,'midfielder','player','active','approved',1,'2026-06-19 04:29:36.029','2026-06-19 04:29:36.029',NULL,1,0),(70,6,70,10,'forward','player','active','approved',1,'2026-06-19 04:29:36.045','2026-06-19 04:29:36.045',NULL,1,0),(71,6,71,11,'forward','player','active','approved',1,'2026-06-19 04:29:36.063','2026-06-19 04:29:36.063',NULL,1,0),(72,6,72,12,'forward','player','active','approved',1,'2026-06-19 04:29:36.082','2026-06-19 04:29:36.082',NULL,1,0),(73,7,73,1,'goalkeeper','captain','active','approved',1,'2026-06-19 04:29:36.198','2026-06-19 04:29:36.198',NULL,1,0),(74,7,74,2,'defender','vice_captain','active','approved',1,'2026-06-19 04:29:36.220','2026-06-19 04:29:36.220',NULL,1,0),(75,7,75,3,'defender','player','active','approved',1,'2026-06-19 04:29:36.241','2026-06-19 04:29:36.241',NULL,1,0),(76,7,76,4,'defender','player','active','approved',1,'2026-06-19 04:29:36.264','2026-06-19 04:29:36.264',NULL,1,0),(77,7,77,5,'defender','player','active','approved',1,'2026-06-19 04:29:36.289','2026-06-19 04:29:36.289',NULL,1,0),(78,7,78,6,'midfielder','player','active','approved',1,'2026-06-19 04:29:36.316','2026-06-19 04:29:36.316',NULL,1,0),(79,7,79,7,'midfielder','player','active','approved',1,'2026-06-19 04:29:36.342','2026-06-19 04:29:36.342',NULL,1,0),(80,7,80,8,'midfielder','player','active','approved',1,'2026-06-19 04:29:36.368','2026-06-19 04:29:36.368',NULL,1,0),(81,7,81,9,'midfielder','player','active','approved',1,'2026-06-19 04:29:36.392','2026-06-19 04:29:36.392',NULL,1,0),(82,7,82,10,'forward','player','active','approved',1,'2026-06-19 04:29:36.416','2026-06-19 04:29:36.416',NULL,1,0),(83,7,83,11,'forward','player','active','approved',1,'2026-06-19 04:29:36.437','2026-06-19 04:29:36.437',NULL,1,0),(84,7,84,12,'forward','player','active','approved',1,'2026-06-19 04:29:36.454','2026-06-19 04:29:36.454',NULL,1,0),(85,8,85,1,'goalkeeper','captain','active','approved',1,'2026-06-19 04:29:36.569','2026-06-19 04:29:36.569',NULL,1,0),(86,8,86,2,'defender','vice_captain','active','approved',1,'2026-06-19 04:29:36.586','2026-06-19 04:29:36.586',NULL,1,0),(87,8,87,3,'defender','player','active','approved',1,'2026-06-19 04:29:36.603','2026-06-19 04:29:36.603',NULL,1,0),(88,8,88,4,'defender','player','active','approved',1,'2026-06-19 04:29:36.620','2026-06-19 04:29:36.620',NULL,1,0),(89,8,89,5,'defender','player','active','approved',1,'2026-06-19 04:29:36.640','2026-06-19 04:29:36.640',NULL,1,0),(90,8,90,6,'midfielder','player','active','approved',1,'2026-06-19 04:29:36.659','2026-06-19 04:29:36.659',NULL,1,0),(91,8,91,7,'midfielder','player','active','approved',1,'2026-06-19 04:29:36.681','2026-06-19 04:29:36.681',NULL,1,0),(92,8,92,8,'midfielder','player','active','approved',1,'2026-06-19 04:29:36.705','2026-06-19 04:29:36.705',NULL,1,0),(93,8,93,9,'midfielder','player','active','approved',1,'2026-06-19 04:29:36.733','2026-06-19 04:29:36.733',NULL,1,0),(94,8,94,10,'forward','player','active','approved',1,'2026-06-19 04:29:36.761','2026-06-19 04:29:36.761',NULL,1,0),(95,8,95,11,'forward','player','active','approved',1,'2026-06-19 04:29:36.786','2026-06-19 04:29:36.786',NULL,1,0),(96,8,96,12,'forward','player','active','approved',1,'2026-06-19 04:29:36.812','2026-06-19 04:29:36.812',NULL,1,0),(97,1,102,2222,'goalkeeper','player','active','approved',0,'2026-07-01 15:01:46.097',NULL,NULL,NULL,0),(103,33,103,1,'goalkeeper','captain','active','approved',1,'2026-07-02 00:39:45.354',NULL,NULL,110,0),(105,33,102,23,'goalkeeper','player','active','pending',0,'2026-07-04 06:54:52.616',NULL,NULL,NULL,0),(106,34,108,1,'goalkeeper','captain','active','pending',1,'2026-07-07 01:22:40.816',NULL,NULL,112,0),(107,35,109,1,'goalkeeper','captain','active','pending',1,'2026-07-07 01:25:37.887',NULL,NULL,113,0),(108,36,110,1,'goalkeeper','captain','active','pending',1,'2026-07-07 01:26:43.590',NULL,NULL,114,0),(109,37,111,1,'goalkeeper','captain','active','pending',1,'2026-07-07 01:37:32.914',NULL,NULL,115,0),(110,38,112,1,'goalkeeper','captain','active','pending',1,'2026-07-16 10:44:28.946',NULL,NULL,116,0),(111,39,113,1,'goalkeeper','captain','active','pending',1,'2026-07-16 11:16:08.027',NULL,NULL,117,0),(112,40,114,1,'goalkeeper','captain','active','pending',1,'2026-07-16 11:17:01.881',NULL,NULL,118,0),(113,41,115,1,'goalkeeper','captain','active','pending',1,'2026-07-16 11:18:19.475',NULL,NULL,119,0);
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
  `group_id` int DEFAULT NULL,
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `team_standings_group_id_team_id_key` (`group_id`,`team_id`),
  KEY `team_standings_team_id_fkey` (`team_id`),
  CONSTRAINT `team_standings_group_id_fkey` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `team_standings_team_id_fkey` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=242 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_standings`
--

LOCK TABLES `team_standings` WRITE;
/*!40000 ALTER TABLE `team_standings` DISABLE KEYS */;
INSERT INTO `team_standings` VALUES (9,1,3,2,6,3,1,2,9,8,10,1,'2026-06-20 06:17:11.431','2026-11-09 01:43:23.000',NULL),(10,2,3,4,6,2,0,4,10,12,6,1,'2026-06-20 06:17:11.441','2026-11-09 01:43:23.000',NULL),(11,3,3,3,6,2,1,3,9,12,7,1,'2026-06-20 06:17:11.453','2026-11-09 01:43:23.000',NULL),(12,4,3,1,6,3,2,1,14,10,11,1,'2026-06-20 06:17:11.462','2026-11-09 01:43:23.000',NULL),(13,5,4,2,3,1,1,1,5,7,4,1,'2026-06-20 06:17:11.469','2026-11-09 01:43:23.000',NULL),(14,6,4,4,3,1,0,2,4,6,3,1,'2026-06-20 06:17:11.475','2026-11-09 01:43:23.000',NULL),(15,7,4,1,3,2,0,1,7,4,6,1,'2026-06-20 06:17:11.482','2026-11-09 01:43:23.000',NULL),(16,8,4,3,3,1,1,1,5,4,4,1,'2026-06-20 06:17:11.489','2026-11-09 01:43:23.000',NULL),(204,38,61,0,3,0,2,1,0,1,2,1,'2026-07-16 12:33:19.656','2026-11-09 02:11:08.000',NULL),(205,39,61,0,3,0,2,1,0,1,2,1,'2026-07-16 12:33:21.235','2026-11-09 02:11:08.000',NULL),(206,40,61,0,3,1,2,0,1,0,5,1,'2026-07-16 12:33:53.439','2026-11-09 02:11:08.000',NULL),(207,41,61,0,3,1,2,0,1,0,5,1,'2026-07-16 12:33:55.908','2026-11-09 02:11:08.000',NULL),(208,41,62,0,1,1,0,0,1,0,3,1,'2026-07-16 12:39:27.486','2026-11-09 02:11:08.000',NULL),(209,40,62,0,1,0,0,1,0,1,0,1,'2026-07-16 12:39:27.501','2026-11-09 02:11:08.000',NULL),(210,1,5,0,0,0,0,0,0,0,0,1,'2026-07-16 14:14:39.000','2026-11-09 01:43:00.000',NULL),(211,2,5,0,0,0,0,0,0,0,0,1,'2026-07-16 14:14:39.000','2026-11-09 01:43:00.000',NULL),(212,3,5,0,0,0,0,0,0,0,0,1,'2026-07-16 14:14:39.000','2026-11-09 01:43:00.000',NULL),(213,4,5,0,0,0,0,0,0,0,0,1,'2026-07-16 14:14:39.000','2026-11-09 01:43:00.000',NULL),(214,5,5,0,0,0,0,0,0,0,0,1,'2026-07-16 14:14:39.000','2026-11-09 01:43:00.000',NULL),(215,6,5,0,0,0,0,0,0,0,0,1,'2026-07-16 14:14:39.000','2026-11-09 01:43:00.000',NULL),(216,7,5,0,0,0,0,0,0,0,0,1,'2026-07-16 14:14:39.000','2026-11-09 01:43:00.000',NULL),(217,8,5,0,0,0,0,0,0,0,0,1,'2026-07-16 14:14:39.000','2026-11-09 01:43:00.000',NULL),(218,1,7,0,0,0,0,0,0,0,0,1,'2026-07-16 14:43:58.000','2026-11-09 01:42:55.000',NULL),(219,2,7,0,0,0,0,0,0,0,0,1,'2026-07-16 14:43:58.000','2026-11-09 01:42:55.000',NULL),(220,3,7,0,0,0,0,0,0,0,0,1,'2026-07-16 14:43:58.000','2026-11-09 01:42:55.000',NULL),(221,4,7,0,0,0,0,0,0,0,0,1,'2026-07-16 14:43:58.000','2026-11-09 01:42:55.000',NULL),(222,5,8,0,0,0,0,0,0,0,0,1,'2026-07-16 14:43:58.000','2026-11-09 01:42:55.000',NULL),(223,6,8,0,0,0,0,0,0,0,0,1,'2026-07-16 14:43:58.000','2026-11-09 01:42:55.000',NULL),(224,7,8,0,0,0,0,0,0,0,0,1,'2026-07-16 14:43:58.000','2026-11-09 01:42:55.000',NULL),(225,8,8,0,0,0,0,0,0,0,0,1,'2026-07-16 14:43:58.000','2026-11-09 01:42:55.000',NULL),(226,1,6,0,0,0,0,0,0,0,0,1,'2026-07-16 14:56:33.000','2026-11-09 01:42:58.000',NULL),(227,2,6,0,0,0,0,0,0,0,0,1,'2026-07-16 14:56:33.000','2026-11-09 01:42:58.000',NULL),(228,3,6,0,0,0,0,0,0,0,0,1,'2026-07-16 14:56:33.000','2026-11-09 01:42:58.000',NULL),(229,4,6,0,0,0,0,0,0,0,0,1,'2026-07-16 14:56:33.000','2026-11-09 01:42:58.000',NULL),(230,5,6,0,0,0,0,0,0,0,0,1,'2026-07-16 14:56:33.000','2026-11-09 01:42:58.000',NULL),(231,6,6,0,0,0,0,0,0,0,0,1,'2026-07-16 14:56:33.000','2026-11-09 01:42:58.000',NULL),(232,7,6,0,0,0,0,0,0,0,0,1,'2026-07-16 14:56:33.000','2026-11-09 01:42:58.000',NULL),(233,8,6,0,0,0,0,0,0,0,0,1,'2026-07-16 14:56:33.000','2026-11-09 01:42:58.000',NULL),(234,1,9,0,0,0,0,0,0,0,0,1,'2026-07-16 14:56:39.000','2026-11-09 01:42:53.000',NULL),(235,2,9,0,0,0,0,0,0,0,0,1,'2026-07-16 14:56:39.000','2026-11-09 01:42:53.000',NULL),(236,3,9,0,0,0,0,0,0,0,0,1,'2026-07-16 14:56:39.000','2026-11-09 01:42:53.000',NULL),(237,4,9,0,0,0,0,0,0,0,0,1,'2026-07-16 14:56:39.000','2026-11-09 01:42:53.000',NULL),(238,5,10,0,0,0,0,0,0,0,0,1,'2026-07-16 14:56:39.000','2026-11-09 01:42:53.000',NULL),(239,6,10,0,0,0,0,0,0,0,0,1,'2026-07-16 14:56:39.000','2026-11-09 01:42:53.000',NULL),(240,7,10,0,0,0,0,0,0,0,0,1,'2026-07-16 14:56:39.000','2026-11-09 01:42:53.000',NULL),(241,8,10,0,0,0,0,0,0,0,0,1,'2026-07-16 14:56:39.000','2026-11-09 01:42:53.000',NULL);
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
  `user_id` int DEFAULT NULL,
  `field_type` int NOT NULL DEFAULT '7',
  PRIMARY KEY (`id`),
  UNIQUE KEY `teams_name_key` (`name`),
  KEY `teams_user_id_fkey` (`user_id`),
  CONSTRAINT `teams_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teams`
--

LOCK TABLES `teams` WRITE;
/*!40000 ALTER TABLE `teams` DISABLE KEYS */;
INSERT INTO `teams` VALUES (1,'FC Rồng Vàng','Trần Văn Hùng',NULL,NULL,1,'2026-06-19 04:29:32.912','2026-06-19 04:29:32.912',NULL,1,5),(2,'CLB Sao Biển','Nguyễn Minh Tuấn',NULL,NULL,1,'2026-06-19 04:29:33.753','2026-06-19 04:29:33.753',NULL,1,7),(3,'FC Hùng Mạnh','Lê Văn Đức',NULL,NULL,1,'2026-06-19 04:29:34.365','2026-06-19 04:29:34.365',NULL,1,5),(4,'CLB Thần Tốc','Phạm Quốc Bảo',NULL,NULL,1,'2026-06-19 04:29:34.920','2026-06-19 04:29:34.920',NULL,1,7),(5,'FC Bất Bại','Hoàng Văn Nam',NULL,NULL,1,'2026-06-19 04:29:35.388','2026-06-19 04:29:35.388',NULL,1,7),(6,'CLB Chiến Thắng','Vũ Đình Sơn',NULL,NULL,1,'2026-06-19 04:29:35.787','2026-06-19 04:29:35.787',NULL,1,7),(7,'FC Bão Lửa','Đặng Minh Khoa',NULL,NULL,1,'2026-06-19 04:29:36.161','2026-06-19 04:29:36.161',NULL,1,7),(8,'CLB Thiên Lôi','Bùi Văn Tùng',NULL,NULL,1,'2026-06-19 04:29:36.535','2026-06-30 03:47:33.000','2026-06-30 03:47:33.000',1,7),(33,'fc d','d1',NULL,NULL,1,'2026-07-02 00:39:45.000',NULL,NULL,110,7),(34,'nva','Chưa cập nhật',NULL,NULL,1,'2026-07-07 01:22:40.000',NULL,NULL,112,7),(35,'nvb','Chưa cập nhật',NULL,NULL,1,'2026-07-07 01:25:37.000',NULL,NULL,113,7),(36,'nvc','Chưa cập nhật',NULL,NULL,1,'2026-07-07 01:26:43.000',NULL,NULL,114,7),(37,'nvr','Chưa cập nhật',NULL,NULL,1,'2026-07-07 01:37:32.000',NULL,NULL,115,7),(38,'vui fc','Chưa cập nhật',NULL,NULL,1,'2026-07-16 10:44:28.000',NULL,NULL,116,7),(39,'Buon fc','Chưa cập nhật',NULL,NULL,1,'2026-07-16 11:16:07.000',NULL,NULL,117,7),(40,'Khoc fc','Chưa cập nhật',NULL,NULL,1,'2026-07-16 11:17:01.000',NULL,NULL,118,7),(41,'Cuoi fc','Chưa cập nhật',NULL,NULL,1,'2026-07-16 11:18:19.000',NULL,NULL,119,7);
/*!40000 ALTER TABLE `teams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tournament_rules`
--

DROP TABLE IF EXISTS `tournament_rules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tournament_rules` (
  `id` int NOT NULL AUTO_INCREMENT,
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
  `user_id` int DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'Luật mặc định',
  `custom_rules` json DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tournament_rules_tournament_id_key` (`tournament_id`),
  KEY `tournament_rules_user_id_fkey` (`user_id`),
  CONSTRAINT `tournament_rules_tournament_id_fkey` FOREIGN KEY (`tournament_id`) REFERENCES `tournaments` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tournament_rules_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tournament_rules`
--

LOCK TABLES `tournament_rules` WRITE;
/*!40000 ALTER TABLE `tournament_rules` DISABLE KEYS */;
INSERT INTO `tournament_rules` VALUES (1,1,1,'2026-06-16 14:46:29.466','2026-06-16 14:46:29.466',NULL,3,1,0,3,3,11,6,2,'[\"goal_diff\", \"goals_scored\", \"head_to_head\"]',1,'','Luật mặc định',NULL),(2,2,1,'2026-06-19 04:29:32.654','2026-06-19 04:29:32.654',NULL,3,1,0,3,3,20,7,2,'[\"goal_diff\", \"goals_scored\", \"head_to_head\"]',1,NULL,'Luật mặc định',NULL),(11,12,1,'2026-07-08 02:25:31.000','2026-07-08 02:25:31.000',NULL,3,1,0,3,3,11,7,2,'[\"goal_difference\", \"goals_scored\", \"head_to_head\"]',1,'','Luật mặc định',NULL),(27,13,1,'2026-07-16 10:30:26.000','2026-07-16 10:30:26.000',NULL,3,1,0,3,3,11,1,2,'[\"goal_difference\", \"goals_scored\", \"head_to_head\"]',1,'Giải [ Season Test 01 — 8 teams single group] áp dụng luật: Đội hình 6-11 cầu thủ. Điểm số: Thắng (3) - Hòa (1) - Thua (0). ','Luật mặc định',NULL);
/*!40000 ALTER TABLE `tournament_rules` ENABLE KEYS */;
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
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tournaments_name_key` (`name`),
  KEY `tournaments_user_id_fkey` (`user_id`),
  CONSTRAINT `tournaments_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tournaments`
--

LOCK TABLES `tournaments` WRITE;
/*!40000 ALTER TABLE `tournaments` DISABLE KEYS */;
INSERT INTO `tournaments` VALUES (1,'Victory Championship','Sân chơi cạnh tranh dành cho các đội tuyển hàng đầu.','https://res.cloudinary.com/dwczzcsxh/image/upload/v1781621083/tournaments/logo/202606/89989f33-3db3-48d6-9499-3cb0293f3c22.jpg',0,'2026-06-16 14:44:44.148','2026-07-09 14:07:20.000',NULL,1),(2,'Giải Bóng Đá Thành Phố 2024','Giải bóng đá thường niên cấp thành phố',NULL,0,'2026-06-19 04:29:32.605','2026-07-09 14:07:43.000',NULL,1),(12,'sdadasd','dasda',NULL,0,'2026-07-08 02:25:31.000','2026-07-16 11:20:56.000',NULL,1),(13,'Giai sinh vien ','danh cho sinh vien',NULL,1,'2026-07-16 10:30:26.000','2026-07-16 10:30:26.000',NULL,1);
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
INSERT INTO `user_role` VALUES (1,1),(2,2),(2,3),(3,3),(4,4),(5,5),(110,5),(111,5),(112,5),(113,5),(114,5),(115,5),(116,5),(117,5),(118,5),(119,5),(120,5),(121,5);
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
  `fcm_token` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  UNIQUE KEY `users_email_key` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=122 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'System Admin','admin@gmail.com','$2b$12$vllbUdZfTl9hxTQWLhLwX.LDvtag.SgqiECe/tkXyj2fAVRHJbmVO',NULL,1,1,'2026-06-16 14:41:10.508','2026-06-16 14:41:10.551','2026-06-16 14:41:10.551',NULL),(2,'Nguyễn Văn An','an.2021001@student.edu.vn','$2b$10$j.VxOPbkSuW0aENDu6KMneEgEq2ALm1UgEf1zctwd/Q0c0ciE5hP6','0901000001',1,1,'2026-06-16 14:41:11.022','2026-06-16 14:41:11.031','2026-11-09 01:22:35.000','ducH53wPSv2hbsdpcB79EL:APA91bGy9DEkTOCFQ4bq_m0u4-TeMMf33_fv2QkPXZAflC7cdg1l44i3NcFATAO1uwtYR7D0vh0gwckTfMvNQP9Lt4YpCFa35iwMENjIdWeSRagB6d-UI8U'),(3,'Trần Minh Khoa','khoa.2021002@student.edu.vn','$2b$10$5BunYhpOGf6i0doasuM2YeVG8xRuy/ZIEJDnrRPJrkozuDxlnjeha','0901000002',1,1,'2026-06-16 14:41:11.373','2026-06-16 14:41:11.376','2026-11-09 01:20:46.000',NULL),(4,'Lê Văn Trọng Tài','trongtai@football.local','$2b$12$yMiG112juZa6NQdcCUzfqehh0IeUWD3HvHRqQdZnz5HCWvZw7q1Ya',NULL,1,1,'2026-06-16 14:41:11.919','2026-06-16 14:41:11.921','2026-06-16 14:41:11.921',NULL),(5,'Phạm Thị Bình','binh.register@student.edu.vn','$2b$12$oUN2emgbVkD3/Ef1uYIQ/uwOyzWBmJ7E0695rt3XU/pWaJQE1QHj6','0901000005',1,0,NULL,'2026-06-16 14:41:12.363','2026-06-16 14:41:12.363',NULL),(6,'Leader FC Rồng Vàng','leader_team1@seed.local','$2b$10$PeGHT9ZOlbo6DuHIdszzaeJl6g46NC/jtDsZsPY7ZsRRLQnjyPxfm','0767554546',1,1,'2026-06-19 04:29:32.922','2026-06-19 04:29:32.927','2026-11-09 02:22:52.000','ducH53wPSv2hbsdpcB79EL:APA91bGy9DEkTOCFQ4bq_m0u4-TeMMf33_fv2QkPXZAflC7cdg1l44i3NcFATAO1uwtYR7D0vh0gwckTfMvNQP9Lt4YpCFa35iwMENjIdWeSRagB6d-UI8U'),(7,'Nguyễn Văn An','player_t1_p1@seed.local','$2b$10$uGi7A/13rSsQasZp1pn7IOHMLNkyXIgOfpP8KLy82lSa326MmH6Qe','0923145255',1,1,'2026-06-19 04:29:32.981','2026-06-19 04:29:32.984','2026-07-09 19:42:49.000','fQlvR_JzQMS1EiH0mTIHzz:APA91bGi6_flhPtTD6dnpa01fkXWsjn-8qq2J1vrKRya4cAbkA1ktLfqshVXVwP4mUrs5rf7EXDj-M3sQK653OEaNbD_h-pSrBoT3BSvVizfTHWULKWlqOY'),(8,'Trần Thị Bình','player_t1_p2@seed.local','$2b$10$A901Py56z4js4IT8u.ewXeZQSMyT0thmTP6/L68/h2pzxfFynKDUu','022222201545654554',1,1,'2026-06-19 04:29:33.082','2026-06-19 04:29:33.085','2026-06-29 02:03:23.000',NULL),(9,'Lê Văn Cường','player_t1_p3@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:33.125','2026-06-19 04:29:33.128','2026-06-19 04:29:33.128',NULL),(10,'Phạm Thị Dung','player_t1_p4@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:33.166','2026-06-19 04:29:33.169','2026-06-19 04:29:33.169',NULL),(11,'Hoàng Văn Em','player_t1_p5@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:33.202','2026-06-19 04:29:33.204','2026-06-19 04:29:33.204',NULL),(12,'Vũ Thị Phương','player_t1_p6@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:33.233','2026-06-19 04:29:33.235','2026-06-19 04:29:33.235',NULL),(13,'Đặng Văn Giang','player_t1_p7@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:33.267','2026-06-19 04:29:33.270','2026-06-19 04:29:33.270',NULL),(14,'Bùi Thị Hoa','player_t1_p8@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:33.303','2026-06-19 04:29:33.306','2026-06-19 04:29:33.306',NULL),(15,'Đỗ Văn Inh','player_t1_p9@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:33.341','2026-06-19 04:29:33.344','2026-06-19 04:29:33.344',NULL),(16,'Ngô Thị Kim','player_t1_p10@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:33.373','2026-06-19 04:29:33.376','2026-06-19 04:29:33.376',NULL),(17,'Dương Văn Long','player_t1_p11@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:33.410','2026-06-19 04:29:33.412','2026-06-19 04:29:33.412',NULL),(18,'Mai Thị Minh','player_t1_p12@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:33.445','2026-06-19 04:29:33.448','2026-06-19 04:29:33.448',NULL),(19,'Leader CLB Sao Biển','leader_team2@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:33.760','2026-06-19 04:29:33.762','2026-06-19 04:29:33.762',NULL),(20,'Lý Văn Năm','player_t2_p1@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:33.781','2026-06-19 04:29:33.783','2026-06-19 04:29:33.783',NULL),(21,'Trương Thị Oanh','player_t2_p2@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:33.820','2026-06-19 04:29:33.823','2026-06-19 04:29:33.823',NULL),(22,'Huỳnh Văn Phú','player_t2_p3@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:33.857','2026-06-19 04:29:33.860','2026-06-19 04:29:33.860',NULL),(23,'Phan Thị Quỳnh','player_t2_p4@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:33.893','2026-06-19 04:29:33.895','2026-06-19 04:29:33.895',NULL),(24,'Đinh Văn Rồng','player_t2_p5@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:33.924','2026-06-19 04:29:33.926','2026-06-19 04:29:33.926',NULL),(25,'Cao Thị Sen','player_t2_p6@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:33.954','2026-06-19 04:29:33.956','2026-06-19 04:29:33.956',NULL),(26,'Tô Văn Thắng','player_t2_p7@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:33.987','2026-06-19 04:29:33.990','2026-06-19 04:29:33.990',NULL),(27,'Lưu Thị Uyên','player_t2_p8@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:34.019','2026-06-19 04:29:34.022','2026-06-19 04:29:34.022',NULL),(28,'Kiều Văn Vinh','player_t2_p9@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:34.052','2026-06-19 04:29:34.054','2026-06-19 04:29:34.054',NULL),(29,'Châu Thị Xuân','player_t2_p10@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:34.085','2026-06-19 04:29:34.088','2026-06-19 04:29:34.088',NULL),(30,'Hồ Văn Yên','player_t2_p11@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:34.122','2026-06-19 04:29:34.124','2026-06-19 04:29:34.124',NULL),(31,'Đào Thị Zung','player_t2_p12@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:34.154','2026-06-19 04:29:34.156','2026-06-19 04:29:34.156',NULL),(32,'Leader FC Hùng Mạnh','leader_team3@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:34.372','2026-06-19 04:29:34.374','2026-06-19 04:29:34.374',NULL),(33,'Mạc Văn Anh','player_t3_p1@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:34.391','2026-06-19 04:29:34.393','2026-06-19 04:29:34.393',NULL),(34,'Từ Thị Bảo','player_t3_p2@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:34.424','2026-06-19 04:29:34.427','2026-06-19 04:29:34.427',NULL),(35,'Tăng Văn Chi','player_t3_p3@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:34.462','2026-06-19 04:29:34.465','2026-06-19 04:29:34.465',NULL),(36,'Văn Thị Diệu','player_t3_p4@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:34.497','2026-06-19 04:29:34.500','2026-06-19 04:29:34.500',NULL),(37,'Nghiêm Văn Ến','player_t3_p5@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:34.529','2026-06-19 04:29:34.531','2026-06-19 04:29:34.531',NULL),(38,'Sầm Thị Én','player_t3_p6@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:34.560','2026-06-19 04:29:34.562','2026-06-19 04:29:34.562',NULL),(39,'Ông Văn Gà','player_t3_p7@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:34.594','2026-06-19 04:29:34.596','2026-06-19 04:29:34.596',NULL),(40,'Hà Thị Hải','player_t3_p8@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:34.627','2026-06-19 04:29:34.630','2026-06-19 04:29:34.630',NULL),(41,'Khúc Văn Ích','player_t3_p9@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:34.659','2026-06-19 04:29:34.661','2026-06-19 04:29:34.661',NULL),(42,'Tề Thị Lan','player_t3_p10@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:34.693','2026-06-19 04:29:34.695','2026-06-19 04:29:34.695',NULL),(43,'Lạc Văn Mạnh','player_t3_p11@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:34.720','2026-06-19 04:29:34.722','2026-06-19 04:29:34.722',NULL),(44,'Tiêu Thị Nga','player_t3_p12@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:34.746','2026-06-19 04:29:34.748','2026-06-19 04:29:34.748',NULL),(45,'Leader CLB Thần Tốc','leader_team4@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:34.928','2026-06-19 04:29:34.930','2026-06-19 04:29:34.930',NULL),(46,'Mao Văn Ổn','player_t4_p1@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:34.947','2026-06-19 04:29:34.949','2026-06-19 04:29:34.949',NULL),(47,'Hứa Thị Phi','player_t4_p2@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:34.980','2026-06-19 04:29:34.982','2026-06-19 04:29:34.982',NULL),(48,'Vu Văn Quân','player_t4_p3@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.004','2026-06-19 04:29:35.005','2026-06-19 04:29:35.005',NULL),(49,'Rong Thị Sang','player_t4_p4@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.026','2026-06-19 04:29:35.027','2026-06-19 04:29:35.027',NULL),(50,'Vàng Văn Thịnh','player_t4_p5@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.047','2026-06-19 04:29:35.049','2026-06-19 04:29:35.049',NULL),(51,'Ưu Thị Ung','player_t4_p6@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.069','2026-06-19 04:29:35.071','2026-06-19 04:29:35.071',NULL),(52,'Xu Văn Vỹ','player_t4_p7@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.091','2026-06-19 04:29:35.093','2026-06-19 04:29:35.093',NULL),(53,'Yên Thị Xuyên','player_t4_p8@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.117','2026-06-19 04:29:35.119','2026-06-19 04:29:35.119',NULL),(54,'Zi Văn Ý','player_t4_p9@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.145','2026-06-19 04:29:35.147','2026-06-19 04:29:35.147',NULL),(55,'An Thị Bé','player_t4_p10@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.175','2026-06-19 04:29:35.178','2026-06-19 04:29:35.178',NULL),(56,'Ba Văn Cao','player_t4_p11@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.205','2026-06-19 04:29:35.208','2026-06-19 04:29:35.208',NULL),(57,'Chị Thị Dạy','player_t4_p12@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.237','2026-06-19 04:29:35.239','2026-06-19 04:29:35.239',NULL),(58,'Leader FC Bất Bại','leader_team5@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.394','2026-06-19 04:29:35.395','2026-06-19 04:29:35.395',NULL),(59,'Em Văn Phú','player_t5_p1@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.407','2026-06-19 04:29:35.409','2026-06-19 04:29:35.409',NULL),(60,'Kì Thị Lạ','player_t5_p2@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.435','2026-06-19 04:29:35.437','2026-06-19 04:29:35.437',NULL),(61,'Mới Văn Nhỏ','player_t5_p3@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.465','2026-06-19 04:29:35.467','2026-06-19 04:29:35.467',NULL),(62,'Nhất Thị Ổn','player_t5_p4@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.493','2026-06-19 04:29:35.495','2026-06-19 04:29:35.495',NULL),(63,'Phúc Văn Quý','player_t5_p5@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.521','2026-06-19 04:29:35.523','2026-06-19 04:29:35.523',NULL),(64,'Rõ Thị Sáng','player_t5_p6@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.549','2026-06-19 04:29:35.551','2026-06-19 04:29:35.551',NULL),(65,'Tốt Văn Uy','player_t5_p7@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.577','2026-06-19 04:29:35.579','2026-06-19 04:29:35.579',NULL),(66,'Vui Thị Xanh','player_t5_p8@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.603','2026-06-19 04:29:35.605','2026-06-19 04:29:35.605',NULL),(67,'Yêu Văn Zin','player_t5_p9@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.625','2026-06-19 04:29:35.627','2026-06-19 04:29:35.627',NULL),(68,'Ân Thị Bình','player_t5_p10@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.648','2026-06-19 04:29:35.650','2026-06-19 04:29:35.650',NULL),(69,'Bất Văn Chiến','player_t5_p11@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.669','2026-06-19 04:29:35.671','2026-06-19 04:29:35.671',NULL),(70,'Đạt Thị Đẹp','player_t5_p12@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.688','2026-06-19 04:29:35.689','2026-06-19 04:29:35.689',NULL),(71,'Leader CLB Chiến Thắng','leader_team6@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.790','2026-06-19 04:29:35.792','2026-06-19 04:29:35.792',NULL),(72,'Hiền Văn Giỏi','player_t6_p1@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.803','2026-06-19 04:29:35.805','2026-06-19 04:29:35.805',NULL),(73,'Khỏe Thị Lành','player_t6_p2@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.826','2026-06-19 04:29:35.828','2026-06-19 04:29:35.828',NULL),(74,'Mạnh Văn Ngay','player_t6_p3@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.845','2026-06-19 04:29:35.847','2026-06-19 04:29:35.847',NULL),(75,'Nhân Thị Ổn','player_t6_p4@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.871','2026-06-19 04:29:35.874','2026-06-19 04:29:35.874',NULL),(76,'Phước Văn Quang','player_t6_p5@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.897','2026-06-19 04:29:35.899','2026-06-19 04:29:35.899',NULL),(77,'Rực Thị Sáng','player_t6_p6@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.933','2026-06-19 04:29:35.935','2026-06-19 04:29:35.935',NULL),(78,'Tài Văn Uy','player_t6_p7@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.962','2026-06-19 04:29:35.964','2026-06-19 04:29:35.964',NULL),(79,'Vững Thị Xây','player_t6_p8@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:35.987','2026-06-19 04:29:35.990','2026-06-19 04:29:35.990',NULL),(80,'Yên Văn Trong','player_t6_p9@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.015','2026-06-19 04:29:36.017','2026-06-19 04:29:36.017',NULL),(81,'Ổn Thị Bình','player_t6_p10@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.032','2026-06-19 04:29:36.033','2026-06-19 04:29:36.033',NULL),(82,'Bền Văn Chí','player_t6_p11@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.049','2026-06-19 04:29:36.050','2026-06-19 04:29:36.050',NULL),(83,'Đức Thị Độ','player_t6_p12@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.067','2026-06-19 04:29:36.069','2026-06-19 04:29:36.069',NULL),(84,'Leader FC Bão Lửa','leader_team7@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.164','2026-06-19 04:29:36.166','2026-06-19 04:29:36.166',NULL),(85,'Hòa Văn Giải','player_t7_p1@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.179','2026-06-19 04:29:36.181','2026-06-19 04:29:36.181',NULL),(86,'Kiên Thị Lực','player_t7_p2@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.202','2026-06-19 04:29:36.204','2026-06-19 04:29:36.204',NULL),(87,'Minh Văn Nhanh','player_t7_p3@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.224','2026-06-19 04:29:36.225','2026-06-19 04:29:36.225',NULL),(88,'Nhờ Thị Ở','player_t7_p4@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.245','2026-06-19 04:29:36.247','2026-06-19 04:29:36.247',NULL),(89,'Phúc Văn Quý','player_t7_p5@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.269','2026-06-19 04:29:36.271','2026-06-19 04:29:36.271',NULL),(90,'Rộng Thị Sạch','player_t7_p6@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.294','2026-06-19 04:29:36.296','2026-06-19 04:29:36.296',NULL),(91,'Thắng Văn Uy','player_t7_p7@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.321','2026-06-19 04:29:36.323','2026-06-19 04:29:36.323',NULL),(92,'Vẻ Thị Xinh','player_t7_p8@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.347','2026-06-19 04:29:36.350','2026-06-19 04:29:36.350',NULL),(93,'Yêu Văn Zay','player_t7_p9@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.373','2026-06-19 04:29:36.375','2026-06-19 04:29:36.375',NULL),(94,'Ân Thị Ban','player_t7_p10@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.397','2026-06-19 04:29:36.399','2026-06-19 04:29:36.399',NULL),(95,'Bền Văn Cao','player_t7_p11@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.420','2026-06-19 04:29:36.421','2026-06-19 04:29:36.421',NULL),(96,'Chín Thị Dồi','player_t7_p12@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.441','2026-06-19 04:29:36.442','2026-06-19 04:29:36.442',NULL),(97,'Leader CLB Thiên Lôi','leader_team8@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.538','2026-06-19 04:29:36.539','2026-06-19 04:29:36.539',NULL),(98,'Em Văn Phúc','player_t8_p1@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.549','2026-06-19 04:29:36.551','2026-06-19 04:29:36.551',NULL),(99,'Kha Thị Lắm','player_t8_p2@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.572','2026-06-19 04:29:36.574','2026-06-19 04:29:36.574',NULL),(100,'Mau Văn Nhớ','player_t8_p3@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.589','2026-06-19 04:29:36.591','2026-06-19 04:29:36.591',NULL),(101,'Nhớ Thị Ổn','player_t8_p4@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.607','2026-06-19 04:29:36.608','2026-06-19 04:29:36.608',NULL),(102,'Phú Văn Quý','player_t8_p5@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.624','2026-06-19 04:29:36.625','2026-06-19 04:29:36.625',NULL),(103,'Rợp Thị Sóng','player_t8_p6@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.644','2026-06-19 04:29:36.646','2026-06-19 04:29:36.646',NULL),(104,'Tốt Văn Uy','player_t8_p7@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.664','2026-06-19 04:29:36.666','2026-06-19 04:29:36.666',NULL),(105,'Vui Thị Xuân','player_t8_p8@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.685','2026-06-19 04:29:36.688','2026-06-19 04:29:36.688',NULL),(106,'Yên Văn Dạy','player_t8_p9@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.710','2026-06-19 04:29:36.712','2026-06-19 04:29:36.712',NULL),(107,'Ổn Thị Bền','player_t8_p10@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.739','2026-06-19 04:29:36.742','2026-06-19 04:29:36.742',NULL),(108,'Bất Văn Chi','player_t8_p11@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.766','2026-06-19 04:29:36.768','2026-06-19 04:29:36.768',NULL),(109,'Đại Thị Đồng','player_t8_p12@seed.local','$2b$10$ADWwm2M5Nsm1GTn8YEAhKOqQgctX8wSO8HM5Lv1XfUq5vmbbm3F7C',NULL,1,1,'2026-06-19 04:29:36.791','2026-06-19 04:29:36.794','2026-06-19 04:29:36.794',NULL),(110,'Nguyentui','d@gmail.com','$2b$10$BUw4OqgOCtFKve6B3DNX3usoNVAWQZW/liazTrLC34TjIibvkwsVK','0884562342',1,1,'2026-06-27 01:02:16.000','2026-06-27 01:01:50.000','2026-10-15 00:45:22.000','cpbpNs_QSVusjtcWTCPlhu:APA91bFTT-zcgmeFDgAQy2Myc1B8Lr33SFnA9WuxdJmurYC25bHr_QBXO9yK7iiEHdHY5T3F9vbLH6lI2z6OVpTdXgXMOb696Bu4XeXFP6IcyUWQWKOQgZ8'),(111,'d1','d1@gmail.com','$2b$10$l9S1LChl6dsey0sQMhOXX.DPsAO/vjvDAWGIDmVrb.YSDdF7u1uca','0564444448',1,1,'2026-06-29 05:46:06.000','2026-06-29 04:48:35.000','2026-07-06 06:58:09.000','c4rMDxQmSky0vwSaxSFD8Z:APA91bE2Sw1yV-1hpQqYOFGISd5K5CfmWwJaYH5520ZQDjwmP0WllLet0P-lBJuakV0gED1xGioKV47N_dzDeYvhhuX6pgDVMkzMyrGBnz9fWd--wAhmWiI'),(112,'nguyenvana','a@gmail.com','$2b$10$kRUjuDdAodX0Vi3MYMFUXOwg6reR3rt.i43p0q9sSsZex0wk2lEp2','0546878451',1,1,'2026-07-16 08:45:34.000','2026-07-07 01:21:58.000','2026-07-16 10:26:53.000','cpbpNs_QSVusjtcWTCPlhu:APA91bFTT-zcgmeFDgAQy2Myc1B8Lr33SFnA9WuxdJmurYC25bHr_QBXO9yK7iiEHdHY5T3F9vbLH6lI2z6OVpTdXgXMOb696Bu4XeXFP6IcyUWQWKOQgZ8'),(113,'nguyenvanb','b@gmai.com','$2b$10$KCN9cgpI6OJw9zaISy1EN.8QQVXKPZ35OZdueWHdoN7JmayZY376a','0123456789',1,0,NULL,'2026-07-07 01:23:37.000','2026-07-08 06:07:27.000','eLqfraA9RoWQsWMjmCZndw:APA91bGEOZk2kvc3fwstPY8srmXUHWZurKMzW7y4Mfo36Z_gj0p1iT1_wzGD_akf97XHPXo3V51EpMTTthLTtZNSAW-LCeAWhN-z_5EwvK6g-rhp8ANoimU'),(114,'nguyenvanc','c@gmail.com','$2b$10$nS.22i7.cVrpl8XlVDlo.uJqIG8uQg7WNkrZGEu5Mryd36XYbyeVa','0548745120',1,0,NULL,'2026-07-07 01:26:18.000','2026-07-08 11:21:11.000','eLqfraA9RoWQsWMjmCZndw:APA91bGEOZk2kvc3fwstPY8srmXUHWZurKMzW7y4Mfo36Z_gj0p1iT1_wzGD_akf97XHPXo3V51EpMTTthLTtZNSAW-LCeAWhN-z_5EwvK6g-rhp8ANoimU'),(115,'nguyenvanr','r@gmail.com','$2b$10$g/W0UbZbUN41zD7nWxstiuAhF26callnE0Eu5UnvumplGIFII4bMS','0293013123',1,1,'2026-07-09 01:41:50.000','2026-07-07 01:37:05.000','2026-07-15 00:37:44.000','fIbcncmyT4-7KEOn26TIP6:APA91bEsQF0b8GzFbZp_4obXjBNVfYdFrS8VvEeTRL0mOU1AySkPqhRoIA3sjAy8eGyenePvpMXyWmfVCwlbmVgnXbJ6uPtyeC7TJIyjrQhQnnK-PLntM4A'),(116,'nguyen truong thinh','t1@gmail.com','$2b$10$zcMBfT5rBuCayoQ/sQRLheGlDV6vH2avdPuICmUIGLXT7Qc3JjCZq','0987654321',1,1,'2026-07-16 10:43:29.000','2026-07-16 10:27:52.000','2026-07-16 14:28:14.000','cpbpNs_QSVusjtcWTCPlhu:APA91bFTT-zcgmeFDgAQy2Myc1B8Lr33SFnA9WuxdJmurYC25bHr_QBXO9yK7iiEHdHY5T3F9vbLH6lI2z6OVpTdXgXMOb696Bu4XeXFP6IcyUWQWKOQgZ8'),(117,'nguyen van t','t2@gmail.com','$2b$10$VW7lJzkN68YrJuAxhB079.fDVEMbfhvu96ZcS8NuwKahbDM1LqVk.','0192384323',1,1,'2026-07-16 11:15:31.000','2026-07-16 11:12:20.000','2026-07-16 12:08:59.000','cpbpNs_QSVusjtcWTCPlhu:APA91bFTT-zcgmeFDgAQy2Myc1B8Lr33SFnA9WuxdJmurYC25bHr_QBXO9yK7iiEHdHY5T3F9vbLH6lI2z6OVpTdXgXMOb696Bu4XeXFP6IcyUWQWKOQgZ8'),(118,'nguyen iu em','t3@gmail.com','$2b$10$19McgOWh7wBQfZqZXaq4Ue95qr03.F0EcKglmDhpXCDotuU1QmFEy','0938473243',1,1,'2026-07-16 11:16:38.000','2026-07-16 11:13:02.000','2026-07-16 12:08:33.000','cpbpNs_QSVusjtcWTCPlhu:APA91bFTT-zcgmeFDgAQy2Myc1B8Lr33SFnA9WuxdJmurYC25bHr_QBXO9yK7iiEHdHY5T3F9vbLH6lI2z6OVpTdXgXMOb696Bu4XeXFP6IcyUWQWKOQgZ8'),(119,'ngyen anh iu','t4@gmail.com','$2b$10$mKehmjuRaJPO1Cr7w3cCHeVoALQ1I53MfWWzg.pqoEe8dDY4tXlhC','0934285483',1,1,'2026-07-16 11:17:32.000','2026-07-16 11:13:34.000','2026-07-16 12:02:35.000','cpbpNs_QSVusjtcWTCPlhu:APA91bFTT-zcgmeFDgAQy2Myc1B8Lr33SFnA9WuxdJmurYC25bHr_QBXO9yK7iiEHdHY5T3F9vbLH6lI2z6OVpTdXgXMOb696Bu4XeXFP6IcyUWQWKOQgZ8'),(120,'nguyen em anh','t5@gmail.com','$2b$10$8yYJg8ASlrIzV7K87yBHOuQR6XjzHtSe4K8GSAG3CJou0szYsMnYq','0392943294',1,0,NULL,'2026-07-16 11:14:43.000','2026-07-16 11:14:43.000',NULL),(121,'nguyenanhem','t6@gmail.com','$2b$10$zp2XwsGuvNrUL/3c0G4Nsua4ot5gteypeoq90CChhJUxleG4Il5gG','0934824827',1,0,NULL,'2026-07-16 11:15:14.000','2026-07-16 11:15:14.000',NULL);
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `venues_name_key` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `venues`
--

LOCK TABLES `venues` WRITE;
/*!40000 ALTER TABLE `venues` DISABLE KEYS */;
INSERT INTO `venues` VALUES (1,'Sân bóng An Phú','Địa chỉ 1, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(2,'Sân bóng Bình Minh','Địa chỉ 2, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(3,'Sân bóng Thành Công','Địa chỉ 3, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(4,'Sân bóng Hoàng Gia','Địa chỉ 4, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(5,'Sân bóng Phú Mỹ','Địa chỉ 5, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(6,'Sân bóng Thanh Niên','Địa chỉ 6, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(7,'Sân bóng Đông Á','Địa chỉ 7, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(8,'Sân bóng Tây Đô','Địa chỉ 8, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(9,'Sân bóng Đại Nam','Địa chỉ 9, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(10,'Sân bóng Hòa Bình','Địa chỉ 10, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(11,'Sân bóng Victory','Địa chỉ 11, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(12,'Sân bóng Galaxy','Địa chỉ 12, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(13,'Sân bóng Dragon','Địa chỉ 13, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(14,'Sân bóng Green Field','Địa chỉ 14, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(15,'Sân bóng Golden Star','Địa chỉ 15, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(16,'Sân bóng Tân Sơn','Địa chỉ 16, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(17,'Sân bóng Long Thành','Địa chỉ 17, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(18,'Sân bóng Thống Nhất','Địa chỉ 18, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(19,'Sân bóng Minh Châu','Địa chỉ 19, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(20,'Sân bóng Kim Cương','Địa chỉ 20, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(21,'Sân bóng Blue Sky','Địa chỉ 21, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(22,'Sân bóng Red Star','Địa chỉ 22, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(23,'Sân bóng Future','Địa chỉ 23, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(24,'Sân bóng Champions','Địa chỉ 24, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(25,'Sân bóng Elite','Địa chỉ 25, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(26,'Sân bóng City Sport','Địa chỉ 26, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(27,'Sân bóng River Park','Địa chỉ 27, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(28,'Sân bóng Central','Địa chỉ 28, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(29,'Sân bóng Arena','Địa chỉ 29, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL),(30,'Sân bóng Pro League','Địa chỉ 30, TP.HCM',1,'2026-06-16 14:41:12.386','2026-06-16 14:41:12.386',NULL);
/*!40000 ALTER TABLE `venues` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'football_management'
--

--
-- Dumping routines for database 'football_management'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-09  2:32:12
