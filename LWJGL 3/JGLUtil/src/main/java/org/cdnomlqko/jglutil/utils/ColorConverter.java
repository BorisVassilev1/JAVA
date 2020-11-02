package org.cdnomlqko.jglutil.utils;

import org.joml.Vector3f;

/**
 * This class handles color conversion between RGB and HSV, using {@link Vector3f} as a color container
 * @author CDnoMlqko
 *
 */
public class ColorConverter {
	/**
	 * Converts colors from HSV to RGB
	 * @param hsv - color in HSV in the range [0, 1]
	 * @return RGB color result in the range [0, 1]
	 */
	public static Vector3f hsvToRgb(Vector3f hsv) {
		float hue = hsv.x, saturation = hsv.y, value = hsv.z;
		
	    float r, g, b;

	    int h = (int)(hue * 6);
	    float f = hue * 6 - h;
	    float p = value * (1 - saturation);
	    float q = value * (1 - f * saturation);
	    float t = value * (1 - (1 - f) * saturation);

	    if (h == 0) {
	        r = value;
	        g = t;
	        b = p;
	    } else if (h == 1) {
	        r = q;
	        g = value;
	        b = p;
	    } else if (h == 2) {
	        r = p;
	        g = value;
	        b = t;
	    } else if (h == 3) {
	        r = p;
	        g = q;
	        b = value;
	    } else if (h == 4) {
	        r = t;
	        g = p;
	        b = value;
	    } else if (h <= 6) {
	        r = value;
	        g = p;
	        b = q;
	    } else {
	        throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
	    }
	    return new Vector3f(r, g, b);
	}
}
