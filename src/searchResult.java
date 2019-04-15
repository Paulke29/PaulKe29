import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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
	 * Output JSON type for Query Result
	 * @param path
	 * @throws IOException
	 */
	public void toJSON(Path path) throws IOException{
		PrettyJSONWriter.Rearchformat(this.Result, path);
	}

}