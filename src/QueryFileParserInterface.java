import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * TODO
 * @author TODO
 */
public interface QueryFileParserInterface {

	/**
	 * Having a queryFile and start to decide whether exact search or not
	 * 
	 * @param queryFile source file
	 * @param isExact   boolean variable
	 * @throws IOException handled exception
	 */
	public default void parseFile(Path queryFile, boolean isExact) throws IOException {
		try (BufferedReader readLine = Files.newBufferedReader(queryFile, StandardCharsets.UTF_8)) {
			String line = null;
			while ((line = readLine.readLine()) != null) {
				parseLine(line, isExact);
			}
		}
	}
	  
	/**
	 * Parse the line and output the result
	 * 
	 * @param line    line from queryFile
	 * @param isExact decide exact search or not
	 */
	public void parseLine(String line, boolean isExact);
	
	/**
	 * Output JSON type for Query Result
	 * 
	 * @param path
	 * @throws IOException handled exception
	 */
	public void toJSON(Path path) throws IOException;
}
