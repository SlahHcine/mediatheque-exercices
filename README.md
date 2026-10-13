# Médiathèque · Exercices Java

Formation **Java · Spring Boot · Hibernate / JPA** — Slah Hassine

Squelette prêt à l'emploi des 4 exercices autour du projet **mediatheque-api**.
📄 **L'énoncé complet est dans [Enonce_Exercices_Mediatheque.pdf](Enonce_Exercices_Mediatheque.pdf)** : lis-le avant de commencer.

| Fichier (`src/`) | Exercice | Notions |
|---|---|---|
| `Exercice1_Adherent.java` | L'adhérent | Programmation objet, encapsulation, validation |
| `Exercice2_Catalogue.java` | Le catalogue | Streams, Optional, records |
| `Exercice3_PolitiqueEmprunt.java` | La politique d'emprunt | Règles R1 à R5, exceptions, java.time, BigDecimal |
| `Exercice4_ServiceEmprunt.java` | Le service d'emprunt | Synthèse : emprunter et rendre |

## Ouvrir le projet dans IntelliJ

1. Bouton vert **Code › Download ZIP**.
2. Décompresse le zip à côté de `mediatheque-api` (par exemple dans `~/dev`) et renomme le dossier `mediatheque-exercices-main` en `mediatheque-exercices`.
3. IntelliJ : `File › Open…` sur le dossier `mediatheque-exercices`, puis **Trust Project**.

Si IntelliJ affiche *Project JDK is not defined* : **Setup SDK** et choisis le **JDK 25**.

## Lancer un exercice

En haut à droite, choisis **Exercice 1 - Adherent** puis clique sur la flèche verte.
Relancer : `Ctrl + R` (Mac) · `Shift + F10` (Windows).

Sans IntelliJ (Java 21 ou plus) :

```bash
cd src
java Exercice1_Adherent.java
```

## Règles du jeu

- Remplace chaque `throw new UnsupportedOperationException("À compléter");` par ton code.
- Ne modifie pas la partie **VÉRIFICATIONS** en bas de chaque fichier.
- Objectif : toutes les vérifications à `[OK]`, par exemple `Score : 11 / 11  Bravo !`
