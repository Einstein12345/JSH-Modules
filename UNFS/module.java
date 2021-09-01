package UNFS;

import java.util.HashMap;

import UNFS.events.ListDirectoryEvent;
import terra.shell.modules.Module;
import terra.shell.modules.ModuleEvent.DummyEvent;

public class module extends Module {

	private DirectoryScout ds;
	private static HashMap<String, UNFSFileDescriptor> remoteIpZFSPaths = new HashMap<String, UNFSFileDescriptor>();

	@Override
	public String getName() {
		return "UNFS";
	}

	public static void updateRemotePathing(UNFSFileDescriptor[] paths) {
		for (UNFSFileDescriptor desc : paths) {
			remoteIpZFSPaths.put(desc.getFilePath(), desc);
		}
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
		ds = new DirectoryScout();
		ds.start();
	}

	@Override
	public void trigger(DummyEvent event) {
		if (event.getME() instanceof ListDirectoryEvent) {
			ListDirectoryEvent lde = (ListDirectoryEvent) event.getME();
			Object[] args = lde.getArgs();
			if (args.length == 1) {
				String path = args[0].toString();
			}
		}
	}

}
