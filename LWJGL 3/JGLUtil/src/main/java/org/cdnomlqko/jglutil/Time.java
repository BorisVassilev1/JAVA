package org.cdnomlqko.jglutil;

/**
 * This class handles time.
 * @author CDnoMlqko
 *
 */
public class Time {
	/**
	 * Time elapsed from previous updateTime() call in nanoseconds.
	 */
	public long deltaTimeI;// in nanoseconds
	/**
	 * Time elapsed from previous updateTime() call in seconds.
	 */
	public double deltaTime;// in seconds
	/**
	 * System.nanoTime() at the last call of UpdateTime().
	 */
	public long timeNow;
	/**
	 * System.nanoTime() at the previous call of UpdataTime().
	 */
	public long timePrev;
	/**
	 * System.nanoTime() at the initTime() call.
	 */
	public long startTime;
	/**
	 * time elapsed from the initTime() call.
	 */
	public long timeFromStart;
	
	public Time()
	{
		timeNow = System.nanoTime();
		timePrev = timeNow;
		startTime = timeNow;
		timeFromStart = 0;
	}
	
	/**
	 * Update. Call this every game loop for it to work.
	 */
	public void updateTime()
	{
		timeNow = System.nanoTime();
		deltaTimeI = timeNow - timePrev;
		timePrev = System.nanoTime();
		deltaTime = deltaTimeI / 1000000000.0;
		timeFromStart = timeNow - startTime; 
	}
	
	
}

