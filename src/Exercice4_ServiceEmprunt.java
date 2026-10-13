import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * EXERCICE 4 · Le service d'emprunt (synthèse : objets, Streams, Optional, exceptions, règles)
 *
 * Contexte : le service orchestre un emprunt de bout en bout. Il retrouve le livre et l'adhérent,
 * fait vérifier les règles par la PolitiqueEmprunt, met à jour le stock et enregistre l'emprunt.
 * Les données sont gardées en mémoire (des listes) : la base de données arrivera en séance 6.
 *
 * Tout est FOURNI sauf la classe ServiceEmprunt, à compléter.
 *
 * Lancer : java Exercice4_ServiceEmprunt.java
 */
public class Exercice4_ServiceEmprunt {

    // ============================== FOURNI : exceptions métier ==============================
    static class RegleMetierException extends RuntimeException {             // → HTTP 409
        RegleMetierException(String message) { super(message); }
    }
    static class RessourceIntrouvableException extends RuntimeException {    // → HTTP 404
        RessourceIntrouvableException(String message) { super(message); }
    }

    // ============================== FOURNI : le modèle ==============================
    static class Livre {
        private final String isbn;
        private final String titre;
        private int disponibles;

        Livre(String isbn, String titre, int disponibles) {
            this.isbn = isbn; this.titre = titre; this.disponibles = disponibles;
        }
        String getIsbn() { return isbn; }
        String getTitre() { return titre; }
        int getDisponibles() { return disponibles; }
        void emprunterExemplaire() { disponibles--; }
        void rendreExemplaire() { disponibles++; }
    }

    static class Adherent {
        private final String email;
        private final boolean actif;
        Adherent(String email, boolean actif) { this.email = email; this.actif = actif; }
        String getEmail() { return email; }
        boolean isActif() { return actif; }
    }

    enum StatutEmprunt { EN_COURS, RENDU }

    static class Emprunt {
        private final long id;
        private final Livre livre;
        private final Adherent adherent;
        private final LocalDate dateEmprunt;
        private final LocalDate dateRetourPrevue;
        private LocalDate dateRetourEffective;
        private BigDecimal penalite = BigDecimal.ZERO.setScale(2);
        private StatutEmprunt statut = StatutEmprunt.EN_COURS;

        Emprunt(long id, Livre livre, Adherent adherent, LocalDate dateEmprunt, LocalDate dateRetourPrevue) {
            this.id = id; this.livre = livre; this.adherent = adherent;
            this.dateEmprunt = dateEmprunt; this.dateRetourPrevue = dateRetourPrevue;
        }
        void cloturer(LocalDate dateRetourEffective, BigDecimal penalite) {
            this.dateRetourEffective = dateRetourEffective;
            this.penalite = penalite;
            this.statut = StatutEmprunt.RENDU;
        }
        long getId() { return id; }
        Livre getLivre() { return livre; }
        Adherent getAdherent() { return adherent; }
        LocalDate getDateRetourPrevue() { return dateRetourPrevue; }
        BigDecimal getPenalite() { return penalite; }
        StatutEmprunt getStatut() { return statut; }
        boolean estEnCours() { return statut == StatutEmprunt.EN_COURS; }
    }

    // ============================== FOURNI : la politique (corrigé de l'exercice 3) ==============================
    static class PolitiqueEmprunt {
        void verifierEligibilite(boolean actif, boolean aUnRetard, int empruntsEnCours, int disponibles) {
            if (!actif) throw new RegleMetierException("R3 · adhérent inactif");
            if (aUnRetard) throw new RegleMetierException("R3 · emprunt en retard");
            if (empruntsEnCours >= 3) throw new RegleMetierException("R1 · 3 emprunts en cours maximum");
            if (disponibles <= 0) throw new RegleMetierException("R4 · aucun exemplaire disponible");
        }
        LocalDate dateRetourPrevue(LocalDate dateEmprunt) { return dateEmprunt.plusDays(21); }
        boolean estEnRetard(LocalDate prevue, LocalDate aujourdhui) { return aujourdhui.isAfter(prevue); }
        BigDecimal calculerPenalite(LocalDate prevue, LocalDate effective) {
            long jours = ChronoUnit.DAYS.between(prevue, effective);
            if (jours <= 0) return BigDecimal.ZERO.setScale(2);
            return new BigDecimal("0.20").multiply(BigDecimal.valueOf(jours))
                    .min(new BigDecimal("10.00")).setScale(2, RoundingMode.HALF_UP);
        }
    }

    // ============================== À COMPLÉTER : le service ==============================
    static class ServiceEmprunt {
        private final List<Livre> livres;
        private final List<Adherent> adherents;
        private final List<Emprunt> emprunts = new ArrayList<>();
        private final PolitiqueEmprunt politique = new PolitiqueEmprunt();
        private long prochainId = 1;

        ServiceEmprunt(List<Livre> livres, List<Adherent> adherents) {
            this.livres = livres;
            this.adherents = adherents;
        }

        /** Q1. Retrouver un livre par ISBN, sinon RessourceIntrouvableException("Livre introuvable : " + isbn). */
        Livre trouverLivre(String isbn) {
            // TODO : stream, filter, findFirst, orElseThrow(() -> new RessourceIntrouvableException(...))
            throw new UnsupportedOperationException("À compléter");
        }

        /** Q1 bis. Retrouver un adhérent par email, sinon RessourceIntrouvableException("Adhérent introuvable : " + email). */
        Adherent trouverAdherent(String email) {
            // TODO : même principe que trouverLivre
            throw new UnsupportedOperationException("À compléter");
        }

        /** Q2. Les emprunts EN COURS d'un adhérent. */
        List<Emprunt> empruntsEnCours(String email) {
            // TODO : filter sur l'email de l'adhérent ET estEnCours()
            throw new UnsupportedOperationException("À compléter");
        }

        /** Q3. Vrai si l'un des emprunts en cours de l'adhérent est en retard à la date donnée. */
        boolean aUnRetard(String email, LocalDate aujourdhui) {
            // TODO : anyMatch avec politique.estEnRetard(e.getDateRetourPrevue(), aujourdhui)
            throw new UnsupportedOperationException("À compléter");
        }

        /**
         * Q4. Emprunter un livre :
         *  1. retrouver le livre et l'adhérent (404 s'ils n'existent pas)
         *  2. faire vérifier les règles par la politique (R1, R3, R4)
         *  3. retirer un exemplaire du stock
         *  4. créer l'emprunt (id = prochainId++, retour prévu par R2), l'ajouter à la liste, le renvoyer
         */
        Emprunt emprunter(String isbn, String email, LocalDate dateEmprunt) {
            // TODO : réutilise les méthodes Q1 à Q3, puis politique.verifierEligibilite(...)
            throw new UnsupportedOperationException("À compléter");
        }

        /**
         * Q5. Rendre un livre :
         *  1. retrouver l'emprunt par id, sinon RessourceIntrouvableException("Emprunt introuvable : " + id)
         *  2. s'il est déjà rendu → RegleMetierException("Emprunt déjà rendu")
         *  3. calculer la pénalité (R5), clôturer l'emprunt, remettre l'exemplaire en stock
         */
        Emprunt rendre(long empruntId, LocalDate dateRetour) {
            // TODO : findFirst + orElseThrow, puis if (!emprunt.estEnCours()) throw ..., puis cloturer et rendreExemplaire
            throw new UnsupportedOperationException("À compléter");
        }
    }

    // ============================== VÉRIFICATIONS (ne pas modifier) ==============================
    static ServiceEmprunt nouveauService() {
        return new ServiceEmprunt(
                List.of(new Livre("978-01", "Dune", 2), new Livre("978-02", "Fondation", 1),
                        new Livre("978-03", "1984", 3), new Livre("978-04", "Hypérion", 1)),
                List.of(new Adherent("alice@mail.fr", true), new Adherent("bob@mail.fr", false)));
    }

    public static void main(String[] args) {
        LocalDate j1 = LocalDate.of(2026, 10, 1);
        System.out.println("Exercice 4 · Le service d'emprunt");

        verifier("Q1 · trouver le livre 978-01 → Dune", () -> nouveauService().trouverLivre("978-01").getTitre().equals("Dune"));
        verifierException("Q1 · ISBN inconnu → 404", RessourceIntrouvableException.class, "Livre introuvable",
                () -> nouveauService().trouverLivre("000"));
        verifierException("Q1 · email inconnu → 404", RessourceIntrouvableException.class, "Adhérent introuvable",
                () -> nouveauService().trouverAdherent("zoe@mail.fr"));
        verifier("Q4 · Alice emprunte Dune : emprunt n°1, retour le 22/10, stock 2 → 1", () -> {
            var s = nouveauService();
            var e = s.emprunter("978-01", "alice@mail.fr", j1);
            return e.getId() == 1 && e.getDateRetourPrevue().equals(LocalDate.of(2026, 10, 22))
                    && s.trouverLivre("978-01").getDisponibles() == 1;
        });
        verifier("Q2 · après 2 emprunts, Alice a 2 emprunts en cours", () -> {
            var s = nouveauService();
            s.emprunter("978-01", "alice@mail.fr", j1);
            s.emprunter("978-03", "alice@mail.fr", j1);
            return s.empruntsEnCours("alice@mail.fr").size() == 2;
        });
        verifierException("Q4 · R3 : Bob est inactif → refusé", RegleMetierException.class, "inactif",
                () -> nouveauService().emprunter("978-01", "bob@mail.fr", j1));
        verifierException("Q4 · R4 : Fondation n'a qu'un exemplaire, 2e emprunt refusé", RegleMetierException.class, "R4", () -> {
            var s = nouveauService();
            s.emprunter("978-02", "alice@mail.fr", j1);
            s.emprunter("978-02", "alice@mail.fr", j1);
        });
        verifierException("Q4 · R1 : un 4e emprunt est refusé", RegleMetierException.class, "R1", () -> {
            var s = nouveauService();
            s.emprunter("978-01", "alice@mail.fr", j1);
            s.emprunter("978-02", "alice@mail.fr", j1);
            s.emprunter("978-03", "alice@mail.fr", j1);
            s.emprunter("978-04", "alice@mail.fr", j1);
        });
        verifier("Q3 · le 30 octobre, un emprunt du 1er octobre est en retard", () -> {
            var s = nouveauService();
            s.emprunter("978-01", "alice@mail.fr", j1);
            return !s.aUnRetard("alice@mail.fr", LocalDate.of(2026, 10, 22))
                    && s.aUnRetard("alice@mail.fr", LocalDate.of(2026, 10, 30));
        });
        verifierException("Q4 · R3 : avec un retard, nouvel emprunt refusé", RegleMetierException.class, "retard", () -> {
            var s = nouveauService();
            s.emprunter("978-01", "alice@mail.fr", j1);
            s.emprunter("978-03", "alice@mail.fr", LocalDate.of(2026, 10, 30));
        });
        verifier("Q5 · rendu le 30/10 : 8 jours de retard → 1.60 €, statut RENDU, stock remis", () -> {
            var s = nouveauService();
            var e = s.emprunter("978-01", "alice@mail.fr", j1);
            var rendu = s.rendre(e.getId(), LocalDate.of(2026, 10, 30));
            return rendu.getPenalite().toString().equals("1.60") && rendu.getStatut() == StatutEmprunt.RENDU
                    && s.trouverLivre("978-01").getDisponibles() == 2 && s.empruntsEnCours("alice@mail.fr").isEmpty();
        });
        verifierException("Q5 · rendre deux fois → refusé", RegleMetierException.class, "déjà rendu", () -> {
            var s = nouveauService();
            var e = s.emprunter("978-01", "alice@mail.fr", j1);
            s.rendre(e.getId(), LocalDate.of(2026, 10, 10));
            s.rendre(e.getId(), LocalDate.of(2026, 10, 11));
        });
        verifierException("Q5 · emprunt inconnu → 404", RessourceIntrouvableException.class, "Emprunt introuvable",
                () -> nouveauService().rendre(99, j1));
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
