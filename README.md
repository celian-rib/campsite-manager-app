# PT4 - FlotsBlancs

## Setup projet

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
DB_URL=<url>
DB_USER=<user>
DB_PASSWORD=<password>
```
7. Le projet utilise [Lombok](https://projectlombok.org/features/GetterSetter) , pour VScode il faut donc installer [cette extension](https://marketplace.visualstudio.com/items?itemName=GabrielBB.vscode-lombok)

## Setup tunnel SSH (Se connecter à la BD depuis chez soi)
> La base est actuellement hébergée sur l'intranet de l'IUT, pour établir une connexion avec une machine n'étant pas sur le réseau de l'IUT il faut setup un tunnel ssh

1. Ouvrir un terminal
```
ssh <idnum>@info-ssh2.iut.u-bordeaux.fr -L <port>:info-titania.iut.bx1:3306
```
Exemple : `ssh criboulet@info-ssh2.iut.u-bordeaux.fr -L 7777:info-titania.iut.bx1:3306`

2. Modifier le .env
> Le port dans DB URL doit être le même que celui donné dans la commande ssh précédente

> Ici on doit utiliser les identifiants de la base de Gaël. (Fichier .env disponible sur Discord)
```
DB_URL="jdbc:mysql://127.0.0.1:<port>/etu_gbarrebeylot"
DB_USER=<user>
DB_PASSWORD=<password>
```
## Ajouter une dépendance Maven

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

## Ajouter une feature (Workflow)

1. Checkout sur main et pull
2. Créer une branche portant le nom de la feature : `git checkout -b ma-feature`
3. Coder la feature
4. Implémenter des tests pour cette feature (Si besoin)
5. Créer une Merge Request vers main
6. Demander à un autre membre de l'équipe de review la Merge Request
7. Merge

## Lancer les tests

TO DO

## Build le projet

TO DO