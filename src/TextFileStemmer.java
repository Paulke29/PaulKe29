import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.io.BufferedReader;
import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

import java.nio.charset.StandardCharsets;
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
	 * Output a list of query words
	 * 
	 * @param queryfile
	 * @param stemmer
	 * @return a list of query words
	 */
	public static ArrayList<Set<String>> QuerystemLine(Path queryfile, Stemmer stemmer) {
		ArrayList<Set<String>> answer = new ArrayList<>();

		try (BufferedReader readline = Files.newBufferedReader(queryfile, StandardCharsets.UTF_8)) {
			String line = null;
			Set<String> QuerySet = null;
			Set<String> SetQuery = null;
			while ((line = readline.readLine()) != null) {
				String string2 = TextParser.clean(line.trim());
				QuerySet = new TreeSet<>();
				QuerySet.add(string2);
				if (!QuerySet.isEmpty()) {
					for (String string3 : QuerySet) {
						if (!string3.isBlank()) {
							SetQuery = new TreeSet<>();
							for (String String4 : TextParser.parse(string3)) {
								SetQuery.addAll(stemLine(String4));
							}
							answer.add(SetQuery);
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return answer;
	}

	/**
	 * Stem words to a set of query words
	 * 
	 * @param queryfile
	 * @return a set of query words
	 */
	public static ArrayList<Set<String>> QuerystemLine2(Path queryfile) {
		return QuerystemLine(queryfile, new SnowballStemmer(DEFAULT));
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
	public static ArrayList<String> stemLine(String line, Stemmer stemmer) {
		ArrayList<String> answer = new ArrayList<>();
		for (String words : TextParser.parse(line)) {
			answer.add(stemmer.stem(words).toString());
		}
		return answer;
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
