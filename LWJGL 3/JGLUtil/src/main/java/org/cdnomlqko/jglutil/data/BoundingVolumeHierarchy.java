package org.cdnomlqko.jglutil.data;

import static org.lwjgl.opengl.GL46.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.cdnomlqko.jglutil.Renderer;
import org.cdnomlqko.jglutil.gameobject.MeshedGameObject;
import org.cdnomlqko.jglutil.mesh.BasicMesh;
import org.cdnomlqko.jglutil.mesh.MeshUtils;
import org.cdnomlqko.jglutil.shader.ShaderUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

public class BoundingVolumeHierarchy implements Bufferable {
	
	public Node root;
	
	public static MeshedGameObject display_object = new MeshedGameObject(MeshUtils.makeLineCube(1,1,1), new Material(new Vector3f(1.0f)), ShaderUtils.getUnlitShader());
	
	public static Random rand = new Random();
	
	public int max_depth = 0;
	
	public ArrayList<Boundable> objects;
	
	public static class Node implements Bufferable{
		public Node left = null;
		public Node right = null;
		
		public AABB box;
		
		public int depth;
		public int split_axis;
		
		public int object_index = -1;
		
		public Node(Node child1, Node child2) {
			this.split_axis = -1;
			this.left = child1;
			this.right = child2;
			this.depth = 0;
		}
		
		public Node(List<Pair<AABB, Integer>> objects, int depth, int[] max_depth) {

			this.depth = depth;
			if(depth > max_depth[0]) max_depth[0] = depth;
			
			if(objects.size() == 1) {
				this.box = objects.get(0).first;
				this.object_index = objects.get(0).second;
				return;
			}
			
			this.split_axis = (int)Math.floor(rand.nextFloat() * 3);
			
			Collections.sort(objects, new Comparator<Pair<AABB, Integer>>() {
				@Override
				public int compare(Pair<AABB, Integer> o1, Pair<AABB, Integer> o2) {
					return Float.compare(o1.first.center.get(split_axis),o2.first.center.get(split_axis));
				}
			});
			
			this.left = new Node(objects.subList(0, objects.size() / 2)				 , depth + 1, max_depth);
			this.right = new Node(objects.subList(objects.size() / 2, objects.size()), depth + 1, max_depth);
			
			this.box = AABB.union(left.box, right.box);
		}
		
		public static int size = AABB.size + 16;
		
		@Override
		public void writeToBuffer(ByteBuffer buff, int offset) {
			box.writeToBuffer(buff, offset);
			buff.putInt(offset + AABB.size, object_index);
			
			int index = offset / size;
			if(left != null)
				left.writeToBuffer(buff, (2 * index + 1) * size);
			if(right != null)
				right.writeToBuffer(buff, (2 * index + 2) * size);
		}
		
		public void draw() {
			//if(rand.nextFloat() > 0.99) {
				box.draw();
			//}
			if(left != null)
				left.draw();
			if(right != null)
				right.draw();
		}
		
		@Override
		public String toString() {
			return super.toString();
		}
	}
	
	public static class AABB implements Bufferable {
		public Vector3f min;
		public Vector3f max;
		
		public Vector3f center;
		
		public AABB(Vector3f min, Vector3f max) {
			this.min = min;
			this.max = max;
			this.center = new Vector3f(min);
			center.add(max).div(2.f);
		}
		
		public AABB(AABB box) {
			this.min = new Vector3f(box.min);
			this.max = new Vector3f(box.max);
			this.center = new Vector3f(box.center);
		}
		
		public AABB(List<Pair<AABB, Integer>> list) {
			this.min = new Vector3f(Float.MAX_VALUE);
			this.max = new Vector3f(Float.MIN_VALUE);
			for(Pair<AABB, Integer> b : list) {
				min.min(b.first.min);
				max.max(b.first.max);
			}
			recalcCenter();
		}
		
		public void recalcCenter() {
			this.center.set(min).add(max).mul(0.5f);
		}
		
		public static int size = 48;
		
		@Override
		public void writeToBuffer(ByteBuffer buff, int offset) {
			buff.putFloat(offset     , min.x);
			buff.putFloat(offset + 4 , min.y);
			buff.putFloat(offset + 8 , min.z);
			buff.putFloat(offset + 12, 0f);
			buff.putFloat(offset + 16, max.x);
			buff.putFloat(offset + 20, max.y);
			buff.putFloat(offset + 24, max.z);
			buff.putFloat(offset + 28, 0f);
			buff.putFloat(offset + 32, center.x);
			buff.putFloat(offset + 36, center.y);
			buff.putFloat(offset + 40, center.z);
			buff.putFloat(offset + 44, 0f);
		}
		
		public static AABB union(AABB b1, AABB b2) {
			Vector3f min = new Vector3f(b1.min), max = new Vector3f(b1.max);
			min.min(b2.min);
			max.max(b2.max);
			return new AABB(min,max);
		}
		
		public void unite(AABB other) {
			this.min.min(other.min);
			this.max.max(other.max);
			recalcCenter();
		}
		
		public float volume() {
			Vector3f size = new Vector3f();
			max.sub(min, size);
			return size.x * size.y * size.z;
		}
		
		public void draw() {
			Vector3f size = new Vector3f();
			max.sub(min, size);
			
			display_object.transform.setScale(size.x, size.y, size.z);
			display_object.transform.setPosition(center);
			display_object.transform.updateWorldMatrix();
			Renderer.draw(display_object);
		}
		
		@Override
		public String toString() {
			return "AABB [min=" + min + ", max=" + max + "]";
		}
	}
	
	public interface Boundable {
		
		public BoundingVolumeHierarchy.AABB getBoundingBox();	
	
	}
	
	public class Pair<A, B> {
		public A first;
		public B second;
		
		public Pair(A first, B second) {
			this.first = first;
			this.second = second;
		}
	}
	
	public BoundingVolumeHierarchy(BasicMesh mesh, Transformation transform) {
		int indices_gl = mesh.getIbo();
		int vertices_gl = mesh.getVertices().getBufferId();
		
		glBindBuffer(GL_ARRAY_BUFFER, indices_gl);
		int i_size = glGetBufferParameteri(GL_ARRAY_BUFFER, GL_BUFFER_SIZE);
		IntBuffer indices = BufferUtils.createIntBuffer(i_size / 4);
		glGetBufferSubData(GL_ARRAY_BUFFER, 0, indices);
		
		glBindBuffer(GL_ARRAY_BUFFER, vertices_gl);
		int v_size = glGetBufferParameteri(GL_ARRAY_BUFFER, GL_BUFFER_SIZE);
		FloatBuffer vertices = BufferUtils.createFloatBuffer(v_size / 4);
		glGetBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		ArrayList<Pair<AABB, Integer>> boxes = new ArrayList<>();
		this.objects = new ArrayList<Boundable>();
		
		int i = 0;
		while(indices.hasRemaining()) {
			int i0 = indices.get() * 3;
			int i1 = indices.get() * 3;
			int i2 = indices.get() * 3;
			
			Vector3f 
			v0 = new Vector3f(
					vertices.get(i0    ),
					vertices.get(i0 + 1),
					vertices.get(i0 + 2)),
			v1 = new Vector3f(
					vertices.get(i1    ),
					vertices.get(i1 + 1),
					vertices.get(i1 + 2)),
			v2 = new Vector3f(
					vertices.get(i2    ),
					vertices.get(i2 + 1),
					vertices.get(i2 + 2));
			
			Matrix4f mat = transform.getWorldMatrix();
			
			v0.mulPosition(mat);
			v1.mulPosition(mat);
			v2.mulPosition(mat);
			
			Triangle t = new Triangle(v0, v1, v2);
			
			AABB box = t.getBoundingBox();
			
			float phi = 0.000001f;
			
			box.min.sub(phi, phi, phi);
			box.max.add(phi, phi, phi);
			boxes.add(new Pair<AABB, Integer>(box, i));
			objects.add(t);
			i ++;
		}
		System.out.println(boxes.size());
		int[] m_depth = {0};
		root = new Node(boxes, 0, m_depth);
		max_depth = m_depth[0];
		
	}
	
	public void draw() {
		root.draw();
	}

	@Override
	public void writeToBuffer(ByteBuffer buff, int offset) {
		root.writeToBuffer(buff, 0);
	}
	
}
