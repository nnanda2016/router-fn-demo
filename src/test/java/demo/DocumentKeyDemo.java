package demo;

import org.apache.commons.lang3.StringUtils;

import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
public class DocumentKeyDemo {
	public static void main(String[] args) {
		process("user", 123);
		process("response5", 98233);
		process("audiences-7", 11993);
		extract("doc-1-345");
		extract("xyz-2-");
		extract("");
		extract(null);
	}
	
	private static void process(final String resourceType, final int id) {
		final int index = StringUtils.length(resourceType);
		final String key = new StringBuilder()
				.append(resourceType)
				.append("-")
				.append(id)
				.toString();
		final Tuple2<String, Integer> keyHolder = Tuples.of(key, index);
		final int extractedId = getId(keyHolder);
		System.out.printf("[Key Holder: %s][Key: %s][Extracted Id: %d]\n", keyHolder, keyHolder.getT1(), extractedId);
		extract(key);
	}
	
	private static int getId(final Tuple2<String, Integer> keyHolder) {
		return Integer.valueOf(StringUtils.substringAfterLast(keyHolder.getT1(), "-"));
	}
	
	private static void extract(final String documentKey) {
		final int index = StringUtils.lastIndexOf(documentKey, "-");
		System.out.println("======> [doc type: " + StringUtils.substring(documentKey, 0, index) + "][id: " + StringUtils.substring(documentKey, index + 1) + "]");
	}
}
