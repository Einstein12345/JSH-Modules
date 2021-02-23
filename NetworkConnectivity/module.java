package NetworkConnectivity;

import terra.shell.modules.Module;
import terra.shell.modules.ModuleEvent.DummyEvent;

public class module extends Module {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6369560331522476652L;

	@Override
	public String getName() {
		return "NetWatch";
	}

	@Override
	public void run() {
		getLogger().log("Running");
	}

	@Override
	public String getVersion() {
		return "0.1";
	}

	@Override
	public String getAuthor() {
		return "DS";
	}

	@Override
	public String getOrg() {
		return "T3RRA";
	}

	@Override
	public void onEnable() {
		getLogger().log("NetWatch Module Enabled!");
	}

	@Override
	public void trigger(DummyEvent event) {
		getLogger().log("Triggered!");
	}

}
