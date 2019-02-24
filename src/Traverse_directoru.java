import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

public class Traverse_directoru {
	public static HashSet<Path> traverse_file(Path path) throws IOException {
		HashSet<Path> file = new HashSet<>();
		HashSet<Path> file2 = new HashSet<>();
			try (DirectoryStream<Path> listing = Files.newDirectoryStream(path)) {
				
				for (Path files : listing) {
					if ((Files.isDirectory(files) == false)
							&& (files.getFileName().toString().toLowerCase().endsWith("text")
									|| files.getFileName().toString().toLowerCase().endsWith("txt")|| files.getFileName().toString().toLowerCase().endsWith("html") )) {
						file.add(files);
					} 
					else {
						System.out.println(file.size());
						file.addAll((traverse_file(files)));
						}
					}
			}
		return file;
	}

//	public static HashSet<Path> traverse_HTML(Path path) throws IOException{
//		 HashSet<Path> file = new HashSet<>();
//		 try (DirectoryStream<Path> listing = Files.newDirectoryStream(path)){
//			 for (Path files : listing) {
//				 if ((Files.isDirectory(files) == false) && (files.getFileName().toString().toUpperCase().endsWith("HTML"))){
//					 file.add(files);
//				 }
//				 else {
//					 traverse_file(path);
//				 }
//			 }
//		 }	 
//		 return file;
//	}

}
