package org.boby.RayTracing.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

/**
 * A class that loads GLSL Shader Files and adds #include functionality
 * 
 * @author Boby
 *
 */
public class ShaderParser {
	public static String ParseShaderFile(String file) {
		StringBuilder parsed = new StringBuilder();
		
		int libraryLines = 0;
		int lines = 0;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer _libraryLines = stack.mallocInt(1);
			IntBuffer _lines = stack.mallocInt(1);
			
			_lines.put(0);
			_lines.rewind();
			_libraryLines.put(0);
			_libraryLines.rewind();
			
			ParseRecursively(parsed, file, 0, _lines, _libraryLines);
			libraryLines = _libraryLines.get();
			lines = _lines.get();
		}
		//System.out.println("parsed " + lines + " lines of code with " + libraryLines + " lines of included code. All: " + (lines + libraryLines));
		return parsed.toString();
	}
	private static void ParseRecursively(StringBuilder parsed, String file, int depth, IntBuffer lines, IntBuffer libLines) {
		String filePathPrefix = file.substring(0,file.lastIndexOf("/"));
		BufferedReader reader = null;
		
		try {
			String line;
			reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null)
			{
				//System.out.println(line);
				if(depth == 0) {
					int a = lines.get();
					lines.rewind();
					lines.put(a + 1);
					lines.rewind();
				}
				if(line.startsWith("#include ")) {
					String fileToInclude = line.substring(10, line.length() - 1);
					parsed.append("//").append(line).append("\n");
					ParseRecursively(parsed, "./res/shaders/includes/" + fileToInclude, depth + 1, lines, libLines);
				} else {
					parsed.append(line).append("\n");
					if(depth != 0) {
						int b = libLines.get();
						libLines.rewind();
						libLines.put(b + 1);
						libLines.rewind();
					}
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
