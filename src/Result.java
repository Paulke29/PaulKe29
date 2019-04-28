import java.text.DecimalFormat;

/**
 * @author PaulKe
 *
 */
public class Result implements Comparable<Result> {
	/**
	 * define the file
	 */
	private String where;

	/**
	 * define the number of words
	 */
	private int count;

	/**
	 * define the total of words in a file
	 */
	private int totalWords;

	/**
	 * define the decimal
	 */
	private final DecimalFormat df;

	/**
	 * Create constructor
	 * 
	 * @param where      having location
	 * @param count      calculating the number of words
	 * @param TotalWords total words of file
	 */
	public Result(String where, int count, int TotalWords) {
		this.where = where;
		this.count = count;
		this.totalWords = TotalWords;
		df = new DecimalFormat("0.00000000");
	}

	/**
	 * having the total words of file
	 * 
	 * @return TotalWords
	 */
	public int getTotalWords() {
		return this.totalWords;
	}

	/**
	 * having the location
	 * 
	 * @return where location
	 */
	public String getWhere() {
		return this.where;
	}

	/**
	 * get count
	 * 
	 * @return count matched words
	 */
	public int getCount() {
		return this.count;
	}

	/**
	 * set up count
	 * 
	 * @param count matched words
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * get score
	 * 
	 * @return score ratio of count and total words
	 */
	public double getScore() {
		return ((double) this.count / this.totalWords);
	}

	/**
	 * Set up the format of score
	 * 
	 * @return the format of score
	 */
	public String getFormattedScore() {
		return df.format(this.getScore());
	}

	/**
	 * Update the count in Result object
	 * 
	 * @param newCount new matched words
	 */
	public void updateCount(int newCount) {
		this.count = this.count + newCount;
	}

	@Override
	public String toString() {
		return "Where: " + where + " " + "Count: " + count + " " + "Score: " + getScore();

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
