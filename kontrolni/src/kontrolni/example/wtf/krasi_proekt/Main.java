package kontrolni.example.wtf.krasi_proekt;

import java.util.Scanner;

public class Main {
	public static Player p1 = new Player(), p2 = new Player();
	public static Scanner sc = new Scanner(System.in);
	
	public static int turn_id = 0;
	
	public static void main(String[] args) {
		System.out.println("wellCUM to my game!!!");
		
		while(true) {
			
			if(turn_id%2 == 0) {
				turn(p1, p2);
			} else {
				turn(p2, p1);
			}
			turn_id++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }
	
	public static void turn(Player me, Player other) {
		print_board(me, other);
		
		System.out.println("\nChoose index in your hand to play.");
		int i = sc.nextInt();
		
		Card play = me.getHand().remove(i);
		me.getOn_board().add(play);
		
		print_board(me, other);
		System.out.println("\nChoose a card of yours to attack your opponent's. Type -1 to skip");
		int x = sc.nextInt();
		if(x == -1) {
			System.out.println("you skipped!");
			return;
		}
		int y = sc.nextInt();
		Card c1 = me.getOn_board().get(x);
		Card c2 = other.getOn_board().get(y);
		c1.attack(c2);
		
		if(!c1.isAlive()) me.getOn_board().remove(x);
		if(!c2.isAlive()) other.getOn_board().remove(y);
		
		print_board(me, other);
	}
	
	public static void print_board(Player me, Player other) {
		int player_id = turn_id % 2 + 1;
		System.out.println("\n\n\n\nplayer " + player_id + " is on turn now!");
		
		System.out.println("Your hand:");
		System.out.println(me.getHand().toString());
		
		System.out.println("Enemy board:");
		System.out.println(other.getOn_board().toString());
		
		System.out.println("Your board:");
		System.out.println(me.getOn_board().toString());
	}
	
}
