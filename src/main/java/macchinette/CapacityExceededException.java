package macchinette;

/**
 * Eccezione controllata (Checked Exception) che segnala il superamento della capacità fisica di stoccaggio.
 *
 * <p>Questa eccezione viene sollevata quando un'operazione di caricamento (es. inserimento di prodotti in un binario)
 * tenta di superare il limite massimo di oggetti contenibili definito per quella specifica unità di stoccaggio.
 */
public class CapacityExceededException extends DistributoreException {

    /**
     * Identificativo univoco della versione per la serializzazione.
     * Garantisce la compatibilità tra la classe serializzata e quella caricata in memoria.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Costruisce una nuova istanza dell'eccezione.
     *
     * <p>Inizializza l'eccezione impostando il messaggio standard di errore a "capacity",
     * per notificare al client che lo spazio fisico disponibile è esaurito.
     */
    public CapacityExceededException() {
        super("capacity");
    }
}