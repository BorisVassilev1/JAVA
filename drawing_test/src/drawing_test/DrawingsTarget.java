package drawing_test;

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
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Toolkit;

/**
 * Информатика, 8 клас, издателство Просвета, 2017 Автори: Светла Бойчева,
 * Николина Николова, Елиза Стефанова, Антон Денев Урок 3.4.2.1. Компютърна
 * графика Задача: Да се създаде приложение с ГПИ, което изчертава линиите от
 * мишена от концентрични кръгове.
 * 
 * Версия 1.
 */

public class DrawingsTarget extends JFrame {
	PerlinNoiseGenerator pnoise = new PerlinNoiseGenerator();
	SimplexNoise snoise = new SimplexNoise();

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel pnlCanvas;
	private JPanel pnlControl;
	private JButton btnDraw;
	private JButton btnReset;
	int size = 10000;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					DrawingsTarget frame = new DrawingsTarget();
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
	public DrawingsTarget() {
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(DrawingsTarget.class.getResource("/drawing_test/pacman.jpg")));
		setTitle("Noise Generator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(51, 102, 102));
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		pnlControl = new JPanel();
		pnlControl.setBackground(Color.LIGHT_GRAY);
		pnlControl.setBorder(new LineBorder(new Color(102, 0, 0)));
		pnlControl.setLayout(new MigLayout("", "[][grow]", "[][][][][][grow][][bottom]"));

		btnDraw = new JButton("\u0427\u0435\u0440\u0442\u0435\u0436");
		btnDraw.setForeground(new Color(0, 0, 0));
		btnDraw.addActionListener(new ActionListener() {

			private float[][] noise;

			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				// Извличане на графиката
				Graphics g = pnlCanvas.getGraphics();

				// Извличане на ширината на панела
				int w = pnlCanvas.getWidth();
				// Извличане на височината на панела
				int h = pnlCanvas.getHeight();

				int[][] simNoise = new int[250][250];
				float zoff = (float) Math.random();

				float xoff = 0f;
				for (int x = 0; x < 1000; x++) {
					float yoff = 0f;
					for (int y = 0; y < 1000; y++) {
						// simNoise[x][y]= (int) (snoise.noise(xoff, yoff, zoff)*128+128);
						Color gridColor = new Color((int) (snoise.noise(xoff, yoff, zoff) * 128 + 128),
								(int) (snoise.noise(xoff, yoff, zoff) * 128 + 128),
								(int) (snoise.noise(xoff, yoff, zoff) * 128 + 128));
						g.setColor(gridColor);
						g.fillRect(x * 2, y * 2, 2, 2);
						yoff += 0.01f;

					}
					xoff += 0.01f;
				}
				zoff += 0.01f;

			}
		});
		pnlControl.add(btnDraw, "cell 0 0,growx,aligny bottom");

		pnlCanvas = new JPanel();
		pnlCanvas.setForeground(new Color(102, 0, 0));
		pnlCanvas.setBackground(new Color(255, 255, 255));
		pnlCanvas.setBorder(new LineBorder(new Color(102, 0, 0)));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(pnlControl, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(pnlCanvas, GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(pnlControl, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
				.addComponent(pnlCanvas, GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE));

		btnReset = new JButton("\u0418\u0437\u0447\u0438\u0441\u0442\u0438");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnlCanvas.repaint();

			}
		});
		pnlControl.add(btnReset, "cell 0 1");

		JButton btnExit = new JButton("\u0418\u0437\u0445\u043E\u0434");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setForeground(new Color(102, 0, 0));
		pnlControl.add(btnExit, "cell 0 7,growx,aligny top");
		contentPane.setLayout(gl_contentPane);
	}

}
