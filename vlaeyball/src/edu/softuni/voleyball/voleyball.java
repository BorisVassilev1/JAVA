package edu.softuni.voleyball;

import java.util.Scanner;

public class voleyball {
	public static void main(String[] args)
	{
		Scanner console = new Scanner(System.in);
		
		String year = console.nextLine();
		int p = Integer.parseInt(console.nextLine());
		int h = Integer.parseInt(console.nextLine());
		
		float sum = (48 - h)*3.0f/4 + h + p*2.0f/3;
		
		if(year.equals("leap"))
		{
			sum+=15.0f/100*sum;
		}
		int fsum = (int) Math.floor(sum);
		System.out.println(fsum);
		
	}
}
