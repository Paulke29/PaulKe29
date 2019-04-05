import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
	 * @param file TODO Add description
	 * @param index InvertedIndex object
	 * @throws IOException
	 */
	public void singleIndex(Path file, InvertedIndex index) throws IOException {
		Predicate<Path> TextFile = TextFileFinder.TEXT_EXT;
		if (TextFile.test(file)) {
			// TODO Not using try-with-resources.
			BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8);
			String line;
			int number = 0;
			String files = file.toString();
			Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
			while ((line = reader.readLine()) != null) {
				for (String words : TextParser.parse(line)) {
					String newWords = stemmer.stem(words).toString();
					index.add(newWords, files, number + 1);
					number++;
				}
			}
		}
	}

	/**
	 * TODO You are missing descriptions for this method and the below parameters.
	 * @param files
	 * @param index InvertedIndex object
	 * @throws IOException
	 */
	public void filesIndex(List<Path> files, InvertedIndex index) throws IOException {
		for (Path file : files) {
			singleIndex(file, index);
		}
	}

}
