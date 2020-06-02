package it.polito.tdp.borders.model;

//deve essere comparabile!!!
public class Evento implements Comparable<Evento> {

//	un singolo evento, quindi non abbiamo bisogno di enum

	private int t;
	private Country stato; // in cui arrivano i migranti al tempo t
	private int n; // numero di migranti che arrivano in "stato" al tempo "t"

//	n/2 si sposterà al tempo t+1

	public Evento(int t, Country stato, int n) {
		super();
		this.t = t;
		this.stato = stato;
		this.n = n;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	public Country getStato() {
		return stato;
	}

	public void setStato(Country stato) {
		this.stato = stato;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	@Override
	public int compareTo(Evento other) {
		return this.t - other.t;
	}

}
