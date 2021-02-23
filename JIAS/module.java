package JIAS;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

import terra.shell.config.Configuration;
import terra.shell.launch.Launch;
import terra.shell.modules.Module;
import terra.shell.modules.ModuleEvent;
import terra.shell.modules.ModuleEvent.DummyEvent;

public class module extends Module {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8594702227365158599L;
	private boolean isgood = true;
	private boolean canwrite = true, canread = true;
	private Hashtable<UUID, Device> devices = new Hashtable<UUID, Device>();
	private ArrayList<Device> readOnly = new ArrayList<Device>();
	private ArrayList<Device> writeOnly = new ArrayList<Device>();
	private ArrayList<Device> duplex = new ArrayList<Device>();
	private int sampleRate = 44100;

	@Override
	public String getName() {
		return "JIAS";
	}

	public void log(String msg) {
		getLogger().log("--" + msg);
	}

	@Override
	public void run() {

	}

	@Override
	public String getVersion() {
		return null;
	}

	@Override
	public String getAuthor() {
		return "DS";
	}

	@Override
	public String getOrg() {
		return null;
	}

	@Override
	public void onEnable() {
		
		System.loadLibrary("JIAS");

		Configuration conf = Launch.getConfig("JIAS");
		if (conf == null) {
			conf = new Configuration(new File(Launch.getConfD().getPath(), "JIAS"));
			conf.setValue("dspFile", "/dev/dsp");
			conf.setValue("sampleRate", 44100);
		}
		sampleRate = Integer.parseInt((String) conf.getValue("sampleRate"));
		// Hook into OSS
		File dsp = new File((String) conf.getValue("dspFile"));
		if (!dsp.exists()) {
			isgood = false;
			getLogger().log("Unable to start Audio System, " + dsp.getPath() + " not found");
			return;
		}
		if (!dsp.canWrite()) {
			getLogger().log("Unable to write to " + dsp.getPath() + ", Audio Output will not be available!");
			canwrite = false;
		}
		if (!dsp.canRead()) {
			getLogger().log("Unable to read from " + dsp.getPath() + ", Audio Input will not be available!");
			canread = false;
		}

		if (!canread && !canwrite) {
			isgood = false;
			getLogger().log("Cannot read or write to /dev/dsp");
			return;
		}
		// Read only
		if (canread && !canwrite) {
			Device d = new Device(this, dsp, Device.TYPE_STEREO_IN, 44100, 16);
			devices.put(d.getUid(), d);
			readOnly.add(d);
		}
		// Write only
		if (!canread && canwrite) {
			Device d = new Device(this, dsp, Device.TYPE_STEREO_OUT, 44100, 16);
			devices.put(d.getUid(), d);
			writeOnly.add(d);
		}
		// RW
		if (canread && canwrite) {
			Device d = new Device(this, dsp, Device.TYPE_STEREO_DUPLEX, 44100, 16);
			devices.put(d.getUid(), d);
			duplex.add(d);
		}
	}

	@Override
	public void trigger(DummyEvent event) {
		Object o = event.getME().getArgs()[0];
		if (o instanceof String) {
			String arg = (String) o;
			if (arg.equals("testTone")) {
				getLogger().log("SoundTest");
				int[] tone = genTestTone(100);
				for (int i = 0; i < tone.length; i++) {
					for (int j = 0; j < writeOnly.size(); j++) {
						// Write 'tone' to device;
						writeOnly.get(j).getDeviceHandler().write((byte) tone[i]);
					}
				}
			}
			if (arg.equals("addDevice") && event.getME().getArgs().length >= 3) {
				// 1 is device file
				// 2 is type
				Object dFobj = event.getME().getArgs()[1];
				String dF;
				Object typeObj = event.getME().getArgs()[2];
				int type;

				if (dFobj instanceof String) {
					dF = (String) dFobj;
				}
				if (typeObj instanceof Integer) {
					type = (int) typeObj;
				}
			}
		}

	}

	public int[] genTestTone(int numSamps) {
		int[] ints = new int[numSamps];
		for (int i = 0; i < numSamps; i++) {
			double time = i / (44100);
			double freq = 440;
			double sinValue = ((Math.sin(2 * Math.PI * freq * time)) + (Math.sin(2 * Math.PI * (freq / 1.8) * time))
					+ (Math.sin(2 * Math.PI * (freq / 1.5) * time))) / 3.0;
			ints[i] = (int) (16000 * sinValue);
		}
		return ints;
	}

}
