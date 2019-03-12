import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A utility class for finding all text files in a directory using lambda
 * functions and streams.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Spring 2019
 */
public class TextFileFinder {

	/**
	 * A lambda function that returns true if the path is a file that ends in a .txt or .text extension
	 * (case-insensitive).
	 *
	 * @see Files#isRegularFile(Path, java.nio.file.LinkOption...)
	 */
	// TODO YOU MUST USE LAMBDA FUNCTIONS HERE
	public static final Predicate<Path> TEXT_EXT = (Path path) -> {return (path.toString().toLowerCase().endsWith(".txt") || path.toString().toLowerCase().endsWith(".text"))&& Files.isRegularFile(path);};

	/**
	 * Returns a stream of text files, following any symbolic links encountered.
	 *
	 * @param start the initial path to start with
	 * @return a stream of text files
	 *
	 * @throws IOException
	 *
	 * @see #TEXT_EXT
	 * @see FileVisitOption#FOLLOW_LINKS
	 *
	 * @see Files#walk(Path, FileVisitOption...)
	 * @see Files#find(Path, int, java.util.function.BiPredicate, FileVisitOption...)
	 *
	 * @see Integer#MAX_VALUE
	 */
	public static Stream<Path> find(Path start) throws IOException {
		// TODO YOU MUST USE STREAMS HERE
		BiPredicate<Path, BasicFileAttributes> same = (path, b) -> TEXT_EXT.test(path);
		return Files.find(start,Integer.MAX_VALUE,same, FileVisitOption.FOLLOW_LINKS);
//		throw new UnsupportedOperationException("Not yet implemented");
		
	}

	/**
	 * Returns a list of text files.
	 *
	 * @param start the initial path to search
	 * @return list of text files
	 * @throws IOException
	 *
	 * @see #find(Path)
	 */
	public static List<Path> list(Path start) throws IOException {
		// TODO REUSE THE STREAM FROM FIND(...) HERE!
//		throw new UnsupportedOperationException("Not yet implemented");
		Stream<Path> pathlist = find(start);
		ArrayList<Path> result = new ArrayList<>();
		for(Object a : pathlist.toArray()) {
			result.add((Path)a);
		}
		result.stream().filter(x -> TEXT_EXT.test(start)).findFirst();
		
			
		
		return result;
	}

	/**
	 * Demonstrates this class.
	 *
	 * @param args unused
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Path path = Path.of(".", "text", "simple").normalize();
		list(path).forEach(System.out::println);
	}
}
