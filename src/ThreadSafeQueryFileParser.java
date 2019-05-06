import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author PaulKe
 *
 */
public class ThreadSafeQueryFileParser extends QueryFileParser   {
	/**
	 * inital variable InvertedIndex Object
	 */
	private final InvertedIndex index;
	/**
	 * initial the data structure
	 */
	private final TreeMap<String, ArrayList<Result>> result;
	/**
	 * Creating constructor 
	 * @param index
	 */
	public ThreadSafeQueryFileParser(InvertedIndex index) {
		super(index);
		this.index = index;
		this.result = new TreeMap<> ();
	}
	/**
	 * @param isExact
	 * @param queryfile
	 * @param threadIndex
	 * @param threads
	 * @throws IOException
	 */
	public void parsefile(boolean isExact, Path queryfile)throws IOException {
		try (BufferedReader readLine = Files.newBufferedReader(queryfile, StandardCharsets.UTF_8)){
			String line = null;
			while((line = readLine.readLine())!= null) {
				parseLine(line,isExact);
			}
		}
	}
	/**
	 * @param line Queryline
	 * @param isExact boolean exact search or not
	 */
	public void parseLine(String line, boolean isExact) {
		TreeSet<String> queries = TextFileStemmer.uniqueStems(line);
		ArrayList<Result> results = new ArrayList<>();
		String cleanedLine = String.join(" ", queries);
		if (!queries.isEmpty() && !result.containsKey(cleanedLine)) {
			results = index.search(queries,isExact);
		}
		synchronized(this.result) {
			result.put(cleanedLine, results);
		}
	}
	/**
	 * @param Exact
	 * @param Quryline
	 * @param index
	 * @param threads
	 */
	public void SafeSearch(Boolean Exact, Path Quryline, threadSafeIndex index, int threads) {
		WorkQueue task = new WorkQueue(threads);
		task.execute(new Task(Exact, Quryline, index));
		task.finish();
		task.shutdown();
	}

	/**
	 * @author PaulKe
	 *
	 */
	private static class Task implements Runnable {
		/**
		 * QueryLine which for search
		 */
		private Path Queryline;
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
		Task(Boolean Exact, Path Queryline, threadSafeIndex threadIndex) {
			this.Queryline = Queryline;
			this.threadIndex = threadIndex;
			this.Exact = Exact;
		}

		@Override
		public void run() {
			synchronized (threadIndex) {
				parsefile(Exact,Queryline);
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
		synchronized(this.result) {
			PrettyJSONWriter.resultFormat(this.result, path); 
		}
	}

}
