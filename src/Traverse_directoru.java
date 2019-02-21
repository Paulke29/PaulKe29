import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

public class Traverse_directoru {
	public static HashSet<Path> traverse_file(Path path) throws IOException{
		 HashSet<Path> file = new HashSet<>();
		 try (DirectoryStream<Path> listing = Files.newDirectoryStream(path)){
			 for (Path files : listing) {
				 if ((Files.isDirectory(files) == false) && (files.getFileName().toString().endsWith("text") || files.getFileName().toString().endsWith("txt"))){
					 file.add(files);
				 }
				 else {
					 traverse_file(path);
				 }
			 }
		 }	 
		 return file;
	}

}
