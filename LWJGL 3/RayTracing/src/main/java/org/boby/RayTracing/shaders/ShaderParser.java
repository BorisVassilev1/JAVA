package org.boby.RayTracing.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.lwjgl.opengl.GL46.*;

/**
 * A class that loads GLSL Shader Files and adds #include functionality
 * 
 * @author Boris
 *
 */
public class ShaderParser {
	public static String ParseShaderFile(String file) {
		StringBuilder parsed = new StringBuilder();
		
		AtomicInteger libraryLines = new AtomicInteger();
		AtomicInteger lines = new AtomicInteger();
		
		// TODO: line count must be kept track of because of possible future error tracking
		// example: when you have 100 lines of includes and 200 lines of shader code, the compiler may throw an error on line 255 
		// because the contents of the two files are just appended.
		// TODO: it counts one line less when the file ends with an empty line. I have no idea how to fix it now.
		ParseRecursively(parsed, file, 0, lines, libraryLines);
		
//		System.out.println("Successfully parsed " + lines + " lines of code with " + libraryLines + " lines of included code. Total " + (lines.get() + libraryLines.get()) + " lines.");
		
		return parsed.toString();
	}
	
	public static void ParseRecursively(StringBuilder parsed, String file, int depth, AtomicInteger lines, AtomicInteger libLines) {
		BufferedReader reader = null;
		
		try {
			String line;
			reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null)
			{
				if(depth == 0) {
					lines.incrementAndGet();
				}
				if(line.startsWith("#include ")) {
					String fileToInclude = line.substring(10, line.length() - 1);
					parsed.append("//").append(line).append("\n");
					ParseRecursively(parsed, "./res/shaders/includes/" + fileToInclude, depth + 1, lines, libLines);
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

	public static void getBlockUniforms(Shader shader) {
		int program = shader.getProgramId();
		
		int _numBlocks[] = new int[1];
		glGetProgramInterfaceiv(program, GL_UNIFORM_BLOCK, GL_ACTIVE_RESOURCES, _numBlocks);
		int numBlocks = _numBlocks[0];

		int blockProperties[] = { GL_NUM_ACTIVE_VARIABLES, GL_BUFFER_BINDING };
		int activeUnifProp[] = { GL_ACTIVE_VARIABLES };
		int unifProperties[] = { GL_TYPE, GL_LOCATION, GL_OFFSET};

		for (int blockIx = 0; blockIx < numBlocks; ++blockIx) {
			
			String blockName;
			blockName = glGetProgramResourceName(program, GL_UNIFORM_BLOCK, blockIx);
			//System.out.println(blockName + " " + blockIx);

			int _numActiveUnifs[] = new int[2];
			glGetProgramResourceiv(program, GL_UNIFORM_BLOCK, blockIx, blockProperties, null, _numActiveUnifs);
			int numActiveUnifs = _numActiveUnifs[0];
			int buffer_binding = _numActiveUnifs[1];
			
			shader.createUBO(blockName, buffer_binding);
			
			if (numActiveUnifs == 0) {
				continue;
			}

			int blockUnifs[] = new int[numActiveUnifs];
			glGetProgramResourceiv(program, GL_UNIFORM_BLOCK, blockIx, activeUnifProp, _numActiveUnifs, blockUnifs);

			for (int unifIx = 0; unifIx < numActiveUnifs; ++unifIx) {
				int values[] = new int[3];

				glGetProgramResourceiv(program, GL_UNIFORM, blockUnifs[unifIx], unifProperties, null, values);

				String name;
				name = glGetProgramResourceName(program, GL_UNIFORM, blockUnifs[unifIx]);
				//System.out.println("\t" + name + " " + values[0] + " " + values[2]);
			}
		}
	}
	
	public static void getNonBlockUniforms(Shader shader) {
		int program = shader.getProgramId();
		
		int _numUniforms[] = new int[1];
		glGetProgramInterfaceiv(program, GL_UNIFORM, GL_ACTIVE_RESOURCES, _numUniforms);
		int numUniforms = _numUniforms[0];
		int properties[] = {GL_BLOCK_INDEX, GL_TYPE, GL_NAME_LENGTH, GL_LOCATION};
		
		for(int unif = 0; unif < numUniforms; ++unif)
		{
			int values[] = new int[4];
			glGetProgramResourceiv(program, GL_UNIFORM, unif, properties, null, values);
			
			if(values[0] != -1) {
				continue;
			}
			
			String name;
			name = glGetProgramResourceName(program, GL_UNIFORM, unif);
			//System.out.println(name + " " + values[1] + " " + values[3]);
			
			shader.createUniform(name);
			
		}
	}
	
	public static void getSSBOs(Shader shader) {
		int program = shader.getProgramId();
		
		int _numSSBOs[] = new int[1];
		glGetProgramInterfaceiv(program, GL_SHADER_STORAGE_BLOCK, GL_ACTIVE_RESOURCES, _numSSBOs);
		
		int blockProperties[] = { GL_NUM_ACTIVE_VARIABLES, GL_BUFFER_BINDING };
		int activeUnifProp[] = { GL_ACTIVE_VARIABLES};
		int unifProperties[] = { GL_TYPE, GL_OFFSET};
		
		for(int block = 0; block < _numSSBOs[0]; block ++) {
			String blockName;
			blockName = glGetProgramResourceName(program, GL_SHADER_STORAGE_BLOCK, block);
			
			int blockInfo[] = new int[2];
			glGetProgramResourceiv(program, GL_SHADER_STORAGE_BLOCK, block, blockProperties, null, blockInfo);
			
			//System.out.println(blockName + " " + block + " " + blockInfo[1]);
			shader.createSSBO(blockName, blockInfo[1]);
			
			int blockUnifs[] = new int[blockInfo[0]];
			glGetProgramResourceiv(program, GL_SHADER_STORAGE_BLOCK, block, activeUnifProp, blockInfo, blockUnifs);
			
			for(int i = 0; i < blockInfo[0]; i ++) {
				int values[] = new int[2];
				glGetProgramResourceiv(program, GL_BUFFER_VARIABLE, blockUnifs[i], unifProperties, null, values);
				
				String variableName;
				variableName = glGetProgramResourceName(program, GL_BUFFER_VARIABLE, blockUnifs[i]);
				//System.out.println("\t" + variableName + " " + " " + values[0] + " " + values[1]);
			}
		}
	}
}
