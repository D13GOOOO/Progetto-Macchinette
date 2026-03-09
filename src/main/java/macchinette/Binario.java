package macchinette;

import java.util.Objects;

/**
 * Rappresenta un'unità di stoccaggio fisica (slot o binario) all'interno del distributore automatico.
 *
 * <p>Un binario è modellato come un contenitore sequenziale di tipo <b>FIFO</b> (First-In, First-Out) ,
 * soggetto a vincoli fisici di capacità e dimensione.
 * Ogni binario è "monotematico": può contenere esclusivamente istanze identiche dello stesso prodotto.
 *
 * <p>La gestione dei prodotti segue rigorosamente l'ordine temporale: i nuovi articoli vengono
 * aggiunti in coda allo stoccaggio, mentre l'erogazione preleva sempre l'elemento inserito meno recentemente.
 */
public class Binario {

    /** Il numero massimo di prodotti che questo binario può contenere. */
    private final int capacita;

    /** La dimensione fisica massima accettata dal binario. */
    private final Taglia taglia;

    /** Il tipo di prodotto attualmente stoccato nel binario (null se vuoto). */
    private Prodotto tipoProdotto;

    /** Il numero di unità del prodotto attualmente presenti. */
    private int quantita;

    /*-
     * AF: Un binario rappresenta una quadrupla (C, T, P, Q) dove:
     * - C: Capacità massima.
     * - T: Taglia massima accettata.
     * - P: Il tipo di prodotto contenuto (o null se Q=0).
     * - Q: La quantità attuale di prodotti (0 <= Q <= C).
     *
     * RI: Affinché lo stato sia consistente:
     * - capacita > 0.
     * - taglia != null.
     * - 0 <= quantita <= capacita.
     * - Se quantita > 0, allora tipoProdotto != null e compatibile con la taglia.
     * - Se quantita == 0, tipoProdotto è considerato irrilevante (e mantenuto null per coerenza).
     */

    /**
     * Crea un nuovo binario vuoto con le specifiche fisiche indicate.
     *
     * <p>Il binario viene inizializzato con quantità zero e nessun prodotto associato.
     *
     * @param capacita La capacità massima di stoccaggio (numero di slot). Deve essere un intero positivo.
     * @param taglia   La dimensione massima del prodotto ospitabile. Non deve essere null.
     * @throws IllegalArgumentException se {@code capacita} è minore o uguale a zero.
     * @throws NullPointerException     se {@code taglia} è null.
     */
    public Binario(int capacita, Taglia taglia) {
        if (capacita <= 0) {
            throw new IllegalArgumentException("La capacità del binario deve essere positiva: " + capacita);
        }
        this.capacita = capacita;
        this.taglia = Objects.requireNonNull(taglia, "La taglia del binario non può essere null");
        this.quantita = 0;
        this.tipoProdotto = null;
    }

    /**
     * Restituisce la capacità totale del binario.
     *
     * @return Il numero massimo di prodotti che il binario può contenere.
     */
    public int getCapacita() {
        return capacita;
    }

    /**
     * Restituisce la taglia fisica del binario.
     *
     * @return La costante {@link Taglia} associata a questo binario.
     */
    public Taglia getTaglia() {
        return taglia;
    }

    /**
     * Restituisce il numero di prodotti attualmente presenti nel binario.
     *
     * @return La quantità corrente di prodotti stoccati.
     */
    public int getQuantitaAttuale() {
        return quantita;
    }

    /**
     * Verifica se il binario è vuoto.
     *
     * @return {@code true} se la quantità di prodotti è zero, {@code false} altrimenti.
     */
    public boolean isVuoto() {
        return quantita == 0;
    }

    /**
     * Calcola lo spazio libero residuo nel binario.
     *
     * <p>Il valore è calcolato come differenza tra la capacità totale e la quantità attuale.
     *
     * @return Il numero di prodotti che possono ancora essere inseriti.
     */
    public int spazioDisponibile() {
        return capacita - quantita;
    }

    /**
     * Ispeziona il prossimo prodotto disponibile per l'erogazione senza rimuoverlo.
     *
     * @return Il riferimento al tipo di prodotto contenuto (la testa della coda).
     * @throws BinarioVuotoException se il binario è vuoto e non c'è nessun prodotto da ispezionare.
     */
    public Prodotto peek() throws BinarioVuotoException {
        if (isVuoto()) {
            throw new BinarioVuotoException();
        }
        return tipoProdotto;
    }

    /**
     * Carica una quantità specifica di prodotti nel binario.
     *
     * <p>L'operazione è atomica: se l'inserimento non è possibile per qualsiasi motivo (capacità insufficiente,
     * taglia incompatibile o prodotto diverso da quello presente), lo stato del binario rimane invariato.
     * Se il binario è vuoto, il prodotto specificato ne diventa il tipo esclusivo.
     *
     * @param p        Il prodotto da inserire. Non deve essere null.
     * @param quantitaDaAggiungere Il numero di unità da aggiungere. Deve essere un intero non negativo.
     * @throws NullPointerException      se {@code p} è null.
     * @throws IllegalArgumentException  se {@code quantitaDaAggiungere} è negativa.
     * @throws SizeMismatchException     se la taglia del prodotto è maggiore della taglia del binario.
     * @throws ItemMismatchException     se il binario contiene già prodotti e {@code p} è diverso dal tipo presente.
     * @throws CapacityExceededException se lo spazio disponibile non è sufficiente per la quantità richiesta.
     */
    public void carica(Prodotto p, int quantitaDaAggiungere) throws DistributoreException {
        Objects.requireNonNull(p, "Il prodotto da caricare non può essere null");
        if (quantitaDaAggiungere < 0) {
            throw new IllegalArgumentException("La quantità da caricare non può essere negativa");
        }
        if (quantitaDaAggiungere == 0) return;

        if (!this.taglia.ospita(p.getTaglia())) {
            throw new SizeMismatchException();
        }

        if (this.quantita > 0 && !p.equals(this.tipoProdotto)) {
            throw new ItemMismatchException();
        }

        if (this.quantita + quantitaDaAggiungere > capacita) {
            throw new CapacityExceededException();
        }

        this.tipoProdotto = p;
        this.quantita += quantitaDaAggiungere;
    }

    /**
     * Preleva (eroga) un singolo prodotto dal binario.
     *
     * <p>Decrementa il contatore di unità presenti. Se l'operazione porta la quantità a zero,
     * il riferimento al tipo di prodotto viene resettato a null per permettere il caricamento
     * futuro di prodotti di tipo diverso.
     *
     * @return Il prodotto rimosso dalla testa della coda.
     * @throws BinarioVuotoException se il binario è vuoto al momento della chiamata.
     */
    public Prodotto dispensa() throws BinarioVuotoException {
        if (isVuoto()) {
            throw new BinarioVuotoException();
        }

        Prodotto p = this.tipoProdotto;
        this.quantita--;

        if (this.quantita == 0) {
            this.tipoProdotto = null;
        }

        return p;
    }

    /**
     * Restituisce la rappresentazione testuale dello stato interno del binario.
     *
     * <p>Formato: {@code <ProdottoNext, TagliaBin, Quantita, Capacita>}
     * Se il binario è vuoto, il campo ProdottoNext sarà "-".
     *
     * @return Stringa descrittiva del binario.
     */
    @Override
    public String toString() {
        if (isVuoto()) {
            return "<-, " + taglia + ", 0, " + capacita + ">";
        }
        return "<" + tipoProdotto + ", " + taglia + ", " + quantita + ", " + capacita + ">";
    }
}