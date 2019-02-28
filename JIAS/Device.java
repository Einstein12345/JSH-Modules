package JIAS;

import java.io.File;
import java.util.UUID;

import JIAS.handlers.DuplexHandler;
import JIAS.handlers.ReadOnlyHandler;
import JIAS.handlers.WriteOnlyHandler;

public class Device {

	public static final int TYPE_STEREO_OUT = 0, TYPE_STEREO_IN = 1, TYPE_MONO_OUT = 2, TYPE_MONO_IN = 3,
			TYPE_STEREO_DUPLEX = 4, TYPE_MONO_DUPLEX = 5;
	private File dev;
	private boolean usable = true, canRead, canWrite;
	private int type = 4;
	private UUID uid = UUID.randomUUID();
	private int sampleRate, bitDepth;
	private DeviceHandler wrap;

	// INIT Device Handler, use Dev as wrapper for access control
	public Device(module m, File dev, int type, int sampleRate, int bitDepth) {
		this.dev = dev;
		this.type = type;
		this.canRead = dev.canRead();
		this.canWrite = dev.canWrite();
		this.sampleRate = sampleRate;
		this.bitDepth = bitDepth;
		switch (type) {
		case 0:
			if (!dev.canWrite()) {
				usable = false;
				m.log("Attempt to register device failed, unable to write to file");
			} else {
				wrap = new WriteOnlyHandler(this);
			}
		case 1:
			if (!dev.canRead()) {
				usable = false;
				m.log("Attempt to register device failed, unable to read from file");
			} else {
				wrap = new ReadOnlyHandler(this);
			}
		case 2:
			if (!dev.canWrite()) {
				usable = false;
				m.log("Attempt to register device failed, unable to write to file");
			} else {
				wrap = new WriteOnlyHandler(this);
			}
		case 3:
			if (!dev.canRead()) {
				usable = false;
				m.log("Attempt to register device failed, unable to read from file");
			} else {
				wrap = new ReadOnlyHandler(this);
			}
		case 4:
			if (!dev.canRead() || !dev.canWrite()) {
				usable = false;
				m.log("Attempt to register duplex device failed, unable to read/write to file");
			} else {
				wrap = new DuplexHandler(this);
			}
		case 5:
			if (!dev.canRead() || !dev.canWrite()) {
				usable = false;
				m.log("Attempt to register duplex device failed, unable to read/write to file");
			} else {
				wrap = new DuplexHandler(this);
			}
		}
	}
	
	public DeviceHandler getDeviceHandler() {
		return wrap;
	}

	public File getFile() {
		return dev;
	}

	public boolean usable() {
		return usable;
	}

	public int type() {
		return type;
	}

	public UUID getUid() {
		return uid;
	}

	public boolean canRead() {
		return canRead;
	}

	public boolean canWrite() {
		return canWrite;
	}

	public int getSamplerate() {
		return sampleRate;
	}

	public int getBitDepth() {
		return bitDepth;
	}
}
