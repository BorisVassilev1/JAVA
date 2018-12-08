package edu.school.sedmitsa18;

import java.util.ArrayList;
import java.util.function.Function;

public class Main {

	public static void main(String[] args) {
		ArrayList<Integer> a = new ArrayList<Integer>();
		for(int i = 0 ; i < 10; i ++)
		{
			a.add((int) (Math.random() * 10));
		}
		
		System.out.println(a.toString());
		
		ArrayList<Integer> b = filter(a, (x) -> 
		{
			return x == 5 || x == 6;	
		});
		
		System.out.println(b.toString());
	}
	
	public static ArrayList<Integer> filter (ArrayList<Integer> a, Function<Integer,Boolean> func)
	{
		ArrayList<Integer> b = new ArrayList<Integer>();
		for(int i = 0; i < a.size(); i ++)
		{
			if(func.apply(a.get(i)))
			{
				b.add(a.get(i));
			}
		}
		return b;
	}
	
}
