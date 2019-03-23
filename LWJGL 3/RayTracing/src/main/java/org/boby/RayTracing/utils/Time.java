package org.boby.RayTracing.utils;

public class Time {// traces the time between updates
	
	public static long deltaTimeI;// in nanoseconds
	public static double deltaTime;// in seconds
	public static long timeNow;
	public static long timePrev;
	
	public static void initTime()
	{
		timeNow = System.nanoTime();
		timePrev = System.nanoTime();;
	}
	
	public static void updateTime()
	{
		timeNow = System.nanoTime();
		deltaTimeI = timeNow - timePrev;
		timePrev = System.nanoTime();
		deltaTime = deltaTimeI / 1000000000.0;
	}
	
	
}

