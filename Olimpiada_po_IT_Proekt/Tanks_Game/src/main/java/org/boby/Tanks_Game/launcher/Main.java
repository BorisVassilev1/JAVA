package org.boby.Tanks_Game.launcher;

import java.awt.EventQueue;

import javax.swing.JFrame;

import org.boby.Tanks_Game.App;
import org.boby.Tanks_Game.NativeLoader;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;

public class Main {// това е launcher-а на играта

	private JFrame frame;
	public static Thread gameThread;
	public static App app;
	public static JButton btnFindMatch;
	private static JButton btnPlay;
	private static JButton btnAccept;
	private static JLabel lblMatchFound;
	public static Socket gameSocket;
	public static Socket socket;
	private static boolean isPlaying = false;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {//стартиране на прозореца
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
		NativeLoader.loadNatives("lib/natives-win");//natives на lwjgl библиотеката се налага да бъдат добавени, тъй като по неясна за мен причина maven не го прави
		app = new App();
		gameThread = new Thread(app);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
			socket = IO.socket("http://localhost:3001");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
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
				int i = (Integer) args[0];
				
				btnAccept = new JButton("Accept");
				btnAccept.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.out.println("You have accepted the match");
						ack.call(args[0]);
						btnAccept.removeActionListener(this);
						btnAccept.setEnabled(false);
						lblMatchFound.setEnabled(false);
					}
				});
				btnAccept.setBounds(167, 111, 89, 23);
				frame.getContentPane().add(btnAccept);
				btnAccept.setEnabled(true);
				
				lblMatchFound = new JLabel("Match found!");
				lblMatchFound.setFont(new Font("Tahoma", Font.PLAIN, 16));
				lblMatchFound.setBounds(167, 76, 98, 24);
				frame.getContentPane().add(lblMatchFound);
				lblMatchFound.setEnabled(true);
				System.out.println("created the buttons on the screen!");
				
		}});
		
		socket.on("Game", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				System.out.println("Game " + args[0]);
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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// искаме при затваряне на единия прозорец, да се затвори и другия.
		frame.getContentPane().setLayout(null);
		
		btnPlay = new JButton("play!!");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				//startGame();
				gameSocket.connect();
				System.out.println("trying to connect...");
		    	System.out.println("if it is taking too long, your internet connection may be interrupted! "
		    			+ "\nUse Ctrl + c to terminate the process. Chack your connection and try again.");
			}
		});
		btnPlay.setBounds(12, 13, 97, 25);
		frame.getContentPane().add(btnPlay);
		
		btnFindMatch = new JButton("find match");
		btnFindMatch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				JSONObject obj = new JSONObject();
//				try {
//					obj.put("Random", true);
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				if(!socket.connected())
//				{
//					System.out.println("the socket isnt connected");
//				}
				socket.emit("Find Match");
				btnFindMatch.setEnabled(false);
			}
		});
		btnFindMatch.setBounds(157, 190, 108, 30);
		frame.getContentPane().add(btnFindMatch);
		
		
	}
	
	public static void setStartButtonVisibility(boolean a)
	{
		btnPlay.setVisible(a);
	}
	public static void startGame()
	{
		setStartButtonVisibility(false);
		gameThread = new Thread(app);
		gameThread.setName("Tanks Game");
		gameThread.start();// стартиране на играта
		isPlaying = true;
	}
}
