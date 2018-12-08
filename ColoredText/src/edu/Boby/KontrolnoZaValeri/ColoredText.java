package edu.Boby.KontrolnoZaValeri;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class ColoredText {

	private JFrame frame;
	private JLabel lblPreview;
	private JButton btnPostavi;
	private JButton btnRed;
	private JButton btnGreen;
	private JButton btnBlue;
	private JButton btnWhite;
	private JButton btnBlack;
	private JButton btnMakeWhiter;
	private JButton button;
	private JLabel lbl1;
	private JLabel lbl2;
	private JLabel lbl3;
	private JLabel lbl4;
	private JLabel lbl5;
	
	private int whereAmIGoingToPutTheNextText = 1;
	private JLabel lbl6;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ColoredText window = new ColoredText();
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
	public ColoredText() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 548, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTextField txtInput = new JTextField();
		txtInput.setBounds(10, 11, 134, 20);
		frame.getContentPane().add(txtInput);
		txtInput.setColumns(10);
		
		btnPostavi = new JButton("postavi");
		btnPostavi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = txtInput.getText();
				lblPreview.setText(text);
			}
		});
		btnPostavi.setBounds(189, 10, 89, 23);
		frame.getContentPane().add(btnPostavi);
		
		lblPreview = new JLabel("");
		lblPreview.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblPreview.setBounds(10, 42, 134, 20);
		frame.getContentPane().add(lblPreview);
		
		btnRed = new JButton("\u0427\u0435\u0440\u0432\u0435\u043D\u043E");
		btnRed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblPreview.setForeground(new Color(255,0,0));
			}
		});
		btnRed.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnRed.setForeground(Color.RED);
		btnRed.setBounds(10, 81, 89, 23);
		frame.getContentPane().add(btnRed);
		
		btnGreen = new JButton("\u0417\u0435\u043B\u0435\u043D\u043E");
		btnGreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblPreview.setForeground(new Color(0,255,0));
			}
		});
		btnGreen.setForeground(Color.GREEN);
		btnGreen.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnGreen.setBounds(109, 81, 89, 23);
		frame.getContentPane().add(btnGreen);
		
		btnBlue = new JButton("\u0421\u0438\u043D\u044C\u043E");
		btnBlue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblPreview.setForeground(new Color(0,0,255));
			}
		});
		btnBlue.setForeground(Color.BLUE);
		btnBlue.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnBlue.setBounds(208, 81, 89, 23);
		frame.getContentPane().add(btnBlue);
		
		btnWhite = new JButton("\u0411\u044F\u043B\u043E");
		btnWhite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblPreview.setForeground(new Color(255,255,255));
			}
		});
		btnWhite.setForeground(Color.WHITE);
		btnWhite.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnWhite.setBounds(307, 81, 89, 23);
		frame.getContentPane().add(btnWhite);
		
		btnBlack = new JButton("\u0427\u0435\u0440\u043D\u043E");
		btnBlack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblPreview.setForeground(new Color(0,0,0));
			}
		});
		btnBlack.setForeground(Color.BLACK);
		btnBlack.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnBlack.setBounds(406, 81, 89, 23);
		frame.getContentPane().add(btnBlack);
		
		btnMakeWhiter = new JButton("\u0418\u0437\u0441\u0432\u0435\u0442\u043B\u0438");
		btnMakeWhiter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color currentColor = lblPreview.getForeground();
				int red = currentColor.getRed();
				int green = currentColor.getGreen();
				int blue = currentColor.getBlue();
				red+= 10;
				if(red >= 256) {red = 255;}
				green += 10;
				if(green >= 256) {green = 255;}
				blue += 10;
				if(blue >= 256) {blue = 255;}
				lblPreview.setForeground(new Color(red, green, blue));
			}
		});
		btnMakeWhiter.setBounds(307, 10, 89, 23);
		frame.getContentPane().add(btnMakeWhiter);
		
		button = new JButton("\u0417\u0430\u043F\u0430\u0437\u0438");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color previewColor = lblPreview.getForeground();
				String previewText = lblPreview.getText();
				if(whereAmIGoingToPutTheNextText == 1)
				{
					lbl1.setText(previewText);
					lbl1.setForeground(previewColor);
					whereAmIGoingToPutTheNextText+=1;
				}
				else if(whereAmIGoingToPutTheNextText == 2)
				{
					lbl2.setText(previewText);
					lbl2.setForeground(previewColor);
					whereAmIGoingToPutTheNextText+=1;
				}
				else if(whereAmIGoingToPutTheNextText == 3)
				{
					lbl3.setText(previewText);
					lbl3.setForeground(previewColor);
					whereAmIGoingToPutTheNextText+=1;
				}
				else if(whereAmIGoingToPutTheNextText == 4)
				{
					lbl4.setText(previewText);
					lbl4.setForeground(previewColor);
					whereAmIGoingToPutTheNextText+=1;
				}
				else if(whereAmIGoingToPutTheNextText == 5)
				{
					lbl5.setText(previewText);
					lbl5.setForeground(previewColor);
					whereAmIGoingToPutTheNextText+=1;
				}
				else if(whereAmIGoingToPutTheNextText == 6)
				{
					lbl6.setText(previewText);
					lbl6.setForeground(previewColor);
					whereAmIGoingToPutTheNextText+=1;
				}
				else
				{
					System.out.println("При повече от 6 натискания, бутонът не функционира.");
				}
				
					
			}
		});
		button.setBounds(199, 39, 185, 31);
		frame.getContentPane().add(button);
		
		lbl1 = new JLabel("");
		lbl1.setBounds(10, 115, 89, 14);
		frame.getContentPane().add(lbl1);
		
		lbl2 = new JLabel("");
		lbl2.setBounds(109, 115, 89, 14);
		frame.getContentPane().add(lbl2);
		
		lbl3 = new JLabel("");
		lbl3.setBounds(208, 115, 89, 14);
		frame.getContentPane().add(lbl3);
		
		lbl4 = new JLabel("");
		lbl4.setBounds(307, 115, 89, 14);
		frame.getContentPane().add(lbl4);
		
		lbl5 = new JLabel("");
		lbl5.setBounds(406, 115, 89, 14);
		frame.getContentPane().add(lbl5);
		
		lbl6 = new JLabel("");
		lbl6.setBounds(10, 140, 89, 14);
		frame.getContentPane().add(lbl6);
	}
}
