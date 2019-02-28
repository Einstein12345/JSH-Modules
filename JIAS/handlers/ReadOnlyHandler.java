package JIAS.handlers;

import java.io.InputStream;
import java.io.OutputStream;

import JIAS.Device;
import JIAS.DeviceHandler;

public class ReadOnlyHandler extends DeviceHandler{

	public ReadOnlyHandler(Device d) {
		super(d);
	}

	@Override
	public InputStream getInStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputStream getOutStream() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void write(byte[] b) {
		return;
	}

}
