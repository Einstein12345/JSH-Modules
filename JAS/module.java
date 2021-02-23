package JAS;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;

import terra.shell.logging.Logger;
import terra.shell.modules.Module;
import terra.shell.modules.ModuleEvent.DummyEvent;

public class module extends Module {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2257304483186826792L;
	private boolean ok = true;
	private boolean cf = true;
	private Hashtable<String, String> config = new Hashtable<String, String>();
	private static Datasink cd; // Current Datasink
	private Datasink nil;
	private boolean stop = false;
	private static Hashtable<String, Datasink> rd = new Hashtable<String, Datasink>(); // Registered
	private JASD jasd; // Datasinks
	private static Logger slog;

	@Override
	public String getName() {
		return "JAS";
	}

	public boolean stop() {
		return stop;
	}

	@Override
	public void run() {
		getLogger().log("Populating /audio");
		File def = new File("/audio/DEF");
		cd = new Datasink(def);
		rd.put("DEF", cd);
		File nil = new File("/audio/NIL");
		this.nil = new Datasink(nil);
		rd.put("NIL", this.nil);
		try {
			def.createNewFile();
			nil.createNewFile();
		} catch (Exception e) {
			getLogger().log("Failed to create DEF and NIL datasinks! " + e.getMessage());
		}
		try {
			final BufferedOutputStream bout = new BufferedOutputStream(
					new FileOutputStream(nil));
			bout.write(0);
			bout.flush();
			bout.close();
		} catch (Exception e) {
			getLogger().log("Failed to populate NIL datasink!");
		}
		getLogger().log("Finished Populating /audio");
		jasd = new JASD(this);
		jasd.start();
	}

	@Override
	public String getVersion() {
		return "0.1";
	}

	public static String getV() {
		return "0.1";
	}

	@Override
	public String getAuthor() {
		return "D.S";
	}

	@Override
	public String getOrg() {
		return "T3RRA";
	}

	@Override
	public void onEnable() {
		slog = log;
		getLogger().log("Loading JAS");
		File ad = new File("/audio/");
		if (!ad.exists()) {
			try {
				ad.mkdir();
				getLogger().log("Created /audio/");
				getLogger().log("/audio/ will contain all audio datasinks");
			} catch (Exception e) {
				e.printStackTrace();
				getLogger().log("Failed to create /audio, FAILURE IN LAUNCH!");
				ok = false;
				return;
			}
		}
		File cd = new File("/config/JAS");
		if (!cd.exists()) {
			try {
				cd.mkdir();
				getLogger().log("Created config dir");
			} catch (Exception e) {
				e.printStackTrace();
				getLogger().log("Couldn't create config dir!");
			}
		}
		File conf = new File("/config/JAS/conf.conf");
		if (!conf.exists()) {
			getLogger().log("No config found, using defaults");
			cf = false;
		}
		if (cf) {
			try {
				Scanner sc = new Scanner(new FileInputStream(conf));
				while (sc.hasNext()) {
					final String tmp = sc.nextLine();
					String[] tm = tmp.split(":");
					if (tm.length == 2) {
						config.put(tm[0], tm[1]);
					} else {
						config.put(tm[0], "NA");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				cf = false;
				getLogger().log("Unable to read config, using defaults");
			}
		}
		getLogger().log("JAS Loaded!");
	}

	public Datasink getCurrent() {
		return cd;
	}

	public void changeCurrent(String datasink) {
		if (rd.containsKey(datasink))
			cd = rd.get(datasink);
	}

	public static Set<String> getRD() {
		return rd.keySet();
	}

	public static Datasink getSink(String name) {
		return rd.get(name);
	}

	public static void registerSink(String name) {
		if (!rd.containsKey(name)) {
			name.replace("\\.", "_");
			final File tmp = new File("/audio/" + name);
			try {
				tmp.createNewFile();
				rd.put((String) name, new Datasink(tmp));
				sgetLogger().log("Registered " + name);
			} catch (Exception e) {
				sgetLogger().log("Failed to create audio datasink " + name);
			}
		}
	}

	public static void deregisterSink(String name) {
		if (rd.containsKey(name)) {
			try {
				name.replace("\\.", "_");
				if (rd.get(name) != cd) {
					rd.remove(name);
					final File tmp = new File(name);
					if (tmp.exists()) {
						tmp.delete();
					}
					sgetLogger().log("Removed " + name);
				} else {
					sgetLogger().log("Unable to remove current datasink!");
				}
			} catch (Exception e) {
				sgetLogger().log("Failed to remove audio datasink " + name);
			}
		}
	}

	@Override
	public void trigger(DummyEvent event) {
		final Object[] args = event.getME().getArgs();
		if (args.length > 0) {
			if (args[0].equals("DS") && args.length > 1) {
				if (rd.containsKey(args[1])) {
					cd = rd.get(args[1]);
				} else {
					getLogger().log(args[1] + " is not a registered datasink");
				}
			}
			if (args[0].equals("PA")) {
				jasd.pause();
				getLogger().log("Toggling pause");
			}
			if (args[0].equals("RD") && args.length > 1) {
				registerSink(args[1] + "");
			}
			if (args[0].equals("DR") && args.length > 1) {
				deregisterSink(args[1] + "");
			}
		}
	}
}
