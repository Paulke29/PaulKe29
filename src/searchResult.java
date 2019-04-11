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
	 * initial new QuerySearch 
	 */
	public searchResult(){
		this.Result = new TreeMap<>();
	}

	/**
	 * @param where
	 * @param score
	 * @param count
	 * @param TotalWords
	 * @return WordsResult
	 */
	public Result WordsResult(String where, double score, int count, int TotalWords) {
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
	public void updateCount(Result Result1, Result Result2) {
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
					updateCount(result, result2);
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
					for (String file : filesindex.get(Keys).keySet()) {
						where = file;
						count = filesindex.get(Keys).get(file).size();
						TotalWords = wordcounts.get(file);
						score = (double) count / TotalWords;
						if (getResultList.isEmpty()) {
							getResultList.add(WordsResult(where, score, count, TotalWords));
						} else {
							Result result2 = WordsResult(where, score, count, TotalWords);
							if (partialUpdate(getResultList, result2, TotalWords) == false) {
								getResultList.add(WordsResult(where, score, count, TotalWords));
							}
						}
					}
				}
			}
		}
		return getResultList;
	}


	/**
	 * @param isExact
	 * @param queryfile
	 * @param wordcounts
	 * @param filesindex
	 * @throws IOException
	 */
	public void SearchResult(boolean isExact, Path queryfile, TreeMap<String, Integer> wordcounts,
			TreeMap<String, TreeMap<String, TreeSet<Integer>>> filesindex) throws IOException {
		ArrayList<Result> SearchResultList;
		String QueryWord = null;
		for (Set<String> words : TextFileStemmer.QuerystemLine2(queryfile)) {
			if (!words.isEmpty()) {
				SearchResultList = new ArrayList<>();
				QueryWord = String.join(" ", words);
				for (String SearchWords : words) {
					{
						ArrayList<Result> SingleResult = getResult(isExact, SearchWords, wordcounts, filesindex);
						if (find(SearchResultList, SingleResult) == false) {
							SearchResultList.addAll(SingleResult);
						} else {
							SearchResultList.addAll(addnew(SearchResultList, SingleResult));
						}
					}
					Collections.sort(SearchResultList);
				}
				this.Result.put(QueryWord, SearchResultList);
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