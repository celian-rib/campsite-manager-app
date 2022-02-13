# Structure du projet

## 1 / Routage et hiérarchie JavaFX

### Élèments :

![](/Users/celian/Desktop/flots-blancs/assets/2022-02-13-11-56-10-image.png)

- Le **Routeur** est une classe statique permettant de changer la page (ou scène actuellement affichée à l'écran).

- Un **Stage** JavaFX est tout simplement la fenêtre de l'application

> Soit ce qui contient les boutons réduire / plein écran / fermer

- La classe **RootScene** est une scène javaFX contenant
  
  - La Barre de navigation
  
  - La page courante (Scène JFX) qui est du type **BaseScene**
  
  - **RootScène n'est jamais déchargée**

- La classe **BaseScene** est donc une page de l'application, cette dernière est chargée / déchargée en fonction de la page sélectionné via la barre de navigation.
  
  - BaseScene.java est abstraite, il faut donc extends pour en faire une vraie page :
  
  - ```java
    public class LoginScene extends BaseScene
    ```
    
    - ici LoginScene contiendra ensuite ses éléments : boutons, labels, etc...

### Hiérarchie sous form d'arbre :

![](/Users/celian/Desktop/flots-blancs/assets/2022-02-13-12-07-28-image.png)


