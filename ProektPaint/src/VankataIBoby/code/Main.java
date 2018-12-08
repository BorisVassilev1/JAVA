package VankataIBoby.code;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class Main implements ActionListener {

	private JFrame frame;
	public static Canvas panel;
	
	boolean isFirstFrame = true;
	
	Timer timer = new Timer(0,this);
	private JTextField txtImgName;
	private JTextField txtImgFormat;
	
	private JButton btnSaveImage;
	private JButton button;
	private JButton button_1;
	private JButton button_2;
	private JButton button_3;
	private JButton button_4;
	private JButton button_5;
	
	private JSlider sldWidth;
	private JSlider sldPicScale;
	
	private JButton[] btnBrushes = new JButton[3];
	
	private JTextField txtLoadImageName;
	
	private JLabel lblImgName;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
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
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 900, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new Canvas(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
		};
		panel.setBounds(12, 13, 770, 527);
		panel.setBackground(Color.WHITE);
		panel.init();
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(panel);
		
		btnSaveImage = new JButton("saveImage");
		btnSaveImage.setBounds(785, 13, 97, 25);
		btnSaveImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = txtImgName.getText();
				String ext = txtImgFormat.getText();
				panel.saveImage(name, ext);
			}
		});
		frame.getContentPane().add(btnSaveImage);
		
		txtImgName = new JTextField();
		txtImgName.setBounds(785, 52, 97, 22);
		frame.getContentPane().add(txtImgName);
		txtImgName.setColumns(10);
		
		JLabel lblName = new JLabel("name:");
		lblName.setBounds(794, 34, 56, 16);
		frame.getContentPane().add(lblName);
		
		txtImgFormat = new JTextField();
		txtImgFormat.setBounds(785, 98, 97, 22);
		frame.getContentPane().add(txtImgFormat);
		txtImgFormat.setColumns(10);
		
		JLabel lblFormat = new JLabel("format:");
		lblFormat.setBounds(794, 77, 56, 16);
		frame.getContentPane().add(lblFormat);
		
		button = new JButton("");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mouseFunctions.brushColor = Color.RED;
			}
		});
		button.setBounds(794, 524, 16, 16);
		button.setBackground(Color.RED);
		frame.getContentPane().add(button);
		
		button_1 = new JButton("");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mouseFunctions.brushColor = Color.YELLOW;
			}
		});
		button_1.setBounds(813, 524, 16, 16);
		button_1.setBackground(Color.YELLOW);
		frame.getContentPane().add(button_1);
		
		button_2 = new JButton("");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mouseFunctions.brushColor = Color.BLUE;
			}
		});
		button_2.setBounds(832, 524, 16, 16);
		button_2.setBackground(Color.BLUE);
		frame.getContentPane().add(button_2);
		
		button_3 = new JButton("");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mouseFunctions.brushColor = Color.GREEN;
			}
		});
		button_3.setBounds(851, 524, 16, 16);
		button_3.setBackground(Color.GREEN);
		frame.getContentPane().add(button_3);
		
		button_4 = new JButton("");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mouseFunctions.brushColor = Color.BLACK;
			}
		});
		button_4.setBounds(813, 504, 16, 16);
		button_4.setBackground(Color.BLACK);
		frame.getContentPane().add(button_4);
		
		button_5 = new JButton("");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mouseFunctions.brushColor = Color.WHITE;
			}
		});
		button_5.setBounds(832, 504, 16, 16);
		button_5.setBackground(Color.WHITE);
		frame.getContentPane().add(button_5);
		
		sldWidth = new JSlider();
		sldWidth.addChangeListener( new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				mouseFunctions.brushWidth = sldWidth.getValue() / 5;
			}
		});
		sldWidth.setBounds(794, 480, 85, 20);
		frame.getContentPane().add(sldWidth);
		
		btnBrushes[0] = new JButton("brush");
		btnBrushes[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				mouseFunctions.isBrush = true;
				mouseFunctions.isBucket = false;
				mouseFunctions.isPic = false;
			}
		});
		
		btnBrushes[0].setBounds(794, 450, 85, 20);
		frame.getContentPane().add(btnBrushes[0]);
		
		btnBrushes[1] = new JButton("bucket");
		btnBrushes[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				mouseFunctions.isBrush = false;
				mouseFunctions.isBucket = true;
				mouseFunctions.isPic = false;
			}
		});
		btnBrushes[1].setBounds(794, 420, 85, 20);
		frame.getContentPane().add(btnBrushes[1]);
		
		btnBrushes[2] = new JButton("Pic Buck");
		btnBrushes[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mouseFunctions.isBrush = false;
				mouseFunctions.isBucket = true;
				mouseFunctions.isPic = true;
				
				try {
					mouseFunctions.paintingImage = ImageIO.read(new File(txtLoadImageName.getText()));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnBrushes[2].setBounds(794,390,80,20);
		frame.getContentPane().add(btnBrushes[2]);
		
		txtLoadImageName = new JTextField();
		txtLoadImageName.setBounds(785, 142, 97, 22);
		txtLoadImageName.setText("big_Smile.png");
		frame.getContentPane().add(txtLoadImageName);
		txtLoadImageName.setColumns(10);
		
		lblImgName = new JLabel("load pic:");
		lblImgName.setBounds(794,122, 56, 18);
		frame.getContentPane().add(lblImgName);
		
		sldPicScale = new JSlider();
		sldPicScale.addChangeListener( new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				mouseFunctions.imgScale = sldPicScale.getValue() / 100f;
			}
		});
		sldPicScale.setBounds(794, 180, 85, 20);
		sldPicScale.setValue(100);
		frame.getContentPane().add(sldPicScale);
		
		panel.addMouseListener( new MouseAdapter() {
			public void mousePressed(MouseEvent e)
			{
				mouseFunctions.mousePressed(e.getX(),e.getY(),e.getButton());
			}
			public void mouseReleased(MouseEvent e)
			{
				mouseFunctions.mouseReleased(e.getX(), e.getY(), e.getButton());
			}
		});
		panel.addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e)
			{
				mouseFunctions.mouseMoved(e);
			}
			public void mouseDragged(MouseEvent e)
			{
				mouseFunctions.mouseDragged(e);
			}
		});
		
		timer.start();
		isFirstFrame = false;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		panel.paintComponent(panel.getGraphics());
	}
}
