package macchinette;

import java.util.Objects;

/**
 * Implementazione di una strategia di calcolo del resto ibrida.
 *
 * <p>Questa classe realizza un algoritmo composito che tenta di bilanciare l'ottimizzazione del magazzino
 * con la garanzia di erogazione. La logica si discosta dall'approccio puramente "Greedy" introducendo
 * una fase preliminare euristica mirata allo smaltimento di specifici tagli medi (50 e 20 centesimi).
 */
public class StrategiaMista implements StrategiaResto {

    /*-
     * AF: La strategia è modellata come una funzione composita f(x) = g(h(x)), dove:
     * - h(x) è la sotto-funzione prioritaria che tenta di coprire il debito utilizzando esclusivamente
     * le monete da 50 e 20 centesimi (tagli tattici per evitare l'esaurimento rapido di unità o spiccioli).
     * - g(y) è la sotto-funzione di completamento (fallback) che copre il debito residuo y
     * utilizzando un algoritmo Greedy decrescente (Largest First) su tutte le monete rimanenti.
     *
     * RI: La classe è priva di stato interno mutabile (stateless). L'invariante è banalmente vero.
     */

    /**
     * Crea una nuova istanza della strategia mista.
     *
     * <p>Inizializza l'oggetto rendendolo pronto per eseguire i calcoli.
     */
    public StrategiaMista() {
    }

    /**
     * Calcola l'insieme di monete necessario per raggiungere l'importo target, applicando una logica a due stadi.
     *
     * <p>L'algoritmo procede sequenzialmente:
     * <ol>
     * <li><b>Fase Euristica:</b> Tenta di massimizzare l'uso di monete da 50 e 20 centesimi.</li>
     * <li><b>Fase di Completamento:</b> Se permane un debito residuo, procede con una selezione standard
     * dei tagli più grandi disponibili (Greedy) per coprire la differenza.</li>
     * </ol>
     *
     * <p>Questo metodo è una funzione pura: non modifica lo stato dell'aggregato {@code cassa} passato in input.
     *
     * @param restoDovuto L'importo monetario esatto da restituire. Non deve essere null.
     * @param cassa       L'aggregato rappresentante la disponibilità fisica attuale delle monete. Non deve essere null.
     * @return Un nuovo oggetto {@link Aggregato} contenente le monete selezionate.
     * @throws NullPointerException       se uno dei parametri è null.
     * @throws ChangeNotPossibleException se la combinazione delle due fasi non è in grado di coprire
     * l'intero importo richiesto con le disponibilità attuali.
     */
    @Override
    public Aggregato calcolaResto(Importo restoDovuto, Aggregato cassa) throws ChangeNotPossibleException {
        Objects.requireNonNull(restoDovuto, "L'importo del resto non può essere null");
        Objects.requireNonNull(cassa, "L'aggregato cassa non può essere null");

        int rimanente = restoDovuto.getTotalCents();
        Aggregato resto = new Aggregato();

        Moneta[] prioritarie = {Moneta.C50, Moneta.C20};
        for (Moneta m : prioritarie) {
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
            Moneta[] tuttiITagli = Moneta.values();
            for (int i = tuttiITagli.length - 1; i >= 0; i--) {
                if (rimanente == 0) break;

                Moneta m = tuttiITagli[i];
                int val = m.getValore().getTotalCents();

                int qtyTotale = cassa.getQuantita(m);
                int qtyGiaImpegnata = resto.getQuantita(m);
                int qtyUtilizzabile = qtyTotale - qtyGiaImpegnata;

                if (qtyUtilizzabile > 0) {
                    int numNecessarie = rimanente / val;
                    int daPrendere = Math.min(numNecessarie, qtyUtilizzabile);

                    if (daPrendere > 0) {
                        resto.aggiungiMoneta(m, daPrendere);
                        rimanente -= (daPrendere * val);
                    }
                }
            }
        }

        if (rimanente > 0) {
            throw new ChangeNotPossibleException();
        }

        return resto;
    }
}