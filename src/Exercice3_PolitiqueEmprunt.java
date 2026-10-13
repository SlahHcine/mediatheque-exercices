import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.function.BooleanSupplier;

/**
 * EXERCICE 3 · La politique d'emprunt (règles R1 à R5, exceptions métier, java.time, BigDecimal)
 *
 * Contexte : c'est le cœur métier de la médiathèque. La classe PolitiqueEmprunt est en Java pur :
 * pas de base de données, pas de Spring. Quand une règle est violée, elle lève une
 * RegleMetierException (que l'API traduira plus tard en HTTP 409).
 *
 * Rappel des règles :
 *   R1 · 3 emprunts en cours maximum par adhérent
 *   R2 · un emprunt dure 21 jours
 *   R3 · pas d'emprunt si l'adhérent est inactif ou a un emprunt en retard
 *   R4 · au moins un exemplaire disponible pour emprunter
 *   R5 · 0,20 € par jour de retard, plafonné à 10 €
 *
 * Lancer : java Exercice3_PolitiqueEmprunt.java
 */
public class Exercice3_PolitiqueEmprunt {

    /** Exception métier : une RuntimeException, donc pas de « throws » à écrire. */
    static class RegleMetierException extends RuntimeException {
        RegleMetierException(String message) { super(message); }
    }

    static class PolitiqueEmprunt {
        static final int MAX_EMPRUNTS_EN_COURS = 3;
        static final int DUREE_EMPRUNT_JOURS = 21;
        static final BigDecimal PENALITE_PAR_JOUR = new BigDecimal("0.20");
        static final BigDecimal PENALITE_MAX = new BigDecimal("10.00");

        /**
         * Q1. Vérifier qu'un emprunt est autorisé. Lever RegleMetierException avec ces messages,
         * dans cet ordre de priorité :
         *   inactif                      → "R3 · adhérent inactif"
         *   un emprunt en retard         → "R3 · emprunt en retard"
         *   déjà 3 emprunts en cours     → "R1 · 3 emprunts en cours maximum"
         *   aucun exemplaire disponible  → "R4 · aucun exemplaire disponible"
         * Si tout va bien, la méthode ne fait rien.
         */
        void verifierEligibilite(boolean adherentActif, boolean aUnRetard, int empruntsEnCours, int exemplairesDisponibles) {
            // TODO : une suite de if (...) throw new RegleMetierException("...");
            throw new UnsupportedOperationException("À compléter");
        }

        /** Q2. Règle R2 : la date de retour prévue = date d'emprunt + 21 jours. */
        LocalDate dateRetourPrevue(LocalDate dateEmprunt) {
            // TODO : plusDays
            throw new UnsupportedOperationException("À compléter");
        }

        /** Q3. Un emprunt est en retard si on est APRÈS la date de retour prévue (le jour même : pas en retard). */
        boolean estEnRetard(LocalDate dateRetourPrevue, LocalDate aujourdhui) {
            // TODO : isAfter
            throw new UnsupportedOperationException("À compléter");
        }

        /**
         * Q4. Règle R5 : 0,20 € par jour de retard, plafonné à 10 €. Rendu à temps → 0.00.
         * Le résultat a toujours 2 décimales (ex. 1.60, 0.00, 10.00).
         */
        BigDecimal calculerPenalite(LocalDate dateRetourPrevue, LocalDate dateRetourEffective) {
            // TODO : ChronoUnit.DAYS.between, multiply, min(PENALITE_MAX), setScale(2, RoundingMode.HALF_UP)
            throw new UnsupportedOperationException("À compléter");
        }
    }

    // ============================== VÉRIFICATIONS (ne pas modifier) ==============================
    public static void main(String[] args) {
        var p = new PolitiqueEmprunt();
        LocalDate prevu = LocalDate.of(2026, 10, 22);
        System.out.println("Exercice 3 · La politique d'emprunt");

        verifier("Q1 · adhérent actif, 0 emprunt, livre disponible → autorisé", () -> {
            p.verifierEligibilite(true, false, 0, 2);
            return true;
        });
        verifier("Q1 · 2 emprunts en cours → encore autorisé", () -> {
            p.verifierEligibilite(true, false, 2, 1);
            return true;
        });
        verifierException("Q1 · R1 : 3 emprunts en cours → refusé", RegleMetierException.class, "R1",
                () -> p.verifierEligibilite(true, false, 3, 1));
        verifierException("Q1 · R3 : adhérent inactif → refusé", RegleMetierException.class, "inactif",
                () -> p.verifierEligibilite(false, false, 0, 1));
        verifierException("Q1 · R3 : emprunt en retard → refusé", RegleMetierException.class, "retard",
                () -> p.verifierEligibilite(true, true, 0, 1));
        verifierException("Q1 · R4 : aucun exemplaire → refusé", RegleMetierException.class, "R4",
                () -> p.verifierEligibilite(true, false, 0, 0));
        verifierException("Q1 · priorité : inactif ET 3 emprunts → message R3", RegleMetierException.class, "R3",
                () -> p.verifierEligibilite(false, false, 3, 0));
        verifier("Q2 · emprunt le 1er octobre → retour le 22 octobre",
                () -> p.dateRetourPrevue(LocalDate.of(2026, 10, 1)).equals(prevu));
        verifier("Q2 · emprunt le 20 décembre → retour le 10 janvier (changement d'année)",
                () -> p.dateRetourPrevue(LocalDate.of(2026, 12, 20)).equals(LocalDate.of(2027, 1, 10)));
        verifier("Q3 · le jour même → pas en retard", () -> !p.estEnRetard(prevu, prevu));
        verifier("Q3 · le lendemain → en retard", () -> p.estEnRetard(prevu, prevu.plusDays(1)));
        verifier("Q4 · rendu à temps → 0.00", () -> p.calculerPenalite(prevu, prevu.minusDays(3)).toString().equals("0.00"));
        verifier("Q4 · 1 jour de retard → 0.20", () -> p.calculerPenalite(prevu, prevu.plusDays(1)).toString().equals("0.20"));
        verifier("Q4 · 8 jours de retard → 1.60", () -> p.calculerPenalite(prevu, prevu.plusDays(8)).toString().equals("1.60"));
        verifier("Q4 · 50 jours → 10.00 (exactement le plafond)", () -> p.calculerPenalite(prevu, prevu.plusDays(50)).toString().equals("10.00"));
        verifier("Q4 · 90 jours → 10.00 (plafonné)", () -> p.calculerPenalite(prevu, prevu.plusDays(90)).toString().equals("10.00"));
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
