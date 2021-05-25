package snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

/**
 * 
 * A snake game class.
 * 
 * @author Tsvetomir
 * @author Stefan
 */
public class Snake implements KeyListener{

	private final Queue<Segment> segments;

	private class Segment {
		public int x;
		public int y;

		public Segment(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	private final int height;
	private final int width;
	private int direction = 2;
	private int newDir = direction;
	private final boolean pole[][];
	private final int voxelSize;
	private int headX, headY;
	
	private Color color = Color.green;
	private Color foodColor = Color.red;
	private final Canvas canvas;
	private final Timer timer;
	private JLabel scoreLabel;
	
	private int foodX = 10, foodY = 10;
	private int score = 0;
	
	private Font font = new Font("Dialog", Font.BOLD, 50);
	
	/**
	 * Constructor for the snake game
	 * 
	 * @param height - height of the game boundary
	 * @param width - width of the game boundary
	 * @param startX - start X position of the snake's tail
	 * @param startY - start Y position of the snake's tail
	 * @param voxel_size - the size of each voxel of the game boundary
	 * @param startLength - the initial length of the snake
	 * 
	 * @implNote
	 * Note that the snake is ganerated towards positive X. That means that if startX + startLength > width, this will crash.
	 */
	public Snake(int height, int width, int startX, int startY, int voxel_size, int startLength) {
		this.height = height;
		this.width = width;
		headX = startX + startLength - 1;
		headY = startY;
		pole = new boolean[height][width];
		segments = new LinkedList<Segment>();
		for(int i = 0; i < startLength; i ++) {
			segments.add(new Segment(startX + i, startY));
			pole[startY][startX + i] = true;
		}
		this.voxelSize = voxel_size;
		
		this.canvas = new Canvas();
		canvas.setBounds(0, 0, width * voxel_size, height * voxel_size);
		canvas.init();
		timer = new Timer();
	}
	
	/**
	 * Draws the snake to the canvas. Note that the method {@link Canvas#repaint()} must be called for the changes to take effect.
	 */
	public void draw() {
		Graphics2D g = canvas.ig;
		g.setBackground(Color.WHITE);
		g.setColor(color);
		g.clearRect(0, 0, width * voxelSize, height * voxelSize);
		for (Segment s : segments) {
			g.fillRect(s.x * voxelSize, s.y * voxelSize, voxelSize, voxelSize);
		}
		g.setColor(foodColor);
		g.fillRect(foodX * voxelSize, foodY * voxelSize, voxelSize, voxelSize);
		g.setColor(Color.black);
		g.drawRect(0, 0, width * voxelSize - 1, height * voxelSize - 1);
	}
	
	/**
	 * Moves the snake forward. If food shall be eaten, the score is incremented, the food is moved via {@link #moveFood()} and the {@link #updateScoreLabel()} is called.
	 * If the snake goes out of bounds or intersepts itself, it dies and {@link #gameOver()} is called.
	 */
	public boolean move() {
		direction = newDir;
		Segment nov = null;
		if (direction == 0)
			nov = new Segment(headX - 1, headY);
		else if (direction == 1)
			nov = new Segment(headX, headY - 1);
		else if (direction == 2)
			nov = new Segment(headX + 1, headY);
		else if (direction == 3)
			nov = new Segment(headX, headY + 1);
		
		if(nov.x >= width || nov.x < 0 || nov.y >= height || nov.y < 0 || pole[nov.y][nov.x]) {
			gameOver();
			return false;
		}
		
		if(nov.x == foodX && nov.y == foodY) {
			score ++;
			updateScoreLabel();
			moveFood();
		}
		else {
			Segment tail = segments.poll();
			pole[tail.y][tail.x] = false;
		}
		
		segments.add(nov);
		pole[nov.y][nov.x] = true;
		headX = nov.x;
		headY = nov.y;
		return true;
	}
	/**
	 * Moves the food to a random position.
	 */
	private void moveFood() {
		foodX = (int)(Math.random() * width);
		foodY = (int)(Math.random() * height);
	}
	
	/**
	 * Updates the score label. 
	 * @see {@link #setLblScore(JLabel)}, {@link #getLblScore()}.
	 */
	public void updateScoreLabel() {
		if(scoreLabel != null)
			scoreLabel.setText(Integer.toString(score));
	}
	
	/**
	 * The game ends.
	 */
	private void gameOver() {
		// text rendering settings
		canvas.ig.setFont(font);
		canvas.ig.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		// clear the screen and write the text
		canvas.ig.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		canvas.ig.setColor(Color.red);
		canvas.ig.drawString("Game Over", 50, 210);
		
		// stop the game
		timer.cancel();
		
		System.out.println("Game Over!!");
	}
	
	/**
	 * Starts the snake game. The snake will move once every 500ms.
	 */
	public void start() {
		canvas.setIgnoreRepaint(false);
		draw();
		canvas.repaint();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(move())
					draw();
				canvas.repaint();
			}

		}, 0, 500);
	}
	
	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public int getDirection() {
		return direction;
	}

	public int getVoxelSize() {
		return voxelSize;
	}

	public int getHeadX() {
		return headX;
	}

	public int getHeadY() {
		return headY;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getFoodColor() {
		return foodColor;
	}

	public void setFoodColor(Color foodColor) {
		this.foodColor = foodColor;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public JLabel getLblScore() {
		return scoreLabel;
	}

	public void setLblScore(JLabel lblScore) {
		this.scoreLabel = lblScore;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int id = e.getKeyCode();
		if(id >= KeyEvent.VK_LEFT && id <= KeyEvent.VK_DOWN) {
			int nd = id - KeyEvent.VK_LEFT;
			if(Math.abs(nd - direction) != 2)
				newDir = nd;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
	
}