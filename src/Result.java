import java.text.DecimalFormat;

/**
 * @author paulke
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
	 * define the occur of words
	 */
	private double score;
	/**
	 * define the total of words in a file
	 */
	private int TotalWords;

	/**
	 * define the decimal
	 */
	DecimalFormat df = new DecimalFormat("0.00000000");

	/**
	 * Create constructor
	 * 
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
	public String getWhere() {
		return this.where;
	}

	/**
	 * get count
	 * 
	 * @return count
	 */
	public int getCount() {
		return this.count;
	}

	/**
	 * set up count
	 * 
	 * @param count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * get score
	 * 
	 * @return score
	 */
	public Double getScore() {
		return((double)this.count / this.TotalWords);
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
	 * @param newCount
	 */
	public void updateCount(int newCount) {
		this.count = this.count + newCount;
	}

//	/**
//	 * Set up score
//	 * 
//	 * @param score
//	 */
//	public void setScore(double score) {
//		this.score = score;
//	}

	public String toString() {
		return "Where: " + where + " " + "Count: " + count + " " + "Score: " + score;

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
