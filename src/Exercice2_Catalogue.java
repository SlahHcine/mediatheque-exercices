import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

/**
 * EXERCICE 2 · Le catalogue (Streams, Optional, records)
 *
 * Contexte : la page d'accueil et la recherche de la médiathèque interrogent le catalogue.
 * Toutes les méthodes s'écrivent avec un Stream, sans boucle for.
 *
 * Lancer : java Exercice2_Catalogue.java
 */
public class Exercice2_Catalogue {

    /** Un livre du catalogue (record : immuable, accesseurs titre(), auteur()…). */
    record Livre(String isbn, String titre, String auteur, int annee, int exemplaires, int disponibles) {
        boolean estDisponible() { return disponibles > 0; }
    }

    static class Catalogue {
        private final List<Livre> livres;

        Catalogue(List<Livre> livres) {
            this.livres = List.copyOf(livres);
        }

        /** Q1. Les livres qui ont au moins un exemplaire disponible, triés par titre. */
        List<Livre> livresDisponibles() {
            // TODO : filter(Livre::estDisponible), sorted(Comparator.comparing(...)), toList()
            throw new UnsupportedOperationException("À compléter");
        }

        /** Q2. Les TITRES des livres d'un auteur, sans tenir compte des majuscules ("asimov" = "Asimov"). */
        List<String> titresDe(String auteur) {
            // TODO : filter avec equalsIgnoreCase, puis map(Livre::titre)
            throw new UnsupportedOperationException("À compléter");
        }

        /** Q3. Chercher un livre par ISBN. Absent → Optional vide (jamais null). */
        Optional<Livre> trouverParIsbn(String isbn) {
            // TODO : filter sur l'isbn puis findFirst()
            throw new UnsupportedOperationException("À compléter");
        }

        /** Q4. Le nombre de livres par auteur, auteurs triés par ordre alphabétique. */
        Map<String, Long> nombreDeLivresParAuteur() {
            // TODO : collect(Collectors.groupingBy(Livre::auteur, TreeMap::new, Collectors.counting()))
            throw new UnsupportedOperationException("À compléter");
        }

        /** Q5. Les titres des n livres les plus récents (du plus récent au plus ancien). */
        List<String> lesPlusRecents(int n) {
            // TODO : sorted(Comparator.comparing(Livre::annee).reversed()), limit(n), map
            throw new UnsupportedOperationException("À compléter");
        }

        /** Q6. Le nombre total d'exemplaires disponibles dans toute la médiathèque. */
        int totalDisponibles() {
            // TODO : mapToInt(Livre::disponibles).sum()
            throw new UnsupportedOperationException("À compléter");
        }
    }

    // ============================== VÉRIFICATIONS (ne pas modifier) ==============================
    public static void main(String[] args) {
        var catalogue = new Catalogue(List.of(
                new Livre("978-01", "Fondation", "Asimov", 1951, 3, 0),
                new Livre("978-02", "Les Robots", "Asimov", 1950, 2, 1),
                new Livre("978-03", "Dune", "Herbert", 1965, 4, 2),
                new Livre("978-04", "1984", "Orwell", 1949, 2, 2),
                new Livre("978-05", "La Ferme des animaux", "Orwell", 1945, 1, 0),
                new Livre("978-06", "Hypérion", "Simmons", 1989, 2, 1)));
        System.out.println("Exercice 2 · Le catalogue");

        verifier("Q1 · 4 livres disponibles", () -> catalogue.livresDisponibles().size() == 4);
        verifier("Q1 · triés par titre : 1984 en premier, Les Robots en dernier", () -> {
            var l = catalogue.livresDisponibles();
            return l.get(0).titre().equals("1984") && l.get(l.size() - 1).titre().equals("Les Robots");
        });
        verifier("Q2 · titres de « asimov » (minuscules acceptées)",
                () -> catalogue.titresDe("asimov").equals(List.of("Fondation", "Les Robots")));
        verifier("Q2 · auteur inconnu → liste vide", () -> catalogue.titresDe("Tolkien").isEmpty());
        verifier("Q3 · ISBN 978-03 → Dune",
                () -> catalogue.trouverParIsbn("978-03").map(Livre::titre).orElse("").equals("Dune"));
        verifier("Q3 · ISBN inconnu → Optional vide", () -> catalogue.trouverParIsbn("000").isEmpty());
        verifier("Q4 · {Asimov=2, Herbert=1, Orwell=2, Simmons=1}",
                () -> catalogue.nombreDeLivresParAuteur().toString().equals("{Asimov=2, Herbert=1, Orwell=2, Simmons=1}"));
        verifier("Q5 · les 2 plus récents : Hypérion puis Dune",
                () -> catalogue.lesPlusRecents(2).equals(List.of("Hypérion", "Dune")));
        verifier("Q6 · 6 exemplaires disponibles au total", () -> catalogue.totalDisponibles() == 6);
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
