import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

// TODO Keep method order between this and InvertedIndex consistent

/**
 * @author PaulKe
 *
 */
public class ThreadSafeIndex extends InvertedIndex {

	/**
	 * initial simple read and write lock
	 */
	private SimpleReadWriteLock lock; // TODO final

	/**
	 * initial threadSafeIndex class
	 */
	public ThreadSafeIndex() {

		lock = new SimpleReadWriteLock();
	}

	@Override
	public boolean add(String words, String location, int position) {

		lock.writeLock().lock();
		try {
			return super.add(words, location, position);
		} finally {
			lock.writeLock().unlock();
		}
	}

	// TODO Remove
	@Override
	public ArrayList<Result> search(Collection<String> queries, boolean exact) {

		lock.readLock().lock();
		try {
			return exact ? this.exactSearch(queries) : this.partialSearch(queries);
		} finally {
			lock.readLock().unlock();
		}

	}

	@Override
	public ArrayList<Result> exactSearch(Collection<String> QueryLine) {

		lock.readLock().lock();
		try {
			return super.exactSearch(QueryLine);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public ArrayList<Result> partialSearch(Collection<String> QueryLine) {

		lock.readLock().lock();
		try {
			return super.partialSearch(QueryLine);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public int wordCount() {

		lock.readLock().lock();
		try {
			// TODO return super.wordCount();
			return this.finalIndex.size();
		} finally {
			lock.readLock().unlock();
		}
	}

	// TODO Fix all of these to call super instead of accessing the finalIndex directly
	
	@Override
	public boolean contains(String word) {

		lock.readLock().lock();
		try {
			return this.finalIndex.containsKey(word);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public int wordCount(String location) {

		lock.readLock().lock();
		try {
			return wordCount.get(location);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean contains(String word, String location) {

		lock.readLock().lock();
		try {
			return this.finalIndex.containsKey(word) && this.finalIndex.get(word).containsKey(location);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
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

	@Override
	public int locationCount() {

		lock.readLock().lock();
		try {
			return wordCount.size();
		} finally {
			lock.readLock().unlock();
		}

	}

	@Override
	public int locationCount(String word) {

		lock.readLock().lock();
		try {
			if (this.finalIndex.containsKey(word)) {
				return this.finalIndex.get(word).size();
			} else {
				return 0;
			}
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public void nestJSON(Path path) throws IOException {

		lock.readLock().lock();
		try {
			PrettyJSONWriter.asNestedStructure(this.finalIndex, path);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public void locationsJSON(Path path) throws IOException {

		lock.readLock().lock();
		try {
			PrettyJSONWriter.asObject(this.wordCount, path);
		} finally {
			lock.readLock().unlock();
		}

	}

}