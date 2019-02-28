package stmpets;

import terra.shell.utils.keys.Event;
import terra.shell.utils.keys.EventInformation;
import terra.shell.utils.math.Coordinate;
import terra.shell.utils.system.EventManager;

public class TouchScreenType implements terra.shell.utils.keys.EventType {
	private int x;
	private int y;

	@Override
	public Event createEvent(EventInformation data) {
		if (data.type() == 3) {
			if (data.code() == 0) {
				x = data.value();
			} else if (data.code() == 1) {
				y = data.value();
			}
		} else if (data.type() == 0) {
			TouchEvent te = new TouchEvent(new Coordinate(x, y));
			EventManager.invokeEvent(te);
		}
		return null;
	}

	@Override
	public String type() {
		// TODO Auto-generated method stub
		return "stmpets";
	}

}
