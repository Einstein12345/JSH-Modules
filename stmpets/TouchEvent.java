package stmpets;

import terra.shell.utils.keys.Event;
import terra.shell.utils.math.Coordinate;

public class TouchEvent implements Event {
	private Coordinate origin;

	@EventPriority(id = "stmpe-ts", value = Event.MED)
	public TouchEvent(Coordinate coords) {
		origin = coords;
	}

	@Override
	public String getCreator() {
		// TODO Auto-generated method stub
		return "stmpe-ts";
	}

	public int getX() {
		return origin.getX();
	}

	public int getY() {
		return origin.getY();
	}

}
