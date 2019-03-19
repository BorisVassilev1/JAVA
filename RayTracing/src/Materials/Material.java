package Materials;

import java.awt.Color;

import org.newdawn.slick.opengl.Texture;

public class Material {
	public Color albedo;
	public Color reflection;
	public float reflectionIndex;
	public float refractionIndex;// currently not used
	public Texture tex; // currently not used
	public Texture normalMap; // currently not used
}
