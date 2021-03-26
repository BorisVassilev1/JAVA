package org.cdnomlqko.jglutil.examples;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL46.*;

import org.cdnomlqko.jglutil.Input;
import org.cdnomlqko.jglutil.Renderer;
import org.cdnomlqko.jglutil.Time;
import org.cdnomlqko.jglutil.Window;
import org.cdnomlqko.jglutil.data.textures.Texture2D;
import org.cdnomlqko.jglutil.gameobject.ShadedGameObject;
import org.cdnomlqko.jglutil.mesh.MeshUtils;
import org.cdnomlqko.jglutil.shader.ComputeShader;
import org.cdnomlqko.jglutil.shader.VFShader;
import org.cdnomlqko.jglutil.utils.FramerateManager;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;

public class MoldSimulation extends ApplicationBase{

	Texture2D texture;
	Texture2D texture2;
	ComputeShader compute;
	ComputeShader generator;
	ComputeShader sub;
	ComputeShader blur;
	ComputeShader copy;
	
	ShadedGameObject renderingQuad;
	VFShader renderingQuadShader;
	
	Time time;
	FramerateManager frm;
	
	Window window;
	Input input;
	
	int agentsBufferId = -1;
	int agents1 = 1000, agents2 = 100;
	int agentsCount = agents1 * agents2;
	int agentSize = 16;
	
	@Override
	public void init() {
		window = new Window("mold simulation", 1200, 900, false, true);
		texture = new Texture2D(window.getWidth(), window.getHeight());
		texture.bindImage(0);
		texture2 = new Texture2D(window.getWidth(), window.getHeight());
		texture2.bindImage(1);
		
		input = new Input(window);
		
		time = new Time();
		frm = new FramerateManager(time);
	
		frm.setSecondPassedCallback((framerate) -> {
			System.out.println("Framerate: " + framerate);
		});
		
		renderingQuadShader = new VFShader("./res/shaders/verfrag_shaders/TextureOnScreenVertexShader.vs",
				"./res/shaders/verfrag_shaders/TextureOnScreenFragmentShader.fs");
		renderingQuad = new ShadedGameObject(MeshUtils.makeQuad(2f), renderingQuadShader);
		
		compute = new ComputeShader("./res/shaders/compute_shaders/MoldSimulation.comp");
		generator = new ComputeShader("./res/shaders/compute_shaders/AgentGenerator.comp");
		sub = new ComputeShader("./res/shaders/compute_shaders/Substract.comp");
		blur = new ComputeShader("./res/shaders/compute_shaders/Blur.comp");
		copy = new ComputeShader("./res/shaders/compute_shaders/Copy.comp");
		
		agentsBufferId = glGenBuffers();
		int buffer_size = agentsCount * agentSize;
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, agentsBufferId);
		glBufferData(GL_SHADER_STORAGE_BUFFER, BufferUtils.createByteBuffer(buffer_size), GL_DYNAMIC_DRAW);
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, 0);
		
		compute.setSSBO("Agents", agentsBufferId);
		generator.setSSBO("Agents", agentsBufferId);
		
		compute.bind();
		compute.setUniform("agents_dim", new Vector2i(agents1, agents2));
		compute.unbind();
		
		generator.bind();
		generator.setUniform("agents_dim", new Vector2i(agents1, agents2));
		generator.unbind();
		
		Renderer.Compute(generator, agents1, agents2, 1);
	}

	@Override
	public void loop() {
		
		while (!window.shouldClose()) {
			time.updateTime();
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glfwPollEvents();
			
			input.update();
			
			Renderer.Compute(compute, agents1, agents2, 1);
			Renderer.Compute(sub, texture.getWidth(), texture.getHeight(), 1);
			Renderer.Compute(blur, texture.getWidth(), texture.getHeight(), 1);
			Renderer.Compute(copy, texture.getWidth(), texture.getHeight(), 1);			
			
			texture.bind();
			Renderer.draw(renderingQuad);
			texture.unbind();
			
			window.swapBuffers();
			frm.update();
		}
		
	}

	@Override
	public void cleanup() {
		frm.stop();
	}
	
	public static void main(String [] args) {
		new MoldSimulation().run();
	}
}
