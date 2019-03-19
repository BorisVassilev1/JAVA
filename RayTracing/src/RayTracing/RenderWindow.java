package RayTracing;

import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import Objects.Cube;
import Objects.IntersectionPoint;
import Objects.Object3d;

import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class RenderWindow {

	private JFrame frame;
	public static JPanel panel;

	/**
	 * Launch the application.
	 */
	public static void create() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RenderWindow window = new RenderWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RenderWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setBounds(100, 100, 974, 660);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(10, 11, 800, 600);
		frame.getContentPane().add(panel);
		
		JButton btnRender = new JButton("Render");
		btnRender.addActionListener(new ActionListener() {
			@SuppressWarnings({ "unused" })
			public void actionPerformed(ActionEvent e) {
				Graphics g = panel.getGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
				
				float imageRatio = panel.getWidth() / (float )panel.getHeight();
				
				float fieldOfView = 70;
				Rays.UpdateViewPlane(imageRatio, fieldOfView);
				
				for(int x = 0; x < panel.getWidth(); x ++)
				{
					for(int y = 0; y < panel.getHeight(); y ++)
					{
						Color col = Rays.traceScreenPoint(x / (float) panel.getWidth(), y / (float) panel.getHeight());
						if(col != null)
						{
							g.setColor(col);
							g.drawRect(x, y, 1, 1);
						}
					}
				}
				
			}
		});
		btnRender.setBounds(820, 11, 128, 23);
		frame.getContentPane().add(btnRender);
		Rays.initProjection();
	}
	
	
}
