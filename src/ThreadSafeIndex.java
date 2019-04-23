import java.util.Set;

/**
 * @author PaulKe
 *
 */
public class ThreadSafeIndex extends InvertedIndex {

	/**
	 * Mutli-Thread add
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
	 * @param QueryLine
	 */
	public void MutileThreadExactSearch(Set<String> QueryLine) {
		synchronized(this.finalIndex) {
			while(this.finalIndex == null) {
				try {
					finalIndex.wait();
				}catch(InterruptedException e) {
					
				}
			}
			super.ExactSearch(QueryLine);
			this.finalIndex.notify();
		}
	}
	
	/**
	 * Mutli-thread partial search
	 * @param QueryLine
	 */
	public void MutileThreadPartialSearch(Set<String> QueryLine) {
		synchronized(this.finalIndex) {
			while(this.finalIndex == null) {
				try {
					finalIndex.wait();
				}catch(InterruptedException e) {
					
				}
			}
			super.partialSearch(QueryLine);
			this.finalIndex.notify();
		}
	}
}
