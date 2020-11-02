package org.cdnomlqko.jglutil.gameobject;

import org.cdnomlqko.jglutil.data.Renderable;
import org.cdnomlqko.jglutil.mesh.Mesh;
import org.cdnomlqko.jglutil.shader.VFShader;

/**
 * this is a {@link GameObject} and {@link Renderable} that does not have a material
 * @author CDnoMlqko
 *
 */
public class ShadedGameObject extends Renderable{
	
	public ShadedGameObject(Mesh mesh, VFShader shader) {
		super(mesh, shader);
	}
	
	@Override
	public void prepareForRender() {
		if(shader.hasUniform("worldMatrix")) {
			shader.setUniform("worldMatrix", transform.getWorldMatrix());
		}
		
		// Will use the texture bound to GL_TEXTURE0
		if(shader.hasUniform("texture_sampler"))
			shader.setUniform("texture_sampler", 0);
	}
}
