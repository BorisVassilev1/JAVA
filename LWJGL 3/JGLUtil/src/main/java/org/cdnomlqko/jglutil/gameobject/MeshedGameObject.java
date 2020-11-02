package org.cdnomlqko.jglutil.gameobject;

import org.cdnomlqko.jglutil.data.Material;
import org.cdnomlqko.jglutil.data.Renderable;
import org.cdnomlqko.jglutil.data.Scene;
import org.cdnomlqko.jglutil.mesh.Mesh;
import org.cdnomlqko.jglutil.shader.VFShader;

/**
 * This class is a {@link GameObject} and {@link Renderable} that has a {@link Material}
 * @author CDnoMlqko
 *
 */
public class MeshedGameObject extends  Renderable{
	
	private Material material;
	private int materialId;
	
	@Override
	public void register(Scene scene) {
		super.register(scene);
		
		materialId = scene.registerMaterial(material);
		if(shader == null) 
			shader = scene.getDefaultShader();
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public MeshedGameObject(Mesh mesh, Material material, VFShader shader) {
		super(mesh, shader);
		this.material = material;
	}
	
	public int getMaterialId() {
		return materialId;
	}

	@Override
	public void prepareForRender() {
		// Set uniforms
		if(shader.hasUniform("worldMatrix"))
			shader.setUniform("worldMatrix", transform.getWorldMatrix());
		
		if(shader.hasUniform("material_index"))
			shader.setUniform("material_index", getMaterialId());
		
		if(shader.hasUniform("texture_sampler"))
			shader.setUniform("texture_sampler", 0);
	}
}
