package edu.vkashti.bikoveikravi;

import java.util.Scanner;

public class Main {
	// globalni promenlivi:
	static Scanner console;
	static String secNum;
	static String num;
	static boolean isInputValid;

	public static void main(String[] args) {
		console = new Scanner(System.in);
		// izbirane na slochaino chislo mejdu 1000 i 9999;
		secNum = Integer.toString((int) (Math.random() * 9000 + 1000));
		System.out.println(secNum);
		num = "";
		boolean isInputValid = false;

		input();

		while (!secNum.equals(num)) {
			int bikove = 0;
			int kravi = 0;
			// namirane na broq bikove i kravi:
			for (int i = 0; i < num.length(); i++) {
				// proverka za bik:
				if (num.charAt(i) == secNum.charAt(i)) {
					bikove++;
				}
				// proverka za krava:
				for (int j = 0; j < num.length(); j++) {
					// ako i = j i cifrite sa =, to sme namerili bik, a ne krava.
					if (i != j && secNum.charAt(i) == num.charAt(j)) {
						kravi++;
					}
				}
			}
			// pehatane na rezultat:
			System.out.printf("Vasheto chislo ima: %d bika i %d kravi.%n", bikove, kravi);
			// vavejdane na novo chislo:
			System.out.println("Vavedete sledvashto chislo");
			input();
		}
	}

	public static void input() {
		// ako vavedenoto chislo ne e validno, trqbva da vavedem novo.
		isInputValid = false;
		while (isInputValid == false) {
			// vavejdane:
			num = console.nextLine();
			// proverka dali tainoto chislo ne e validno:
			if (secNum.length() != num.length()) {
				System.out.println("invalid input!");
			} else {
				// togava to e validno:
				isInputValid = true;
			}
		}
	}
}
