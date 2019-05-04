import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Paulke
 *
 */
public class QueryFileParser {
	/**
	 * QuerySearch Result
	 */
	private final TreeMap<String, ArrayList<Result>> result;
	/**
	 * initial InvertedIndex object
	 */
	private final InvertedIndex index;

	/**
	 * initial new QueryFileSearch
	 * 
	 * @param index initial InvertedIndex
	 */
	public QueryFileParser(InvertedIndex index) {
		this.result = new TreeMap<>();
		this.index = index;
	}

	/**
	 * Having a queryFile and start to decide whether exact search or not
	 * 
	 * @param queryFile source file 
	 * @param isExact boolean variable
	 * @throws IOException handled exception
	 */
	public void parseFile(Path queryFile, boolean isExact) throws IOException {
		try (BufferedReader readLine = Files.newBufferedReader(queryFile, StandardCharsets.UTF_8)) {
			String line = null;
			while ((line = readLine.readLine()) != null) {
				parseLine(line, isExact);
			}
		}
	}

	/**
	 * Parse the line and output the result
	 * 
	 * @param line    line from queryFile
	 * @param isExact decide exact search or not
	 */
	public void parseLine(String line, boolean isExact) {
		TreeSet<String> queries = TextFileStemmer.uniqueStems(line);
		String cleanedLine = String.join(" ", queries);
		if (!queries.isEmpty() && !result.containsKey(cleanedLine)) {
			result.put(cleanedLine, index.search(queries,isExact));
		}
	}
	/**
	 * @param isExact
	 * @param queryfile
	 * @param threadIndex
	 * @param threads
	 * @throws IOException
	 */
	public void Safeparsefile(boolean isExact, Path queryfile, int threads)throws IOException {
		try (BufferedReader readLine = Files.newBufferedReader(queryfile, StandardCharsets.UTF_8)){
			String line = null;
			while((line = readLine.readLine())!= null) {
				SafeparseLine(line,isExact);
			}
		}
	}
	/**
	 * @param line queryline
	 * @param isExact boolean exact search or not
	 */
	public void SafeparseLine(String line, boolean isExact) {
		TreeSet<String> queries = TextFileStemmer.uniqueStems(line);
		String cleanedLine = String.join(" ", queries);
		if (!queries.isEmpty() && !result.containsKey(cleanedLine)) {
			result.put(cleanedLine, index.search(queries,isExact));
		}
	}
	/**
	 * @param Exact
	 * @param Quryline
	 * @param index
	 * @param threads
	 */
	public void SafeSearch(Boolean Exact, Collection<String> Quryline, threadSafeIndex index, int threads) {
		WorkQueue task = new WorkQueue(threads);
		task.execute(new Task(Exact, Quryline, index));
		task.finish();
		task.shutdown();
	}

	/**
	 * @author Paulke
	 *
	 */
	private static class Task implements Runnable {
		/**
		 * QueryLine which for search
		 */
		private Collection<String> Queryline;
		/**
		 * initial ThreadSafeIndex
		 */
		threadSafeIndex threadIndex = new threadSafeIndex();
		/**
		 * whether Exact Search or not
		 */
		Boolean Exact = null;

		/**
		 * Initial Task
		 * @param Exact
		 * @param Queryline
		 * @param threadIndex
		 */
		Task(Boolean Exact, Collection<String> Queryline, threadSafeIndex threadIndex) {
			this.Queryline = Queryline;
			this.threadIndex = threadIndex;
			this.Exact = Exact;
		}

		@Override
		public void run() {
			synchronized (threadIndex) {
				if (Exact == true) {
					threadIndex.ExactSearch(Queryline);
				} else {
					threadIndex.partialSearch(Queryline);
				}
			}

		}

	}
	/**
	 * Output JSON type for Query Result
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void toJSON(Path path) throws IOException {
		PrettyJSONWriter.resultFormat(this.result, path); 
	}

}