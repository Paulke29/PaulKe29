import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;
import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * @author Paulke
 *
 */
public class ThreadSafeInvertedIndexBuilder extends InvertedIndexBuilder {
	/**
	 * add single object of index with  multi-threads
	 * @param file
	 * @param index
	 * @throws IOException
	 */
	public static void filesIndex(Path file, threadSafeIndex index) throws IOException {
		Predicate<Path> TextFile = TextFileFinder.TEXT_EXT;
		if (TextFile.test(file)) {
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
			}
		}else {
			System.out.println("ERROR");
		}
	}
	/**
	 * Initial mulidIndex method
	 * @param files
	 * @param wordindex
	 * @param threads
	 * @throws IOException 
	 */
	public void threadIndex(Path files, threadSafeIndex wordindex, int threads) throws IOException {
		for (Path file : TextFileFinder.list(files)) {
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
					filesIndex(file, index);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}		
		}
	}
}
