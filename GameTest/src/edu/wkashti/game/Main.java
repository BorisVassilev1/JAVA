package edu.wkashti.game;

import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import java.awt.Color;
import javax.swing.border.LineBorder;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.Toolkit;
import java.awt.Window.Type;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Main extends JFrame implements ActionListener , KeyListener {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static JPanel pnlCanvas;
	static boolean[] IsKeyPressed = new boolean[192];
	static {
		Arrays.fill(IsKeyPressed,false);
	}
	private JPanel pnlControl;
	Timer timer = new Timer(0, this);
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
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
		setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/edu/wkashti/game/pacman.jpg")));
		setTitle("\u0418\u0433\u0440\u04384\u043A\u0430");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(51, 102, 102));
		contentPane.setBackground(Color.YELLOW);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		pnlControl = new JPanel();
		pnlControl.setBackground(Color.LIGHT_GRAY);
		pnlControl.setBorder(new LineBorder(new Color(102, 0, 0)));
		
		pnlCanvas = new JPanel(){
			public void paintComponent(Graphics g)
		    {
				super.paintComponent(g);
		          
		        int w = pnlCanvas.getWidth();
		        int h = pnlCanvas.getHeight();
		        Calculate.Draw(g, w, h);
		    }
		};
		pnlCanvas.setBounds(0, 0, 800, 600);
		pnlCanvas.setForeground(Color.BLACK);
		pnlCanvas.setBackground(Color.WHITE);
		pnlCanvas.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		pnlCanvas.addKeyListener(this);
		pnlCanvas.setFocusable(true);
		pnlCanvas.requestFocusInWindow();
		contentPane.setLayout(null);
		
		contentPane.add(pnlCanvas);
		
		timer.setDelay(33);
		timer.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Calculate.Update();
		pnlCanvas.repaint();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		e.consume();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int KeyCode = e.getKeyCode();
		IsKeyPressed[KeyCode]= true;
		e.consume();
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int KeyCode = e.getKeyCode();
		IsKeyPressed[KeyCode]= false;
		e.consume();
	}
}
