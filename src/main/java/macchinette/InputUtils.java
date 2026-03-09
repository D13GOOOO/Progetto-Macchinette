package macchinette;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Classe di utilità (Stateless) per l'analisi e la tokenizzazione degli input testuali.
 *
 * <p>Questa classe agisce come un layer di traduzione, convertendo le rappresentazioni
 * testuali "sporche" (file di configurazione, input da console) in Oggetti di Dominio
 * fortemente tipizzati e validati (come {@link Importo}, {@link Prodotto}, {@link Aggregato}).
 *
 * <p>L'utilizzo di questa classe centralizza la logica di interpretazione delle stringhe, evitando
 * che la logica di business venga inquinata da dettagli di formattazione.
 */
public final class InputUtils {

    /*-
     * AF: La classe rappresenta una collezione di funzioni pure statiche del tipo:
     * f: String -> DomainObject.
     *
     * RI: La classe non è istanziabile e non mantiene alcuno stato statico mutabile.
     */

    /**
     * Costruttore privato per impedire l'istanziazione accidentale.
     *
     * <p>Solleva un'eccezione {@link UnsupportedOperationException} se invocato,
     * poiché la classe è progettata per essere puramente statica.
     */
    private InputUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Converte una stringa numerica in un oggetto Importo.
     *
     * <p>Gestisce correttamente la conversione da formato decimale umano al formato interno intero (centesimi),
     * prevenendo errori di arrotondamento virgola mobile.
     * Restituisce l'importo corrispondente al valore decimale elaborato.
     *
     * @param str La stringa rappresentante il valore monetario.
     * @return Un nuovo oggetto {@link Importo}.
     * @throws IllegalArgumentException se {@code str} è null.
     * @throws NumberFormatException    se la stringa non è un numero valido o se il valore è troppo grande per un intero.
     */
    public static Importo parseImporto(String str) {
        if (str == null) throw new IllegalArgumentException("La stringa importo non può essere null");
        try {
            BigDecimal bd = new BigDecimal(str.trim());
            int cents = bd.movePointRight(2).intValueExact();
            return new Importo(cents);
        } catch (NumberFormatException | ArithmeticException e) {
            throw new NumberFormatException("Formato importo non valido o fuori scala: " + str);
        }
    }

    /**
     * Converte una stringa in una costante Taglia.
     *
     * @param str La stringa rappresentante la taglia.
     * @return La costante enum corrispondente.
     * @throws NullPointerException     se {@code str} è null.
     * @throws IllegalArgumentException se la stringa non corrisponde a nessuna taglia nota.
     */
    public static Taglia parseTaglia(String str) {
        Objects.requireNonNull(str, "La stringa taglia non può essere null");
        return Taglia.valueOf(str.trim().toUpperCase());
    }

    /**
     * Analizza la stringa di un prodotto nel formato standard con separatore pipe.
     *
     * <p>Formato atteso: {@code nome|prezzo|taglia}
     *
     * @param str La stringa di input da analizzare.
     * @return L'oggetto {@link Prodotto} risultante.
     * @throws IllegalArgumentException se il formato è errato.
     */
    public static Prodotto parseProdotto(String str) {
        return parseProdottoGenerico(str, "\\|");
    }

    /**
     * Analizza la stringa di un prodotto nel formato specifico per il caricamento binari.
     *
     * <p>Formato atteso: {@code nome@prezzo@taglia}
     *
     * @param str La stringa di input da analizzare.
     * @return L'oggetto {@link Prodotto} risultante.
     * @throws IllegalArgumentException se il formato è errato.
     */
    public static Prodotto parseProdottoAt(String str) {
        return parseProdottoGenerico(str, "@");
    }

    /**
     * Analizza la stringa di un prodotto nel formato alternativo con punto e virgola.
     *
     * <p>Formato atteso: {@code nome;prezzo;taglia}
     *
     * @param str La stringa di input da analizzare.
     * @return L'oggetto {@link Prodotto} risultante.
     * @throws IllegalArgumentException se il formato è errato.
     */
    public static Prodotto parseProdottoSemicolon(String str) {
        return parseProdottoGenerico(str, ";");
    }

    /**
     * Logica comune di analisi ed estrazione dati per i prodotti.
     *
     * @param str   La stringa contenente i dati del prodotto.
     * @param regex L'espressione regolare usata come separatore dei campi.
     * @return Un nuovo oggetto {@link Prodotto} popolato con i dati estratti.
     * @throws IllegalArgumentException se la stringa non contiene esattamente le 3 componenti richieste.
     */
    private static Prodotto parseProdottoGenerico(String str, String regex) {
        Objects.requireNonNull(str, "La stringa prodotto non può essere null");
        String[] parts = str.trim().split(regex);

        if (parts.length < 3) {
            throw new IllegalArgumentException("Formato prodotto incompleto o errato (richiesti: nome, prezzo, taglia): " + str);
        }

        String nome = parts[0].trim();
        Importo prezzo = parseImporto(parts[1].trim());
        Taglia taglia = parseTaglia(parts[2].trim());

        return new Prodotto(nome, prezzo, taglia);
    }

    /**
     * Interpreta la stringa di un aggregato nel formato standard (virgola e "x").
     *
     * <p>Formato atteso: {@code n x Prezzo, m x Prezzo}
     *
     * @param str La stringa di input.
     * @return Un nuovo oggetto {@link Aggregato} contenente le monete specificate.
     */
    public static Aggregato parseAggregato(String str) {
        return parseAggregatoGenerico(str, ",", "x");
    }

    /**
     * Interpreta la stringa di un aggregato nel formato alternativo (punto e virgola e "*").
     *
     * <p>Formato atteso: {@code n * Prezzo; m * Prezzo}
     *
     * @param str La stringa di input.
     * @return Un nuovo oggetto {@link Aggregato} contenente le monete specificate.
     */
    public static Aggregato parseAggregatoStar(String str) {
        return parseAggregatoGenerico(str, ";", "\\*");
    }

    /**
     * Logica comune di interpretazione per gli aggregati di monete.
     *
     * <p>Il metodo analizza la stringa, cerca le monete corrispondenti nell'enum {@link Moneta}
     * e popola l'aggregato. Se un valore monetario non corrisponde a nessuna moneta reale,
     * viene sollevata un'eccezione.
     *
     * @param str          La stringa completa da analizzare.
     * @param entrySep     Il separatore tra le diverse entrate.
     * @param pairSepRegex La regex del separatore tra quantità e valore.
     * @return L'aggregato popolato.
     * @throws IllegalArgumentException se il formato è errato o viene specificata una moneta inesistente.
     */
    private static Aggregato parseAggregatoGenerico(String str, String entrySep, String pairSepRegex) {
        Aggregato a = new Aggregato();
        if (str == null || str.trim().isEmpty()) {
            return a;
        }

        String[] entries = str.split(entrySep);
        for (String entry : entries) {
            if (entry.trim().isEmpty()) continue;

            String[] pair = entry.trim().split(pairSepRegex);
            if (pair.length < 2) {
                throw new IllegalArgumentException("Formato coppia quantità-valore errato: " + entry);
            }

            int quantita = Integer.parseInt(pair[0].trim());
            Importo valore = parseImporto(pair[1].trim());

            Moneta monetaTrovata = null;
            for (Moneta m : Moneta.values()) {
                if (m.getValore().equals(valore)) {
                    monetaTrovata = m;
                    break;
                }
            }

            if (monetaTrovata == null) {
                throw new IllegalArgumentException("Nessuna moneta fisica corrisponde all'importo: " + valore);
            }

            a.aggiungiMoneta(monetaTrovata, quantita);
        }
        return a;
    }
}