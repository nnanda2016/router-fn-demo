package demo;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
public class ExtractResourceDemo {
	public static void main(String[] args) {
		extractFromPath("");
		extractFromPath("/users");
		extractFromPath("/users/123/location/3");
	}
	
	private static void extractFromPath(final String path) {
		final String[] pathElementsArr = StringUtils.split(path, "/");
		System.out.println("Path elements array: " + Arrays.toString(pathElementsArr));
		
		final int length = pathElementsArr.length;
		if (length < 1) {
			System.out.println("No id or type can be determined.");
			return;
		}
		
		if (length >= 2) {
			System.out.println("id -> " + pathElementsArr[length -1]);
			System.out.println("type -> " + pathElementsArr[length -2]);
		} else {
			System.out.println("id -> null");
			System.out.println("type -> " + pathElementsArr[0]);
		}
	}
}
