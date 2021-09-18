package ClusterTest;

import java.io.File;
import java.io.FileOutputStream;

import terra.shell.launch.Launch;
import terra.shell.modules.Module;
import terra.shell.modules.ModuleEvent.DummyEvent;
import terra.shell.utils.keys.Event;
import terra.shell.utils.streams.NullInputStream;

public class module extends Module {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ClusterTest";
	}

	@Override
	public void run() {
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOrg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub

	}

	@Override
	public void trigger(Event event) {
		log.debug("Triggered");
		if (event instanceof DummyEvent) {
			DummyEvent dummyEvent = (DummyEvent) event;
			if (dummyEvent.getME().getArgs()[0].toString().equals("Run")) {
				ClusterTestProc p = new ClusterTestProc();
				p.run();
				try {
					Launch.getConnectionMan().sendToAll(p,
							new FileOutputStream(new File(Launch.getConfD(), "ClusterTestResults.txt")),
							new NullInputStream());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
