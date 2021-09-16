package snake;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class HighscoreDisplay {
	public void start(Snake snake) {
		JFrame frame = new JFrame();
		frame.setAutoRequestFocus(false);
		frame.setBounds(400, 200, 300,250);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		
		JLabel lbl = new JLabel("<html>Enter yor name and then press the<br> button to record your score!<html>");
		lbl.setBounds(0, 0,300, 30);
		
		JPanel tablePanel = new JPanel();
		tablePanel.setBounds(10, 70, 270, 130);
		tablePanel.setOpaque(true);
		tablePanel.setLayout(new BorderLayout());
		
		// Create a table for the high-scores
		JTable tblHighScores = new JTable(new DefaultTableModel(new String [][] {{"gosho" , "5"}, {"pesho", "6"}}, new String[] {"Name", "Score"}));
		tblHighScores.setFillsViewportHeight(true);
		tblHighScores.setEnabled(false);
		tblHighScores.setDefaultEditor(Object.class, null);
		tblHighScores.setFont(new Font(Font.DIALOG, Font.PLAIN, 10));
		
		
		JScrollPane scrollPane = new JScrollPane(tblHighScores);
		scrollPane.setVisible(true);
		
		tablePanel.add(scrollPane);
		
		
		DefaultTableModel model = (DefaultTableModel) tblHighScores.getModel();
		Vector<String> a = new Vector<>();
		a.add("Name");
		a.add("Score");
		Vector<Vector> data = readFromFile();
		if(data != null) {
			model.setDataVector(data, a);
		}
		
		JTextField field = new JTextField("");
		field.setBounds(10, 40, 130, 20);
		
		JButton btnWrite = new JButton("write score");
		btnWrite.setBounds(150, 40, 130, 20);
		btnWrite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = field.getText();
				
				if(name.equals("")) return;
				
				DefaultTableModel model = (DefaultTableModel) tblHighScores.getModel();
				model.addRow(new String[] {name, snake.getLblScore().getText()});
				
				Vector<Vector> data = model.getDataVector();
				Collections.sort(data, new Comparator<Vector>() {
					@Override
					public int compare(Vector o1, Vector o2) {
						return -Integer.parseInt((String)o1.get(1)) + Integer.parseInt((String)o2.get(1));
					}
				});
				
				Vector<String> a = new Vector<>();
				a.add("Name");
				a.add("Score");
				model.setDataVector(data, a);
				
				writeToFile(data);
				
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); // close the window
			}
		});
		
		frame.getContentPane().add(tablePanel);
		frame.getContentPane().add(field);
		frame.getContentPane().add(btnWrite);
		frame.getContentPane().add(lbl);
		
		frame.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    	Main.isHighScoresWindowOpened = false;
		    }
		});
		
		// Show the frame
		frame.setVisible(true);
	}
	
	private void writeToFile(Vector<Vector> vec) {
		try {
			FileOutputStream fos = new FileOutputStream(new File("data"));
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(vec);
			oos.close();
			fos.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	
	private Vector<Vector> readFromFile() {
		Vector<Vector> a = null;
		try {
			FileInputStream fis = new FileInputStream(new File("data"));
			ObjectInputStream ois = new ObjectInputStream(fis);
			a = (Vector<Vector>) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			//e.printStackTrace();
		}
		return a;
	}
}
