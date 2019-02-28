package JIAS;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class DeviceHandler {
	protected byte[] buf;
	protected int bufIt = 0;
	protected Device d = null;
	private String path;
	
	private File dev;
	
	protected int sampleRate, bitDepth;
	protected BufferedOutputStream bout;
	protected BufferedInputStream bin;

	public DeviceHandler(Device d) {
		this.d = d;
		path = d.getFile().getAbsolutePath();
		dev = new File(path);
		sampleRate = d.getSamplerate();
		bitDepth = d.getBitDepth();
		buf = new byte[sampleRate * bitDepth];
	}

	public void write(byte[] b) {
		if (dev.exists() && d.canWrite()) {
			_write(b);
		}
	}
	
	public void write(byte b) {
		if(dev.exists() && d.canWrite()) {
			_write(b);
		}
	}

	public byte read() {
		if (dev.exists() && d.canRead()) {
			return _read();
		}
		return 0;
	}
	
	public byte[] readMult(int size) {
		return _readSome(size);
	}

	public abstract InputStream getInStream();

	public abstract OutputStream getOutStream();

	private native void _write(byte b); // Write appropriate
										// C++ files to
										// match up,
										// generate Header
										// file
	// Create timer synced with Sample Rate that triggers '_wrte(buf)' after
	// flushing 'bout' into 'buf'.

	private native void _write(byte[] b);

	private native byte _read();

	private native byte[] _readSome(int amount);
}
