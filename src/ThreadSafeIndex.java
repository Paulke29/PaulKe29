import java.util.ArrayList;
import java.util.Set;

/**
 * @author PaulKe
 *
 */
public class ThreadSafeIndex extends InvertedIndex {

	/**
	 * Mutli-Thread add
	 * 
	 * @param words
	 * @param location
	 * @param position
	 */
	public void MutileThreadAdd(String words, String location, int position) {
		synchronized (this.finalIndex) {
			while (this.finalIndex == null) {
				try {
					finalIndex.wait();
				} catch (InterruptedException e) {
				}
			}
			super.add(words, location, position);
			this.finalIndex.notifyAll();
		}
	}

	/**
	 * Mutli-thread exact search
	 * 
	 * @param QueryLine
	 * @return 
	 */
	public ArrayList<Result>  MutileThreadExactSearch(Set<String> QueryLine) {
		ArrayList<Result> getResultList = new ArrayList<>();
		synchronized (this.finalIndex) {
			getResultList.addAll(super.ExactSearch(QueryLine));
			System.out.println("Synchronized: "+ super.ExactSearch(QueryLine));
		}
		System.out.println("Thread exact: "+ getResultList);
		return getResultList;
	}

	/**
	 * Mutli-thread partial search
	 * 
	 * @param QueryLine
	 */
	public ArrayList<Result>  MutileThreadPartialSearch(Set<String> QueryLine) {
		ArrayList<Result> getResultList = new ArrayList<>();
		synchronized (this.finalIndex) {
			getResultList.addAll(super.partialSearch(QueryLine));
		}
		return getResultList;
	}
}
