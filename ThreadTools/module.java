package ThreadTools;

import terra.shell.logging.LogManager;
import terra.shell.logging.Logger;
import terra.shell.modules.Module;
import terra.shell.modules.ModuleEvent;

public class module extends Module {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2084665288733978151L;
	private static Logger log = LogManager.getLogger("ThreadTools");

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ThreadTools";
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
		return "DS";
	}

	@Override
	public String getOrg() {
		// TODO Auto-generated method stub
		return "T3RRA";
	}

	public static void threadWait(final Thread t) {
		Thread th = new Thread(new Runnable() {
			public void run() {
				synchronized (t) {
					try {
						t.wait();
					} catch (Exception e) {
						getLogger().log("Failed to force " + t.getName() + " to pause!");
					}
				}
			}
		});
		th.setName("WT:" + t.getName());
		th.start();
	}

	@Override
	public void onEnable() {
		getLogger().log("ThreadTools Module Lib is now loaded");
	}

	@Override
	public void trigger(ModuleEvent.DummyEvent me) {
		// TODO Auto-generated method stub

	}

}
