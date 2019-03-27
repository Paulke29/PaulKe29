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
	/**
	 * @param isExact
	 * @param query
	 * @param wordcounts
	 * @param filesindex
	 * @throws IOException
	 */
	public searchResult(boolean isExact, Path query, TreeMap<String, Integer> wordcounts,
			TreeMap<String, TreeMap<String, TreeSet<Integer>>> filesindex) throws IOException {
//		this.isExact = isExact;
//		this.query = query;
//		this.wordcounts = wordcounts;
//		this.filesindex = filesindex;
	}

	/**
	 * @param where
	 * @param score
	 * @param count
	 * @param TotalWords
	 * @return WordsResult
	 */
	public Result WordsResult(String where, double score, int count, int TotalWords) {
//		double score2 = Double.parseDouble(score);
		return new Result(where, score, count, TotalWords);
	}

	/**
	 * @param Result1
	 * @param Result2
	 * @return true if find the same file
	 */
	public boolean find(ArrayList<Result> Result1, ArrayList<Result> Result2) {
		for (Result result : Result1) {
			for (Result result2 : Result2) {
				if (result.getWhere().equals(result2.getWhere())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param Result1
	 * @param Result2
	 */
	public void updateCount(ArrayList<Result> Result1, ArrayList<Result> Result2) {
//		ArrayList<Result> addnew = new ArrayList<>();
		int TotalWords = 0;
		int count;
		for (Result result : Result1) {
			for (Result result2 : Result2) {
				Double score1;
				if (result.getWhere().equals(result2.getWhere())) {
					count = result.getCount();
					count += result2.getCount();
					result.setCount(count);
					TotalWords = result.getTotalWords();
					score1 = (double) count / TotalWords;
//					System.out.println("Score Double: "+ score1);
					result.setScore(score1);
				}
			}
		}
	}
	public void updateCount2(Result Result1, Result Result2) {
		int TotalWords = 0;
		int count;
		Double score1;
		count = Result1.getCount();
		count += Result2.getCount();
		Result1.setCount(count);
		TotalWords = Result1.getTotalWords();
		score1 = (double) count / TotalWords;
		Result1.setScore(score1);
	}

	/**
	 * @param Result1
	 * @param Result2
	 * @return addnew
	 */
	public ArrayList<Result> addnew(ArrayList<Result> Result1, ArrayList<Result> Result2) {
		ArrayList<Result> addnew = new ArrayList<>();
		System.out.println("xxxx");
		for (Result result2 : Result2) {
			boolean notfind = false;
			for (Result result : Result1) {
				if (result.getWhere().equals(result2.getWhere())) {
					updateCount2(result, result2);
					notfind = true;
				}
			}
			if (notfind == false) {
				addnew.add(result2);
			}
		}
		return addnew;
	}

	/**
	 * @param getResultList
	 * @param result2
	 * @param TotalWords
	 * @return true if paritailUpdatae
	 */
	public boolean partialUpdate(ArrayList<Result> getResultList, Result result2, int TotalWords) {
		for (Result result : getResultList) {
			int count;
			double score;
			if (result.getWhere().equals(result2.getWhere())) {
				count = result.getCount();
				count += result2.getCount();
				result.setCount(count);
				score = (double) count / TotalWords;
				result.setScore(score);
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
		ArrayList<Result> getResultList = new ArrayList<>();
		String where;
		double score;
		int count = 0;
		int TotalWords = 0;
		System.out.println("QueryWord: " + QueryWords);
		if (isExact == true) {
			if (filesindex.containsKey(QueryWords)) {
				for (String file : filesindex.get(QueryWords).keySet()) {
					where = file;
					count = filesindex.get(QueryWords).get(file).size();
					TotalWords = wordcounts.get(file);
					score = (double) count / TotalWords;
					getResultList.add(WordsResult(where, score, count, TotalWords));
				}
			}
		} else {
			for (String Keys : filesindex.keySet()) {
				if (Keys.startsWith(QueryWords)) {
					System.out.println("Key: " + Keys);
					for (String file : filesindex.get(Keys).keySet()) {
						where = file;
						count = filesindex.get(Keys).get(file).size();
						TotalWords = wordcounts.get(file);
						score = (double) count / TotalWords;
//						System.out.println("Partial score: "+ score);
						if (getResultList.isEmpty()) {
							getResultList.add(WordsResult(where, score, count, TotalWords));
//							System.out.println("Partial GetResultList is empty");
//							System.out.println("Partial GetResultList1: "+getResultList.toString());
						} else {
							Result result2 = WordsResult(where, score, count, TotalWords);
							if (partialUpdate(getResultList, result2, TotalWords) == false) {
								getResultList.add(WordsResult(where, score, count, TotalWords));
//								System.out.println("Partial GetResultList3: "+getResultList.toString());
							}
//							System.out.println("Partial GetResultList2: "+getResultList.toString());
						}
					}
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
				System.out.println("QueryLine: " + words.toString());
				SearchResultList = new ArrayList<>();
				QueryWord = String.join(" ", words);
				for (String SearchWords : words) {
					{
						ArrayList<Result> SingleResult = getResult(isExact, SearchWords, wordcounts, filesindex);
						if (find(SearchResultList, SingleResult) == false) {
							SearchResultList.addAll(SingleResult);
//							System.out.println("After update SearchAResultList: "+ SearchResultList.toString());
						} else {
//							updateCount(SearchResultList, SingleResult);
							SearchResultList.addAll(addnew(SearchResultList, SingleResult));
						}
					}
					Collections.sort(SearchResultList);
				}
//				System.out.println("Query SearchResultList is: "+ SearchResultList.toString());
				Result.put(QueryWord, SearchResultList);
			}

		}
//		System.out.println("Final Result: " + Result.toString());
		return Result;

	}

}
