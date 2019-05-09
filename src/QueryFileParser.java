import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Paulke
 *
 */
public class QueryFileParser implements QueryFileParserInterface{ // TODO implements QueryFileParserInterface

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

//	// TODO Remove
//	/**
//	 * Having a queryFile and start to decide whether exact search or not
//	 * 
//	 * @param queryFile source file
//	 * @param isExact   boolean variable
//	 * @throws IOException handled exception
//	 */
//	public void parseFile(Path queryFile, boolean isExact) throws IOException {
//
//		try (BufferedReader readLine = Files.newBufferedReader(queryFile, StandardCharsets.UTF_8)) {
//			String line = null;
//			while ((line = readLine.readLine()) != null) {
//				parseLine(line, isExact);
//			}
//		}
//	}

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
			result.put(cleanedLine, index.search(queries, isExact));
		}
	}

	/**
	 * Output JSON type for Query Result
	 * 
	 * @param path
	 * @throws IOException handled exception
	 */
	public void toJSON(Path path) throws IOException {

		PrettyJSONWriter.resultFormat(this.result, path);
	}

}