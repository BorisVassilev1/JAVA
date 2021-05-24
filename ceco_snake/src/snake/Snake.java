package snake;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

public class Snake implements KeyListener{

	private Queue<Segment> segments;

	private class Segment {
		public int x;
		public int y;

		public Segment(int x, int y) {
			this.x = x;
			this.y = y;
		}

	}

	private int height = 16;
	private int width = 16;
	private int direction = 2;
	private int new_dir = direction;
	private boolean pole[][];
	private int voxel_size = 20;
	private int headX, headY;
	
	private Color color = Color.green;
	private Color foodColor = Color.red;
	private Canvas canvas;
	private Timer timer;
	private JLabel scoreLabel;
	
	int foodX = 10, foodY = 10;
	int score = 0;
	
	public Snake(int height, int width, int startX, int startY, int voxel_size, int start_length) {
		this.height = height;
		this.width = width;
		headX = startX + start_length - 1;
		headY = startY;
		pole = new boolean[height][width];
		segments = new LinkedList<Segment>();
		for(int i = 0; i < start_length; i ++) {
			segments.add(new Segment(startX + i, startY));
			pole[startY][startX + i] = true;
		}
		this.voxel_size = voxel_size;
		
		this.canvas = new Canvas();
		canvas.setBounds(0, 0, width * voxel_size, height * voxel_size);
		canvas.init();
	}

	public void draw() {
		Graphics2D g = canvas.ig;
		g.setBackground(Color.WHITE);
		g.setColor(color);
		g.clearRect(0, 0, width * voxel_size, height * voxel_size);
		for (Segment s : segments) {
			g.fillRect(s.x * voxel_size, s.y * voxel_size, voxel_size, voxel_size);
		}
		g.setColor(foodColor);
		g.fillRect(foodX * voxel_size, foodY * voxel_size, voxel_size, voxel_size);
		g.setColor(Color.black);
		g.drawRect(0, 0, width * voxel_size - 1, height * voxel_size - 1);
	}

	public void move() {
		direction = new_dir;
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
			return;
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
	}
	
	private void moveFood() {
		foodX = (int)(Math.random() * width);
		foodY = (int)(Math.random() * height);
	}
	
	public void updateScoreLabel() {
		if(scoreLabel != null)
			scoreLabel.setText(Integer.toString(score));
	}
	
	private void gameOver() {
		System.out.println("Game over kiddo!!");
		timer.cancel();
	}
	
	public void start() {
		timer = new Timer();
		
		draw();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				move();
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

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getVoxel_size() {
		return voxel_size;
	}

	public void setVoxel_size(int voxel_size) {
		this.voxel_size = voxel_size;
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

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
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
				new_dir = nd;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
	
}