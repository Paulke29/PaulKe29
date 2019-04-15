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
	 * 
	 * @param index
	 */
	public searchResult(InvertedIndex index) {
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
			String queryword = String.join(" ", words);
			if (!words.isEmpty() && !this.Result.containsKey(queryword)) {
				if (isExact == true) {
					Result.put(queryword, this.index.ExactSearch(words));
				}
				if (isExact == false) {
					Result.put(queryword, this.index.partialSearch(words));
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
		PrettyJSONWriter.Rearchformat(this.Result, path);
	}

}