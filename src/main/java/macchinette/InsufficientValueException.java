package macchinette;

/**
 * Eccezione controllata (Checked Exception) che segnala un credito insufficiente per completare l'acquisto.
 *
 * <p>Questa eccezione viene sollevata quando l'utente tenta di acquistare un prodotto ma il valore totale
 * delle monete inserite (il credito accumulato) è strettamente inferiore al prezzo di vendita del prodotto selezionato.
 */
public class InsufficientValueException extends DistributoreException {

    /**
     * Identificativo univoco della versione per la serializzazione.
     * Garantisce la compatibilità tra la classe serializzata e quella caricata in memoria.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Costruisce una nuova istanza dell'eccezione.
     *
     * <p>Inizializza l'eccezione impostando il messaggio standard di errore a "value",
     * indicando al client che il credito fornito non copre il costo richiesto.
     */
    public InsufficientValueException() {
        super("value");
    }
}