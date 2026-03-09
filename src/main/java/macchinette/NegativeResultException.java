package macchinette;

/**
 * Eccezione controllata (Checked Exception) che segnala il tentativo di generare un importo monetario negativo.
 *
 * <p>Questa eccezione viene sollevata tipicamente durante operazioni aritmetiche (come la sottrazione)
 * tra oggetti {@link Importo}, quando il risultato matematico dell'operazione violerebbe
 * l'invariante di rappresentazione della classe Importo (che ammette esclusivamente valori non negativi).
 */
public class NegativeResultException extends DistributoreException {

    /**
     * Identificativo univoco della versione per la serializzazione.
     * Garantisce la compatibilità tra la classe serializzata e quella caricata in memoria.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Costruisce una nuova istanza dell'eccezione.
     *
     * <p>Inizializza l'eccezione impostando il messaggio standard di errore a "negative-result",
     * necessario per conformarsi al protocollo di output dei client.
     */
    public NegativeResultException() {
        super("negative-result");
    }
}