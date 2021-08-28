package UNFS;

import java.net.Inet4Address;
import java.net.InetAddress;

import terra.shell.utils.JProcess;
import terra.shell.utils.ReturnValue;
import terra.shell.utils.system.ByteClassLoader.Replaceable;

@Replaceable(replaceable = false)
public class DirectoryScoutReturnValue extends ReturnValue {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6149621023513794652L;
	private final String[] paths;
	private Inet4Address remIp;

	public DirectoryScoutReturnValue(JProcess p, String[] paths) {
		super(p);
		this.paths = paths;
		try {
			remIp = (Inet4Address) Inet4Address.getLocalHost();
		} catch (Exception e) {
			remIp = (Inet4Address) InetAddress.getLoopbackAddress();
		}
	}

	public Inet4Address getRemoteIp() {
		return remIp;
	}

	@Override
	public boolean setValues(Object... values) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object[] getReturnValue() {
		// TODO Auto-generated method stub
		return paths;
	}

	/**
	 * @deprecated Use {@link #setValues(Object...)} instead
	 */
	@Override
	public boolean processReturn(Object... values) {
		return setValues(values);
	}

}
