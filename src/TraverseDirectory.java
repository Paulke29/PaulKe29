import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

/**
 * @author paulke
 *
 */
public class TraverseDirectory {
//	private static HashSet<Path> file;
	
	/**
	 * initial the class
	 */
	public TraverseDirectory() {
//		file = new HashSet<>();
	
	}

	/**
	 * @param path
	 * @return traversefiles class
	 * @throws IOException
	 */
	public HashSet<Path> getDirectory(Path path) throws IOException {
		return traversefiles(path);
	}

	/**
	 * @param path
	 * @return files which is stored the location 
	 * @throws IOException
	 */
	public static HashSet<Path> traversefiles(Path path) throws IOException {
		HashSet<Path> file = new HashSet<>();
		try (DirectoryStream<Path> listing = Files.newDirectoryStream(path)) {

			for (Path files : listing) {

				if (Files.isDirectory(files) == false && (files.getFileName().toString().toLowerCase().endsWith("text")
						|| files.getFileName().toString().toLowerCase().endsWith("txt")
						|| files.getFileName().toString().toLowerCase().endsWith("html")
						|| !files.getFileName().toString().toLowerCase().contains("."))) {

					file.add(files);
				} else {
					file.addAll(traversefiles(files));
				}
			}
		}
		return file;
	}
}
