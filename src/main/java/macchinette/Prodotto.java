package macchinette;

import java.util.Objects;

/**
 * Rappresenta un articolo commerciale gestito dal distributore automatico.
 *
 * <p>Questa classe è implementata come un <b>Value Object immutabile</b>: una volta istanziato,
 * lo stato interno (nome, prezzo, taglia) non può essere modificato. Questa caratteristica
 * garantisce la thread-safety intrinseca e la coerenza dell'inventario nel tempo: un prodotto
 * non può cambiare nome o prezzo mentre si trova in un binario.
 */
public class Prodotto implements Comparable<Prodotto> {

    /** Il nome commerciale o descrittivo del prodotto. */
    private final String nome;

    /** Il valore monetario richiesto per l'acquisto del prodotto. */
    private final Importo prezzo;

    /** La classificazione dimensionale del prodotto. */
    private final Taglia taglia;

    /*-
     * AF: Un oggetto Prodotto rappresenta concettualmente una tripla (N, P, T) che identifica un bene, dove:
     * - N: Identificativo testuale del prodotto (Nome).
     * - P: Valore economico di scambio (Prezzo).
     * - T: Ingombro fisico spaziale (Taglia).
     *
     * RI: Affinché l'oggetto sia consistente, devono essere sempre vere le seguenti condizioni:
     * - nome non deve essere null e deve contenere almeno un carattere visibile (non solo spazi).
     * - prezzo deve essere un riferimento valido (non null) a un oggetto Importo.
     * - taglia deve essere un riferimento valido (non null) a una costante Taglia.
     */

    /**
     * Crea un nuovo Prodotto validando i dati in ingresso.
     *
     * <p>Inizializza l'oggetto immutabile assegnando i campi specificati e verificando
     * che soddisfino l'invariante di rappresentazione.
     *
     * @param nome   Il nome del prodotto. Non deve essere null né composto solo da spazi.
     * @param prezzo Il prezzo di vendita. Non deve essere null.
     * @param taglia La dimensione fisica. Non deve essere null.
     * @throws NullPointerException     se uno qualsiasi dei parametri è null.
     * @throws IllegalArgumentException se {@code nome} è vuoto o composto solo da spazi bianchi.
     */
    public Prodotto(String nome, Importo prezzo, Taglia taglia) {
        Objects.requireNonNull(nome, "Il nome del prodotto non può essere null");
        if (nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome del prodotto non può essere vuoto");
        }
        this.nome = nome;
        this.prezzo = Objects.requireNonNull(prezzo, "Il prezzo non può essere null");
        this.taglia = Objects.requireNonNull(taglia, "La taglia non può essere null");
    }

    /**
     * Restituisce il nome del prodotto.
     *
     * @return La stringa identificativa.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce il prezzo del prodotto.
     *
     * @return L'oggetto {@link Importo} immutabile.
     */
    public Importo getPrezzo() {
        return prezzo;
    }

    /**
     * Restituisce la taglia del prodotto.
     *
     * @return La costante enumerativa {@link Taglia}.
     */
    public Taglia getTaglia() {
        return taglia;
    }

    /**
     * Definisce l'ordinamento naturale tra prodotti.
     *
     * <p>La precedenza dei criteri di ordinamento è rigorosamente definita come segue:
     * <ol>
     * <li><b>Taglia:</b> Ordine crescente (S &lt; M &lt; L &lt; XL).</li>
     * <li><b>Nome:</b> Ordine lessicografico (alfabetico, case-sensitive).</li>
     * <li><b>Prezzo:</b> Ordine crescente di valore.</li>
     * </ol>
     *
     * @param o Il prodotto da confrontare. Non deve essere null.
     * @return Un intero negativo, zero o positivo se {@code this} è rispettivamente minore, uguale o maggiore di {@code o}.
     * @throws NullPointerException se {@code o} è null.
     */
    @Override
    public int compareTo(Prodotto o) {
        Objects.requireNonNull(o, "Impossibile confrontare con un prodotto null");

        int cmpTaglia = this.taglia.compareTo(o.taglia);
        if (cmpTaglia != 0) return cmpTaglia;

        int cmpNome = this.nome.compareTo(o.nome);
        if (cmpNome != 0) return cmpNome;

        return this.prezzo.compareTo(o.prezzo);
    }

    /**
     * Verifica l'uguaglianza logica profonda tra due prodotti.
     *
     * <p>Due prodotti sono considerati uguali se e solo se coincidono esattamente
     * in tutte le loro componenti: taglia, nome e prezzo.
     *
     * @param o L'oggetto con cui confrontare.
     * @return {@code true} se gli oggetti rappresentano lo stesso prodotto, {@code false} altrimenti.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prodotto)) return false;
        Prodotto altro = (Prodotto) o;
        return taglia == altro.taglia &&
                nome.equals(altro.nome) &&
                prezzo.equals(altro.prezzo);
    }

    /**
     * Calcola l'hash code del prodotto.
     *
     * @return Un intero calcolato combinando gli hash di taglia, nome e prezzo.
     */
    @Override
    public int hashCode() {
        return Objects.hash(taglia, nome, prezzo);
    }

    /**
     * Restituisce la rappresentazione testuale del prodotto.
     *
     * <p>Il formato di output è strutturato come segue: {@code <Nome, Prezzo, Taglia>}.
     *
     * @return La stringa formattata.
     */
    @Override
    public String toString() {
        return "<" + nome + ", " + prezzo + ", " + taglia + ">";
    }
}