import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;

// TODO Remove old TODO comments
// TODO Always address any warnings in your code

// TODO Configure Eclipse to remove unused imports every time you save.
// TODO Check Piazza for how to configure Eclipse to give you Javadoc warnings.
// TODO Make sure to format code before code review (can use the built in formatter and built in correct indentation feature)
// TODO "Clean up" of any old print statements and commented out code

/*
 * TODO Exception Handling...
 * When Driver.main throws an exception, the user will see a stack trace.
 * All output to the user should be (1) user friendly and (2) informative.
 * -- a stack trace is not something we consider user friendly
 * -- a message like "Something went wrong." is not informative
 *
 * Output a message so that the user can fix the issue before running your code again.
 *
 * Driver.main should never throw an exception.
 *
 * Throw all checked exceptions (the exceptions that Eclipse forces you to do something with)
 * to Driver.main, and catch them there.
 *
 * Judgement call for any unchecked exception (like a NullPointerException) whether
 * you should throw or catch.
 */

/*
 * Driver classes are the only class you do not share with other developers, and
 * the only project-specific class.
 *
 * All other classes have to be general and will be shared with other developers.
 * Anything "generally useful" should not be in Driver. Might move your directory
 * traversal into another class.
 *
 * You want to simplify main() to make future projects easier.

	public static void main(String[] args) {

		ArgumentMap map = new ArgumentMap(args);
		...

		if (map.hasFlag(-path)) {
			if (map.hasValue(-path)) {
				try {
					call code to biuld index
				}
				catch ( ... ) {
					output error to user
				}
			}
			else {
				warn user the value was missing
			}
		}

		if (map.hasFlag(-index)) {

		}

		if (map.hasFlag(-locations)) {

		}

	}


 *
 *
 */

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
		 if(argumentMap.hasFlag("-path") == false || argumentMap == null){
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
