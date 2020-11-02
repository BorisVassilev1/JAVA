package org.cdnomlqko.jglutil.data;

import org.cdnomlqko.jglutil.Renderer;
import org.cdnomlqko.jglutil.gameobject.GameObject;
import org.cdnomlqko.jglutil.mesh.Mesh;
import org.cdnomlqko.jglutil.shader.VFShader;

/**
 * An object that can be rendered by the {@link Renderer}. It contains a {@link VFShader} and a {@link Mesh}
 * @author CDnoMlqko
 *
 */
public abstract class Renderable extends GameObject{
	protected VFShader shader;
	protected Mesh mesh;
	
	public Renderable(Mesh mesh, VFShader shader) {
		this.mesh = mesh;
		this.shader = shader;
	}
	/**
	 * This is called before rendering.
	 */
	public abstract void prepareForRender();

	public VFShader getShader() {
		return shader;
	}

	public void setShader(VFShader shader) {
		this.shader = shader;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}
}
