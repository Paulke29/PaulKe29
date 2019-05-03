import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.List;
import java.util.function.Predicate;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * this file is going to create single object and add those object together
 * 
 * @author paulke
 *
 */
public class InvertedIndexBuilder {

	
	/**
	 * add single object of index
	 * 
	 * @param file  a single file
	 * @param index InvertedIndex object
	 * @throws IOException
	 */
	public static void singleIndex(Path file, InvertedIndex index) throws IOException {
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
		}
	}
	/**
	 * add single object of index with  mutilt-threads
	 * @param file
	 * @param index
	 * @throws IOException
	 */
	public static void MutileIndex(Path file, threadSafeIndex index) throws IOException {
		System.out.println("BUILDING: " + file.toString());
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
	 * add the index of words from list of files
	 * 
	 * @param files list of file
	 * @param index InvertedIndex object
	 * @throws IOException
	 */
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
					MutileIndex(file, index);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}		
		}
	}


}
