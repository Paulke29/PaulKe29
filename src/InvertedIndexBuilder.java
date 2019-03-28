import java.io.IOException;
import java.nio.file.Path;

import java.util.List;

public class InvertedIndexBuilder {
	/**
	 * Build up constructor
	 */
	public InvertedIndexBuilder() {
	}

	/**
	 * @param file
	 * @param invertedindex
	 * @return Object invertedIndex
	 * @throws IOException
	 */
	public InvertedIndex index(Path file, InvertedIndex invertedindex) throws IOException {
		int number = 0;
		InvertedIndex SingleinvertedIndex = new InvertedIndex();
		if (file != null && (file.getFileName().toString().toLowerCase().endsWith("text")
				|| file.getFileName().toString().toLowerCase().endsWith("txt"))) {
			for (String words : TextFileStemmer.stemFile(file)) {
				SingleinvertedIndex.add(words, file.toString(), number + 1);
				number++;
			}
			SingleinvertedIndex.wordcount(file.toString(), number);
		}
		invertedindex.addAll(SingleinvertedIndex);
		return invertedindex;
	}

	/**
	 * @param files
	 * @throws IOException
	 */
	public void filesIndex(List<Path> files) throws IOException {

		InvertedIndex invertedindex = new InvertedIndex();
		for (Path file : files) {
			index(file, invertedindex);
		}
	}

	/**
	 * @return the number of words for every single file
	 */
	public InvertedIndex wordcount() {
		return null;

	}

}
