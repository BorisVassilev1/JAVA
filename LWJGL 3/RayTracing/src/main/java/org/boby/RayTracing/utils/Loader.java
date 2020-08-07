package org.boby.RayTracing.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Loader {
	/**
	 * a public method for reading text files
	 * 
	 * @param file
	 *            - file path
	 * @return the file in the form of a String
	 */
	public static String readFile(String file)
	{
		BufferedReader reader = null;
		StringBuilder string = new StringBuilder();
		try {
			String line;
			reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null)
			{
				string.append(line).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return string.toString();
	}
}
