package org.boby.Tanks_Game;

public class Time {// traces the time between updates
	
	static long deltaTimeI;// in seconds
	static double deltaTime;// in nanosecs
	static long timeNow;
	static long timePrev;
	
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

