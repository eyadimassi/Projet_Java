-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Apr 29, 2025 at 07:50 PM
-- Server version: 9.1.0
-- PHP Version: 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cosmeticswebsite`
--

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
CREATE TABLE IF NOT EXISTS `cart` (
  `id` int NOT NULL AUTO_INCREMENT,
  `guest_id` varchar(191) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `guest_id` (`guest_id`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `cart`
--

INSERT INTO `cart` (`id`, `guest_id`, `user_id`, `created_at`) VALUES
(1, NULL, 1, '2025-04-28 23:00:00'),
(2, NULL, 2, '2025-04-28 23:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `cart_items`
--

DROP TABLE IF EXISTS `cart_items`;
CREATE TABLE IF NOT EXISTS `cart_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cart_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `cart_id` (`cart_id`),
  KEY `product_id` (`product_id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
CREATE TABLE IF NOT EXISTS `categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) NOT NULL,
  `parent_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`id`, `nom`, `parent_id`) VALUES
(1, 'Makeup', NULL),
(2, 'Skincare', NULL),
(3, 'Face', 1),
(4, 'Eyes', 1),
(5, 'Lips', 1),
(6, 'Face Care', 2),
(7, 'Body Care', 2),
(8, 'Sun Protection', 2),
(9, 'Moisturizers', 2),
(10, 'Lipsticks', 5),
(11, 'Gloss', 5),
(12, 'Concealer', 4),
(13, 'Mascara', 4),
(14, 'Eye Liners', 4),
(15, 'Foundation', 3),
(16, 'Powder', 3),
(17, 'Contouring', 3);

-- --------------------------------------------------------

--
-- Table structure for table `marque`
--

DROP TABLE IF EXISTS `marque`;
CREATE TABLE IF NOT EXISTS `marque` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `marque`
--

INSERT INTO `marque` (`id`, `nom`) VALUES
(1, 'Huda Beauty'),
(2, 'Maybelline'),
(3, 'Charlotte Tilbury'),
(4, 'Nars'),
(5, 'NYX'),
(6, 'The Ordinary'),
(7, 'Byoma'),
(8, 'La Roche-Posay'),
(9, 'Filorga');

-- --------------------------------------------------------

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
CREATE TABLE IF NOT EXISTS `order` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `statut` enum('pending','processing','shipped','delivered','cancelled') DEFAULT 'pending',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `total_price` int NOT NULL,
  `total_products` int NOT NULL,
  `delivery` enum('express','a domicile','pickup') NOT NULL,
  `cart_id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `order`
--

INSERT INTO `order` (`id`, `user_id`, `statut`, `created_at`, `total_price`, `total_products`, `delivery`, `cart_id`) VALUES
(1, 2, 'pending', '2025-04-28 23:00:00', 308, 3, 'a domicile', 2),
(2, 2, 'pending', '2025-04-28 23:00:00', 177, 3, 'pickup', 2);

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
CREATE TABLE IF NOT EXISTS `order_items` (
  `id` int NOT NULL,
  `order_id` int NOT NULL,
  `product_id` int NOT NULL,
  `quantity` int NOT NULL,
  `price` double NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `produit`
--

DROP TABLE IF EXISTS `produit`;
CREATE TABLE IF NOT EXISTS `produit` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `prix` decimal(10,0) NOT NULL,
  `category_id` int DEFAULT NULL,
  `marque_id` int DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `image_path` varchar(255) NOT NULL,
  `imagehover_path` varchar(500) NOT NULL,
  `ordered` int NOT NULL,
  `shade` json NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `fk_produit_marque` (`marque_id`),
  KEY `fk_produit_categorie` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `produit`
--

INSERT INTO `produit` (`id`, `nom`, `description`, `prix`, `category_id`, `marque_id`, `created_at`, `image_path`, `imagehover_path`, `ordered`, `shade`) VALUES
(1, 'Sérum Illuminateur', 'Ce sérum illuminateur pour le visage contribue à améliorer sensiblement le teint et la texture de la peau, pour des résultats uniformes et éclatants.', 61, 6, 7, '2025-04-17 11:52:54', 'prodimages/Sérum Illuminateur.png', 'prodimages/Sérum Illuminateur_hover.png', 10, '[]'),
(2, 'Nettoyant Gel Crème', 'Cette gelée nettoyante ultra-hydratante élimine en douceur et efficacement la saleté, l’excès de sébum et 	le maquillage sans abîmer la barrière cutanée. Profitez d’un nettoyant quotidien pour le visage, en une 	seule étape.', 54, 6, 7, '2025-04-17 11:58:17', 'prodimages/Nettoyant Gel Crème.jpg', 'prodimages/Nettoyant Gel Crème_hover.jpg', 10, '[]'),
(3, 'HYDRA-HYAL Sérum Hydratant', 'Découvrez le sérum HYDRA-HYAL, un soin hydratant à lacide hyaluronique, pour repulper la peau visiblement. Intégrant un complexe qui délivre dans la peau 5 types d’acides hyaluronique pour hydrater, lisser et repulper.', 191, 9, 9, '2025-04-17 12:04:59', 'prodimages/HYDRA-HYAL Sérum Hydratant.jpg', 'prodimages/HYDRA-HYAL Sérum Hydratant_hover.jpg', 10, '[]'),
(4, 'TIME-FILLER 5XP Crème visage anti-rides', 'TIME-FILLER 5XP CRÈME, FILORGA réinvente sa crème anti-ride best-seller qui agit simultanément sur 5 types de rides, grâce au cœur de formule 5XP inspiré de 5 techniques de médecine esthétique.', 261, 6, 9, '2025-04-17 12:08:43', 'prodimages/TIME-FILLER 5XP Crème visage anti-rides.jpg', 'prodimages/TIME-FILLER 5XP Crème visage anti-rides-hover.jpg', 10, '[]'),
(5, 'Effaclar DUO+M Soin triple correction anti-imperfections', 'Sérum pour illuminer la peau et l’hydrater en profondeur', 58, 6, 8, '2025-04-17 12:12:58', 'prodimages/Effaclar DUO+M Soin triple correction anti-imperfections.jpg', 'prodimages/Effaclar DUO+M Soin triple correction anti-imperfections-hover.jpg', 10, '[]'),
(6, 'Facteurs Naturels d\'Hydratation + PhytoCéramides', 'La crème hydratante The Ordinary la plus nourrissante et fraiche.', 40, 9, 6, '2025-04-17 12:23:26', 'prodimages/Facteurs Naturels d\'Hydratation + PhytoCéramides.jpg', 'prodimages/Facteurs Naturels d\'Hydratation + PhytoCéramides-hover.jpg', 10, '[]'),
(7, 'Tonique Exfoliant à l\'Acide Glycolique 7%', 'Une reformulation de notre sérum hydratant le plus apprécié, qui hydrate et repulpe instantanément tout en lissant la texture et les rides.', 109, 6, 6, '2025-04-17 12:26:58', 'prodimages/Tonique Exfoliant à l\'Acide Glycolique 7%.jpg', 'prodimages/Tonique Exfoliant à l\'Acide Glycolique 7%-hover.jpg', 10, '[]'),
(8, 'ANTHELIOS_FLUIDE_OIL_CONTROL_UVMUNE_400_AVEC_PARFUM_SPF50+', ' Résistant à l\'eau et à la transpiration. Yeux anti-piqûres.\r\n    Sans marques blanches et sans parfum.\r\n    Testé sous contrôle dermatologique sur peaux sensibles et réactives.\r\n    Testé sous contrôle ophtalmologique sur les contours des yeux et des yeux sur les peaux sensibles.\r\n    Testé sur les porteurs de lentilles de contact.', 70, 8, 8, '2025-04-17 13:21:10', 'prodimages/ANTHELIOS_FLUIDE_OIL_CONTROL_UVMUNE_400_AVEC_PARFUM_SPF50+.jpg', 'prodimages/ANTHELIOS_FLUIDE_OIL_CONTROL_UVMUNE_400_AVEC_PARFUM_SPF50+-hover.jpg', 1, '[]'),
(9, 'Anthelios UVMUNE 400 Crème solaire peau sensible SPF50+', 'Offre une protection SPF 50+ à large spectre contre les rayons UV nocifs, assurant une défense supérieure de la peau sans compromettre le confort ou la sensation.', 80, 8, 8, '2025-04-17 13:21:10', 'prodimages/Anthelios UVMUNE 400 Crème solaire peau sensible SPF50+.jpg', 'prodimages/Anthelios UVMUNE 400 Crème solaire peau sensible SPF50+-hover.jpg', 1, '[]'),
(12, 'BYOMA Hydrating Body Wash', 'Pour une peau normale à sèche - Lavage du corps hydratant et crémeux au squalane, Beta Glucan - Nettoie, Adoucit et apaise - Réparation des barrières cutanées - 16,9 fl oz', 61, 7, 7, '2025-04-17 13:38:50', 'prodimages/BYOMA Hydrating Body Wash.jpg', 'prodimages/BYOMA Hydrating Body Wash -hover.jpg', 1, '[]'),
(13, 'BYOMA Lotion pour le corps sensible ', 'Pour peau sèche et irritée - Hydratant sans parfum avec beurre colloïdal d\'avoine et de karité - Apaise et soulage les démangeaisons - Support de barrière cutanée - 16,9 fl oz', 80, 7, 7, '2025-04-17 13:38:50', 'prodimages/BYOMA Lotion pour le corps sensible.png', 'prodimages/BYOMA Lotion pour le corps sensible-hover.jpg', 1, '[]'),
(14, 'Sulphate 4% Cleanser for Body and Hair', 'A gentle, fragrance-free shampoo and body wash for daily use.', 50, 7, 6, '2025-04-17 13:46:02', 'prodimages/Sulphate 4% Cleanser for Body and Hair.jpg', 'prodimages/Sulphate 4% Cleanser for Body and Hair-hover.jpg', 1, '[]'),
(15, 'Natural Moisturizing Factors + Inulin Body Lotion', 'A lightweight, non-greasy body lotion for instant and all-day surface hydration.', 46, 7, 6, '2025-04-17 13:46:02', 'prodimages/Natural Moisturizing Factors + Inulin Body Lotion.jpg', 'prodimages/Natural Moisturizing Factors + Inulin Body Lotion-hover.jpg', 1, '[]'),
(16, 'Byoma smoothing body polish', 'Byoma Smoothing Body Polish est un gommage corporel régénérant qui rafraîchit et renouvelle la peau sous la douche. La combinaison d\'exfoliants chimiques et physiques agit ensemble en douceur mais efficacement pour cibler et équilibrer la peau rugueuse et bosselée.', 76, 7, 7, '2025-04-17 13:57:44', 'prodimages/Byoma smoothing body polish.jpg', 'prodimages/Byoma smoothing body polish-hover.jpg', 1, '[]'),
(17, 'NCEF-SHOT Sérum concentré anti-rides, fermeté et éclat', 'NCEF-SHOT est un sérum visage anti-âge ultra-concentré qui s\'utilise en cure de 10 jours pour améliorer la qualité de peau et corriger les signes du vieillissement cutané. Un véritable coup de boost pour les peaux dévitalisées et fatiguées !', 80, 6, 9, '2025-04-17 14:11:36', 'prodimages/NCEF-SHOT Sérum concentré anti-rides, fermeté et éclat.jpg', 'prodimages/NCEF-SHOT Sérum concentré anti-rides, fermeté et éclat-hover.jpg', 1, '[]'),
(18, 'LIFT-STRUCTURE RADIANCE', 'LIFT-STRUCTURE RADIANCE, un fluide rose éclat ultra-liftant révélateur d’éclat naturel inspiré des techniques de médecine esthétique, qui agit sur le manque de fermeté, la perte de volume, les traits fatigués, le manque d’homogénéité ainsi que le teint terne pour un effet anti-âge radical.', 79, 6, 9, '2025-04-17 14:11:36', 'prodimages/LIFT-STRUCTURE RADIANCE.jpg', 'prodimages/LIFT-STRUCTURE RADIANCE-hover.jpg', 1, '[]'),
(19, 'HYDRA-HYAL CRÈME Crème de jour hydratante et repulpante', 'Découvrez la crème visage HYDRA-HYAL, le bain d\'hydratation en pot. Pour compléter votre routine, combinez la crème et le sérum.', 90, 6, 9, '2025-04-17 14:11:36', 'prodimages/HYDRA-HYAL CRÈME.jpg', 'prodimages/HYDRA-HYAL CRÈME-hover.jpg', 1, '[]'),
(20, 'OXYGEN-GLOW Crème perfectrice éclat', 'Découvrez OXYGEN-GLOW, une crème de jour perfectrice qui corrige la peau en temps réel et booste l\'éclat instantanément et durablement pour une belle peau au naturel.', 90, 6, 9, '2025-04-17 14:11:36', 'prodimages/OXYGEN-GLOW CRÈME.jpg', 'prodimages/OXYGEN-GLOW CRÈME-hover.jpg', 1, '[]'),
(21, 'Solution à la Caféine 5% + EGCG', 'Solution décongestionnante pour les yeux fatigués.', 50, 6, 6, '2025-04-17 14:20:52', 'prodimages/Solution à la Caféine 5% + EGCG.jpg', 'prodimages/Solution à la Caféine 5% + EGCG-hover.jpg', 1, '[]'),
(22, 'Sérum Multi-Peptides + Peptides de Cuivre 1%', 'Un sérum anti-âge innovant qui offre une protection antioxydante supplémentaire.', 46, 6, 6, '2025-04-17 14:20:52', 'prodimages/Sérum Multi-Peptides + Peptides de Cuivre 1%.jpg', 'prodimages/Sérum Multi-Peptides + Peptides de Cuivre 1%-hover.jpg', 1, '[]'),
(23, 'EFFACLAR GEL MOUSSANT PURIFIANT NETTOYANT PEAU GRASSE', 'LA ROCHE-POSAY EFFACLAR GEL MOUSSANT PURIFIANT Nouvelle formule enrichie par la science du microbiome grâce au nouvel actif Phylobioma. Purifie la peau en douceur et réduit visiblement les imperfections tout en respectant le pH physiologique de la peau. Sans savon. Peaux grasses et sensibles à tendance acnéique.', 53, 6, 8, '2025-04-17 14:39:17', 'prodimages/EFFACLAR GEL MOUSSANT PURIFIANT NETTOYANT PEAU GRASSE.jpg', 'prodimages/EFFACLAR GEL MOUSSANT PURIFIANT NETTOYANT PEAU GRASSE-hover.jpg', 1, '[]'),
(24, 'Hydraphase HA Yeux Contour des yeux', 'Soin Contour des Yeux Hydratant et Anti-Fatigue à l\'Acide Hyaluronique', 51, 9, 8, '2025-04-17 14:39:17', 'prodimages/Hydraphase HA Yeux Contour des yeux.jpg', 'prodimages/Hydraphase HA Yeux Contour des yeux-hover.jpg', 1, '[]'),
(25, 'Hydraphase HA Légère Crème Hydratante Visage à l\'Acide Hyaluronique', 'Crème hydratante visage légère à l\'acide hyaluronique, peau normale à mixte. Hydrate et repulpe la peau. Ravive l\'éclat du teint.', 60, 9, 8, '2025-04-17 14:45:08', 'prodimages/Hydraphase HA Légère Crème Hydratante Visage à l\'Acide Hyaluronique.jpg', 'prodimages/Hydraphase HA Légère Crème Hydratante Visage à l\'Acide Hyaluronique-hover.jpg', 1, '[]'),
(26, 'Easy Bake Fragrance Free Loose Baking and Setting Powder', 'This powder feels weightless, and the silky texture blends seamlessly. It keeps makeup in place without creasing & absorbs shine/oil with zero flashback.', 80, 16, 1, '2025-04-17 19:29:01', 'prodimages/Easy Bake Fragrance Free Loose Baking and Setting Powder.png', 'prodimages/Easy Bake Fragrance Free Loose Baking and Setting Powder-hover.png', 10, '[{\"hex\": \"#8B4513\", \"name\": \"Banana Bread\", \"image\": \"Easy Bake Fragrance Free Loose Baking and Setting Powder.png\"}, {\"hex\": \"#F5F5DC\", \"name\": \"Cinnamon Bun\", \"image\": \"Easy Bake Fragrance Free Loose Baking and Setting Powder Brown.png\"}]'),
(28, 'FauxFilter Luminous Matte Foundation', '#FauxFilter is back with the same full coverage and long-lasting properties, you know and love from the OG formula, but with 24-hour flexible wear, a luminous matte finish AND… it’s fragrance-free!', 76, 15, 1, '2025-04-17 20:01:10', 'prodimages/FauxFilter Luminous Matte Foundation.jpg', 'prodimages/FauxFilter Luminous Matte Foundation-hover.jpg', 0, '[{\"hex\": \"#E6AC78\", \"name\": \"Macchiato 400G\", \"image\": \"prodimages/FauxFilter Luminous Matte Foundation.jpg\"}, {\"hex\": \"#FBDBB7\", \"name\": \"Panna Cotta 130G\", \"image\": \"prodimages/FauxFilter Luminous Matte FoundationPannaCotta.jpg\"}, {\"hex\": \"#A16B40\", \"name\": \"Mocha 500G\", \"image\": \"prodimages/FauxFilter Luminous Matte FoundationMocha.jpg\"}]'),
(29, 'FauxFilter Luminous Matte Concealer', 'A crease-proof, medium to full buildable coverage creamy concealer that brightens, conceals, and moves with you for a luminous matte, skin-like finish that lasts all day.', 61, 12, 1, '2025-04-17 20:23:28', 'prodimages/FauxFilter Luminous Matte Concealer.jpg', 'prodimages/FauxFilter Luminous Matte Concealer-hover.jpg', 0, '[{\"hex\": \"#db9a77\", \"name\": \"Graham Cracker 4.1G\", \"image\": \"prodimages/FauxFilter Luminous Matte Concealer.jpg\"}, {\"hex\": \"#f6cabb\", \"name\": \"Yogurt Drops 1.5B\", \"image\": \"prodimages/FauxFilter Luminous Matte ConcealerYogurt.jpg\"}, {\"hex\": \"#edbf9d\", \"name\": \"Coconut Flakes 2.7N\", \"image\": \"prodimages/FauxFilter Luminous Matte ConcealerCoconut.jpg\"}, {\"hex\": \"#9a5e35\", \"name\": \"Salted Caramel 7.3G\", \"image\": \"prodimages/FauxFilter Luminous Matte ConcealerCaramel.jpg\"}]'),
(30, 'Life Liner Double Ended Eyeliner Liquid & Pencil', 'The ultimate lifeproof, dual-ended eyeliner with a liquid tip one side and pencil on the other. Long-lasting, waterproof, smudge proof and fade proof.', 36, 14, 1, '2025-04-17 21:46:32', 'imagesprod/Life Liner Double Ended Eyeliner Liquid & Pencil.jpg', 'imagesprod/Life Liner Double Ended Eyeliner Liquid & Pencil-hover.jpg', 1, '[]'),
(31, 'LEGIT LASHES Double-Ended Volumizing and Lengthening Mascara Mini', 'Our mini LEGIT LASHES Mascara means you can bring your fave mascara with you on-the-go! It’s the same dual-ended, long-lasting, matte black mascara as our OG, but mini size! It delivers major volume, dramatic curl & insane length without weighing your lashes down! Plus, the two formulas mean it’s bigger than most mascara minis!', 60, 13, 1, '2025-04-17 21:49:39', 'imagesprod/LEGIT LASHES Double-Ended Volumizing and Lengthening Mascara Mini.jpg', 'imagesprod/LEGIT LASHES Double-Ended Volumizing and Lengthening Mascara Mini-hover.jpg', 1, '[]'),
(32, 'LEGIT Lashes Waterproof Mascara Topcoat', 'LEGIT LASHES Waterproof Mascara Topcoat is a clear gel layer that makes your fave mascara water-, sweat-, and smudge-proof! One swipe of this topcoat with its precision brush will lock mascara into place, protecting them from tears, fears, downpours, late-night dancing & so much more! Whether you’re HIIT-ing the gym, jet-setting or a bride-to-be, this jelly-like texture features film formers and flexible resins to ensure extra comfort throughout the day and/or night without weighing down your lashes.', 60, 13, 1, '2025-04-17 21:49:39', 'imagesprod/LEGIT Lashes Waterproof Mascara Topcoat.jpg', 'imagesprod/LEGIT Lashes Waterproof Mascara Topcoat-hover.jpg', 1, '[]'),
(34, 'BEAUTIFUL SKIN FOUNDATION', 'My buildable, medium-coverage, hydrating foundation for a healthy-looking, BEAUTIFUL SKIN EFFECT! 3 Neutral is a shade for fair skin with neutral undertones.', 95, 15, 3, '2025-04-18 12:42:57', 'imagesprod/BEAUTIFUL SKIN FOUNDATION.png', 'imagesprod/BEAUTIFUL SKIN FOUNDATION-hover.png', 1, '[{\"hex\": \"#E8D3B8\", \"nom\": \"3 NEUTRAL\", \"image_path\": \"imagesprod/BEAUTIFUL SKIN FOUNDATION.png\"}, {\"hex\": \"#F1DBC7\", \"nom\": \"2 Neutral\", \"image_path\": \"imagesprod/BEAUTIFUL SKIN FOUNDATION 2.png\"}, {\"hex\": \"#D6B99D\", \"nom\": \"4 Neutral\", \"image_path\": \"imagesprod/BEAUTIFUL SKIN FOUNDATION 4.png\"}]'),
(35, 'BEAUTIFUL SKIN SUN-KISSED GLOW BRONZER', 'Fini lumineux, couvrance modulable, soin pour la peau.', 87, 17, 3, '2025-04-18 12:49:10', 'prodimages/BEAUTIFUL SKIN SUN-KISSED GLOW BRONZER.png', 'prodimages/BEAUTIFUL SKIN SUN-KISSED GLOW BRONZER-hover.png', 1, '[{\"hex\": \"#D1B59A\", \"nom\": \"1 FAIR\", \"image_path\": \"prodimages/BEAUTIFUL SKIN SUN-KISSED GLOW BRONZER.png\"}, {\"hex\": \"#6A4E3A\", \"nom\": \"3 TAN\", \"image_path\": \"prodimages/BEAUTIFUL SKIN SUN-KISSED GLOW BRONZER TAN.png\"}]'),
(36, 'EXAGGER-EYES VOLUME MASCARA', 'NEW! volumising mascara for multiplied, thicker, fanned-out looking lashes, 28-HOUR EXAGGERATED FAST VOLUME + CURL!', 58, 13, 3, '2025-04-18 15:14:55', 'prodimages/EXAGGER-EYES VOLUME MASCARA.png', 'prodimages/EXAGGER-EYES VOLUME MASCARA-hover.png', 1, '[]'),
(37, 'PILLOW TALK PUSH UP LASHES! MASCARA', 'My miracle Pillow Talk mascara in a travel-sized tube for instant volume, length, stretch & 24HOUR VERTICAL LIFT EFFECT on the go!', 93, 13, 3, '2025-04-18 15:14:55', 'prodimages/PILLOW TALK PUSH UP LASHES! MASCARA.png', 'prodimages/PILLOW TALK PUSH UP LASHES! MASCARA-hover.jpg', 1, '[]'),
(38, 'PILLOW TALK EYELINER', 'Smokey berry-brown eyeliner pencil for DREAMY, DEFINED Pillow Talk EYES!', 45, 14, 3, '2025-04-18 15:17:12', 'prodimages/PILLOW TALK EYELINER.png', 'prodimages/PILLOW TALK EYELINER-hover.png', 1, '[]'),
(39, 'PILLOW TALK BIG LIP PLUMPGASM', 'Lip-plumping lip gloss in a pink nude shade for an instant plumping effect!.', 65, 11, 3, '2025-04-18 15:24:43', 'prodimages/PILLOW TALK BIG LIP PLUMPGASM.png', 'prodimages/PILLOW TALK BIG LIP PLUMPGASM-hover.png', 1, '[]'),
(40, 'LIP LUSTRE HIGH SOCIETY', 'Lip-plumping lip gloss in a pink nude shade for an instant plumping effect!.', 65, 11, 3, '2025-04-18 15:24:43', 'prodimages/LIP LUSTRE HIGH SOCIETY.png', 'prodimages/LIP LUSTRE HIGH SOCIETY-hover.png', 1, '[{\"hex\": \"#B76E79\", \"nom\": \"HIGH SOCIETY\", \"image_path\": \"prodimages/LIP LUSTRE HIGH SOCIETY.png\"}, {\"hex\": \"#5A2C4A\", \"nom\": \"UNLEASH ME\", \"image_path\": \"prodimages/LIP LUSTRE UNLEASH ME.png\"}, {\"hex\": \"#F28C8C\", \"nom\": \"SWEET STILETTO\", \"image_path\": \"prodimages/LIP LUSTRE SWEET STILETTO.png\"}]'),
(41, 'MATTE REVOLUTION', 'Lip-plumping lip gloss in a pink nude shade for an instant plumping effect!.', 95, 10, 3, '2025-04-18 15:30:34', 'prodimages/MATTE REVOLUTION.png', 'prodimages/MATTE REVOLUTION-hover.png', 1, '[{\"hex\": \"#9E5A4A\", \"nom\": \"MARK OF A KISS\", \"image_path\": \"prodimages/MATTE REVOLUTION.png\"}, {\"hex\": \"#A67C6B\", \"nom\": \"VERY VICTORIA\", \"image_path\": \"prodimages/MATTE REVOLUTION VERY VICTORIA.png\"}, {\"hex\": \"#5E1F3D\", \"nom\": \"FESTIVAL MAGIC\", \"image_path\": \"prodimages/MATTE REVOLUTION FESTIVAL MAGIC.png\"}, {\"hex\": \"#9B1B30\", \"nom\": \"RED CARPET RED\", \"image_path\": \"prodimages/MATTE REVOLUTION RED CARPET RED.png\"}]'),
(42, 'HOLLYWOOD CONTOUR WAND', 'Easy liquid contour that sculpts and shapes the look of your face. Now in 2 sculpting shades!', 84, 17, 3, '2025-04-18 15:41:46', 'prodimages/HOLLYWOOD CONTOUR WAND.png', 'prodimages/HOLLYWOOD CONTOUR WAND-hover.png', 1, '[{\"hex\": \"#C8A27E\", \"nom\": \"FAIR-MEDIUM\", \"imagePath\": \"prodimages/HOLLYWOOD CONTOUR WAND.png\"}, {\"hex\": \"#7B4B3A\", \"nom\": \"MEDIUM DEEP\", \"imagePath\": \"prodimages/HOLLYWOOD CONTOUR WAND MEDIUM DEEP.png\"}]'),
(43, 'THE HOLLYWOOD CONTOUR DUO', 'MAGICAL SAVINGS ! Save a magical 10% when you buy these HOLLYWOOD EFFECT beauty icons together!!', 70, 17, 3, '2025-04-18 15:41:46', 'prodimages/THE HOLLYWOOD CONTOUR DUO.png', 'prodimages/THE HOLLYWOOD CONTOUR DUO-hover.png', 1, '[]'),
(44, 'PILLOW TALK BEAUTY SOULMATES FACE PALETTE', 'FLAWLESS PINK Perfect + love-blush with my Pillow Talk face palette featuring awakening AIRbrush Flawless Finish powder and Pillow Talk matte blush.', 65, 16, 3, '2025-04-18 15:43:21', 'prodimages/PILLOW TALK BEAUTY SOULMATES FACE PALETTE.png', 'prodimages/PILLOW TALK BEAUTY SOULMATES FACE PALETTE-hover.png', 1, '[]'),
(45, 'SUPERSTAR LIPS', 'Superstar Lips in Pillow Talk is the best-selling iconic dreamy nude pink lipstick in a brand new texture! Combining the high shine of a gloss and the staying power of a lipstick, this cushiony texture glides onto your lips with a creamy feel.', 74, 10, 3, '2025-04-18 15:52:59', 'prodimages/SUPERSTAR LIPS.png', 'prodimages/SUPERSTAR LIPS-hover.png', 1, '[{\"hex\": \"#D8A39D\", \"nom\": \"PILLOW TALK\", \"image_path\": \"prodimages/SUPERSTAR LIPS.png\"}, {\"hex\": \"#A03C4F\", \"nom\": \"WALK OF NO SHAME\", \"image_path\": \"prodimages/SUPERSTAR LIPS WALK OF NO SHAME.png\"}]'),
(46, 'PILLOW TALK LOVE EFFECT LIPSTICK KISS TALK', 'K.I.S.S.I.N.G - KISS TALK Limited-edition fresh-pink Pillow Talk lipstick with a satin-shine finish.', 69, 10, 3, '2025-04-18 15:52:59', 'prodimages/PILLOW TALK LOVE EFFECT LIPSTICK KISS TALK.png', 'prodimages/PILLOW TALK LOVE EFFECT LIPSTICK KISS TALK-hover.png', 1, '[]'),
(47, 'Super Stay Vinyl Ink ROYAL 55 ROYAL', 'Vinyl Ink de Maybelline New-York: une encre à lèvres effet vinyl aux couleurs ultra-pigmentées pour une 	 tenue 16H*. Un fini effet vinyl, brillant longue tenue en un seul geste.', 55, 10, 2, '2025-04-23 12:00:41', 'prodimages/Super Stay Vinyl Ink 55 ROYAL.jpg', 'prodimages/Super Stay Vinyl Ink 55 ROYAL-hover.jpg', 1, '[{\"hex\": \"#5C003F\", \"nom\": \"Super Stay Vinyl Ink 55 ROYAL\", \"imagePath\": \"prodimages/Super Stay Vinyl Ink 55 ROYAL.jpg\"}, {\"hex\": \"#D8A0B0\", \"nom\": \"Super Stay Vinyl Ink 20 COY\", \"imagePath\": \"prodimages/Super Stay Vinyl Ink 20 COY.jpg\"}, {\"hex\": \"#8B004F\", \"nom\": \"Super Stay Vinyl Ink 30 UNRIVALED\", \"imagePath\": \"prodimages/Super Stay Vinyl Ink 30 UNRIVALED.jpg\"}, {\"hex\": \"#E25B45\", \"nom\": \"Super Stay Vinyl Ink 125 KEEN\", \"imagePath\": \"prodimages/Super Stay Vinyl Ink 125 KEEN.jpg\"}]'),
(48, 'Lifter Gloss BUBBLEGUM', 'Lifter Gloss is formulated with Hyaluronic Acid that visibly smooths lip surface and enhances lip contour with high shine for fuller looking lips..', 65, 11, 2, '2025-04-23 12:13:16', 'prodimages/Lifter Gloss BUBBLEGUM.jpg', 'prodimages/Lifter Gloss BUBBLEGUM-hover.jpg', 1, '[{\"hex\": \"#EC5C9F\", \"nom\": \"Lifter Gloss BUBBLEGUM\", \"imagePath\": \"prodimages/Lifter Gloss BUBBLEGUM.jpg\"}, {\"hex\": \"#B03A6E\", \"nom\": \"Lifter Gloss TAFFY\", \"imagePath\": \"prodimages/Lifter Gloss TAFFY.jpg\"}, {\"hex\": \"#B8860B\", \"nom\": \"Lifter Gloss CRYSTAL\", \"imagePath\": \"prodimages/Lifter Gloss CRYSTAL.jpg\"}, {\"hex\": \"#F88379\", \"nom\": \"Lifter Gloss PEACH RING\", \"imagePath\": \"prodimages/Lifter Gloss PEACH RING.jpg\"}]'),
(49, 'Lash Sensational Sky High Mascara BURGUNDY HAZE', 'Take your lashes to the next dimension with Maybelline’s 1st glitter mascara, Sky High Space Diamond. Featuring the iconic Sky High flex tower brush, this pearl infused formula delivers a silver diamond finish for lashes so lengthened and gleaming, they’re out of this world!', 75, 13, 2, '2025-04-23 12:24:28', 'prodimages/Lash Sensational Sky High Mascara BURGUNDY HAZE.jpg', 'prodimages/Lash Sensational Sky High Mascara BURGUNDY HAZE-hover.jpg', 1, '[]'),
(50, 'Lash Sensational Sky High Washable Mascara BLUE MIST', 'Take your lashes to the next dimension with Maybelline’s 1st glitter mascara, Sky High Space Diamond. Featuring the iconic Sky High flex tower brush, this pearl infused formula delivers a silver diamond finish for lashes so lengthened and gleaming, they’re out of this world!', 75, 13, 2, '2025-04-23 12:24:28', 'prodimages/Lash Sensational Sky High Washable Mascara BLUE MIST.jpg', 'prodimages/Lash Sensational Sky High Washable Mascara BLUE MIST-hover.jpg', 1, '[]'),
(51, 'Lash Sensational Sky High Washable Mascara BLACKEST BLACK', 'Take your lashes to the next dimension with Maybelline’s 1st glitter mascara, Sky High Space Diamond. Featuring the iconic Sky High flex tower brush, this pearl infused formula delivers a silver diamond finish for lashes so lengthened and gleaming, they’re out of this world!', 75, 13, 2, '2025-04-23 12:24:28', 'prodimages/Lash Sensational Sky High Washable Mascara BLACKEST BLACK.jpg', 'prodimages/Lash Sensational Sky High Washable Mascara BLACKEST BLACK-hover.jpg', 1, '[]'),
(52, 'Lash Sensational Sky High Glitter Mascara SPACE DIAMOND', 'Take your lashes to the next dimension with Maybelline’s 1st glitter mascara, Sky High Space Diamond. Featuring the iconic Sky High flex tower brush, this pearl infused formula delivers a silver diamond finish for lashes so lengthened and gleaming, they’re out of this world!', 75, 13, 2, '2025-04-23 12:24:28', 'prodimages/Lash Sensational Sky High Glitter Mascara SPACE DIAMOND.jpg', 'prodimages/Lash Sensational Sky High Glitter Mascara SPACE DIAMOND-hover.jpg', 1, '[]'),
(53, 'Maybelline Lash Sensational', 'Take your lashes to the next dimension with Maybelline’s 1st glitter mascara, Sky High Space Diamond. Featuring the iconic Sky High flex tower brush, this pearl infused formula delivers a silver diamond finish for lashes so lengthened and gleaming, they’re out of this world!', 75, 13, 2, '2025-04-23 12:25:44', 'prodimages/Maybelline Lash Sensational.jpg', 'prodimages/Maybelline Lash Sensational-hover.jpg', 1, '[]'),
(54, 'Fit Me Dewy + Smooth Liquid Foundation 375', 'Fit Me® Dewy + Smooth Foundation is a dewy foundation for dry skin that hydrates rough patches leaving skin with a dewy makeup look and naturally luminous finish.', 92, 15, 2, '2025-04-23 12:35:22', 'prodimages/Fit Me Dewy + Smooth Liquid Foundation 375 JAVA.jpg', 'prodimages/Fit Me Dewy + Smooth Liquid Foundation 375 JAVA-hover.jpg', 1, '[{\"hex\": \"#4B3621\", \"nom\": \"375 JAVA\", \"imagePath\": \"prodimages/Fit Me Dewy + Smooth Liquid Foundation 375 JAVA.jpg\"}, {\"hex\": \"#C68642\", \"nom\": \"310 SUN BEIGE\", \"imagePath\": \"prodimages/Fit Me Dewy + Smooth Liquid Foundation 310 SUN BEIGE.jpg\"}, {\"hex\": \"#D2A679\", \"nom\": \"240 GOLDEN BEIGE\", \"imagePath\": \"prodimages/Fit Me Dewy + Smooth Liquid Foundation 240 GOLDEN BEIGE.jpg\"}]'),
(55, 'Instant Age Rewind Eraser Dark Circles Treatment Multi-Use Concealer 095', 'Instant Eraser is the do-it-all concealer that moisturizes too! This multi-use concealer does it all: hydrates, conceals, contours and corrects in a click! Get up to 12 hours of moisturizing wear for crease-resistant coverage and a sponge tip applicator.', 55, 12, 2, '2025-04-23 12:47:16', 'prodimages/Instant Age Rewind Eraser Dark Circles Treatment Multi-Use Concealer 095.jpg', 'prodimages/Instant Age Rewind Eraser Dark Circles Treatment Multi-Use Concealer 095-hover.jpg', 1, '[{\"hex\": \"#F3C9B2\", \"nom\": \"Concealer 095\", \"imagePath\": \"prodimages/Instant Age Rewind Eraser Dark Circles Treatment Multi-Use Concealer 095.jpg\"}, {\"hex\": \"#6E3C2C\", \"nom\": \" Concealer 159\", \"imagePath\": \"prodimages/Instant Age Rewind Instant Eraser Color Correcting Concealer 159.jpg\"}, {\"hex\": \"#E3B697\", \"nom\": \"Concealer 130\", \"imagePath\": \"prodimages/Instant Age Rewind Eraser Dark Circles Treatment Multi-Use Concealer 130.jpg\"}, {\"hex\": \"#F2CDA9\", \"nom\": \"Concealer 121\", \"imagePath\": \"prodimages/Instant Age Rewind Eraser Dark Circles Treatment Multi-Use Concealer 121.jpg\"}]'),
(56, 'Maybelline Fit Me Matte 110 PORCELAIN', ' Poreless Powder face makeup. Mattifies and leaves a poreless-looking finish with long-lasting shine control.', 55, 16, 2, '2025-04-23 13:17:44', 'prodimages/Maybelline Fit Me Matte 110 PORCELAIN.jpg', 'prodimages/Maybelline Fit Me Matte 110 PORCELAIN-hover.jpg', 1, '[{\"hex\": \"#F3E0D7\", \"nom\": \"110 PORCELAIN\", \"imagePath\": \"prodimages/Maybelline Fit Me Matte 110 PORCELAIN.jpg\"}, {\"hex\": \"#E8C8B0\", \"nom\": \"122 CREAMY BEIGE\", \"imagePath\": \"prodimages/Maybelline Fit Me Matte 122 CREAMY BEIGE.jpg\"}]'),
(57, 'Can\'t Stop Won\'t Stop Contour Concealer 07', 'Full coverage, long-wear cream concealer!', 45, 12, 5, '2025-04-23 13:26:53', 'prodimages/Can\'t Stop Won\'t Stop Contour Concealer 07.png', 'prodimages/Can\'t Stop Won\'t Stop Contour Concealer 07-hover.png', 1, '[{\"hex\": \"#F1DBC7\", \"nom\": \"07\", \"imagePath\": \"prodimages/Can\'t Stop Won\'t Stop Contour Concealer 07.png\"}, {\"hex\": \"#E8D3B8\", \"nom\": \"02 Alabester\", \"imagePath\": \"prodimages/Can\'t Stop Won\'t Stop Contour Concealer 02 Alabaster.png\"}, {\"hex\": \"#D6B99D\", \"nom\": \"4 Neutral\", \"imagePath\": \"images/teintes/4_neutral.jpg\"}]'),
(58, 'Highlight & Contour Pro Palette', 'Define your features like a pro with our refillable highlight and contour palette! Each set includes eight customizable highlighting and contouring shades perfect for emphasizing your favorite features.', 75, 17, 5, '2025-04-23 13:48:19', 'prodimages/Highlight & Contour Pro Palette.png', 'prodimages/Highlight & Contour Pro Palette-hover.png', 1, '[]'),
(59, 'Bare With Me Blur Tint Foundation 24 JAVA', 'Blurring tint foundation in 24 medium coverage, matte shades', 55, 15, 5, '2025-04-23 13:48:19', 'prodimages/Bare With Me Blur Tint Foundation 24 JAVA.png', 'prodimages/Bare With Me Blur Tint Foundation 24 JAVA-hover.png', 1, '[{\"hex\": \"#3B2F2F\", \"nom\": \"24 JAVA\", \"imagePath\": \"prodimages/Bare With Me Blur Tint Foundation 24 JAVA.png\"}, {\"hex\": \"#FBE8D3\", \"nom\": \"02 FAIR\", \"imagePath\": \"prodimages/Bare With Me Blur Tint Foundation 02 FAIR.jpg\"}]'),
(60, 'Vivid Brights Colored Liquid Eyeliner', 'Looking for pro-level graphic artistry made easy? Swipe right on bright with Vivid Brights Liquid Eyeliner by NYX Professional Makeup', 35, 14, 5, '2025-04-23 13:52:19', 'prodimages/Vivid Brights Colored Liquid Eyeliner.jpg', 'prodimages/Vivid Brights Colored Liquid Eyeliner-hover.png', 1, '[]'),
(61, 'Epic Wear Waterproof Eyeliner Stick', 'High-impact, waterproof eyeliner pencil!', 35, 14, 5, '2025-04-23 13:52:19', 'prodimages/Epic Wear Waterproof Eyeliner Stick.png', 'prodimages/Epic Wear Waterproof Eyeliner Stick-hover.png', 1, '[]'),
(62, 'Epic Ink Waterproof Liquid Eyeliner', 'High-impact, waterproof eyeliner pencil!', 35, 14, 5, '2025-04-23 13:52:19', 'prodimages/Epic Ink Waterproof Liquid Eyeliner.png', 'prodimages/Epic Ink Waterproof Liquid Eyeliner-hover.png', 1, '[]'),
(63, 'Soft Matte Lip Cream', 'Plush Matte Formula!', 50, 10, 5, '2025-04-23 14:01:10', 'prodimages/Soft Matte Lip Cream.png', 'prodimages/Soft Matte Lip Cream-hover.png', 1, '[]'),
(64, 'Lip Lingerie XXL Matte Liquid Lipstick', '16H Full-Bodied Matte Liquid Lipstick', 50, 10, 5, '2025-04-23 14:01:10', 'prodimages/Lip Lingerie XXL Matte Liquid Lipstick.png', 'prodimages/Lip Lingerie XXL Matte Liquid Lipstick-hover.png', 1, '[]'),
(65, 'FAT OIL SLICK CLICK', 'Get slick lips in just a click with this lip oil stick! Our pigmented lip oil balm in click-up packaging dispenses just the right amount of lip oil for the perfect lip look.', 50, 10, 5, '2025-04-23 14:01:10', 'prodimages/FAT OIL SLICK CLICK.png', 'prodimages/FAT OIL SLICK CLICK-hover.png', 1, '[]'),
(66, 'Fat Oil Lip Drip', 'Say hello to Fat Oil Lip Drip, our first lip oil of its kind. This creamy lip oil will have your lips dripping with FAT perks.!', 50, 11, 5, '2025-04-23 14:01:10', 'prodimages/Fat Oil Lip Drip.png', 'prodimages/Fat Oil Lip Drip-hover.png', 1, '[]'),
(67, 'Lip I.V. Hydrating Lip Gloss Stain', 'Get all the vitamin-infused hydration of a lip serum in a hydrating lip gloss stain with Lip I.V. Lip Gloss Stain.', 50, 11, 5, '2025-04-23 14:01:10', 'prodimages/Lip I.V. Hydrating Lip Gloss Stain.png', 'prodimages/Lip I.V. Hydrating Lip Gloss Stain-hover.png', 1, '[]'),
(68, 'Afterglow Lip Shine Gloss', 'Lip-plumping lip gloss in a pink nude shade for an instant plumping effect!.', 125, 11, 4, '2025-04-23 16:14:28', 'prodimages/Afterglow Lip Shine Gloss.png', 'prodimages/Afterglow Lip Shine Gloss-hover.png', 1, '[]'),
(69, 'Afterglow Sheer Hydrating Lip Oil', 'A sensorial gel-oil gloss with long-lasting hydration and sheer shine.', 145, 10, 4, '2025-04-23 16:14:28', 'prodimages/Afterglow Sheer Hydrating Lip Oil.png', 'prodimages/Afterglow Sheer Hydrating Lip Oil-hover.png', 1, '[]'),
(70, 'Powermatte Long-Lasting Lipstick Rich pink berry', 'A sensorial gel-oil gloss with long-lasting hydration and sheer shine.', 115, 10, 4, '2025-04-23 16:14:28', 'prodimages/Powermatte Long-Lasting Lipstick Rich pink berry.png', 'prodimages/Powermatte Long-Lasting Lipstick Rich pink berry-hover.png', 1, '[]'),
(71, 'Explicit Refillable Satin Lipstick', 'A high-end dimensional satin lipstick that delivers long-lasting comfort with vibrant, pure-color payoff and creamy, smudge-resistant wear.', 85, 10, 4, '2025-04-23 16:14:28', 'prodimages/Explicit Refillable Satin Lipstick.png', 'prodimages/Explicit Refillable Satin Lipstick-hover.png', 1, '[]'),
(72, 'Mini Laguna Bronzer Powder Talc-Free', 'An iconic powder bronzer in nine, talc-free shades that creates instant warmth and a long-wearing natural looking glow.', 90, 16, 4, '2025-04-23 16:14:28', 'prodimages/Mini Laguna Bronzer Powder Talc-Free.png', 'prodimages/Mini Laguna Bronzer Powder Talc-Free-hover.png', 1, '[]'),
(73, 'Soft Matte Advanced Perfecting Powder', 'A multi-purpose, skin-perfecting powder that blurs imperfections, smooths the look of skin, and sets makeup for up to 24 hours.', 90, 16, 4, '2025-04-23 16:14:28', 'prodimages/Soft Matte Advanced Perfecting Powder.png', 'prodimages/Soft Matte Advanced Perfecting Powder-hover.png', 1, '[]'),
(74, 'Mini Radiant Creamy Concealer with Medium Coverage', 'A multipurpose concealer that brightens, corrects, and perfects for up to 16 hours with a creamy medium-to-buildable coverage and natural, radiant finish.', 70, 12, 4, '2025-04-23 16:14:28', 'prodimages/Mini Radiant Creamy Concealer with Medium Coverage.png', 'prodimages/Mini Radiant Creamy Concealer with Medium Coverage-hover.png', 1, '[]');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('client','admin') NOT NULL DEFAULT 'client',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `adress` json NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `email`, `password`, `role`, `created_at`, `adress`) VALUES
(2, 'ayouta', 'ayouta@yahoo.fr', '12345', 'client', '2025-04-29 18:33:01', '\"texas\"'),
(3, 'zeineb', 'zeineb@gmail.com', '12345', 'admin', '2025-04-29 18:52:55', '\"1 rue jaafar el barmaki\"');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `categories`
--
ALTER TABLE `categories`
  ADD CONSTRAINT `categories_ibfk_1` FOREIGN KEY (`id`) REFERENCES `categories` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Constraints for table `marque`
--
ALTER TABLE `marque`
  ADD CONSTRAINT `marque_ibfk_1` FOREIGN KEY (`id`) REFERENCES `marque` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Constraints for table `produit`
--
ALTER TABLE `produit`
  ADD CONSTRAINT `fk_produit_categorie` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_produit_marque` FOREIGN KEY (`marque_id`) REFERENCES `marque` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
