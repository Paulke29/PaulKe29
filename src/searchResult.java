import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

public class searchResult implements Comparable<Object> {
	private long score = 1;
	private int count;

	public TreeMap<String, String> whereMap = new TreeMap<>();
	public TreeMap<String, Integer> countMap = new TreeMap<>();
	public TreeMap<String, Float> scoreMap = new TreeMap<>();
	static WordIndex wordindex = new WordIndex();
	static int totalword;
	static String textname;

	public searchResult() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public int getTotal(Path file) throws IOException {
		int total = 0;
		for (String words : TextFileStemmer.stemFile(file)) {
			total++;
		}
		System.out.println("Total: "+ total);
		return total;
	}

	/**
	 * @param queryfile
	 * @param pathfile
	 * @return
	 * @throws IOException
	 */
	public long getCount(Path queryfile, Path pathfile) throws IOException {
		System.out.println("Path file name: " + pathfile.getFileName());
		ArrayList wordlist = TextFileStemmer.stemFile(pathfile);
		Set<String> unique = new HashSet<String>(wordlist);
		for (String words : wordindex.getQuery(queryfile)) {
			count += Collections.frequency(unique, words);
		}
		System.out.println("Count1:" + count);
		return count;
	}

	public long getScore(Path queryfile, Path pathfile) throws IOException {
		DecimalFormat FORMATTER = new DecimalFormat("0.00000000");
		score = getCount(queryfile, pathfile) / getTotal(pathfile);
		System.out.println("Score:"+FORMATTER.format(Math.PI));
		return score;

	}

	@Override
	public int compareTo(Object other) {
		return count;
//		 TODO Auto-generated method stub
//		return this.score > other.score;
	}
}
