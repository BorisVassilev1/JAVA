package canvasTest;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;


import net.miginfocom.swing.MigLayout;

import java.awt.Font;
import javax.swing.JLabel;
import java.awt.BorderLayout;

public class canvasTest extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7278338626754250739L;
	private JFrame frame;
	private JPanel contentPane;
	JPanel pnlCanvas = new JPanel();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					canvasTest window = new canvasTest();
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
	public canvasTest() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		
		
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Arial", Font.PLAIN, 11));
		frame.getContentPane().setBackground(new Color(255, 255, 255));
		frame.getContentPane().setForeground(new Color(0, 0, 0));
		
		JLabel lblNewLabel = new JLabel("test canvas:");
		frame.getContentPane().add(lblNewLabel, BorderLayout.NORTH);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		contentPane = new JPanel();
		contentPane.setForeground(new Color(51, 102, 102));
		contentPane.setBackground(new Color(255, 255, 204));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[35%][65%,grow]", "[grow]"));
		
		JPanel pnlControl = new JPanel();
		pnlControl.setBackground(new Color(255, 255, 153));
		pnlControl.setBorder(new LineBorder(new Color(102, 0, 0)));
		contentPane.add(pnlControl, "cell 0 0,grow");
		pnlControl.setLayout(new MigLayout("", "[]", "[]"));
		
		
		
		
		
		
		
		
		
		
		Graphics canvas = pnlCanvas.getGraphics();
		pnlCanvas.setBackground(new Color(255, 255, 204));
		pnlCanvas.setBorder(new LineBorder(new Color(102, 0, 0)));
		contentPane.add(pnlCanvas, "cell 1 0,grow");
		canvas.fillRect(100,100, 100, 100);
	}

}
