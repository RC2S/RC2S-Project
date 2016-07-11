# Raspberry Cubic Control System (RC2S)
----------

Composé d’un **ensemble de cubes de LED** s’animant au rythme de la musique ou du son ambiant,  
la suite RC2S vient s’étoffer d’un ensemble de **solutions logicielles** permettant l’administration complète des différents cubes en votre possession.  
Une dimension créative s’y ajoute, l’extensibilité offerte par la création de **plugins** faisant de votre imagination la seule limite à son évolution.

Dans sa **version initiale**, la suite RC2S propose à l’utilisateur de gérer les différents cubes de LED qu’il possède,  
d’interagir avec ces derniers en allumant ou éteignant chaque LED qui les composent, de jouer les différentes animations préenregistrées…  
Il aura également la possibilité d’installer des plugins créés pour améliorer l’expérience utilisateur en ajoutant de nouvelles fonctionnalités.

Un utilisateur avancé pourra, quant à lui, profiter des mêmes fonctionnalités tout en ayant la possibilité de **créer**  
lui-même **ses propres plugins** à travers une **API dédiée**.  
Il pourra ainsi développer de nouveaux contenus qui pourront être intégrés dans la solution RC2S et partagés avec la communauté !

Vous retrouverez tous les détails du projet dans la [documentation complète].

## La Solution
----------


Version : **1.0**

- **RC2S Pi Station** : Ensemble matériel composé d’un Cube de LED connecté à une carte Raspberry Pi.

- **RC2S Daemon** : Programme Java installé sur la carte Raspberry Pi recevant des instructions de la part du serveur et permettant le contrôle du Cube de LED connecté sur la même carte.

- **RC2S Application Server** : Serveur d’application JEE centralisant la gestion de Pi Stations et servant de relais pour les clients.

- **RC2S Client** : Client lourd JavaFX permettant la connexion à un serveur d’application RC2S et la gestion des Cubes liés à un utilisateur.

- **RC2S Plugin Maker** : Environnement de développement de plugins pour la suite RC2S, intégrant entre autre fonctionnalité un parser d’annotations pour faciliter et alléger le développement.  
Les plugins ainsi créés pourront interagir avec le serveur et le client, permettant l’ajout de nouvelles fonctionnalités suivant les besoins de l’utilisateur.

## Installation
----------

Les prérequis :

- [JDK 8] - Java Development Kit 8+
- [Gradle 2.14+] - Moteur de production Java
- [Payara 162+] - Serveur d'applications Java
- [Node.js 6.2+] - Javascript côté serveur

Obtenir le projet :

```sh
$ git clone [git-repo-url] rc2s-project
$ cd rc2s-project
```

Compilation du projet Java :

- Sans le déploiement automatique des applications générées :
```sh
$ gradle clean publishToMavenLocal install build -PskipServerAutodeploy=true -PskipJNLPAutodeploy=true
```

- Avec le déploiement automatique sur le serveur d'application Payara :

Suivre la [configuration du serveur Payara] puis
```sh
$ export RC2S_HOME="/path/to/payara/glassfish/domains/rc2s-payara"
$ gradle clean publishToMavenLocal install build
```

- Compilation en environnement de Production :
```sh
$ export RC2S_HOME="/path/to/payara/glassfish/domains/rc2s-payara"
$ gradle clean publishToMavenLocal install build -Penv=prod
```

Lancement du projet Node JS :
```sh
$ cd rc2s-pluginmaker
$ npm install
$ node app.js
```

## Architecture Logicielle
----------
Photo


## License
----------

RC2S est un projet annuel de 3ème année proposé par des étudiants de l'ESGI (Ecole Supérieure de Génie Informatique).  
Développé par Mathieu BOISNARD, Valentin FRIES et Vincent MILANO.

   [JDK 8]: <http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html>
   [Gradle 2.14+]: <https://gradle.org/gradle-download/>
   [Payara 162+]: <http://www.payara.fish/downloads>
   [Node.js 6.2+]: <http://nodejs.org>
   [documentation complète]: <http://github.com>
   [configuration du serveur Payara]: <http://github.com>