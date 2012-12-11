package search;

import models.Textbook;

public class SearchObject implements Comparable<SearchObject> {	
	private Textbook t;
	private int rank;
	
	public SearchObject(Textbook t, int rank) {
		this.t = t;
		this.rank = rank;
	}
	
	public Textbook textbook() {
		return t;
	}

	@Override
	public int compareTo(SearchObject o) {
		return o.rank - rank;		
	}
}