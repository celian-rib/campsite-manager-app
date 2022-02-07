# Init ORM

si erreur jlink alors on supprime le gros truc plugin dans maven

On doit config jdbc avec le jar et cette commande

mvn install:install-file -Dfile=ojdbc8.jar -DgroupId=com.oracle -DartifactId=ojdbc8 -Dversion=19.3 -Dpackaging=jar


Le module info est dans la branche d'Emilien et ça marche niquel

Connexion au mysql

"jdbc:mysql://IP:PORT/Nom_Base","USER","PASSWORD"