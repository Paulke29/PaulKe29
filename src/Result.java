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
	private double score; // TODO Remove
	
	/**
	 * define the total of words in a file
	 */
	private int TotalWords; // TODO Fix capitalization

	/**
	 * define the decimal
	 */
	DecimalFormat df = new DecimalFormat("0.00000000"); // TODO Use keywords private, final, and fix variable name, initialize in the constructor

	/**
	 * Create constructor
	 * 
	 * @param where
	 * @param count
	 * @param score
	 * @param TotalWords
	 */
	// TODO public Result(String where, int count, int TotalWords) {
	public Result(String where, double score, int count, int TotalWords) {
		this.where = where;
		this.count = count;
		this.score = score; // TODO Remove
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
	public Double getScore() { // TODO double not Double
		return ((double) this.count / this.TotalWords);
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

	// TODO @Override
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
