package edu.softuni.voleyball;

import java.util.Scanner;

public class pointInFigure {
	
	public static void main(String[] args)
	{
		Scanner console = new Scanner(System.in);
		
		int h = console.nextInt();
		int x = console.nextInt();
		int y = console.nextInt();
		//1,1  2,4
		//0,0  3,1
		if((x>1*h && x<2*h && y>1*h && y<4*h) || (x>0*h && x<3*h && y>0*h && y<1*h))
		{
			System.out.println("inside");
		}
		else if((x<1*h || x>2*h || y<1*h || y>4*h) && (x<0*h || x>3*h || y<0*h || y>1*h))
		{
			System.out.println("outside");
		}
		else if(x>1*h && x<2*h && y==h)
		{
			System.out.println("inside");
		}
		else
		{
			System.out.println("border");
		}
	}
}
