import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;
import java.io.BufferedReader;
import java.io.FileReader;
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
	public static TreeSet<String> stemLine(String line) {
		// THIS IS PROVIDED FOR YOU; NO NEED TO MODIFY
		return stemLine(line, new SnowballStemmer(DEFAULT));
	}

	/**
	 * Returns a set of cleaned and stemmed words parsed from the provided line.
	 *
	 * @param line    the line of words to clean, split, and stem
	 * @param stemmer the stemmer to use
	 * @return a sorted set of cleaned and stemmed words
	 *
	 * @see Stemmer#stem(CharSequence)
	 * @see TextParser#parse(String)
	 */
	public static TreeSet<String> stemLine(String line, Stemmer stemmer) {
		// TODO Fill in
		TreeSet answer = new TreeSet<>();
		for(String words: TextParser.parse(line)) {
			answer.add(stemmer.stem(words));
		}
		return answer;
//		throw new UnsupportedOperationException("Not yet implemented.");
	}

	/**
	 * Reads a file line by line, parses each line into cleaned and stemmed words,
	 * and then adds those words to a set.
	 *
	 * @param inputFile  the input file to parse
	 * @return a sorted set of stems from file
	 * @throws IOException if unable to read or parse file
	 *
	 * @see #stemLine(String)
	 * @see TextParser#parse(String)
	 */
	public static TreeSet<String> stemFile(Path inputFile) throws IOException {
		// TODO Fill in
		TreeSet<String> answer = new TreeSet<>();
		try (BufferedReader read_line = Files.newBufferedReader(inputFile)){
		    String line = null;
			while ((line = read_line.readLine()) != null) {
				for(String words: TextParser.parse(line)) {
					answer.addAll(stemLine(words));
					}
				
				}
			}catch(IOException e) {
			 e.printStackTrace();
		}
		return answer;
		
//		throw new UnsupportedOperationException("Not yet implemented.");
	}

	/**
	 * A simple main method that demonstrates this class.
	 *
	 * @param args unused
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Modify (or remove) this method as necessary to debug.

		String text = "practic practical practice practiced practicer practices "
				+ "practicing practis practisants practise practised practiser "
				+ "practisers practises practising practitioner practitioners";

		System.out.println(stemLine(text));

//		Path inputPath = Paths.get("test", "animals.text");
		Path inputPath = Paths.get("test", "rfc475.txt");
		Set<String> actual = TextFileStemmer.stemFile(inputPath);
		System.out.println(actual);
	}
}
