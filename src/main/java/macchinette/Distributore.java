package macchinette;

import java.util.List;
import java.util.Objects;

/**
 * Rappresenta il controllore principale del distributore automatico.
 *
 * <p>Questa classe agisce da coordinatore centrale del sistema, gestendo tre responsabilità principali:
 * <ol>
 * <li><b>Gestione Inventario:</b> Mantiene lo stato dei binari e dei prodotti in essi contenuti.</li>
 * <li><b>Contabilità:</b> Gestisce il fondo cassa (aggregato di monete) accumulando pagamenti ed erogando resti.</li>
 * <li><b>Logica di Vendita:</b> Coordina le transazioni assicurando che siano atomiche (o avvengono completamente o falliscono senza effetti collaterali).</li>
 * </ol>
 */
public class Distributore {

    /** La lista sequenziale dei binari (slot) fisici. La lista è immutabile, il contenuto dei binari è mutabile. */
    private final List<Binario> binari;

    /** Il fondo cassa corrente. Aggiornato durante le transazioni e le operazioni di manutenzione. */
    private Aggregato cassa;

    /** La strategia (Pattern Strategy) utilizzata per il calcolo del resto. */
    private final StrategiaResto strategia;

    /*-
     * AF: Un oggetto Distributore rappresenta un sistema di vendita S composto da tre elementi:
     * - B: Una sequenza ordinata di contenitori (i binari), accessibili tramite indice numerico.
     * - C: Un insieme di monete (il fondo cassa) disponibile per i resti.
     * - Alg: Una strategia algoritmica utilizzata per calcolare la composizione del resto.
     *
     * RI: Affinché lo stato del distributore sia consistente:
     * - La lista dei binari non deve essere null e non deve contenere elementi null.
     * - Il fondo cassa non deve essere null (al limite può essere un aggregato vuoto).
     * - La strategia di resto non deve essere null.
     */

    /**
     * Crea un nuovo distributore automatico configurato e pronto all'uso.
     *
     * <p>Inizializza il sistema validando i componenti e stabilendo l'invariante di rappresentazione.
     * Verifica preventivamente che la lista dei binari non contenga elementi null.
     *
     * @param binari    La lista dei binari fisici installati nel distributore. Non deve essere null né contenere elementi null.
     * @param cassa     La dotazione iniziale di monete per il fondo cassa. Non deve essere null.
     * @param strategia L'algoritmo da utilizzare per il calcolo del resto. Non deve essere null.
     * @throws NullPointerException se uno qualsiasi dei parametri è null, o se la lista binari contiene elementi null.
     */
    public Distributore(List<Binario> binari, Aggregato cassa, StrategiaResto strategia) {
        this.binari = Objects.requireNonNull(binari, "La lista dei binari non può essere null");
        this.cassa = Objects.requireNonNull(cassa, "La cassa iniziale non può essere null");
        this.strategia = Objects.requireNonNull(strategia, "La strategia non può essere null");

        for (Binario b : binari) {
            Objects.requireNonNull(b, "La lista dei binari non può contenere elementi null");
        }
    }

    /**
     * Carica una determinata quantità di un prodotto nel sistema.
     *
     * <p>L'operazione segue una logica "greedy" sequenziale (First Fit): scorre i binari disponibili e tenta di inserire
     * il prodotto nel primo binario compatibile (per taglia e tipologia di contenuto), riempiendolo fino alla capacità massima.
     * Se rimane del prodotto, prosegue con i binari successivi finché non viene esaurita la quantità o lo spazio.
     *
     * @param p        Il prodotto da caricare. Non deve essere null.
     * @param quantita Il numero totale di unità da inserire. Deve essere un intero non negativo.
     * @return Il numero di prodotti che <b>non</b> è stato possibile caricare (residuo) per mancanza di spazio o binari idonei.
     * @throws NullPointerException     se il prodotto è null.
     * @throws IllegalArgumentException se la quantità è negativa.
     */
    public int carica(Prodotto p, int quantita) {
        Objects.requireNonNull(p, "Il prodotto da caricare non può essere null");
        if (quantita < 0) {
            throw new IllegalArgumentException("La quantità non può essere negativa");
        }

        int rimasti = quantita;

        for (Binario b : binari) {
            if (rimasti == 0) break;

            if (!b.getTaglia().ospita(p.getTaglia())) {
                continue;
            }

            try {
                if (!b.isVuoto() && !b.peek().equals(p)) {
                    continue;
                }
            } catch (BinarioVuotoException e) {
                continue;
            }

            int daInserire = Math.min(rimasti, b.spazioDisponibile());
            if (daInserire > 0) {
                try {
                    b.carica(p, daInserire);
                    rimasti -= daInserire;
                } catch (DistributoreException e) {
                }
            }
        }
        return rimasti;
    }

    /**
     * Esegue una transazione di vendita atomica.
     *
     * <p>Coordina l'interazione tra binario, pagamento e strategia di resto seguendo un modello transazionale a due fasi:
     * <ol>
     * <li><b>Validazione e Calcolo (Fase di Pre-Commit):</b> Verifica la disponibilità del prodotto, la sufficienza del credito
     * e la fattibilità tecnica del resto utilizzando una copia simulata del fondo cassa che include il pagamento corrente.</li>
     * <li><b>Esecuzione (Commit):</b> Se tutte le condizioni sono soddisfatte, rende persistenti le modifiche
     * (aggiorna la cassa reale ed eroga il prodotto).</li>
     * </ol>
     * Se una qualsiasi condizione fallisce, viene lanciata un'eccezione e lo stato del distributore rimane invariato.
     *
     * @param indiceBinario L'indice del binario da cui prelevare il prodotto (0-based).
     * @param pagamento     Le monete inserite dall'utente per il pagamento. Non deve essere null.
     * @return L'aggregato di monete che costituisce il resto.
     * @throws SlotNotFoundException      se l'indice non corrisponde a un binario esistente.
     * @throws BinarioVuotoException      se il binario selezionato è vuoto.
     * @throws InsufficientValueException se il valore del pagamento è inferiore al prezzo del prodotto.
     * @throws ChangeNotPossibleException se il distributore non dispone delle monete necessarie per il resto.
     * @throws NullPointerException       se l'aggregato pagamento è null.
     */
    public Aggregato eroga(int indiceBinario, Aggregato pagamento) throws DistributoreException {
        Objects.requireNonNull(pagamento, "L'aggregato pagamento non può essere null");

        if (indiceBinario < 0 || indiceBinario >= binari.size()) {
            throw new SlotNotFoundException();
        }

        Binario binario = binari.get(indiceBinario);
        if (binario.isVuoto()) {
            throw new BinarioVuotoException();
        }

        Prodotto prodotto = binario.peek();
        Importo valorePagato = pagamento.getValoreTotale();
        Importo prezzo = prodotto.getPrezzo();

        if (valorePagato.compareTo(prezzo) < 0) {
            throw new InsufficientValueException();
        }

        Importo restoDovuto;
        try {
            restoDovuto = valorePagato.sottrazione(prezzo);
        } catch (NegativeResultException e) {
            throw new InsufficientValueException();
        }

        Aggregato cassaSimulata = new Aggregato();
        cassaSimulata.aggiungi(this.cassa);
        cassaSimulata.aggiungi(pagamento);

        Aggregato restoCalcolato = strategia.calcolaResto(restoDovuto, cassaSimulata);

        try {
            cassaSimulata.rimuovi(restoCalcolato);
            this.cassa = cassaSimulata;
            binario.dispensa();
        } catch (MissingValueException | MissingCoinsException e) {
            throw new ChangeNotPossibleException();
        }

        return restoCalcolato;
    }

    /**
     * Aggiunge un insieme di monete al fondo cassa (Operazione di manutenzione).
     *
     * <p>Incrementa le quantità delle monete nel fondo cassa unendo quelle fornite.
     *
     * @param monete L'aggregato di monete da versare nel distributore. Non deve essere null.
     * @throws NullPointerException se il parametro monete è null.
     */
    public void aggiungiMonete(Aggregato monete) {
        Objects.requireNonNull(monete, "Le monete da aggiungere non possono essere null");
        this.cassa.aggiungi(monete);
    }

    /**
     * Preleva l'intero contenuto del fondo cassa (Operazione di manutenzione).
     *
     * <p>Il fondo cassa del distributore viene resettato a un aggregato vuoto.
     *
     * @return Un nuovo aggregato contenente tutte le monete precedentemente presenti.
     */
    public Aggregato svuotaCassa() {
        Aggregato precedente = this.cassa;
        this.cassa = new Aggregato();
        return precedente;
    }

    /**
     * Restituisce il valore totale attualmente contenuto nel fondo cassa.
     *
     * @return L'importo totale somma di tutte le monete presenti.
     */
    public Importo getImportoTotaleCassa() {
        return cassa.getValoreTotale();
    }

    /**
     * Fornisce accesso in sola lettura allo stato di un binario specifico.
     *
     * @param indice L'indice numerico del binario (0-based).
     * @return Il riferimento al binario richiesto.
     * @throws SlotNotFoundException se l'indice è fuori dai limiti validi.
     */
    public Binario getBinario(int indice) throws SlotNotFoundException {
        if (indice < 0 || indice >= binari.size()) {
            throw new SlotNotFoundException();
        }
        return binari.get(indice);
    }

    /**
     * Genera un report testuale dell'inventario prodotti.
     *
     * <p>Il formato elenca solo i binari non vuoti, riportando per ciascuno: indice, nome del prodotto e prezzo.
     *
     * @return La stringa formattata dell'inventario.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Prodotti disponibili:\n");
        for (int i = 0; i < binari.size(); i++) {
            Binario b = binari.get(i);
            if (!b.isVuoto()) {
                try {
                    Prodotto p = b.peek();
                    sb.append(String.format("Binario %d: %s - %s\n",
                            i, p.getNome(), p.getPrezzo()));
                } catch (BinarioVuotoException e) {
                }
            }
        }
        return sb.toString();
    }
}