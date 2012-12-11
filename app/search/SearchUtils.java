package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Textbook;

public class SearchUtils {
	
	public static List sortByFreqs(List input) {
		Map<Textbook, Integer> rank = new HashMap<Textbook, Integer>();
		
		for (Object o : input) {
			if (o instanceof Textbook) {
				Textbook t = (Textbook) o;
				if (!rank.containsKey(t)) 
					rank.put(t, 0);
				rank.put(t, rank.get(t) + 1);
			}
		}				
		List<SearchObject> searchObjects = new ArrayList<SearchObject>();
		for (Textbook t : rank.keySet())
			searchObjects.add(new SearchObject(t, rank.get(t)));
		Collections.sort(searchObjects);		
		
		List results = new ArrayList();
		for (SearchObject s : searchObjects)
			results.add(s.textbook());
		
		return results;
	}
}


