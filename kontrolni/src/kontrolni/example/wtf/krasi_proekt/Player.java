package kontrolni.example.wtf.krasi_proekt;

import java.util.ArrayList;

public class Player {
	private ArrayList<Card> hand;
	private ArrayList<Card> on_board;
	
	public ArrayList<Card> getHand() {
		return hand;
	}

	public ArrayList<Card> getOn_board() {
		return on_board;
	}

	public Player() {
		hand = new ArrayList<>();
		on_board = new ArrayList<>();
		
		hand.add(new Card(1, 1));
		hand.add(new Card(2, 2));
		hand.add(new Card(3, 3));
		hand.add(new Card(4, 4));
		hand.add(new Card(5, 5));
	}
}
