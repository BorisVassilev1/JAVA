package org.cdnomlqko.jglutil.data;

import java.nio.ByteBuffer;

/**
 * An object that can be written into a buffer
 * @author CDnoMlqko
 *
 */
public interface Bufferable {
	/**
	 * Writes the object's data to a buffer.
	 * @param buff - buffer to write in
	 * @param offset - offset from the start of the buffer
	 */
	public void writeToBuffer(ByteBuffer buff, int offset);
}
