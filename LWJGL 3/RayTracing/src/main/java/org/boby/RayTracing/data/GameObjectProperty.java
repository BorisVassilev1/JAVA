package org.boby.RayTracing.data;

import java.nio.ByteBuffer;

public interface GameObjectProperty {
	public int getSize();
	
	public void writeToBuffer(ByteBuffer buff, int offset);
}
