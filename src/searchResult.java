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

public class searchResult{

	static WordIndex wordindex = new WordIndex();
	static int totalword;
	static String textname;
	private String QueryWord;

	public searchResult() {
//		Result = new TreeMap<>();
//		ResultList = new ArrayList<>();
//		SearchResultList = new ArrayList<>();
	}

	/**
	 * @param where
	 * @param score
	 * @param count
	 * @return WordsResult
	 */
	public TreeMap<Object, Object> WordsResult(String where, String score, int count) {
		TreeMap<Object, Object> WordsResult = new TreeMap<>();
		float score2 = Float.parseFloat(score);
		WordsResult.put("where",where);
		WordsResult.put("count",count);
		WordsResult.put("score",score2);
		return WordsResult;
	}

	public void updateCount(String file, ArrayList<TreeMap<Object, Object>> getResultList,
			TreeMap<String, TreeMap<String, TreeSet<Integer>>> filesindex, String QueryWords) {
		int count = 0;
		for (TreeMap<Object, Object> treeMap : getResultList) {
			if (treeMap.containsKey(file)) {
				count = (int) treeMap.get("count");
				count += filesindex.get(QueryWords).get(file).size();
			}
		}
	}
	/**
	 * @param isExact
	 * @param QueryWords
	 * @param wordcounts
	 * @param filesindex
	 * @return getResultList
	 */
	public ArrayList<TreeMap<Object, Object>> getResult(boolean isExact, String QueryWords,
			TreeMap<String, Integer> wordcounts, TreeMap<String, TreeMap<String, TreeSet<Integer>>> filesindex) {
		DecimalFormat df = new DecimalFormat("0.00000000");
		ArrayList<TreeMap<Object, Object>> getResultList = new ArrayList<>();
		String where;
		String score = null;
		int count = 0;
		int TotalWords = 0;
		if (isExact == true) {
			if (filesindex.containsKey(QueryWords)) {
				for (String file : filesindex.get(QueryWords).keySet()) {
					where = file;
					count = filesindex.get(QueryWords).get(file).size();
					TotalWords = wordcounts.get(file);
					score = df.format((float) count / TotalWords);
					getResultList.add(WordsResult(where, score, count));
				}
			}
		} else {
			for (String Keys : filesindex.keySet()) {
				if (Keys.startsWith(QueryWords)) {
					for (String file : filesindex.get(Keys).keySet()) {
						where = file;
						count = filesindex.get(Keys).get(file).size();
						TotalWords = wordcounts.get(file);
						score = df.format((float) count / TotalWords);
						getResultList.add(WordsResult(where, score, count));
					}
				}
			}
		}
		return getResultList;
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
		ArrayList<TreeMap<Object,Object>> SearchResultList;
		TreeMap<String, ArrayList<TreeMap<Object,Object>>> Result = new TreeMap<>();
		for (Set<String> words : TextFileStemmer.QuerystemLine2(queryfile)) {
			if (!words.isEmpty()) {
				SearchResultList = new ArrayList<>();
				QueryWord = String.join(" ", words);
				for (String SearchWords : words) {
					SearchResultList.addAll(getResult(isExact, SearchWords,wordcounts, filesindex));
				}
				Result.put(QueryWord, SearchResultList);
			}
		}
		return Result;
	}
//	public TreeMap<String, ArrayList<TreeMap<Object, Object>>> SortResult()
	
}
