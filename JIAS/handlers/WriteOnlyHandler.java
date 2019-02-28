package JIAS.handlers;

import java.io.InputStream;
import java.io.OutputStream;

import JIAS.Device;
import JIAS.DeviceHandler;

public class WriteOnlyHandler extends DeviceHandler{

	public WriteOnlyHandler(Device d) {
		super(d);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public byte read() {
		return 0;
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

}
