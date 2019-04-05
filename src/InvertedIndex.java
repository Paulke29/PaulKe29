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
		/*
		 * TODO There is a null pointer exception that happens if either
		 * word or location are not in finalIndex. When those two aren't in the
		 * index, you should return 0 instead of cause a null pointer exception.
		 */
		return finalIndex.get(word).get(location).size();
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
		// TODO Null pointer.
		return finalIndex.get(word).size();
	}
	
	/*
	 * TODO You have some of the count methods I suggested here, but none of the
	 * other types of methods I suggested in the Piazza post. If you are unclear
	 * ASK FOR HELP. But this class needs more general methods to be fully 
	 * reusable.
	 */
}
