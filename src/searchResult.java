import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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
	public searchResult(String Queryword, Path pathfile) {
		try {
			this.location = pathfile.getFileName().toString();
			this.score = getScore(Queryword, pathfile);
//			this.count = getMatch(Queryword, pathfile);
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
	public void Result(String querywords, Path pathfile) throws IOException {
		ArrayList<String> wordlist = TextFileStemmer.stemFile(pathfile);
		System.out.println("Query words: "+ querywords);
		count = Collections.frequency(wordlist, querywords);
		System.out.println("File name: "+ pathfile.getFileName().toString());
		System.out.println("Match: "+ count);
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
////		System.out.println("Query word: " + Queryword);
//		count = Collections.frequency(wordlist, Queryword);
		for(String words : TextFileStemmer.stemFile(queryfile)) {
			System.out.println("Query words: "+ words);
			count = Collections.frequency(wordlist, words);
			System.out.println("File name: "+ pathfile.getFileName().toString());
			System.out.println("Query file name: "+ queryfile.getFileName().toString());
			System.out.println("Match: "+ count);
		}
		return count;
	}

	/**
	 * @param Queryword
	 * @param pathfile
	 * @return score
	 * @throws IOException
	 */
	public float getScore(String Queryword, Path pathfile) throws IOException {
		DecimalFormat df = new DecimalFormat("0.00000000");
//		int count = getMatch(Queryword, pathfile);
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
