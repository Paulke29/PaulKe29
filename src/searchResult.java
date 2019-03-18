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
	public static TreeMap<String, String> whereMap = new TreeMap<>();
	public static TreeMap<String, Integer> countMap = new TreeMap<>();
	public static TreeMap<String, Float> scoreMap = new TreeMap<>();
	static WordIndex wordindex = new WordIndex();
	static int totalword;
	static String textname;
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
//		TreeMap<String, Map[]> Result = new TreeMap<>();
		ArrayList<String> wordlist = TextFileStemmer.stemFile(pathfile);	
		ArrayList<TreeMap[]>SearchResultList = new ArrayList<>();
		for (Set<String> words : TextFileStemmer.QuerystemLine2(queryfile)) {
//			System.out.println("Words: "+ words);
			TreeSet<String>QueryOrder = new TreeSet<>();
			count = 0;
			if (!words.isEmpty()) {
				for (String SearchWords : words) {
					if (!SearchWords.isBlank()) {
						for (String WordsString : TextParser.parse(SearchWords)) {
							QueryOrder.add(WordsString);
							System.out.println("Search Words: " + WordsString);
							count += Collections.frequency(wordlist, WordsString);
//							System.out.println("Count: " + count);
							String score = df.format((float) count / getTotal(pathfile));
							float score2 = Float.parseFloat(score);
//							System.out.println("Score: " + score);
							scoreMap.put("score", score2);
						}
					}
					
				}
				System.out.println("Query Order: "+ QueryOrder);
//				System.exit(2);
				ArrayList<TreeMap<String,String>>SearchResult = new ArrayList<>();
				if (this.count > 0) {
					System.out.println("words: " + words.toString());
					whereMap.put("where", pathfile.toString());
					System.out.println("WhereMap: " + whereMap);
					countMap.put("count", count);
					System.out.println("countMap: " + countMap);
//				scoreMap.put("score", score);
					System.out.println("scoreMap: " + scoreMap);
					TreeMap[] ResultSearch = new TreeMap[3];
					System.out.println("ResultSearch: " + ResultSearch);
					ResultSearch[0] = whereMap;
					ResultSearch[1] = countMap;
					ResultSearch[2] = scoreMap;
					SearchResultList.add(ResultSearch);
					Result.put(words.toString(), SearchResultList);
					System.out.println("New File and New Words");

					if (Result.containsKey(words.toString())) {
						Result.get(words.toString());
					} else {
						Result.putIfAbsent(words.toString(), SearchResultList);
					}
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
