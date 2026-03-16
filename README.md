🛒 ShopBy Backend

📌 Description

ShopBy est un backend e-commerce développé avec Spring Boot.
Il simule une plateforme de vente en ligne avec gestion complète des utilisateurs, articles et commandes.

L’objectif du projet est de mettre en pratique des concepts avancés de développement backend :
- Architecture en couches
- Authentification sécurisée
- Gestion des rôles
- Pagination & filtres dynamiques
- Optimisation des performances de lecture
- Tests unitaires
- Migration de base de données avec Flyway

🚀 Fonctionnalités principales
- Gestion des articles
- Gestion des commandes
- Gestion des commentaires par article
- Gestion des types d'articles
- Gestion des status de Commandes
- Gestion des marques d'articles
- Gestion des listes d'envies
- Gestion des users (Connexion, Inscription)
- Gestion des roles utilisateurs

Principes utilisées :
- Authentification pour certaines routes necessitant une permissions
- Création de Token d'authentification JWT permettant l'authentification utilisés dans de nombreuses routes API
- Gestion des permissions et des roles pour les utilisateurs
- Pagination sur les requetes possédant un nombre élévé de résultat
- Mise en places de test unitaires sur les controllers et service de chaque fonctionnalités
- Gestion des exception des différentes routes spécifiques avec RestControllerAdvice (Conflict, Ok, Create, NotFound)
- Mise en place de DTO pour envoyer les elements sans exposer nos elements dans l'api de la base de données lors d'une requete sur la bdd.
- Mise en place de mapper pour simplifier le retour d'elements dans les resultats renvoyés par l'api
- Mise en place de spécification pour gérer les différents filtres remplis par l'utilisateurs
- Stockage en local d'image pour les images des articles qui vont être enregistrés dans un dossier du serveur

🏗️ Architecture
Architecture modulaire organisée par fonctionnalité.

Chaque module contient :
- controller
- service
- repository
- specification
- dto
- mapper
- entity

🧱 Stack technique
- Java 17
- Spring Boot 3
- Spring Data JPA
- Spring Security
- Hibernate
- MySQL
- FlyWay
- Lombok
- Maven
