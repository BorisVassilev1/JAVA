package snake;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main {

	private JFrame frame;
	
	Snake snake;
	
	/*
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

	/*
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		snake = new Snake(20, 20, 8, 8, 20, 5);
		
		frame = new JFrame();
		frame.setAutoRequestFocus(true);
		frame.setName("Snek");
		frame.setTitle("Snek");
		frame.setResizable(false);
		
		Image icon = null;
		
		try {
			icon = ImageIO.read(new File("./res/icon.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		frame.setIconImage(icon);
		frame.setBounds(400, 400, snake.getCanvas().getWidth() + 200, snake.getCanvas().getHeight() + 37);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		Font labelFont = new Font("Dialog", Font.BOLD, 30);
		
		JLabel lblScoreText = new JLabel("Score:");
		lblScoreText.setFont(labelFont);
		lblScoreText.setBounds(snake.getCanvas().getWidth() + 10, 10, 150, 30);
		
		JLabel lblScoreNum = new JLabel("0");
		lblScoreNum.setFont(labelFont);
		lblScoreNum.setBounds(snake.getCanvas().getWidth() + 10, 40, 150, 30);
		snake.setLblScore(lblScoreNum);
		
		frame.getContentPane().add(lblScoreText);
		frame.getContentPane().add(lblScoreNum);
		frame.getContentPane().add(snake.getCanvas());
		frame.setVisible(true);

		frame.addKeyListener(snake);
		
		snake.start();
		
	}
}