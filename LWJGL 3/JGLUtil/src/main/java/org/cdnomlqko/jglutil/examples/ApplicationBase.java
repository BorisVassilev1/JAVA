package org.cdnomlqko.jglutil.examples;

import org.lwjgl.Version;

/**
 * This class is used as a sample structure of an application.
 * @author CDnoMlqko
 *
 */
public abstract class ApplicationBase {
	/**
	 * starts the program
	 */
	public void run() {
		System.out.println("LWJGL version: " + Version.getVersion());

		init();
		loop();
		cleanup();
	}
	/**
	 * called first, for initialization
	 */
	public abstract void init();
	
	/**
	 * this will be the game loop
	 */
	public abstract void loop();
	
	/**
	 * called just before exit, for deallocation of memory.
	 */
	public abstract void cleanup();
}
