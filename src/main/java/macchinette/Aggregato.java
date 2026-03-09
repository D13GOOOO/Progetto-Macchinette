package macchinette;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Rappresenta un multi-insieme (Multiset) mutabile di monete.
 *
 * <p>Questa classe realizza l'astrazione di un "mucchio" di denaro in cui l'identità della singola moneta non è rilevante (non conta l'ordine), ma è fondamentale la cardinalità (quantità) di ciascun taglio.
 * È progettata per modellare entità del dominio che cambiano nel tempo, come il fondo cassa
 * del distributore, il credito accumulato dall'utente o l'insieme delle monete di resto.
 */
public class Aggregato implements Iterable<Moneta> {

    /** Struttura dati interna associativa (Mappa: Tipo di Moneta -> Quantità posseduta). */
    private final Map<Moneta, Integer> monete;

    /*-
     * AF: Lo stato concreto dell'oggetto mappa una funzione parziale M: Moneta -> Integer.
     * L'aggregato astratto rappresenta l'insieme di coppie (m, q) dove:
     * - m è un taglio di moneta definito in Moneta.
     * - q = M(m) è la quantità intera strettamente positiva di quella moneta.
     * Il valore economico totale V è derivato dalla sommatoria:
     * V = Sum(M(m) * Valore(m)) per ogni m nel dominio di M.
     *
     * RI: Affinché l'istanza sia valida, devono essere rispettate le seguenti condizioni:
     * - Il riferimento monete non è null.
     * - La mappa non contiene chiavi null.
     * - La mappa non contiene valori null o minori/uguali a zero.
     * - Invariante di compattezza: se la quantità di una moneta scende a zero, la chiave deve essere
     * rimossa dalla mappa. Non esistono entry con valore 0.
     */

    /**
     * Crea un nuovo aggregato vuoto.
     *
     * <p>Inizializza un nuovo multi-insieme con cardinalità zero per ogni moneta e valore totale nullo.
     */
    public Aggregato() {
        this.monete = new EnumMap<>(Moneta.class);
    }

    /**
     * Aggiunge una specifica quantità di una moneta all'aggregato.
     *
     * <p>Se la quantità specificata è positiva, aggiorna lo stato incrementando il conteggio
     * associato alla moneta. Se la moneta non era presente, viene inizializzata con la quantità data.
     * Se la quantità è zero, lo stato dell'aggregato non viene modificato.
     *
     * @param m        La moneta da aggiungere. Non deve essere null.
     * @param quantita Il numero di pezzi da aggiungere. Deve essere un intero non negativo.
     * @throws NullPointerException se la moneta {@code m} è null.
     */
    public void aggiungiMoneta(Moneta m, int quantita) {
        Objects.requireNonNull(m, "La moneta non può essere null");
        if (quantita <= 0) return;
        monete.merge(m, quantita, Integer::sum);
    }

    /**
     * Unisce il contenuto di un altro aggregato a questo (Unione di Multiset).
     *
     * <p>Questa operazione è additiva: per ogni coppia (moneta, quantità) presente in {@code altro},
     * esegue un'aggiunta al corrente aggregato. L'aggregato sorgente rimane invariato.
     *
     * @param altro L'aggregato sorgente da cui prelevare le monete. Non deve essere null.
     * @throws NullPointerException se l'aggregato {@code altro} è null.
     */
    public void aggiungi(Aggregato altro) {
        Objects.requireNonNull(altro, "L'aggregato da aggiungere non può essere null");
        for (Map.Entry<Moneta, Integer> entry : altro.monete.entrySet()) {
            this.aggiungiMoneta(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Restituisce la quantità disponibile per un determinato taglio di moneta.
     *
     * @param m La moneta di cui verificare la disponibilità. Non deve essere null.
     * @return Il numero di occorrenze della moneta (restituisce 0 se la moneta non è presente).
     * @throws NullPointerException se {@code m} è null.
     */
    public int getQuantita(Moneta m) {
        Objects.requireNonNull(m, "La moneta richiesta non può essere null");
        return monete.getOrDefault(m, 0);
    }

    /**
     * Calcola il valore economico totale rappresentato dall'aggregato.
     *
     * <p>Il risultato è ottenuto dalla sommatoria pesata di tutte le monete contenute.
     * L'implementazione utilizza metodi di aritmetica esatta ({@code Math.multiplyExact}, {@code Math.addExact})
     * per garantire che eventuali overflow degli interi vengano rilevati immediatamente lanciando un'eccezione,
     * invece di produrre risultati errati silenziosi.
     *
     * @return Un nuovo oggetto {@link Importo} immutabile che rappresenta la somma totale.
     */
    public Importo getValoreTotale() {
        int totaleCents = 0;
        for (Map.Entry<Moneta, Integer> entry : monete.entrySet()) {
            int valoreMoneta = entry.getKey().getValore().getTotalCents();
            int quantita = entry.getValue();
            int parziale = Math.multiplyExact(valoreMoneta, quantita);
            totaleCents = Math.addExact(totaleCents, parziale);
        }
        return new Importo(totaleCents);
    }

    /**
     * Rimuove una singola unità di una specifica moneta.
     *
     * <p>Decrementa di 1 la quantità associata alla moneta indicata.
     * Se la quantità residua scende a zero, la moneta viene rimossa dalla struttura dati interna.
     *
     * @param m La moneta da rimuovere. Non deve essere null.
     * @throws NullPointerException  se {@code m} è null.
     * @throws MissingCoinsException se la moneta non è presente nell'aggregato (quantità 0).
     */
    public void rimuoviMoneta(Moneta m) throws MissingCoinsException {
        Objects.requireNonNull(m, "La moneta da rimuovere non può essere null");
        Integer quantita = monete.get(m);
        if (quantita == null) {
            throw new MissingCoinsException();
        }
        if (quantita == 1) {
            monete.remove(m);
        } else {
            monete.put(m, quantita - 1);
        }
    }

    /**
     * Sottrae un intero aggregato da questo (Differenza di Multiset).
     *
     * <p>L'operazione implementa una logica strettamente <b>transazionale</b> per garantire l'integrità
     * del fondo cassa. Il processo avviene in tre fasi sequenziali:
     * <ol>
     * <li><b>Verifica Globale (Fail-Fast):</b> Controlla se il valore totale posseduto è sufficiente a coprire la richiesta.</li>
     * <li><b>Verifica Puntuale (Composizione):</b> Controlla se esiste la disponibilità fisica per ogni singolo taglio richiesto.</li>
     * <li><b>Commit:</b> Solo se entrambe le verifiche hanno successo, le modifiche vengono applicate rimuovendo le monete.</li>
     * </ol>
     *
     * @param altro L'aggregato contenente le monete da rimuovere (il sottraendo). Non deve essere null.
     * @throws NullPointerException  se {@code altro} è null.
     * @throws MissingValueException se il valore totale di questo aggregato è inferiore al valore totale di {@code altro}.
     * @throws MissingCoinsException se, pur essendoci valore sufficiente, questo aggregato non contiene
     * esattamente le monete richieste per coprire il sottraendo.
     */
    public void rimuovi(Aggregato altro) throws MissingValueException, MissingCoinsException {
        Objects.requireNonNull(altro, "L'aggregato da rimuovere non può essere null");

        if (this.getValoreTotale().compareTo(altro.getValoreTotale()) < 0) {
            throw new MissingValueException();
        }

        for (Map.Entry<Moneta, Integer> entry : altro.monete.entrySet()) {
            Moneta m = entry.getKey();
            int quantitaRichiesta = entry.getValue();
            int quantitaDisponibile = this.getQuantita(m);

            if (quantitaDisponibile < quantitaRichiesta) {
                throw new MissingCoinsException();
            }
        }

        for (Map.Entry<Moneta, Integer> entry : altro.monete.entrySet()) {
            Moneta m = entry.getKey();
            int quantitaDaRimuovere = entry.getValue();
            int nuovaQuantita = this.getQuantita(m) - quantitaDaRimuovere;

            if (nuovaQuantita == 0) {
                this.monete.remove(m);
            } else {
                this.monete.put(m, nuovaQuantita);
            }
        }
    }

    /**
     * Restituisce un iteratore sui tipi di monete presenti nell'aggregato.
     *
     * <p>L'ordine di iterazione è garantito essere l'ordine naturale definito nell'Enum {@link Moneta} (crescente per valore).
     *
     * @return Un iteratore che permette di scorrere le chiavi (Monete) presenti nella mappa.
     */
    @Override
    public Iterator<Moneta> iterator() {
        return monete.keySet().iterator();
    }

    /**
     * Restituisce la rappresentazione testuale dell'aggregato per scopi di debug o logging.
     *
     * <p>Formato di output: {@code <Quantità x Valore, ...>}
     *
     * @return Una stringa che elenca il contenuto dell'aggregato.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("<");
        int count = 0;
        for (Map.Entry<Moneta, Integer> entry : monete.entrySet()) {
            if (count > 0) sb.append(", ");
            sb.append(entry.getValue()).append(" x ").append(entry.getKey().getValore());
            count++;
        }
        sb.append(">");
        return sb.toString();
    }
}