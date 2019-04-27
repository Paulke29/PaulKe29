import java.util.ArrayList;
import java.util.Set;

/**
 * @author PaulKe
 *
 */
public class ThreadSafeIndex extends InvertedIndex {
	/**
	 * 
	 */
	private SimpleReadWriteLock lock;
	
	public ThreadSafeIndex() {
		lock = new SimpleReadWriteLock();
	}
	/**
	 * Mutli-Thread add
	 * 
	 * @param words
	 * @param location
	 * @param position
	 * @return 
	 */
	public boolean add(String words, String location, int position) {
		synchronized(this.finalIndex) {
			super.add(words, location, position);
			this.finalIndex.notifyAll();
			return true;
		}
//		lock.writeLock().lock();
//		try {
//			super.add(words, location, position);
//			this.finalIndex.notifyAll();
//			return true;
//		} finally {
//			lock.writeLock().unlock();
//		}
	}

	/**
	 * Mutli-thread exact search
	 * 
	 * @param QueryLine
	 * @return 
	 */
	public ArrayList<Result>ExactSearch(Set<String> QueryLine) {
		ArrayList<Result> getResultList = new ArrayList<>();
		lock.readLock().lock();
		try {
			getResultList.addAll(super.ExactSearch(QueryLine));
			return getResultList;
		}finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Mutli-thread partial search
	 * 
	 * @param QueryLine
	 */
	public ArrayList<Result>  PartialSearch(Set<String> QueryLine) {
		ArrayList<Result> getResultList = new ArrayList<>();
		lock.readLock().lock();
		try {
			getResultList.addAll(super.PartialSearch(QueryLine));
			return getResultList;
		}finally {
			lock.readLock().unlock();
		}
	}
}
