import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Outputs several tree-based data structures in "pretty" JSON format where
 * newlines are used to separate elements, and nested elements are indented.
 *
 * Warning: This class is not thread-safe. If multiple threads access this class
 * concurrently, access must be synchronized externally.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Spring 2019
 */
public class PrettyJSONWriter {

	/**
	 * Writes the elements as a pretty JSON array.
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param level    the initial indent level
	 * @throws IOException
	 */
	public static void asArray(TreeSet<Integer> elements, Writer writer, int level) throws IOException {
		writer.write('[');
		writer.write('\n');
		var iterator = elements.iterator();
		if (iterator.hasNext()) {
			indent(iterator.next().toString(), writer, level + 1);
			while (iterator.hasNext()) {
				writer.write(",\n");
				indent(iterator.next().toString(), writer, level + 1);
			}
			writer.write("\n");

		}
		indent(writer, level);
		writer.write(']');
	}

	/**
	 * Output location format
	 * 
	 * @param counting
	 * @param path
	 * @throws IOException
	 */
	public static void location_format(TreeMap<String, Integer> counting, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asObject(counting, writer, 0);
		}
	}
	public static void asResultArray(ArrayList<Result> result, Writer writer, int level) throws IOException {
		int lastKey = result.size() - 1;
		int beforeLast = result.size() - 2;
		if(result.size() <1) {
			writer.write("\n");
		}
		else if (result.size() == 1) {
			writer.write('\n');
			indent(writer, 2);
			writer.write('{');
			writer.write('\n');
			quote("where", writer, 3);
			writer.write(": ");
			quote(result.get(0).getWhere(), writer);
			writer.write(",");
			writer.write('\n');
			quote("count", writer, 3);
			writer.write(": ");
			writer.write(Integer.toString(result.get(lastKey).getCount()));
			writer.write(",");
			writer.write('\n');
			quote("score", writer, 3);
			writer.write(": ");
			writer.write(result.get(lastKey).getFormattedScore());
			writer.write('\n');
			indent(writer, 2);
			writer.write('}');
			writer.write('\n');
		}  
		else {
			for (int x = 0; x <= beforeLast; x++) {
				writer.write('\n');
				indent(writer, 2);
				writer.write('{');
				writer.write('\n');
				quote("where", writer, 3);
				writer.write(": ");
				quote(result.get(x).getWhere(), writer);
				writer.write(",");
				writer.write('\n');
				quote("count", writer, 3);
				writer.write(": ");
				writer.write(Integer.toString(result.get(x).getCount()));
//				writer.write(2);
				writer.write(",");
				writer.write('\n');
				quote("score", writer, 3);
				writer.write(": ");
				writer.write(result.get(x).getFormattedScore());
				writer.write('\n');
				indent(writer, 2);
				writer.write('}');
				writer.write(",");			
			}
			writer.write('\n');
			indent(writer, 2);
			writer.write('{');
			writer.write('\n');
			quote("where", writer, 3);
			writer.write(": ");
			quote(result.get(lastKey).getWhere(), writer);
			writer.write(",");
			writer.write('\n');
			quote("count", writer, 3);
			writer.write(": ");
			writer.write(Integer.toString(result.get(lastKey).getCount()));
			writer.write(",");
			writer.write('\n');
			quote("score", writer, 3);
			writer.write(": ");
			writer.write(result.get(lastKey).getFormattedScore());
			writer.write('\n');
			indent(writer, 2);
			writer.write('}');
			writer.write('\n');
		}

	}
	public static void FormatSearch(TreeMap<String, ArrayList<Result>> result, Writer writer, int level)throws IOException {
		writer.write('{');
		writer.write('\n');
		if (!result.isEmpty()) {
			for (String SearchWords : result.headMap(result.lastKey()).keySet()) {
				quote(SearchWords.toString(), writer, level + 1);
				writer.write(": ");
				writer.write('[');
				asResultArray(result.get(SearchWords), writer, level + 1);
				indent(writer, 1);
				writer.write(']');
				writer.write(",");
				writer.write("\n");
			}
			quote(result.lastKey().toString(), writer, 1);
			writer.write(": ");
			writer.write('[');
			asResultArray(result.get(result.lastKey()), writer, level + 1);
			indent(writer, 1);
			writer.write(']');
		}
		writer.write('\n');
		writer.write('}');
	}
	/**
	 * JSN format of QuerySearch
	 * @param result
	 * @param path
	 * @throws IOException
	 */
	public static void Rearchformat(TreeMap<String,ArrayList<Result>> result,Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			FormatSearch(result, writer, 0);
		}
	}
	/**
	 * Writes the elements as a pretty JSON array to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException
	 *
	 * @see #asArray(TreeSet, Writer, int)
	 */
	public static void asArray(TreeSet<Integer> elements, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asArray(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a pretty JSON array.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asArray(TreeSet, Writer, int)
	 */
	public static String asArray(TreeSet<Integer> elements) {
		try {
			StringWriter writer = new StringWriter();
			asArray(elements, writer, 0);
			return writer.toString();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Writes the elements as a pretty JSON object.
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param level    the initial indent level
	 * @throws IOException
	 */
	public static void asObject(TreeMap<String, Integer> elements, Writer writer, int level) throws IOException {
		writer.write('{');
		writer.write('\n');
		if (elements != null) {
			for (String keys : elements.headMap(elements.lastKey()).keySet()) {
				quote(keys, writer, level + 1);
				writer.write(": ");
				indent(elements.get(keys), writer, level);
				writer.write(",\n");
			}
			quote(elements.lastKey(), writer, level + 1);
			writer.write(": ");
			indent(elements.get(elements.lastKey()), writer, level);
			writer.write("\n");
		}

		indent(writer, level);
		writer.write('}');

	}

	/**
	 * Writes the elements as a pretty JSON object to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException
	 *
	 * @see #asObject(TreeMap, Writer, int)
	 */
	public static void asObject(TreeMap<String, Integer> elements, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asObject(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a pretty JSON object.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asObject(TreeMap, Writer, int)
	 */
	public static String asObject(TreeMap<String, Integer> elements) {
		try {
			StringWriter writer = new StringWriter();
			asObject(elements, writer, 0);
			return writer.toString();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Writes the elements as a nested pretty JSON object.
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param level    the initial indent level
	 * @throws IOException
	 */
	public static void asNestedObject(TreeMap<String, TreeSet<Integer>> elements, Writer writer, int level)
			throws IOException {

		writer.write('{');
		writer.write('\n');

		if (elements != null) {
			for (String keys : elements.headMap(elements.lastKey()).keySet()) {
				quote(keys, writer, level + 1);
				writer.write(": ");
				asArray(elements.get(keys), writer, level + 1);
				writer.write(",\n");
			}

			quote(elements.lastKey(), writer, level + 1);
			writer.write(": ");
			asArray(elements.get(elements.lastKey()), writer, level + 1);

		}
	}

	/**
	 * Output nested Structure
	 * 
	 * @param elements
	 * @param path
	 * @throws IOException
	 */
	public static void asNestedStructure(TreeMap<String, TreeMap<String, TreeSet<Integer>>> elements, Path path)
			throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asNestedStructure(elements, writer, 0);
		}
	}

	/**
	 * create nested structure
	 * 
	 * @param elements
	 * @param writer
	 * @param level
	 * @throws IOException
	 */
	public static void asNestedStructure(TreeMap<String, TreeMap<String, TreeSet<Integer>>> elements, Writer writer,
			int level) throws IOException {
		if (!elements.isEmpty()) {
			writer.write('{');
			writer.write('\n');

			for (String keys : elements.headMap(elements.lastKey()).keySet()) {
				quote(keys, writer, level + 1);
				writer.write(": ");
				asNestedObject(elements.get(keys), writer, level + 1);
				writer.write('\n');
				indent(writer, level + 1);
				writer.write('}');
				writer.write(",\n");

			}
			quote(elements.lastKey(), writer, level + 1);
			writer.write(": ");
			writer.write('{');
			writer.write('\n');
			indent(writer, level + 1);
			for (String filename : elements.get(elements.lastKey()).keySet()) {
				quote(filename, writer, level + 1);
				writer.write(": ");

				asArray(elements.get(elements.lastKey()).get(filename), writer, level + 3);
			}
			writer.write('\n');
			indent(writer, level + 1);
			writer.write("}");
			writer.write("\n");
			writer.write('}');

		}

	}

	/**
	 * Output a empty file
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void emptyFile(Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			emptyFile(writer);
		}
	}

	/**
	 * create a empty file
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public void emptyFile(Writer writer) throws IOException {
		writer.write('{');
		writer.write('\n');
		writer.write("}");
	}

	/**
	 * Writes the elements as a nested pretty JSON object to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException
	 *
	 * @see #asNestedObject(TreeMap, Writer, int)
	 */
	public static void asNestedObject(TreeMap<String, TreeSet<Integer>> elements, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asNestedObject(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a nested pretty JSON object.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asNestedObject(TreeMap, Writer, int)
	 */
	public static String asNestedObject(TreeMap<String, TreeSet<Integer>> elements) {
		try {
			StringWriter writer = new StringWriter();
			asNestedObject(elements, writer, 0);
			return writer.toString();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Writes the {@code \t} tab symbol by the number of times specified.
	 *
	 * @param writer the writer to use
	 * @param times  the number of times to write a tab symbol
	 * @throws IOException
	 */
	public static void indent(Writer writer, int times) throws IOException {
		for (int i = 0; i < times; i++) {
			writer.write('\t');
		}
	}

	/**
	 * Indents and then writes the element.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @param times   the number of times to indent
	 * @throws IOException
	 *
	 * @see #indent(String, Writer, int)
	 * @see #indent(Writer, int)
	 */
	public static void indent(Integer element, Writer writer, int times) throws IOException {
		indent(element.toString(), writer, times);
	}

	/**
	 * Indents and then writes the element.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @param times   the number of times to indent
	 * @throws IOException
	 *
	 * @see #indent(Writer, int)
	 */
	public static void indent(String element, Writer writer, int times) throws IOException {
		indent(writer, times);
		writer.write(element);
	}

	/**
	 * Writes the element surrounded by {@code " "} quotation marks.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @throws IOException
	 */
	public static void quote(String element, Writer writer) throws IOException {
		writer.write('"');
		writer.write(element);
		writer.write('"');
	}

	/**
	 * Indents and then writes the element surrounded by {@code " "} quotation
	 * marks.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @param times   the number of times to indent
	 * @throws IOException
	 *
	 * @see #indent(Writer, int)
	 * @see #quote(String, Writer)
	 */
	public static void quote(String element, Writer writer, int times) throws IOException {
		indent(writer, times);
		quote(element, writer);
	}

	/**
	 * A simple main method that demonstrates this class.
	 *
	 * @param args unused
	 */
	public static void main(String[] args) {
		TreeSet<Integer> elements = new TreeSet<>();
		System.out.println("Empty:");
		System.out.println(asArray(elements));

		elements.add(65);
		System.out.println("\nSingle:");
		System.out.println(asArray(elements));

		elements.add(66);
		elements.add(67);
		System.out.println("\nSimple:");
	}
}
