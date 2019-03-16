package org.boby.Tanks_Game.launcher;

import java.awt.EventQueue;

import javax.swing.JFrame;

import org.boby.Tanks_Game.App;
import org.boby.Tanks_Game.NativeLoader;
import org.boby.Tanks_Game.Utils;
import org.newdawn.slick.opengl.Texture;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.net.URISyntaxException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

public class Main {// това е launcher-а на играта

	private JFrame frame;
	public static Thread gameThread;
	public static App app;
	public static JButton btnFindMatch;
	//private static JButton btnPlay;
	private static JButton btnAccept;
	private static JButton btnDecline;
	private static JButton btnExitQueue;
	private static JLabel lblMatchFound;
	private static JLabel lblWaiting;
	public static Socket gameSocket;
	public static Socket socket;
	private static boolean isPlaying = false;
	private static boolean isInQueue = false;
	public static Texture tankTex;
	public static Texture backgroundTex;
	public static Texture bulletTex;
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

	public Main() {
		NativeLoader.loadNatives("lib/natives-win");//loading lwjgl natives
		
		
		app = new App();
		gameThread = new Thread(app);
		initialize();
	}

	private void initialize() {
		try {
			socket = IO.socket("http://localhost:3001");
			gameSocket = IO.socket("http://localhost:3000");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		app.initSockets();
		
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				System.out.println("connected to the server successfully!");
		}});
		
		socket.on("Match Found", new Emitter.Listener() {
			@Override
			public void call(final Object... args) {
				//TODO: start the client for the found game
				System.out.println("the server has found a game for you!");
				final Ack ack = (Ack) args[args.length - 1];
				final Utils.HappeningContainer happCont = new Utils.HappeningContainer();
				int i = (Integer) args[0];
				
				new java.util.Timer().schedule( 
				        new java.util.TimerTask() {
				            @Override
				            public void run() {
				            	if(!happCont.hasHappened())
				            	{
					            	btnFindMatch.setEnabled(true);
									btnExitQueue.setEnabled(false);
									btnAccept.setVisible(false);
									btnDecline.setVisible(false);
									lblMatchFound.setVisible(false);
									ack.call(args[0], false);
									socket.emit("Leave Queue");
									isInQueue = false;
				            	}
				            }
				        }, 
				        5000 
				);
				
				lblMatchFound.setVisible(true);
				
				btnAccept = new JButton("Accept");
				btnAccept.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.out.println("You have accepted the match");
						happCont.happen();
						btnDecline.setEnabled(false);
						btnAccept.setEnabled(false);
						lblMatchFound.setVisible(false);
						lblWaiting.setVisible(true);
						lblMatchFound.setVisible(false);
						ack.call(args[0], true);
					}
				});
				btnAccept.setBounds(167, 111, 89, 23);
				frame.getContentPane().add(btnAccept);
				
				btnDecline = new JButton("Decline");
				btnDecline.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.out.println("You have accepted the match");
						happCont.happen();
						btnAccept.setEnabled(false);
						btnDecline.setEnabled(false);
						lblMatchFound.setVisible(false);
						lblWaiting.setVisible(true);
						lblMatchFound.setVisible(false);
						ack.call(args[0], false);
					}
				});
				btnDecline.setBounds(167, 85, 89, 23);
				frame.getContentPane().add(btnDecline);
				
				
				System.out.println("created the buttons on the screen!");
				frame.getContentPane().update(frame.getContentPane().getGraphics());
				
		}});
		
		socket.on("Game", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				
				
				if((Boolean) args[0])
				{
					lblWaiting.setVisible(false);
					btnAccept.setVisible(false);
					btnDecline.setVisible(false);
					
					btnExitQueue.setEnabled(false);
					btnExitQueue.setVisible(false);
					System.out.println("Game start");
					//startGame();
					gameSocket.connect();
					System.out.println("trying to connect...");
			    	System.out.println("if it is taking too long, your internet connection may be interrupted! "
			    			+ "\nUse Ctrl + c to terminate the process. Chack your connection and try again.");
				}
				else
				{
					lblWaiting.setVisible(false);
					btnAccept.setVisible(false);
					btnDecline.setVisible(false);
					
					btnExitQueue.setVisible(false);
					btnFindMatch.setEnabled(true);
					System.out.println("Game fail");
				}
			}
		});
		
		socket.on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				System.out.println("can't connect to server!");
				System.out.println("Your connection may be interrupted!");
				socket.disconnect();
		}});
		socket.connect();
		
		System.out.println("connecting to the server...");
		frame = new JFrame();
		frame.setIconImage(new ImageIcon("res/Tank.png").getImage());
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// искаме при затваряне на единия прозорец, да се затвори и другия.
		frame.getContentPane().setLayout(null);
		
		btnFindMatch = new JButton("Find Match");
		btnFindMatch.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnFindMatch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				socket.emit("Find Match");
				btnFindMatch.setEnabled(false);
				btnExitQueue.setVisible(true);
				btnExitQueue.setEnabled(true);
			}
		});
		btnFindMatch.setBounds(147, 182, 129, 38);
		frame.getContentPane().add(btnFindMatch);
		
		lblWaiting = new JLabel("Waiting for other players");
		lblWaiting.setBounds(147, 60, 129, 30);
		lblWaiting.setVisible(false);
		frame.getContentPane().add(lblWaiting);

		lblMatchFound = new JLabel("Match found!");
		lblMatchFound.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblMatchFound.setBounds(167, 56, 98, 24);
		lblMatchFound.setVisible(false);
		frame.getContentPane().add(lblMatchFound);
		
		btnExitQueue = new JButton("Leave Queue");
		btnExitQueue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnFindMatch.setEnabled(true);
				btnExitQueue.setVisible(false);
				socket.emit("Leave Queue");
				isInQueue = false;
			}
		});
		btnExitQueue.setBounds(157, 231, 108, 23);
		btnExitQueue.setVisible(false);
		frame.getContentPane().add(btnExitQueue);
		
		frame.setResizable(false);
		
	}
	
	public static void startGame()
	{
		gameThread = new Thread(app);//we need a new thread so when we start a second game after one has ended we dont get an exception
		gameThread.setName("Tanks Game");
		gameThread.start();// start the game
		isPlaying = true;
	}
}
