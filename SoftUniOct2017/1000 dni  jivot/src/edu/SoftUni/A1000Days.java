package edu.SoftUni;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Scanner;

public class A1000Days {
	
	public static void main(String[] args)
	{
		
		Scanner scanner = new Scanner(System.in);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate date = LocalDate.parse( scanner.nextLine(), formatter);
		date.format(formatter);
		
		System.out.println(date.plusDays(999).format(formatter));
		
		
	}
}
//25-02-1995