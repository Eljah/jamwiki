-- MySQL dump 10.13  Distrib 5.4.1-beta, for unknown-linux-gnu (x86_64)
--
-- Host: localhost    Database: jamwikidev
-- ------------------------------------------------------
-- Server version	5.4.1-beta-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `jam_authorities`
--

DROP TABLE IF EXISTS `jam_authorities`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `jam_authorities` (
  `username` varchar(100) NOT NULL,
  `authority` varchar(30) NOT NULL,
  UNIQUE KEY `jam_u_auth` (`username`,`authority`),
  KEY `jam_i_auth_username` (`username`),
  KEY `jam_i_auth_authority` (`authority`),
  CONSTRAINT `jam_f_auth_username` FOREIGN KEY (`username`) REFERENCES `jam_users` (`username`),
  CONSTRAINT `jam_f_auth_authority` FOREIGN KEY (`authority`) REFERENCES `jam_role` (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `jam_authorities`
--

LOCK TABLES `jam_authorities` WRITE;
/*!40000 ALTER TABLE `jam_authorities` DISABLE KEYS */;
INSERT INTO `jam_authorities` VALUES ('admin','ROLE_ADMIN'),('admin','ROLE_SYSADMIN'),('admin','ROLE_TRANSLATE');
/*!40000 ALTER TABLE `jam_authorities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jam_category`
--

DROP TABLE IF EXISTS `jam_category`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `jam_category` (
  `child_topic_id` int(11) NOT NULL,
  `category_name` varchar(255) NOT NULL DEFAULT '',
  `sort_key` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`child_topic_id`,`category_name`),
  UNIQUE KEY `jam_u_cat_pk` (`child_topic_id`,`category_name`),
  KEY `jam_f_cat_child_topic_id` (`child_topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `jam_category`
--

LOCK TABLES `jam_category` WRITE;
/*!40000 ALTER TABLE `jam_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `jam_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jam_file`
--

DROP TABLE IF EXISTS `jam_file`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `jam_file` (
  `file_id` int(11) NOT NULL,
  `virtual_wiki_id` int(11) NOT NULL,
  `file_name` varchar(200) NOT NULL,
  `delete_date` datetime DEFAULT NULL,
  `file_read_only` int(11) NOT NULL DEFAULT '0',
  `file_admin_only` int(11) NOT NULL DEFAULT '0',
  `file_url` varchar(200) NOT NULL,
  `mime_type` varchar(100) NOT NULL,
  `topic_id` int(11) NOT NULL,
  `file_size` int(11) NOT NULL,
  PRIMARY KEY (`file_id`),
  UNIQUE KEY `jam_u_file_url` (`file_url`),
  UNIQUE KEY `jam_u_file_topic` (`virtual_wiki_id`,`topic_id`),
  KEY `jam_f_file_topic` (`topic_id`),
  CONSTRAINT `jam_f_file_vwiki` FOREIGN KEY (`virtual_wiki_id`) REFERENCES `jam_virtual_wiki` (`virtual_wiki_id`),
  CONSTRAINT `jam_f_file_topic` FOREIGN KEY (`topic_id`) REFERENCES `jam_topic` (`topic_id`)  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `jam_file`
--

LOCK TABLES `jam_file` WRITE;
/*!40000 ALTER TABLE `jam_file` DISABLE KEYS */;
/*!40000 ALTER TABLE `jam_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jam_file_version`
--

DROP TABLE IF EXISTS `jam_file_version`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `jam_file_version` (
  `file_version_id` int(11) NOT NULL,
  `file_id` int(11) NOT NULL,
  `upload_comment` varchar(200) DEFAULT NULL,
  `file_url` varchar(200) NOT NULL,
  `wiki_user_id` int(11) DEFAULT NULL,
  `wiki_user_display` varchar(100) NOT NULL,
  `upload_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mime_type` varchar(100) NOT NULL,
  `file_size` int(11) NOT NULL,
  PRIMARY KEY (`file_version_id`),
  UNIQUE KEY `jam_u_filev_url` (`file_url`),
  KEY `jam_f_filev_file` (`file_id`),
  KEY `jam_f_filev_wuser` (`wiki_user_id`),
  CONSTRAINT `jam_f_filev_file` FOREIGN KEY (`file_id`) REFERENCES `jam_file` (`file_id`),
  CONSTRAINT `jam_f_filev_wuser` FOREIGN KEY (`wiki_user_id`) REFERENCES `jam_wiki_user` (`wiki_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `jam_file_version`
--

LOCK TABLES `jam_file_version` WRITE;
/*!40000 ALTER TABLE `jam_file_version` DISABLE KEYS */;
/*!40000 ALTER TABLE `jam_file_version` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jam_group`
--

DROP TABLE IF EXISTS `jam_group`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `jam_group` (
  `group_id` int(11) NOT NULL,
  `group_name` varchar(30) NOT NULL,
  `group_description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`group_id`),
  UNIQUE KEY `jam_u_group_name` (`group_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `jam_group`
--

LOCK TABLES `jam_group` WRITE;
/*!40000 ALTER TABLE `jam_group` DISABLE KEYS */;
INSERT INTO `jam_group` VALUES (1,'GROUP_ANONYMOUS','All non-logged in users are automatically assigned to the anonymous group.'),(2,'GROUP_REGISTERED_USER','All logged in users are automatically assigned to the registered user group.');
/*!40000 ALTER TABLE `jam_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jam_group_authorities`
--

DROP TABLE IF EXISTS `jam_group_authorities`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `jam_group_authorities` (
  `group_id` int(11) NOT NULL,
  `authority` varchar(30) NOT NULL,
  UNIQUE KEY `jam_u_gauth` (`group_id`,`authority`),
  KEY `jam_i_gauth_group` (`group_id`),
  KEY `jam_i_gauth_authority` (`authority`),
  CONSTRAINT `jam_f_gauth_group` FOREIGN KEY (`group_id`) REFERENCES `jam_group` (`group_id`),
  CONSTRAINT `jam_f_gauth_authority` FOREIGN KEY (`authority`) REFERENCES `jam_role` (`role_name`)  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `jam_group_authorities`
--

LOCK TABLES `jam_group_authorities` WRITE;
/*!40000 ALTER TABLE `jam_group_authorities` DISABLE KEYS */;
INSERT INTO `jam_group_authorities` VALUES (1,'ROLE_EDIT_EXISTING'),(1,'ROLE_EDIT_NEW'),(1,'ROLE_UPLOAD'),(1,'ROLE_VIEW'),(2,'ROLE_EDIT_EXISTING'),(2,'ROLE_EDIT_NEW'),(2,'ROLE_MOVE'),(2,'ROLE_UPLOAD'),(2,'ROLE_VIEW');
/*!40000 ALTER TABLE `jam_group_authorities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jam_group_members`
--

DROP TABLE IF EXISTS `jam_group_members`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `jam_group_members` (
  `id` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `group_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `jam_f_gmemb_username` (`username`),
  KEY `jam_f_gmemb_group` (`group_id`),
  CONSTRAINT `jam_f_gmemb_username` FOREIGN KEY (`username`) REFERENCES `jam_users` (`username`),
  CONSTRAINT `jam_f_gmemb_group` FOREIGN KEY (`group_id`) REFERENCES `jam_group` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `jam_group_members`
--

LOCK TABLES `jam_group_members` WRITE;
/*!40000 ALTER TABLE `jam_group_members` DISABLE KEYS */;
INSERT INTO `jam_group_members` VALUES (1,'admin',2);
/*!40000 ALTER TABLE `jam_group_members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jam_recent_change`
--

DROP TABLE IF EXISTS `jam_recent_change`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `jam_recent_change` (
  `topic_version_id` int(11) NOT NULL,
  `previous_topic_version_id` int(11) DEFAULT NULL,
  `topic_id` int(11) NOT NULL,
  `topic_name` varchar(200) NOT NULL,
  `edit_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `edit_comment` varchar(200) DEFAULT NULL,
  `wiki_user_id` int(11) DEFAULT NULL,
  `display_name` varchar(200) NOT NULL,
  `edit_type` int(11) NOT NULL,
  `virtual_wiki_id` int(11) NOT NULL,
  `virtual_wiki_name` varchar(100) NOT NULL,
  `characters_changed` int(11) DEFAULT NULL,
  PRIMARY KEY (`topic_version_id`),
  UNIQUE KEY `jam_u_rc_pk` (`topic_version_id`),
  KEY `jam_f_rc_p_topic_v` (`previous_topic_version_id`),
  KEY `jam_f_rc_topic` (`topic_id`),
  KEY `jam_f_rc_wuser` (`wiki_user_id`),
  KEY `jam_f_rc_vwiki` (`virtual_wiki_id`),
  KEY `jam_rc_edit_date_wiki_name` (`edit_date`,`virtual_wiki_name`),
  CONSTRAINT `jam_f_rc_p_topic_v` FOREIGN KEY (`previous_topic_version_id`) REFERENCES `jam_topic_version` (`topic_version_id`),
  CONSTRAINT `jam_f_rc_topic` FOREIGN KEY (`topic_id`) REFERENCES `jam_topic` (`topic_id`),
  CONSTRAINT `jam_f_rc_topic_ver` FOREIGN KEY (`topic_version_id`) REFERENCES `jam_topic_version` (`topic_version_id`),
  CONSTRAINT `jam_f_rc_vwiki` FOREIGN KEY (`virtual_wiki_id`) REFERENCES `jam_virtual_wiki` (`virtual_wiki_id`),
  CONSTRAINT `jam_f_rc_wuser` FOREIGN KEY (`wiki_user_id`) REFERENCES `jam_wiki_user` (`wiki_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `jam_recent_change`
--

LOCK TABLES `jam_recent_change` WRITE;
/*!40000 ALTER TABLE `jam_recent_change` DISABLE KEYS */;
INSERT INTO `jam_recent_change` VALUES (1,NULL,1,'StartingPoints','2009-06-16 19:13:06','Automatically created by system setup',1,'admin',1,1,'en',1439),(2,NULL,2,'LeftMenu','2009-06-16 19:13:06','Automatically created by system setup',1,'admin',1,1,'en',354),(3,NULL,3,'BottomArea','2009-06-16 19:13:06','Automatically created by system setup',1,'admin',1,1,'en',73),(4,NULL,4,'StyleSheet','2009-06-16 19:13:06','Automatically created by system setup',1,'admin',1,1,'en',7900),(5,NULL,5,'HelloWorld','2009-06-16 19:13:53','',NULL,'127.0.0.1',1,1,'en',12),(6,5,5,'HelloWorld','2009-06-16 19:19:14','',1,'admin',1,1,'en',20),(7,6,5,'HelloWorld','2009-06-16 19:20:09','',NULL,'127.0.0.1',1,1,'en',18);
/*!40000 ALTER TABLE `jam_recent_change` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jam_role`
--

DROP TABLE IF EXISTS `jam_role`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `jam_role` (
  `role_name` varchar(30) NOT NULL,
  `role_description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `jam_role`
--

LOCK TABLES `jam_role` WRITE;
/*!40000 ALTER TABLE `jam_role` DISABLE KEYS */;
INSERT INTO `jam_role` VALUES ('ROLE_ADMIN','Provides the ability to perform wiki maintenance tasks not available to normal users.'),('ROLE_EDIT_EXISTING','Allows a user to edit an existing topic.'),('ROLE_EDIT_NEW','Allows a user to create a new topic.'),('ROLE_MOVE','Allows a user to move a topic to a different name.'),('ROLE_SYSADMIN','Allows access to set database parameters, modify parser settings, and set other wiki system settings.'),('ROLE_TRANSLATE','Allows access to the translation tool used for modifying the values of message keys used to display text on the wiki.'),('ROLE_UPLOAD','Allows a user to upload a file to the wiki.'),('ROLE_VIEW','Allows a user to view topics on the wiki.');
/*!40000 ALTER TABLE `jam_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jam_topic`
--

DROP TABLE IF EXISTS `jam_topic`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `jam_topic` (
  `topic_id` int(11) NOT NULL AUTO_INCREMENT,
  `virtual_wiki_id` int(11) NOT NULL,
  `topic_name` varchar(200) NOT NULL,
  `delete_date` datetime DEFAULT NULL,
  `topic_read_only` int(11) NOT NULL DEFAULT '0',
  `topic_admin_only` int(11) NOT NULL DEFAULT '0',
  `current_version_id` int(11) DEFAULT NULL,
  `topic_type` int(11) NOT NULL,
  `redirect_to` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`topic_id`),
  UNIQUE KEY `jam_u_topic_id` (`topic_id`),
  UNIQUE KEY `jam_u_topic_name` (`topic_name`,`virtual_wiki_id`,`delete_date`),
  KEY `jam_f_topic_vwiki` (`virtual_wiki_id`),
  KEY `jam_f_topic_topicv` (`current_version_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 PACK_KEYS=0;
SET character_set_client = @saved_cs_client;


--
-- Dumping data for table `jam_topic`
--

LOCK TABLES `jam_topic` WRITE;
/*!40000 ALTER TABLE `jam_topic` DISABLE KEYS */;
INSERT INTO `jam_topic` VALUES (1,1,'StartingPoints',NULL,0,0,1,1,NULL),(2,1,'LeftMenu',NULL,0,1,2,1,NULL),(3,1,'BottomArea',NULL,0,1,3,1,NULL),(4,1,'StyleSheet',NULL,0,1,4,1,NULL),(5,1,'HelloWorld',NULL,0,0,7,1,NULL);
/*!40000 ALTER TABLE `jam_topic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jam_topic_cache`
--

DROP TABLE IF EXISTS `jam_topic_cache`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `jam_topic_cache` (
  `topic_id` int(11) NOT NULL,
  `topic_version_id` int(11) NOT NULL,
  `virtual_wiki_id` int(11) NOT NULL,
  `topic_name` varchar(200) NOT NULL,
  `data` mediumblob,
  PRIMARY KEY (`topic_id`,`topic_version_id`,`virtual_wiki_id`),
  KEY `jam_f_topic_vwiki` (`virtual_wiki_id`),
  KEY `jam_u_topic_name` (`topic_name`,`virtual_wiki_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 PACK_KEYS=0;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `jam_topic_version`
--

DROP TABLE IF EXISTS `jam_topic_version`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `jam_topic_version` (
  `topic_version_id` int(11) NOT NULL AUTO_INCREMENT,
  `topic_id` int(11) NOT NULL,
  `edit_comment` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `version_content` mediumblob,
  `version_content_clean` mediumblob,
  `version_content_short` text CHARACTER SET utf8,
  `wiki_user_id` int(11) DEFAULT NULL,
  `wiki_user_display` varchar(100) CHARACTER SET utf8 NOT NULL,
  `edit_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `edit_type` int(11) NOT NULL,
  `bz_type` tinyint(4) unsigned DEFAULT '0',
  `previous_topic_version_id` int(11) DEFAULT NULL,
  `characters_changed` int(11) DEFAULT NULL,
  PRIMARY KEY (`topic_version_id`),
  UNIQUE KEY `jam_u_topicv_id` (`topic_version_id`),
  KEY `jam_f_topicv_topic` (`topic_id`),
  KEY `jam_f_topicv_wuser` (`wiki_user_id`),
  KEY `jam_f_topicv_pver` (`previous_topic_version_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_bin PACK_KEYS=0;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `jam_topic_version`
--

LOCK TABLES `jam_topic_version` WRITE;
/*!40000 ALTER TABLE `jam_topic_version` DISABLE KEYS */;
INSERT INTO `jam_topic_version` VALUES (1,1,'Automatically created by system setup','<div style=\"text-align:center;font-weight:bold;color:green;font-size:150%\">JAMWiki has been successfully installed!</div>\n\nYou can edit any page by clicking the \"Edit\" tab at the top of the page.  Configuration settings for this installation may be modified from the [[Special:Admin]] page.  In addition, these topics can be updated to change the look and feel of your Wiki:\n\n* [[LeftMenu]] - The left navigation menu that appears on every page.\n* [[BottomArea]] - The footer that appears on every page.\n* [[StyleSheet]] - The stylesheet that contains CSS style information for the site.\n\nFinally, please contribute to the JAMWiki project by reporting the success or failure of your installation at [http://jamwiki.org/ jamwiki.org].  Feedback, questions and bug reports should also be left at [http://jamwiki.org/ jamiki.org] so that JAMWiki can continue to improve.  In addition, [http://jamwiki.org/ jamwiki.org] also has the latest JAMWiki documentation including instructions for further cusomizing your installation.\n\nSome other useful functions:\n\n* [[Special:RecentChanges]] - A list of recently modified Wiki topics.\n* [[Special:Upload]] - Used to upload files and images to the Wiki.\n* [[Special:Search]] - Search the Wiki.\n* [[Special:Account]] - Create an account so that edits are propertly credited.\n* [[Special:Allpages]] - A list of all the topics in the Wiki.\n* [[Special:Specialpages]] - An index of wiki \"Special\" pages.\n',NULL,NULL,1,'127.0.0.1','2009-06-16 19:13:06',1,0,NULL,1439),(2,2,'Automatically created by system setup','* [[StartingPoints|StartingPoints]]\n* [[Special:RecentChanges|Recent Changes]]\n* [[Special:Search|Search]]\n----\n* [[Special:Allpages|All Pages]]\n* [[Special:Specialpages|Special Pages]]\n* [[Special:Upload|Upload File]]\n----\n* [http://jamwiki.org/ JAMWiki]\n* [http://sourceforge.net/projects/jamwiki/ Sourceforge Page]\n* [http://mediawiki.org/ MediaWiki]\n',NULL,NULL,1,'127.0.0.1','2009-06-16 19:13:06',1,0,NULL,354),(3,3,'Automatically created by system setup','<font size=\"-3\">All contents copyright of the author. &copy;2009.</font>\n',NULL,NULL,1,'127.0.0.1','2009-06-16 19:13:06',1,0,NULL,73),(4,4,'Automatically created by system setup','/* blue theme */\n\n/* defaults */\n\nbody {\n	background: #f9f9f9 url(../images/denalibg.jpg) 0 0 no-repeat;\n	color: black;\n	margin: 0;\n	padding: 0;\n}\nbody, input, select {\n	font: 95% sans-serif, tahoma;\n}\na {\n	text-decoration: none;\n	color: #5555bb;\n}\na:hover {\n	border-bottom: 1px dotted #5555bb;\n}\na.logo {\n	border: 0;\n}\na.externallink:after {\n	content:url(\"../images/external.png\");\n}\na.edit {\n	color: #aa1111;\n	border-color: #aa1111;\n}\na.edit:hover {\n	border-bottom: 1px dotted #aa1111;\n}\na.redirect {\n	font-size: 150%;\n	text-decoration: none;\n	margin-left: 20px;\n}\na.redirect:before {\n	content:url(\"../images/redirect-arrow.png\");\n}\na.interwiki:after {\n	content:url(\"../images/external.png\");\n}\np {\n	margin: .4em 0 .5em 0;\n	line-height: 1.5em;\n}\nh1, h2, h3, h4, h5 {\n	color: black;\n	margin: 0;\n	padding: 0.5em 0em 0.2em 0em;\n	text-align: left;\n}\nh1 {\n	border-bottom: 1px solid #D8D8E7;\n	font-size: 200%;\n	font-weight: normal;\n}\nh2 {\n	border-bottom: 1px solid #D8D8E7;\n	font-size: 175%;\n	font-weight: normal;\n}\nh3 {\n	font-size: 150%;\n	font-weight: normal;\n}\nh4 {\n	font-size: 125%;\n	font-weight: bold;\n}\nh5 {\n	font-size: 110%;\n	font-weight: bold;\n}\npre {\n	background-color: #FBFBFD;\n	border: 1px dashed #7E7ECB;\n	color: black;\n	font-size: 110%;\n	line-height: 1.1em;\n	padding: 1em;\n}\ncode {\n	color: black;\n	font-size: 120%;\n}\ndt {\n	font-weight: bold;\n	margin-bottom: .1em;\n}\ndl {\n	margin-top: .2em;\n	margin-bottom: .5em;\n}\ndd {\n	line-height: 1.5em;\n	margin-left: 2em;\n	margin-bottom: .1em;\n}\nul {\n	line-height: 1.5em;\n	list-style-type: square;\n	margin: .3em 0 0 1.5em;\n	padding: 0;\n}\nol {\n	line-height: 1.5em;\n	margin: .3em 0 0 3.2em;\n	padding: 0;\n	list-style-image: none;\n}\nli {\n	margin-bottom: .1em;\n}\ntextarea {\n	width: 99%;\n}\nfieldset {\n	padding: 5px;\n	border: 1px solid #D8D8E7;\n}\nlegend {\n	padding: 0.5em;\n	font-weight: bold;\n}\n.red {\n	font-weight: bold;\n	color: #ff0000;\n}\n.green {\n	font-weight: bold;\n	color: #009900;\n}\ndiv.message {\n	margin: 1.5em 0 1.5em 0;\n	padding: 0 5px;\n}\ndiv.submenu {\n	margin: 1.0em 6em 0.8em 6em;\n	text-align: center;\n}\n.clear {\n	clear: both;\n}\n\n/* global positioning */\n\n#wiki-page {\n	font-size: 100%;\n	color: black;\n	padding: 0 5px;\n}\n#wiki-navigation {\n	position: absolute;\n	top: 5px;\n	left: 5px;\n	font-size: 90%;\n	padding: 5px 5px 5px 0;\n	vertical-align: top;\n	width: 180px;\n}\n#wiki-content {\n	position: relative;\n	margin-left: 185px;\n	font-size: 80%;\n	vertical-align: top;\n}\n#wiki-footer {\n	clear: both;\n	font-size: 80%;\n	color: gray;\n	padding: 15px 0 5px 0;\n	text-align: right;\n}\n\n/* main content area */\n\n#contents {\n	background: white;\n	padding: 14px;\n	padding-bottom: 25px;\n	border: 1px solid #D8D8E7;\n}\n#contents-header {\n	margin-bottom: 0.1em;\n}\n#contents-subheader {\n	font-size: 100%;\n	line-height: 1.2em;\n	margin: 0 0 1.4em 1em;\n	color: #7d7d7d;\n	width: auto;\n}\n\n/* navbar elements */\n\n#logo {\n	border: 0;\n	margin: 8px 0 0 0;\n	text-align: center;\n	vertical-align: middle;\n}\n#nav-menu {\n	background: white;\n	border: 1px solid #D8D8E7;\n	border-bottom: 0;\n	font-size: 90%;\n	margin: 2px 0 8px 0;\n}\n#nav-menu hr {\n	display: none;\n}\n#nav-menu ul {\n	padding: 8px;\n	list-style-type: none;\n	border-bottom: 1px solid #D8D8E7;\n	margin-left: 0;\n}\n#nav-search {\n	background: white;\n	margin: 18px 0 8px 0;\n	padding: 8px;\n	padding-top: 0;\n	text-align: center;\n	border: 1px solid #D8D8E7;\n}\n#nav-search form {\n	padding: 0;\n	margin: 0;\n}\n#nav-search br {\n	padding-top: 4px;\n}\n#nav-search input {\n	margin-top: 8px;\n	background-color: white;\n	border: 1px solid #D8D8E7;\n}\n\n/* diff page */\n\n.diff-delete {\n	float: left;\n	position: relative;\n	background: #eeee88;\n	font-weight: bold;\n	padding: 0.1em;\n	width: 48%;\n	overflow: hidden;\n}\n.diff-add {\n	float: left;\n	position: relative;\n	background: #88ee88;\n	font-weight: bold;\n	padding: 0.1em;\n	width: 48%;\n	overflow: hidden;\n}\n.diff-unchanged {\n	float: left;\n	position: relative;\n	background: #dddddd;\n	padding: 0.1em;\n	width: 48%;\n	overflow: hidden;\n}\n.diff-indicator {\n	float: left;\n	position: relative;\n	font-weight: bold;\n	padding: 0.1em;\n	text-align: right;\n	width: 7px;\n}\n.diff-line {\n	clear: both;\n	padding: 0.5em 0.1em 0.1em 0.1em;\n	font-weight: bold;\n}\n.diff-entry {\n	clear: both;\n	margin: 1px;\n	padding: 1px;\n}\n.diff-change {\n	font-weight: bold;\n	color: red;\n}\n\n/* topic elements */\n\n#toc {\n	border: 1px solid #D8D8E7;\n	margin: 0.2em auto 0.2em 0em;\n	padding: 0.5em 0.8em 0.5em 0.8em;\n	background-color: #fff;\n}\n#toc ul {\n	list-style-type: none;\n	list-style-image: none;\n	margin-left: 0;\n}\n#toc ul ul {\n	margin-left: 1em;\n}\n#category-index {\n	border: 1px solid #aaa;\n	background-color: #f9f9f9;\n	padding: 10px 5px 10px 5px;\n	margin-top: 1em;\n	clear: both;\n}\n#topic-file-download {\n	border: 1px solid #aaa;\n	background-color: #f9f9f9;\n	padding: 10px 5px 10px 5px;\n	margin-top: 1em;\n	clear: both;\n}\n\n/* tab menu */\n\n#tab-menu {\n	padding: 0;\n	margin: 0;\n	margin-left: 6px;\n}\n#tab-menu .tab-item {\n	float: left;\n	background: white;\n	color: blue;\n	border: 1px solid #D8D8E7;\n	border-bottom: none;\n	padding: 0.2em 0.5em 0.2em 0.5em;\n	margin: 0;\n	margin-right: 6px;\n	font-size: 85%;\n	text-align: center;\n}\n\n/* user menu */\n\n#user-menu {\n	float: right;\n	margin-right: 2em;\n	padding: 0;\n	text-align: center;\n}\n#user-menu li {\n	display: inline;\n	list-style-type: none;\n	padding: 0 0.8em;\n	font-size: 90%;\n}\n\n/* footer */\n\n#wiki-footer hr, #wiki-footer br {\n	display: none;\n}\n\n/* recent changes, user contributions, history */\n\nli.minorchange {\n	color: #777777;\n}\nli.standardchange {\n	color: black;\n}\nli.deletechange {\n	color: #770000;\n}\nli.movechange {\n	color: #777777;\n}\nli.undeletechange {\n	color: #007777;\n}\nspan.edit-comment {\n	text-decoration: italic;\n}\nspan.section-link {\n	color: #999;\n}\nspan.section-link a {\n	color: #333;\n}\n\n/* topic pages */\n\n#content-article {\n	padding: 10px 0 10px 0;\n}\n#content-article pre {\n	overflow: hidden;\n}\n.editsection {\n	font-size: 67%;\n	float: right;\n	margin-left: 5px;\n	position: relative;\n}\n\n/* images */\n\nimg.wikiimg {\n	border: 0;\n}\na.wikiimg {\n	border: 0;\n}\ndiv.imgcaption {\n	padding: 0.3em 0em 0.2em 0em;\n}\ndiv.imgleft {\n	float: left;\n	margin: 0.3em 0.3em 0.3em 0em;\n}\ndiv.imgright {\n	float: right;\n	margin: 0.3em 0em 0.3em 0.3em;\n}\ndiv.imgcenter {\n	text-align: center;\n	margin: 0.3em 0em 0.3em 0em;\n}\ndiv.imgthumb {\n	border: 1px solid #bbbbbb;\n	padding: 0.3em;\n	position: relative;\n}\ntable.gallery {\n	border: 1px solid #ccc;\n	margin: 2px;\n	padding: 2px;\n	background-color: white;\n}\ntable.gallery tr {\n	vertical-align: middle;\n}\ntable.gallery td {\n	background-color: #f9f9f9;\n	border: solid 2px white;\n	text-align: center;\n	vertical-align: middle;\n	width:  150px;\n}\nimg.gallery {\n	border: 1px solid #bbbbbb;\n	padding: 0.3em;\n}\n#toolbar a {\n	border: none;\n	text-decoration: none;\n}\n#toolbar a img {\n	padding: 0;\n	margin: 0 1px 0 0;\n	border: 1px solid #7E7ECB;\n}\n#toolbar a img:hover {\n	border-color: #D8D8E7;\n}\n\n/* search results */\n\n.highlight {\n	background:yellow;\n}\n.searchresult {\n	background-color: #dcdcdc;\n	margin: 1em 0 0 0;\n}\n.searchsummary {\n	margin: 0.5em 0 0.7em 0;\n}\n#searchpower {\n	font-size: 90%;\n	font-style: italic;\n	margin: 1em 0 1em 0;\n}\n#searchhints {\n	margin: 1.5em 0 1.5em 0;\n}\n\n/* form styles */\n\n.formentry {\n	width: 99%;\n	padding: 3px 5px;\n	clear: both;\n}\n.formcaption {\n	float: left;\n	width: 250px;\n	font-weight: bold;\n	padding-right: 10px;\n}\n.formcaption-medium {\n	float: left;\n	width: 170px;\n	font-weight: bold;\n	padding-right: 10px;\n}\n.formcaption-small {\n	float: left;\n	width: 90px;\n	font-weight: bold;\n	padding-right: 10px;\n}\n.formelement {\n}\n.formhelp {\n	font-size: 85%;\n	color: #5f5f5f;\n	clear: both;\n}\n.lightbg {\n	background-color: #ffffff;\n}\n.mediumbg {\n	background-color: #e9e9f8;\n}\n.darkbg {\n	background-color: #d8d8e7;\n}\n.translationElement {\n	float: left;\n	vertical-align: top;\n	padding: 3px;\n	width: 49%;\n	overflow: hidden;\n}\ntextarea.translation {\n	width: 100%;\n	overflow:auto;\n	height:5em;\n}\ntextarea.medium {\n	width: 400px;\n	height: 60px;\n}\n',NULL,NULL,1,'127.0.0.1','2009-06-16 19:13:06',1,0,NULL,7900),(5,5,'','Hello World!',NULL,NULL,NULL,'127.0.0.1','2009-06-16 19:13:53',1,0,NULL,12),(6,5,'','Hello World!\n\n\'\'\'Hello World!\'\'\'',NULL,NULL,1,'127.0.0.1','2009-06-16 19:19:14',1,0,5,20),(7,5,'','Hello World!\n\n\'\'\'Hello World!\'\'\'\n\n\'\'Hello World!\'\'',NULL,NULL,NULL,'127.0.0.1','2009-06-16 19:20:09',1,0,6,18);
/*!40000 ALTER TABLE `jam_topic_version` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jam_users`
--

DROP TABLE IF EXISTS `jam_users`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `jam_users` (
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `enabled` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `jam_users`
--

LOCK TABLES `jam_users` WRITE;
/*!40000 ALTER TABLE `jam_users` DISABLE KEYS */;
INSERT INTO `jam_users` VALUES ('admin','YDZcavIlBA+nceQVy6h1MJwtLoD6xQ64BHHaVH4SP9G83L0x1rr3WSFIExD42ObflQs/7MqdfSpdf/IPIm4LTjdEvUzqWnQo',1);
/*!40000 ALTER TABLE `jam_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jam_virtual_wiki`
--

DROP TABLE IF EXISTS `jam_virtual_wiki`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `jam_virtual_wiki` (
  `virtual_wiki_id` int(11) NOT NULL,
  `virtual_wiki_name` varchar(100) NOT NULL,
  `default_topic_name` varchar(200) NOT NULL,
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`virtual_wiki_id`),
  UNIQUE KEY `jam_u_vwiki_name` (`virtual_wiki_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `jam_virtual_wiki`
--

LOCK TABLES `jam_virtual_wiki` WRITE;
/*!40000 ALTER TABLE `jam_virtual_wiki` DISABLE KEYS */;
INSERT INTO `jam_virtual_wiki` VALUES (1,'en','StartingPoints','2009-06-16 19:13:05');
/*!40000 ALTER TABLE `jam_virtual_wiki` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jam_watchlist`
--

DROP TABLE IF EXISTS `jam_watchlist`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `jam_watchlist` (
  `wiki_user_id` int(11) NOT NULL,
  `topic_name` varchar(200) NOT NULL DEFAULT '',
  `virtual_wiki_id` int(11) NOT NULL,
  PRIMARY KEY (`wiki_user_id`,`topic_name`,`virtual_wiki_id`),
  KEY `jam_f_wlist_vwiki` (`virtual_wiki_id`),
  CONSTRAINT `jam_f_wlist_userid` FOREIGN KEY (`wiki_user_id`) REFERENCES `jam_wiki_user` (`wiki_user_id`),
  CONSTRAINT `jam_f_wlist_vwiki` FOREIGN KEY (`virtual_wiki_id`) REFERENCES `jam_virtual_wiki` (`virtual_wiki_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `jam_watchlist`
--

LOCK TABLES `jam_watchlist` WRITE;
/*!40000 ALTER TABLE `jam_watchlist` DISABLE KEYS */;
/*!40000 ALTER TABLE `jam_watchlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jam_wiki_user`
--

DROP TABLE IF EXISTS `jam_wiki_user`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `jam_wiki_user` (
  `wiki_user_id` int(11) NOT NULL,
  `login` varchar(100) NOT NULL,
  `display_name` varchar(100) DEFAULT NULL,
  `create_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `last_login_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_ip_address` varchar(39) NOT NULL,
  `last_login_ip_address` varchar(39) NOT NULL,
  `default_locale` varchar(8) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `editor` varchar(50) DEFAULT NULL,
  `signature` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`wiki_user_id`),
  UNIQUE KEY `jam_u_wuser_login` (`login`),
  CONSTRAINT `jam_f_wuser_users` FOREIGN KEY (`login`) REFERENCES `jam_users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `jam_wiki_user`
--

LOCK TABLES `jam_wiki_user` WRITE;
/*!40000 ALTER TABLE `jam_wiki_user` DISABLE KEYS */;
INSERT INTO `jam_wiki_user` VALUES (1,'admin',NULL,'2009-06-16 19:13:02','2009-06-16 19:13:02','127.0.0.1','127.0.0.1',NULL,NULL,'toolbar',NULL);
/*!40000 ALTER TABLE `jam_wiki_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2010-01-17  3:42:32
