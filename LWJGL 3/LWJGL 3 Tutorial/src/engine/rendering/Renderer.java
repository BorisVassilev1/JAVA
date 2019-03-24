package engine.rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import engine.rendering.models.ModelEntity;
import engine.rendering.models.TexturedModel;
import engine.rendering.models.UntexturedModel;
import engine.shaders.BasicShader;
 
public class Renderer {
	private BasicShader shader;
	
	public Renderer(BasicShader shader) {
		this.shader = shader;
	}
	
    public void renderModel(UntexturedModel model){
        GL30.glBindVertexArray(model.getVertexArrayID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }
    
    public void renderTexturedModel(TexturedModel model){
        GL30.glBindVertexArray(model.getVertexArrayID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getMaterial().getTextureID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }
    
    public void renderModelEntity(ModelEntity entity){
        GL30.glBindVertexArray(entity.getModel().getVertexArrayID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        shader.loadTransformationMatrix(entity.getTransformationMatrix());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getMaterial().getTextureID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }
}