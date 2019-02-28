package ClusterTest;

import java.io.File;
import java.io.FileOutputStream;

import terra.shell.launch.Launch;
import terra.shell.modules.Module;
import terra.shell.modules.ModuleEvent.DummyEvent;
import terra.shell.utils.JProcess;
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
	public void trigger(DummyEvent event) {
		if (event.getME().getArgs()[0].toString().equals("Run")) {
			JProcess p = new JProcess() {
				@Override
				public String getName() {
					return "TestClusterProcess";
				}

				@Override
				public boolean start() {
					log.log("Process started");
					log.log("My UUID is: " + this.getUUID());
					log.log("Launch contains " + Launch.cmds.size() + " commands");
					log.log("Process completed");
					return true;
				}

			};
			try {
				Launch.getConnectionMan().queueProcess(p,
						new FileOutputStream(new File(Launch.getConfD(), "ClusterTestResults.txt")),
						new NullInputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
