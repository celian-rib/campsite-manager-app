# PT4 - FlotsBlancs

## 1/ Setup projet

1. Clone le projet `git@gitlab-ce.iut.u-bordeaux.fr:criboulet/pt4.git`
2. Installer dépendances `mvn clean package`
3. Installer ojdbc (Gestionnaire base de donnée pour l'ORM)
    ```
    mvn install:install-file -Dfile=ojdbc8.jar -DgroupId=com.oracle -DartifactId=ojdbc8 -Dversion=19.3 -Dpackaging=jar
    ```
4. Ouvrir le projet dans un IDE (VSCode ou Eclipse)
5. Lancer un serveur sql en local
6. Editer le fichier `.env` et mettre les informations de connexion au serveur SQL
```
DB_URL=""
DB_USER=root
DB_PASSWORD=root
```

## 2/ Ajouter une dépendance Maven

1. Ajouter la dépendance au fichier `pom.xml`
> Exemple :
```xml
<dependency>
    <groupId>io.github.cdimascio</groupId>
    <artifactId>dotenv-java</artifactId>
    <version>2.2.0</version>
</dependency>
```
2. Ajouter un require de cette dépendance dans `module-info.java`

## 3/ Ajouter une feature (Workflow)

1. Checkout sur main et pull
2. Créer une branche portant le nom de la feature : `git checkout -b ma-feature`
3. Coder la feature
4. Implémenter des tests pour cette feature (Si besoin)
5. Créer une Merge Request vers main
6. Demander à un autre membre de l'équipe de review la Merge Request
7. Merge

## 4/ Lancer les tests

// TO DO

## 5/ Build le projet

// TO DO