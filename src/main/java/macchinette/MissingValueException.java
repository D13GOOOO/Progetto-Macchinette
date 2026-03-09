package macchinette;

/**
 * Eccezione controllata (Checked Exception) che segnala l'assenza di un valore obbligatorio.
 *
 * <p>Questa eccezione viene sollevata quando il sistema si aspetta un dato (tipicamente numerico o monetario)
 * per completare un'operazione o un parsing, ma l'input fornito è incompleto o malformato.
 */
public class MissingValueException extends DistributoreException {

    /**
     * Identificativo univoco della versione per la serializzazione.
     * Garantisce la compatibilità tra la classe serializzata e quella caricata in memoria.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Costruisce una nuova istanza dell'eccezione.
     *
     * <p>Inizializza l'eccezione impostando il messaggio standard di errore a "missing-value",
     * per notificare al client che un'operazione non può procedere per mancanza di dati.
     */
    public MissingValueException() {
        super("missing-value");
    }
}