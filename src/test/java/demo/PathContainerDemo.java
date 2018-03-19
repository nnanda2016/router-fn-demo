package demo;

import org.springframework.http.server.PathContainer;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
public class PathContainerDemo {
	public static void main(String[] args) {
		PathContainer pathContainer = PathContainer.parsePath("");
		System.out.println(pathContainer.elements().size());
		
		pathContainer = PathContainer.parsePath("/api/users/123?q=abc");
		
		System.out.println(pathContainer.elements().get(5).value());
		
		final PathContainer subPath = pathContainer.subPath(pathContainer.elements().size() - 3, pathContainer.elements().size());
		System.out.println(subPath);
		
	}
}
