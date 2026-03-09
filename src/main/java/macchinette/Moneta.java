package macchinette;

/**
 * Rappresenta i tagli di moneta fisicamente accettati dal distributore.
 *
 * <p>Mentre la classe {@link Importo} gestisce concetti matematici astratti (dove 0,03€ è un valore valido),
 * questa enumerazione definisce i <b>vincoli hardware</b> del sistema: il distributore può accettare ed
 * erogare solo specifiche monete reali (da 1 centesimo a 2 Euro).
 *
 * <p>L'ordine in cui sono elencate le monete non è casuale, ma rispetta il valore crescente.
 * Questa caratteristica è sfruttata dagli algoritmi di resto (ad esempio, per tentare di usare
 * le monete più grandi prima di quelle piccole).
 */
public enum Moneta {

    /** Taglio da 1 centesimo. */
    C1(1),
    /** Taglio da 2 centesimi. */
    C2(2),
    /** Taglio da 5 centesimi. */
    C5(5),
    /** Taglio da 10 centesimi. */
    C10(10),
    /** Taglio da 20 centesimi. */
    C20(20),
    /** Taglio da 50 centesimi. */
    C50(50),
    /** Taglio da 1 unità (es. 1 Euro). */
    U1(100),
    /** Taglio da 2 unità (es. 2 Euro). */
    U2(200);

    /** Il valore economico intrinseco della moneta. */
    private final Importo valore;

    /*-
     * AF: Ogni costante dell'enumerazione (es. C50) rappresenta l'oggetto fisico "Moneta" e mappa
     * direttamente al suo valore economico reale incapsulato in un oggetto Importo.
     *
     * RI: Affinché la definizione sia coerente:
     * - Ogni moneta deve avere un valore associato non nullo.
     * - Il valore deve essere strettamente positivo (una moneta da 0 o -1 euro non esiste fisicamente).
     */

    /**
     * Inizializza una costante enumerativa associandole il valore economico specificato.
     *
     * <p>Costruisce la moneta creando e associando in modo immutabile il corrispondente oggetto {@link Importo}.
     *
     * @param cents Il valore del taglio espresso in centesimi. Deve essere strettamente positivo.
     * @throws IllegalArgumentException se {@code cents} è minore o uguale a zero.
     */
    Moneta(int cents) {
        if (cents <= 0) {
            throw new IllegalArgumentException("Il valore facciale della moneta deve essere positivo: " + cents);
        }
        this.valore = new Importo(cents);
    }

    /**
     * Restituisce il valore economico associato al taglio.
     *
     * @return L'oggetto {@link Importo} che rappresenta il valore della moneta.
     */
    public Importo getValore() {
        return valore;
    }
}