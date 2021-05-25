package snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * A {@link JPanel} that you can draw on.
 * @author Tsvetomir
 * @author Stefan
 */
public class Canvas extends JPanel {
	
	// ¯\_(ツ)_/¯
	private static final long serialVersionUID = 1L;
	
	// An image to write to
	private BufferedImage image;
	/*
	 * This is used to draw on the canvas
	 */
	public Graphics2D ig;
	
	/**
	 * Initializes the Canvas's drawing features. Must be called after the canvas's bounds have been set.
	 */
	public void init() {
		// create the image
		image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		// create graphics
		ig = image.createGraphics();
		
		// set some default drawing colors.
		ig.setBackground(Color.white);
		ig.clearRect(0, 0, getWidth(), getHeight());
		ig.setColor(Color.black);
	}
	
	/**
	 * Saves the image to a file with a specified name.
	 * @param fileName - name of the file without extension
	 * @param fileType - extension of the file
	 */
	public void saveImage(String fileName, String fileType) {
		try {
			ImageIO.write(image, fileType, new File(fileName + "." + fileType));
			System.out.println("saved image!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	/**
	 * Paints the image on the screen.
	 */
	public void paintComponent(Graphics g) {
		((Graphics2D) g).drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}
}