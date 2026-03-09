package macchinette;

/**
 * Eccezione controllata (Checked Exception) che segnala un'incongruenza tra tipologie di prodotti.
 *
 * <p>Questa eccezione viene sollevata per garantire il vincolo di omogeneità dei binari:
 * un binario non vuoto può accettare solo prodotti identici (per nome, prezzo e taglia)
 * a quelli già contenuti. Se si tenta di inserire un prodotto diverso, viene generato questo errore.
 */
public class ItemMismatchException extends DistributoreException {

    /**
     * Identificativo univoco della versione per la serializzazione.
     * Garantisce la compatibilità tra la classe serializzata e quella caricata in memoria.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Costruisce una nuova istanza dell'eccezione.
     *
     * <p>Inizializza l'eccezione impostando il messaggio standard di errore a "item",
     * segnalando al client che il prodotto fornito non corrisponde a quello atteso o presente.
     */
    public ItemMismatchException() {
        super("item");
    }
}