package org.cdnomlqko.jglutil.utils;

import static org.lwjgl.assimp.Assimp.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.cdnomlqko.jglutil.examples.pathTracing.PathTracingExample;
import org.cdnomlqko.jglutil.mesh.BasicMesh;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;
import org.lwjgl.system.MemoryUtil;

/**
 * This class loads models using {@link Assimp}
 * @author CDnoMlqko
 *
 */
public class ModelLoader {
	public static BasicMesh load(String fileName) {
		int flags = aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_FixInfacingNormals;
		AIScene scene = aiImportFile(fileName, flags);
		if (scene == null) {
			System.err.println("Error while loading model: " + fileName);
		}

		PointerBuffer buff = scene.mMeshes();
		AIMesh aiMesh = AIMesh.createSafe(buff.get(0));

		BasicMesh mesh = null;

		// process vertices:
		int vertices_count = aiMesh.mNumVertices();
		FloatBuffer vertices = MemoryUtil.memCallocFloat(vertices_count * 3);
		AIVector3D.Buffer aiVertices = aiMesh.mVertices();
		while (aiVertices.remaining() > 0) {
			AIVector3D aiVertex = aiVertices.get();
			vertices.put(aiVertex.x());
			vertices.put(aiVertex.y());
			vertices.put(aiVertex.z());
		}
		vertices.flip();
		
		// proces indices:
		int indices_count = aiMesh.mNumFaces();
		IntBuffer indices = MemoryUtil.memCallocInt(indices_count * 3);
		AIFace.Buffer aiIndices = aiMesh.mFaces();
		while (aiIndices.remaining() > 0) {
			AIFace aiFace = aiIndices.get();
			IntBuffer ibuff = aiFace.mIndices();
			indices.put(ibuff.get());
			indices.put(ibuff.get());
			indices.put(ibuff.get());
		}
		indices.flip();
		
		// process normals
		FloatBuffer normals = MemoryUtil.memCallocFloat(vertices_count * 3);
		AIVector3D.Buffer aiNormals = aiMesh.mNormals();
//		if(aiNormals != null)
		while (aiNormals.remaining() > 0) {
			AIVector3D aiNormal = aiNormals.get();
			normals.put(aiNormal.x());
			normals.put(aiNormal.y());
			normals.put(aiNormal.z());
		}
		normals.flip();
		
		
		
		FloatBuffer texCoords = MemoryUtil.memCallocFloat(vertices_count * 2);
		AIVector3D.Buffer aiTexCoords = aiMesh.mTextureCoords(0);
		if(aiTexCoords != null) {
			while(aiTexCoords.remaining() > 0) {
				AIVector3D aitexCoord = aiTexCoords.get();
				texCoords.put(aitexCoord.x());
				texCoords.put(aitexCoord.y());
			}
			texCoords.flip();
		}
		
		FloatBuffer colors = MemoryUtil.memCallocFloat(vertices_count * 4);
		AIColor4D.Buffer aiColors = aiMesh.mColors(0);
		if(aiColors != null) {
			for(int i = 0; i < colors.capacity(); i ++) {
				AIColor4D aiColor = aiColors.get();
				colors.put(aiColor.r());
				colors.put(aiColor.g());
				colors.put(aiColor.b());
				colors.put(aiColor.a());
			}
			colors.flip();
		}
		
		mesh = new BasicMesh(vertices, normals, indices, texCoords, colors);
		
		MemoryUtil.memFree(vertices);
		MemoryUtil.memFree(normals);
		MemoryUtil.memFree(indices);
		MemoryUtil.memFree(texCoords);
		MemoryUtil.memFree(colors);
		
		return mesh;
	}
}
