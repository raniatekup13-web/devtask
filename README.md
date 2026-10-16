# DevTask

Application complète de gestion de projets et de tâches pour une équipe. L’interface React s’adapte au rôle de la personne connectée et l’API Spring Boot applique les mêmes règles côté serveur.

## Fonctions

- Création de compte développeur, connexion JWT et déconnexion.
- Tableau de bord avec projets, tâches, progression et indicateurs.
- Gestion des projets et des tâches par les administrateurs.
- Affectation des tâches aux développeurs et consultation de la liste des membres.
- Les développeurs voient les tâches qui leur sont attribuées et peuvent en changer le statut; une tâche non attribuée n’est visible que par l’admin.
- Recherche, filtres, dates d’échéance et interface responsive.
- Synchronisation automatique entre les rôles (actualisation silencieuse toutes les 6 secondes).
- Bootstrap 5 intégré au bundle, illustration vectorielle locale et aperçu animé du flux des tâches.
- PostgreSQL, validation, BCrypt, documentation OpenAPI et Docker Compose.

## Démarrage avec Docker

Prérequis : Docker Desktop avec Docker Compose.
 
1. Depuis ce dossier, démarrer les services :

Déconnectez-vous puis reconnectez-vous pour obtenir un nouveau jeton avec le rôle administrateur. Les variables `POSTGRES_USER` et `POSTGRES_DB` personnalisées dans `.env` doivent aussi être utilisées dans la commande `psql`.

- Application : [http://localhost:5173](http://localhost:5173)
- API : [http://localhost:8080/api](http://localhost:8080/api)
- Swagger : [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

 
## Développement local

PostgreSQL doit être démarré et les variables `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` et `JWT_SECRET` doivent être définies dans l’environnement du backend. Puis :


## API et rôles

- `POST /api/auth/register` et `POST /api/auth/login` sont publics.
- Tous les autres endpoints nécessitent `Authorization: Bearer <token>`.
- `ADMIN` gère les projets et les tâches, consulte tous les utilisateurs et attribue les tâches.
- `DEVELOPER` consulte les projets, ne reçoit que les tâches qui lui sont attribuées et peut mettre à jour leur statut.
- Les modifications sont synchronisées automatiquement avec les autres sessions connectées; la page affiche aussi l’heure de dernière synchronisation.
- Les mots de passe ne sont jamais inclus dans les réponses de l’API.

 
