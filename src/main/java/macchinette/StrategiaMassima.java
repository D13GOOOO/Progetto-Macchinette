package macchinette;

import java.util.Objects;

/**
 * Implementazione della strategia di calcolo del resto mirata alla massimizzazione del numero di monete erogate.
 * <p>Questa classe realizza un algoritmo "Smallest Value First" (o Greedy Inverso). L'approccio consiste nel
 * tentare di coprire il debito residuo utilizzando prioritariamente le monete di taglio più piccolo
 * disponibile. Questo comportamento è utile per "svuotare" il distributore dagli spiccioli accumulati,
 * preferendo restituire molte monete di piccolo taglio piuttosto che poche di grande valore.
 */
public class StrategiaMassima implements StrategiaResto {

    /*-
     * AF: Rappresenta una funzione pura f(D, C) -> R, dove:
     * - D è il debito (Importo).
     * - C è la disponibilità di cassa (Aggregato).
     * - R è il resto calcolato (Aggregato).
     * La funzione applica una logica di selezione che privilegia i tagli con valore ordinale minore.
     *
     * RI: La classe è priva di stato (stateless). L'invariante è sempre soddisfatto.
     */

    /**
     * Crea una nuova istanza della strategia.
     *
     * <p>Inizializza l'oggetto rendendolo pronto per eseguire i calcoli.
     */
    public StrategiaMassima() {
    }

    /**
     * Calcola l'insieme di monete da restituire massimizzando la cardinalità dell'insieme risultante.
     *
     * <p>L'algoritmo itera sui tagli di moneta in ordine crescente di valore (ordine naturale, da 1 centesimo a 2 Euro).
     * Per ogni taglio, preleva la massima quantità possibile compatibilmente con la disponibilità in cassa
     * e con il debito residuo, saturando i tagli minori prima di passare ai maggiori.
     *
     * <p>Questo metodo è una funzione pura: non modifica lo stato dell'aggregato {@code cassa} passato in input.
     *
     * @param restoDovuto L'importo totale che deve essere restituito. Non deve essere null.
     * @param cassa       L'aggregato contenente le monete fisicamente disponibili. Non deve essere null.
     * @return Un nuovo oggetto {@link Aggregato} contenente le monete selezionate per il resto.
     * @throws NullPointerException       se uno dei parametri è null.
     * @throws ChangeNotPossibleException se l'algoritmo non riesce a coprire l'intero importo
     * con le monete disponibili seguendo rigorosamente l'ordine crescente.
     */
    @Override
    public Aggregato calcolaResto(Importo restoDovuto, Aggregato cassa) throws ChangeNotPossibleException {
        Objects.requireNonNull(restoDovuto, "L'importo del resto non può essere null");
        Objects.requireNonNull(cassa, "L'aggregato cassa non può essere null");

        int rimanente = restoDovuto.getTotalCents();
        Aggregato resto = new Aggregato();

        for (Moneta m : Moneta.values()) {
            if (rimanente == 0) break;

            int val = m.getValore().getTotalCents();
            int qtyDisponibile = cassa.getQuantita(m);

            if (qtyDisponibile > 0) {
                int numNecessarie = rimanente / val;
                int daPrendere = Math.min(numNecessarie, qtyDisponibile);

                if (daPrendere > 0) {
                    resto.aggiungiMoneta(m, daPrendere);
                    rimanente -= (daPrendere * val);
                }
            }
        }

        if (rimanente > 0) {
            throw new ChangeNotPossibleException();
        }

        return resto;
    }
}