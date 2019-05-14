
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

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
		InvertedIndex wordIndex;
		InvertedIndexBuilder invertedIndexBuilder;
		QueryFileParserInterface results;
		int threads = 0;
		WebCrawler crawler = null;
		if (argumentMap.hasFlag("-threads")) {
			String numThreads = argumentMap.getString("-threads", "5");
			try {
				threads = Integer.parseInt(numThreads);
			} catch (NumberFormatException e) {
				threads = 5;
			}
			if(threads <1) {
				threads =5;
			}
			wordIndex = new ThreadSafeIndex();
			results = new ThreadSafeQueryFileParser((ThreadSafeIndex) wordIndex, threads);
			invertedIndexBuilder = new ThreadSafeInvertedIndexBuilder((ThreadSafeIndex) wordIndex, threads);
		} else {
			wordIndex = new InvertedIndex();
			results = new QueryFileParser(wordIndex);
			invertedIndexBuilder = new InvertedIndexBuilder(wordIndex);
		}
		if (argumentMap.hasFlag("-url")) {
			String Seed = argumentMap.getString("-url");
			int limit;
			URL seed = null;
			try {
				seed = new URL(Seed);
				limit = Integer.parseInt(argumentMap.getString("-limit", "50"));
			} catch (MalformedURLException e) {
				System.err.println("Illegal url: " + Seed + " please check your argument");
				return;
			} catch (NumberFormatException e) {
				System.err.println("Illegal limit number: " + argumentMap.getString("-limit"));
				return;
			}
			crawler = new WebCrawler((ThreadSafeIndex) wordIndex, threads);
			crawler.craw(seed, limit);

		}
		if (argumentMap.hasFlag("-path")) {
			try {
				if (argumentMap.hasValue("-path")) {
					Path path = argumentMap.getPath("-path");
					invertedIndexBuilder.build(path);
				}
			} catch (IOException e) {
				System.out.println("Couldn't to print index from path");
			}
		}
		if (argumentMap.hasFlag("-query")) {
			if (argumentMap.hasValue("-query")) {
				Path query = argumentMap.getPath("-query");
				try {
					boolean exact = false;
					if (argumentMap.hasFlag("-exact")) {
						exact = true;
					}
					results.parseFile(query, exact);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (argumentMap.hasFlag("-index")) {

			Path indexPath = argumentMap.getPath("-index", Path.of("index.json"));
			try {
				wordIndex.nestJSON(indexPath);
			} catch (IOException e) {
				System.out.println("Couldn't get anything from path: " + indexPath);
			}

		}
		if (argumentMap.hasFlag("-locations")) {

			Path locationPath = argumentMap.getPath("-locations", Path.of("locations.json"));
			try {
				wordIndex.locationsJSON(locationPath);
			} catch (IOException e) {
				System.out.println("Couldn't get anything  from path: " + locationPath);
			}
		}
		if (argumentMap.hasFlag("-results")) {
			Path result = argumentMap.getPath("-results", Path.of("results.json"));
			try {
				results.toJSON(result);
			} catch (IOException e) {
				System.out.println(e);
			}
		}

		Duration elapsed = Duration.between(start, Instant.now());
		double seconds = (double) elapsed.toMillis() / Duration.ofSeconds(1).toMillis();
		System.out.printf("Elapsed: %f seconds%n", seconds);
	}
}