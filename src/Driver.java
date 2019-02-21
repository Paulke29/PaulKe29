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
	
	
	
	/**
	 * catching valid information from file
	 * @param args
	 * @return TreeMap without clean format data 
	 * @throws IOException
	 */
//	public static TreeMap<String,TreeMap<String,TreeSet<Integer>>> infor_catching(String[] args) throws IOException  {
//		TreeMap<String,TreeMap<String,TreeSet<Integer>>>infro_catching = new TreeMap<>();
//		WordIndex wordindex = new WordIndex();
//		Path path;
//        ArgumentMap argumentMap = new ArgumentMap(args);
//        /**
//	      * checking whether input has "Path" flag or not"
//	     */
//        if(argumentMap.hasFlag("-path") == true) {
//        	/**
//        	 * when "-Path" is true, return the directory of the file 
//        	 */
//        	path = argumentMap.getPath("-path");
//        	for(Path file : traverse_file(path)) {
//        		infro_catching.putAll(wordindex.index(file));
//        	}
//        	
//        	
//        }
//        return infro_catching;
//     
//	}
	
	
	public static void main(String[] args) throws IOException {
		// store initial start time
		Instant start = Instant.now();
		// TODO Modify this method as necessary.
		 ArgumentMap argumentMap = new ArgumentMap(args);
		 WordIndex wordindex = new WordIndex();
		 Traverse_directoru traverse_file = new Traverse_directoru();
		 Path path = null;
		 if(argumentMap.hasFlag("-path") == true) {
			 if(argumentMap.hasFlag("-index") == true)
			 path = argumentMap.getPath("-path");
	        	for(Path file : Traverse_directoru.traverse_file(path)){
	        		wordindex.index(file);
	        	}
		 }
       
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
