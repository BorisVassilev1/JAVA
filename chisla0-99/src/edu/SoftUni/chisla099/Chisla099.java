package edu.SoftUni.chisla099;

import java.util.Scanner;

public class Chisla099 {
	public static void main(String[] args)
	{
		String[] Numbers= new String[] {
			"zero","one","two","three","four","five","six","seven","eight","nine","ten",
			"eleven","twelve","thirteen","fourteen","fifteen","sixteen","seventeen","eighteen","nineteen","twenty",
			"thirty","forty","fifty","sixty","seventy","eighty","ninety","one hundred"
		};
		
		Scanner scan = new Scanner(System.in);
		String out;
		int a = Integer.parseInt(scan.nextLine());
		
		if(a>=0 && a<=100)
		{
			if(a<=20)
			{
				out = Numbers[a];
			}
			else if(a%10!=0)
			{
				out = Numbers[a/10+18]+" "+Numbers[a%10];
			}
			else
			{
				out = Numbers[a/10+18];
			}
		System.out.println(out);
		}
		else
		{
			System.out.println("invalid number");
		}
		
	}
}
