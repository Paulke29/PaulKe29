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
	private SimpleReadWriteLock lock;
	
	 /**
	 * 
	 */
	ThreadSafeInvertedIndexBuilder(){
		 lock = new SimpleReadWriteLock();
	 }
	/**
	 * add single object of index with  multi-threads
	 * @param file
	 * @param index
	 * @throws IOException
	 */
	public void singleIndex(Path file, threadSafeIndex index) throws IOException {
		Predicate<Path> TextFile = TextFileFinder.TEXT_EXT;
		if (TextFile.test(file)) {
			lock.readLock().lock();
			try (BufferedReader read_line = Files.newBufferedReader(file)) {
				String line;
				int number = 0;
				String files = file.toString();
				Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
				while ((line = read_line.readLine()) != null) {
					for (String words : TextParser.parse(line)) {
						String newWords = stemmer.stem(words).toString();
						index.add(newWords, files, number + 1);
						number++;
					}
				}
			}finally {
				lock.readLock().unlock();
			}
		}else {
			System.out.println("ERROR");
		}
	}
	public void filesIndex(List<Path> files, InvertedIndex index) throws IOException {
		for (Path file : files) {
			singleIndex(file, index);
		}
	}
	/**
	 * Initial mulidIndex method
	 * @param files
	 * @param wordindex
	 * @param threads
	 * @throws IOException 
	 */
	public void threadIndex(List<Path> files, threadSafeIndex wordindex, int threads) throws IOException {
		for (Path file : files) {
			WorkQueue task = new WorkQueue(threads);
			task.execute(new Task(file, wordindex));
			task.finish();
			task.shutdown();
		}
	}

	/**
	 * @author PaulKe
	 *
	 */
	private static class Task implements Runnable {
		/**
		 * 
		 */
		private Path file;
		/**
		 * 
		 */
		threadSafeIndex index = new threadSafeIndex();
	
		/**
		 * @param file
		 * @param index
		 */
		Task(Path file, threadSafeIndex index){
			this.file = file;
			this.index = index;
		}
		@Override
		public void run() {
			synchronized(index.finalIndex) {
				try {
					singleIndex(file, index);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}		
		}
	}
}
