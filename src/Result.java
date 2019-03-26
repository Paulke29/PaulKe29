import java.text.DecimalFormat;
import java.util.TreeMap;
import java.util.TreeSet;

public class Result  implements Comparable<Result>  {
	private String where;
	private int count;
	private double score;
	private int TotalWords;
	
	DecimalFormat df = new DecimalFormat("0.00000000");

	/**
	 * Create constructor
	 * @param where
	 * @param count
	 * @param score
	 * @param TotalWords 
	 */
	public Result(String where, double score, int count, int TotalWords) {
		this.where = where;
		this.count = count;
		this.score = score;
		this.TotalWords = TotalWords;
	}
	/**
	 * @return TotalWords
	 */
	public int getTotalWords() {
		return this.TotalWords;
	}
	/**
	 * @return where
	 */
	public String getWhere(){
		return this.where;
	}
	/**
	 * get count
	 * @return count
	 */
	public int getCount(){
		return this.count;
	}
	/**
	 * set up count
	 * @param count
	 */
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * get score
	 * @return score
	 */
	public Double getScore(){
		return this.score;
	}
	
	public String getFormattedScore(){
//		System.out.println("Format score: "+ this.score);
		return df.format(this.score);
	}
	/**
	 * Set up score
	 * @param score
	 */
	public void setScore(double score) {
		this.score = score;
	}
	public String toString () {
		return "Where: "+ where+" "+ "Count: "+count+" "+ "Score: "+score;
		
	}
	@Override
	public int compareTo(Result other) {
		int result = Double.compare(other.getScore(), this.getScore());
		if (result == 0) {
			result = Integer.compare(other.getCount(), this.getCount());
			if (result == 0) {
				result = this.getWhere().compareTo(other.getWhere());
			}
			return result;
		}
		return result;
	}
}
