package movingObject;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import java.awt.Color;

public class Main extends JFrame implements ActionListener, KeyListener{

	private JPanel contentPane;
	
	int x = 0;
	int y = 0;
	
	Timer timer = new Timer(0,this);
	
	static boolean[] isKeyPressed = new boolean[255];
	private JPanel panel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		for(int i = 0; i < isKeyPressed.length; i ++)
		{
			isKeyPressed[i] = false;
		}
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
					frame.requestFocusInWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 638, 535);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		addKeyListener(this);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
			
				for(int i = 0; i < 255; i ++)
				{
					if(isKeyPressed[i])
					{
						System.out.println(i);
					}
				}
				
				if(isKeyPressed[87])
				{
					y -= 1;
					System.out.println("henlo");
				}
				if(isKeyPressed[83])
				{
					y += 1;
				}
				if(isKeyPressed[65])
				{
					x -= 1;
				}
				if(isKeyPressed[68])
				{
					x += 1;
				}
				
				g.drawOval(x - 30, y - 30, 60, 60);
			}
		};
		panel.setBackground(Color.WHITE);
		panel.setBounds(12, 13, 596, 462);
		panel.setFocusable(true);
		contentPane.add(panel);
		x = panel.getWidth() / 2;
		y = panel.getHeight() / 2;
		
		timer.setDelay(33);
		timer.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//panel.paintComponents(panel.getGraphics());
		panel.repaint();
		//System.out.println("henlo");
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		e.consume();
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		isKeyPressed[keyCode] = false;
		e.consume();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		//System.out.println(keyCode);
		isKeyPressed[keyCode] = true;
		e.consume();
	}
}
