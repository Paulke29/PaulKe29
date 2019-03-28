import java.io.IOException;
import java.nio.file.Path;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * A special type of {@link Index} that indexes the locations words were found.
 */

public class InvertedIndex {
	private TreeMap<String, TreeMap<String, TreeSet<Integer>>> finalindex;
	private TreeMap<String, Integer> wordcount;

	/**
	 * initial TreeMap
	 * @param words
	 * @param file
	 */
	public InvertedIndex() {
		finalindex = new TreeMap<>();
		wordcount = new TreeMap<>();
	}

	/**
	 * @param words    String words
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
	 * Calculating the number of words for every single file
	 * @param file
	 * @param number
	 */
	public void wordcount(String file, int number) {
		if (number > 0) {
			wordcount.put(file, number);
		}
	}


	/**
	 * Output finalIndex 
	 * @param path
	 * @throws IOException
	 */
	public void toJSON(Path path) throws IOException {
		PrettyJSONWriter.asNestedStructure(this.finalindex, path);
	}

	/**
	 * Output location format
	 * @param path
	 * @throws IOException
	 */
	public void locationsJSON(Path path) throws IOException {
		PrettyJSONWriter.asObject(this.wordcount, path);
	}
}
