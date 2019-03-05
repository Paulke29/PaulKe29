import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

// TODO Never use underscore for normal variable or method or class names.
// TODO Use CamelCase instead.

// TODO Can user the "Refactor" option in Eclipse, it renames EVERYTHING for you.
// (it will change the class name, the file name, and all the code that uses this class at once)

// TODO members as nouns, methods as actions, and class names like job titles
// TODO Traverse_directoru => TraverseDirectory => DirectoryTraverser

public class Traverse_directoru {
	private static HashSet<Path> file ;
	public Traverse_directoru() {
		file = new HashSet<>();
	}
	public HashSet<Path> getDirectory(Path path) throws IOException{
		return traverse_file(path);
	}
	public static HashSet<Path> traverse_file(Path path) throws IOException {
//		HashSet<Path> file = new HashSet<>();
		HashSet<Path> file2 = new HashSet<>();
			try (DirectoryStream<Path> listing = Files.newDirectoryStream(path)) {

				for (Path files : listing) {

					if (Files.isDirectory(files) == false
							&& (files.getFileName().toString().toLowerCase().endsWith("text")
									|| files.getFileName().toString().toLowerCase().endsWith("txt")|| files.getFileName().toString().toLowerCase().endsWith("html") || !files.getFileName().toString().toLowerCase().contains("."))) {

						file.add(files);
					}
					else {
						file.addAll(traverse_file(files));
					}
				}
			}
		return file;
	}
}
