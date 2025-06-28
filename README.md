# ChâTop

ChâTop est une API REST développée avec Spring Boot pour une société de location immobilière dans une zone touristique.

## Fonctionnalités

- Création d'utilisateur
- Connexion sécurisée via JWT
- Visualisation des locations disponibles
- Consultation et modification d'une location
- Messagerie

---

## Prérequis

- Java 17 (ou version supérieure)  
- Maven Wrapper inclus dans le projet (`./mvnw`)
- Base de données MySql

---


## Installation


### Depuis la ligne de commande

1. Clonez le dépôt :
   git clone https://github.com/Janouy/Developpez-le-back-end-en-utilisant-Java-et-Spring.git
   cd chatopApi

---

## Configuration

### Création d'une base de données MySQL

Les tables se créeront automatiquement au lancement du projet

### Ajouter un .env sous src/main/ressources

- DB_HOST= host de la base de données
- DB_PORT= port de la base de données
- DB_NAME= nom de votre base de données
- DB_USER= nom d'utilisateur
- DB_PASSWORD= mot de passe de connexion à la base de données
- JWT_KEY= clé servant à générer un jwt

---

## Lancement du projet

   - depuis votre IDE

ou

   - Avec le goal Maven :  ./mvnw spring-boot:run

ou

   - En exécutant le JAR : 
       - ./mvnw clean install (clean install compile, teste et package l’application en .jar)
       - puis depuis le répertoire target : cd target puis java -jar chatopApi-0.0.1-SNAPSHOT.jar


