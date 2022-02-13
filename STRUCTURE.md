# Structure du projet

## 1 / Routage et hiérarchie JavaFX

### Élèments :

![](./assets/2022-02-13-11-56-10-image.png)

- Le **Routeur** est une classe statique (Pas d'instance, toutes les méthodes sont `static`) permettant de changer la page (ou scène actuellement affichée à l'écran).

- Un **Stage** JavaFX est tout simplement la fenêtre de l'application, soit ce qui contient les boutons réduire / plein écran / fermer et la racine de la hiérarchie (RootScene)

- La classe **RootScene** est une scène javaFX contenant
  
  - La Barre de navigation
  
  - La page courante (Scène JFX) qui est du type **BaseScene**
  
  - **RootScène n'est jamais déchargée** (Il y a une unique instance dans le projet)

- La classe **BaseScene** est une page de l'application, cette dernière est chargée / déchargée par RootScene.
  
  - BaseScene.java est abstraite, il faut donc extends pour en faire une vraie page :
  
  - ```java
    public class LoginScene extends BaseScene
    ```
    
    - ici LoginScene contiendra ensuite ses éléments : boutons, labels, etc...

### Changer de page :

Pour changer de page, il faut changer l'instance de BaseScene actuellement affichée par RootScene.

Pour cela : 

```java
Router.goToScreen(Routes.HOME);
```

1. `Router.goToScreen(...)` -> dis à la RootScene de changer sa scène courante

2.  RootScene efface sa scène courante et affiche la nouvelle donnée par le routeur.

### Hiérarchie sous forme d'arbre :

![](./assets/2022-02-13-12-07-28-image.png)
