import java.io.IOException;
import java.nio.file.Path;

import java.util.List;

/**
 * @author paulke
 *
 */
public class InvertedIndexBuilder {
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
	 * @param invertedindex
	 * @throws IOException
	 */
	public void index(Path file) throws IOException {
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
