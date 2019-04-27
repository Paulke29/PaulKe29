import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;


/**
 * @author Paulke
 *
 */  
public class searchResult {
	/**
	 * QuerySearch 
	 */
	protected TreeMap<String, ArrayList<Result>> Result;
	/**
	 * initial InvertedIndex object
	 */
	private final InvertedIndex index;
	/**
	 * initial new QuerySearch 
	 * @param index 
	 */
	public searchResult(InvertedIndex index){
		this.Result = new TreeMap<>();
		this.index = index;
	}
	/**
	 * @param isExact
	 * @param queryfile
	 * @throws IOException
	 */
	public void SearchResult(boolean isExact, Path queryfile) throws IOException {
		for (Set<String> words : TextFileStemmer.QuerystemLine2(queryfile)) {
			String joined = String.join(" ", words);
			if (!words.isEmpty() && !this.Result.containsKey(joined)) {
				if (isExact == true) {
					Result.put(joined, this.index.ExactSearch(words));
				} 
				if(isExact == false) {
					Result.put(joined, this.index.PartialSearch(words));
				}
			}

		}
	}
	/**
	 * @param isExact
	 * @param queryfile
	 * @param threadIndex
	 * @param threads
	 * @throws IOException
	 */
	public void SafeSearchResult(boolean isExact, Path queryfile, int threads)
			throws IOException {
		for (Set<String> words : TextFileStemmer.QuerystemLine2(queryfile)) {
			String joined = String.join(" ", words);
			if (!words.isEmpty() && !this.Result.containsKey(joined)) {
				if (isExact == true) {
					Result.put(joined,this.index.ExactSearch(words));
				} 
				if(isExact == false) {
					Result.put(joined,this.index.PartialSearch(words));
				}
			}
		}
	}
	/**
	 * @param Exact
	 * @param Quryline
	 * @param index
	 * @param threads
	 */
	public void SafeSearch(Boolean Exact, Set<String> Quryline, ThreadSafeIndex index, int threads) {
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
		private Set<String> Queryline;
		/**
		 * initial ThreadSafeIndex
		 */
		ThreadSafeIndex threadIndex = new ThreadSafeIndex();
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
		Task(Boolean Exact, Set<String> Queryline, ThreadSafeIndex threadIndex) {
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
					threadIndex.PartialSearch(Queryline);
				}
			}

		}

	}
	/**
	 * Output JSON type for Query Result
	 * @param path
	 * @throws IOException
	 */
	public void toJSON(Path path) throws IOException{
		PrettyJSONWriter.Rearchformat(this.Result, path);
	}

}