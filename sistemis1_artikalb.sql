-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: sistemis1
-- ------------------------------------------------------
-- Server version	5.7.17-log

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
-- Table structure for table `artikalb`
--

DROP TABLE IF EXISTS `artikalb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `artikalb` (
  `IdArtikla` int(11) NOT NULL AUTO_INCREMENT,
  `Cena` int(11) DEFAULT NULL,
  `Kolicina` int(11) DEFAULT NULL,
  `Tip` int(11) DEFAULT NULL,
  `IdKorpe` int(11) DEFAULT NULL,
  `IdProdavca` int(11) DEFAULT NULL,
  PRIMARY KEY (`IdArtikla`),
  KEY `FK_artikalb_IdKorpe` (`IdKorpe`),
  KEY `FK_artikalb_IdProdavca` (`IdProdavca`),
  CONSTRAINT `FK_artikalb_IdKorpe` FOREIGN KEY (`IdKorpe`) REFERENCES `korpab` (`IdKorpe`),
  CONSTRAINT `FK_artikalb_IdProdavca` FOREIGN KEY (`IdProdavca`) REFERENCES `prodavacb` (`IdProdavca`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `artikalb`
--

LOCK TABLES `artikalb` WRITE;
/*!40000 ALTER TABLE `artikalb` DISABLE KEYS */;
INSERT INTO `artikalb` VALUES (1,100,5,1,1,5),(2,200,3,2,1,5),(3,300,4,3,1,2),(4,400,3,4,1,4),(5,400,3,4,2,4),(6,500,2,5,2,3);
/*!40000 ALTER TABLE `artikalb` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-02-11  2:10:00
