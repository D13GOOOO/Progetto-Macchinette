package macchinette;

import java.util.Objects;

/**
 * Rappresenta un valore monetario non negativo e immutabile.
 *
 * <p>Questa classe modella un importo economico distinguendo esplicitamente tra unità intere e centesimi.
 * La struttura interna è progettata per garantire automaticamente la normalizzazione dei valori:
 * un importo non potrà mai trovarsi in uno stato inconsistente (come "0 euro e 150 centesimi"),
 * ma verrà sempre convertito nella sua forma canonica corretta (es. "1 euro e 50 centesimi").
 *
 * <p>Essendo immutabile, la classe garantisce che il valore non possa essere alterato accidentalmente
 * dopo la creazione; ogni operazione matematica produce una nuova istanza, assicurando la stabilità
 * dei dati anche in contesti condivisi.
 */
public class Importo implements Comparable<Importo> {

    /** La componente delle unità intere (Euro). */
    private final int unita;

    /** La componente dei centesimi (sempre tra 0 e 99 inclusi). */
    private final int centesimi;

    /*-
     * AF: L'oggetto rappresenta una quantità economica astratta il cui valore totale in centesimi
     * è dato dalla formula: (unita * 100) + centesimi.
     *
     * RI: Affinché l'oggetto sia valido, devono essere rispettate le seguenti condizioni:
     * - unita deve essere un intero non negativo (>= 0).
     * - centesimi deve essere un valore intero normalizzato nell'intervallo [0, 99].
     * - Il valore totale calcolato non deve eccedere Integer.MAX_VALUE.
     */

    /**
     * Crea un nuovo Importo specificando esplicitamente le due componenti.
     *
     * <p>Questo costruttore serve quando i dati provengono da un input utente strutturato o da una lettura
     * testuale che distingue già tra parte intera e frazionaria. Esegue una validazione stretta
     * per impedire la creazione di importi "denormalizzati" (es. 1 unità e 150 centesimi).
     *
     * @param unita     La parte intera dell'importo. Deve essere un intero non negativo.
     * @param centesimi La parte frazionaria dell'importo. Deve essere compreso tra 0 e 99.
     * @throws IllegalArgumentException se {@code unita} è negativa o se {@code centesimi} non è nell'intervallo valido.
     */
    public Importo(int unita, int centesimi) {
        if (unita < 0) {
            throw new IllegalArgumentException("Le unità non possono essere negative: " + unita);
        }
        if (centesimi < 0 || centesimi > 99) {
            throw new IllegalArgumentException("I centesimi devono essere compresi tra 0 e 99: " + centesimi);
        }
        this.unita = unita;
        this.centesimi = centesimi;
    }

    /**
     * Crea un nuovo Importo normalizzando un valore totale grezzo in centesimi.
     *
     * <p>Questo costruttore è fondamentale per le operazioni aritmetiche: prende un risultato grezzo
     * (che potrebbe avere centesimi > 99) e lo converte automaticamente nella forma canonica,
     * calcolando le unità come {@code totalCents / 100} e i centesimi come {@code totalCents % 100}.
     *
     * @param totalCents Il valore totale espresso in centesimi. Deve essere non negativo.
     * @throws IllegalArgumentException se il totale è negativo.
     */
    public Importo(int totalCents) {
        if (totalCents < 0) {
            throw new IllegalArgumentException("L'importo totale non può essere negativo: " + totalCents);
        }
        this.unita = totalCents / 100;
        this.centesimi = totalCents % 100;
    }

    /**
     * Restituisce la parte intera dell'importo.
     *
     * @return Il numero di unità intere.
     */
    public int getUnita() {
        return unita;
    }

    /**
     * Restituisce la parte frazionaria dell'importo.
     *
     * @return Il numero di centesimi (sempre tra 0 e 99).
     */
    public int getCentesimi() {
        return centesimi;
    }

    /**
     * Calcola il valore totale equivalente in centesimi.
     *
     * <p>Questo metodo proietta lo stato complesso dell'oggetto (coppia unita, centesimi) in un singolo
     * valore scalare, restituendo il risultato dell'espressione {@code unita * 100 + centesimi}.
     *
     * @return Il valore totale come {@code int}.
     */
    public int getTotalCents() {
        return unita * 100 + centesimi;
    }

    /**
     * Esegue l'addizione aritmetica tra due importi in modo sicuro.
     *
     * <p>L'operazione non modifica nessuno dei due oggetti originali (immutabilità).
     *
     * @param altro L'importo da aggiungere a questo. Non deve essere null.
     * @return Un nuovo oggetto Importo che rappresenta la somma dei valori.
     * @throws NullPointerException se l'argomento è null.
     * @throws ArithmeticException  se il risultato supera {@code Integer.MAX_VALUE}.
     */
    public Importo somma(Importo altro) {
        Objects.requireNonNull(altro, "L'importo da sommare non può essere null");
        int totale = Math.addExact(this.getTotalCents(), altro.getTotalCents());
        return new Importo(totale);
    }

    /**
     * Esegue la sottrazione aritmetica tra due importi.
     *
     * <p>Poiché il dominio del problema non ammette importi negativi (debito),
     * questa operazione è parziale: è definita solo se il minuendo è maggiore o uguale al sottraendo.
     *
     * @param altro L'importo da sottrarre. Non deve essere null.
     * @return Un nuovo oggetto Importo che rappresenta la differenza.
     * @throws NullPointerException    se l'argomento è null.
     * @throws NegativeResultException se il risultato sarebbe negativo (questo importo è minore di {@code altro}).
     */
    public Importo sottrazione(Importo altro) throws NegativeResultException {
        Objects.requireNonNull(altro, "L'importo da sottrarre non può essere null");
        int totA = this.getTotalCents();
        int totB = altro.getTotalCents();

        if (totA < totB) {
            throw new NegativeResultException();
        }
        return new Importo(totA - totB);
    }

    /**
     * Esegue la moltiplicazione scalare dell'importo.
     *
     * @param n Il fattore intero per cui moltiplicare. Deve essere non negativo.
     * @return Un nuovo oggetto Importo con valore totale moltiplicato per n.
     * @throws IllegalArgumentException se {@code n} è negativo.
     * @throws ArithmeticException      se il prodotto supera {@code Integer.MAX_VALUE}.
     */
    public Importo moltiplicazione(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Il moltiplicatore deve essere non negativo: " + n);
        }
        int prodotto = Math.multiplyExact(this.getTotalCents(), n);
        return new Importo(prodotto);
    }

    /**
     * Esegue la divisione intera dell'importo per un altro importo (calcolo del quoziente).
     *
     * <p>Calcola il quoziente della divisione euclidea tra i valori totali in centesimi dei due oggetti.
     * Il risultato rappresenta il rapporto intero tra le due grandezze economiche.
     *
     * @param altro L'importo divisore. Non deve essere null e deve essere maggiore di zero.
     * @return Il numero intero di volte che il divisore è contenuto nel dividendo.
     * @throws NullPointerException se l'argomento è null.
     * @throws ArithmeticException  se l'importo divisore è zero.
     */
    public int divisione(Importo altro) {
        Objects.requireNonNull(altro, "Il divisore non può essere null");
        int totDivisore = altro.getTotalCents();
        if (totDivisore == 0) {
            throw new ArithmeticException("Divisione per zero non ammessa");
        }
        return this.getTotalCents() / totDivisore;
    }

    /**
     * Confronta questo importo con un altro per stabilire l'ordine naturale.
     *
     * <p>L'ordinamento è basato sul valore economico totale.
     *
     * @param o L'importo da confrontare. Non deve essere null.
     * @return Un intero negativo, zero o positivo se questo oggetto è rispettivamente minore, uguale o maggiore di quello fornito.
     * @throws NullPointerException se l'argomento è null.
     */
    @Override
    public int compareTo(Importo o) {
        Objects.requireNonNull(o, "Impossibile confrontare con null");
        return Integer.compare(this.getTotalCents(), o.getTotalCents());
    }

    /**
     * Verifica l'uguaglianza logica profonda tra due importi.
     *
     * <p>Due oggetti Importo sono considerati uguali se e solo se rappresentano
     * esattamente le stesse componenti (stesse unità e stessi centesimi).
     *
     * @param o L'oggetto da confrontare.
     * @return {@code true} se gli oggetti sono logicamente identici, altrimenti {@code false}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Importo)) return false;
        Importo altro = (Importo) o;
        return this.unita == altro.unita && this.centesimi == altro.centesimi;
    }

    /**
     * Genera un codice hash coerente con il metodo equals.
     *
     * @return L'hash calcolato sulla combinazione di unità e centesimi.
     */
    @Override
    public int hashCode() {
        return Objects.hash(unita, centesimi);
    }

    /**
     * Restituisce una rappresentazione testuale leggibile dell'importo.
     *
     * <p>Formato di output: {@code "[u] unit[s] [c] cent[s]"}.
     * Le parti con valore zero vengono omesse per brevità, a meno che il totale non sia zero.
     *
     * @return La stringa formattata.
     */
    @Override
    public String toString() {
        if (unita == 0 && centesimi == 0) {
            return "0 cents";
        }
        StringBuilder sb = new StringBuilder();
        if (unita > 0) {
            sb.append(unita).append(unita == 1 ? " unit" : " units");
        }
        if (centesimi > 0) {
            if (unita > 0) {
                sb.append(" ");
            }
            sb.append(centesimi).append(centesimi == 1 ? " cent" : " cents");
        }
        return sb.toString();
    }
}