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
				
				float imageRatio = panel.getWidth() / (float )panel.getHeight();
				
				float fieldOfView = 70;
				fieldOfView *= Math.PI / 180;
				float planeOffset = (float) (1/Math.tan(fieldOfView/2));
				
				Vector3f campos2 = new Vector3f(Main.cam.pos.x, Main.cam.pos.y, Main.cam.pos.z);
				Vector3f camrot2 = new Vector3f(Main.cam.rot.x, Main.cam.rot.y + 180, Main.cam.rot.z);
				Vector3f[] projPlane = {
						(Vector3f) new Vector3f(- 1 * imageRatio, - 1, planeOffset).normalise(),
						(Vector3f) new Vector3f(+ 1 * imageRatio, - 1, planeOffset).normalise(),
						(Vector3f) new Vector3f(- 1 * imageRatio, + 1, planeOffset).normalise(),
						(Vector3f) new Vector3f(+ 1 * imageRatio, + 1, planeOffset).normalise()
				};
				projPlane[0] = Matrices.rotateVector(projPlane[0], (Vector3f) new Vector3f(camrot2).scale((float) (Math.PI / 180)));
				projPlane[1] = Matrices.rotateVector(projPlane[1], (Vector3f) new Vector3f(camrot2).scale((float) (Math.PI / 180)));
				projPlane[2] = Matrices.rotateVector(projPlane[2], (Vector3f) new Vector3f(camrot2).scale((float) (Math.PI / 180)));
				projPlane[3] = Matrices.rotateVector(projPlane[3], (Vector3f) new Vector3f(camrot2).scale((float) (Math.PI / 180)));
				
				for(int x = 0; x < panel.getWidth(); x ++)
				{
					for(int y = 0; y < panel.getHeight(); y ++)
					{
						rayDir = lerp(projPlane[0], projPlane[1], projPlane[2], projPlane[3], x / (float) panel.getWidth(), y / (float) panel.getHeight());
						
						//ArrayList<IntersectionPoint> intPoints = new ArrayList<IntersectionPoint>();
						IntersectionPoint closest = null;
						for (Object3d obj : Main.scene) {
							for(int i = 0; i < obj.mesh.tris.length; i ++)
							{
								if(Rays.rayToTriangle(campos2, rayDir, obj.mesh.tris[i], intPoint)) {
									
									if(closest == null)
									{
										closest = new IntersectionPoint(intPoint, obj, i);
									}
									else if(Vector3f.sub(Main.cam.pos, closest.point, null).lengthSquared() > Vector3f.sub(Main.cam.pos, intPoint, null).lengthSquared())
									{
										closest.set(intPoint, obj, i);
									}
									
//									g.setColor(new Color(obj.mesh.colors[i].x,obj.mesh.colors[i].y,obj.mesh.colors[i].z, 0.1f));
//									g.drawRect(x , y, 1, 1);
								}
							}
						}
						if(closest != null)
						{
							Cube c = new Cube();
							c.position.set(closest.point);
							c.scale.set(0.1f, 0.1f, 0.1f);
							
							g.setColor(new Color(closest.object.mesh.colors[closest.triangleIndex].x,closest.object.mesh.colors[closest.triangleIndex].y,closest.object.mesh.colors[closest.triangleIndex].z));
							g.drawRect(x , y, 1, 1);
						}
					}
				}
				
			}
		});
		btnRender.setBounds(820, 11, 128, 23);
		frame.getContentPane().add(btnRender);
	}
	
	public static Vector3f lerp(Vector3f a, Vector3f b, Vector3f c, Vector3f d, float x, float y)
	{
		Vector3f vecAB = lerp(a, b, x);
		Vector3f vecCD = lerp(c, d, x);
		return lerp(vecAB, vecCD, y);
	}
	public static float lerp(float a , float b, float k)
	{
		return (a * k + b * (1 - k));
	}
	public static Vector3f lerp(Vector3f a, Vector3f b, float k)
	{
		return new Vector3f(lerp(a.x, b.x, k), lerp(a.y, b.y, k), lerp(a.z, b.z, k));
	}
}
