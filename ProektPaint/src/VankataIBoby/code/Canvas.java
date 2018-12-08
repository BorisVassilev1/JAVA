package VankataIBoby.code;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Canvas extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BufferedImage image;
	public Graphics2D ig;
	
	public void init()
	{
		image = new BufferedImage(this.getWidth(),this.getHeight(), BufferedImage.TYPE_INT_RGB);
		ig = image.createGraphics();
		ig.fillRect(0, 0, getWidth(), getHeight());
		ig.setColor(new Color(0,0,0));
	}
	
	public void saveImage(String name,String type) {
//		image = new BufferedImage(this.getWidth(),this.getHeight(), BufferedImage.TYPE_INT_RGB);
//		Graphics2D g2 = image.createGraphics();
//		g2.fillRect(0, 0, getWidth(), getHeight());
//		g2.setColor(new Color(0,0,0));
//		print(g2);
//		g2.dispose();
		try{
			ImageIO.write(image, type, new File(name+"."+type));
			System.out.println("saved image!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void paintComponent(Graphics g) {
		//super.paintComponent(g);
		//mouseFunctions.draw((Graphics2D) g);
		mouseFunctions.draw(ig);
		((Graphics2D) g).drawImage(Main.panel.image, 0, 0, getWidth(), getHeight(), null);
		mouseFunctions.update();
	}
}
