package macchinette;

/**
 * Eccezione controllata (Checked Exception) che segnala l'assenza di specifiche sulle monete in un input.
 *
 * <p>Questa eccezione viene sollevata quando il sistema elabora un comando o una stringa che dovrebbe contenere
 * una definizione di monete (es. quantità e tipo), ma tale definizione risulta assente o non identificabile.
 */
public class MissingCoinsException extends DistributoreException {

    /**
     * Identificativo univoco della versione per la serializzazione.
     * Garantisce la compatibilità tra la classe serializzata e quella caricata in memoria.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Costruisce una nuova istanza dell'eccezione.
     *
     * <p>Inizializza l'eccezione impostando il messaggio standard di errore a "missing-coins",
     * per segnalare al client che la specifica delle monete è omessa.
     */
    public MissingCoinsException() {
        super("missing-coins");
    }
}