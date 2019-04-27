import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author paulke
 *
 */
public class InvertedIndex {
	/**
	 * creating a dataStructure for index
	 */
	protected final TreeMap<String, TreeMap<String, TreeSet<Integer>>> finalIndex;
	/**
	 * creating a dataStructure for word count
	 */
	private final TreeMap<String, Integer> wordCount;

	/**
	 * initial TreeMap
	 */
	public InvertedIndex() {
		finalIndex = new TreeMap<>();
		wordCount = new TreeMap<>();
	}
  public String toString() {
	return this.finalIndex.toString();
	  
  }
	/**
	 * @param words    String words
	 * @param location filename
	 * @param position index for every words
	 * @return true or false
	 */
	public boolean add(String words, String location, int position) {
		finalIndex.putIfAbsent(words, new TreeMap<>());
		finalIndex.get(words).putIfAbsent(location, new TreeSet<>());
		boolean checking = this.finalIndex.get(words).get(location).add(position);
		if (checking) {
			Integer number = this.wordCount.getOrDefault(location, 0);
			this.wordCount.put(location, number + 1);
		}
		return checking;
	}


	/**
	 * Exact search for query words
	 * 
	 * @param QueryLine
	 * @return exact search result
	 */
	public ArrayList<Result> ExactSearch(Set<String> QueryLine) {
		ArrayList<Result> getResultList = new ArrayList<>();
		Map<String, Result> findUp = new HashMap<>();
		for (String queryWord : QueryLine) {
			if (this.contains(queryWord)) {
				this.searchProcess(queryWord, getResultList, findUp);
			}
		}
		System.out.println("Exact Search1: "+ getResultList);
		Collections.sort(getResultList);
		
		System.out.println("Exact Search: "+ getResultList);
		return getResultList;
	}

	/**
	 * Process of Exact Search
	 * 
	 * @param queryWord
	 * @param getResultList
	 * @param findUp
	 */
	private void searchProcess(String queryWord, ArrayList<Result> getResultList, Map<String, Result> findUp) {
		int count = 0;
		int TotalWords = 0;
		double score = 0;
		for (String location : this.finalIndex.get(queryWord).keySet()) {
			if (findUp.containsKey(location)) {
				findUp.get(location).updateCount(this.wordCount(queryWord, location));
			} else {
				count = finalIndex.get(queryWord).get(location).size();
				TotalWords = wordCount.get(location);
				score = (double) count / TotalWords;
				Result newResult = new Result(location, score, count, TotalWords);
				getResultList.add(newResult);
				findUp.put(location, newResult);
			}
		}
	}

	/**
	 * Partial search for query word
	 * 
	 * @param queryLine
	 * @return Partial Search for query words
	 */
	public ArrayList<Result> PartialSearch(Set<String> queryLine) {
		ArrayList<Result> getResultList = new ArrayList<>();
		Map<String, Result> lookUp = new HashMap<>();
		for (String queryWord : queryLine) {
			this.partialSearchHelper(queryWord, getResultList, lookUp);
		}
		Collections.sort(getResultList);
		return getResultList;
	}
	/**
	 * Partial Search process 
	 * @param word
	 * @param result
	 * @param lookUp
	 */
	private void partialSearchHelper(String word, ArrayList<Result> result, Map<String,Result> lookUp) {
		System.out.print("Partial Search\n");
		for(String exist : this.finalIndex.tailMap(word).keySet()) {
			if(exist.startsWith(word)) {
				this.searchProcess(exist, result, lookUp);
			}else {
				break;
			}
		}
	}

	/**
	 * Output finalIndex
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void nestJSON(Path path) throws IOException {
		PrettyJSONWriter.asNestedStructure(this.finalIndex, path);
	}

	/**
	 * Output location format
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void locationsJSON(Path path) throws IOException {
		PrettyJSONWriter.asObject(this.wordCount, path);
	}

	/**
	 * find the number of words stored in finalIndex
	 * 
	 * @return returns the number of words stored by inverted index
	 */
	public int wordCount() {
		return this.finalIndex.size();
	}

	/**
	 * Tests whether the index contains the specified word.
	 *
	 * @param word key word to look for
	 * @return true if the word is stored in the index
	 */
	public boolean contains(String word) {
		System.out.print("COndgdzg\n");
		return this.finalIndex.containsKey(word);
	}

	/**
	 * Judge finalIndex contains this word with this specific location or not
	 * 
	 * @param word
	 * @param location
	 * @return true if finalIndex contains this word with this specific location
	 */
	public boolean contains(String word, String location) {
		return this.finalIndex.containsKey(word) && this.finalIndex.get(word).containsKey(location);
	}

	/**
	 * Get the number of words found for a specific location
	 * 
	 * @param location
	 * @return the number of words found for a specific location in wordCount
	 */
	public int wordCount(String location) {
		return wordCount.get(location);
	}

	/**
	 * the number of times the word was found at that location
	 * 
	 * @param word
	 * @param location
	 * @return the number of times the word was found at that location from
	 *         finalIndex
	 */
	public int wordCount(String word, String location) {
		if (this.contains(word, location)) {
			return this.finalIndex.get(word).get(location).size();
		} else {
			return 0;
		}
	}

	/**
	 * Having wordCount
	 * 
	 * @return the wordCount structure
	 */
	public TreeMap<String, Integer> getwordCount() {
		return this.wordCount;
	}

	/**
	 * Having the finalIndex
	 * 
	 * @return the finalIndex structure
	 */
	public TreeMap<String, TreeMap<String, TreeSet<Integer>>> getfinalIndex() {
		return this.finalIndex;
	}

	/**
	 * the number of locations stored
	 * 
	 * @return the number of locations stored by wordCount
	 */
	public int locationCount() {
		return wordCount.size();
	}

	/**
	 * the number of locations stored that specific word
	 * 
	 * @param word
	 * @return the number of locations stored by finalIndex for that specific word
	 */
	public int locationCount(String word) {
		if (this.finalIndex.containsKey(word)) {
			return this.finalIndex.get(word).size();
		} else {
			return 0;
		}
	}

}
