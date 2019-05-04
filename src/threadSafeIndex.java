import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author PaulKe
 *
 */
public class threadSafeIndex extends InvertedIndex {
	/**
	 * 
	 */
	private SimpleReadWriteLock lock;
	
	public threadSafeIndex() {
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
		lock.writeLock().lock();
		try{
			super.add(words, location, position);
//			this.finalIndex.notifyAll();
			return true;
		}finally {
			lock.writeLock().unlock();
		}
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
	public ArrayList<Result>  partialSearch(Set<String> QueryLine) {
		ArrayList<Result> getResultList = new ArrayList<>();
		lock.readLock().lock();
		try {
			getResultList.addAll(super.partialSearch(QueryLine));
			return getResultList;
		}finally {
			lock.readLock().unlock();
		}
	}
	
	public int wordCount() {
		lock.readLock().lock();
		try {
			return this.finalIndex.size();
		}finally {
			lock.readLock().unlock();
		}	
	}
	public boolean contains(String word) {
		lock.readLock().lock();
		try {
			return this.finalIndex.containsKey(word);
		}finally {
			lock.readLock().unlock();
		}
	}
	public int wordCount(String location) {
		lock.readLock().lock();
		try {
			return wordCount.get(location);
		}finally {
			lock.readLock().unlock();
		}
	}
	public boolean contains(String word, String location) {
		lock.readLock().lock();
		try {
			return this.finalIndex.containsKey(word) && this.finalIndex.get(word).containsKey(location);
		}finally {
			lock.readLock().unlock();
		}	
	}

	public int wordCount(String word, String location) {
		lock.readLock().lock();
		try {
			if (this.contains(word, location)) {
				return this.finalIndex.get(word).get(location).size();
			} else {
				return 0;
			}
		} finally {
			lock.readLock().unlock();
		}
 
	}
	public int locationCount() {
		lock.readLock().lock();
		try {
			return wordCount.size();
		}finally {
			lock.readLock().unlock();
		}
		
	}

	public int locationCount(String word) {
		lock.readLock().lock();
		try {
			if (this.finalIndex.containsKey(word)) {
			return this.finalIndex.get(word).size();
		} else {
			return 0;
		}
		}finally {
			lock.readLock().unlock();
		}
	}
	public void nestJSON(Path path) throws IOException {
		lock.readLock().lock();
		try {
			PrettyJSONWriter.asNestedStructure(this.finalIndex, path);
		}finally {
			lock.readLock().unlock();
		}
	}
	public void locationsJSON(Path path) throws IOException {
		lock.readLock().lock();
		try {
			PrettyJSONWriter.asObject(this.wordCount, path);
		}finally {
			lock.readLock().unlock();
		}
		
	}

}