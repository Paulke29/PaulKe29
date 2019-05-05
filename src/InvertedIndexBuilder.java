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

}