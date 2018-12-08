package VankataIBoby.code;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class mouseFunctions {
	static int mouseX = 0; 
	static int mouseY = 0;
	
	static int prevMouseX = 0;
	static int prevMouseY = 0;
	
	static boolean isMousePressed = false;
	
	public static Color brushColor = new Color(0,0,0);
	public static int brushWidth = 1;
	public static BufferedImage paintingImage;
	public static float imgScale = 0.2f;
	
	public static boolean isBrush = true;
	public static boolean isBucket = false;
	public static boolean isPic = false;
	
	public static void mousePressed(int x, int y,int buton)
	{
		isMousePressed = true;
	}
	public static void mouseReleased(int mouseX, int mouseY, int button)
	{
		isMousePressed = false;
		if(isBucket)
		{
			BufferedImage img = Main.panel.image;
			int[] image = img.getRGB(0, 0, img.getWidth(),img.getHeight(), null, 0, img.getWidth());
			int passed[][] = new int[img.getWidth()][img.getHeight()];
			for(int i = 0; i < img.getWidth(); i ++)
			{
				for(int j = 0; j < img.getHeight(); j ++)
				{
					passed[i][j] = 0;
				}
			}
			
			int targetColor = image[ mouseY*img.getWidth() + mouseX];
			if(!(targetColor == brushColor.getRGB()))
			{
				passed[mouseX][mouseY] = 2;
				boolean shouldContinue = true;
				while(shouldContinue)
				{
					shouldContinue = false;
					for(int y = 0; y < img.getHeight(); y ++)
					{
						for(int x = 0; x < img.getWidth(); x ++)
						{
							if(passed[x][y] == 2)
							{
								if(isPic)
								{
									int width = paintingImage.getWidth();
									int height = paintingImage.getHeight();
									
									int px =(int)(((x > mouseX)? (x - mouseX + width / 2 * imgScale): (x - mouseX + width / 2 * imgScale)) / imgScale) % width;
									int py =(int)(((y > mouseY)? (y - mouseY + height / 2 * imgScale): (y - mouseY + height / 2 * imgScale)) / imgScale) % height;
									
									//System.out.println(px + " " + py);
									
									if(px < 0) px = width + px;
									if(py < 0) py = height + py;
									
									image[y * img.getWidth() + x] =
											paintingImage.getRGB(px,py);
								}
								else
								{
									image[y * img.getWidth() + x] = brushColor.getRGB();
								}
								
								if(x >= 1 && passed[x-1][y] == 0)
								{
									if(image[y * img.getWidth() + x - 1] == targetColor)
									{
										passed[x-1][y] = 2;
										shouldContinue = true;
									}
								}
								if(y >= 1 && passed[x][y-1] == 0)
								{
									if(image[(y - 1) * img.getWidth() + x] == targetColor)
									{
										passed[x][y-1] = 2;
										shouldContinue = true;
									}
								}
								if(x < img.getWidth() - 1 && passed[x+1][y] == 0)
								{
									if(image[y * img.getWidth() + x + 1] == targetColor)
									{
										passed[x+1][y] = 2;
										shouldContinue = true;
									}
								}
								if(y < img.getHeight() - 1 && passed[x][y+1] == 0)
								{
									if(image[(y + 1) * img.getWidth() + x] == targetColor)
									{
										passed[x][y+1] = 2;
										shouldContinue = true;
									}
								}
							}
						}
					}
//					img.setRGB(0, 0, img.getWidth(), img.getHeight(), image, 0, img.getWidth());
//					Graphics2D g = (Graphics2D) Main.panel.getGraphics();
//					((Graphics2D) g).drawImage(Main.panel.image, 0, 0, Main.panel.getWidth(), Main.panel.getHeight(), null);
				}
			}
			img.setRGB(0, 0, img.getWidth(), img.getHeight(), image, 0, img.getWidth());
		}
	}
	
	public static void mouseMoved(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}
	public static void mouseDragged(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	public static void draw(Graphics2D g)
	{
		g.setColor(brushColor);
		g.setStroke(new BasicStroke(brushWidth));
		
		if(isMousePressed)
		{
			if(isBrush) {
				g.drawLine(mouseX, mouseY, prevMouseX, prevMouseY);
			}
		}
	}
	
	public static void update()
	{
		prevMouseX = mouseX;
		prevMouseY = mouseY;
	}
	
}
