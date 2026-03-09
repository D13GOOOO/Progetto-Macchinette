package macchinette;

/**
 * Eccezione controllata (Checked Exception) che segnala l'indisponibilità fisica di prodotti in un binario.
 *
 * <p>Questa eccezione viene sollevata quando si tenta di prelevare (erogare) o ispezionare un prodotto
 * da uno specifico binario, ma il contatore della quantità di prodotti in quel binario è pari a zero.
 */
public class BinarioVuotoException extends DistributoreException {

    /**
     * Identificativo univoco della versione per la serializzazione.
     * Garantisce la compatibilità tra la classe serializzata e quella caricata in memoria.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Costruisce una nuova istanza dell'eccezione.
     *
     * <p>Inizializza l'eccezione impostando il messaggio standard di errore a "empty",
     * per notificare al client che l'operazione è fallita a causa dell'assenza di prodotti.
     */
    public BinarioVuotoException() {
        super("empty");
    }
}