## ORMLite

> L'ORM utilisé dans le projet

## Créer une entité

```java
// 1 Créer une instance de l'entité
var client = new Client();
client.setFirstName("Tom");
client.setName("Cruise");

// 2 La faire persister (Mettre dans la BD)
Database.getInstance().getClientsDao().create(client);

// 3 Initialiser les relations (A mettre que si l'entité référence une autre entité)
// Ici client à un attribut "reservations" qui référence la classe Reservation
// il faut donc mettre le refresh (Voir différences update / refresh plus bas)
Database.getInstance().getClientsDao().refresh(testClient);
```

## Mettre à jour une entité

```java
// Ajout du numéro de téléhpone sur l'entité
client.setPhone("+33 00 00 00 00 00");

// Mise à jour dans la BD
Database.getInstance().getClientsDao().update(client);
```

## Supprimer une entité

```java
Database.getInstance().getClientsDao().delete(client);
```

## Différence Update / Refresh

**update** écrit les changements faits sur un objet depuis la mémoire vers la base de données

**refresh** met à jour l'objet en mémoire depuis la base de données

## DAOs

Un **DAO** pour `Data Access Object` est un Objet représentant une table de la BD.
Il en existe donc un par table, ces derniers sont accessibles dans le Singleton de
Database.java

```java
Database.getInstance().getXXXXXXDao().??
```

Il existe plusiuers méthodes sur ce DA, les plus importantes sont :
```java
// queryForAll pour récupérer une liste de toutes les entités de la table (Attentions aux perfs)
Database.getInstance().getUsersDao().queryForAll()
```
```java
// forEach pour directement itérer sur la table (Attentions aux perfs)
Database.getInstance().getUsersDao().forEach(user -> {
    System.out.println(user.getName());
});
```
```java
// Query builder pour former des requêtes
Database.getInstance().getUsersDao().queryBuilder().where().eq("login", id);
```
