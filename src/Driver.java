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
		if (argumentMap.hasFlag("-path")) {
			if (argumentMap.hasValue("-path")) {
				path = argumentMap.getPath("-path");
				try {
					if (Files.isDirectory(path) == false) { // TODO Some of this might move into your builder class
						filesindex.putAll(wordindex.getWordsindex(path));
					} else {
						for (Path file : traversefile.getDirectory(path)) {
							filesindex.putAll(wordindex.getWordsindex(file));
						}
					}
				} catch (IOException e) {
					System.out.println(e); // TODO Fix exception handling
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
					format.asNestedObject_file(filesindex, index); // TODO Not naming things properly in Java
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
		if(argumentMap.hasFlag("-locations")) {
			if(argumentMap.hasValue("-locations")) {
				location = argumentMap.getPath("-locations");
				try {
					PrettyJSONWriter.location_format(wordindex.getWordcount(path),location);
				}catch (IOException e) {
					System.out.println(e);
				}
			}
			else {
				try {
					location = argumentMap.getPath("locations.json");
					if(Files.isDirectory(path) == false) {
						PrettyJSONWriter.location_format(wordindex.getWordcount(path),location);
					}
					else {
						for(Path file : traversefile.getDirectory(path)){
			        		PrettyJSONWriter.location_format(wordindex.getWordcount(file),location);
			        	}
					}
					
				}catch (IOException e) {
					System.out.println(e);
				}
				
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
