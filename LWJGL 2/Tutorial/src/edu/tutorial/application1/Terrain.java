package edu.tutorial.application1;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import edu.tutorial.vectors.Vector3;

public class Terrain {
	public static int[][][] generateTerrainArray(int sizeX, int sizeZ, int maxHeight, double noiseStepPerBlock, float seed) {
		int terrain[][][] = new int[sizeX][maxHeight][sizeZ];
		double ns = noiseStepPerBlock;

		float locI = Main.i ;
		locI = locI % 360;
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				int y = (int) (SimplexNoise.noise(x * ns, z * ns, seed) * maxHeight / 2) + maxHeight / 2;
				
				//int y = (int) (Math.sin( Math.sqrt( ( (x-sizeX/2) * (x-sizeX/2) + (z-sizeZ/2) * (z-sizeZ/2))) / 4  - locI ) * maxHeight/8 + maxHeight/2 ) ;
				
				for (int i = 0; i < y; i++) {
					terrain[x][i][z] = 1;
				}
				
				
				// for(int y = 0; y < maxHeight ; y ++)
				// {
				// terrain[x][y][z] = (int) Math.round(SimplexNoise.noise(x * ns, y * ns, z *
				// ns));
				// }
				
				
			}
		}
		return terrain;
	}

	public static void draw(ArrayList<Quad> quads) {
		glBegin(GL_QUADS);
		for (Quad quad : quads) {
			quad.draw();
		}
		glEnd();
	}

	public static ArrayList<Quad> calculateTerrainQuads(int terrain[][][],int sizeX, int sizeY, int sizeZ)
	{
		ArrayList<Quad> quadsList = new ArrayList<Quad>();
		
		for(int x = 0; x < sizeX; x++)
		{
			for(int y = 0; y < sizeY; y++)
			{
				for(int z = 0; z < sizeZ; z++)
				{
					if(terrain[x][y][z] == 1)
					{
						Quad quad;
						
						//positive X
						//glColor3f(1, 0, 1);
						if(x + 1 <sizeX) {
							if(terrain[x + 1][y][z] == 0)
							{
								quad = new Quad(
										new Vector3(0.5f + x, -0.5f + y, -0.5f + z),
										new Vector3(0.5f + x, -0.5f + y,  0.5f + z),
										new Vector3(0.5f + x,  0.5f + y,  0.5f + z),
										new Vector3(0.5f + x,  0.5f + y, -0.5f + z),
										new Vector3(0 , 0.5f , 0));
								quadsList.add(quad);
							}
						}
						else {
							quad = new Quad(
									new Vector3(0.5f + x, -0.5f + y, -0.5f + z),
									new Vector3(0.5f + x, -0.5f + y,  0.5f + z),
									new Vector3(0.5f + x,  0.5f + y,  0.5f + z),
									new Vector3(0.5f + x,  0.5f + y, -0.5f + z),
									new Vector3(0 , 0.5f , 0));
							quadsList.add(quad);
						}
						
						//positive Y
						//glColor3f(0, 1, 0);
						if(y + 1 <sizeY) {
							if(terrain[x][y + 1][z] == 0)
							{
								quad = new Quad(
										new Vector3(-0.5f + x, 0.5f + y, -0.5f + z),
										new Vector3( 0.5f + x, 0.5f + y, -0.5f + z),
										new Vector3( 0.5f + x, 0.5f + y,  0.5f + z),
										new Vector3(-0.5f + x, 0.5f + y,  0.5f + z),
										new Vector3(0 , 0.8f , 0 ));
								quadsList.add(quad);
							}
						}
						else {
							quad = new Quad(
									new Vector3(-0.5f + x, 0.5f + y, -0.5f + z),
									new Vector3( 0.5f + x, 0.5f + y, -0.5f + z),
									new Vector3( 0.5f + x, 0.5f + y,  0.5f + z),
									new Vector3(-0.5f + x, 0.5f + y,  0.5f + z),
									new Vector3(0 , 0.8f , 0 ));
							quadsList.add(quad);
						}
						
						//Positive Z
						//glColor3f(1, 1, 0);
						if(z + 1 <sizeZ) {
							if(terrain[x][y][z + 1] == 0)
							{
								quad = new Quad(
										new Vector3(-0.5f + x, -0.5f + y, 0.5f + z),
										new Vector3(-0.5f + x,  0.5f + y, 0.5f + z),
										new Vector3( 0.5f + x,  0.5f + y, 0.5f + z),
										new Vector3( 0.5f + x, -0.5f + y, 0.5f + z),
										new Vector3(0 , 0.5f , 0));
								quadsList.add(quad);
							}
						}
						else {
							quad = new Quad(
									new Vector3(-0.5f + x, -0.5f + y, 0.5f + z),
									new Vector3(-0.5f + x,  0.5f + y, 0.5f + z),
									new Vector3( 0.5f + x,  0.5f + y, 0.5f + z),
									new Vector3( 0.5f + x, -0.5f + y, 0.5f + z),
									new Vector3(0 , 0.5f , 0));
							quadsList.add(quad);
						}
						
						//Negative X
						//glColor3f(0, 1, 1);
						if(x - 1 >= 0) {
							if(terrain[x - 1][y][z] == 0)
							{
								quad = new Quad(
										new Vector3(-0.5f + x, -0.5f + y, -0.5f + z),
										new Vector3(-0.5f + x, -0.5f + y,  0.5f + z),
										new Vector3(-0.5f + x,  0.5f + y,  0.5f + z),
										new Vector3(-0.5f + x,  0.5f + y, -0.5f + z),
										new Vector3(0 , 0.5f , 0));
								quadsList.add(quad);
							}
						}
						else {
							quad = new Quad(
									new Vector3(-0.5f + x, -0.5f + y, -0.5f + z),
									new Vector3(-0.5f + x, -0.5f + y,  0.5f + z),
									new Vector3(-0.5f + x,  0.5f + y,  0.5f + z),
									new Vector3(-0.5f + x,  0.5f + y, -0.5f + z),
									new Vector3(0 , 0.5f , 0));
							quadsList.add(quad);
						}
						
						//Negative Y
						//glColor3f(0, 0, 1);
						if(y - 1 >= 0) {
							if(terrain[x][y - 1][z] == 0)
							{
								quad = new Quad(
										new Vector3(-0.5f + x, -0.5f + y, -0.5f + z),
										new Vector3( 0.5f + x, -0.5f + y, -0.5f + z),
										new Vector3( 0.5f + x, -0.5f + y,  0.5f + z),
										new Vector3(-0.5f + x, -0.5f + y,  0.5f + z),
										new Vector3(0,0,1));
								quadsList.add(quad);
							}
						}
						else {
							quad = new Quad(
									new Vector3(-0.5f + x, -0.5f + y, -0.5f + z),
									new Vector3( 0.5f + x, -0.5f + y, -0.5f + z),
									new Vector3( 0.5f + x, -0.5f + y,  0.5f + z),
									new Vector3(-0.5f + x, -0.5f + y,  0.5f + z),
									new Vector3(0,0,1));
							quadsList.add(quad);
						}
						
						//Negative Z
						//glColor3f(1, 0, 0);
						if(z - 1 >= 0) {
							if(terrain[x][y][z - 1] == 0)
							{
								quad = new Quad(
										new Vector3(-0.5f + x, -0.5f + y, -0.5f + z),
										new Vector3(-0.5f + x,  0.5f + y, -0.5f + z),
										new Vector3( 0.5f + x,  0.5f + y, -0.5f + z),
										new Vector3( 0.5f + x, -0.5f + y, -0.5f + z),
										new Vector3(0 , 0.5f , 0));
								quadsList.add(quad);
							}
						}
						else {
							quad = new Quad(
									new Vector3(-0.5f + x, -0.5f + y, -0.5f + z),
									new Vector3(-0.5f + x,  0.5f + y, -0.5f + z),
									new Vector3( 0.5f + x,  0.5f + y, -0.5f + z),
									new Vector3( 0.5f + x, -0.5f + y, -0.5f + z),
									new Vector3(0 , 0.5f , 0));
							quadsList.add(quad);
						}
					}
				}
			}
		}
		return quadsList;
	}
}
