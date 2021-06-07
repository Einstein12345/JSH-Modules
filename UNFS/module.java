package UNFS;

import terra.shell.modules.Module;
import terra.shell.modules.ModuleEvent.DummyEvent;

public class module extends Module {

	@Override
	public String getName() {
		return "UNFS";
	}

	@Override
	public void run() {
	}

	@Override
	public String getVersion() {
		return "inDev 0";
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "T3RRA";
	}

	@Override
	public String getOrg() {
		// TODO Auto-generated method stub
		return "T3RRA";
	}

	@Override
	public void onEnable() {
		log.log("Enabled UNFS (Unified Networked File System)");
		log.log("Scanning local directory structure in subprocess");
		DirectoryScout ds = new DirectoryScout();
		ds.start();
	}

	@Override
	public void trigger(DummyEvent event) {
	}

}
