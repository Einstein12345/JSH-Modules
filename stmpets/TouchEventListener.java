package stmpets;

import terra.shell.logging.LogManager;
import terra.shell.logging.Logger;
import terra.shell.utils.keys.Event;
import terra.shell.utils.system.EventListener;

public class TouchEventListener extends EventListener {
	private static Logger log = LogManager.getLogger("stmpe-ts");

	@Override
	public void trigger(Event e) {
		TouchEvent te = (TouchEvent) e;
		getLogger().log(te.getX() + " : " + te.getY());
	}

}
