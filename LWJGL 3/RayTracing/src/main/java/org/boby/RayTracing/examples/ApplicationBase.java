package org.boby.RayTracing.examples;

import org.lwjgl.Version;

public abstract class ApplicationBase {
	public void run() {
//		Configuration.DEBUG.set(true);

		System.out.println("LWJGL version: " + Version.getVersion());

		init();
		loop();
		cleanup();
	}
	public abstract void init();
	
	public abstract void loop();
	
	public abstract void cleanup();
}
