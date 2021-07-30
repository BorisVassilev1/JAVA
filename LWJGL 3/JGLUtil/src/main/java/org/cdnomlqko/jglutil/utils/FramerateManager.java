package org.cdnomlqko.jglutil.utils;

import static org.lwjgl.glfw.GLFW.*;

import java.util.Timer;
import java.util.TimerTask;

import org.cdnomlqko.jglutil.Time;

/** 
 * Counts framerate, tracks time passed each update.
 * @author CDnoMlqko
 *
 */
public class FramerateManager {
	
	/**
	 * how many nanoseconds has the previous frame taken
	 */
	public long nano_this_frame;
	
	private double lastPrintTime;
	private int frames_last_second;
	
	private Time time;
	
	private Timer t;
	private TimerTask tt;
	
	@FunctionalInterface
	public interface OneSecondPassedCallback {
		public void apply (long frames_this_second);
	}
	
	private OneSecondPassedCallback callback;
		
	public FramerateManager(Time time) {
		this.time = time;
		lastPrintTime = glfwGetTime();
				
		t = new Timer();
		tt = new TimerTask() {
			@Override
			public void run() {
				double glfwTimeNow = glfwGetTime();
				if (callback != null)
					callback.apply(frames_last_second);

				frames_last_second = 0;
				lastPrintTime = glfwTimeNow;
			};
		};
		t.scheduleAtFixedRate(tt, 0, 1000);
	}
	
	/**
	 * call this every iteration of the game loop
	 */
	public void update() {
		nano_this_frame = calculateNanoThisFrame();
		
		frames_last_second ++;
	}
	/**
	 * call this at the end of each iteration of the game loop. Preferably, use it only if vsync is off.
	 * @param FPS - desired framerate.
	 */
	public void sync(int FPS) {
		long nanoseconds_per_frame = 1000000000 / FPS;
		long offset = time.timeNow + nanoseconds_per_frame - System.nanoTime();
		
		if(offset > 0) {
			try {
				Thread.sleep((int)Math.ceil(offset / 1000000f));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Calculates how many nanoseconds have passed from the beginning of the current iteration of the game loop.
	 * @return the result of that calculation
	 */
	public long calculateNanoThisFrame() {
		return System.nanoTime() - time.timeNow;
	}
	
	/**
	 * func will be called every second
	 * @param func - the function to be called
	 */
	public void setSecondPassedCallback(OneSecondPassedCallback func) {
		this.callback = func;
	}
	
	public void stop() {
		//fpsThread.stop();
		tt.cancel();
		t.purge();
		t.cancel();
	}
}
