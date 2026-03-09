/**
 * Contiene i componenti core per la simulazione e gestione di un distributore automatico.
 * <p>
 * L'architettura del pacchetto è progettata seguendo i principi di <b>Information Hiding</b> e
 * <b>Separation of Concerns</b>. Il dominio è suddiviso in:
 * <ul>
 * <li><b>Modello Dati (Immutabile/Value Types):</b> {@link macchinette.Importo}, {@link macchinette.Moneta}, {@link macchinette.Prodotto}.
 * Queste classi garantiscono la thread-safety intrinseca tramite immutabilità.</li>
 * <li><b>Gestione Inventario e Cassa:</b> {@link macchinette.Binario}, {@link macchinette.Aggregato}.
 * Incapsulano lo stato mutabile proteggendolo da accessi non validi (RI).</li>
 * <li><b>Logica di Business (Strategy Pattern):</b> {@link macchinette.StrategiaResto} e le sue implementazioni
 * ({@link macchinette.StrategiaMista}, ecc.) per isolare l'algoritmo di calcolo dal contesto.</li>
 * <li><b>Parsing e Adattamento:</b> {@link macchinette.InputUtils}, per separare la logica di interpretazione
 * degli input testuali dalle classi di dominio.</li>
 * <li><b>Controller Principale:</b> {@link macchinette.Distributore}, che orchestra le interazioni.</li>
 * </ul>
 *
 * <h2>Risorse Utilizzate</h2>
 * <ul>
 * <li>Materiale didattico del corso (PDJ, JT, EJ, note sul sito principale, appunti personali).</li>
 * <li>Documentazione ufficiale Java (Oracle Docs) per {@code EnumMap}, {@code BigDecimal} e {@code Math.exact}.</li>
 * <li>Supporto AI (modello LLM) per il confronto critico sulle scelte architetturali (Liskov, Design by Contract),
 * la revisione degli invarianti di rappresentazione e il raffinamento della Javadoc.</li>
 * </ul>
 */
package macchinette;