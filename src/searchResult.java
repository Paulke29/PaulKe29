import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

// TODO Fix Javadoc

/**
 * @author Paulke
 *
 */
public class searchResult { // TODO Refactor to QueryFileParser
	/**
	 * QuerySearch
	 */
	TreeMap<String, ArrayList<Result>> Result; // TODO Keywords, capitalization
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

	/* TODO Instead of SearchResult method...
	 * 
	public void parseFile(Path queryFile, boolean isExact) throws IOException {
		try (BufferedReader readline = Files.newBufferedReader(queryfile, StandardCharsets.UTF_8)) {
			for all the lines
				parseLine(line, isExact);
		}
	}
	
	public void parseLine(String line, boolean isExact) {
		TreeSet<String> queries = TextFileStemmer.uniqueStems(...);
		String cleanedLine = String.join(" ", queries);
		
		if (queries.isEmpty()) {
			results.put(cleanedLine, this.index.search(queries, isExact));
		}
	}
	*/
	
	
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