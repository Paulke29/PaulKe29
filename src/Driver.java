import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

// TODO Fix formatting and old comments---don't resubmit until that is done.

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
	 * 
	 * Initializes the classes necessary based on the provided command-line
	 * arguments. This includes (but is not limited to) how to build or search an
	 * inverted index.
	 *
	 * @param args flag/value pairs used to start this program
	 *   
	 */
	public static void main(String[] args) {
		// store initial start time
		Instant start = Instant.now();
		// TODO Modify this method as necessary.
		ArgumentMap argumentMap = new ArgumentMap(args);
		InvertedIndex wordindex = new InvertedIndex();
		Path path = null; // TODO Declare this where you define it
		Path index = null; // TODO Declare this where you define it
		Path location = null; // TODO Declare this where you define it
		InvertedIndexBuilder  invertedIndexBuilder = new InvertedIndexBuilder(wordindex);
		if (argumentMap.hasFlag("-path")){
			try {
				if (argumentMap.hasValue("-path")) {
					path = argumentMap.getPath("-path");
					invertedIndexBuilder.filesIndex(TextFileFinder.list(path));
				}
			} catch (IOException e) {
				// TODO Need to be more descriptive
				System.out.println("Invalid path");
			}

		}
		if (argumentMap.hasFlag("-index")) {
			try {
				// TODO Use argumentMap methods to simplify this...
				// Path path = argumentMap.getPath("-index", Path.of("index.json"));
				if (argumentMap.hasValue("-index")) {
					index = argumentMap.getPath("-index");
					wordindex.nestJSON(index);
				} else {
					index = Paths.get("index.json");
					wordindex.nestJSON(index);
				}
			} catch (IOException e) {
			// TODO Need to be more descriptive
				System.out.println("Invalide index");
			}

		}
		if (argumentMap.hasFlag("-locations")) {
			// TODO Same comments as above.
			try {
				if (argumentMap.hasValue("-locations")) {
					location = argumentMap.getPath("-locations");
					wordindex.locationsJSON(location);
				} else {
					location = argumentMap.getPath("locations.json");
					wordindex.locationsJSON(location);
				}
			} catch (IOException e) {
				System.out.println("Invalid location");
			}

		}


		System.out.println(Arrays.toString(args)); // TODO Remove

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
