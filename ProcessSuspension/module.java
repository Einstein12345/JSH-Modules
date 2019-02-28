package ProcessSuspension;

import terra.shell.modules.Module;
import terra.shell.modules.ModuleEvent.DummyEvent;
import terra.shell.utils.JProcess;

public class module extends Module {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9053724507394736745L;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "PS";
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
		return "D.S";
	}

	@Override
	public String getOrg() {
		// TODO Auto-generated method stub
		return "T3RRA";
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		log.log("Init Process Suspension Module!");
	}

	@Override
	public void trigger(DummyEvent event) {
		Object[] args = event.getME().getArgs();
		try {
			JProcess p = (JProcess) args[0];
			p.suspend();
			log.log("Suspended " + args[0]);
		} catch (ClassCastException e) {
			log.log("Cannot suspend " + args[0] + " is not JProcess");
		}
	}

}
