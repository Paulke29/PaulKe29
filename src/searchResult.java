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
	private String where;
	private String score;
	private int count;
	private int TotalWords;
	private String location;
	public String Queryword;
	private static TreeMap<String, ArrayList<TreeMap<Object,Object>>> Result;
	private static ArrayList<TreeMap<Object,Object>> ResultList;
	private static TreeMap<Object,Object> ResultSearch;
	private ArrayList<TreeMap<Object,Object>> SearchResultList;
	private TreeMap<String, String> whereMap;
	private TreeMap<String, Integer> countMap;
	private TreeMap<String, Float> scoreMap;
	static WordIndex wordindex = new WordIndex();
	static int totalword;
	static String textname;
	private String QueryWord;

	public searchResult() {
		Result = new TreeMap<>();
//		ResultList = new ArrayList<>();
//		SearchResultList = new ArrayList<>();
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

	public TreeMap<String, ArrayList<TreeMap<Object,Object>>> getSearchResult(boolean isExact, Path query,
			TreeMap<String, Integer> wordcounts, TreeMap<String, TreeMap<String, TreeSet<Integer>>> filesindex)
			throws IOException {
		return SearchResult(isExact, query, wordcounts, filesindex);
	}

	/**
	 * @param queryfile
	 * @param pathfile
	 * @return the result of Search
	 * @throws IOException
	 */
	public TreeMap<String, ArrayList<TreeMap<Object, Object>>> SearchResult(boolean isExact, Path queryfile,
			TreeMap<String, Integer> wordcounts, TreeMap<String, TreeMap<String, TreeSet<Integer>>> filesindex)
			throws IOException {
		DecimalFormat df = new DecimalFormat("0.00000000");
		for (Set<String> words : TextFileStemmer.QuerystemLine2(queryfile)) {
			count = 0;
			TotalWords = 0;
			if (!words.isEmpty()) {
				SearchResultList = new ArrayList<>();
				ResultList = new ArrayList<>();
				QueryWord = String.join(" ", words);
				for (String SearchWords : words) {
					if (!SearchWords.isBlank()) {
						if (isExact == true) {
							if (filesindex.containsKey(SearchWords)) {
								for (String filename : filesindex.get(SearchWords).keySet()) {
									where = filename;
									count += filesindex.get(SearchWords).get(filename).size();
									for (String Namefile : wordcounts.keySet()) {
										if (filename.equals(Namefile)) {
											TotalWords = wordcounts.get(filename);
										}
									}
									score = df.format((float) count / TotalWords);
								}
							}
						} else {
							for (String Keys : filesindex.keySet()) {
								if (Keys.startsWith(SearchWords)) {
									for (String filename : filesindex.get(Keys).keySet()) {
										where = filename;
										count += filesindex.get(Keys).get(filename).size();
										for (String Namefile : wordcounts.keySet()) {
											if (filename.equals(Namefile)) {
												TotalWords = wordcounts.get(filename);
											}
										}
										score = df.format((float) count / TotalWords);
									}
								}
							}
						}
					}
				}
				if (count > 0) {
					ResultSearch = new TreeMap<>();
					ResultSearch.put("where", where);
					ResultSearch.put("count", count);
					float score2 = Float.parseFloat(score);
					ResultSearch.put("score", score2);
					SearchResultList.add(ResultSearch);
					ResultList.addAll(SearchResultList);
				} else {
					ResultSearch = null;
				}
				if (Result.containsKey(QueryWord)) {
					Result.get(QueryWord).add(ResultSearch);
				}
				if (!Result.containsKey(QueryWord)) {
//						SearchResultList.add(ResultSearch);
						Result.put(QueryWord, ResultList);
				}
			}
		}
		return Result;
	}

	public String toString() {
		return "QueryWord:" + Queryword + " " + "where:" + location + " " + "count:" + count + " " + "score:" + score;

	}

	@Override
	public int compareTo(searchResult other) {
		return this.score.compareTo(other.score);
	}
}
