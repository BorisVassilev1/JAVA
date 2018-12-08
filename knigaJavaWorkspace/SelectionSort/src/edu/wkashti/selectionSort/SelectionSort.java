package edu.wkashti.selectionSort;

import java.util.Arrays;
import java.util.Scanner;

public class SelectionSort {
	public static void main(String[] args) {
		Scanner console = new Scanner(System.in);
		int n = Integer.parseInt(console.nextLine());
		int[] nums1 = new int[n];
		int[] nums2 = new int[n];
		int swaps1 = 0;
		int swaps2 = 0;
		for(int i=0; i<n; i++)
		{
			nums1[i]=Integer.parseInt(console.nextLine());
			nums2[i] = nums1[i];
		}
		for(int i = 0; i < n; i++)
		{
			int minIndex = i;
			for(int j = i + 1; j < n; j ++)
			{
				if(nums1[j] < nums1[minIndex])
				{
					minIndex=j;
				}
			}
			
			if(minIndex != i)
			{
				int c = nums1[minIndex];
				nums1[minIndex] = nums1[i];
				nums1[i] = c;
				swaps1 ++;
			}
		}
		
		for(int i = 0; i < n; i++)
		{
			int minIndex = i;
			for(int j = i + 1; j < n; j ++)
			{
				if(nums2[j] > nums2[minIndex])
				{
					minIndex=j;
				}
			}
			
			if(minIndex != i)
			{
				int c = nums2[minIndex];
				nums2[minIndex] = nums2[i];
				nums2[i] = c;
				swaps2 ++;
			}
		}
		
		System.out.println(Math.min(swaps1, swaps2));
	}
	
	public static void swap(int a,int b)
	{
		int c = a;
		a = b;
		b = c;
	}
}
