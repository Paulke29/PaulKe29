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
	TreeMap<String, ArrayList<Result>> Result;
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
				System.out.println("Join: "+joined);
				if (isExact == true) {
					System.out.println("Exact Search Search Result");
					Result.put(joined, this.index.ExactSearch(words));
				} 
				if(isExact == false) {
					System.out.println("Partial Search Search Result");
					Result.put(joined, this.index.partialSearch(words));
				}
			}

		}
	}
	/**
	 * @param isExact
	 * @param queryfile
	 * @param index
	 * @param threads
	 * @throws IOException
	 */
	public void SafeSearchResult(boolean isExact, Path queryfile, ThreadSafeIndex index, int threads) throws IOException {
		for (Set<String> words : TextFileStemmer.QuerystemLine2(queryfile)) {
			SafeSearch(isExact,words,index, threads);
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
		ThreadSafeIndex index = new ThreadSafeIndex();
		/**
		 * whether Exact Search or not
		 */
		Boolean Exact = null;

		/**
		 * Initial Task
		 * @param Exact
		 * @param Queryline
		 * @param index
		 */
		Task(Boolean Exact, Set<String> Queryline, ThreadSafeIndex index) {
			this.Queryline = Queryline;
			this.index = index;
			this.Exact = Exact;
		}

		@Override
		public void run() {
			synchronized (index) {
				if (Exact == true) {
					index.MutileThreadExactSearch(Queryline);
				} else {
					index.MutileThreadPartialSearch(Queryline);
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