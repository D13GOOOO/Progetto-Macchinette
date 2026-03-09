package macchinette;

/**
 * Interfaccia funzionale che definisce il contratto per gli algoritmi di calcolo del resto.
 *
 * <p>Questa interfaccia è il cuore del pattern <b>Strategy</b>. Isola la logica algoritmica
 * (come selezionare le monete) dal contesto che detiene lo stato (il Distributore).
 *
 * <p><b>Contratto per le implementazioni:</b>
 * Le classi che implementano questa interfaccia devono comportarsi come funzioni pure:
 * <ul>
 * <li>Non devono mantenere stato interno mutabile tra una chiamata e l'altra (Stateless).</li>
 * <li>Non devono modificare gli oggetti passati come parametri (in particolare l'aggregato {@code cassa}).</li>
 * <li>Devono garantire il determinismo: a parità di input, devono produrre sempre lo stesso output.</li>
 * </ul>
 */
public interface StrategiaResto {

    /**
     * Calcola una combinazione di monete prelevabili dalla cassa che eguagli l'importo richiesto.
     *
     * <p>Questo metodo non deve modificare lo stato dell'aggregato {@code cassa} passato in input.
     *
     * @param restoDovuto L'importo target che deve essere coperto. Non deve essere null.
     * @param cassa       La disponibilità di monete da cui attingere. Non deve essere null.
     * @return Un <b>nuovo</b> oggetto {@link Aggregato} contenente le monete selezionate.
     * Il valore totale dell'aggregato restituito deve essere uguale a {@code restoDovuto}
     * e la quantità di ogni moneta non deve superare la disponibilità in {@code cassa}.
     * @throws NullPointerException       se uno dei parametri è null.
     * @throws ChangeNotPossibleException se, secondo la logica specifica dell'algoritmo,
     * non è possibile formare l'importo esatto con le monete disponibili.
     */
    Aggregato calcolaResto(Importo restoDovuto, Aggregato cassa) throws ChangeNotPossibleException;
}