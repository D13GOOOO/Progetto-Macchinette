package macchinette;

import java.util.Objects;

/**
 * Implementazione della strategia di calcolo del resto basata sulla minimizzazione del numero di monete.
 *
 * <p>Questa classe realizza l'algoritmo <b>Greedy</b> (ingordo) standard. L'approccio consiste nel
 * tentare di coprire il debito residuo utilizzando, ad ogni passo, la moneta di taglio
 * più grande disponibile nel distributore.
 */
public class StrategiaMinima implements StrategiaResto {

    /*-
     * AF: Rappresenta una funzione pura f(D, C) -> R, dove:
     * - D è il debito (Importo).
     * - C è la disponibilità di cassa (Aggregato).
     * - R è il resto calcolato (Aggregato).
     * La funzione applica una logica "Largest Value First" per selezionare gli elementi di R.
     *
     * RI: La classe è priva di stato (stateless). L'invariante è sempre soddisfatto.
     */

    /**
     * Crea una nuova istanza della strategia.
     *
     * <p>Inizializza una nuova istanza della strategia, pronta per eseguire calcoli.
     */
    public StrategiaMinima() {
    }

    /**
     * Calcola l'insieme di monete da restituire minimizzando la cardinalità dell'insieme risultante.
     *
     * <p>L'algoritmo itera sui tagli di moneta in ordine decrescente di valore. Per ogni taglio,
     * preleva la massima quantità possibile compatibilmente con la disponibilità in cassa
     * e con il debito residuo.
     *
     * <p>Questo metodo è una funzione pura: non modifica lo stato dell'aggregato {@code cassa} passato in input.
     *
     * @param restoDovuto L'importo totale che deve essere restituito. Non deve essere null.
     * @param cassa       L'aggregato contenente le monete fisicamente disponibili. Non deve essere null.
     * @return Un nuovo oggetto {@link Aggregato} contenente le monete selezionate.
     * @throws NullPointerException       se uno dei parametri è null.
     * @throws ChangeNotPossibleException se l'algoritmo greedy non riesce a coprire l'intero importo
     * con le monete disponibili (ad esempio, se mancano i tagli piccoli necessari per il "fine tuning").
     */
    @Override
    public Aggregato calcolaResto(Importo restoDovuto, Aggregato cassa) throws ChangeNotPossibleException {
        Objects.requireNonNull(restoDovuto, "L'importo del resto non può essere null");
        Objects.requireNonNull(cassa, "L'aggregato cassa non può essere null");

        int rimanente = restoDovuto.getTotalCents();
        Aggregato resto = new Aggregato();
        Moneta[] tagli = Moneta.values();

        for (int i = tagli.length - 1; i >= 0; i--) {
            if (rimanente == 0) break;

            Moneta m = tagli[i];
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