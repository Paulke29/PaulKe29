import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Creating ThreadSafeIndex for mutil-threading
 * 
 * @author PaulKe
 *
 */
public class ThreadSafeIndex extends InvertedIndex {

	/**
	 * initial simple read and write lock
	 */
	private final SimpleReadWriteLock lock;

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
	public void nestJSON(Path path) throws IOException {

		lock.readLock().lock();
		try {
			super.nestJSON(path);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public void locationsJSON(Path path) throws IOException {

		lock.readLock().lock();
		try {
			super.locationsJSON(path);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public int locationCount() {

		lock.readLock().lock();
		try {
			return super.locationCount();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public int wordCount() {

		lock.readLock().lock();
		try {
			return super.wordCount();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean contains(String word) {

		lock.readLock().lock();
		try {
			return super.contains(word);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean contains(String word, String location) {

		lock.readLock().lock();
		try {
			return super.contains(word, location);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public int wordCount(String location) {

		lock.readLock().lock();
		try {
			return super.wordCount(location);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public int wordCount(String word, String location) {

		lock.readLock().lock();
		try {
			return super.wordCount(word, location);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public int locationCount(String word) {

		lock.readLock().lock();
		try {
			return super.locationCount(word);
		} finally {
			lock.readLock().unlock();
		}
	}

	public void addAll(InvertedIndex other) {

		lock.writeLock().lock();
		try {
			super.addAll(other);
		} finally {
			lock.writeLock().unlock();
		}
	}
}