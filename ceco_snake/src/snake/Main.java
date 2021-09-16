package snake;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * The main class
 * @author Tsvetomir
 * @author Stefan
 */
public class Main {
	
	/**
	 * The application frame
	 */
	public static JFrame frame;
	
	/**
	 * The snake game
	 */
	public Snake snake;
	/**
	 * The window icon
	 */
	public Image icon;
	
	// settings
	/**
	 * The window name
	 */
	public final String windowName = "Snake";
	/**
	 * The font that will be used for labels
	 */
	public final Font labelFont = new Font(Font.DIALOG, Font.BOLD, 30);
	/**
	 * Path to the icon file
	 */
	public final String iconPath = "./res/icon.png";
	
	public static boolean isHighScoresWindowOpened = false;
	
	/**
	 * Launch the game.
	 */
	public static void main(String[] args) {
		// Auto-generated code
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

	/*
	 * Initialize everything.
	 */
	private void initialize() {
		if(!loadResources()) return; // load resources. If it fails, exit

		snake = new Snake(20, 20, 8, 8, 20, 5); // create the snake
		
		// initialize the frame
		frame = new JFrame();
		frame.setAutoRequestFocus(true); // Otherwise keyboard input will not go to the window
		frame.setName(windowName);
		frame.setTitle(windowName);
		frame.setResizable(false);
		frame.setIconImage(icon);
		frame.setBounds(400, 200, snake.getCanvas().getWidth() + 200, snake.getCanvas().getHeight() + 37);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		// Create a text label
		JLabel lblScoreText = new JLabel("Score:");
		lblScoreText.setFont(labelFont);
		lblScoreText.setBounds(snake.getCanvas().getWidth() + 10, 10, 150, 30);
		
		// Create a label for displaying the score number.
		JLabel lblScoreNum = new JLabel("0");
		lblScoreNum.setFont(labelFont);
		lblScoreNum.setBounds(snake.getCanvas().getWidth() + 10, 40, 150, 30);
		snake.setLblScore(lblScoreNum); // The snake will write to it whenever the score has to be updated
		
		JButton btnHighscores = new JButton("Highscores");
		btnHighscores.setBounds(snake.getCanvas().getWidth() + 10, 200, 170, 30);
		btnHighscores.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isHighScoresWindowOpened) return;
				isHighScoresWindowOpened = true;
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						new HighscoreDisplay().start(snake);
					}
				});
			}
		});
		btnHighscores.setFocusable(false);
		
		// Add everything to the frame
		frame.getContentPane().add(lblScoreText);
		frame.getContentPane().add(lblScoreNum);
		frame.getContentPane().add(btnHighscores);
		frame.getContentPane().add(snake.getCanvas());
		
		// The snake requires input from the window
		frame.addKeyListener(snake);
		
		// Show the frame
		frame.setVisible(true);
		
		// Start the game
		snake.start();
	}
	
	/**
	 * Loads the required resources for the game to run.
	 * @return returns true if the operation was successful, false otherwise.
	 */
	public boolean loadResources() {
		try {
			// Load the icon
			icon = ImageIO.read(new File(iconPath));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}