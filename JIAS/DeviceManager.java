package JIAS;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.Timer;

public class DeviceManager {
	private Hashtable<Integer, Device> devices = new Hashtable<Integer, Device>();
	private Hashtable<Integer, ArrayList<Device>> devByType = new Hashtable<Integer, ArrayList<Device>>();
	private Hashtable<Integer, DeviceHandler> devHandle = new Hashtable<Integer, DeviceHandler>();

	// TODO register devices by type into devByType list
	public DeviceManager() {
		// Populate devByType
		devByType.put(0, new ArrayList<Device>());
		devByType.put(1, new ArrayList<Device>());
		devByType.put(2, new ArrayList<Device>());
		devByType.put(3, new ArrayList<Device>());
		devByType.put(4, new ArrayList<Device>());
		devByType.put(5, new ArrayList<Device>());

	}

	public boolean registerDevice(Device d) {
		if (!d.getFile().exists()) {
			return false;
		}
		if (!d.usable()) {
			return false;
		}
		devices.put(devices.size(), d);
		devByType.get(d.type()).add(d);
		return true;
	}

	public boolean registerType(int t, DeviceHandler dev) {
		if (devByType.containsKey(t) || devHandle.containsKey(t)) {
			return false;
		}
		devByType.put(t, new ArrayList<Device>());
		devHandle.put(t, dev);
		return true;
	}

	public class StereoDuplexHandler extends DeviceHandler {
		private int sampleRate = 44100;

		public StereoDuplexHandler(Device d) throws Exception {
			super(d);
			if (!(d.type() == Device.TYPE_STEREO_DUPLEX)) {
				return;
			}
			this.d = d;
			_registerInOut();
			_setBitDepth();
		}

		@Override
		public void write(byte[] b) {
			if (d == null)
				return;
			for (int i = 0; i < b.length; i++) {
				buf[i] = b[i];
			}
			bufIt++;
			// TODO Add b to a byte[] buffer that matches sample rate
		}

		@Override
		public byte read() {
			if (d == null)
				return -1;
			return 0;
		}

		@Override
		public InputStream getInStream() {
			if (d == null)
				return null;
			return null;
		}

		@Override
		public OutputStream getOutStream() {
			if (d == null) {
				return null;
			}
			return null;
		}

		private void _registerInOut() throws Exception {
			bout = new BufferedOutputStream(new FileOutputStream(d.getFile()));
			bin = new BufferedInputStream(new FileInputStream(d.getFile()));
		}

		private void _setBitDepth() {
			// TODO Use OSS API to determine bitdepth of exact device
			buf = new byte[sampleRate * bitDepth];
		}

		private void write() {
			write(buf);
			buf = new byte[sampleRate * bitDepth];
		}

		private void _startDaemon() {
			Timer t = new Timer(bufIt, null);
			t.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Write next byte to output device.
					try {
						write();
						// buf[0] = buf[1];// Move buffer left
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			t.setDelay(1000);
			t.start();
		}
	}

}
