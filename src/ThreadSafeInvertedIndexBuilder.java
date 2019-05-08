
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Paulke
 *
 */
public class ThreadSafeInvertedIndexBuilder extends InvertedIndexBuilder {

	/**
	 * number of threads
	 */
	private int threads;
	
	// TODO private final ThreadSafeIndex index;

	/**
	 * TODO 
	 * @param index   initial threadSafeIndex
	 * @param threads number of threads
	 * 
	 */
	ThreadSafeInvertedIndexBuilder(ThreadSafeIndex index, int threads) { // TODO public

		super(index);
		// TODO this.index = index;
		this.threads = threads;
	}

	/**
	 * Initial mulidIndex method
	 * 
	 * @param files     Queryfile to add
	 * @param wordindex data structre to store data
	 * @param threads   the number of threads
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
		private Path file; // TODO final

		/**
		 * @param file QueryFile to add
		 */
		Task(Path file) { // TODO public

			this.file = file;

		}

		@Override
		public void run() {

			try {

				singleIndex(file, index);
				
				/* TODO
				InvertedIndex local = new InvertedIndex();
				singleIndex(file, local);
				index.addAll(local);
				*/

			} catch (IOException e) {
				e.printStackTrace(); // TODO Fix
			}
		}
	}
}
