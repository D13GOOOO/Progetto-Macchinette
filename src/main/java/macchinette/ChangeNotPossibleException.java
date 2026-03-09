package macchinette;

/**
 * Eccezione controllata (Checked Exception) che segnala l'impossibilità combinatoria di erogare il resto esatto.
 *
 * <p>Questa eccezione viene sollevata dalle strategie di calcolo del resto quando, pur essendoci potenzialmente
 * un valore totale sufficiente nella cassa, non esiste una combinazione di monete disponibili
 * che sommate eguaglino esattamente l'importo da restituire.
 */
public class ChangeNotPossibleException extends DistributoreException {

    /**
     * Identificativo univoco della versione per la serializzazione.
     * Garantisce la compatibilità tra la classe serializzata e quella caricata in memoria.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Costruisce una nuova istanza dell'eccezione.
     *
     * <p>Inizializza l'eccezione impostando il messaggio standard di errore a "change-not-possible",
     * per notificare al client che l'erogazione non può essere finalizzata per mancanza di spiccioli adeguati.
     */
    public ChangeNotPossibleException() {
        super("change-not-possible");
    }
}