
/**
 * @author PaulKe
 *
 */
public class ThreadSafeIndex extends InvertedIndex {

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
}
