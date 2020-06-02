package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	private Graph<Country, DefaultEdge> graph;
	private Map<Integer, Country> countriesMap;
	private Simulatore sim;

	public Model() {
		this.countriesMap = new HashMap<>();
		this.sim = new Simulatore();

	}

	public void creaGrafo(int anno) {

		this.graph = new SimpleGraph<>(DefaultEdge.class);

		BordersDAO dao = new BordersDAO();

		// vertici
		dao.getCountriesFromYear(anno, this.countriesMap);
		Graphs.addAllVertices(graph, this.countriesMap.values());

		// archi
		List<Adiacenza> archi = dao.getCoppieAdiacenti(anno);
		for (Adiacenza c : archi) {
			graph.addEdge(this.countriesMap.get(c.getState1no()), this.countriesMap.get(c.getState2no()));

		}
	}
	
	public List<Country> getCountries() {
		
//		Creiamo una lista da appoggio in cui scaricare il vertexSet
		List<Country> countries = new ArrayList<Country>();
		countries.addAll(this.graph.vertexSet());
		
//		Ordiniamo la lista così gli stati li avremo in ordine alfabetico
		Collections.sort(countries);
		
		return countries;
	}

	public List<CountryAndNumber> getCountryAndNumber() {

//		Utilizziamo questa classe country and number (di vicini)

//		L'abbiamo creata comparable così la ordiniamo in base al number (decrescente)

		List<CountryAndNumber> list = new ArrayList<>();

		for (Country c : graph.vertexSet()) {
			list.add(new CountryAndNumber(c, graph.degreeOf(c)));
		}
		Collections.sort(list);
		return list;
	}

	public void simula(Country partenza) {
		
//		simuliamo solo se abbiamo il grafo pronto
		if (this.graph != null) {
			sim.init(partenza, this.graph);
			sim.run();
		}
	}

	public Integer getT() {
//		ritorna -1 se la simulazione non è stata fatta
		return this.sim.getT();
	}
	
//	ricicliamo la classe country and number, stavolta number rappresenta gli stanziali
	public List<CountryAndNumber> getStanziali(){
		
		Map<Country, Integer> stanziali = this.sim.getStanziali();
		
		List<CountryAndNumber> lista = new ArrayList<>();
		
//		scorro le chiavi e ogni volta prendo lo stato e il numero di stanziali
//		li salvo nell'oggetto c&n e lo aggiungo alla lista
		for(Country c: stanziali.keySet()) {
			
			CountryAndNumber cn = new CountryAndNumber(c, stanziali.get(c));
			lista.add(cn);
		}
		
		Collections.sort(lista);
		
		return lista;
		
	}

	
	

}
