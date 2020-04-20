package com.tonelope.tennis.scoreprocessor;

import com.tonelope.tennis.scoreprocessor.AbstractPoint.Team;

/**
 *
 *
 * @author Tony Lopez
 *
 */
public class ConversionUtils {

	private ConversionUtils() {
		
	}
	
	public static AbstractPoint[] binaryStringToPoints(String str) {
		AbstractPoint[] points = new AbstractPoint[str.length()];
		for(int i = 0; i < str.length(); i++) {
			points[i] = new Point(str.charAt(i) == '0' ? Team.HOME : Team.VISITOR);
		}
		return points;
	}
}
