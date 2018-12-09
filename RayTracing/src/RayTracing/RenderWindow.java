package RayTracing;

import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

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
				
				Vector3f intPoint = new Vector3f();
				Vector3f rayDir = new Vector3f(0,0,1);
				Graphics g = panel.getGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
				
				
				for(int x = 0; x < panel.getWidth(); x ++)
				{
					for(int y = 0; y < panel.getHeight(); y ++)
					{
						Vector3f[] projPlane = {
								new Vector3f(Main.cam.getX() - panel.getWidth() / 2, Main.cam.getY() - panel.getHeight() / 2,100),
								new Vector3f(Main.cam.getX() + panel.getWidth() / 2, Main.cam.getY() - panel.getHeight() / 2,100),
								new Vector3f(Main.cam.getX() - panel.getWidth() / 2, Main.cam.getY() + panel.getHeight() / 2,100),
								new Vector3f(Main.cam.getX() + panel.getWidth() / 2, Main.cam.getY() + panel.getHeight() / 2,100)
						};
						projPlane[0] = Matrices.rotateVector(projPlane[0], (Vector3f) Main.cam.rot.negate(null).scale((float) (Math.PI / 180)));
						projPlane[1] = Matrices.rotateVector(projPlane[0], (Vector3f) Main.cam.rot.negate(null).scale((float) (Math.PI / 180)));
						projPlane[2] = Matrices.rotateVector(projPlane[0], (Vector3f) Main.cam.rot.negate(null).scale((float) (Math.PI / 180)));
						projPlane[3] = Matrices.rotateVector(projPlane[0], (Vector3f) Main.cam.rot.negate(null).scale((float) (Math.PI / 180)));
						
						rayDir = (Vector3f) new Vector3f(	lerp(projPlane[0], projPlane[1], x / panel.getWidth()).x,
												lerp(projPlane[0], projPlane[2], y / panel.getHeight()).y,
												projPlane[0].z).normalise();
						
//						rayDir.set(0, 0, 1);
//						rayDir = Matrices.rotateVector(rayDir, (Vector3f) (new Vector3f(
//								Main.cam.rot.x + (float)y / panel.getHeight() * 2 * 35.0f,
//								Main.cam.rot.y + (float)x / panel.getWidth() * 2 * 35.0f,
//								Main.cam.rot.z
//								)).negate(null).scale((float) (Math.PI / 180)));
						
						//System.out.println(rayDir);
						//ArrayList<IntersectionPoint> intPoints = new ArrayList<IntersectionPoint>();
						IntersectionPoint closest = null;
						for (Object3d obj : Main.scene) {
							for(int i = 0; i < obj.mesh.tris.length; i ++)
							{
								if(Rays.rayToTriangle(Main.cam.pos, rayDir, obj.mesh.tris[i], intPoint)) {
									
									if(closest == null)
									{
										closest = new IntersectionPoint(intPoint, obj, i);
									}
									else if(Vector3f.sub(Main.cam.pos, closest.point, null).length() > Vector3f.sub(Main.cam.pos, intPoint, null).length())
									{
										closest.set(intPoint, obj, i);
									}
								}
							}
						}
						if(closest != null)
						{
							g.setColor(new Color(closest.object.mesh.colors[closest.triangleIndex].x,closest.object.mesh.colors[closest.triangleIndex].y,closest.object.mesh.colors[closest.triangleIndex].z));
							g.drawRect(x + panel.getWidth() / 2, y + panel.getHeight() / 2, 1, 1);
						}
					}
				}
				
			}
		});
		btnRender.setBounds(820, 11, 128, 23);
		frame.getContentPane().add(btnRender);
	}
	
	public static Vector3f lerp(Vector3f a, Vector3f b, float k)
	{
		return new Vector3f((a.x * k + b.x * (1-k))/k,(a.y * k + b.y * (1-k))/k,(a.z * k + b.z * (1-k))/k);
	}
}
