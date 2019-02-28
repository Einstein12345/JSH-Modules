package JIAS.handlers;

import java.io.InputStream;
import java.io.OutputStream;

import JIAS.Device;
import JIAS.DeviceHandler;

public class DuplexHandler extends DeviceHandler{

	public DuplexHandler(Device d) {
		super(d);
	}

	@Override
	public InputStream getInStream() {
		return null;
	}

	@Override
	public OutputStream getOutStream() {
		// TODO Auto-generated method stub
		return null;
	}

}
