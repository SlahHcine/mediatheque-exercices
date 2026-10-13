import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.function.BooleanSupplier;

/**
 * EXERCICE 1 · L'adhérent (programmation objet, encapsulation, validation)
 *
 * Contexte : la médiathèque enregistre ses adhérents. Un adhérent ne doit jamais être
 * créé avec des données invalides : c'est lui-même qui vérifie ses données (modèle riche).
 *
 * Lancer : java Exercice1_Adherent.java
 * Objectif : toutes les vérifications à [OK].
 */
public class Exercice1_Adherent {

    static class Adherent {

        public static final int PERIODE_NOUVEL_ADHERENT_JOURS = 7;

        private final String nom;
        private final String prenom;
        private final String email;
        private final LocalDate dateInscription;
        private boolean actif;

        /**
         * Q1. Le constructeur refuse les données invalides (IllegalArgumentException) :
         *  - nom ou prénom null ou vide (ou seulement des espaces) → "Le nom est obligatoire" / "Le prénom est obligatoire"
         *  - email null, sans @ ou sans point → "Email invalide"
         * Il enregistre le nom et le prénom sans espaces autour, l'email sans espaces et en minuscules.
         * Un nouvel adhérent est actif.
         */
        Adherent(String nom, String prenom, String email, LocalDate dateInscription) {
            // TODO : vérifie chaque paramètre (isBlank, contains), puis affecte les attributs (strip, toLowerCase)
            throw new UnsupportedOperationException("À compléter");
        }

        public String getNom() { return nom; }
        public String getPrenom() { return prenom; }
        public String getEmail() { return email; }
        public LocalDate getDateInscription() { return dateInscription; }
        public boolean isActif() { return actif; }

        /** Q2. Renvoie « Prénom NOM », par exemple « Alice DURAND ». */
        public String getNomComplet() {
            // TODO : concatène le prénom, un espace et le nom en majuscules
            throw new UnsupportedOperationException("À compléter");
        }

        /** Q3. Désactive l'adhérent (il ne pourra plus emprunter : règle R3). */
        public void desactiver() {
            // TODO : une seule ligne
            throw new UnsupportedOperationException("À compléter");
        }

        /** Q3 bis. Réactive l'adhérent. */
        public void reactiver() {
            // TODO : une seule ligne
            throw new UnsupportedOperationException("À compléter");
        }

        /** Q4. Un adhérent est « nouveau » s'il est inscrit depuis MOINS de 7 jours. */
        public boolean estNouveau(LocalDate aujourdhui) {
            // TODO : ChronoUnit.DAYS.between(dateInscription, aujourdhui)
            throw new UnsupportedOperationException("À compléter");
        }

        /**
         * Q5. Règle R6 : l'email est unique. Deux adhérents sont donc égaux s'ils ont le même email.
         * Redéfinir equals ET hashCode (toujours les deux ensemble).
         */
        @Override
        public boolean equals(Object o) {
            // TODO : même référence → true ; instanceof Adherent autre → comparer les emails
            throw new UnsupportedOperationException("À compléter");
        }

        @Override
        public int hashCode() {
            // TODO : Objects.hash(...) sur le même attribut que equals
            throw new UnsupportedOperationException("À compléter");
        }

        @Override
        public String toString() {
            return "Adherent[" + getNomComplet() + ", " + email + (actif ? "" : ", inactif") + "]";
        }
    }

    // ============================== VÉRIFICATIONS (ne pas modifier) ==============================
    public static void main(String[] args) {
        LocalDate aujourdhui = LocalDate.of(2026, 10, 17);
        System.out.println("Exercice 1 · L'adhérent");

        verifier("Q1 · un adhérent valide est créé et actif", () -> {
            var a = new Adherent("Durand", "Alice", "alice@mail.fr", aujourdhui);
            return a.isActif() && a.getNom().equals("Durand");
        });
        verifier("Q1 · les espaces sont retirés et l'email est en minuscules", () -> {
            var a = new Adherent("  Durand ", " Alice", "  Alice.Durand@Mail.FR ", aujourdhui);
            return a.getNom().equals("Durand") && a.getPrenom().equals("Alice")
                    && a.getEmail().equals("alice.durand@mail.fr");
        });
        verifierException("Q1 · nom vide refusé", IllegalArgumentException.class, "nom",
                () -> new Adherent("   ", "Alice", "alice@mail.fr", aujourdhui));
        verifierException("Q1 · prénom null refusé", IllegalArgumentException.class, "prénom",
                () -> new Adherent("Durand", null, "alice@mail.fr", aujourdhui));
        verifierException("Q1 · email sans @ refusé", IllegalArgumentException.class, "Email",
                () -> new Adherent("Durand", "Alice", "alice.mail.fr", aujourdhui));
        verifier("Q2 · nom complet = « Alice DURAND »",
                () -> new Adherent("Durand", "Alice", "alice@mail.fr", aujourdhui).getNomComplet().equals("Alice DURAND"));
        verifier("Q3 · désactiver puis réactiver", () -> {
            var a = new Adherent("Durand", "Alice", "alice@mail.fr", aujourdhui);
            a.desactiver();
            boolean inactif = !a.isActif();
            a.reactiver();
            return inactif && a.isActif();
        });
        verifier("Q4 · inscrit il y a 6 jours → nouveau",
                () -> new Adherent("Durand", "Alice", "a@b.fr", aujourdhui.minusDays(6)).estNouveau(aujourdhui));
        verifier("Q4 · inscrit il y a 7 jours → plus nouveau (limite exacte)",
                () -> !new Adherent("Durand", "Alice", "a@b.fr", aujourdhui.minusDays(7)).estNouveau(aujourdhui));
        verifier("Q5 · même email → égaux, même hashCode", () -> {
            var a = new Adherent("Durand", "Alice", "alice@mail.fr", aujourdhui);
            var b = new Adherent("Dupont", "Alicia", "ALICE@mail.fr", aujourdhui.minusDays(30));
            return a.equals(b) && a.hashCode() == b.hashCode();
        });
        verifier("Q5 · emails différents → pas égaux",
                () -> !new Adherent("Durand", "Alice", "alice@mail.fr", aujourdhui)
                        .equals(new Adherent("Durand", "Alice", "bob@mail.fr", aujourdhui)));
        bilan();
    }

    // ------------------------------ outils de vérification ------------------------------
    static int reussis = 0, total = 0;

    static void verifier(String description, BooleanSupplier test) {
        total++;
        try {
            if (test.getAsBoolean()) { reussis++; System.out.println("  [OK]      " + description); }
            else System.out.println("  [ERREUR]  " + description);
        } catch (UnsupportedOperationException e) {
            System.out.println("  [A FAIRE] " + description);
        } catch (Exception e) {
            System.out.println("  [ERREUR]  " + description + " → " + e);
        }
    }

    static void verifierException(String description, Class<? extends Exception> attendue, String messageContient, Runnable code) {
        total++;
        try {
            code.run();
            System.out.println("  [ERREUR]  " + description + " → aucune exception levée");
        } catch (UnsupportedOperationException e) {
            System.out.println("  [A FAIRE] " + description);
        } catch (Exception e) {
            if (attendue.isInstance(e) && e.getMessage() != null && e.getMessage().contains(messageContient)) {
                reussis++; System.out.println("  [OK]      " + description);
            } else {
                System.out.println("  [ERREUR]  " + description + " → " + e);
            }
        }
    }

    static void bilan() {
        System.out.println("Score : " + reussis + " / " + total + (reussis == total ? "  Bravo !" : ""));
    }
}
