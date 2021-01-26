package org.cdnomlqko.jglutil;

import org.cdnomlqko.jglutil.mesh.MeshUtils;
import org.cdnomlqko.jglutil.shader.ShaderUtils;
import org.lwjgl.Version;

public class JGLUtil {
	
	public static void init() {
		MeshUtils.init();
		ShaderUtils.init();
	}
	
	public static void delete() {
		MeshUtils.delete();
		ShaderUtils.delete();
	}
}
