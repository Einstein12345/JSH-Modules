package SBS;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import terra.shell.modules.Module;
import terra.shell.modules.ModuleEvent.DummyEvent;

public class module extends Module {
	/**
	 * 
	 */
	private static final long serialVersionUID = 70770338315304184L;
	Hashtable<String, String> conf = new Hashtable<String, String>();
	ArrayList<String> dlist = new ArrayList<String>();
	boolean ok;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "SBS";
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
		getLogger().log("Enabling SBS FS");
		getLogger().log("Reading Configuration");
		File conf = new File("/config/SBS/def.conf");
		File list = new File("/config/SBS/SBS.list");
		if (conf.exists()) {
			try {
				final Scanner sc = new Scanner(new FileInputStream(conf));
				while (sc.hasNext()) {
					final String tmp = sc.nextLine();
					final String[] tmpa = tmp.split(":");
					if (tmpa.length > 2) {
						this.conf.put(tmpa[0], tmpa[1]);
						getLogger().log("Loaded Configuration Variable: " + tmpa[0]
								+ " = " + tmpa[1]);
					}
				}
				sc.close();
			} catch (Exception e) {
				e.printStackTrace();
				getLogger().log("Unable to read conf file!");
				ok = false;
			}
		} else {
			getLogger().log("/config/SBS/def.conf not found, loading defaults!");
			// Load Defaults
		}
		if (list.exists()) {
			getLogger().log("Loading SBS drives from /config/SBS/SBS.list..");
			try {
				final Scanner sc = new Scanner(new FileInputStream(list));
				while (sc.hasNext()) {
					final String tmp = sc.nextLine();
					dlist.add(tmp);
					getLogger().log("Added unchecked SBS drive, " + tmp);
				}
				sc.close();
			} catch (Exception e) {
				e.printStackTrace();
				getLogger().log("Unable to read list file! No SBS drives loaded!");
			}
		}
	}

	@Override
	public void trigger(DummyEvent event) {
		Object[] args = event.getME().getArgs();
		if (args.length > 0) {
			if (args[0].equals("scan")) {
				scanSBS();
			}
			if (args[0].equals("list")) {
				listSBS();
			}
		}
	}

	private void scanSBS() {
		getLogger().log("Scanning SBS drives...");
		getLogger().log("Please wait, this may take a moment...");
		for(int i = 0; i < dlist.size(); i ++){
			
		}
	}

	private void listSBS() {
		getLogger().log("Available SBS drives: ");
		int drives = 0;
		for (int i = 0; i < dlist.size(); i++) {
			getLogger().log(i + ": " + dlist.get(i));
			drives++;
		}
		getLogger().log("Total " + drives + " drives listed");
	}

}
