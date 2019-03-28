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
	HashMap<String, HashSet<Integer>> answer = new HashMap<>();
	HashSet<Integer> index;
	
	private TreeMap<String, TreeMap<String, TreeSet<Integer>>> wordsindex; // TODO final, refactor to just index

	/**
	 * TODO
	 * @param words
	 * @param file
	 */
	public InvertedIndex() {
		wordsindex = new TreeMap<>();
	}

	/**
	 * @param file
	 * @return index(file)
	 * @throws IOException
	 */
	public TreeMap<String, TreeMap<String, TreeSet<Integer>>> getWordsindex(Path file) throws IOException {
		return index(file);
	}
	
	/*
	 * TODO Separate out data structore/storage logic from building logic.
	 * 
	 * Create an InvertedIndexBuilder that parses files and adds the words to an index.
	 * 
	 * create a add(String text, String location, int position)
	 * 
	 * Hopefully this allows you to avoid breaking encapsulation, but if still breaks its okay just ask for help.
	 */

	/**
	 * @param file
	 * @return wordcount class
	 * @throws IOException
	 */
	public TreeMap<String, Integer> getWordcount(Path file) throws IOException {
		return wordcount(file);
	}

	/**
	 * @param file
	 * @return the index of words in file
	 * @throws IOException
	 */
	public TreeMap<String, TreeMap<String, TreeSet<Integer>>> index(Path file) throws IOException {

		int number = 1;
		if (file == null) {
			wordsindex = null;
		}
		if (file != null && (file.getFileName().toString().toLowerCase().endsWith("text")
				|| file.getFileName().toString().toLowerCase().endsWith("txt"))) {
			for (String words : TextFileStemmer.stemFile(file)) {
				if (!wordsindex.containsKey(words)) {
					TreeSet<Integer> position = new TreeSet<>();
					position.add(number);
					TreeMap<String, TreeSet<Integer>> textindex = new TreeMap<>();
					textindex.put(file.toString(), position);
					wordsindex.put(words, textindex);
				} else {

					if (!wordsindex.get(words).containsKey(file.toString())) {
						TreeSet<Integer> position = new TreeSet<>();
						position.add(number);
						wordsindex.get(words).put(file.toString(), position);

					}
					if (!wordsindex.get(words).get(file.toString()).contains(number)) {
						wordsindex.get(words).get(file.toString()).add(number);
					}
				}
				number++;

			}
		}
		return wordsindex;
	}

//	/**
//	 * @param file
//	 * @return the number of words
//	 * @throws IOException
//	 */
//	public TreeMap<String, Integer> wordcount(Path file) throws IOException {
//		TreeMap<String, Integer> counting = new TreeMap<>();
//		HashSet<Path> files = new HashSet<>();
//		files.addAll(TraverseDirectory.traversefiles(file));
//
//		for (Path counting_words : files) {
//			int number = 0;
//			if (counting_words.getFileName().toString().toLowerCase().endsWith("text")
//					|| counting_words.getFileName().toString().toLowerCase().endsWith("txt")) {
//				for (String words : TextFileStemmer.stemFile(counting_words)) {
//					number++;
//				}
//				if (number != 0) {
//					counting.put(counting_words.toString(), number);
//				}
//
//			}
//
//		}
//
//		return counting;
//
//	}

	public boolean add(String element, int position) {
		if (!answer.containsKey(element)) {
			index = new HashSet<>();
			index.add(position);
			answer.put(element, index);
			return true;
		} else {
			if (answer.containsKey(element) && !answer.get(element).contains(position)) {
				answer.get(element).add(position);
				return true;
			} else {
				return false;
			}
		}
	}


	public int numPositions(String element) {
		if (!answer.containsKey(element)) {
			return 0;
		} else {
			if (answer.get(element) == null) {
				return 0;
			} else {

				return index.size();
			}

		}

	}


	public int numElements() {

		return answer.keySet().size();
	}


	public boolean contains(String element) {
		if (answer.containsKey(element)) {
			return true;
		}
		return false;
	}

	
	/**
	 * @param element
	 * @param position
	 * @return
	 */
	public boolean contains(String element, int position) {
		if (!answer.containsKey(element)) {
			return false;
		} else {
			for (Integer a : answer.get(element)) {
				if (a == position) {
					return true;
				}
			}

		}
		return false;

	}

	
	/**
	 * @return get elements
	 */
	public Collection<String> getElements() {
		try {
			HashSet<String> elements = new HashSet<>();
			elements.addAll(answer.keySet());
			Collection<String> newlist = Collections.unmodifiableCollection(elements);
			return newlist;
		} catch (UnsupportedOperationException e) {
			@SuppressWarnings("unchecked")
			Collection<String> e2 = (Collection<String>) e;
			return e2;
		}
	}

	
	/**
	 * @param element
	 * @return the position as collection
	 */
	public Collection<Integer> getPositions(String element) {

		try {
			HashSet<Integer> position = new HashSet<>();
			if (!answer.containsKey(element)) {
				Collection<Integer> newlist = Collections.unmodifiableCollection(position);
				return newlist;
			} else {
				for (Integer positions : answer.get(element)) {
					position.add(positions);
				}
				Collection<Integer> newlist = Collections.unmodifiableCollection(position);
				return newlist;

			}
		} catch (UnsupportedOperationException e) {
			@SuppressWarnings("unchecked")
			Collection<Integer> e2 = (Collection<Integer>) e;
			return e2;
		}
	}

}
