import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * @author Paulke
 *
 */
public class ThreadSafeInvertedIndexBuilder extends InvertedIndexBuilder {

	/**
	 * 
	 */
	private final SimpleReadWriteLock lock;

	/**
	 * @param index initial threadSafeIndex
	 * 
	 */
	ThreadSafeInvertedIndexBuilder(threadSafeIndex index) {

		super(index);
		lock = new SimpleReadWriteLock();
	}

	/**
	 * add single object of index with multi-threads
	 * 
	 * @param file
	 * @param index
	 * @throws IOException
	 */
	public void singleIndex(Path file, threadSafeIndex index) throws IOException {

		Predicate<Path> TextFile = TextFileFinder.TEXT_EXT;
		if (TextFile.test(file)) {
//			lock.writeLock().lock();
//			try (BufferedReader read_line = Files.newBufferedReader(file)) {
//				String line;
//				int number = 0;
//				String files = file.toString();
//				Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
//				while ((line = read_line.readLine()) != null) {
//					for (String words : TextParser.parse(line)) {
//						String newWords = stemmer.stem(words).toString();
//						index.add(newWords, files, number + 1);
//						number++;
//					}
//				}
//			} finally {
//				lock.readLock().unlock();
//			}
			lock.writeLock().lock();
			try {
				super.singleIndex(file,index);
			}finally {
				lock.writeLock().unlock();
			}
		} else {
			System.out.println("ERROR");
		}
	}

	/**
	 * @param file
	 * @param index
	 * @throws IOException
	 */
	public void filesIndex(Path file, threadSafeIndex index) throws IOException {

		lock.writeLock().lock();
		try {
			super.filesIndex(file, index);
		} finally {
			lock.writeLock().unlock();
		}

	}

	public void build(Path path) throws IOException {

		lock.writeLock().lock();
		try {
			List<Path> files = TextFileFinder.list(path);
			for (Path file : files) {
				super.filesIndex(file, this.index);
			}
		} finally {
			lock.writeLock().unlock();
		}

	}

	/**
	 * Initial mulidIndex method
	 * 
	 * @param files
	 * @param wordindex
	 * @param threads
	 * @throws IOException
	 */
	public void threadIndex(Path files, threadSafeIndex wordindex, int threads) throws IOException {

		WorkQueue task = new WorkQueue(threads);
		task.execute(new Task(files, wordindex));
		task.finish();
		task.shutdown();
	}

	/**
	 * @author PaulKe
	 *
	 */
	private static class Task implements Runnable {

		/**
		 * Path files for adding 
		 */
		private Path files;

		/**
		 * initial threadSafeIndex class 
		 */
		threadSafeIndex index = new threadSafeIndex();

		/**
		 * @param files
		 * @param index
		 */
		Task(Path files, threadSafeIndex index) {

			this.files = files;
			this.index = index;
		}

		@Override
		public void run() {

			synchronized (index.finalIndex) {
				try {
					build(files);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
