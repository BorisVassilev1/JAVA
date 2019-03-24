package main;

import engine.io.Window;
import engine.maths.Vector3f;
import engine.rendering.Renderer;
import engine.rendering.models.ModelEntity;
import engine.rendering.models.TexturedModel;
import engine.shaders.BasicShader;
 
public class Main {
	private static final int WIDTH = 800, HEIGHT = 600, FPS = 60;
	private static Window window = new Window(WIDTH, HEIGHT, FPS, "LWJGL");
	private static BasicShader shader = new BasicShader();
	private static Renderer renderer = new Renderer(shader);
	
    public static void main(String[] args) {
    	window.create();
    	window.setBackgroundColor(1.0f, 0.0f, 0.0f);
    	shader.create();
        
        TexturedModel model = new TexturedModel(new float[] {
        		-0.5f, 0.5f, 0,  //TOP LEFT V0
				 0.5f, 0.5f, 0,  //TOP RIGHT V1
				 0.5f, -0.5f, 0, //BOTTOM RIGHT V2
				-0.5f, -0.5f, 0  //BOTTOM LEFT V3
        }, new float[] {
        		 0, 0,           //TOP LEFT V0
        		 1, 0,           //TOP RIGHT V1
        		 1, 1,           //BOTTOM RIGHT V2
        		 0, 1            //BOTTOM LEFT V3
        }, new int[] {
        		 0, 1, 2,        //Triangle 1
				 2, 3, 0         //Triangle 2
        }, "beautiful.png");
        
        ModelEntity entity = new ModelEntity(model, new Vector3f(0, 0, 1), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
        
        while (!window.closed()) {
        	if (window.isUpdating()) {
        		window.update();
	            shader.bind();
	            shader.useMatrices();
	            renderer.renderModelEntity(entity);
	            shader.unbind();
	            window.swapBuffers();
        	}
        }
 
        model.remove();
        shader.remove();
        window.stop();
    }
}