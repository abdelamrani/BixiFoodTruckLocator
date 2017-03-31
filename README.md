# Bixi-FoodTruckLocator

## Prérequis

- Java 1.8
- Maven 3
- Postgresql 9.5 ou plus
- PostGIS pour Postgresql
- pgcrypto

## Compilation et exécution

    $ mvn spring-boot:run

    ou

    $ ./run.sh

Le projet est alors disponible à l'adresse [http://localhost:8080/](http://localhost:8080/)

## Routes disponibles

- [http://localhost:8080/](http://localhost:8080/)
- [http://localhost:8080/citations](http://localhost:8080/citations)
- [http://localhost:8080/citations/1](http://localhost:8080/citations/1)
- [http://localhost:8080/citations/2](http://localhost:8080/citations/2)

## BD

La BD Postgresql doit être installée et le schéma créé pour que l'application démarre correctement.
Pour créer le schéma et les tables, veuillez exécuter le script ``CREATE_DB.sql``. L'utilisateur se connectant à la BD
ainsi que son mot de passe sont spécifiés dans le fichier ``application.properties``.

### PostGIS

La BD nécessite l'ajout de l'extension **PostGIS**. Pour des exemples et les instructions d'installation, veuillez
consulter la page [suivante](http://shisaa.jp/postset/postgis-postgresqls-spatial-partner-part-2.html).

### pgcrypto

La BD nécessite l'ajout de l'extension **pgcrypto**. Pour l'installer sous Linux, exécutez la commande `sudo apt-get install postgresql-contrib`.

### Pour exécuter le script sql dans postgres au path spécifié
psql -U postgres -f sql/create.sql

### Pour se connecter à la BD "projet"
psql -U postgres projet

### Pour faire un refresh de ressources sans devoir tout partir à noveau
mvn compile (dans un nouvel onglet du terminal)
