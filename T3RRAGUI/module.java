package T3RRAGUI;

import terra.shell.modules.Module;
import terra.shell.modules.ModuleEvent;

public class module extends Module {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8757584057732671559L;

	@Override
	public String getName() {
		return "TGUI";
	}

	@Override
	public void run() {

	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "0.1";
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "D.S";
	}

	@Override
	public String getOrg() {
		// TODO Auto-generated method stub
		return "T3RRA";
	}

	@Override
	public void onEnable() {
		log.log("TGUI Enabled");
	}

	@Override
	public void trigger(ModuleEvent.DummyEvent me) {
		// TODO Auto-generated method stub

	}

}
