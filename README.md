ChâTop Backend

ChâTop est une société de location immobilière dans une zone touristique qui souhaite offrir une plateforme en ligne permettant aux locataires potentiels de contacter les propriétaires des différentes propriétés à louer.

Sommaire

- [Description du projet](#Description du projet)
- [Technologies utilisées](#technologies-utilisées)
- [Installation](#installation)
- [API REST](#API REST)
- [Déploiement avec Docker](#Déploiement avec Docker)
- [Contribuer au projet](#Contribuer au projet)


- [Description du projet](#Description du projet)
    Technologies utilisées
    Installation
    Configuration
    API REST
    Déploiement avec Docker
    Contribuer au projet
    Contact

Description du projet

Le projet backend de ChâTop est construit avec Spring Boot, utilisant JPA/Hibernate pour la gestion de la base de données et Spring Security. OAuth2 Resource Server configure l’application pour agir comme un serveur de ressources OAuth2, validant les JWT tokens pour les requêtes entrantes. Le service utilise un stockage d'images en cloud via Cloudinary.
L'api est ouverte à la modification et l'extension, il est par exemple possible d'utiliser un autre algorithme que celui proposé par défaut pour le chiffrement des tokens ou un autre service de stockage des images que Cloudinary proposé par défaut.

Technologies utilisées

    Spring Boot pour la gestion du backend.
    Spring Security avec OAuth2 Resource Server pour sécuriser les endpoints API et gérer l'authentification via JWT.
    JPA/Hibernate pour la gestion des entités et des bases de données.
    MySQL comme base de données, gérée via Docker.
    Cloudinary pour le stockage des images des locations.
    Swagger pour la documentation de l'API (à venir).

Installation
Prérequis

    Java 17 ou version supérieure.
    Maven pour la gestion des dépendances.
    Docker (si vous utilisez Docker pour la base de données).
    MySQL si vous choisissez de ne pas utiliser Docker.

Cloner le repository

git clone https://github.com/jceintrey/chaotop-backend.git
cd chaotop-backend

Installer les dépendances

./mvnw install

Configuration de la base de données MySQL avec Docker

Si vous utilisez Docker, vous pouvez configurer MySQL avec le fichier docker-compose.yml :

    Créez un fichier .env à la racine du projet avec les variables suivantes :
MYSQL_ROOT_PASSWORD=pleasechangeit
MYSQL_DATABASE=chaotop
MYSQL_USER=chaotop
MYSQL_PASSWORD=pleasechangeit
    Lancez MySQL avec Docker :

docker-compose up -d

Cela démarre le serveur MySQL sur le port 3309 en local.
Configurer l'application

Dans le fichier src/main/resources/application.properties, vous pouvez définir les paramètres de connexion à la base de données et aux services Cloudinary :

# MySQL
spring.datasource.url=jdbc:mysql://localhost:3309/chaotop
spring.datasource.username=chaotop
spring.datasource.password=${CHAOTOP_DBKEY:pleasechooseastrongpassword}

# Jwt
chaotop.jwtsecretkey=${CHAOTOP_JWTKEY:pleasedefine256bitskeyinenv}
chaotop.jwtexpirationtime=3600000

# Cloudinary Storage
chaotop.cloudinarycloudname=diqyy0y8d
chaotop.cloudinaryapikey=${CHAOTOP_CLOUDINARYKEY:pleaseconfigureyourenv}
chaotop.cloudinaryapisecret=${CHAOTOP_CLOUDINARYSECRET:pleaseconfigureyourenv}

API REST

Voici une description des principales routes de l'API :
1. POST /auth/register

    Description : Permet l'inscription d'un nouvel utilisateur.
    Réponse :
        200: Succès.
        Validation : Les champs name, email, et password doivent être non nuls.

2. POST /auth/login

    Description : Permet la connexion d'un utilisateur.
    Réponse :
        200: Succès avec un token JWT.
        401: Erreur si l'email ou le mot de passe est incorrect.
        Validation : L'email doit être test@test.com et le mot de passe test!31.

3. POST /messages

    Description : Envoi d'un message à un propriétaire.
    Réponse :
        200: Succès.
        400: Erreur si un champ est manquant.
        401: Erreur si le token JWT est absent ou invalide.

4. GET /auth/me

    Description : Récupère les informations de l'utilisateur connecté.
    Réponse :
        200: Succès avec les détails de l'utilisateur.
        401: Erreur si le token JWT est absent ou invalide.

5. GET /rentals

    Description : Liste toutes les maisons disponibles à la location.
    Réponse :
        200: Succès avec les maisons disponibles.
        401: Erreur si le token JWT est absent ou invalide.

6. GET /rentals/:id

    Description : Récupère les détails d'une maison spécifique.
    Réponse :
        200: Succès avec les détails de la maison.
        401: Erreur si le token JWT est absent ou invalide.

7. POST /rentals

    Description : Permet de créer une nouvelle maison à louer.
    Réponse :
        200: Succès, création confirmée.
        401: Erreur si le token JWT est absent ou invalide.

8. PUT /rentals/:id

    Description : Permet de mettre à jour une maison existante.
    Réponse :
        200: Succès, mise à jour confirmée.
        401: Erreur si le token JWT est absent ou invalide.

9. GET /user/:id

    Description : Récupère les informations d'un utilisateur spécifique.
    Réponse :
        200: Succès avec les détails de l'utilisateur.
        401: Erreur si le token JWT est absent ou invalide.

Déploiement avec Docker

Le projet peut être facilement déployé avec Docker en utilisant un fichier Dockerfile et un docker-compose.yml. Voici un exemple de base :

    Dockerfile : Créez un Dockerfile pour construire l'image du backend.

FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY target/chaotop-backend-0.0.1-SNAPSHOT.jar chaotop-backend.jar
ENTRYPOINT ["java", "-jar", "/chaotop-backend.jar"]

    docker-compose.yml : Utilisez docker-compose pour orchestrer les services.

version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - mysql
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: "pleasechangeit"
      MYSQL_DATABASE: "chaotop"
      MYSQL_USER: "chaotop"
      MYSQL_PASSWORD: "pleasechangeit"
    ports:
      - "3309:3306"

Démarrer les services Docker

docker-compose up --build

Contribuer au projet

ChâTop est un projet proposé par OpenClassrooms dans le cadre du parcours Développeur Java Angular Full-stack. Si vous souhaitez contribuer au projet :

    Forkez le repository.
    Créez une nouvelle branche pour votre fonctionnalité (git checkout -b feature/feature-name).
    Committez vos changements (git commit -m 'Add new feature').
    Poussez vos changements sur votre fork (git push origin feature/feature-name).
    Ouvrez une Pull Request pour révision.

Contact

    GitHub : https://github.com/jceintrey/chaotop-backend
    Email : jeremie.ceintrey@gmail.com
