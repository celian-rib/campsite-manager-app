# JavaFX

## 1/ Stage

Un **Stage** java fx est le conteneur de l'application, cela correspond donc à la fenêtre de l'application.

On peut avoir plusieurs stages pour une application, dans ce cas on a plusieurs fenêtres.

Un stage affiche une seule et unique **Scene**.

## 2/ Scene

Une **Scene** est ce qui contient tout les élèments à afficher sur la fenêtre.

C'est la racine de l'arbre d'affichage. On peut lui mettre autant d'enfant que l'on veut, par exemple ajouter un bouton à la scène revient à l'ajouter comme enfant de la scène.

Chacun des éléèments que l'on ajoute à une scène font partie du `Scene Graph`, et l'ensemble nous donne un arbre :

![JavaFX overview of JavaFX internal application structure.](http://tutorials.jenkov.com/images/java-javafx/javafx-overview-1.png)

> En Orange => tous les élèments que l'on peut ajouter à une scène : Bouton / Label / etc...

> C'est élèments extends la classe `Node` -> Ce sont des neouds de l'arbre

## 3/ Visualisation globale de l'abre JFX

![JavaFX - Application](https://www.tutorialspoint.com/javafx/images/javafx_application_structure.jpg)

## 4/ Layouts

Les **Layouts** sont des composants (noeuds) qui contiennt d'autres noeuds.

- Par exemple on peut créer une boite qui contient 2 boutons et un autre Layout.
  
  - Dans ce cas la boite est un Layout avec trois enfants
  
  - 2 enfant sont des boutons
  
  - 1 enfant est un autre layout qui lui même à des enfants

![Working with the JavaFX Scene Graph | JavaFX 2 Tutorials and Documentation](https://docs.oracle.com/javafx/2/scenegraph/img/figure1.png)

Il existes différents types de **Layouts** chacu avec des spécificités différentes sur la manière de disposer leurs enfants à l'écran.

Exemples de layouts basiques :

- **HBox** : arrange une série de Node en ligne horizontalement
  
  - ![](https://s1.o7planning.com/fr/10625/images/2765408.gif)

- **VBox** : arrange une série de Node en ligne verticalement

- **BorderPane** : Permet de coller des élèments sur les bords de ce layout (haut/bas/gauche/droite)
  
  - ![](https://www.vojtechruzicka.com/963c18c24f70c59fe397e365e7a4bc19/anchor-pane-corners.gif)

- **FlowPane** : arrange les élèments horizontalement et revient à la ligne si il ny a pas asser de place pour un élèment
  
  - ![](https://s1.o7planning.com/en/10627/images/2760857.gif)

- **GridPane** : permet de placer les élèments dans une grille
  
  - ![](https://s1.o7planning.com/en/10641/images/2783200.gif)

- **StackPane** : place tous les élèments les uns sur les autres.
  
  - ![](https://johnloomis.org/ece538/notes/javafx/StackPane/2782505.gif)



## 5/ Controls

Les **Controls** sont les élèments donnant des fonctionnalités à l'application

Les plus iportants sont :

- Button

- Label

- CheckBox

- TextField

- RadioButton

- etc...


