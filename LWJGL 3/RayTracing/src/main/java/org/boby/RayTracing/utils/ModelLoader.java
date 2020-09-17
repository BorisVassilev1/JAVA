package org.boby.RayTracing.utils;

import static org.lwjgl.assimp.Assimp.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.boby.RayTracing.mesh.BasicMesh;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.system.MemoryUtil;

public class ModelLoader {
	public static BasicMesh load(String fileName) {
		int flags = aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_FixInfacingNormals;
		AIScene scene = aiImportFile(fileName, flags);
		if (scene == null) {
			System.err.println("error");
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
		
		// FloatBuffer colors = BufferUtils.createFloatBuffer(vertices_count * 3);
		// for(int i = 0; i < colors.capacity(); i ++) {
		// colors.put(1.0f);
		// }
		// colors.flip();
		//
		// FloatBuffer texCoords = BufferUtils.createFloatBuffer(vertices_count * 2);

		mesh = new BasicMesh(vertices, normals, indices);
		
		MemoryUtil.memFree(vertices);
		MemoryUtil.memFree(normals);
		MemoryUtil.memFree(indices);
		
		return mesh;
	}
}
