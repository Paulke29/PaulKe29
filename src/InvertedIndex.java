import java.io.IOException;
import java.nio.file.Path;
import java.util.TreeMap;
import java.util.TreeSet;



/**
 * @author paulke
 *
 */
public class InvertedIndex {
	// TODO Fix variable names, use camelCase or change to 1 word names
	// TODO For example, finalindex becomes "finalIndex" or just "index"
	// TODO Make both of these members final.
	
	/**
	 * creating a dataStructure for index
	 */
	private TreeMap<String, TreeMap<String, TreeSet<Integer>>> finalindex;
	/**
	 * creating a dataStructure for word count
	 */
	private TreeMap<String, Integer> wordcount;

	/**
	 * initial TreeMap
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
		/*
		 * TODO This method can be simplified... notice the repeated code?
		 * You are adding a position in all of the if/else if/else blocks below.
		 * 
		 * So, to simplify, try working backwards. Start with this:
		 * 
		 * finalindex.get(words).get(location).add(position);
		 * 
		 * And then work backwards to make sure that code will always work. For example,
		 * if finalindex.get(words).get(location) is null, then you need to init the
		 * inner tree set.
		 */
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

	/*
	 * TODO Can't actually have add and wordcount as separate methods. Then it is
	 * possible for the word count to not match what you have stored in your index.
	 * For example, nothing is stopping other code from doing something like:
	 * 
	 * wordcount("hello.txt", -12);
	 * 
	 * So I suggest you embed updating the word count in the add method for data
	 * integrity. So if you actually add something to your index (i.e. it is not a
	 * duplicate), then increase the count for that location by 1.
	 */

	/**
	 * Output finalIndex 
	 * @param path
	 * @throws IOException
	 */
	public void nestJSON(Path path) throws IOException {
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
	
	// TODO See Piazza for the additional type of methods to add to this class 
	// to make it more generally useful.
}
