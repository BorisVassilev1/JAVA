package org.boby.RayTracing.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.lwjgl.system.MemoryStack;

/**
 * A class that loads GLSL Shader Files and adds #include functionality
 * 
 * @author Boby
 *
 */
public class ShaderParser {
	public static String ParseShaderFile(String file, ArrayList<String> uniform_names, ArrayList<String> ssbo_names) {
		StringBuilder parsed = new StringBuilder();
		
		AtomicInteger libraryLines = new AtomicInteger();
		AtomicInteger lines = new AtomicInteger();
		
		// TODO: line count must be kept track of because of possible future error tracking
		// example: when you have 100 lines of includes and 200 lines of shader code, the compiler may throw an error on line 255 
		// because the contents of the two files are just appended.
		// TODO: it counts one line less when the file ends with an empty line. I have no idea how to fix it now.
		ParseRecursively(parsed, file, 0, lines, libraryLines, uniform_names, ssbo_names);
		
//		System.out.println("Successfully parsed " + lines + " lines of code with " + libraryLines + " lines of included code. Total " + (lines.get() + libraryLines.get()) + " lines.");
		
		return parsed.toString();
	}
	
	private static void ParseRecursively(StringBuilder parsed, String file, int depth, AtomicInteger lines, AtomicInteger libLines, ArrayList<String> uniform_names, ArrayList<String> ssbo_names) {
		//String filePathPrefix = file.substring(0,file.lastIndexOf("/"));
		BufferedReader reader = null;
		
		try {
			String line;
			reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null)
			{
				//System.out.println(line);
				if(depth == 0) {
					lines.incrementAndGet();
				}
				if(line.startsWith("#include ")) {
					String fileToInclude = line.substring(10, line.length() - 1);
					parsed.append("//").append(line).append("\n");
					ParseRecursively(parsed, "./res/shaders/includes/" + fileToInclude, depth + 1, lines, libLines, uniform_names, ssbo_names);
				}
				else if(depth == 0 && line.startsWith("uniform ")) {
					parsed.append(line).append("\n");
					int word_begin = line.substring(8).indexOf(' ') + 1 + 8;
					int word_end = line.indexOf(';');
					String uniform_name = line.substring(word_begin, word_end);
					//System.out.println("\"" + uniform_name + "\"");
					uniform_names.add(uniform_name);
				}
				else if(depth == 0 && line.startsWith("layout(std430) buffer") ) {
					parsed.append(line).append("\n");
					int word_begin = 22;
					int word_end = line.substring(22).indexOf(' ') + 22;
					String ssbo_name = line.substring(word_begin, word_end);
//					System.out.println("\"" + ssbo_name + "\"");
					ssbo_names.add(ssbo_name);
				}
				else {
					parsed.append(line).append("\n");
					if(depth != 0) {
						libLines.incrementAndGet();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
