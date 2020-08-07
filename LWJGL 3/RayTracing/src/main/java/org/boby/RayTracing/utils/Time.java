package org.boby.RayTracing.utils;

public class Time {// traces the time between updates
	/**
	 * Time elapsed from previous updateTime() call in nanoseconds.
	 */
	public static long deltaTimeI;// in nanoseconds
	/**
	 * Time elapsed from previous updateTime() call in seconds.
	 */
	public static double deltaTime;// in seconds
	/**
	 * System.nanoTime() at the last call of UpdateTime().
	 */
	public static long timeNow;
	/**
	 * System.nanoTime() at the previous call of UpdataTime().
	 */
	public static long timePrev;
	/**
	 * System.nanoTime() at the initTime() call.
	 */
	public static long startTime;
	/**
	 * time elapsed from the initTime() call.
	 */
	public static long timeFromStart;
	
	/**
	 * Initialize. Call once on program startup.
	 */
	public static void initTime()
	{
		timeNow = System.nanoTime();
		timePrev = timeNow;
		startTime = timeNow;
		timeFromStart = 0;
	}
	
	/**
	 * Update
	 */
	public static void updateTime()
	{
		timeNow = System.nanoTime();
		deltaTimeI = timeNow - timePrev;
		timePrev = System.nanoTime();
		deltaTime = deltaTimeI / 1000000000.0;
		timeFromStart = timeNow - startTime; 
	}
	
	
}

