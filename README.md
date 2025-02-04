ChâTop Backend

ChâTop est une société de location immobilière dans une zone touristique qui souhaite offrir une plateforme en ligne permettant aux locataires potentiels de contacter les propriétaires des différentes propriétés à louer.

Sommaire

1. [Description](#description)
2. [Technologies](#technologies)
3. [Installation](#installation)
4. [Api endpoints](#api-endpoints)
5. [Déploiement avec Docker](#déploiement-avec-docker)
6. [Contribuer au projet](#contribuer-au-projet)

# Description

Le projet backend de ChâTop est construit avec Spring Boot, utilisant JPA/Hibernate pour la gestion de la base de données et Spring Security pour gérer l'authentification. OAuth2 Resource Server configure l’application pour agir comme un serveur de ressources OAuth2, validant les JWT tokens pour les requêtes entrantes. Le service utilise un stockage d'images en cloud via Cloudinary.  

L'api est ouverte à la modification et l'extension, il est par exemple possible d'utiliser un autre algorithme que celui proposé par défaut pour le chiffrement des tokens ou un autre service de stockage des images que Cloudinary proposé par défaut.

# Technologies

* Spring Boot pour la gestion du backend.
* Spring Security avec OAuth2 Resource Server pour sécuriser les endpoints API et gérer l'authentification via JWT.
* JPA/Hibernate pour la gestion des entités et des bases de données.
* MySQL comme base de données, gérée via Docker.
* Cloudinary pour le stockage des images des locations.
* Swagger pour la documentation de l'API (à venir).

# Déploiement de l'application
Deux déploiements sont proposés. Déploiement "manuel" ou avec Docker.

## Déploiement "manuel" de l'application

### Prérequis

* Java 17 ou version supérieure.
* Maven pour la gestion des dépendances.
* MySQL Server

### Configurer Mysql Server
Une fois Mysql Server installé, configurer une base de donnée pour l'application et un utilisateur avec droits RW sur la base.
```bash
create database chaotop;
CREATE USER 'chaotop'@'%' IDENTIFIED BY 'chaotopdbuserpassword';
GRANT ALL PRIVILEGES ON chaotop.* TO 'chaotop'@'%';
flush privileges;
```
Et vérifier ensuite l'accès à la base
```bash
mysql -h mysql -uchaotop -p chaotop
```

### Configurer les variables d'environnement pour Mysql
Les variables d'environnement utilisées sont listées dans application.properties.
```bash
- CHAOTOP_DBURL
- CHAOTOP_DBUSER
- CHAOTOP_DBKEY
```
Exemple:
- CHAOTOP_DBURL = jdbc:mysql://localhost:3306/chaotop
- CHAOTOP_DBUSER = chaotop
- CHAOTOP_DBKEY = chaotopdbuserpassword



### Cloner le repository

```bash
git clone https://github.com/jceintrey/chaotop-backend.git
cd chaotop-backend
```
### Installer les dépendances

```bash
./mvnw install
```
En cas d'erreur de connexion à la base, le build ne se fera pas. Il est possible de skip les tests d'accès à la base et d'y revenir ultérieurement.

## Configurer l'application avec les connecteurs par défaut

### Connecteur de stockage cloudinary
Cloudinary est un service Cloud permettant le stockage d'image.
#### Cloudinary API Key

Créez un compte si besoin et configurez une clé d'api sur [Cloudinary](https://cloudinary.com/) pour l'application.

![clé API Cloudinary ](/src/main/resources/static/sc1.png)

#### Définition des variables d'environnement

Dans le fichier src/main/resources/application.properties, vous pouvez définir les paramètres de connexion au service de stockage Cloudinary.  
Seul le secret est vraiment secret et ne doit pas être défini dans application.properties. Définir à minima CHAOTOP_CLOUDINARYSECRET dans les variables d'environnement avec la valeur du secret.  

Concernant le nom de l'environnement et le nom de clé d'api, vous pouvez au choix utiliser les variables d'environnement CHAOTOP_CLOUDINARYCLOUDNAME et CHAOTOP_CLOUDINARYKEY ou définir les valeurs directement dans application.properties:


```
chaotop.cloudinarycloudname=${CHAOTOP_CLOUDINARYCLOUDNAME:youcanalsoconfigureithere}
chaotop.cloudinaryapikey=${CHAOTOP_CLOUDINARYKEY:youcanalsoconfigureithere}
chaotop.cloudinaryapisecret=${CHAOTOP_CLOUDINARYSECRET}
```
#### Connecteur SGBD MySQL

Créez les 3 variables d'environnement CHAOTOP_DBURL, CHAOTOP_DBURL, CHAOTOP_DBKEY.  
Seul CHAOTOP_DBKEY est secret et ne doit pas être défini dans application.properties.

```bash
spring.datasource.url=${CHAOTOP_DBURL}
spring.datasource.username=${CHAOTOP_DBUSER}
spring.datasource.password=${CHAOTOP_DBKEY:pleasechooseastrongpassword}
```


#### Paramètres pour Jwt HmacJwtFactory
Générer une clé d'au moins 256bits

```bash
openssl rand -out secret.key 32
```
Créez et configurez ensuite la variable d'environnement CHAOTOP_JWTKEY avec cette clé. Cette clé doit rester secrète.

La configuration jwt est définie dans application.properties.

```bash
chaotop.jwtsecretkey=${CHAOTOP_JWTKEY:pleasedefine256bitskeyinenv}
chaotop.jwtexpirationtime=3600000
```


## Déploiement avec docker

Le projet peut être facilement déployé avec Docker et Docker Compose.  
## Prérequis
- Avoir docker et docker compose installés. Voir [Docker compose install](https://docs.docker.com/compose/install/) et [Docker install](https://docs.docker.com/engine/install/)

```bash
docker version
docker compose version
```
- Avoir cloné les sources à partir du dépot origine ou du fork [Cloner le repository](#cloner-le-repository)
- Avoir configuré le Connecteur de stockage cloudinary [le Connecteur de stockage cloudinary](#cloudinary-api-key)


## Configurer les variables d'environnement
Renommez les fichiers .sample-env* pour le setup des variables d'environnement
```bash
mv .sample-env-chaotop .env-chaotop
mv .sample-env-mysql .env-mysql 
```
> Attention à ne pas pas synchroniser .env* dans git (déclaré dans .gitigniore)

Configurez ensuite les variables d'environnement:
 - CHAOTOP_DBUSER et MYSQL_USER doivent être identitiques
 - CHAOTOP_DBKEY et MYSQL_PASSWORD doivent être identitiques


### .env-chaotop
```bash
CHAOTOP_DBURL=jdbc:mysql://mysql:3306/chaotop
CHAOTOP_DBUSER=chaotop
CHAOTOP_DBKEY=chaotopdbuserpassword


CHAOTOP_JWTKEY=pleasedefine256bitskeyinenv

CHAOTOP_CLOUDINARYCLOUDNAME=thecloudinarycloudenv
CHAOTOP_CLOUDINARYKEY=thecloudinarykey
CHAOTOP_CLOUDINARYSECRET=thecloudinarysecret
```
### .env-mysql
```bash
MYSQL_ROOT_PASSWORD=pleasechooseastrongpassword
MYSQL_DATABASE=chaotop
MYSQL_USER=chaotop
MYSQL_PASSWORD=chaotopdbuserpassword
```


## Dockerfile : Créez un fichier Dockerfile pour construire l'image du backend api
Deux options possibles:
- un fichier Dockerfile avec COPY du jar de l'application
- un fichier Dockerfile complet permettant de faire le build complet à partir des sources du projet


### Option 1 : Simple Dockerfile avec COPY du jar de l'application
```bash
FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY ./target/chaotop-backend-0.0.1-SNAPSHOT.jar chaotop-backend.jar
ENTRYPOINT ["java", "-jar", "/chaotop-backend.jar"]
```
### Option 2 : Dockerfile complet permettant de faire le build à partir des sources du projet
Le fichier Dockerfile est déjà présent à la racine de l'application.

```bash
# Maven image used to build
FROM maven:latest AS build

# Workdir
WORKDIR /app

# Copy pom.xml and run mvn dependency
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copy source files
COPY src ./src

# Build the app with DskipTests to ovoid jdbc connection errors
RUN mvn clean install -DskipTests

# Build the final image with openjdk image
FROM openjdk:17-jdk-alpine

# workdir
WORKDIR /app

# Copy the previous generated jar file
COPY --from=build /app/target/chaotop-backend-0.0.1-SNAPSHOT.jar chaotop-backend.jar

# Entrypoint
ENTRYPOINT ["java", "-jar", "/app/chaotop-backend.jar"]
```

## docker-compose.yml : Utilisez ensuite le fichier docker-compose.yml fourni
Le fichier docker-compose.yml est présent à la racine de l'application.

Pour construire exécuter l'application
```bash
docker compose up -d
```


## Vérifier et accéder aux logs

```bash
docker compose ps
docker compose logs -f --tail=10
```



# Api endpoints

Voici une description des principales routes de l'API :

1. POST /auth/register

```markdown
Description : Permet l'inscription d'un nouvel utilisateur.  
Réponse :  
200: Succès.  
Validation : Les champs name, email, et password doivent être non nuls.  
```
  

2. POST /auth/login
```markdown
Description : Permet la connexion d'un utilisateur.
Réponse :
200: Succès avec un token JWT.
401: Erreur si l'email ou le mot de passe est incorrect.
Validation : Les credentials doivent correspondre à un utilisateur valide.
```

3. POST /messages
```markdown
Description : Envoi d'un message à un propriétaire.
Réponse :
200: Succès.
400: Erreur si un champ est manquant.
401: Erreur si le token JWT est absent ou invalide.
```

4. GET /auth/me
```markdown
Description : Récupère les informations de l'utilisateur connecté.
Réponse :
200: Succès avec les détails de l'utilisateur.
401: Erreur si le token JWT est absent ou invalide.
```

5. GET /rentals
```markdown
Description : Liste toutes les maisons disponibles à la location.
Réponse :
200: Succès avec les maisons disponibles.
401: Erreur si le token JWT est absent ou invalide.
```

6. GET /rentals/:id
```markdown
Description : Récupère les détails d'une maison spécifique.
Réponse :
200: Succès avec les détails de la maison.
401: Erreur si le token JWT est absent ou invalide.
```

7. POST /rentals
```markdown
Description : Permet de créer une nouvelle maison à louer.
Réponse :
200: Succès, création confirmée.
401: Erreur si le token JWT est absent ou invalide.
```

8. PUT /rentals/:id
```markdown
Description : Permet de mettre à jour une maison existante.
Réponse :
200: Succès, mise à jour confirmée.
401: Erreur si le token JWT est absent ou invalide.
```

9. GET /user/:id
```markdown
Description : Récupère les informations d'un utilisateur spécifique.
Réponse :
200: Succès avec les détails de l'utilisateur.
401: Erreur si le token JWT est absent ou invalide.
```


## Contribuer au projet

ChâTop est un projet proposé par OpenClassrooms dans le cadre du parcours Développeur Java Angular Full-stack. Si vous souhaitez contribuer au projet :

- Forkez le repository.
- Créez une nouvelle branche pour votre fonctionnalité (git checkout -b feature/feature-name).
- Committez vos changements (git commit -m 'Add new feature').
- Poussez vos changements sur votre fork (git push origin feature/feature-name).
- Ouvrez une Pull Request pour révision.

## Contact

- GitHub : https://github.com/jceintrey/chaotop-backend  
- Email : jeremie.ceintrey@gmail.com  

