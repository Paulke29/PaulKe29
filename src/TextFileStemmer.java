import java.io.IOException;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Set;
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
	 * @param queryfile
	 * @param stemmer
	 * @return Returns a set of cleaned and stemmed words parsed from the provided line.
	 */
	public static ArrayList<Set<String>>QuerystemLine(Path queryfile, Stemmer stemmer) {
		ArrayList<Set<String>> answer = new ArrayList<>();

		try (BufferedReader readline = Files.newBufferedReader(queryfile)) {
			String line = null;
			Set<String> QuerySet = null;
//			Set<String> SetQuery = new TreeSet<>();
			while ((line = readline.readLine()) != null) {
//				System.out.println("Line: " + line);
				String string2 = TextParser.clean(line);
//				System.out.println("string2: " + string2);
				QuerySet = new TreeSet<>();
				QuerySet.add(string2);
				if(!QuerySet.isEmpty()) {
					answer.add(QuerySet);
				}
			}
//			System.out.println("ArrayList: " + answer);
//			System.exit(2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return answer;
	}
	public static ArrayList<Set<String>> QuerystemLine2(Path queryfile) {
		// THIS IS PROVIDED FOR YOU; NO NEED TO MODIFY
		return QuerystemLine(queryfile, new SnowballStemmer(DEFAULT));
	}
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
	
	public static ArrayList<String> stemLine2(String line) {
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
	public static ArrayList<String> stemLine(String line, Stemmer stemmer) {
		// TODO Fill in
		ArrayList<String>answer = new ArrayList<>();
		for(String words: TextParser.parse(line)) {
			answer.add((String) stemmer.stem(words));
		}
		return answer;

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
	public static ArrayList<String> stemFile(Path inputFile) throws IOException {
		// TODO Fill in
		ArrayList<String> answer = new ArrayList<>();
		try (BufferedReader readline = Files.newBufferedReader(inputFile)){
		    String line = null;
			while ((line = readline.readLine()) != null) {
				for(String words: TextParser.parse(line)) {
					answer.addAll(stemLine2(words));
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

		System.out.println(stemLine2(text));

//		Path inputPath = Paths.get("test", "animals.text");
//		Path inputPath = Paths.get("test", "rfc475.txt");
//		ArrayList<String> actual = TextFileStemmer.stemFile(inputPath);
//		
//		System.out.println(actual);
	}
}
