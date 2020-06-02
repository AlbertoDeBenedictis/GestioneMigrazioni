package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

// Messo per comodità così dividiamo un po' il codice
public class Simulatore {

//	Qual è il nostro MODELLO?
	private Graph<Country, DefaultEdge> grafo;

//	Quali sono i TIPI DI EVENTO? Da inserire nella coda prioritaria
//	L'unico evento è l'arrivo dei migranti in uno stato
	private PriorityQueue<Evento> queue;

//	Quali sono i PARAMETRI della simulazione? (un valore costante e uno stato da scegliere)
	private int N_MIGRANTI = 1000;
	private Country partenza;

//	Qual è l'OUTPUT? (ci viene detto nel testo)	
	private int T = -1;
//	chiave: paese, value: n° stanziati
	Map<Country, Integer> stanziali;

	
	
	/**
	 * Metodo per inizializzare il simulatore
	 * 
	 * @param partenza da dove partono i migranti
	 * @param grafo    il grafo su cui si muovono i migranti
	 */
	public void init(Country partenza, Graph<Country, DefaultEdge> grafo) {

//		1. Prendiamo i valori
		this.partenza = partenza;
		this.grafo = grafo;

//		2. Impostiamo lo stato iniziale
		this.T = 1;
		stanziali = new HashMap<>();

//		Metto i migranti stanziali uguali a 0 in ogni stato
		for (Country c : this.grafo.vertexSet()) {
			stanziali.put(c, 0);
		}

//		Creo la coda e inserisco il primo evento (arrivano 1000 migranti in stato di partenza
		this.queue = new PriorityQueue<Evento>();
		this.queue.add(new Evento(T, partenza, N_MIGRANTI));

	}

	public void run() {

//		Finchè c'è un evento nella coda, lo estraggo e lo eseguo
		Evento e;
		while ((e = this.queue.poll()) != null) {

//			ESEGUO L'EVENTO e

//			1. mi recupero le info dell'evento
			this.T = e.getT();
			int nPersone = e.getN();
			Country stato = e.getStato();

//			2. Cerco i vicini di stato
			List<Country> vicini = Graphs.neighborListOf(this.grafo, stato);

//			3. Genero gli eventi (n/2 migranti divisi tra i vicini)
//			NB: gli int arrotondano automaticamente per difetto
			int migranti = (nPersone / 2) / vicini.size();

//			se abbiamo abbastanza migranti, si spostano
			if (migranti > 0) {
//				generiamo gli spostamenti
				for (Country confinante : vicini) {

//					genero l'evento e lo aggiungo alla lista
					queue.add(new Evento(e.getT() + 1, confinante, migranti));

//					registro le stanziali nella mappa degli stanziali
					int stanziali = nPersone - (migranti * vicini.size());

//					NB: non posso sovrascrivere stanziali, perchè potrei esserci già passato
//					SOLUZIONE: aggiungo al value precedente le stanziali nuove
					this.stanziali.put(stato, this.stanziali.get(stato) + stanziali);
				}
			}

		}
	}

	public Integer getT() {
		return this.T;
	}

	public Map<Country, Integer> getStanziali() {
		return this.stanziali;
	}

}
