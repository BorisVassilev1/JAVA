package org.boby.RayTracing.utils;

public class FramerateManager {
	
	public int average_framerate = 0;
	public float average_framerate_calc = 0;
	public int current_framerate = 0;
	public int frame_count = 0;
	public int frame_offset = 60;
	
	int target_framerate = 120;
	
	Time time;
	
	public FramerateManager(Time time) {
		this.time = time;
	}
	
	public void update() {
		//sync(target_framerate);
		current_framerate = calculateFrameRate();
		average_framerate_calc += current_framerate / (float)(frame_offset);
		frame_count = (frame_count + 1) % frame_offset;
		
		
		if(frame_count == 0) { 
			average_framerate = (int)Math.ceil(average_framerate_calc);
//			System.out.println(average_framerate_calc);
			average_framerate_calc = 0;
		}
	}
	
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
		
//		long endTime =  Time.timeNow + nanoseconds_per_frame;
//		while(System.nanoTime() < endTime) {
//			try {
//				Thread.sleep(1);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	
	public int calculateFrameRate() {
		return (int)(1000000000 / (System.nanoTime() - time.timeNow));
	}
}
