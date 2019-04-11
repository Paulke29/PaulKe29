import java.io.IOException;
import java.nio.file.Path;
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
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> finalIndex;
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
	 * 
	 * @param words
	 * @param location
	 * @param position
	 */
	public void MutileThreadAdd(String words, String location, int position) {
		synchronized (this.finalIndex) {
			while (this.finalIndex.isEmpty()) {
				try {
					this.finalIndex.wait();
				} catch (InterruptedException e) {
				}
			}
			add(words, location, position);
			this.finalIndex.notifyAll();
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
		return this.finalIndex.containsKey(word);
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
	public boolean wordCount(String word, String location) {
		return this.finalIndex.containsKey(word) && this.finalIndex.get(word).containsKey(location);
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
