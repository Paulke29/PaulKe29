import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;

// TODO Address warnings, including Javadoc. I won't keep reviewing code with warnings.
// TODO Still too much code in Driver

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
		InvertedIndex wordindex = new InvertedIndex();
		TraverseDirectory traversefile = new TraverseDirectory();
		PrettyJSONWriter format = new PrettyJSONWriter();
		Path path = null;
		Path index = null;
		Path location = null;
		TreeMap<String, TreeMap<String, TreeSet<Integer>>> filesindex = new TreeMap<>();
		InvertedIndexBuilder  invertedIndexBuilder = new InvertedIndexBuilder();
		if (argumentMap.hasFlag("-path")) {
			try {
				if (argumentMap.hasValue("-path")) {
					path = argumentMap.getPath("-path");
					invertedIndexBuilder.filesIndex(TextFileFinder.list(path));
				} else {
					path = argumentMap.getPath("-path");
					invertedIndexBuilder.filesIndex(TextFileFinder.list(path));
				}
			} catch (IOException e) {
				System.out.println("Invalid path");
			}

		}
		if (argumentMap.hasFlag("-index")) {
			try {
				if (argumentMap.hasValue("-index")) {
					index = argumentMap.getPath("-index");
					format.asNestedObject_file(filesindex, index); // TODO Not naming things properly in Java
				} else {
					index = Paths.get("index.json");
					format.asNestedObject_file(filesindex, index);
				}
			} catch (IOException e) {
				System.out.println("Invalide index");
			}

		}
		if (argumentMap.hasFlag("-locations")) {
			try {
				if (argumentMap.hasValue("-locations")) {
					location = argumentMap.getPath("-locations");
					PrettyJSONWriter.location_format(wordindex.getWordcount(path), location);
				} else {
					location = argumentMap.getPath("locations.json");
					if (Files.isDirectory(path) == false) {
						PrettyJSONWriter.location_format(wordindex.getWordcount(path), location);
					} else {
						for (Path file : traversefile.getDirectory(path)) {
							PrettyJSONWriter.location_format(wordindex.getWordcount(file), location);
						}
					}
				}
			} catch (IOException e) {
				System.out.println("Invalid location");
			}

		}


		System.out.println(Arrays.toString(args));

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
