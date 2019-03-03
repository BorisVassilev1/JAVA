package org.boby.Tanks_Game.launcher;

import java.awt.EventQueue;

import javax.swing.JFrame;

import org.boby.Tanks_Game.App;
import org.boby.Tanks_Game.NativeLoader;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;
import java.awt.event.ActionEvent;

public class Main {// това е launcher-а на играта

	private JFrame frame;
	public static Thread gameThread;
	public static App app;
	private static JButton btnPlay;
	private Socket socket;
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
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
			socket = IO.socket("http://localhos:3001");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		socket.on("Match Found", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				//TODO: start the client for the found game
				
		}});
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// искаме при затваряне на единия прозорец, да се затвори и другия.
		frame.getContentPane().setLayout(null);
		
		JButton btnPlay = new JButton("play!!");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				setStartButtonVisibility(false);
				gameThread = new Thread(app);
				gameThread.setName("Tanks Game");
				gameThread.start();// стартиране на играта
			}
		});
		btnPlay.setBounds(12, 13, 97, 25);
		frame.getContentPane().add(btnPlay);
		
		JButton btnFindMatchPlz = new JButton("find match plz");
		btnFindMatchPlz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JSONObject obj = new JSONObject();
				try {
					obj.put("Random", true);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				socket.emit("Find Match", obj);
			}
		});
		btnFindMatchPlz.setBounds(134, 74, 135, 58);
		frame.getContentPane().add(btnFindMatchPlz);
	}
	
	public static void setStartButtonVisibility(boolean a)
	{
		btnPlay.setVisible(a);
	}
}
