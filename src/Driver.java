import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Class responsible for running this project based on the provided command-line
 * arguments. See the README for details.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Spring 2019
 */
public class Driver {
	/**
	 * Initializes the classes necessary based on the provided command-line
	 * arguments. This includes (but is not limited to) how to build or search an
	 * inverted index.
	 *
	 * @param args flag/value pairs used to start this program
	 * @throws IOException
	 */
	public static void main(String[] args) {
		// store initial start time
		Instant start = Instant.now();
		// TODO Modify this method as necessary.

		ArgumentMap argumentMap = new ArgumentMap(args);
		WordIndex wordindex = new WordIndex();
		TraverseDirectory traversefile = new TraverseDirectory();
		PrettyJSONWriter format = new PrettyJSONWriter();
		searchResult Search;
		Path path = null;
		Path index = null;
		Path location = null;
		Path query = null;
		Path result = null;
		TreeMap<String, TreeMap<String, TreeSet<Integer>>> filesindex = new TreeMap<>();
		TreeMap<String, Integer> WordsCount = new TreeMap<>();
		TreeMap<String, ArrayList<Result>>SearchResult = new TreeMap<>();
		if (argumentMap.hasFlag("-path")) {
			if (argumentMap.hasValue("-path")) {
				path = argumentMap.getPath("-path");
				try {
					if (Files.isDirectory(path) == false) {
						filesindex.putAll(wordindex.getWordsindex(path));
						WordsCount = wordindex.getWordcount(path);

					} else {
						for (Path file : traversefile.getDirectory(path)) {
							filesindex.putAll(wordindex.getWordsindex(file));
							WordsCount = wordindex.getWordcount(path);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();

				}
			} else {
				try {
					path = argumentMap.getPath("-path");
					filesindex = wordindex.getWordsindex(path);
				} catch (IOException e) {
					System.out.println(e);
				}

			}
		}
		if (argumentMap.hasFlag("-index")) {
			if (argumentMap.hasValue("-index")) {
				index = argumentMap.getPath("-index");
				try {
					format.asNestedObject_file(filesindex, index);
				} catch (IOException e) {
					System.out.println(e);
				}
			} else {
				index = Paths.get("index.json");
				try {
					format.asNestedObject_file(filesindex, index);
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}
		if (argumentMap.hasFlag("-locations")) {
			if (argumentMap.hasValue("-locations")) {
				location = argumentMap.getPath("-locations");
				try {
					PrettyJSONWriter.location_format(wordindex.getWordcount(path), location);
				} catch (IOException e) {
					System.out.println(e);
				}
			} else {
				try {
					location = argumentMap.getPath("locations.json");
					if (Files.isDirectory(path) == false) {
						PrettyJSONWriter.location_format(wordindex.getWordcount(path), location);
					} else {
						for (Path file : traversefile.getDirectory(path)) {
							PrettyJSONWriter.location_format(wordindex.getWordcount(file), location);
						}
					}

				} catch (IOException e) {
					System.out.println(e);
				}

			}
		}
		if (argumentMap.hasFlag("-query")) {
			if (argumentMap.hasValue("-query")) {
				query = argumentMap.getPath("-query");
				try {
					if (argumentMap.hasFlag("-exact")) {
						searchResult ResultSeatch = new searchResult(true, query, WordsCount, filesindex);
						SearchResult = ResultSeatch.getSearchResult(true, query, WordsCount, filesindex);
					} else {
						searchResult ResultSeatch = new searchResult(true, query, WordsCount, filesindex);
						SearchResult = ResultSeatch.getSearchResult(false, query, WordsCount, filesindex);
					}

				} catch (IOException e) {
//					System.out.println(e);
					e.printStackTrace();
				}
			}
		}
//		System.exit(0);
		if (argumentMap.hasFlag("-results")) {
			System.out.println("Result");
			if (argumentMap.hasValue("-results")) {
				result = argumentMap.getPath("-results");
				try {
					System.out.println("Format result: "+ SearchResult.toString());
					PrettyJSONWriter.Rearchformat(SearchResult, result);
				} catch (IOException e) {
					System.out.println(e);
				}
			} else {
				result = Paths.get("results.json");
				try {
					PrettyJSONWriter.Rearchformat(SearchResult, result);
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}
		System.out.println(Arrays.toString(args));

		// calculate time elapsed and output
		Duration elapsed = Duration.between(start, Instant.now());
		double seconds = (double) elapsed.toMillis() / Duration.ofSeconds(1).toMillis();
		System.out.printf("Elapsed: %f seconds%n", seconds);
	}

	/*
	 * Generally, "driver" classes are responsible for setting up and calling other
	 * classes, usually from a main() method that parses command-line parameters. If
	 * the driver were only responsible for a single class, we use that class name.
	 * For example, "PizzaDriver" is what we would name a driver class that just
	 * sets up and calls the Pizza class.
	 */
}
