package org.boby.Tanks_Game;

public class Time {// клас, който измерва времето между кадрите, за да не настъпват отклонения при устройства с различни възможности
	
	static long deltaTimeI;// в секунди
	static double deltaTime;// в наносекунди
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

