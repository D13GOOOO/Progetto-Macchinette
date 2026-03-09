package macchinette;

/**
 * Eccezione controllata (Checked Exception) che segnala un'incompatibilità dimensionale tra prodotto e contenitore.
 *
 * <p>Questa eccezione viene sollevata durante le operazioni di caricamento quando la taglia fisica del prodotto
 * che si tenta di inserire eccede la capacità dimensionale massima del binario di destinazione
 * (es. tentativo di inserire un prodotto L in un binario S).
 */
public class SizeMismatchException extends DistributoreException {

    /**
     * Identificativo univoco della versione per la serializzazione.
     * Garantisce la compatibilità tra la classe serializzata e quella caricata in memoria.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Costruisce una nuova istanza dell'eccezione.
     *
     * <p>Inizializza l'eccezione impostando il messaggio standard di errore a "size",
     * per notificare al client la mancata corrispondenza dimensionale.
     */
    public SizeMismatchException() {
        super("size");
    }
}