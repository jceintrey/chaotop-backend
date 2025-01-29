[33mcommit 122df66edb8e027a486be83d035f6e3077d10027[m[33m ([m[1;36mHEAD[m[33m -> [m[1;32mrefactor/user-management-and-authentication-service[m[33m)[m
Author: Jérémie Ceintrey <jeremie.ceintrey@gmail.com>
Date:   Wed Jan 29 18:36:15 2025 +0100

    remove unused imports

[33mcommit bf4acf0ceb960ee3b9e59803decb593d736a73c8[m
Author: Jérémie Ceintrey <jeremie.ceintrey@gmail.com>
Date:   Wed Jan 29 18:22:23 2025 +0100

    Sortir la logique de récupération du contexte d'authentification du
    controller
    
    - Création d'une méthode Optional<String> getAuthenticatedUserEmail()
      dans AuthenticationService
    - maj du controller en conséquence

[33mcommit 3569d168b7ba40ce71735f28f819490763891dac[m[33m ([m[1;31morigin/refactor/user-management-service[m[33m)[m
Author: Jérémie Ceintrey <jeremie.ceintrey@gmail.com>
Date:   Wed Jan 29 18:08:45 2025 +0100

    user-management-service: refactor du service UserManagementService et
    création de authenticationService pour sortir la logique métier de construction des réponses et d'authentification du controller
    
    -DatabaseUserDetailsService: Renommage de DatabaseUserAuthenticationService en DatabaseUserDetailsService (plus explicite)
    
    -Création d'une classe de service AuthenticationService :
    Permet de gérer de sortir la logique d'authentification du controller (notamment l'utilisation des bean JwtFactory et Authentication)
     - AuthenticationService:  Création d'une méthode authenticate(LoginRequest request) ->  TokenResponse
     - AuthController: Réécriture de /login en utilisant AuthenticationService
    
    -Renommage du dto MeResponse en UserProfileResponse (plus explicite)
    
    -Refactoring de DefaultUserManagementService
    Dans la même logique de pour AuthenticationService, l'idée est de sortir la logique de gestion des utilisateurs du controller.
    Pour /me, initialement le controller réalisait la construction du DTO réponse.
     - DefaultUserManagementService: Ajout de getUserInformationResponse(String email) permettant de renvoyer un UserProfileResponse
     - AuthController: Réécriture de /me en utilisant getUserInformationResponse à la place de construire la réponse
     - DefaultUserManagementService: Mise à jour de  getUserId(String email) et getUserbyEmail(String email) pour renvoyer des Optionals
     - AuthController: Mise à jour dans le controller pour gérer des Optional à la place.
     - ajout d'une méthode boolean isEmailAlreadyUsed
    
    -Fix : login après register
    authentification après création d'un nouvel utilisateur
     - AuthController : utilisation du service AuthenticationService authenticationService.authenticate(loginRequest)

[33mcommit fb56f12f35ec600d25c4f3cffd8498c9a3db063b[m[33m ([m[1;31morigin/main[m[33m, [m[1;32mmain[m[33m)[m
Merge: 16d9c1f def3299
Author: jceintrey <jeremie.ceintrey@gmail.com>
Date:   Wed Jan 29 14:57:16 2025 +0100

    Merge pull request #11 from jceintrey/feature/rentals-route
    
    Feature/rentals route

[33mcommit def3299ea7f2e98c42484480f27e426d034d10aa[m[33m ([m[1;31morigin/feature/rentals-route[m[33m, [m[1;32mfeature/rentals-route[m[33m)[m
Author: Jérémie Ceintrey <jeremie.ceintrey@gmail.com>
Date:   Wed Jan 29 14:21:06 2025 +0100

    rentals-route: Configuration des endpoint pour les locations
     - ajout de updateRental

[33mcommit 3e30a66601242f10ea39b3c52b5f07445e3113d6[m
Author: Jérémie Ceintrey <jeremie.ceintrey@gmail.com>
Date:   Wed Jan 29 11:18:22 2025 +0100

    rentals-route: Utilisation de ModelMapper pour gérer la conversion entre
    l'entité et la réponse
     - Modification du service RentalService qui va gérer le mapping et
       renvoyer une RentalResponse ou une RentalListResponse au controller
     - Le bean du mapper est configuré dans AppConfig avec un mappage du
       owner de l'entité sur ownerid dans la réponse

[33mcommit b0e0e6a87bee75e57fa357ffd31469f2f9d6691a[m
Author: Jérémie Ceintrey <jeremie.ceintrey@gmail.com>
Date:   Wed Jan 29 09:30:28 2025 +0100

    rentals-route: Configuration des routes
     - getAllRentals : retourne l'ensemble des locations
     - getRentalById : retourne la location correspondant à l'id
     - createRental : ajoute une location (le stockage de l'image n'est pas
       géré pour le moment)

[33mcommit 16d9c1f3b585b28ceb1a7684b8a14eaa2926089f[m
Merge: b5e1e96 b67cf21
Author: jceintrey <jeremie.ceintrey@gmail.com>
Date:   Mon Jan 27 16:21:18 2025 +0100

    Merge pull request #10 from jceintrey/refactor/generate-random-sampleuser-password
    
    generate-random-sampleuser-password: Configuration d'un mot de passe

[33mcommit b67cf2199da67d8592408ad01dd5f280217e6843[m[33m ([m[1;31morigin/refactor/generate-random-sampleuser-password[m[33m)[m
Author: Jérémie Ceintrey <jeremie.ceintrey@gmail.com>
Date:   Mon Jan 27 16:17:12 2025 +0100

    generate-random-sampleuser-password: batch génération utilisateurs
     - génération batch d'utilisateurs sample
     - fix = caracter manquant dans regex AppConfigProperties

[33mcommit d4514d1739d41f1596ef9c0c363f791c69aa759f[m
Author: Jérémie Ceintrey <jeremie.ceintrey@gmail.com>
Date:   Mon Jan 27 15:25:07 2025 +0100

    generate-random-sampleuser-password: Configuration d'un mot de passe
    sample aléatoire à chaque démarrage de l'api
     - Ajout de passai dans les dépendances
     - génération d'un mot de passe aléatoire pour l'utilisateur

[33mcommit b5e1e964c20e7692cb44beb52428efc73010ef17[m
Merge: 3509049 3441cf9
Author: jceintrey <jeremie.ceintrey@gmail.com>
Date:   Mon Jan 27 14:55:58 2025 +0100

    Merge pull request #9 from jceintrey/feature/auth-routes
    
    auth-routes: Ajout des routes pour le controller d'authentification

[33mcommit 3441cf9dee7e3675723768cb8e4cdd0ff0260657[m[33m ([m[1;31morigin/feature/auth-routes[m[33m)[m
Author: Jérémie Ceintrey <jeremie.ceintrey@gmail.com>
Date:   Mon Jan 27 14:52:45 2025 +0100

    auth-routes: Ajout des routes pour le controller d'authentification
    
    - AppConfig: renommage de JWTFactoryConfig en AppConfig pour être plus général sur la configuration de l'application non spécifique à SpringSecurity
    - AppConfig: ajout d'un bean UserManagementService dans AppConfig
    - DataBaseEntityUser: renommage de DBUser en DataBaseEntityUser pour être plus explicite car cette classe est relative à JPA
    - DatabaseUserAuthenticationService: renommage de CustomerUserDetailsService en DatabaseUserAuthenticationService plus explicite car relatif à l'authentification Spring Security
    - AuthController : revue des injections de dépendance avec injection d'un authenticationManager, d'une jwtFactory, et d'un userManagementService
    - Suppresion du endpoint /status qui était utilisé pour validation de l'authentification jwt
    - Ajout du endpoint /me qui renvoie les informations de l'utilisateur authentifié
    - Ajout du endpoint /register permettant d'enregistrer un nouvel utilisateur
    - Ajout d'une interface UserManagementService
    - Renommage de UserService en DefaultUserManagementService et implémentation de l'interface UserManagementService
    - Injection de UserDetailsService dans SpringSecurityConfig au lieu de DatabaseUserAuthenticationService et annotation @Primary sur DatabaseUserAuthenticationService pour indiquer qu'elle est l'implémentation par défaut.

[33mcommit 3509049d11819a5af6da1a33d9a8b377beb2042f[m
Merge: 4d0aa3c 9fa8d15
Author: jceintrey <jeremie.ceintrey@gmail.com>
Date:   Mon Jan 27 10:24:54 2025 +0100

    Merge pull request #8 from jceintrey/refactor/jwt-auth-without-custom-jwt-filter
    
    Refactor/jwt auth without custom jwt filter

[33mcommit 9fa8d15cc585661f34f89b079dbac4d2fc539106[m[33m ([m[1;31morigin/refactor/jwt-auth-without-custom-jwt-filter[m[33m)[m
Author: Jérémie Ceintrey <jeremie.ceintrey@gmail.com>
Date:   Mon Jan 27 10:16:20 2025 +0100

    jwt-auth-without-custom-jwt-filter : Suppression du couplage entre
    SpringSecurityConfig et la factory
     - renommage de SimpleJwtFactory en HmacJwtFactory (Todo: autre implémentation
       comme RSAJwtFactory)
     - création d'un fichier de configuration JwtFactoryConfig permettant
       d'instancier un HmacJwtFactory
     - SpringSecurityConfig ne se soucie plus de l'implémentation de
       JwtFactory

[33mcommit 26d2d1ded6cfff05de90054ca656d7566851ae61[m
Author: Jérémie Ceintrey <jeremie.ceintrey@gmail.com>
Date:   Mon Jan 27 09:48:20 2025 +0100

    jwt-auth-without-custom-jwt-filter: Changement du service JWTService vers Factory JwtFactory
     - OauthRessourceServer a besoin de bean JWTDecoder et JWTEncoder qui
       sont maintenant récupérés auprès de la factory
     - La factory est instanciée par SpringSecurityConfig avec la secret
       String Key
     - La factory SimpleJwtFactory est utilisée comme première
       implémentation de JwtFactory

[33mcommit 4bfd442a97f0733c6fd7ab10a6ba53df0df52575[m
Author: Jérémie Ceintrey <jeremie.ceintrey@gmail.com>
Date:   Fri Jan 24 15:34:11 2025 +0100

    jwt-auth-without-custom-jwt-filter: Simplification de la chaine de
    filtre Spring Security
     - Retrait du filtre personnalisé chargé de réaliser l'authentification
       JWT (extraire le bearer dans l'auth header, décoder, réaliser
       l'authentification et l'ajouter au context Spring)
     - Utilisation de cette intégration dans oauth2RessourceServer

[33mcommit 4d0aa3c9acacd31bab5ff5659dad50b30c8940b3[m
Merge: f3be769 cc45617
Author: jceintrey <jeremie.ceintrey@gmail.com>
Date:   Fri Jan 24 14:44:33 2025 +0100

    Merge pull request #7 from jceintrey/refactor/hide-secrets
    
    hide-secrets : Configuration des secrets à partir des variables

[33mcommit cc456176efa03c9515b1bcd0079d35dc20e21b66[m[33m ([m[1;31morigin/refactor/hide-secrets[m[33m)[m
Author: Jérémie Ceintrey <jeremie.ceintrey@gmail.com>
Date:   Fri Jan 24 14:41:59 2025 +0100

    hide-secrets : Configuration des secrets à partir des variables
    d'environnement
     - AppConfigProperties: la classe est utilisée pour gérer les variables
       personnalisée de l'application

[33mcommit f3be769808647ba82873357b1a7c5b761b92d75b[m
Merge: 9c90b2e dffead2
Author: jceintrey <jeremie.ceintrey@gmail.com>
Date:   Thu Jan 23 17:55:23 2025 +0100

    Merge pull request #6 from jceintrey/refactor/logging-simplify
    
    logging-simplify: Utilisation de l'annotation @Slf4j

[33mcommit dffead21d5eb480af45f461f30c3223142e0e123[m[33m ([m[1;31morigin/refactor/logging-simplify[m[33m)[m
Author: Jérémie Ceintrey <jeremie.ceintrey@gmail.com>
Date:   Thu Jan 23 17:53:27 2025 +0100

    logging-simplify: Utilisation de l'annotation @Slf4j
     - All: remplacement de l'utilisation de LoggerFactory

[33mcommit 9c90b2ebf87e0205ff89cd9bcfd4f29db1faa52c[m
Merge: 79faf8e e389fc1
Author: jceintrey <jeremie.ceintrey@gmail.com>
Date:   Thu Jan 23 11:34:31 2025 +0100

    Merge pull request #5 from jceintrey/feature/add-me-route
    
    Feature/add me route

[33mcommit e389fc16c0604a7bdf2ebb3c82d23c24ab0a656f[m[33m ([m[1;31morigin/feature/add-me-route[m[33m)[m
Author: Jérémie Ceintrey <jeremie.ceintrey@gmail.com>
Date:   Thu Jan 23 11:31:49 2025 +0100

    add-me-route: nettoyage du code lié à l'utilisation de CustomUserDetails
         - JwtAuthenticationFilter : j'ai retiré la partie rôle avec la construction GrantAuthorities à
           partir du token
         - AuthController : retiré le test du cast CustomUserDetails qui était pour le debug

[33mcommit b80836982d30ccbf43f197cf73d8d00b7b68c766[m
Author: Jérémie Ceintrey <jeremie.ceintrey@gmail.com>
Date:   Wed Jan 22 19:03:27 2025 +0100

    add-me-route: Configurer la route me
     - création du mapping dans AuthController
     - CustomUserDetails: création d'une classe model CustomUserDetails qui étant Userdetails
       afin d'ajouter des informations supplémentaires à l'utilisateur dans
       le context Spring Security
     - JwtAuthenticationFilter: modification de doFilterInternal pour
       récupérer un objet CustomUserDetails auprès du service CustomUserDetailService
     - CustomUserDetailService : modification de loadUserByUsername pour
       construire un CustomUserDetails à partir du DBUser.

[33mcommit 79faf8e2947f5edcee60272ce1f2b232f060f9b8[m
Merge: f5ea69a 81439cd
Author: jceintrey <jeremie.ceintrey@gmail.com>
Date:   Wed Jan 22 17:04:25 2025 +0100

    Merge pull request #4 from jceintrey/feature/configure-logging
    
    configure-logging: Configuration du logging

[33mcommit 81439cd4f452b3f0ab869a1127186f67aab9197f[m[33m ([m[1;31morigin/feature/configure-logging[m[33m)[m
Author: Jérémie Ceintrey <jeremie.ceintrey@gmail.com>
Date:   Wed Jan 22 17:01:58 2025 +0100

    configure-logging: Configuration du logging
     - harmonisation de la configuration des loggers avec un logger par
       classe
     - ajout des debug pour les méthodes les plus critiques

[33mcommit f5ea69a666cfc2adfca37fc8c1de24d1a41b8734[m
Merge: ad17327 2ffc023
Author: jceintrey <jeremie.ceintrey@gmail.com>
Date:   Wed Jan 22 15:37:42 2025 +0100

    Merge pull request #3 from jceintrey/feature/jwt-auth
    
    Feature/jwt auth

[33mcommit 2ffc023145c8eb727ebf570ebe15fce30ac2fb9b[m[33m ([m[1;31morigin/feature/jwt-auth[m[33m)[m
Author: Jérémie Ceintrey <jeremie.ceintrey@gmail.com>
Date:   Wed Jan 22 15:34:37 2025 +0100

    jwt-auth : configuration du filtre d'authentification jwt
     - ajout d'un filtre personnalisé qui étend OncePerRequestFilter
     - déplacement des fonctions liées aux token dans la classe JWTService

[33mcommit 47ca0d91d80900a0f9647a1fe9aeca6018acbf2e[m
Author: Jérémie Ceintrey <jeremie.ceintrey@gmail.com>
Date:   Tue Jan 21 18:25:52 2025 +0100

    jwt-auth: Configuration de l'authentification jwt pour l'api
     - SpringSecurityConfig: configuration des bean pour fournir un encoder et un decoder, ils sont récupérés auprès d'une classe de service JWTService
     - SpringSecurityConfig: configuration de la chaine de sécurité
            -sans authentification sur le endpoint /api/auth/login
            -authentification stateless
            -ajout d'un filtre avant le filtre avant le fitre par défaut
            pour gérer l'authentification par jeton
     - JwtAuthenticationFilter : le filtre ne fait rien pour le moment mais
       devra vérifier le token jwt afin de valider l'authentifcation de la
       requète
     - JWTService : configuration de la classe de service pour manager la
       partie JWT
            -generation d'un token
            -fournir un decoder et un encoder
     - AuthController : RestController avec configuration de deux endpoint
            -/login : réalise une authentification login/password auprès de
            Spring sur la base du username et password fournis
            -/status : endpoint de test permettant de valider
            l'authentification par token

[33mcommit ad173276e3b1cdd65bee48e7dbaa08bac6220d70[m
Merge: d3c0517 e902f8f
Author: jceintrey <jeremie.ceintrey@gmail.com>
Date:   Tue Jan 21 11:59:55 2025 +0100

    Merge pull request #2 from jceintrey/feature/db-auth
    
    db-auth : Configuration de l'authentification des utilisateurs via db

[33mcommit e902f8f8e3da4205d2f1e0d37b2171b66e4d6288[m[33m ([m[1;31morigin/feature/db-auth[m[33m)[m
Author: Jérémie Ceintrey <jeremie.ceintrey@gmail.com>
Date:   Tue Jan 21 11:49:02 2025 +0100

    db-auth : Configuration de l'authentification des utilisateurs via db
    Mysql
         - modification du UserRepository pour rempalcer findByUsername par
           findByEmail qui sera utilisé pour construire un utilisateur pour
           l'authentification via Spring Security
         - Création du service CustomUserDetailsService implémentant un UserDetailsService pour l'authentification avec l'implémentation de la méthode loadByUsername
         - Création de la configuration Spring avec une chaine de filtre de
           sécurité, un Password Brcypt et un authenticationManager
         - Ajout d'une configuration SampleConfig pour définir des données Sample avec
           pour l'instant un utilisateur Sample

[33mcommit d3c0517ae7f91a6a38d46c6a1454c55e6f577a99[m
Merge: e428b3f 00e043c
Author: jceintrey <jeremie.ceintrey@gmail.com>
Date:   Mon Jan 20 15:45:54 2025 +0100

    Merge pull request #1 from jceintrey/feature/setup-db
    
    setup-db : Configuration initiale de la base de donnée Mysql

[33mcommit 00e043c758e88d24fb0a96a081694eaca34ed4df[m[33m ([m[1;31morigin/feature/setup-db[m[33m)[m
Author: Jérémie Ceintrey <jeremie.ceintrey@gmail.com>
Date:   Mon Jan 20 15:38:23 2025 +0100

    setup-db : Configuration initiale de la base de donnée Mysql
     - configuration datasource au niveau de application.properties
     - configuration hibernate en create-drop pour les tests
     - définition des models Message, User et Rental pour représenter
       respectivement les tables messages, users et rentals
     - utilisationdes anotations JPA ManyToOne et JoinColumn pour la
       définition des clés étrangères vers rental et user
     - définition de l'interface UserRepository avec une méthode custom

[33mcommit e428b3f326bac06d47d5fb6d690cb9681f28120a[m
Author: Jérémie Ceintrey <jeremie.ceintrey@gmail.com>
Date:   Fri Jan 17 09:25:54 2025 +0100

    Initial Commit
