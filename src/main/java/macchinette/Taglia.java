package macchinette;

import java.util.Objects;

/**
 * Rappresenta la categoria dimensionale (taglia) di un prodotto o la capacità fisica di un binario.
 *
 * <p>Le taglie costituiscono un insieme finito e totalmente ordinato che riflette una gerarchia fisica crescente.
 * L'ordinamento naturale è definito dalla sequenza di dichiarazione: <b>S &lt; M &lt; L &lt; XL</b>.
 *
 * <p>Questa enumerazione soddisfa il requisito di rappresentazione testuale standard, coincidendo
 * esattamente con le stringhe "S", "M", "L", "XL" utilizzate in input e output.
 */
public enum Taglia {

    /** Small - Dimensione piccola. Corrisponde alla stringa "S". */
    S,

    /** Medium - Dimensione media. Corrisponde alla stringa "M". */
    M,

    /** Large - Dimensione grande. Corrisponde alla stringa "L". */
    L,

    /** Extra-Large - Dimensione extra grande. Corrisponde alla stringa "XL". */
    XL;

    /**
     * Verifica la compatibilità spaziale (capacità di contenimento) rispetto a un'altra taglia.
     *
     * <p>Determina se questa taglia (intesa come contenitore) è sufficientemente capiente
     * per ospitare un oggetto della taglia specificata. La relazione è inclusiva:
     * una taglia è compatibile con se stessa e con tutte le taglie ordinalmente inferiori.
     *
     * @param altra La taglia dell'oggetto che si intende inserire o confrontare. Non deve essere null.
     * @return {@code true} se questa taglia è maggiore o uguale alla taglia specificata.
     * @throws NullPointerException se il parametro {@code altra} è null.
     */
    public boolean ospita(Taglia altra) {
        Objects.requireNonNull(altra, "La taglia da verificare non può essere null");
        return this.compareTo(altra) >= 0;
    }
}