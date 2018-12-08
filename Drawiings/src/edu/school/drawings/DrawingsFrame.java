package edu.school.drawings;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import java.awt.Color;
import javax.swing.border.LineBorder;

public class DrawingsFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DrawingsFrame frame = new DrawingsFrame();
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
	public DrawingsFrame() {
		setTitle("\u0413\u0440\u0430\u0444\u0438\u043A\u0430");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
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
		
		JPanel pnlCanvas = new JPanel();
		pnlCanvas.setBackground(new Color(255, 255, 204));
		pnlCanvas.setBorder(new LineBorder(new Color(102, 0, 0)));
		contentPane.add(pnlCanvas, "cell 1 0,grow");
	}

}
