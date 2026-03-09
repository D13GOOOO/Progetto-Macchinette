package macchinette;

/**
 * Eccezione base controllata (Checked Exception) per l'intero dominio dell'applicazione Distributore.
 *
 * <p>Questa classe funge da radice per la gerarchia delle eccezioni specifiche del progetto.
 * Il suo utilizzo permette ai client di intercettare genericamente qualsiasi errore di logica
 * del distributore (catch polimorfico) oppure di gestire specificamente le sottoclassi
 * per errori puntuali (es. credito insufficiente, prodotto esaurito).
 */
public class DistributoreException extends Exception {

    /**
     * Identificativo univoco della versione per la serializzazione.
     * Garantisce la compatibilità tra la classe serializzata e quella caricata in memoria.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Costruisce una nuova eccezione del distributore con un messaggio di dettaglio specificato.
     *
     * <p>Inizializza l'eccezione salvando il messaggio descrittivo fornito, che potrà essere
     * successivamente recuperato tramite il metodo {@link #getMessage()}.
     *
     * @param message Il messaggio che descrive l'errore.
     */
    public DistributoreException(String message) {
        super(message);
    }
}