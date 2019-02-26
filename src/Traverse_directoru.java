import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

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
					
					if ((Files.isDirectory(files) == false)
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
