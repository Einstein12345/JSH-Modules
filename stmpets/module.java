package stmpets;

import terra.shell.modules.Module;
import terra.shell.modules.ModuleEvent;
import terra.shell.utils.keys.EventInterpreter;
import terra.shell.utils.system.EventManager;

public class module extends Module {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4655749939111813369L;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "stmpe-ts-driver";
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "0.1";
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "DS";
	}

	@Override
	public String getOrg() {
		// TODO Auto-generated method stub
		return "T3RRA";
	}

	@Override
	public void onEnable() {
		EventInterpreter.registerType("/dev/input/event2",
				new TouchScreenType());
		EventManager.registerEvType("stmpe-ts");
		//EventManager.registerListener(new TouchEventListener(), "stmpe-ts");
		getLogger().log("Registered stmpe-ts");
	}

	@Override
	public void trigger(ModuleEvent.DummyEvent me) {
		getLogger().log("Triggered");
	}

}
