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
	private float score = 1;
	private int count;
	private String location;
	public String Queryword;
	public TreeMap<String, String> whereMap;
	public TreeMap<String, Integer> countMap;
	public TreeMap<String, Float> scoreMap;
	static WordIndex wordindex = new WordIndex();
	static int totalword;
	static String textname;
	public searchResult() {}
	public searchResult(Path queryfile, Path pathfile) {
		try {
			this.location = pathfile.getFileName().toString();
			this.score = getScore(queryfile, pathfile);
			this.count = getMatch(queryfile, pathfile);
			this.location = pathfile.getFileName().toString();
			this.Queryword = Queryword;
			toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	public TreeMap<String,Map[]> SearchResult(Path queryfile, Path pathfile) throws IOException {
		TreeMap<String,Map[]>Result = new TreeMap<>();
		ArrayList<String> wordlist = TextFileStemmer.stemFile(pathfile);
		for (Set<String> words : TextFileStemmer.QuerystemLine2(queryfile)) {
			System.out.println("Query words: " + words.toString());
			if (!words.isEmpty()) {
				for (String Querywords : words) {
					count += Collections.frequency(wordlist, Querywords);
				}
			}
			float score = count / this.getTotal(pathfile);
			whereMap.put("where", pathfile.toString());
			countMap.put("count", count);
			scoreMap.put("score",score);
			Map[]ResultSearch = {whereMap,countMap,scoreMap};
			Result.put(words.toString(), ResultSearch);
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
	public int getMatch(Path queryfile, Path pathfile) throws IOException {
////		System.out.println("Path file name: " + pathfile.getFileName());
		ArrayList<String> wordlist = TextFileStemmer.stemFile(pathfile);
		System.out.println("File name: " + pathfile.getFileName().toString());
		System.out.println("Query file name: " + queryfile.getFileName().toString());
		for (Set<String> words : TextFileStemmer.QuerystemLine2(queryfile)) {
			System.out.println("Query words: " + words.toString());
			if (!words.isEmpty()) {
				for (String Querywords : words) {
					count += Collections.frequency(wordlist, Querywords);
				}
			}

		}
		System.out.println("Match: " + count);
		return count;
	}

	/**
	 * @param Queryword
	 * @param pathfile
	 * @return score
	 * @throws IOException
	 */
	public float getScore(Path queryfile, Path pathfile) throws IOException {
		DecimalFormat df = new DecimalFormat("0.00000000");
		int count = getMatch(queryfile, pathfile);
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
		return Float.compare(other.score, this.score);
	}
}
