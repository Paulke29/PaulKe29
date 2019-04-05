import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

/*
 * TODO Blank lines are a problem in multiple files. Try to be consistent, and
 * use them to break up different blocks of code. I'll point out a few places.
 * I already warned you about this: https://github.com/usf-cs212-spring2019/project-Paulke29/blob/081fa8f0a286ad3592322b2859c60691b63a0cfe/src/ArgumentMap.java#L6
 * 
 * You also need to fix your Javadoc in most of your files. I already warned you
 * about this: https://github.com/usf-cs212-spring2019/project-Paulke29/blob/2a7c9a5695d29f2202179a305145cc90cd905e0f/src/InvertedIndexBuilder.java#L6-L8
 * 
 * Go through EVERY file. Every line. Every comment. You risk failing this class
 * because of formatting and comments! 
 */

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
		Instant start = Instant.now();
		ArgumentMap argumentMap = new ArgumentMap(args);
		InvertedIndex wordindex = new InvertedIndex();
		InvertedIndexBuilder invertedIndexBuilder = new InvertedIndexBuilder();
		if (argumentMap.hasFlag("-path")) { // TODO Blank line BEFORE here
			try {
				if (argumentMap.hasValue("-path")) {
					Path path = argumentMap.getPath("-path");
					invertedIndexBuilder.filesIndex(TextFileFinder.list(path), wordindex);
				}
			} catch (IOException e) {
				System.out.println("Couldn't to print index from path");
			}
		}
		if (argumentMap.hasFlag("-index")) {  // TODO Blank line BEFORE here
			Path indexPath = argumentMap.getPath("-index", Path.of("index.json"));
			try {
				wordindex.nestJSON(indexPath);
			} catch (IOException e) {
				System.out.println("Couldn't get anything from path: " + indexPath);
			}

		}  // TODO NO blank line BEFORE here
		if (argumentMap.hasFlag("-locations")) {  // TODO Blank line BEFORE here
			Path locationPath = argumentMap.getPath("-locations", Path.of("locations.json"));
			try {
				wordindex.locationsJSON(locationPath);
			} catch (IOException e) {
				System.out.println("Couldn't get anything  from path: " + locationPath);
			}
		}
		Duration elapsed = Duration.between(start, Instant.now());
		double seconds = (double) elapsed.toMillis() / Duration.ofSeconds(1).toMillis();
		System.out.printf("Elapsed: %f seconds%n", seconds);
	}
}
