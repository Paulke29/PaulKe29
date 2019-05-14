import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Creating mulitthreading for search words
 * 
 * @author PaulKe
 *
 */
public class ThreadSafeQueryFileParser implements QueryFileParserInterface {

	/**
	 * Initial variable InvertedIndex Object
	 */
	private final ThreadSafeIndex index;

	/**
	 * initial the data structure
	 */
	private final TreeMap<String, ArrayList<Result>> result;

	/**
	 * number of threads
	 */
	private int threads;

	/**
	 * Creating constructor
	 * 
	 * @param index   data structure
	 * @param threads the number of threads
	 */
	public ThreadSafeQueryFileParser(ThreadSafeIndex index, int threads) {

		this.index = index;
		this.result = new TreeMap<>();
		this.threads = threads;
	}

	/**
	 * Parse the line and output the result
	 * 
	 * @param line    line from queryFile
	 * @param isExact decide exact search or not
	 */
	@Override
	public void parseLine(String line, boolean isExact) {

		TreeSet<String> queries = TextFileStemmer.uniqueStems(line);
		String cleanedLine = String.join(" ", queries);
		synchronized (result) {
			if (queries.isEmpty() || result.containsKey(cleanedLine)) {
				return;
			}
		}
		ArrayList<Result> local = index.search(queries, isExact);
		synchronized (result) {
			result.put(cleanedLine, local);
		}
	}

	/**
	 * Using WorkQueue to search word
	 * 
	 * @param isExact   decide exact search or not
	 * @param queryfile which file to search
	 * @throws IOException
	 */
	public void parseFile(Path queryFile, boolean isExact) throws IOException {

		WorkQueue task = new WorkQueue(this.threads);
		try (BufferedReader readLine = Files.newBufferedReader(queryFile, StandardCharsets.UTF_8)) {
			String line = null;
			while ((line = readLine.readLine()) != null) {
				task.execute(new Task(isExact, line));
			}
		}
		task.finish();
		task.shutdown();
	}
	/**
	 * @author PaulKe
	 *
	 */
	private class Task implements Runnable {

		/**
		 * QueryLine which for search
		 */
		private String queryLine;

		/**
		 * whether Exact Search or not
		 */
		private Boolean Exact = null; // TODO Fix variable name

		/**
		 * Initial Task
		 * 
		 * @param Exact     decide exact or partial search
		 * @param queryLine the querylind we look for
		 */
		Task(Boolean Exact, String queryLine) { // TODO public

			this.queryLine = queryLine;
			this.Exact = Exact;
		}

		@Override
		public void run() {

			parseLine(queryLine, Exact);
		}
	}

	/**
	 * Output JSON type for Query Result
	 * 
	 * @param path the path of output format
	 * @throws IOException
	 */
	@Override
	public void toJSON(Path path) throws IOException {

		synchronized (result) {
			PrettyJSONWriter.resultFormat(result, path);
		}
	}
}
