import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.math.MathContext;


public class searchResult implements Comparable<searchResult> {
	private float score = 1;
	private int count;
	private String location;
	public TreeMap<String, String> whereMap;
	public TreeMap<String, Integer> countMap;
	public TreeMap<String, Float> scoreMap;
	static WordIndex wordindex = new WordIndex();
	static int totalword;
	static String textname;

	public searchResult(Path queryfile, Path pathfile) {
		try {
			this.location = pathfile.getFileName().toString();
			this.score = getScore(queryfile,pathfile);
			this.count = getCount(queryfile,pathfile);
			this.location = pathfile.getFileName().toString();
//			if(!location.isEmpty()) {
//				whereMap.put("where", location);
//			}
//			if(score != 0) {
//				scoreMap.put("score", score);
//			}
//			if(count != 0) {
//				countMap.put("count", count);
//			}
			toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
//		System.out.println("Total: "+ total);
		return total;
	}

	/**
	 * @param queryfile
	 * @param pathfile
	 * @return total match 
	 * @throws IOException
	 */
	public int getCount(Path queryfile, Path pathfile) throws IOException {
//		System.out.println("Path file name: " + pathfile.getFileName());
		ArrayList wordlist = TextFileStemmer.stemFile(pathfile);
		Set<String> unique = new HashSet<String>(wordlist);
		for (String words : wordindex.getQuery(queryfile)) {
//			System.out.println("Query word: "+words);
			count += Collections.frequency(unique, words);
		}
//		System.out.println("Count1:" + count);
		return count;
	}

	/**
	 * @param queryfile
	 * @param pathfile
	 * @return score 
	 * @throws IOException
	 */
	public float getScore(Path queryfile, Path pathfile) throws IOException {
		DecimalFormat df = new DecimalFormat("0.00000000");
		int count = getCount(queryfile, pathfile);
//		System.out.println("Count score: "+count);
		int total = getTotal(pathfile);
//		System.out.println("Total score: "+total);
		String score = df.format((float)count/total);
//		System.out.println("Score:" + score);
		return Float.valueOf(score);

	}
    public String toString() {
		return "where:"+location+" "+"count:"+count+" "+"score:"+score;
    	
    }
	@Override
	public int compareTo(searchResult other) {
		return Float.compare(other.score, this.score);
	}
}
