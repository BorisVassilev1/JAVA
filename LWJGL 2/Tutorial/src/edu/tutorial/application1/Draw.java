package edu.tutorial.application1;

import static org.lwjgl.opengl.GL11.*;


import org.newdawn.slick.opengl.Texture;

public class Draw {
	
	public static void drawCube(float x, float y, float z, float rx, float ry, float rz, Texture tex)
	{
		glPushMatrix();
		{
			glTranslatef(x, y, z);
			glRotatef(rx, 1, 0, 0);
			glRotatef(ry, 0, 1, 0);
			glRotatef(rz, 0, 0, 1);
			tex.bind();

			glBegin(GL_QUADS);
			{
				glColor3f(1, 1, 0);

				glTexCoord2f(0, 0); glVertex3f(-1, -1, 1);
				glTexCoord2f(0, 1); glVertex3f(-1, 1, 1);
				glTexCoord2f(1, 1); glVertex3f(1, 1, 1);
				glTexCoord2f(1, 0); glVertex3f(1, -1, 1);

				glColor3f(1, 0, 0);

				glTexCoord2f(0, 0); glVertex3f(-1, -1, -1);
				glTexCoord2f(0, 1); glVertex3f(-1, 1, -1);
				glTexCoord2f(1, 1); glVertex3f(1, 1, -1);
				glTexCoord2f(1, 0); glVertex3f(1, -1, -1);

				glColor3f(0, 1, 0);

				glTexCoord2f(0, 0); glVertex3f(-1, -1, -1);
				glTexCoord2f(0, 1); glVertex3f(-1, -1, 1);
				glTexCoord2f(1, 1); glVertex3f(-1, 1, 1);
				glTexCoord2f(1, 0); glVertex3f(-1, 1, -1);

				glColor3f(1, 0, 1);

				glTexCoord2f(0, 0); glVertex3f(1, -1, -1);
				glTexCoord2f(0, 1); glVertex3f(1, -1, 1);
				glTexCoord2f(1, 1); glVertex3f(1, 1, 1);
				glTexCoord2f(1, 0); glVertex3f(1, 1, -1);

				glColor3f(0, 0, 1);

				glTexCoord2f(0, 0); glVertex3f(-1, -1, -1);
				glTexCoord2f(0, 1); glVertex3f(1, -1, -1);
				glTexCoord2f(1, 1); glVertex3f(1, -1, 1);
				glTexCoord2f(1, 0); glVertex3f(-1, -1, 1);

				glColor3f(0, 1, 1);

				glTexCoord2f(0, 0); glVertex3f(-1, 1, -1);
				glTexCoord2f(0, 1); glVertex3f(1, 1, -1);
				glTexCoord2f(1, 1); glVertex3f(1, 1, 1);
				glTexCoord2f(1, 0); glVertex3f(-1, 1, 1);

			}
			glEnd();
		}
		glPopMatrix();
	}
}
