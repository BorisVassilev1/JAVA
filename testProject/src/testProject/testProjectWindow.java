package testProject;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.SystemColor;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JEditorPane;
import java.awt.Canvas;
import java.awt.Graphics;
import javax.swing.JPanel;

public class testProjectWindow {

	private JFrame frmTheBestGame;
	private JTextField txtn;
	/**
	 * @wbp.nonvisual location=68,119
	 */
	JPanel pnlCanvas= new JPanel();
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testProjectWindow window = new testProjectWindow();
					window.frmTheBestGame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public testProjectWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		//background
		frmTheBestGame = new JFrame();
		frmTheBestGame.setTitle("the best window ever");
		frmTheBestGame.setIconImage(Toolkit.getDefaultToolkit().getImage(testProjectWindow.class.getResource("/testProject/pacman.jpg")));
		frmTheBestGame.getContentPane().setForeground(Color.BLACK);
		frmTheBestGame.getContentPane().setBackground(SystemColor.menu);
		
		
		
		JLabel lblFirstName = new JLabel("DUMA:");
		
		txtn = new JTextField();
		txtn.setColumns(10);
		
		
		
		
		JButton btnClose = new JButton("quit");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("window closed! :D\n");
				String textClose = txtn.getText();
				if(textClose!="")
				{
					System.out.println(textClose);
					//JOptionPane.showMessageDialog(null,"ti napisa:" + textClose);
				}
				System.exit(0);
			}
		});
			
		GroupLayout groupLayout = new GroupLayout(frmTheBestGame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnClose, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblFirstName)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtn, GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFirstName)
						.addComponent(txtn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 196, Short.MAX_VALUE)
					.addComponent(btnClose)
					.addContainerGap())
		);
		frmTheBestGame.getContentPane().setLayout(groupLayout);
		frmTheBestGame.setBounds(100, 100, 450, 300);
		frmTheBestGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		
		
	}
}
