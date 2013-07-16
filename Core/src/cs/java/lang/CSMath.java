package cs.java.lang;

import cs.java.common.Point;

public class CSMath {

	public static float getDistance(Point point1, Point point2) {
		double dist = Math.sqrt((point1.x() - point2.x()) * (point1.x() - point2.x())
				+ (point1.y() - point2.y()) * (point1.y() - point2.y()));
		return (float) dist;
	}

}
