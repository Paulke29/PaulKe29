import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.HashSet;
import java.io.Writer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class responsible for running this project based on the provided command-line
 * arguments. See the README for details.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Spring 2019
 */
public class Driver {
	/**
	 * Initializes the classes necessary based on the provided command-line
	 * arguments. This includes (but is not limited to) how to build or search an
	 * inverted index.
	 * 
	 * 
	 *
	 * @param args flag/value pairs used to start this program
	 * @throws IOException 
	 */
	
	
	
	public static void main(String[] args) throws IOException {
		// store initial start time
		Instant start = Instant.now();
		// TODO Modify this method as necessary.
		
		 ArgumentMap argumentMap = new ArgumentMap(args);
		 WordIndex wordindex = new WordIndex();
		 Traverse_directoru traverse_file = new Traverse_directoru();
		 PrettyJSONWriter format = new PrettyJSONWriter();
		 Path path = null;
		 Path index = null;
		 Path location = null;
		 TreeMap<String,TreeMap<String,TreeSet<Integer>>> filesindex = new TreeMap<>();
		 if(argumentMap.hasFlag("-path") == true) {
			
			 path = argumentMap.getPath("-path");
			 if(argumentMap.hasFlag("-index") == true && argumentMap.hasValue("-index") == true) {
//				DirectoryStream<Path> listing = Files.newDirectoryStream(path);
				index = argumentMap.getPath("-index");
				if(Files.isDirectory(path) == false) {
					format.asNestedObject_file(wordindex.getWordsindex(path), index);
				}
				else {
					for(Path file : traverse_file.getDirectory(path)){
						
						format.asNestedObject_file(wordindex.getWordsindex(file), index);
	        		
					}
				}
	        	
			 }
			 if(argumentMap.hasFlag("-index") == true && argumentMap.hasValue("-index") == false) {
				 /**
				  * how to output as "index.json"
				  */
				 index = Paths.get("index.json");
				 if(Files.isDirectory(path) == false) {
						format.asNestedObject_file(wordindex.getWordsindex(path), index);
				 }
				 else {
					 for(Path file : traverse_file.getDirectory(path)){
//						 System.out.println("Driver2"+Traverse_directoru.traverse_file(path).size());
		        		format.asNestedObject_file(wordindex.getWordsindex(file), index);
		        	}
				 }  	
			 }
			 if(argumentMap.hasFlag("-locations") == true && argumentMap.hasValue("-locations") == true){
				
				location = argumentMap.getPath("-locations"); 
				System.out.println("Location");
				format.location_format(wordindex.getWordcount(path),location);
			 } 
			 if(argumentMap.hasFlag("-locations") == true && argumentMap.hasValue("-locations") == false){
					location = argumentMap.getPath("locations.json");
					if(Files.isDirectory(path) == false) {
						format.location_format(wordindex.getWordcount(path),location);
					}
					else {
						for(Path file : traverse_file.getDirectory(path)){
			        		format.location_format(wordindex.getWordcount(file),location);
			        	}
					}
					
				 }
			 
		 }
		 if((argumentMap.hasFlag("-path") == false) || argumentMap == null){
			 Path file = argumentMap.getPath("-path");
			 if(argumentMap.hasFlag("-index") == true && argumentMap.hasValue("-index") == true) {
				 index = argumentMap.getPath("-index"); 
//				 format.asNestedObject_file(wordindex.getWordsindex(file), index); 
				 format.empty_file(index);
			 }
			 if(argumentMap.hasFlag("-index") == true && argumentMap.hasValue("-index") == false) {
				  index = Paths.get("index.json");
//				  format.asNestedObject_file(wordindex.getWordsindex(file), index); 
				  format.empty_file(index);
				  
			 }   
//			 index = argumentMap.getPath("-index"); 
//			 format.empty_file(index);
				
			 
			 	
		 }
//       
		System.out.println(Arrays.toString(args));
		
		// calculate time elapsed and output
		Duration elapsed = Duration.between(start, Instant.now());
		double seconds = (double) elapsed.toMillis() / Duration.ofSeconds(1).toMillis();
		System.out.printf("Elapsed: %f seconds%n", seconds);
	}

	/*
	 * Generally, "driver" classes are responsible for setting up and calling other
	 * classes, usually from a main() method that parses command-line parameters. If
	 * the driver were only responsible for a single class, we use that class name.
	 * For example, "PizzaDriver" is what we would name a driver class that just
	 * sets up and calls the Pizza class.
	 */
}
