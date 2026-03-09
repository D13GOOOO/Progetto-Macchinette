package macchinette;

/**
 * Eccezione controllata (Checked Exception) che segnala un tentativo di accesso a un binario inesistente.
 *
 * <p>Questa eccezione viene sollevata quando un indice di slot fornito in input non corrisponde
 * a nessun binario configurato nel distributore (indice fuori dai limiti validi).
 */
public class SlotNotFoundException extends DistributoreException {

    /**
     * Identificativo univoco della versione per la serializzazione.
     * Garantisce la compatibilità tra la classe serializzata e quella caricata in memoria.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Costruisce una nuova istanza dell'eccezione.
     *
     * <p>Inizializza l'eccezione impostando il messaggio standard di errore a "slot",
     * come richiesto dalle specifiche di protocollo del sistema.
     */
    public SlotNotFoundException() {
        super("slot");
    }
}