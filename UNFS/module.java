package UNFS;

import java.net.Inet4Address;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import UNFS.events.ListDirectoryEvent;
import terra.shell.modules.Module;
import terra.shell.modules.ModuleEvent.DummyEvent;

public class module extends Module {

	private DirectoryScout ds;
	private static HashMap<Inet4Address, HashSet<String>> remoteIpZFSPaths = new HashMap<Inet4Address, HashSet<String>>();

	@Override
	public String getName() {
		return "UNFS";
	}

	public static void updateRemotePathing(Inet4Address ip, String[] paths) {
		remoteIpZFSPaths.put(ip, (HashSet<String>) Set.of(paths));
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
			if(args.length == 1) {
				String path = args[0].toString();
			}
		}
	}

}
