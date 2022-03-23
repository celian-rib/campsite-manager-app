# PT4 - FlotsBlancs

[![pipeline status](https://gitlab-ce.iut.u-bordeaux.fr/criboulet/pt4/badges/main/pipeline.svg)](https://gitlab-ce.iut.u-bordeaux.fr/criboulet/pt4/-/commits/main)

## Setup projet

1. Clone le projet `git clone git@gitlab-ce.iut.u-bordeaux.fr:criboulet/pt4.git`

2. Télécharger le [SDK JavaFX](https://gluonhq.com/products/javafx/) (En fonction de l'OS de la machine) (Path du dossier installé a retenir pour la suite)

3. Installer les dépendances `mvn install`

4. Ouvrir le projet dans un IDE (VSCode ou Eclipse) :

    - **VSCODE** :
        1. Télécharger l'extension [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)
        2.  Ouvrir les paramètres UI de vscode et chercher "vmargs" :
        ![](./assets/vscode-setup.png)
        3. Ajouter ces arguments de lancement pour java
        ```
        --module-path "/CHEMIN/VERS/JAVAFX_SDK_A_TELECHARGER/lib" --add-modules javafx.controls,javafx.fxml
        ```

    - **Eclipse**
        - TODO

7. Le projet utilise [Lombok](https://projectlombok.org/features/GetterSetter) 

    - **VScode** :
        - il faut donc installer [cette extension](https://marketplace.visualstudio.com/items?itemName=GabrielBB.vscode-lombok) (Recharger VSCode si les erreurs persistent)

    - **Eclipse**
        - TODO

5. Lancer un serveur sql en local ou se connecter à l'iut via tunnel ssh (expliqué plus bas)

6. Editer le fichier `.env` et mettre les informations de connexion au serveur SQL

```bash
DB_URL=<url>
DB_USER=<user>
DB_PASSWORD=<password>
SECOND_SCREEN=1 # Ajouter si l'on veut démarrer l'app sur l'écran secondaire
FULL_SCREEN=1 # Ajouter si l'on veut démarrer l'app en plein écran
DEFAULT_ROUTE=<ROUTENAME> # Ajouter pour changer la route par défaut (Permet de gagner du temps en développement)
```

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

```
mvn test
```

## Build le projet

#### A faire pour le premier build

1. Installer le **SDK** de JavaFX depuis https://gluonhq.com/products/javafx/ et le dézipper dans une espace adapté.

2. Créez vous à la racine du projet un launcher.sh ou .bat ou autre en fonction de votre OS. A l'intérieur, mettre la commande

> Windows : 

```bash
java --module-path "chemin\vers\javafx\lib" --add-modules javafx.controls,javafx.fxml -jar target/flots-blancs-1.0.0.jar
pause
```

> Linux : 

```bash
java --module-path "path/to/javafx/lib" --add-modules javafx.controls,javafx.fxml -jar target/flots-blancs-1.0.0-jar-with-dependencies.jar
```
Ne pas oublier de faire chmod u+x launcher.sh.

#### Les autres builds

1. Aller à la racine du projet (/pt4), et effectuer la commande : mvn clean package

2. L'application est build et est mise dans /target

3. ajouter un .env à la racine du .jar en suivant les normes !

4. Pour la lancer, utilisez votre launcher avec la commande sur linux ./launcher.sh, ou sur windows en cliquant sur le .bat

## Résoudre erreur (Type mismatch: cannot convert from Date to Date)

> C'est une erreur entre vscode et maven, pour le coup le code n'est probablement pas le problème

** Résoudre le problème avec : ** `mvn clean package`
