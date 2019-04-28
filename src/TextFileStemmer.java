import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;
import java.io.BufferedReader;
import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;
import java.nio.file.Files;

/**
 * Utility class for parsing and stemming text and text files into sets of
 * stemmed words.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Spring 2019
 *
 * @see TextParser
 */
public class TextFileStemmer {

	/** The default stemmer algorithm used by this class. */
	public static final SnowballStemmer.ALGORITHM DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;

	/**
	 * Returns a set of cleaned and stemmed words parsed from the provided line.
	 * Uses the {@link #DEFAULT} algorithm for stemming.
	 *
	 * @param line the line of words to clean, split, and stem
	 * @return a sorted set of cleaned and stemmed words
	 *
	 * @see SnowballStemmer
	 * @see #DEFAULT
	 * @see #stemLine(String, Stemmer)
	 */
	public static ArrayList<String> stemLine(String line) {
		return stemLine(line, new SnowballStemmer(DEFAULT));
	}

	/**
	 * clean the queryWord and add to collection structure
	 * 
	 * @param line      line with unclean queryWords
	 * @param stemmer   get the stem of each words from queryLine
	 * @param container data structure to add the clean queryWords
	 */
	public static void stemLine(String line, Stemmer stemmer, Collection<String> container) {
		for (String words : TextParser.parse(line)) {
			container.add(stemmer.stem(words).toString());
		}
	}

	/**
	 * clean the queryWord and add to ArrayList
	 * 
	 * @param line    single line from source file
	 * @param stemmer helping get the stem of the word
	 * @return a arrayList contains stemmed and clean queryWord
	 */
	public static ArrayList<String> stemLine(String line, Stemmer stemmer) {
		ArrayList<String> container = new ArrayList<>();
		stemLine(line, stemmer, container);
		return container;
	}

	/**
	 * clean the queryWord and add to Set
	 * 
	 * @param line    single line from source file
	 * @param stemmer helping get the stem of the word
	 * @return a arrayList contains stemmed and clean queryWord
	 */
	public static TreeSet<String> uniqueStems(String line, Stemmer stemmer) {
		TreeSet<String> container = new TreeSet<>();
		stemLine(line, stemmer, container);
		return container;
	}

	/**
	 * Stem words to a set of query words
	 * 
	 * @param queryfile the source file
	 * @return a set of query words getting from query file
	 */
	public static TreeSet<String> uniqueStems(String queryfile) {
		return uniqueStems(queryfile, new SnowballStemmer(DEFAULT));
	}

	/**
	 * Reads a file line by line, parses each line into cleaned and stemmed words,
	 * and then adds those words to a set.
	 *
	 * @param inputFile the input file to parse
	 * @return a sorted set of stems from file
	 * @throws IOException if unable to read or parse file
	 *
	 * @see TextParser#parse(String)
	 */
	public static ArrayList<String> stemFile(Path inputFile) throws IOException {
		ArrayList<String> answer = new ArrayList<>();
		try (BufferedReader read_line = Files.newBufferedReader(inputFile)) {
			String line = null;
			while ((line = read_line.readLine()) != null) {
				for (String words : TextParser.parse(line)) {
					answer.addAll(stemLine(words));
				}
			}
		}
		return answer;
	}

	/**
	 * A simple main method that demonstrates this class.
	 *
	 * @param args unused
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		String text = "practic practical practice practiced practicer practices "
				+ "practicing practis practisants practise practised practiser "
				+ "practisers practises practising practitioner practitioners";

		System.out.println(stemLine(text));

	}
}
