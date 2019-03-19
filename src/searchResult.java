import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class searchResult implements Comparable<searchResult> {
	private String score;
	private int count;
	private String location;
	public String Queryword;
	private TreeMap<String, ArrayList<TreeMap[]>> Result ;
	private TreeMap[] ResultSearch;
	private ArrayList<TreeMap[]> SearchResultList;
	public static TreeMap<String, String> whereMap;
	public static TreeMap<String, Integer> countMap;
	public static TreeMap<String, Float> scoreMap;
	static WordIndex wordindex = new WordIndex();
	static int totalword;
	static String textname;
	private String QueryWord;
	public searchResult() {
		Result = new TreeMap<>();
	}
	/**
	 * @param file
	 * @return total
	 * @throws IOException
	 */
	public int getTotal(Path file) throws IOException {
		int total = 0;
		for (String words : TextFileStemmer.stemFile(file)) {
			total++;
		}
//		System.out.println("Total: "+ total);
		return total;
	}
	
	public TreeMap<String, ArrayList<TreeMap[]>> getSearchResult(Path queryfile, Path pathfile) throws IOException{
		return SearchResult(queryfile,pathfile);
	}
	
	/**
	 * @param queryfile
	 * @param pathfile
	 * @return the result of Search
	 * @throws IOException
	 */
	public TreeMap<String, ArrayList<TreeMap[]>> SearchResult(Path queryfile, Path pathfile) throws IOException {
		DecimalFormat df = new DecimalFormat("0.00000000");
		ArrayList<String> wordlist = TextFileStemmer.stemFile(pathfile);
		for (Set<String> words : TextFileStemmer.QuerystemLine2(queryfile)) {
			TreeSet<String> QueryOrder = new TreeSet<>();
			count = 0;
			if (!words.isEmpty()) {
				QueryWord = " ";
				SearchResultList = new ArrayList<>();
				for (String SearchWords : words) {
					if (!SearchWords.isEmpty()) {
						for (String WordsString : TextParser.parse(SearchWords)) {
							QueryWord = QueryWord + " " + WordsString;
							System.out.println("Search Words: " + WordsString);
							count += Collections.frequency(wordlist, WordsString);
//							System.out.println("Count: " + count);
							String score = df.format((float) count / getTotal(pathfile));
							float score2 = Float.parseFloat(score);
//							System.out.println("Score: " + score);
							scoreMap = new TreeMap<>();
							scoreMap.put("score", score2);
						}
						System.out.println("QueryWord: " + QueryWord.trim());
					}
				}
//				System.exit(0);
				whereMap = new TreeMap<>();
				whereMap.put("where", pathfile.toString());
			    countMap = new TreeMap<>();
				countMap.put("count", count);
				ResultSearch = new TreeMap[3];
				System.out.println("ResultSearch: " + ResultSearch);
				ResultSearch[0] = whereMap;
				ResultSearch[1] = countMap;
				ResultSearch[2] = scoreMap;
				System.out.println("Words: " + words);
				if (Result.containsKey(QueryWord.stripLeading())) {
					Result.get(QueryWord).add(ResultSearch);
				}
				if (!Result.containsKey(QueryWord.stripLeading())) {
					SearchResultList.add(ResultSearch);
					Result.put(QueryWord, SearchResultList);
				}
			}
		}
		return Result;
	}

	/**
	 *
	 * @param queryfile 
	 * @param pathfile
	 * @return total match
	 * @throws IOException
	 */
//	public int getMatch(Path queryfile, Path pathfile) throws IOException {
//////		System.out.println("Path file name: " + pathfile.getFileName());
//		ArrayList<String> wordlist = TextFileStemmer.stemFile(pathfile);
//		System.out.println("File name: " + pathfile.getFileName().toString());
//		System.out.println("Query file name: " + queryfile.getFileName().toString());
//		for (String words : TextFileStemmer.QuerystemLine2(queryfile)) {
//			System.out.println("Query words: " + words.toString());
//			if (!words.isEmpty()) {
////				for (String Querywords : words) {
//					count += Collections.frequency(wordlist, words);
////				}
//			}
//
//		}
//		System.out.println("Match: " + count);
//		return count;
//	}

	/**
	 * @param Queryword
	 * @param pathfile
	 * @return score
	 * @throws IOException
	 */
	public float getScore(Path queryfile, Path pathfile) throws IOException {
		DecimalFormat df = new DecimalFormat("0.00000000");
//		int count = getMatch(queryfile, pathfile);
//		System.out.println("Count score: "+count);
		int total = getTotal(pathfile);
//		System.out.println("Total score: "+total);
		String score = df.format((float) count / total);
//		System.out.println("Score:" + score);
		return Float.valueOf(score);

	}

	public String toString() {
		return "QueryWord:" + Queryword + " " + "where:" + location + " " + "count:" + count + " " + "score:" + score;

	}

	@Override
	public int compareTo(searchResult other) {
		return this.score.compareTo(other.score);
	}
}
