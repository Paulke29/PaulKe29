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

public class searchResult {
	private TreeMap<String, Integer> wordcounts;
	private Path query;
	private boolean isExact;
	private String where;
	private int count;
	private String score;
	private TreeMap<String, TreeMap<String, TreeSet<Integer>>> filesindex;

	/**
	 * @param isExact
	 * @param query
	 * @param wordcounts
	 * @param filesindex
	 * @throws IOException
	 */
	public searchResult(boolean isExact, Path query, TreeMap<String, Integer> wordcounts,
			TreeMap<String, TreeMap<String, TreeSet<Integer>>> filesindex) throws IOException {
		this.isExact = isExact;
		this.query = query;
		this.wordcounts = wordcounts;
		this.filesindex = filesindex;
	}

	/**
	 * @param where
	 * @param score
	 * @param count
	 * @return WordsResult
	 */
	public Result WordsResult(String where, String score, int count,int TotalWords) {
		float score2 = Float.parseFloat(score);
		return new Result(where, score2, count, TotalWords);
	}
	/**
	 * @param Result1
	 * @param Result2
	 * @return True if successfully update the count  False if not find the specific Object to update the count
	 */
	public boolean updateCount(ArrayList<Result> Result1, ArrayList<Result> Result2) {
		DecimalFormat df = new DecimalFormat("0.00000000");
		int TotalWords = 0;
		for (Result result : Result1) {
			for (Result result2 : Result2) {
				if (result.getWhere().equals(result2.getWhere())) {
					count = result.getCount();
					count += result2.getCount();
					result.setCount(count);
					TotalWords = result.getTotalWords();
					score = df.format((float) count / TotalWords);
					float score2 = Float.parseFloat(score);
					result.setScore(score2);
					System.out.println("New count: " + result.getCount());
					System.out.println("New score: " + result.getScore());
					return true;
				}
			}
		}
		return false;
	}
	public boolean partialUpdate(ArrayList<Result> getResultList,Result result2,int TotalWords) {
		for(Result result: getResultList) {
			DecimalFormat df = new DecimalFormat("0.00000000");
			if(result.getWhere().equals(result2.getWhere())) {
				count = result.getCount();
				count += result.getCount();
				score = df.format((float) count / TotalWords);
				float score2 = Float.parseFloat(score);
				result.setScore(score2);
				return true;
			}	
		}
		return false;
	}
	/**
	 * @param isExact
	 * @param QueryWords
	 * @param wordcounts
	 * @param filesindex
	 * @return getResultList
	 */
	public ArrayList<Result> getResult(boolean isExact, String QueryWords, TreeMap<String, Integer> wordcounts,
			TreeMap<String, TreeMap<String, TreeSet<Integer>>> filesindex) {
		DecimalFormat df = new DecimalFormat("0.00000000");
		ArrayList<Result> getResultList = new ArrayList<>();
		String where;
		String score = null;
		int count = 0;
		int TotalWords = 0;
		System.out.println("QueryWord: "+ QueryWords);
		if (isExact == true) {
			if (filesindex.containsKey(QueryWords)) {
				for (String file : filesindex.get(QueryWords).keySet()) {
					where = file;
					count = filesindex.get(QueryWords).get(file).size();
					TotalWords = wordcounts.get(file);
					score = df.format((float) count / TotalWords);
					getResultList.add(WordsResult(where, score, count,TotalWords));
				}
			}
		} else {
			for (String Keys : filesindex.keySet()) {
				if (Keys.startsWith(QueryWords)) {
					System.out.println("Key: "+Keys);
					for (String file : filesindex.get(Keys).keySet()) {
						where = file;
						count = filesindex.get(Keys).get(file).size();
						TotalWords = wordcounts.get(file);
						score = df.format((float) count / TotalWords);	
						if(getResultList.isEmpty()) {
							getResultList.add(WordsResult(where, score, count,TotalWords));
						}
						else{
							if(partialUpdate(getResultList,WordsResult(where, score, count,TotalWords),TotalWords) == false) {
								getResultList.add(WordsResult(where, score, count,TotalWords));
							}
						}
							
					}		
					System.out.println("Partial Search: "+ getResultList);
				}
			}
		}
		return getResultList;
	}

	/**
	 * @param isExact
	 * @param query
	 * @param wordcounts
	 * @param filesindex
	 * @return SortResult
	 * @throws IOException
	 */
	public TreeMap<String, ArrayList<Result>> getSearchResult(boolean isExact, Path query,
			TreeMap<String, Integer> wordcounts, TreeMap<String, TreeMap<String, TreeSet<Integer>>> filesindex)
			throws IOException {
		return SearchResult(isExact, query, wordcounts, filesindex);
	}

	/**
	 * @param isExact
	 * @param queryfile
	 * @param wordcounts
	 * @param filesindex
	 * @param pathfile
	 * @return the result of Search
	 * @throws IOException
	 */
	public TreeMap<String, ArrayList<Result>> SearchResult(boolean isExact, Path queryfile,
			TreeMap<String, Integer> wordcounts, TreeMap<String, TreeMap<String, TreeSet<Integer>>> filesindex)
			throws IOException {
		ArrayList<Result> SearchResultList;
		TreeMap<String, ArrayList<Result>> Result = new TreeMap<>();
		String QueryWord = null;
		for (Set<String> words : TextFileStemmer.QuerystemLine2(queryfile)) {
			if (!words.isEmpty()) {
				SearchResultList = new ArrayList<>();
				QueryWord = String.join(" ", words);
				for (String SearchWords : words) {
					if(SearchResultList.isEmpty()) {
						System.out.println("SearchResultList is Empty");
						SearchResultList.addAll(getResult(isExact, SearchWords, wordcounts, filesindex));
						System.out.println("Empty SearchResultList is: "+ SearchResultList.toString());
					}
					else {
						if(updateCount(SearchResultList,getResult(isExact, SearchWords, wordcounts, filesindex)) == false) {
							System.out.println("UpdateCount is false");
							SearchResultList.addAll(getResult(isExact, SearchWords, wordcounts, filesindex));
							System.out.println("False SearchResultList is: "+ SearchResultList.toString());
						}
						System.out.println("True SearchResultList is: "+ SearchResultList.toString());
					}
					Collections.sort(SearchResultList);
				}
				System.out.println("Query SearchResultList is: "+ SearchResultList.toString());
				Result.put(QueryWord, SearchResultList);
			}
		}
		System.out.println("Final Result: "+Result.toString());
		return Result;
	}

}
