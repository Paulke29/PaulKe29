import java.io.IOException;
import java.nio.file.Path;

import java.util.List;

/* TODO You need to fix all of the Javadoc comments in this file; add descriptions
 * to the methods and the parameters and return values.
 */

/**
 * TODO Add description here
 * @author paulke
 *
 */
public class InvertedIndexBuilder {
	
	/*
	 * TODO Add or adjust methods so that you can call a single method in Driver
	 * and do not have to call TextFileFinder.list(path) there.
	 */
	
	/**
	 * creating InvertedIndex Object
	 */
	private final InvertedIndex index;

	/**
	 * Build up InvertedIndex Object Build up constructor
	 * 
	 * @param index
	 */
	public InvertedIndexBuilder(InvertedIndex index) {
		this.index = index;
	}

	/**
	 * @param file
	 * @throws IOException
	 */
	public void index(Path file) throws IOException {
		/*
		 * TODO Need to improve efficiency.
		 * 
		 * 1) Avoid calling the same thing over and over again. For example, calling 
		 * file.toString() inside a for loop. Save that value and reuse it in the loop.
		 * 
		 * 2) You have a method for testing for text extensions in TextFileFinder.
		 * Please use it.
		 * 
		 * 3) There are too many loops through the words in the file. You loop through
		 * the file in stemFile, add the words to a list, and then loop through that list
		 * again to move the words from that list to the index. You can reduce that. 
		 * Here, generalization is getting in the way of efficiency.
		 * 
		 * So, integrate (i.e. copy/paste) the code from stemFile into here, except
		 * where you were adding to a list add directly to the index instead.
		 */
		int number = 0;
		if (file != null && (file.getFileName().toString().toLowerCase().endsWith("text")
				|| file.getFileName().toString().toLowerCase().endsWith("txt"))) {
			for (String words : TextFileStemmer.stemFile(file)) {
				index.add(words, file.toString(), number + 1);
				number++;
			}
			index.wordcount(file.toString(), number);
		}
	}

	/**
	 * @param files
	 * @throws IOException
	 */
	public void filesIndex(List<Path> files) throws IOException {
		for (Path file : files) {
			index(file);
		}
	}

}
