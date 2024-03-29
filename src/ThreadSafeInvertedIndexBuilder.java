import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Creating ThreadSafeInvertedIndexBuilder class for mutilethreading
 * 
 * @author Paulke
 *
 */
public class ThreadSafeInvertedIndexBuilder extends InvertedIndexBuilder {

	/**
	 * number of threads
	 */
	private int threads;

	/**
	 * initial ThreadSafeIndex object
	 */
	private final ThreadSafeIndex index;

	/**
	 * Initial ThreadSafeInvertedIndexBuilder object
	 * 
	 * @param index   initial threadSafeIndex
	 * @param threads number of threads
	 * 
	 */
	public ThreadSafeInvertedIndexBuilder(ThreadSafeIndex index, int threads) {

		super(index);
		this.index = index;
		this.threads = threads;
	}

	/**
	 * Initial mulidIndex method
	 * 
	 * @param path path for using working queue to build index
	 * @throws IOException
	 */
	public void build(Path path) throws IOException {

		WorkQueue task = new WorkQueue(this.threads);
		List<Path> files = TextFileFinder.list(path);
		for (Path singleFile : files) {
			task.execute(new Task(singleFile));
		}
		task.finish();
		task.shutdown();
	}
	/**
	 * @author PaulKe
	 *
	 */
	private class Task implements Runnable {

		/**
		 * Path files for adding
		 */
		private final Path file;

		/**
		 * @param file QueryFile to add
		 */
		public Task(Path file) {

			this.file = file;
		}

		@Override
		public void run() {

			try {
				InvertedIndex local = new InvertedIndex();
				singleIndex(file, local);
				index.addAll(local);
			} catch (IOException e) {
				System.out.println("IndexBuilder task has occur error");
			}
		}
	}
}
