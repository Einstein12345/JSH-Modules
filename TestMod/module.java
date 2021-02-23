package TestMod;

import terra.shell.modules.Module;
import terra.shell.modules.ModuleEvent;

public class module extends Module {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1361215659063730323L;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "TestMod";

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		NetworkConnectivity.module m = new NetworkConnectivity.module();
		getLogger().log(m.getName());
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
		getLogger().log("TestModule Enabled!");
	}

	@Override
	public void trigger(ModuleEvent.DummyEvent me) {
		// TODO Auto-generated method stub

	}
}
