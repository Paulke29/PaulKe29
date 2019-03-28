import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;

// TODO Refactor this to InvertedIndex and remove the Index interface (or modify to be inverted)

/**
 * A special type of {@link Index} that indexes the locations words were found.
 */

public class InvertedIndex {
	// TODO Need to fix these members
//	private TreeMap<String, TreeSet<Integer>> answer;
//	private TreeSet<Integer> wordindex;
	private TreeMap<String, TreeMap<String, TreeSet<Integer>>> finalindex;
	private TreeMap<String, Integer> wordcount;
	/**
	 * TODO
	 * @param words
	 * @param file
	 */
	public InvertedIndex() {
		finalindex = new TreeMap<>();
		wordcount = new TreeMap<>();
	}


	/**
	 * @param words  String words
	 * @param location filename 
	 * @param position index for every words
	 * @return true or false
	 */
	public boolean add(String words, String location, int position) {
		if (!finalindex.containsKey(words)) {
			TreeMap<String, TreeSet<Integer>> answer = new TreeMap<>();
			TreeSet<Integer> wordindex = new TreeSet<>();
			wordindex.add(position);
			answer.put(location, wordindex);
			finalindex.put(words, answer);
			return true;
		} else {
			if (finalindex.containsKey(words) && !finalindex.get(words).containsKey(location)) {
				TreeSet<Integer> wordindex = new TreeSet<>();
				wordindex.add(position);
				finalindex.get(words).put(location, wordindex);
				return true;
			}
			if (finalindex.containsKey(words) && finalindex.get(words).containsKey(location)) {
				finalindex.get(words).get(location).add(position);
				return true;
			} else {
				return false;
			}
		}
	}
	/**
	 * @param file
	 * @param number
	 */
	public void wordcount(String file,int number) {
		if(number >0) {
			wordcount.put(file, number); 	
		}	
	}
    /**
     * update the InvertedIndex Object
     * @param SingleInvertedIndex
     */
    public void addAll(InvertedIndex SingleInvertedIndex) {
    	for(String keys: SingleInvertedIndex.finalindex.keySet()) {
    		boolean same = true;
    		for(String OldKey: this.finalindex.keySet()) {
    			if(!keys.equals(OldKey)){
    				same = false;
    			}
    			if(keys.equals(OldKey)) {
    				for(String filename : SingleInvertedIndex.finalindex.get(keys).keySet()) {
    					for(String files : wordcount.keySet()) {
    						
    					}
    				}
    			}
    		}
    		if(same == false){
    			this.finalindex.putAll(SingleInvertedIndex.finalindex);
    		}
    	}
    }



}
